package com.baoluo.community.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;

import com.baoluo.community.util.L;
import com.baoluo.im.MqttHelper;

public class BackService extends Service{
	private static final String TAG = "BackService";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		L.i(TAG, TAG + "onCreate");
		super.onCreate();
	}
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		L.i(TAG, TAG + "onStartCommand");
	    checkMqtt.postDelayed(runnable, CHECK_INTERVAL);
		return super.onStartCommand(intent, flags, startId);
	}

	@Override
	public void onDestroy() {
		L.i(TAG, TAG + "onDestroy");
		super.onDestroy();
		this.startService(new Intent("com.baoluo.community.service.BackService"));
	}
	
	private static int CHECK_INTERVAL = 1 * 60 * 1000;  //  1min
 	Handler checkMqtt = new Handler();
	Runnable runnable = new Runnable() {
        @Override  
        public void run() {
        	MqttHelper.getInstance().checkConnection();
        	checkMqtt.postDelayed(this, CHECK_INTERVAL);
        }
    };
}
