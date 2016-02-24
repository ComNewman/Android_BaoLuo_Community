package com.baoluo.community.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.widget.Toast;
/**
 * 判断手机网络连接状态
 * @author Ryan_Fu
 *  2015-5-5
 */
public class NetUtil {
	
	private static final String TAG = "NetUtil";
	public static final int NETWORN_NONE = 0;
	public static final int NETWORN_WIFI = 1;
	public static final int NETWORN_MOBILE = 2;
	/** 
	 * 网络连接是否可用 
	 * @param context 上下文对象
	 * @return
	 */
    public static boolean isConnnected(Context context) {  
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);  
        if (null != connectivityManager) {  
            NetworkInfo networkInfo[] = connectivityManager.getAllNetworkInfo();  
  
            if (null != networkInfo) {  
                for (NetworkInfo info : networkInfo) {  
                    if (info.getState() == NetworkInfo.State.CONNECTED) {  
                        L.i(TAG, "the net is ok");  
                        return true;  
                    }  
                }  
            }  
        }  
        Toast.makeText(context, "网络连接失败,请检查本机联网状况", Toast.LENGTH_SHORT).show();  
        return false;  
    }  
	/**
	 * 获取网络状态
	 * @param context
	 * @return	int 0代表手机没有联网，1代表WIFI，2代表3G网络连接
	 */
	public static int getNetworkState(Context context) {
		ConnectivityManager connManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		// Wifi
		State state = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_WIFI;
		}

		// 3G
		state = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
				.getState();
		if (state == State.CONNECTED || state == State.CONNECTING) {
			return NETWORN_MOBILE;
		}
		return NETWORN_NONE;
	}
}