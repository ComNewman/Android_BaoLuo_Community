package com.baoluo.community.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.EventInfoList;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.entity.HumorInfoList;
import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.entity.TopicInfo;
import com.baoluo.community.entity.TopicInfoList;
import com.baoluo.community.entity.TopicTag;
import com.baoluo.community.entity.TopicTagList;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyActivityDetail;
import com.baoluo.community.ui.AtyHumorDetail;
import com.baoluo.community.ui.AtyTopicDetail;
import com.baoluo.community.ui.AtyTopicTheme;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.EventAdapter;
import com.baoluo.community.ui.adapter.EventClassifyAdapter;
import com.baoluo.community.ui.adapter.HumorListAdapter;
import com.baoluo.community.ui.adapter.TopicAdapter;
import com.baoluo.community.ui.adapter.TopicTagAdapter;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.DensityUtil;
import com.baoluo.community.util.HumorClassifyWindow;
import com.baoluo.community.util.T;

/**
 * 新宝落首页
 * 
 * @author xiangyang.fu
 * 
 */
@SuppressLint("NewApi")
public class BLFrg extends Fragment implements IXListViewListener,
		OnClickListener {
	private View view;
	private ViewPager viewPager;
	private ImageView imageView;
	private TextView textView1, textView2, textView3;
	private List<View> views;
	private RelativeLayout rlHead, rlEventClassify, rlNomal, rlSearch;
	private View view1, view2, view3;
	private EditText etSearch;
	private XListView lvHumor, lvEvent, lvTopic;
	private ListView lvClassify;
	private ImageButton ibFiltrate, ibDel;
	private String[] classifyData;
	private HumorListAdapter humorAdapter;
	private EventAdapter eventAdapter;
//	private TopicAdapter topicAdapter;
	private TopicTagAdapter topicTagAdapter;
	private List<HumorInfo> humors;
	private List<EventInfo> events;
//	private List<TopicInfo> topics;
	private List<TopicTag> topicTags;
	private List<TagInfo> items = new ArrayList<TagInfo>();
	private MainActivity activity;
	private int offset = 0;
	private int currIndex = 0;
	private int bmpW;
	private int pageindex = 1;
	private int pagesize = 20;
	private boolean isTopicSearch;
	private String keyword;
//	private TopicInfoList topicInfoList;
	private TopicTagList  topicTagList;
	private HumorInfoList humorInfoList;
	private EventInfoList eventInfoList;
	private int options = 0;
	private HumorClassifyWindow window;
	private UserSelf self;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.frg_baoluo, container, false);
		activity = (MainActivity) getActivity();
		InitImageView();
		InitHead();
		InitViewPager();
		initListView();
		initData();
		return view;
	}
	@Override
	public void onResume() {
		super.onResume();
	}
	private void initListView() {

		lvClassify = (ListView) view.findViewById(R.id.lv_event_classify);
		lvClassify.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				T.showShort(getActivity(), classifyData[position]);
				// TODO 分类请求数据 并刷新数据
				ibFiltrate.setSelected(false);
				rlEventClassify.setVisibility(View.GONE);
				rlNomal.setVisibility(View.VISIBLE);
			}
		});
		lvHumor = (XListView) view1.findViewById(R.id.xlv_humor);
		lvEvent = (XListView) view2.findViewById(R.id.xlv_eventlist);
		lvTopic = (XListView) view3.findViewById(R.id.xlv_topicList);
		humors = new ArrayList<HumorInfo>();
		events = new ArrayList<EventInfo>();
//		topics = new ArrayList<TopicInfo>();
		topicTags = new ArrayList<TopicTag>();
		humorAdapter = new HumorListAdapter(activity, humors,
				R.layout.humor_list_item);
		eventAdapter = new EventAdapter(activity, events, R.layout.event_item);
//		topicAdapter = new TopicAdapter(activity, topics,
//				R.layout.topic_list_item);
		topicTagAdapter = new TopicTagAdapter(activity, topicTags, R.layout.item_topic_tag);
		lvHumor.setAdapter(humorAdapter);
		lvEvent.setAdapter(eventAdapter);
//		lvTopic.setAdapter(topicAdapter);
		lvTopic.setAdapter(topicTagAdapter);
		lvTopic.setDivider(null);
		lvTopic.setPullLoadEnable(true);
		lvEvent.setPullLoadEnable(true);
		lvHumor.setPullLoadEnable(true);
		lvEvent.setPullRefreshEnable(true);
		lvTopic.setPullRefreshEnable(true);
		lvHumor.setPullRefreshEnable(true);
		lvTopic.setXListViewListener(this);
		lvEvent.setXListViewListener(this);
		lvHumor.setXListViewListener(this);
		lvHumor.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				// selectedItem = position;
				Intent intent = new Intent();
				intent.setClass(getActivity(), AtyHumorDetail.class);
				HumorInfo humor = humors.get(position - 1);
				if (humor != null) {
					Bundle bundle = new Bundle();
					bundle.putSerializable("humor", humor);
					bundle.putInt("humorPageNo", position - 1);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}
		});
		lvEvent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventInfo event = events.get(position - 1);
				Intent intent = new Intent();
				intent.setClass(activity, AtyActivityDetail.class);
				Bundle bundle = new Bundle();
				if (event != null) {
					bundle.putSerializable("eventInfo", event);
					bundle.putInt("eventPageNo", position - 1);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}
		});
		lvTopic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String topicId = topicTags.get(position - 1).getId();
				String title = topicTags.get(position-1).getTitle();
				Intent intent = new Intent();
				intent.setClass(activity, AtyTopicTheme.class);
				intent.putExtra("TagId", topicId);
				intent.putExtra("Title", title);
				startActivity(intent);
			}
		});

		initData();
	}

	private void initData() {
		self = SettingUtility.getUserSelf();
		if (self != null && self.getSex() == 1) {
			imageView.setImageResource(R.drawable.bg_bl_show_nan);
			textView1.setTextAppearance(activity, R.style.boy_bl_title);
			textView2.setTextAppearance(activity, R.style.boy_bl_title);
			textView3.setTextAppearance(activity, R.style.boy_bl_title);
		}
		getHumorList(true);
		getEventList(true);
		getTopicList(true);
		classifyData = new String[] { "美食", "培训", "课程", "体育", "生活", "文化", "运动",
				"科技", "音乐", "娱乐", "其它" };
		List<String> data = new ArrayList<String>();
		data = Arrays.asList(classifyData);
		lvClassify.setAdapter(new EventClassifyAdapter(getActivity(), data,
				R.layout.event_classify_item));
	}

	private void getTopicList(final boolean isReflesh) {
		if (isReflesh) {
			pageindex = 1;
		} else {
			pageindex++;
		}
//		new GetTask(UrlHelper.TOPIC_LIST,
//				new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
//					@Override
//					public void onComplete(Object obj) {
//						topicInfoList = (TopicInfoList)obj;
//						if (topicInfoList.getItems() == null
//								|| topicInfoList.getItems().size() == 0) {
//							T.showShort(getActivity(), "没有更多数据！");
//							lvTopic.onLoadFinsh();
//							return;
//						}
//						if (isReflesh) {
//							topics.clear();
//						}
//						topics.addAll(topicInfoList.getItems());
//						topicAdapter.notifyDataSetChanged();
//						lvTopic.onLoadFinsh();
//					}
//				}, "Pageindex", String.valueOf(pageindex), "Pagesize",
//				String.valueOf(pagesize));
		
		new GetTask(UrlHelper.TOPIC_TAG_LIST,
				new UpdateViewHelper.UpdateViewListener(TopicTagList.class) {
					@Override
					public void onComplete(Object obj) {
						topicTagList = (TopicTagList)obj;
						if (topicTagList.getItems() == null
								|| topicTagList.getItems().size() == 0) {
							T.showShort(getActivity(), "没有更多数据！");
							lvTopic.onLoadFinsh();
							return;
						}
						if (isReflesh) {
							topicTags.clear();
						}
						topicTags.addAll(topicTagList.getItems());
						topicTagAdapter.notifyDataSetChanged();
						lvTopic.onLoadFinsh();
					}
				}, "Pageindex", String.valueOf(pageindex), "Pagesize",
				String.valueOf(pagesize));
	}

	private void getHumorList(final boolean isReflesh) {
		if (isReflesh) {
			pageindex = 1;
		} else {
			pageindex++;
		}
		new GetTask(UrlHelper.HUMOR_LIST,
				new UpdateViewHelper.UpdateViewListener(HumorInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						humorInfoList = (HumorInfoList)obj;
						if (humorInfoList.getItems() == null
								|| humorInfoList.getItems().size() == 0) {
							T.showShort(getActivity(), "没有更多数据！");
							lvHumor.onLoadFinsh();
							return;
						}
						if (isReflesh) {
							humors.clear();
						}
						humors.addAll(humorInfoList.getItems());
						humorAdapter.notifyDataSetChanged();
						lvHumor.onLoadFinsh();
					}
				}, "Pageindex", String.valueOf(pageindex), "Pagesize",
				String.valueOf(pagesize), "options", String.valueOf(options));
	}

	private void getEventList(final boolean isReflesh) {
		if (isReflesh) {
			pageindex = 1;
		} else {
			pageindex++;
		}
		new GetTask(UrlHelper.EVENT_LIST,
				new UpdateViewHelper.UpdateViewListener(EventInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						eventInfoList = (EventInfoList)obj;
						if (eventInfoList.getItems() == null
								|| eventInfoList.getItems().size() == 0) {
							T.showShort(getActivity(), "没有更多数据！");
							lvEvent.onLoadFinsh();
							return;
						}
						List<EventInfo> list = eventInfoList.getItems();
						if (isReflesh) {
							events.clear();
						}
						events.addAll(list);
						eventAdapter.notifyDataSetChanged();
						lvEvent.onLoadFinsh();
					}
				}, "Pageindex", pageindex + "", "Pagesize", pagesize + "");
	}

	@SuppressLint("InflateParams")
	private void InitViewPager() {
		viewPager = (ViewPager) view.findViewById(R.id.vpager);
		views = new ArrayList<View>();
		LayoutInflater inflater = getActivity().getLayoutInflater();
		view1 = inflater.inflate(R.layout.humor_list, null);
		view2 = inflater.inflate(R.layout.event_list, null);
		view3 = inflater.inflate(R.layout.topic_list, null);
		views.add(view1);
		views.add(view2);
		views.add(view3);
		viewPager.setAdapter(new MyViewPagerAdapter(views));
		viewPager.setCurrentItem(0);
		viewPager.setOnPageChangeListener(new MyOnPageChangeListener());

	}

	private void InitHead() {
		rlNomal = (RelativeLayout) view.findViewById(R.id.rl_bl_nomal);
		rlEventClassify = (RelativeLayout) view
				.findViewById(R.id.rl_bl_event_classify);
		ibFiltrate = (ImageButton) view.findViewById(R.id.ib_bl_title_right);
		ibFiltrate.setOnClickListener(this);
		rlHead = (RelativeLayout) view.findViewById(R.id.rl_bl_head);
		rlHead.setOnClickListener(this);

		rlSearch = (RelativeLayout) view.findViewById(R.id.rl_bl_search);
		etSearch = (EditText) view.findViewById(R.id.et_bl_search);
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				keyword = s.toString().trim();
				if (keyword.length() > 0) {
					ibDel.setVisibility(View.VISIBLE);
				} else {
					ibDel.setVisibility(View.GONE);
				}
				switch (currIndex) {
				case 1:
					// TODO
					break;

				case 2:
//					getTopicSearchData(keyword, true);
					break;
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

				if (s.length() > 0) {
					ibDel.setVisibility(View.VISIBLE);
				} else {
					ibDel.setVisibility(View.GONE);
				}
			}
		});

		ibDel = (ImageButton) view.findViewById(R.id.ib_bl_del_text);
		ibDel.setOnClickListener(this);
		textView1 = (TextView) view.findViewById(R.id.tv_bl_humor);
		textView2 = (TextView) view.findViewById(R.id.tv_bl_event);
		textView3 = (TextView) view.findViewById(R.id.tv_bl_topic);
		textView1.setOnClickListener(new MyOnClickListener(0));
		textView2.setOnClickListener(new MyOnClickListener(1));
		textView3.setOnClickListener(new MyOnClickListener(2));
		select(0);

		window = new HumorClassifyWindow(getActivity());
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				ibFiltrate.setSelected(false);
				options = window.getHot() + window.getSex() + window.getRange();
				T.showShort(getActivity(), "" + options);
				getHumorList(true);
			}
		});
	}

	private void restore() {
		rlEventClassify.setVisibility(View.GONE);
		rlNomal.setVisibility(View.VISIBLE);
		ibFiltrate.setSelected(false);
	}

	private void select(int flag) {
		restore();
		switch (flag) {
		case 0:
			textView1.setSelected(true);
			textView2.setSelected(false);
			textView3.setSelected(false);
			rlSearch.setVisibility(View.GONE);
			break;
		case 1:
			textView1.setSelected(false);
			textView2.setSelected(true);
			textView3.setSelected(false);
			break;
		case 2:
			textView1.setSelected(false);
			textView2.setSelected(false);
			textView3.setSelected(true);
			break;
		}
	}

	private void InitImageView() {
		imageView = (ImageView) view.findViewById(R.id.cursor);
		bmpW = BitmapFactory.decodeResource(getResources(),
				R.drawable.bg_bl_show).getWidth();
		offset = (DensityUtil.dip2px(200) / 3 - bmpW) / 2;
		Matrix matrix = new Matrix();
		matrix.postTranslate(offset, 0);
		imageView.setImageMatrix(matrix);
	}

	/**
	 * 
	 * 头标点击监听 3
	 */
	private class MyOnClickListener implements OnClickListener {
		private int index = 0;

		public MyOnClickListener(int i) {
			index = i;
		}

		public void onClick(View v) {
			viewPager.setCurrentItem(index);
			select(index);
		}
	}

	public class MyViewPagerAdapter extends PagerAdapter {
		private List<View> mListViews;

		public MyViewPagerAdapter(List<View> mListViews) {
			this.mListViews = mListViews;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(mListViews.get(position));
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(mListViews.get(position), 0);
			return mListViews.get(position);
		}

		@Override
		public int getCount() {
			return mListViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}
	}

	public class MyOnPageChangeListener implements OnPageChangeListener {

		int one = offset * 2 + bmpW;
		int two = one * 2;

		public void onPageScrollStateChanged(int arg0) {

		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageSelected(int arg0) {
			select(arg0);
			Animation animation = new TranslateAnimation(one * currIndex, one
					* arg0, 0, 0);
			currIndex = arg0;
			animation.setFillAfter(true);
			animation.setDuration(300);
			imageView.startAnimation(animation);
			switch (arg0) {
			case 0:
				ibFiltrate.setVisibility(View.VISIBLE);
				rlSearch.setVisibility(View.GONE);
				break;
			case 1:
				ibFiltrate.setVisibility(View.VISIBLE);
				rlSearch.setVisibility(View.VISIBLE);
				break;
			case 2:
				ibFiltrate.setVisibility(View.GONE);
				rlSearch.setVisibility(View.VISIBLE);
				break;
			}
		}

	}

	@Override
	public void onRefresh() {
		switch (currIndex) {
		case 0:
			getHumorList(true);
			break;
		case 1:
			getEventList(true);
			break;
		case 2:
			if (isTopicSearch) {
//				getTopicSearchData(keyword, true);
			} else {
				getTopicList(true);
			}
			break;
		}
	}

	@Override
	public void onLoadMore() {
		switch (currIndex) {
		case 0:
			getHumorList(false);
			break;
		case 1:
			getEventList(false);
			break;
		case 2:
			if (isTopicSearch) {
//				getTopicSearchData(keyword, false);
			} else {
				getTopicList(false);
			}
			break;
		}
	}
//
//	private void getTopicSearchData(String keyword, final boolean isReflesh) {
//		if (isReflesh) {
//			pageindex = 1;
//		} else {
//			pageindex++;
//		}
//		if (!TextUtils.isEmpty(keyword) && keyword.length() > 1) {
//			new GetTask(UrlHelper.TOPIC_SEARCH,
//					new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
//						@Override
//						public void onComplete(Object obj) {
//							topicInfoList = (TopicInfoList)obj;
//							if (topicInfoList.getItems() == null
//									|| topicInfoList.getItems().size() == 0) {
//								T.showShort(getActivity(), "没有更多数据！");
//								lvTopic.onLoadFinsh();
//								return;
//							}
//							isTopicSearch = true;
//							if (isReflesh) {
//								topics.clear();
//							}
//							topics.addAll(topicInfoList.getItems());
//							topicAdapter.notifyDataSetChanged();
//							lvTopic.onLoadFinsh();
//						}
//					}, "Key", keyword, "PageIndex", String.valueOf(pageindex),
//					"PageSize", String.valueOf(pagesize));
//		}
//	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_bl_title_right:
			switch (currIndex) {
			case 0:
				ibFiltrate.setSelected(!ibFiltrate.isSelected());
				window.showPopupWindow(rlHead);
				break;
			case 1:
				ibFiltrate.setSelected(!ibFiltrate.isSelected());
				if (ibFiltrate.isSelected()) {
					rlNomal.setVisibility(View.GONE);
					rlEventClassify.setVisibility(View.VISIBLE);
				} else {
					rlNomal.setVisibility(View.VISIBLE);
					rlEventClassify.setVisibility(View.GONE);
				}
				break;
			}
			break;
		case R.id.ib_bl_del_text:
			etSearch.setText("");
			switch (currIndex) {
			case 1:
				// TODO
				break;
			case 2:
				isTopicSearch = false;
				getTopicList(true);
				break;
			}
			break;
		case R.id.rl_bl_head:
			switch (currIndex) {
			case 0:
				lvHumor.setSelection(1);
				break;
			case 1:
				lvEvent.setSelection(1);
				break;
			case 2:
				lvTopic.setSelection(1);
				break;

			}
			break;
		}

	}
}
