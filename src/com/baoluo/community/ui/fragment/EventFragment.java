package com.baoluo.community.ui.fragment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.EventInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyActivitySend;
import com.baoluo.community.ui.EventDetailsActivity;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.EventAdapter;
import com.baoluo.community.ui.adapter.EventClassifyAdapter;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.T;

@SuppressLint("NewApi")
/**
 * 活动页面
 * @author xiangyang.fu
 *
 */
public class EventFragment extends Fragment implements OnClickListener,
		IXListViewListener, OnScrollListener {
	private static final String TAG = "EventFragment";
	private View view;
	private RelativeLayout rlTitle, rlNomal, rlClassify;
	private ImageButton ibBack, ibSend, ibSearch;
	private Button btnClassify;
	private EditText etSearch;
	private MainActivity activity;
	private XListView xlvEvent;
	private ListView lvClassify;
	private EventAdapter eventAdapter;
	private String[] classifyData;
	public static List<EventInfo> events = new ArrayList<EventInfo>();
	private EventInfoList eventInfoList;
	private int pageindex = 1;
	private int selPageIndex = 1;
	private int pagesize = 10;
	private int FLAG = -1;
	private boolean isShowClassify;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_baoluo_event, container,
				false);
		activity = (MainActivity) getActivity();
		initUI();
		initData();
		return view;
	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
	}

	private void initUI() {
		// headView = (HeadView) view.findViewById(R.id.hv_head);
		// headView.setHeadViewListener(this);
		rlTitle = (RelativeLayout) view.findViewById(R.id.rl_event_title);
		rlNomal = (RelativeLayout) view.findViewById(R.id.rl_event_nomal);
		rlClassify = (RelativeLayout) view.findViewById(R.id.rl_event_classify);
		ibBack = (ImageButton) view.findViewById(R.id.ib_title_left);
		ibSend = (ImageButton) view.findViewById(R.id.ib_title_right);
		ibSearch = (ImageButton) view.findViewById(R.id.ib_event_search);
		btnClassify = (Button) view.findViewById(R.id.btn_title_title);
		etSearch = (EditText) view.findViewById(R.id.et_event_search);

		ibSearch.setOnClickListener(this);
		ibBack.setOnClickListener(this);
		ibSend.setOnClickListener(this);
		btnClassify.setOnClickListener(this);
		lvClassify = (ListView) view.findViewById(R.id.lv_event_classify);
		lvClassify.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				T.showShort(getActivity(), classifyData[position]);
				// TODO 分类请求数据 并刷新数据
				btnClassify.setSelected(false);
				rlClassify.setVisibility(View.GONE);
				rlNomal.setVisibility(View.VISIBLE);
			}
		});

		xlvEvent = (XListView) view.findViewById(R.id.xlv_eventlist);
		xlvEvent.setDivider(null);
		xlvEvent.setPullLoadEnable(true);
		xlvEvent.setPullRefreshEnable(true);
		xlvEvent.setXListViewListener(this);
		xlvEvent.setOnScrollListener(this);
		xlvEvent.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				EventInfo event = events.get(position - 1);
				Intent intent = new Intent();
				intent.setClass(activity, EventDetailsActivity.class);
				Bundle bundle = new Bundle();
				if (event != null) {
					bundle.putSerializable("eventInfo", event);
					bundle.putInt("eventPageNo", position - 1);
					intent.putExtras(bundle);
				}
				startActivity(intent);
			}
		});
	}

	private void initData() {
		eventAdapter = new EventAdapter(activity, events, R.layout.event_item);
		xlvEvent.setAdapter(eventAdapter);
		getEventList(true);
		classifyData = new String[] { "美食", "培训", "课程", "体育", "生活", "文化", "运动",
				"科技", "音乐", "娱乐", "其他" };
		List<String> data = new ArrayList<String>();
		data = Arrays.asList(classifyData);
		lvClassify.setAdapter(new EventClassifyAdapter(getActivity(), data,
				R.layout.event_classify_item));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_title_left:
			activity.showFragment(MainActivity.FRAMENT_BAOLUO);
			break;
		case R.id.ib_title_right:
			startActivity(new Intent(getActivity(), AtyActivitySend.class));
			break;
		case R.id.ib_event_search:
			T.showShort(getActivity(), "Search");
			break;
		case R.id.btn_title_title:
			btnClassify.setSelected(!btnClassify.isSelected());
			if(btnClassify.isSelected()){
				rlNomal.setVisibility(View.GONE);
				rlClassify.setVisibility(View.VISIBLE);
			}else{
				rlNomal.setVisibility(View.VISIBLE);
				rlClassify.setVisibility(View.GONE);
			}
			break;
		}
	}

	/**
	 * 根据分类标识 加载更多数据
	 * 
	 * @param flag
	 */
	private void loadMoreClassify(final int flag) {
		new GetTask(UrlHelper.EVENT_LIST_CLASSIFY,
				new UpdateViewHelper.UpdateViewListener(EventInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						eventInfoList = (EventInfoList)obj;
						if (eventInfoList.getItems().size() > 0) {
							events.addAll(eventInfoList.getItems());
							eventAdapter.notifyDataSetChanged();
						} else {
							T.showShort(getActivity(), "没有更多数据");
						}
						FLAG = flag;
						xlvEvent.onLoadFinsh();
					}
				}, "Pageindex", String.valueOf(++selPageIndex), "Pagesize",
				String.valueOf(pagesize), "sort", String.valueOf(""),
				"options", String.valueOf(""));
	}

	/**
	 * 获取第一页的分类列表
	 */
	private void getEventClassify(final int flag) {
		selPageIndex = 1;
		new GetTask(UrlHelper.EVENT_LIST_CLASSIFY,
				new UpdateViewHelper.UpdateViewListener(EventInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						eventInfoList = (EventInfoList)obj;
						if (eventInfoList.getItems().size() > 0) {
							events.clear();
							events.addAll(eventInfoList.getItems());
							eventAdapter.notifyDataSetChanged();
						}
						FLAG = flag;
					}
				}, "Pageindex", String.valueOf(pageindex), "Pagesize",
				String.valueOf(pagesize), "sort", String.valueOf(""),
				"options", String.valueOf(""));
	}

	@Override
	public void onRefresh() {
		pageindex = 1;
		selPageIndex = 1;
		getEventList(true);
	}

	@Override
	public void onLoadMore() {
		switch (FLAG) {
		case 0:
		case 1:
		case 2:
		case 3:
			loadMoreClassify(FLAG);
			break;
		default:
			++pageindex;
			getEventList(false);
			break;
		}
	}

	private void getEventList(final boolean isReflesh) {
		new GetTask(UrlHelper.EVENT_LIST,
				new UpdateViewHelper.UpdateViewListener(EventInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						EventInfoList eventInfoList = (EventInfoList)obj;
						if (eventInfoList.getItems() == null
								|| eventInfoList.getItems().size() == 0) {
							T.showShort(getActivity(), "后台没有返回数据！");
							return;
						}
						List<EventInfo> list = eventInfoList.getItems();
						if (isReflesh) {
							events.clear();
						}
						events.addAll(list);
						eventAdapter.notifyDataSetChanged();
						xlvEvent.onLoadFinsh();
					}
				}, "Pageindex", pageindex + "", "Pagesize", pagesize + "");
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// 判断当前最上面显示的是不是头布局，因为Xlistview有刷新控件，所以头布局的位置是1，即第二个
		if (firstVisibleItem == 1) {
			// 获取头布局
			View head = xlvEvent.getChildAt(0);
			if (head != null) {
				// 获取头布局现在的最上部的位置的相反数
				int top = -view.getTop();
				// 获取头布局的高度
				int headerHeight = view.getHeight();
				// 满足这个条件的时候，是头布局在XListview的最上面第一个控件的时候，只有这个时候，我们才调整透明度
				if (top <= headerHeight && top >= 0) {
					// 获取当前位置占头布局高度的百分比
					float f = (float) top / (float) headerHeight;
					rlTitle.getBackground().setAlpha((int) (f * 255));
					// 通知标题栏刷新显示
					rlTitle.invalidate();
				}
			}
		} else if (firstVisibleItem > 1) {
			rlTitle.getBackground().setAlpha(255);
		} else {
			rlTitle.getBackground().setAlpha(0);
		}

	}
}
