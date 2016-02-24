package com.baoluo.community.ui;

import android.location.Location;
import android.os.Bundle;
import android.os.Handler;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.location.LocationManagerProxy;
import com.amap.api.location.LocationProviderProxy;
import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.util.L;

/**
 * 需要定位
 * @author tao.lai
 *
 */
public class AtyBaseLBS extends AtyBase implements AMapLocationListener,Runnable{
	private static final String TAG = "AtyBaseLBS";

	private LocationManagerProxy aMapLocManager = null;
	protected AMapLocation aMapLocation;
	private Handler LBShandler = new Handler();
	private LocationResultListener locationResultListener;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		awakenLoction();   //控制定位开关
	}
	
	/**
	 * 唤起定位
	 */
	protected void awakenLoction() {
		aMapLocManager = LocationManagerProxy.getInstance(this);
		aMapLocManager.setGpsEnable(true);
		aMapLocManager.requestLocationData(LocationProviderProxy.AMapNetwork, -1, 100, this);
		LBShandler.postDelayed(this, 12*1000); 
	}
	
	protected LatLng getLatLng() {
		if(this.aMapLocation != null){
			return new LatLng(this.aMapLocation.getLatitude(),this.aMapLocation.getLongitude());
		}
		return null;
	}
	
	/**
	 * 获取定位描述
	 * @return
	 */
	protected String getLocationDesc() {
		if(this.aMapLocation != null){
			Bundle bundle = this.aMapLocation.getExtras();
			if(bundle != null){
				return bundle.getString("desc");
			}
			return this.aMapLocation.getAddress();
		}
		return null;
	}
	
	/**
	 * 混合定位回调函数
	 */
	@Override
	public void onLocationChanged(AMapLocation location) {
		if(location != null){
			this.aMapLocation = location;
			locationResultListener.locationChanged();
			L.i(TAG, "定位成功");
		}
	}
	
	@Override
	public void run() {
		if (aMapLocation == null) {
			L.i(TAG, "12秒内还没有定位成功，停止定位");
			stopLocation();
		}
	}
	
	@Override
	protected void onPause() {
		super.onPause();
//		stopLocation();
	}

	/**
	 * 销毁定位
	 */
	private void stopLocation() {
		if (aMapLocManager != null) {
			aMapLocManager.removeUpdates(this);
			aMapLocManager.destroy();
			locationResultListener.locationFail();
		}
		aMapLocManager = null;
	}
	
	protected void setLocationResultListener(LocationResultListener listener) {
		this.locationResultListener = listener;
	}
	
	/**
	 * 定位结果回调
	 * @author tao.lai
	 *
	 */
	public interface LocationResultListener{
		/**
		 * 定位成功
		 */
		void locationChanged();
		
		/**
		 * 定位失败
		 */
		 void locationFail();
	}
	
	/*@Override
	public void onLocationChanged(AMapLocation location) {
		if (location != null) {
			this.aMapLocation = location;// 判断超时机制
			Double geoLat = location.getLatitude();
			Double geoLng = location.getLongitude();
			String cityCode = "";
			String desc = "";
			Bundle locBundle = location.getExtras();
			if (locBundle != null) {
				cityCode = locBundle.getString("citycode");
				desc = locBundle.getString("desc");
			}
			String str = ("定位成功:(" + geoLng + "," + geoLat + ")"
					+ "\n精    度    :" + location.getAccuracy() + "米"
					+ "\n定位方式:" + location.getProvider() + "\n定位时间:"
					+ TimeUtil.convertToTime(location.getTime()) + "\n城市编码:"
					+ cityCode + "\n位置描述:" + desc + "\n省:"
					+ location.getProvince() + "\n市:" + location.getCity()
					+ "\n区(县):" + location.getDistrict() + "\n区域编码:" + location
					.getAdCode());
			LatLng start = new LatLng(geoLat,geoLng);
			LatLng end = new LatLng(22.56907917,113.87063026);
			float distance = AMapUtils.calculateLineDistance(start,end);
			str += "\n距离："+distance;
			L.i(TAG, str);
		}
	}*/
	
	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
	}

	@Override
	public void onProviderEnabled(String provider) {
	}

	@Override
	public void onProviderDisabled(String provider) {
	}

	@Override
	public void onLocationChanged(Location location) {
	}
}
