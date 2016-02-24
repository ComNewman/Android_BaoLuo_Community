package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.core.Configs;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 发布 页 选择地理位置(弹出框)
 * 
 * @author tao.lai
 * 
 */
public class AtySelPlaceDialog extends AtyBase implements OnClickListener,
		IXListViewListener {
	private static final String TAG = "AtySelLocation";

	public static final int PLACE_CODE = 0x5;

	private EditText etLoc;
	private Button btnOtherLoc, btnSure;
	private RelativeLayout rlCustom;
	private XListView xvLocs;

	private LatLng mLocation;
	private List<String> placeList;
	private PlaceAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sel_place);
		mLocation = new LatLng(getIntent().getDoubleExtra("latitude",
				Configs.latitude), getIntent().getDoubleExtra("longitude",
				Configs.longitude));
		initUI();
		initData();
	}

	private void initUI() {
		etLoc = (EditText) findViewById(R.id.et_loc);
		rlCustom = (RelativeLayout) findViewById(R.id.rl_custom);
		btnOtherLoc = (Button) findViewById(R.id.btn_other);
		btnSure = (Button) findViewById(R.id.btn_sure);
		xvLocs = (XListView) findViewById(R.id.xv_locs);

		rlCustom.setOnClickListener(this);
		btnOtherLoc.setOnClickListener(this);
		btnSure.setOnClickListener(this);

		xvLocs.setPullLoadEnable(true);
		xvLocs.setPullRefreshEnable(false);
		xvLocs.setXListViewListener(this);
	}

	private void sureEdit() {
		String place = StrUtils.checkEditText(etLoc);
		if (StrUtils.isEmpty(place)) {
			T.showShort(this, "请选择或输入地址描述信息");
			return;
		} else {
			Intent i = new Intent();
			i.putExtra("placeName", place);
			i.putExtra("latitude", mLocation.latitude);
			i.putExtra("longitude", mLocation.longitude);
			setResult(RESULT_OK, i);
			finish();
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == AtySelLocation.LOCATION_CODE) {
				mLocation = new LatLng(data.getDoubleExtra("latitude",
						mLocation.latitude), data.getDoubleExtra("longitude",
						mLocation.longitude));
				L.i(TAG, "onActivityResult   mLocation = " + mLocation.latitude);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_other:
			Intent i = new Intent();
			i.setClass(this, AtySelLocation.class);
			i.putExtra("latitude", mLocation.latitude);
			i.putExtra("longitude", mLocation.longitude);
			startActivityForResult(i, AtySelLocation.LOCATION_CODE);
			break;
		case R.id.btn_sure:
			sureEdit();
			break;
		case R.id.rl_custom:
			etLoc.setText("");
			etLoc.setFocusable(true);
			break;
		default:
			break;
		}
	}

	private void initData() {
		placeList = new ArrayList<String>();
		placeList.add("宝安互联网产业基地");
		placeList.add("新安六路快乐园产业基地");
		placeList.add("快乐园");
		placeList.add("互联网产业基地4栋");
		placeList.add("快乐园");
		placeList.add("互联网产业基地4栋");
		placeList.add("快乐园");
		placeList.add("互联网产业基地4栋");
		placeList.add("快乐园");
		placeList.add("互联网产业基地4栋");

		adapter = new PlaceAdapter(this, placeList, R.layout.item_place_name);
		xvLocs.setAdapter(adapter);
		xvLocs.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				etLoc.setText(placeList.get(position - 1));
			}
		});
	}

	@Override
	public void onRefresh() {

	}

	@Override
	public void onLoadMore() {
		try {
			Thread.sleep(3 * 1000);
			xvLocs.onLoadFinsh();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

	}

	private class PlaceAdapter extends CommonAdapter<String> {
		public PlaceAdapter(Context context, List<String> mDatas,
				int itemLayoutId) {
			super(context, mDatas, itemLayoutId);
		}

		@Override
		public void convert(ViewHolder helper, String item) {
			helper.setText(R.id.tv_place, item);
		}
	}
}
