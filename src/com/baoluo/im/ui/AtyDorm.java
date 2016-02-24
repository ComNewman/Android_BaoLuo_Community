package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.DormInfo;
import com.baoluo.community.entity.DormInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.AtyDormCreate;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.DormAdapter;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.T;

/**
 * 宿舍
 * 
 * @author tao.lai
 * 
 */
public class AtyDorm extends AtyBase implements OnClickListener,
		IXListViewListener {
	private static final String TAG = "AtyDorm";
	

	private ImageView imgBack;
	private Button btnRandom, btnSingle, btnPublish, btnGroup;
	private XListView xlvDorm;
	private DormAdapter adapter;

	private List<DormInfo> list;
	private DormInfoList dorms;
	private int pageIndex = 1;
	private int pageSize = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_dorm);
		initView();
		initData();
	}

	private void initView() {
		btnPublish = (Button) findViewById(R.id.btn_dorm_publish);
		imgBack = (ImageView) findViewById(R.id.img_dorm_back);

		btnRandom = (Button) findViewById(R.id.btn_dorm_random);
		btnGroup = (Button) findViewById(R.id.btn_dorm_group);
		btnSingle = (Button) findViewById(R.id.btn_dorm_singer);

		xlvDorm = (XListView) findViewById(R.id.xv_dorm_list);

		imgBack.setOnClickListener(this);
		btnPublish.setOnClickListener(this);
		btnRandom.setOnClickListener(this);
		btnGroup.setOnClickListener(this);
		btnSingle.setOnClickListener(this);
		btnRandom.setSelected(true);
		xlvDorm.setXListViewListener(this);
	}

	
	private void initData() {
		list = new ArrayList<DormInfo>();
		adapter = new DormAdapter(AtyDorm.this, list,
				R.layout.item_dorm);
		xlvDorm.setAdapter(adapter);
		getData(false,false);
		
	}
	
	private void getData(final boolean isRefresh,final boolean isLoadMore){
		new GetTask(UrlHelper.DORM,new UpdateViewHelper.UpdateViewListener(DormInfoList.class) {
			@Override
			public void onFail() {
				T.showShort(AtyDorm.this, "网络响应失败");
				if(isRefresh || isLoadMore){
					xlvDorm.onLoadFinsh();
				}
			}

			@Override
			public void onComplete(Object obj) {
				dorms = (DormInfoList)obj;
				if (dorms.getItems() != null && dorms.getItems().size() > 0) {
					if(!isLoadMore){
						list.clear();
					}
					list.addAll(dorms.getItems());
					adapter.notifyDataSetChanged();
					if(isRefresh || isLoadMore){
						xlvDorm.onLoadFinsh();
					}
				}
			}
		},"Pageindex", String.valueOf(pageIndex),
		"Pagesize", String.valueOf(pageSize));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dorm_publish:
			startActivity(new Intent(this, AtyDormCreate.class));
			break;
		case R.id.img_dorm_back:
			onBackPressed();
			break;
		case R.id.btn_dorm_group:
			btnGroup.setSelected(true);
			btnSingle.setSelected(false);
			btnRandom.setSelected(false);
			break;
		case R.id.btn_dorm_random:
			btnGroup.setSelected(false);
			btnSingle.setSelected(false);
			btnRandom.setSelected(true);
			break;
		case R.id.btn_dorm_singer:
			btnGroup.setSelected(false);
			btnSingle.setSelected(true);
			btnRandom.setSelected(false);
			break;
		default:
			break;
		}
	}

	
	@Override
	public void onRefresh() {
		pageIndex = 1;
		getData(true,false);
	}

	@Override
	public void onLoadMore() {
		pageIndex ++ ;
		getData(false,true);
	}
}
