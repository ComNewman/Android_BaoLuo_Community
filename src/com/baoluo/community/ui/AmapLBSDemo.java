package com.baoluo.community.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;

/**
 * gps,network混合定位demo
 * @author tao.lai
 *
 */
public class AmapLBSDemo extends AtyBaseLBS implements LocationResultListener{
	
	private static final String TAG = "AmapLBSDemo";

	private TextView myLocation;
	private Button btn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_amp_lbs_demo);
		
		myLocation = (TextView) findViewById(R.id.myLocation);
		btn = (Button) findViewById(R.id.btn_mybtn);
		
		setLocationResultListener(this);
		
		btn.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				myLocation.setText("定位中...");
				awakenLoction();
			}
		});
	}
	
	@Override
	public void locationChanged() {
		myLocation.setText("adress = "+getLocationDesc()+
				"\n:("+getLatLng().longitude+","+getLatLng().latitude+")"
				+"定位方式:"+aMapLocation.getProvider()+"\n adress="+aMapLocation.getAddress()
				+"\n road="+aMapLocation.getRoad()+"\n street="+aMapLocation.getStreet());
	}
	@Override
	public void locationFail() {
		myLocation.setText("定位失败,请重试");
	}
	
}
