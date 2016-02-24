package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 创建位置
 * 
 * @author tao.lai
 * 
 */
public class AtyCreatePosition extends AtyBase implements OnClickListener {
	private static final String TAG = "AtyCreatePosition";

	private AMap aMap;
	private MapView mapView;

	private Button btnSure, btnBack;
	private EditText etPlace;

	private String placeStr;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_create_position);
		Intent intent = getIntent();
		placeStr = intent.getStringExtra("placeStr");
		initUI();
		mapView.onCreate(savedInstanceState);
		initData();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			if (StrUtils.isEmpty(etPlace.getText().toString())) {
				T.showShort(AtyCreatePosition.this, "请输入正确位置信息");
			} else {
				LatLng mTarget = aMap.getCameraPosition().target;
				Intent mIntent = new Intent();
				mIntent.putExtra("latitude", mTarget.latitude);
				mIntent.putExtra("longitude", mTarget.longitude);
				mIntent.putExtra("placeStr", etPlace.getText().toString());
				//setResult(PublishEventActivity.LOCATION_RESULT_CODE, mIntent);
				finish();
				L.i(TAG, mTarget.latitude + "," + mTarget.longitude);
			}
			break;
		case R.id.btn_back:
			onBackPressed();
			break;
		default:
			break;
		}
	}

	private void initUI() {
		mapView = (MapView) findViewById(R.id.mv_amap);
		btnSure = (Button) findViewById(R.id.btn_sure);
		etPlace = (EditText) findViewById(R.id.et_place);
		btnBack = (Button) findViewById(R.id.btn_back);

		btnSure.setOnClickListener(this);
		btnBack.setOnClickListener(this);
	}

	private void initData() {
		if (aMap == null) {
			aMap = mapView.getMap();
		}
		etPlace.setText(placeStr);
		// 设置当前位置
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(22.566939,
				113.870201), 18));
	}

	@Override
	protected void onResume() {
		super.onResume();
		mapView.onResume();
	}

	@Override
	protected void onPause() {
		super.onPause();
		mapView.onPause();
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		mapView.onSaveInstanceState(outState);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}
}
