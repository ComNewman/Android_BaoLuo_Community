package com.baoluo.community.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.BaoLuoEventInfo;
import com.baoluo.community.entity.BaoLuoHome;
import com.baoluo.community.entity.BaoLuoHumorInfo;
import com.baoluo.community.entity.BaoLuoTopicInfo;
import com.baoluo.community.entity.BlogUser;
import com.baoluo.community.entity.BlogUserListInfo;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.Topic;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.support.topicCloud.TopicCloudView;
import com.baoluo.community.ui.AtyFriInfo;
import com.baoluo.community.ui.EventDetailsActivity;
import com.baoluo.community.ui.HumorDetailsActivity;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.HorizontalListViewAdapter;
import com.baoluo.community.ui.customview.HorizontalListView;
import com.baoluo.community.util.DensityUtil;
import com.baoluo.community.util.ImgUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 宝落模块
 * 
 * @author Ryan_Fu 2015-5-12
 */
@SuppressLint("NewApi")
public class BaoluoFragment extends Fragment implements OnClickListener {
	private static final String TAG = "BaoluoFragment";

	private View view;
	private HorizontalListView hlvList;
	private RelativeLayout rlHumorMore, rlActivityMore, rlTopicMore,rlTopics;
	private RelativeLayout rlBlur1, rlBlur2, rlBlur3;
	private ImageView humor1, humor2, humor3;
	private ImageView event1, event2, event3;
	private TextView tvCollectNum1, tvCollectNum2, tvCollectNum3;
	private TextView tvCommentNum1, tvCommentNum2, tvCommentNum3;
	private TextView hotTopic;
	private HorizontalListViewAdapter hlvAdapter;
	private TopicCloudView topicCloudView;
	private ScrollView mscrollView;

	private int pageindex = 1;
	private int pagesize = 10;
	private BaoLuoHome homeData;
	private List<BaoLuoHumorInfo> humors;
	private List<BaoLuoEventInfo> events;
	private List<BaoLuoTopicInfo> topics;
	private BlogUserListInfo userListStr;
	private List<BlogUser> users;
	
	private List<Topic> topicList;
	
	private boolean measured = false;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_baoluo, container, false);
		initView();
		initData();
		return view;
	}
	
	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		getData();
	}

	@Override
	public void onResume() {
		super.onResume();
	}

	@Override
	public void onPause() {
		super.onPause();
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		topicCloudView.topicAutoDestroy();
	}
	
	

	private void initView() {
		hlvList = (HorizontalListView) view.findViewById(R.id.hlv_new_user);
		rlHumorMore = (RelativeLayout) view.findViewById(R.id.rl_humore_more);
		rlActivityMore = (RelativeLayout) view
				.findViewById(R.id.rl_activity_more);
		rlTopicMore = (RelativeLayout) view.findViewById(R.id.rl_topic);

		rlHumorMore.setOnClickListener(this);
		rlActivityMore.setOnClickListener(this);
		rlTopicMore.setOnClickListener(this);
		
		rlBlur1 = (RelativeLayout) view
				.findViewById(R.id.rl_home_image_bottom_1);
		rlBlur2 = (RelativeLayout) view
				.findViewById(R.id.rl_home_image_bottom_2);
		rlBlur3 = (RelativeLayout) view
				.findViewById(R.id.rl_home_image_bottom_3);
		rlBlur1.getBackground().setAlpha(110);
		rlBlur2.getBackground().setAlpha(110);
		rlBlur3.getBackground().setAlpha(110);
		humor1 = (ImageView) view.findViewById(R.id.iv_humor_1);
		humor2 = (ImageView) view.findViewById(R.id.iv_humor_2);
		humor3 = (ImageView) view.findViewById(R.id.iv_humor_3);

		humor1.setOnClickListener(this);
		humor2.setOnClickListener(this);
		humor3.setOnClickListener(this);

		event1 = (ImageView) view.findViewById(R.id.iv_activity_1);
		event2 = (ImageView) view.findViewById(R.id.iv_activity_2);
		event3 = (ImageView) view.findViewById(R.id.iv_activity_3);

		event1.setOnClickListener(this);
		event2.setOnClickListener(this);
		event3.setOnClickListener(this);

		tvCollectNum1 = (TextView) view.findViewById(R.id.tv_home_collectnum_1);
		tvCollectNum2 = (TextView) view.findViewById(R.id.tv_home_collectnum_2);
		tvCollectNum3 = (TextView) view.findViewById(R.id.tv_home_collectnum_3);

		tvCommentNum1 = (TextView) view.findViewById(R.id.tv_home_comment_1);
		tvCommentNum2 = (TextView) view.findViewById(R.id.tv_home_comment_2);
		tvCommentNum3 = (TextView) view.findViewById(R.id.tv_home_comment_3);

		hotTopic = (TextView) view.findViewById(R.id.tv_topic_title);
		hotTopic.setOnClickListener(this);
		
		mscrollView = (ScrollView) view.findViewById(R.id.sv);
		rlTopics = (RelativeLayout) view.findViewById(R.id.rl_topics);
		
		ViewTreeObserver observer = humor3.getViewTreeObserver();
		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				if(!measured){
					measured = true;
					int width = humor3.getMeasuredWidth();
					initMeasure(width);
				}
				return true;
			}
		});
	}
	
	private void initMeasure(int width){
		int sWidth = width/2 - DensityUtil.dip2px(2);
		LayoutParams lp = new LayoutParams(sWidth, sWidth);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT,RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.BELOW,R.id.tv_humor_title);
		lp.setMargins(DensityUtil.dip2px(20), DensityUtil.dip2px(5), 0, 0);
		humor1.setLayoutParams(lp);
		

		lp = new LayoutParams(sWidth, sWidth);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.ALIGN_TOP,R.id.iv_humor_1);
		lp.setMargins(0, 0, DensityUtil.dip2px(20),0);
		humor2.setLayoutParams(lp);
		
		lp = new LayoutParams(width, width/2);
		lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.iv_humor_1);
		lp.addRule(RelativeLayout.BELOW, R.id.iv_humor_1);
		lp.setMargins(0, DensityUtil.dip2px(4), DensityUtil.dip2px(20), 0);
		humor3.setLayoutParams(lp);
		
		lp = new LayoutParams(sWidth, sWidth);
		lp.addRule(RelativeLayout.BELOW, R.id.tv_activity_title);
		lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
		lp.setMargins(DensityUtil.dip2px(20), DensityUtil.dip2px(5), 0, 0);
		event1.setLayoutParams(lp);
		
		lp = new LayoutParams(sWidth, sWidth);
		lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
		lp.addRule(RelativeLayout.ALIGN_TOP,R.id.iv_activity_1);
		lp.setMargins(0, 0, DensityUtil.dip2px(20),0);
		event2.setLayoutParams(lp);
		
		lp = new LayoutParams(width, width/2);
		lp.addRule(RelativeLayout.ALIGN_LEFT, R.id.iv_activity_1);
		lp.addRule(RelativeLayout.BELOW, R.id.iv_activity_1);
		lp.setMargins(0, DensityUtil.dip2px(4), DensityUtil.dip2px(20), 0);
		event3.setLayoutParams(lp);
	}
	
	private void initData() {
		topicList = new ArrayList<Topic>();
		users = new ArrayList<BlogUser>();
		topicCloudView = new TopicCloudView(getActivity());
		hlvAdapter = new HorizontalListViewAdapter(
				getActivity(), users,
				R.layout.item_new_user);
		hlvList.setAdapter(hlvAdapter);
		getData();
		hlvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				T.showShort(getActivity(), users.get(position).getName());
				String userId  = users.get(position).getId();
				if(!StrUtils.isEmpty(userId)){
					Intent i = new Intent();
					i.setClass(getActivity(), AtyFriInfo.class);
					i.putExtra("id", userId);
					startActivity(i);
				}
			}
		});
	}
	
	private void getData(){
		new GetTask(UrlHelper.NEW_USER,
				new UpdateViewHelper.UpdateViewListener(BlogUserListInfo.class) {
					@Override
					public void onComplete(Object obj) {
						userListStr = (BlogUserListInfo)obj;
						if (userListStr.getItems() != null && userListStr.getItems().size() > 0) {
							users.clear();
							users.addAll(userListStr.getItems());
							hlvAdapter.notifyDataSetChanged();
						}
					}
				},"Pageindex", pageindex+"","Pagesize", pagesize+"");
		new GetTask(UrlHelper.BAOLUO_HOME,new UpdateViewHelper.UpdateViewListener(BaoLuoHome.class){
			@Override
			public void onComplete(Object obj) {
				homeData = (BaoLuoHome)obj;
				if (homeData != null) {
					humors = homeData.getTodayMicroblog();
					events = homeData.getEventList();
					topics = homeData.getHomeTopic();
					initHumor();
					initEvent();
					initTopic();
				}
			}
		});
	}

	private void initHumor() {
		if (humors != null && humors.size() > 0) {
			GlobalContext.getInstance().imageLoader.displayImage(humors.get(0)
					.getPictures(), humor1, GlobalContext.options, null);
			tvCollectNum1
					.setText(String.valueOf(humors.get(0).getPraiseNum()));
			tvCommentNum1.setText(String.valueOf(humors.get(0).getCommitNum()));
			if (humors.size() > 1) {
				GlobalContext.getInstance().imageLoader.displayImage(humors
						.get(1).getPictures(), humor2, GlobalContext.options,
						null);
				tvCollectNum2.setText(String.valueOf(humors.get(1)
						.getPraiseNum()));
				tvCommentNum2.setText(String.valueOf(humors.get(1)
						.getCommitNum()));
			}
			if (humors.size() > 2
					&& !StrUtils.isEmpty(humors.get(2).getPictures())) {
				Bitmap bm = GlobalContext.getInstance().imageLoader
						.loadImageSync(humors.get(2).getPictures());
				if (bm != null) {
					humor3.setImageBitmap(ImgUtil.ImageHalfCrop(bm));
				}
				tvCollectNum3.setText(String.valueOf(humors.get(2)
						.getPraiseNum()));
				tvCommentNum3.setText(String.valueOf(humors.get(2)
						.getCommitNum()));
			}
		}
	}
	
	private void initEvent() {
		if (events != null && events.size() > 0) {
			GlobalContext.getInstance().imageLoader.displayImage(events.get(0)
					.getPictures(), event1, GlobalContext.options, null);
			if (events.size() > 1) {
				GlobalContext.getInstance().imageLoader.displayImage(events
						.get(1).getPictures(), event2, GlobalContext.options,
						null);
			}
			if (events.size() > 2
					&& !StrUtils.isEmpty(events.get(2).getPictures())) {
				Bitmap bm = GlobalContext.getInstance().imageLoader
						.loadImageSync(events.get(2).getPictures());
				if (bm != null) {
					event3.setImageBitmap(ImgUtil.ImageHalfCrop(bm));
				}
			}
		}
	}
	
	@SuppressLint("ClickableViewAccessibility")
	private void initTopic(){
		if(topics == null || topics.size() == 0){
			return;
		}
		topicList.clear();
		rlTopics.removeAllViews();
		Topic topic;
		for(BaoLuoTopicInfo ti : topics){
			topic = new Topic(ti.getTitle(),ti.getId());
			topicList.add(topic);
		}
		//DensityUtil.dip2px(400)
		topicCloudView = new TopicCloudView(getActivity(),
				400,400, topicList);
		LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		lp.addRule(RelativeLayout.CENTER_IN_PARENT);
		topicCloudView.setLayoutParams(lp);
		topicCloudView.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL);
		
		topicCloudView.setScrollView(mscrollView);
		topicCloudView.requestFocus();
		topicCloudView.setFocusableInTouchMode(true);
		topicCloudView.startAutoTopic();
		rlTopics.addView(topicCloudView);
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.rl_humore_more:
			((MainActivity) getActivity())
					.showFragment(MainActivity.FRAMENT_HUMOR);
			break;
		case R.id.rl_activity_more:
			((MainActivity) getActivity())
					.showFragment(MainActivity.FRAMENT_ACTIVITY);
			break;
		case R.id.rl_topic:
			((MainActivity) getActivity())
					.showFragment(MainActivity.FRAMENT_TOPIC);
			break;
		case R.id.iv_humor_1:
			if (humors != null && humors.size() > 0) {
				intent.setClass(getActivity(), HumorDetailsActivity.class);
				intent.putExtra("humorId", humors.get(0).getId());
				startActivity(intent);
			}
			break;
		case R.id.iv_humor_2:
			if (humors != null && humors.size() > 1) {
				intent.setClass(getActivity(), HumorDetailsActivity.class);
				intent.putExtra("humorId", humors.get(1).getId());
				startActivity(intent);
			}
			break;
		case R.id.iv_humor_3:
			if (humors != null && humors.size() > 2) {
				intent.setClass(getActivity(), HumorDetailsActivity.class);
				intent.putExtra("humorId", humors.get(2).getId());
				startActivity(intent);
			}
			break;
		case R.id.iv_activity_1:
			activityLink(intent, 0);
			break;
		case R.id.iv_activity_2:
			activityLink(intent, 1);
			break;
		case R.id.iv_activity_3:
			activityLink(intent, 2);
			break;
		case R.id.tv_topic_title:
			((MainActivity) getActivity())
					.showFragment(MainActivity.FRAMENT_TOPIC);
			break;
		default:
			break;
		}
	}

	private void activityLink(Intent intent, int index) {
		if (index == 0 && events.size() == 0) {
			return;
		} else if (index == 1 && events.size() == 1) {
			return;
		} else if (index == 2 && events.size() == 2) {
			return;
		}
		EventInfo event = new EventInfo();
		event.setId(events.get(index).getId());
		L.e(TAG, "eventId=" + event.getId());
		intent.setClass(getActivity(), EventDetailsActivity.class);
		Bundle bundle = new Bundle();
		bundle.putSerializable("eventInfo", event);
		intent.putExtras(bundle);
		startActivity(intent);
	}
}
