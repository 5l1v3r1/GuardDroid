package com.guard.app.utils;

import java.util.HashMap;

public class PolicyMap {

	public static final HashMap<String, String>  PERMISSION_MAP = new HashMap<String, String>();
	public static final HashMap<String, Integer> PERMISSION_STATE_MAP = new HashMap<String, Integer>();
	/*��32λ��ʾ��Ӧѯ��״̬����λѯ�ʣ���λĬ�ϣ���32λ��ʾȨ��״̬����λ������λ����*/	
	public static final int READ_SMS = 					1;
	public static final int SEND_SMS = 					2;
	public static final int RECEIVE_SMS = 				4;
	public static final int CALL_PHONE = 				8;
	public static final int READ_PHONE_STATE = 			16;
	public static final int READ_CONTACTS = 			32;
	public static final int INTERNET = 					64;
	public static final int ACCESS_NETWORK_STATE = 		128;
	public static final int RECORD_AUDIO = 				256;
	public static final int ACCESS_FINE_LOCATION = 		512;
	public static final int CAMERA = 					1024;
	public static final int RECEIVE_BOOT_COMPLETED = 	2048;
	
	static{
		PERMISSION_MAP.put("android.permission.READ_SMS",				"��ȡ����Ϣ����");
		PERMISSION_MAP.put("android.permission.SEND_SMS", 				"���Ͷ���Ϣ����");
		PERMISSION_MAP.put("android.permission.RECEIVE_SMS", 			"���ض���Ϣ����");
		PERMISSION_MAP.put("android.permission.CALL_PHONE",				"����绰");
		PERMISSION_MAP.put("android.permission.READ_PHONE_STATE", 		"��ȡ�绰״̬");
		PERMISSION_MAP.put("android.permission.READ_CONTACTS", 			"��ȡ��ϵ������");
		PERMISSION_MAP.put("android.permission.INTERNET", 				"��������");
		PERMISSION_MAP.put("android.permission.ACCESS_NETWORK_STATE", 	"��ȡ����״̬");	
		PERMISSION_MAP.put("android.permission.RECORD_AUDIO", 			"����¼������");
		PERMISSION_MAP.put("android.permission.ACCESS_FINE_LOCATION",	"��ȡ����ǰλ��");
		PERMISSION_MAP.put("android.permission.CAMERA", 				"��������ͷ");		
	}
	static{
		PERMISSION_STATE_MAP.put("android.permission.READ_SMS",Integer.valueOf(READ_SMS));
		PERMISSION_STATE_MAP.put("android.permission.SEND_SMS",Integer.valueOf(SEND_SMS));
		PERMISSION_STATE_MAP.put("android.permission.RECEIVE_SMS",Integer.valueOf(RECEIVE_SMS));
		PERMISSION_STATE_MAP.put("android.permission.CALL_PHONE",Integer.valueOf(CALL_PHONE));
		PERMISSION_STATE_MAP.put("android.permission.READ_PHONE_STATE",Integer.valueOf(READ_PHONE_STATE));
		PERMISSION_STATE_MAP.put("android.permission.READ_CONTACTS",Integer.valueOf(READ_CONTACTS));
		PERMISSION_STATE_MAP.put("android.permission.INTERNET",Integer.valueOf(INTERNET));
		PERMISSION_STATE_MAP.put("android.permission.ACCESS_NETWORK_STATE",Integer.valueOf(ACCESS_NETWORK_STATE));	
		PERMISSION_STATE_MAP.put("android.permission.RECORD_AUDIO",Integer.valueOf(RECORD_AUDIO));
		PERMISSION_STATE_MAP.put("android.permission.ACCESS_FINE_LOCATION",Integer.valueOf(ACCESS_FINE_LOCATION));
		PERMISSION_STATE_MAP.put("android.permission.CAMERA",Integer.valueOf(CAMERA));
		PERMISSION_STATE_MAP.put("android.permission.RECEIVE_BOOT_COMPLETED",Integer.valueOf(RECEIVE_BOOT_COMPLETED));	
	}



}
