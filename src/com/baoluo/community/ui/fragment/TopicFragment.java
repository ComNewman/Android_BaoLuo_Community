package com.baoluo.community.ui.fragment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.entity.TagListInfo;
import com.baoluo.community.entity.TopicInfo;
import com.baoluo.community.entity.TopicInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.MainActivity.MyTouchListener;
import com.baoluo.community.ui.PublishTopicActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.TopicDetailsActivity;
import com.baoluo.community.ui.adapter.TopicAdapter;
import com.baoluo.community.ui.adapter.TopicSearchTagAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.T;

/**
 * 话题页面
 * 
 * @author xiangyang.fu
 * 
 */
@SuppressLint("NewApi")
public class TopicFragment extends Fragment implements IXListViewListener,
		HeadViewListener, OnClickListener {
	private static final String TAG = "TopicFragment";
	private int LOAD_MORE_FLAG = 0;
	private HeadView headView;
	private View view;
	private EditText etSearch;
	private XListView xlvTopic;
	private RelativeLayout rlSearchTag;
	private RelativeLayout rlNomal;
	private Button btnCancel;
	private EditText etSearchTag;
	private ListView xlvTags;
	private TopicSearchTagAdapter adapter;
	private TagListInfo tagListInfo;
	private List<TagInfo> items = new ArrayList<TagInfo>();

	private MainActivity activity;
	private TopicAdapter topicAdapter;
	private String tag;
	private int pageindex = 1;
	private int tagPageindex = 1;
	private int pagesize = 20;
	private TopicInfoList topicInfoList;
	private List<TopicInfo> topics = new ArrayList<TopicInfo>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_baoluo_topic, container,
				false);
		activity = (MainActivity) getActivity();
		initUI();
		initData();
		return view;
	}

	private void initUI() {
		headView = (HeadView) view.findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		headView.setOnClickListener(this);
		rlSearchTag = (RelativeLayout) view.findViewById(R.id.rl_search);
		rlNomal = (RelativeLayout) view.findViewById(R.id.rl_nomal);

		btnCancel = (Button) view
				.findViewById(R.id.btn_topic_tag_search_cancle);
		btnCancel.setOnClickListener(this);
		etSearch = (EditText) view.findViewById(R.id.et_topic_search);
		etSearch.setOnFocusChangeListener(new android.view.View.OnFocusChangeListener() {
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {
					rlSearchTag.setVisibility(View.VISIBLE);
					etSearchTag.requestFocus();
				}
			}
		});

		xlvTags = (ListView) view.findViewById(R.id.xlv_tag_topic);
		adapter = new TopicSearchTagAdapter(getActivity(), items,
				R.layout.item_topic_search_tag);
		xlvTags.setAdapter(adapter);

		etSearchTag = (EditText) view.findViewById(R.id.et_topic_tag_search);
		etSearchTag.setFocusable(true);
		etSearchTag.setFocusableInTouchMode(true);
		etSearchTag.requestFocus();
		etSearchTag.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String keyword = s.toString().trim();
				if (!TextUtils.isEmpty(keyword) && keyword.length() > 1) {
					new GetTask(UrlHelper.TOPIC_TAG_SEARCH,
							new UpdateViewHelper.UpdateViewListener(TagListInfo.class) {
								@Override
								public void onComplete(Object obj) {
									tagListInfo = (TagListInfo)obj;
									if (tagListInfo.getItems() != null
											&& tagListInfo.getItems().size() > 0) {
										items.addAll(tagListInfo.getItems());
										adapter.notifyDataSetChanged();
									} else {
										T.showShort(getActivity(), "没有搜索到数据");
									}
								}
							},"Key", keyword);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {

			}
		});

		xlvTags.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				tag = items.get(position).getName();
				Map<String, String> params = new HashMap<String, String>();
				params.put("Key", tag);
				params.put("PageIndex", "1");
				params.put("PageSize", "20");
				new GetTask(UrlHelper.TOPIC_SEARCH, params,
						new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
							@Override
							public void onComplete(Object obj) {
								topicInfoList = (TopicInfoList)obj;
								topics.clear();
								topics.addAll(topicInfoList.getItems());
								topicAdapter.notifyDataSetChanged();
								LOAD_MORE_FLAG = 1;
								rlSearchTag.setVisibility(View.GONE);
								etSearchTag.setText("");
								etSearch.setText(tag);
								items.clear();
							}
						});
			}
		});

		topicAdapter = new TopicAdapter(activity, topics,
				R.layout.topic_list_item);
		xlvTopic = (XListView) view.findViewById(R.id.xlv_topic);
		xlvTopic.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String topicId = topics.get(position - 1).getId();
				Intent intent = new Intent();
				intent.setClass(activity, TopicDetailsActivity.class);
				intent.putExtra("id", topicId);
				startActivity(intent);
			}
		});
		xlvTopic.setAdapter(topicAdapter);
		xlvTopic.setXListViewListener(this);
		xlvTopic.setPullLoadEnable(true);
		xlvTopic.setPullRefreshEnable(true);
	}

	private void initData() {
		new GetTask(UrlHelper.TOPIC_LIST,
				new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						topicInfoList = (TopicInfoList)obj;
						topics.clear();
						topics.addAll(topicInfoList.getItems());
						topicAdapter.notifyDataSetChanged();
						LOAD_MORE_FLAG = 0;
					}
				},"Pageindex", String.valueOf(pageindex),
				"Pagesize", String.valueOf(pagesize));
	}

	private void onLoad() {
		xlvTopic.stopRefresh();
		xlvTopic.stopLoadMore();
		xlvTopic.setRefreshTime("刚刚");
	}

	@Override
	public void onRefresh() {
		pageindex = 1;
		new GetTask(UrlHelper.TOPIC_LIST,
				new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						topics.clear();
						topicInfoList = (TopicInfoList)obj;
						if (topicInfoList.getItems().size() > 0) {
							topics.addAll(topicInfoList.getItems());
						}
						topicAdapter.notifyDataSetChanged();
						LOAD_MORE_FLAG = 0;
						onLoad();
						etSearch.setText("");
					}
				},"Pageindex", String.valueOf(pageindex)
		,"Pagesize", String.valueOf(pagesize));
	}

	@Override
	public void onLoadMore() {
		switch (LOAD_MORE_FLAG) {
		case 0:
			pageindex++;
			new GetTask(UrlHelper.TOPIC_LIST,
					new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
						@Override
						public void onComplete(Object obj) {
							TopicInfoList topicInfoList = (TopicInfoList) obj;
							List<TopicInfo> list = new ArrayList<TopicInfo>();
							list = topicInfoList.getItems();
							if (list.size() == 0) {
								T.showShort(activity, "已经是最后一页了");
							}
							topics.addAll(list);
							topicAdapter.notifyDataSetChanged();
							LOAD_MORE_FLAG = 0;
							onLoad();
						}
					},"Pageindex", String.valueOf(pageindex)
			,"Pagesize", String.valueOf(pagesize));
			break;
		case 1:
			tagPageindex++;
			new GetTask(UrlHelper.TOPIC_SEARCH,
					new UpdateViewHelper.UpdateViewListener(TopicInfoList.class) {
						@Override
						public void onComplete(Object obj) {
							topicInfoList = (TopicInfoList)obj;
							if (topicInfoList.getItems().size() > 0) {
								topics.addAll(topicInfoList.getItems());
								topicAdapter.notifyDataSetChanged();
							} else {
								T.showShort(getActivity(), "没有更多数据");
								tagPageindex = 1;
							}
							LOAD_MORE_FLAG = 1;
							onLoad();
						}
					},"Key", tag,
			"PageIndex", tagPageindex + ""
			,"PageSize", "20");
			break;
		default:
			break;
		}

	}

	/**
	 * 判断点击的是不是edittext
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				// 点击的是输入框区域，保留点击EditText的事件
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		// 在该Fragment的构造函数中注册mTouchListener的回调
		if (mTouchListener != null) {
			if (getActivity() != null) {
				((MainActivity) getActivity())
						.registerMyTouchListener(mTouchListener);
			}
		}
	}

	private MainActivity.MyTouchListener mTouchListener = new MyTouchListener() {
		@Override
		public void onTouchevent(MotionEvent event) {
			if (event.getAction() == MotionEvent.ACTION_DOWN) {
				View view = getActivity().getCurrentFocus();
				if (isShouldHideInput(view, event)) {
					InputMethodManager imm = (InputMethodManager) getActivity()
							.getSystemService(Context.INPUT_METHOD_SERVICE);
					if (imm != null) {
						imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
					}
				}
			}
		}
	};

	@Override
	public void leftListener() {
		activity.showFragment(MainActivity.FRAMENT_BAOLUO);
	}

	@Override
	public void rightListener() {
		startActivity(new Intent(getActivity(), PublishTopicActivity.class));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_topic_tag_search_cancle:
			rlSearchTag.setVisibility(View.GONE);
			rlNomal.setVisibility(View.VISIBLE);
			etSearchTag.setText("");
			items.clear();
			break;
		case R.id.hv_head:
			xlvTopic.setSelection(1);
			break;
		default:
			break;
		}
	}
}
