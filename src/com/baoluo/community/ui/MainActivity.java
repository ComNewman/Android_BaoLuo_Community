package com.baoluo.community.ui;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.core.Configs;
import com.baoluo.community.core.InitDatas;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.customview.BottomMenuView;
import com.baoluo.community.ui.fragment.BLFrg;
import com.baoluo.community.ui.fragment.EventFragment;
import com.baoluo.community.ui.fragment.FriendListFragment;
import com.baoluo.community.ui.fragment.GroveFragment;
import com.baoluo.community.ui.fragment.HumorFragment;
import com.baoluo.community.ui.fragment.MessageFragment;
import com.baoluo.community.ui.fragment.MineFragment;
import com.baoluo.community.ui.fragment.MyAttendFragment;
import com.baoluo.community.ui.fragment.TopicFragment;
import com.baoluo.community.ui.fragment.TribeGroupFragment;
import com.baoluo.community.util.L;
import com.baoluo.community.util.MoreWindow;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.MsgInfoHelper;
import com.baoluo.event.MainUnReadMsgEvent;
import com.baoluo.notify.MsgNotify;

import de.greenrobot.event.EventBus;

/**
 * 主页
 * 
 * @author Ryan_Fu 2015-5-12
 */
@SuppressLint("NewApi")
public class MainActivity extends AtyBaseLBS implements OnClickListener,
		LocationResultListener {
	private static final String TAG = "MainActivity";
	public static final int FRAMENT_MSG = 0x0;
	public static final int FRAMENT_BAOLUO = 0x1;
	public static final int FRAMENT_CONTACTS = 0x2;
	public static final int FRAMENT_MINE = 0x3;
	public static final int FRAMENT_HUMOR = 0x4;
	public static final int FRAMENT_ACTIVITY = 0x5;
	public static final int FRAMENT_TOPIC = 0x6;
	public static final int FRAGMENT_GROVE = 0x7;
	public static final int FRAGMENT_ATTEND = 0x8;
	public static final int FRAGMENT_TRIBE = 0x9;

	private BottomMenuView bvMsg, bvContacts, bvBaoluo, bvMine;
	private ImageView ivCenter;
	private TextView tvUnReadMsg;
	private MoreWindow mMoreWindow;
	public FragmentTransaction ft;
	private BLFrg BLFrg;
	private FriendListFragment friendListFragment;
	private MessageFragment messageFragment;
	private MineFragment mineFragment;
	private MyAttendFragment myAttendFragment;
	private GroveFragment groveFragment;
	private HumorFragment humorFragment;
	private EventFragment eventFragment;
	private TopicFragment topicFragment;
	private TribeGroupFragment tribeGroupFragment;
	private Fragment currentFragment;
	private boolean isLongClicked;
	private int unReadNum = 0;
	private LatLng mLocation;

	public Fragment getCurrentFragment() {
		return currentFragment;
	}

	public void setCurrentFragment(Fragment currentFragment) {
		this.currentFragment = currentFragment;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setLocationResultListener(this);
		EventBus.getDefault().register(this);
		initUI();
		initHomePager();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(MainUnReadMsgEvent event) {
		unReadNum = MsgInfoHelper.getInstance().getUnReadMsg();
		if (unReadNum > 0) {
			tvUnReadMsg.setVisibility(View.VISIBLE);
			tvUnReadMsg.setText(StrUtils.getUnReadNum(unReadNum));
		} else {
			tvUnReadMsg.setVisibility(View.GONE);
			MsgNotify.getInstance().clearMsgNotify();
		}
	}

	private void initHomePager() {
		showFragment(FRAMENT_MSG);
		bvMsg.setSelected(true);
	}

	private void initUI() {
		
		tvUnReadMsg = (TextView) findViewById(R.id.tv_main_msg_num);
		bvMsg = (BottomMenuView) findViewById(R.id.bv_msg);
		bvMsg.setOnClickListener(this);
		bvContacts = (BottomMenuView) findViewById(R.id.bv_contacts);
		bvContacts.setOnClickListener(this);
		bvBaoluo = (BottomMenuView) findViewById(R.id.bv_baoluo);
		bvBaoluo.setOnClickListener(baoluoClickListener);
		bvMine = (BottomMenuView) findViewById(R.id.bv_mine);
		bvMine.setOnClickListener(this);
		ivCenter = (ImageView) findViewById(R.id.iv_main_center);
		ivCenter.setOnClickListener(this);

		UserSelf self = SettingUtility.getUserSelf();
		if (self != null && self.getSex() == 0) {
			bvBaoluo.setBtnBg(R.drawable.selector_btn_home_baoluo_nv);
			bvBaoluo.setTextColor(true);
			bvContacts.setBtnBg(R.drawable.selector_btn_home_contacts_nv);
			bvContacts.setTextColor(true);
			bvMine.setBtnBg(R.drawable.selector_btn_home_mine_nv);
			bvMine.setTextColor(true);
			bvMsg.setBtnBg(R.drawable.selector_btn_home_msg_nv);
			bvMsg.setTextColor(true);
			ivCenter.setImageResource(R.drawable.ic_center_nv);
		}
		ivCenter.setOnLongClickListener(new OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				if (!isLongClicked) {
					bvBaoluo.setBtnName("小树林");
					bvBaoluo.setBtnBg(R.drawable.selector_btn_home_grove);
					bvBaoluo.setOnClickListener(groveClickListener);
					swithGrove(MainActivity.FRAGMENT_GROVE);
					isLongClicked = true;
				} else {
					bvBaoluo.setBtnName("宝落");
					bvBaoluo.setBtnBg(R.drawable.selector_btn_home_baoluo);
					bvBaoluo.setOnClickListener(baoluoClickListener);
					swithGrove(MainActivity.FRAMENT_BAOLUO);
					isLongClicked = false;
				}
				return true;
			}
		});
		unReadNum = MsgInfoHelper.getInstance().getUnReadMsg();
		if (unReadNum > 0) {
			tvUnReadMsg.setVisibility(View.VISIBLE);
			tvUnReadMsg.setText(StrUtils.getUnReadNum(unReadNum));
		} else {
			tvUnReadMsg.setVisibility(View.GONE);
		}
	}

	private void swithGrove(int flag) {
		bvMsg.setSelected(false);
		bvContacts.setSelected(false);
		bvBaoluo.setSelected(true);
		bvMine.setSelected(false);
		showFragment(flag);
	}

	private OnClickListener baoluoClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			swithGrove(MainActivity.FRAMENT_BAOLUO);
		}
	};
	private OnClickListener groveClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			swithGrove(MainActivity.FRAGMENT_GROVE);
		}
	};

	private void initSelected(int type) {
		if (type == FRAMENT_MSG) {
			bvMsg.setSelected(true);
		} else {
			bvMsg.setSelected(false);
		}
		if (type == FRAMENT_CONTACTS) {
			bvContacts.setSelected(true);
		} else {
			bvContacts.setSelected(false);
		}
		if (type == FRAMENT_MINE) {
			bvMine.setSelected(true);
		} else {
			bvMine.setSelected(false);
		}
		bvBaoluo.setSelected(false);
		showFragment(type);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bv_msg:
			initSelected(FRAMENT_MSG);
			break;
		case R.id.bv_contacts:
			initSelected(FRAMENT_CONTACTS);
			break;
		case R.id.bv_mine:
			initSelected(FRAMENT_MINE);
			break;
		case R.id.iv_main_center:
			showMoreWindow(v);
			break;
		default:
			break;
		}
	}

	private void showMoreWindow(View view) {
		if (null == mMoreWindow) {
			mMoreWindow = new MoreWindow(this);
			mMoreWindow.init();
		}
		mMoreWindow.showMoreWindow(view, 0);
		/*
		 * mMoreWindow.setTouchInterceptor(new OnTouchListener() { public
		 * boolean onTouch(View v, MotionEvent event) { if (event.getAction() ==
		 * MotionEvent.ACTION_OUTSIDE) { mMoreWindow.dismiss(); return true; }
		 * return false; } });
		 */
	}

	public void showFragment(int fragmentId) {
		ft = getFragmentManager().beginTransaction();
		// ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
		if (BLFrg != null) {
			ft.hide(BLFrg);
		}
		if (friendListFragment != null) {
			ft.hide(friendListFragment);
		}
		if (groveFragment != null) {
			ft.hide(groveFragment);
		}
		if (messageFragment != null) {
			ft.hide(messageFragment);
		}
		if (mineFragment != null) {
			ft.hide(mineFragment);
		}
		if (humorFragment != null) {
			ft.hide(humorFragment);
		}
		if (eventFragment != null) {
			ft.hide(eventFragment);
		}
		if (topicFragment != null) {
			ft.hide(topicFragment);
		}
		if (myAttendFragment != null) {
			ft.hide(myAttendFragment);
		}
		if (tribeGroupFragment != null) {
			ft.hide(tribeGroupFragment);
		}
		switch (fragmentId) {
		case FRAMENT_MSG:
			if (messageFragment == null) {
				messageFragment = new MessageFragment();
				ft.add(R.id.fragment_all, messageFragment);
			} else {
				ft.show(messageFragment);
			}
			setCurrentFragment(messageFragment);
			break;
		case FRAMENT_CONTACTS:

			if (friendListFragment == null) {
				friendListFragment = new FriendListFragment();
				ft.add(R.id.fragment_all, friendListFragment);
			} else {
				ft.show(friendListFragment);
			}
			setCurrentFragment(friendListFragment);
			break;
		case FRAGMENT_GROVE:
			if (groveFragment == null) {
				groveFragment = new GroveFragment();
				ft.add(R.id.fragment_all, groveFragment);
			} else {
				ft.show(groveFragment);
			}
			setCurrentFragment(groveFragment);
			break;
		case FRAMENT_BAOLUO:
			if (BLFrg == null) {
				BLFrg = new BLFrg();
				ft.add(R.id.fragment_all, BLFrg);
			} else {
				ft.show(BLFrg);
			}
			setCurrentFragment(BLFrg);
			break;
		case FRAMENT_MINE:
			if (mineFragment == null) {
				mineFragment = new MineFragment();
				ft.add(R.id.fragment_all, mineFragment);
			} else {
				ft.show(mineFragment);
			}
			setCurrentFragment(mineFragment);
			break;
		case FRAMENT_HUMOR:
			if (humorFragment == null) {
				humorFragment = new HumorFragment();
				ft.add(R.id.fragment_all, humorFragment);
			} else {
				ft.show(humorFragment);
			}
			setCurrentFragment(humorFragment);
			break;
		case FRAMENT_ACTIVITY:
			if (eventFragment == null) {
				eventFragment = new EventFragment();
				ft.add(R.id.fragment_all, eventFragment);
			} else {
				ft.show(eventFragment);
			}
			setCurrentFragment(eventFragment);
			break;
		case FRAMENT_TOPIC:
			if (topicFragment == null) {
				topicFragment = new TopicFragment();
				ft.add(R.id.fragment_all, topicFragment);
			} else {
				ft.show(topicFragment);
			}
			setCurrentFragment(topicFragment);
			break;
		case FRAGMENT_ATTEND:
			if (myAttendFragment == null) {
				myAttendFragment = new MyAttendFragment();
				ft.add(R.id.fragment_all, myAttendFragment);
			} else {
				ft.show(myAttendFragment);
			}
			setCurrentFragment(myAttendFragment);
			break;
		case FRAGMENT_TRIBE:
			if (tribeGroupFragment == null) {
				tribeGroupFragment = new TribeGroupFragment();
				ft.add(R.id.fragment_all, tribeGroupFragment);
			} else {
				ft.show(tribeGroupFragment);
			}
			setCurrentFragment(tribeGroupFragment);
			break;
		default:
			break;
		}
		ft.commit();
	}

	/*
	 * 保存MyTouchListener接口的列表
	 */
	private ArrayList<MyTouchListener> myTouchListeners = new ArrayList<MainActivity.MyTouchListener>();

	/**
	 * 提供给Fragment通过getActivity()方法来注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void registerMyTouchListener(MyTouchListener listener) {
		myTouchListeners.add(listener);
	}

	/**
	 * 提供给Fragment通过getActivity()方法来取消注册自己的触摸事件的方法
	 * 
	 * @param listener
	 */
	public void unRegisterMyTouchListener(MyTouchListener listener) {
		myTouchListeners.remove(listener);
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			L.i(TAG, "onKeyDown");
		}
		return true;
	}

	/**
	 * 分发触摸事件给所有注册了MyTouchListener的接口
	 */

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		for (MyTouchListener listener : myTouchListeners) {
			listener.onTouchevent(ev);
		}
		return super.dispatchTouchEvent(ev);
	}

	public interface MyTouchListener {
		public void onTouchevent(MotionEvent event);

	}

	private void updateLBS() {
		new PostTask(UrlHelper.LBS_UPDATE_LOCAITON,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
							L.i(TAG, "更新定位  成功");
					}
				}, "lat", String.valueOf(mLocation.latitude), "lng",
				String.valueOf(mLocation.longitude));
	}

	@Override
	public void locationChanged() {
		mLocation = getLatLng();
		L.i(TAG, "定位  成功");
		updateLBS();
	}

	@Override
	public void locationFail() {
		mLocation = new LatLng(Configs.latitude, Configs.longitude);
		L.i(TAG, "定位  失败");
	}
}
