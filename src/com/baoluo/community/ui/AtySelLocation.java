package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;

import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.SupportMapFragment;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.overlay.PoiOverlay;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.core.SuggestionCity;
import com.amap.api.services.help.Inputtips;
import com.amap.api.services.help.Inputtips.InputtipsListener;
import com.amap.api.services.help.Tip;
import com.amap.api.services.poisearch.PoiItemDetail;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.amap.api.services.poisearch.PoiSearch.OnPoiSearchListener;
import com.baoluo.community.core.Configs;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 地理位置选择
 * 
 * @author tao.lai
 * 
 */
public class AtySelLocation extends AtyBase implements TextWatcher,HeadViewListener,
		OnPoiSearchListener, OnClickListener {
	private static final String TAG = "AtySelLocation";

	public static final int LOCATION_CODE = 0x0;

	private AMap aMap;

	private HeadView headView;
	private ProgressDialog progDialog = null;
	private ImageView ivSearch;
	private AutoCompleteTextView atvKeyWord;

	private PoiResult poiResult;
	private PoiSearch.Query query;
	private PoiSearch poiSearch;

	private LatLng mLocation;
	private String keyWord;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sel_location);
		mLocation = new LatLng(getIntent().getDoubleExtra("latitude",
				Configs.latitude), getIntent().getDoubleExtra("longitude",
				Configs.longitude));
		initUI();
		initData();
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_head);
		ivSearch = (ImageView) findViewById(R.id.iv_search);
		atvKeyWord = (AutoCompleteTextView) findViewById(R.id.atv_key_word);
		aMap = ((SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.amap)).getMap();
		headView.setHeadViewListener(this);
		ivSearch.setOnClickListener(this);
	}

	private void initData() {
		aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 18));
		atvKeyWord.addTextChangedListener(this);
	}

	private void sureChoice() {
		LatLng mTarget = aMap.getCameraPosition().target;
		Intent mIntent = new Intent();
		mIntent.putExtra("latitude", mTarget.latitude);
		mIntent.putExtra("longitude", mTarget.longitude);
		setResult(RESULT_OK, mIntent);
		L.i(TAG, "latitude=" + mTarget.latitude + "/longitude="
				+ mTarget.longitude);
		finish();
	}

	private void doSearch() {
		keyWord = StrUtils.checkEditText(atvKeyWord);
		if (StrUtils.isEmpty(keyWord)) {
			T.showShort(this, "请输入搜索关键字");
			return;
		} else {
			doSearchQuery();
		}
	}

	protected void doSearchQuery() {
		showProgressDialog();
		query = new PoiSearch.Query(keyWord, "", "");
		query.setPageSize(10);
		query.setPageNum(0);

		poiSearch = new PoiSearch(this, query);
		poiSearch.setOnPoiSearchListener(this);
		poiSearch.searchPOIAsyn();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_search:
			doSearch();
			break;
		
		default:
			break;
		}
	}

	@Override
	public void onPoiItemDetailSearched(PoiItemDetail arg0, int arg1) {
	}

	@Override
	public void onPoiSearched(PoiResult result, int rCode) {
		dissmissProgressDialog();
		if (rCode == 0) {
			if (result != null && result.getQuery() != null) {
				if (result.getQuery().equals(query)) {
					poiResult = result;
					List<PoiItem> poiItems = poiResult.getPois();
					List<SuggestionCity> suggestionCities = poiResult
							.getSearchSuggestionCitys();
					if (poiItems != null && poiItems.size() > 0) {
						aMap.clear();// 清理之前的图标
						PoiOverlay poiOverlay = new PoiOverlay(aMap, poiItems);
						poiOverlay.removeFromMap();
						poiOverlay.addToMap();
						poiOverlay.zoomToSpan();
					} else if (suggestionCities != null
							&& suggestionCities.size() > 0) {
						showSuggestCity(suggestionCities);
					} else {
						T.showShort(this, "对不起，没有搜索到相关数据！");
					}
				}
			} else {
				T.showShort(this, "对不起，没有搜索到相关数据！");
			}
		} else if (rCode == 27) {
			T.showShort(this, "搜索失败,请检查网络连接！");
		} else if (rCode == 32) {
			T.showShort(this, "key验证无效！");
		} else {
			T.showShort(this, "未知错误，请稍后重试!错误码为" + rCode);
		}
	}

	private void showSuggestCity(List<SuggestionCity> cities) {
		String infomation = "推荐城市\n";
		for (int i = 0; i < cities.size(); i++) {
			infomation += "城市名称:" + cities.get(i).getCityName() + "城市区号:"
					+ cities.get(i).getCityCode() + "城市编码:"
					+ cities.get(i).getAdCode() + "\n";
		}
		T.showShort(this, infomation);
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		String newText = s.toString().trim();
		Inputtips inputTips = new Inputtips(AtySelLocation.this,
				new InputtipsListener() {
					@Override
					public void onGetInputtips(List<Tip> tipList, int rCode) {
						if (rCode == 0) {
							List<String> listString = new ArrayList<String>();
							if (tipList != null && tipList.size() > 0) {
								for (int i = 0; i < tipList.size(); i++) {
									listString.add(tipList.get(i).getName());
								}
								ArrayAdapter<String> aAdapter = new ArrayAdapter<String>(
										getApplicationContext(),
										R.layout.item_search_poi, listString);
								atvKeyWord.setAdapter(aAdapter);
								aAdapter.notifyDataSetChanged();
							}
						}
					}
				});
		try {
			inputTips.requestInputtips(newText, "");
		} catch (AMapException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	private void showProgressDialog() {
		if (progDialog == null)
			progDialog = new ProgressDialog(this);
		progDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		progDialog.setIndeterminate(false);
		progDialog.setCancelable(false);
		progDialog.setMessage("正在搜索:\n" + keyWord);
		progDialog.show();
	}

	private void dissmissProgressDialog() {
		if (progDialog != null) {
			progDialog.dismiss();
		}
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		sureChoice();
	}
}
