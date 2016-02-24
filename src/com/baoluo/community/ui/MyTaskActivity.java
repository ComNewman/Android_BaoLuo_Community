package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.EventInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.MyEventAdapter;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;

/**
 * 我的活动界面
 * 
 * @author xiangyang.fu
 * 
 */
public class MyTaskActivity extends Activity implements OnClickListener,
		IXListViewListener {
	private static final String TAG = "MyTaskActivity";

	private Button btnBack;
	private XListView xlvMyEvent;
	private MyEventAdapter myEventAdapter;
	private int pageindex = 1;
	private int pagesize = 1;
	private List<EventInfo> events = new ArrayList<EventInfo>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_my_task);
		initUI();
		initData();
	}

	private void initUI() {
		btnBack = (Button) findViewById(R.id.btn_mytask_back);
		btnBack.setOnClickListener(this);

		xlvMyEvent = (XListView) findViewById(R.id.xlv_mytask);
		xlvMyEvent.setXListViewListener(this);
		xlvMyEvent.setPullLoadEnable(true);
		xlvMyEvent.setPullRefreshEnable(true);
	}

	private void initData() {
		myEventAdapter = new MyEventAdapter(MyTaskActivity.this, events,
				R.layout.mytask_item);
		xlvMyEvent.setAdapter(myEventAdapter);
		getEventTask(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_mytask_back:
			finish();
			break;
		default:
			break;
		}
	}

	private void onLoad() {
		xlvMyEvent.stopRefresh();
		xlvMyEvent.stopLoadMore();
		xlvMyEvent.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		pageindex = 1;
		getEventTask(true);
	}

	@Override
	public void onLoadMore() {
		pageindex++;
		getEventTask(false);
	}

	private void getEventTask(final boolean isReflesh) {
		new GetTask(UrlHelper.EVENT_MY_LIST,
				new UpdateViewHelper.UpdateViewListener(EventInfoList.class) {
					@Override
					public void onComplete(Object obj) {
							EventInfoList eventInfoList = (EventInfoList)obj;
							List<EventInfo> list = eventInfoList.getItems();
							if (list == null || list.size() == 0) {
								return;
							}
							if (isReflesh) {
								events.clear();
							}
							events.addAll(list);
							myEventAdapter.notifyDataSetChanged();
							onLoad();
					}
				}, "Pageindex", String.valueOf(pageindex), "Pagesize",
				String.valueOf(pagesize));
	}
}
