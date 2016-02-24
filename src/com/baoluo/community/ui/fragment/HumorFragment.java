package com.baoluo.community.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.entity.HumorInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyHumorSend;
import com.baoluo.community.ui.HumorDetailsActivity;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.HumorListAdapter;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.HumorClassifyWindow;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;

@SuppressLint("NewApi")
/**
 * 心情页面
 * @author xiangyang.fu
 *
 */
public class HumorFragment extends Fragment implements OnClickListener,
		IXListViewListener, HeadViewListener {
	private static final String TAG = "HumorFragment";

	// private HeadView headView;
	private View view;
	private XListView xlView;

	public static List<HumorInfo> humors = new ArrayList<HumorInfo>(); // 为了心情详情能访问到这个数据(翻页数据展现)，改成public
	private HumorListAdapter humorListAdapter;
	private HumorInfoList humorInfoList;
	private Button btnClassify;
	private RelativeLayout rlTitle, rlClassify;
	private ImageButton ibBack, ibSend, ibShare;
	private TextView tvPraise;
	private boolean praiseFlag;
	private MainActivity activity;
	private HumorClassifyWindow window;
	private int pageNo = 1;
	private int pageSize = 20;
	private int selPageNo = 1;
	private int options = 0;

	// private int selectedItem = -1;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		view = inflater.inflate(R.layout.fragment_baoluo_humor, container,
				false);
		activity = (MainActivity) getActivity();
		initUI();
		initData();
		return view;
	}

	@Override
	public void onResume() {
		super.onResume();

	}

	@Override
	public void onHiddenChanged(boolean hidden) {
		super.onHiddenChanged(hidden);
		MainActivity ma = (MainActivity) getActivity();
		L.i(TAG, "onHiddenChanged" + "         " + ma.getCurrentFragment());
	}

	private void initUI() {
		rlTitle = (RelativeLayout) view.findViewById(R.id.rl_humor_title);
		btnClassify = (Button) view.findViewById(R.id.btn_title_title);
		ibBack = (ImageButton) view.findViewById(R.id.ib_title_left);
		ibSend = (ImageButton) view.findViewById(R.id.ib_title_right);
		ibBack.setOnClickListener(this);
		ibSend.setOnClickListener(this);
		btnClassify.setOnClickListener(this);
		xlView = (XListView) view.findViewById(R.id.xv_list);

		// headView = (HeadView) view.findViewById(R.id.hv_head);
		// headView.setHeadViewListener(this);
		// headView.setOnClickListener(this);

		xlView.setPullLoadEnable(true);
		xlView.setPullRefreshEnable(true);
		xlView.setXListViewListener(this);

		xlView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				L.i(TAG, "点击了：" + position);
				// selectedItem = position;
				Intent intent = new Intent();
				intent.setClass(getActivity(), HumorDetailsActivity.class);
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
		window = new HumorClassifyWindow(getActivity());
		window.setOnDismissListener(new OnDismissListener() {

			@Override
			public void onDismiss() {
				btnClassify.setSelected(false);
				options = window.getHot() + window.getSex() + window.getRange();
				T.showShort(getActivity(), "" + options);
				getHumorClassify();
			}
		});
	}

	private void initData() {

		humorListAdapter = new HumorListAdapter(getActivity(), humors,
				R.layout.humor_list_item);
		xlView.setAdapter(humorListAdapter);

		new GetTask(UrlHelper.HUMOR_LIST,
				new UpdateViewHelper.UpdateViewListener(HumorInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						humorInfoList = (HumorInfoList)obj;
						if (humorInfoList.getItems() != null
								&& humorInfoList.getItems().size() > 0) {
							humors.clear();
							humors.addAll(humorInfoList.getItems());
							humorListAdapter.notifyDataSetChanged();
							// xlView.setSelection(selectedItem);
						}
					}
				}, "Pageindex", String.valueOf(selPageNo), "Pagesize",
				String.valueOf(pageSize), "options", String.valueOf(options));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.ib_title_left:
			activity.showFragment(MainActivity.FRAMENT_BAOLUO);
			break;
		case R.id.ib_title_right:
			startActivity(new Intent(getActivity(), AtyHumorSend.class));
			break;
		case R.id.btn_title_title:
			btnClassify.setSelected(!btnClassify.isSelected());
			window.showPopupWindow(rlTitle);
			break;
		default:
			break;
		}
	}

	/**
	 * 获取分类心情
	 */
	private void getHumorClassify() {
		selPageNo = 1;
		new GetTask(UrlHelper.HUMOR_LIST,
				new UpdateViewHelper.UpdateViewListener(HumorInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						humors.clear();
						humorInfoList = (HumorInfoList)obj;
						humors.addAll(humorInfoList.getItems());
						humorListAdapter.notifyDataSetChanged();
					}
				}, "Pageindex", String.valueOf(selPageNo), "Pagesize",
				String.valueOf(pageSize), "options", String.valueOf(options));
	}

	/**
	 * 分类加载更多
	 *
	 */
	private void loadMoreClassify() {
		new GetTask(UrlHelper.HUMOR_LIST,
				new UpdateViewHelper.UpdateViewListener(HumorInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						humorInfoList = (HumorInfoList)obj;
						if (humorInfoList.getItems().size() > 0) {
							humors.addAll(humorInfoList.getItems());
							humorListAdapter.notifyDataSetChanged();
						} else {
							T.showShort(getActivity(), "没有更多数据");
						}
						xlView.onLoadFinsh();
					}
				}, "Pageindex", String.valueOf(++selPageNo), "Pagesize",
				String.valueOf(pageSize), "options", String.valueOf(options));
	}

	@Override
	public void onRefresh() {
		pageNo = 1;
		selPageNo = 1;
		new GetTask(UrlHelper.HUMOR_LIST,
				new UpdateViewHelper.UpdateViewListener(HumorInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						humors.clear();
						humorInfoList = (HumorInfoList)obj;
						humors.addAll(humorInfoList.getItems());
						humorListAdapter.notifyDataSetChanged();
						xlView.onLoadFinsh();
					}
				}, "Pageindex", String.valueOf(selPageNo), "Pagesize",
				String.valueOf(pageSize), "options", String.valueOf(options));
	}

	@Override
	public void onLoadMore() {
		loadMoreClassify();
	}

	@Override
	public void leftListener() {
		((MainActivity) getActivity())
				.showFragment(MainActivity.FRAMENT_BAOLUO);
	}

	@Override
	public void rightListener() {
		startActivity(new Intent(getActivity(), AtyHumorSend.class));
	}

}
