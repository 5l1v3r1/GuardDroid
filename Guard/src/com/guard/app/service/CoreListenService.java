package com.guard.app.service;


import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;

import android.Manifest.permission;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Credentials;
import android.net.LocalServerSocket;
import android.net.LocalSocket;
import android.os.IBinder;
import android.util.Log;
import com.guard.app.R;
import com.guard.app.db.LogDBHelper;
import com.guard.app.main.GuardApplication;
import com.guard.app.ui.TestActivity;
import com.guard.app.utils.LogStruct;
import com.guard.app.utils.PolicyMap;
import com.guard.app.utils.PolicyResult;
import com.guard.app.utils.PolicyUtil;

public class CoreListenService extends Service{
	
	private static final String TAG = "CoreListenService";
	private ServerThread server;
	private Thread serverThread;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onStart(Intent intent, int startId) {
		Log.d(TAG, "onStart");
		server = new ServerThread(CoreListenService.this);
		serverThread = new Thread(server);
		serverThread.start();
		super.onStart(intent, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.d(TAG, "onDestroy");
		super.onDestroy();
	}

	//��ʾ֪ͨ�����ݿ��¼��־�߳�
	class LogThread implements Runnable {

		private LogStruct logStruct;
		private Context context;
		
		public LogThread(Context context, LogStruct logStruct){
			Log.d("TAG", "LogThread");
			this.context = context;
			this.logStruct = logStruct;
		}
		
		//������Ϊ֪ͨ
		private void LogNotification() {
			NotificationManager nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
	        Notification n = new Notification();        
	        int icon = n.icon = R.drawable.ic_launcher; 
	        String action = null;
	        if (logStruct.action == PolicyResult.ALLOWED) action = "����";
	        else if (logStruct.action == PolicyResult.FORBIDDEN) action = "����";
	        String tickerText = logStruct.packageName +" "+PolicyMap.PERMISSION_MAP.get(logStruct.permission[0])+" ";
	        if (logStruct.permission[0].equals("android.permission.CALL_PHONE")) {
				tickerText += logStruct.extras[0];
			} else if (logStruct.permission[0].equals("android.permission.SEND_SMS")) {
				tickerText += logStruct.extras[0]+" "+"����:"+logStruct.extras[1];
			} else if (logStruct.permission[0].equals("android.permission.INTERNET")) {
				tickerText += logStruct.extras[0];
			}
	        tickerText += action;
	        long when = System.currentTimeMillis();
	        //����֪ͨ����
	        n.icon = icon;
	        n.tickerText = tickerText;	        
	        n.when = when;
	        logStruct.time = when;
	        //���֪ͨ����ת�����ò��Ե�activity
	        Intent intent = new Intent(CoreListenService.this, TestActivity.class); 
	        //����������ת��activity
	        PendingIntent pi = PendingIntent.getActivity(CoreListenService.this, 0, intent, 0); 
	        //�����¼���Ϣ
	        n.setLatestEventInfo(getApplicationContext(), "Guard", tickerText, pi); 
	        //����֪ͨ
	        nm.notify(1, n);
		}
		
		@Override
		public void run() {
			LogDBHelper logDBHelper = new LogDBHelper(context);
			ContentValues values = new ContentValues();
			LogNotification();
			values.put("Pkg", logStruct.packageName);
			values.put("Title", PolicyMap.PERMISSION_MAP.get(logStruct.permission[0]));
			if (logStruct.extras != null) {
				if (logStruct.extras.length == 2) {
					values.put("Content", logStruct.extras[0]+" "+logStruct.extras[1]);
				} else values.put("Content", logStruct.extras[0]);				
			}			
			values.put("Time", logStruct.time);
			values.put("Action", logStruct.action);
			logDBHelper.insert(values);
			Log.d(TAG, "db insert success");
			logDBHelper.close();
		}
		
	}

	//�������������߳�
	class ServerThread implements Runnable {

		private String result = null;
		private LocalServerSocket server = null;
		private InputStream is = null;
		private PrintWriter os = null;
		private LocalSocket connect = null;
		private Context context;
		
		public ServerThread(String mString, Context context) {
			result = mString;
			this.context = context;
		}
		
		public ServerThread(Context context) {
			this.context = context;
		}

		//���в��Դ���
		private int handlePolicy(LocalSocket connect, String packageName) {
			int policyResult = PolicyResult.ALLOWED;
			try {
				PolicyUtil policyUtil = new PolicyUtil();
				LogStruct logStruct = new LogStruct();
				byte[] arrayOfByte = new byte[2048];
				is = connect.getInputStream();
				is.read(arrayOfByte);
				policyUtil.buffer.put(arrayOfByte);
				policyUtil.buffer.position(0);
				//��ȡ�û���Ϊ
 				logStruct.packageName = packageName;
 				logStruct.permission = policyUtil.getStringArray();
 				logStruct.extras = policyUtil.getStringArray();
 				//���Կ���ѯ��̸�Ի���
 				policyResult = GuardApplication.policyMap.get(packageName) & PolicyMap.PERMISSION_STATE_MAP.get(logStruct.permission[0]);
 				if (policyResult != 0) policyResult = PolicyResult.ALLOWED;
 				else policyResult = PolicyResult.FORBIDDEN;
 				logStruct.action = policyResult;
 				//������־��¼�߳�
 				LogThread log = new LogThread(context, logStruct);
 				Thread logThread = new Thread(log);
 				logThread.start();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return policyResult;
		}
		
		@Override
		public void run() {
			try {
				server = new LocalServerSocket("com.repackaging.localsocket");		
				PackageManager pm = getPackageManager();
				while (true) {
					connect = server.accept();
					os = new PrintWriter(connect.getOutputStream());
					Credentials cre = connect.getPeerCredentials();
					Log.d(TAG,"accept socket uid:"+cre.getUid());
					Log.d(TAG,pm.getNameForUid(cre.getUid()));
					int policyResult = handlePolicy(connect,pm.getNameForUid(cre.getUid()));	
					os.println(String.valueOf(policyResult));
					os.flush();
					Log.d(TAG,"send "+String.valueOf(policyResult));
				}	
			} catch (IOException e) {
				e.printStackTrace();
			}
			finally{
				try {
					os.close();
					is.close();
					connect.close();
					server.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}	
	}
}
