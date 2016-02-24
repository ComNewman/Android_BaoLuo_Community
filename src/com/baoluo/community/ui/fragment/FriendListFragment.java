package com.baoluo.community.ui.fragment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.baoluo.community.ui.customview.SlideCutListView;
import com.baoluo.community.ui.customview.SlideCutListView.SlideCutViewListener;
import com.baoluo.community.ui.customview.SlideCutView;
import com.baoluo.community.ui.customview.SlideCutView.OnSlideListener;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.L;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.event.NotifyContactUpdate;
import com.baoluo.im.entity.ContactSearch;
import com.baoluo.im.entity.ContactsInfoList;
import com.baoluo.im.ui.AtyAddFriend;
import com.baoluo.im.ui.AtyChatMqtt;
import com.baoluo.im.ui.AtyIMSearch;
import com.baoluo.im.ui.adapter.ContactsAdapter;
import com.baoluo.im.util.CharacterParser;
import com.baoluo.im.util.PopupWindowHelp;

import de.greenrobot.event.EventBus;

/**
 * 通讯录
 */
@SuppressLint("NewApi")
public class FriendListFragment extends Fragment implements OnClickListener,
		SlideCutViewListener, OnSlideListener, HeadViewListener {

	private static final String TAG = "FriendListFragment";

	private View view;
	private HeadView headView;
	private SlideCutListView lvList;
	private SideBar sbBar;
	private TextView tvDialog;
	private RelativeLayout rlSearch;
	private SlideCutView scv;
	
	private CharacterParser characterParser;
	private List<ContactSearch> list;
	private ContactsAdapter adapter;
	private MyClickListener linstener = new MyClickListener() {
		@Override
		public void myOnClick(int position, View v) {
			switch (v.getId()) {
			case R.id.ll_contacts_attend:
				((MainActivity) getActivity())
						.showFragment(MainActivity.FRAGMENT_ATTEND);
				break;
			case R.id.ll_contacts_tribe:
				((MainActivity) getActivity())
						.showFragment(MainActivity.FRAGMENT_TRIBE);
				break;
			default:
				break;
			}
		}
	};

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		view = inflater.inflate(R.layout.fragment_friend, container, false);
		initUI();
		initData();
		return view;
	}
	@Override
	public void onResume() {
		L.i(TAG, "onResume initData");
		super.onResume();
	}
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(NotifyContactUpdate event) {
		L.i(TAG, "updateContactUpdate   ");
		list.clear();
		list.addAll(initData(ContactSearch.listSwitch(ContactsHelper
				.getInstance().getAllContacts())));
		adapter.notifyDataSetChanged();
	}

	private void initUI() {
		sbBar = (SideBar) view.findViewById(R.id.sb_side_bar);
		tvDialog = (TextView) view.findViewById(R.id.tv_dialog);
		headView = (HeadView) view.findViewById(R.id.hv_friend_head);
		rlSearch = (RelativeLayout) view.findViewById(R.id.rl_search);
		lvList = (SlideCutListView) view.findViewById(R.id.lv_friend_list);
		lvList.setSlideCutViewListener(this);
		rlSearch.setOnClickListener(this);
		headView.setHeadViewListener(this);
	}

	private void initData() {
		characterParser = CharacterParser.getInstance();
		list = ContactSearch.listSwitch(ContactsHelper.getInstance()
				.getAllContacts());
		list = initData(list);
		list = new ArrayList<ContactSearch>();
		adapter = new ContactsAdapter(list, getActivity(), this, linstener);
		lvList.setAdapter(adapter);
		if (list.size() == 0) {
			getData();
			L.i(TAG, " get contacts from servers");
		}
		sbBar.setTextView(tvDialog);
		sbBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {
			@Override
			public void onTouchingLetterChanged(String s) {
				int position = ContactSearch.getPositionForSection(list,
						s.charAt(0));
				if (position != -1) {
					lvList.setSelection(position);
				}
			}
		});
		lvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (position > 0) {
					ContactSearch cs = list.get(position - 1);
					if (cs.isCatalog()) {
						return;
					}
					SlideCutView slideView = cs.getSlideCutView();
					if (slideView.ismIsMoveClick()) {
						return;
					}
					if (slideView.close() && slideView.getScrollX() == 0) {
						if (scv == null || scv.getScrollX() == 0) {
							Intent i = new Intent();
							i.setClass(getActivity(), AtyChatMqtt.class);
							Bundle bundle = new Bundle();
							bundle.putSerializable("contact",
									ContactSearch.objSwitch(cs));
							i.putExtras(bundle);
							startActivity(i);
						}
					}
				}
			}
		});
	}

	private void getData() {
		new GetTask(UrlHelper.CONTACTS,
				new UpdateViewHelper.UpdateViewListener(ContactsInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						ContactsInfoList cl = (ContactsInfoList)obj;
						list.clear();
						list.addAll(initData(ContactSearch.listSwitch(cl
								.getItems())));
						ContactsHelper.getInstance().insertContacts(
								cl.getItems());
						Collections.sort(list, new ContactSearch());
						adapter.notifyDataSetChanged();
					}
				});
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		L.i(TAG, "requestCode :" + requestCode);
		if (resultCode == getActivity().RESULT_OK) {
			switch (requestCode) {
			case PopupWindowHelp.SCANNIN_GREQUEST_CODE:
				Bundle bundle = data.getExtras();
				String url = bundle.getString("result");
				L.i(TAG, bundle.getString("result"));
				Intent intent = new Intent();
				intent.setAction("android.intent.action.VIEW");
				Uri content_url = Uri.parse(url);
				intent.setData(content_url);
				startActivity(intent);
				break;
			default:
				break;
			}
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

	private List<ContactSearch> initData(List<ContactSearch> list) {
		List<ContactSearch> mSortContacts = new ArrayList<ContactSearch>();
		for (int i = 0; i < list.size(); i++) {
			ContactSearch contact = list.get(i);
			String pinyin = characterParser
					.getSelling(contact.getDisplayName());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				contact.setSortLetters(sortString.toUpperCase());
			} else {
				contact.setSortLetters("#");
			}
			mSortContacts.add(contact);
		}
		Collections.sort(mSortContacts, new ContactSearch());
		return ContactSearch.DataInit(mSortContacts);
	}

	@Override
	public void leftListener() {

	}

	@Override
	public void rightListener() {
		startActivity(new Intent(getActivity(), AtyAddFriend.class));
		/*new PopupWindowHelp(getActivity()).showPopupWindow(
				headView.getRightView(), 20);*/
	}

	@Override
	public SlideCutView getSlideCutView(int position) {
		return list.get(position - 1).getSlideCutView();
	}

	@Override
	public void onSlide(View view, int status) {
		if (scv != null && scv != view) {
			scv.shrink();
		}
		if (status == SLIDE_STATUS_ON) {
			scv = (SlideCutView) view;
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_search:
			startActivity(new Intent(getActivity(),AtyIMSearch.class));
			break;
		default:
			break;
		}
	}
}
