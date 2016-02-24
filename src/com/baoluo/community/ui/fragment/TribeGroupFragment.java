package com.baoluo.community.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.baoluo.community.core.InitDatas;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.MainActivity.MyTouchListener;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.SideBar;
import com.baoluo.community.ui.customview.SideBar.OnTouchingLetterChangedListener;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.GroupInfoList;
import com.baoluo.im.ui.AtyMultiChatMqtt;
import com.baoluo.im.ui.adapter.GroupAdapter;
import com.baoluo.im.util.CharacterParser;

import de.greenrobot.event.EventBus;

/**
 * 我的部落（群）
 * 
 * @author tao.lai
 * 
 */
@SuppressLint("NewApi")
public class TribeGroupFragment extends Fragment implements HeadViewListener {
	private static final String TAG = "TribeGroupFragment";

	private View view;
	private HeadView headView;
	private ListView lvList;
	private SideBar sbBar;
	private TextView tvDialog;
	private EditText etSearch;

	private CharacterParser characterParser;
	private List<GroupInfo> list;
	private GroupAdapter adapter;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		view = inflater.inflate(R.layout.fragment_my_attend, container, false);
		initUI();
		initData();
		return view;
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	 @Override
	public void onResume() {
		super.onResume();
		onEventMainThread(null);
	}
	
	public void onEventMainThread(NotifyGroupUpdate event) {
		L.i(TAG, "updategroupUpdate   ");
		list.clear();
		list.addAll(filledData(GroupHelper.getInstance().getAllGroups()));
		Collections.sort(list, new GroupInfo());
		adapter.notifyDataSetChanged();
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initUI() {
		headView = (HeadView) view.findViewById(R.id.hv_head);
		lvList = (ListView) view.findViewById(R.id.lv_attend_list);

		headView.setHeadViewListener(this);
		headView.setHeadTitle("我的群组");

		sbBar = (SideBar) view.findViewById(R.id.attend_sb_side_bar);
		tvDialog = (TextView) view.findViewById(R.id.tv_attend_dialog);
		etSearch = (EditText) view.findViewById(R.id.et_attend_search);

		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}

			@SuppressLint("DefaultLocale")
			@Override
			public void afterTextChanged(Editable s) {
				Editable editable = etSearch.getText();
				if (editable.length() > 0) {
					String pinyin = characterParser.getSelling(editable
							.toString());
					String sortString = pinyin.substring(0, 1).toUpperCase();
					int position = adapter.getPositionForSection(sortString
							.charAt(0));
					if (position != -1) {
						lvList.setSelection(position);
					} else {
						T.showShort(getActivity(), "没有这个这个群组");
					}
				}
			}
		});
	}

	private void initData() {
		characterParser = CharacterParser.getInstance();
		list = GroupHelper.getInstance().getAllGroups();
		list = filledData(list);
		Collections.sort(list, new GroupInfo());
		adapter = new GroupAdapter(getActivity(), list, R.layout.im_list_item);
		lvList.setAdapter(adapter);
		if (list.size() == 0) {
			getGroupData();
		}
		sbBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					lvList.setSelection(position);
				}
			}
		});
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				int groupType = list.get(position).getGroupType();
				Intent i = new Intent();
				i.putExtra(AtyMultiChatMqtt.EXTRA_GROUP, list.get(position));
				//i.putExtra(AtyMultiChatMqtt.GROUP_TYPE, groupType);
				i.setClass(getActivity(), AtyMultiChatMqtt.class);
				startActivity(i);
				T.showShort(getActivity(), list.get(position).getSortLetters());
			}
		});
	}

	private void getGroupData() {
		new GetTask(UrlHelper.MY_GROUP_LIST,
				new UpdateViewHelper.UpdateViewListener(GroupInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						GroupInfoList groupInfoList = (GroupInfoList)obj;
						InitDatas.getInstance().subscibeGroups(groupInfoList.getItems());
						list.addAll(filledData(groupInfoList.getItems()));
						Collections.sort(list, new GroupInfo());
						adapter.notifyDataSetChanged();
					}
				});
	}

	@SuppressLint("DefaultLocale")
	private List<GroupInfo> filledData(List<GroupInfo> list) {
		List<GroupInfo> mSortList = new ArrayList<GroupInfo>();
		for (int i = 0; i < list.size(); i++) {
			GroupInfo group = list.get(i);
			String pinyin = characterParser.getSelling(group.getName());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				group.setSortLetters(sortString.toUpperCase());
			} else {
				group.setSortLetters("#");
			}
			mSortList.add(group);
		}
		return mSortList;
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
		((MainActivity) getActivity())
				.showFragment(MainActivity.FRAMENT_CONTACTS);
	}

	@Override
	public void rightListener() {
		// new PopupWindowHelp(getActivity()).showPopupWindow(btnAdd, 20);
	}
}