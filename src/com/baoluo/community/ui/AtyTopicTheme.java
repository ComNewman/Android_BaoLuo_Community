package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.HumorInfoList;
import com.baoluo.community.entity.TopicInfo;
import com.baoluo.community.entity.TopicInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.TopicThemeAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
/**
 * 话题主题列表
 * @author xiangyang.fu
 *
 */
public class AtyTopicTheme extends AtyBase implements HeadViewListener,IXListViewListener{
	
	private final String TAG = this.getClass().getName();
	private HeadView headView;
	private XListView xlv;
	private TopicThemeAdapter adapter;
	private List<TopicInfo> topics;
	
	private String tagId;
	private String title;
	private int pageSize = 10;
	private int pageIndex = 1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.aty_topic_theme);
		tagId = getIntent().getStringExtra("TagId");
		title = getIntent().getStringExtra("Title");
		initUI();
		getTopicThemeList(true);
		super.onCreate(savedInstanceState);
	}
	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_topic_theme);
		headView.setHeadViewListener(this);
		String titleFormat = getResources().getString(R.string.topic_title);
		headView.setHeadTitle(String.format(titleFormat, title));
		topics = new ArrayList<TopicInfo>();
		xlv = (XListView) findViewById(R.id.xlv_topic_theme);
		adapter = new TopicThemeAdapter(this, topics, R.layout.item_topic_theme);
		xlv.setAdapter(adapter);
		xlv.setDivider(null);
		xlv.setPullLoadEnable(true);
		xlv.setPullRefreshEnable(true);
		xlv.setXListViewListener(this);
		
		xlv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String topicId = topics.get(position - 1).getId();
				L.i(TAG, topicId);
				Intent intent = new Intent();
				intent.setClass(AtyTopicTheme.this, AtyTopicDetail.class);
				intent.putExtra("id", topicId);
				startActivity(intent);
			}
		});

	}
	private void getTopicThemeList(final boolean isReflesh) {
		if (isReflesh) {
			pageIndex = 1;
		} else {
			pageIndex++;
		}
		new GetTask(UrlHelper.TOPIC_THEME_LIST,
				new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						TopicInfoList topicInfoList = (TopicInfoList)obj;
						if (topicInfoList.getItems() == null
								|| topicInfoList.getItems().size() == 0) {
							T.showShort(AtyTopicTheme.this, "没有更多数据！");
							xlv.onLoadFinsh();
							return;
						}
						if (isReflesh) {
							topics.clear();
						}
						topics.addAll(topicInfoList.getItems());
						adapter.notifyDataSetChanged();
						xlv.onLoadFinsh();
					}
				}, "Pageindex", String.valueOf(pageIndex), "Pagesize",
				String.valueOf(pageSize), "TagId",tagId);
	}
	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		
	}
	@Override
	public void onRefresh() {
		getTopicThemeList(true);
	}
	@Override
	public void onLoadMore() {
		getTopicThemeList(false);
	}
}
