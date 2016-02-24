package com.baoluo.community.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
/**
 * 后台服务，用于随时接收消息
 * @author Ryan_Fu
 *  2015-5-5
 */
public class BuLuoCommunityService extends Service{
	
	public static final String TAG = "BuLuoCommunityService";
	
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	@Override
	public void onCreate() {
		Log.i(TAG, "BuLuoCommunityService-onCreate");
		
		super.onCreate();
	}
	
	
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "BuLuoCommunityService-onStartCommand");  
        /*  
         * 这里返回状态有三个值，分别是:  
         * 1、START_STICKY：当服务进程在运行时被杀死，系统将会把它置为started状态，但是不保存其传递的Intent对象，之后，系统会尝试重新创建服务;  
         * 2、START_NOT_STICKY：当服务进程在运行时被杀死，并且没有新的Intent对象传递过来的话，系统将会把它置为started状态，  
         *   但是系统不会重新创建服务，直到startService(Intent intent)方法再次被调用;  
         * 3、START_REDELIVER_INTENT：当服务进程在运行时被杀死，它将会在隔一段时间后自动创建，并且最后一个传递的Intent对象将会再次传递过来。  
         */  
		return super.onStartCommand(intent, flags, startId);
	}
	
	@Override
	public void onDestroy() {
		Log.i(TAG, "BuLuoCommunityService-onDestroy");
		super.onDestroy();
	}
}
