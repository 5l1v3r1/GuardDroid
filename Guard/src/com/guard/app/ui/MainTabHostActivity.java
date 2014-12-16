package com.guard.app.ui;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Window;

import com.guard.app.R;

public class MainTabHostActivity extends FragmentActivity{

	private FragmentTabHost mTabHost;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main_layout);	
		mTabHost = (FragmentTabHost)findViewById(android.R.id.tabhost);
		mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
		mTabHost.addTab(mTabHost.newTabSpec("tab1").setIndicator("���"),SoftwareFragment.class,null);
		mTabHost.addTab(mTabHost.newTabSpec("tab2").setIndicator("��־"),SoftwareFragment.class,null);
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("Ȩ��"),SoftwareFragment.class,null);
		mTabHost.addTab(mTabHost.newTabSpec("tab3").setIndicator("����"),SoftwareFragment.class,null);     
		}
	

}
