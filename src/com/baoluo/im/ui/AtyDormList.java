package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.format.Time;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.DormInfo;
import com.baoluo.community.entity.DormInfoList;
import com.baoluo.community.entity.DormUser;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.AtyDormCreate;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.event.DormUserChangeEvent;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.entity.AttendDorm;
import com.baoluo.im.entity.AttendDormQueue;
import com.baoluo.im.ui.adapter.DormPagerAdapter;
import com.baoluo.im.ui.adapter.DormQueueAdapter;
import com.baoluo.im.ui.adapter.DornAttendUserAdapter;
import com.baoluo.im.util.DepthPageTransformer;
import com.baoluo.im.util.DormQueueCache;

import de.greenrobot.event.EventBus;

/**
 * 宿舍列表
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyDormList extends AtyBase implements OnPageChangeListener,
		OnClickListener {
	private static final String TAG = "AtyDormList";
	private LinearLayout queue;
	private RelativeLayout container;
	private LinearLayout llBottom;
	private ViewPager viewPager;
	private DormPagerAdapter adapter;
	private ImageButton ibBack, ibSend, ibMore, ibNomal;
	private Button btnSingle, btnGroup, btnBackToDorm, btnExitChatDorm;
	private TextView tvSingle, tvGroup;
	private ListView lv;
	private DormQueueAdapter queueAdapter;
	private List<DormInfo> dormInfos;
	private List<AttendDorm> queueDorms;
	private AttendDormQueue queueData;
	private DormInfoList dorms;
	private DormInfo dorm;
	private int _yDelta;
	private int pageIndex = 1;
	private int pageSize = 20;
	private int curPage, preState;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		EventBus.getDefault().register(this);
		setContentView(R.layout.aty_dorm_new);
		initUI();
		getRandomNick();
		initData();
	}

	@Override
	protected void onResume() {
		getChatDorm();
		super.onResume();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(DormUserChangeEvent event) {
		L.i(TAG, "DormUserChangeEvent:"+GsonUtil.getInstance().obj2Str(event));
		if(event != null){
			refreshData(event);
		}
	}

	private void getRandomNick() {
		Time t = new Time();
		t.setToNow();
		int lastmonth = t.month + 1;
		String todayTime = t.year + "年" + lastmonth + "月" + t.monthDay + "日";
		if (!SettingUtility.getTodayTime().equals(todayTime)) {
			startActivity(new Intent(AtyDormList.this,AtyDormGetNick.class));
			SettingUtility.setTodayTime(todayTime);
		}
	}

	private void initData() {
		queueDorms = new ArrayList<AttendDorm>();
		queueAdapter = new DormQueueAdapter(this, queueDorms,
				R.layout.item_dorm_lv_queue);
		lv.setDivider(null);
		lv.setAdapter(queueAdapter);
		dormInfos = new ArrayList<DormInfo>();
		selectData(1);
	}

	private void refreshData(DormUserChangeEvent event) {
		L.i(TAG, "atydormlist   refreshData");
		for (int i = 0; i < dormInfos.size(); i++) {
			if (dormInfos.get(i).getId().equals(event.getDormId())) {
				DormInfo refreshDorm = dormInfos.get(i);
				List<DormUser> users = (List<DormUser>) refreshDorm.getUsers();
//				L.i(TAG, "atydormlist   users.size():   " + (users.size()));
//				L.i(TAG,
//						"event.getOptionType():          "
//								+ event.getOptionType());
//				L.i(TAG, "event.getUserInfo():          "
//						+ GsonUtil.getInstance().obj2Str(event.getUserInfo()));
				switch (event.getOptionType()) {
				case 1:
					users.add(event.getUserInfo());
					if (event.getUserInfo().getId()
							.equals(SettingUtility.getUserSelf().getId())) {
						refreshDorm.setIsAttend(true);
					}
					refreshAttendUser(i, refreshDorm, users);
					break;
				case 2:
					if(users.size()>0){
						for (DormUser dormUser : users) {
							if (dormUser.getId()
									.equals(event.getUserInfo().getId())) {
								users.remove(dormUser);
							}
						}
						
					}
					if (event.getUserInfo().getId()
							.equals(SettingUtility.getUserSelf().getId())) {
						refreshDorm.setIsAttend(false);
					}
					refreshAttendUser(i, refreshDorm, users);
					break;
				case 3:
					TextView tvTime = (TextView) viewPager.findViewWithTag("tv"+i);
					refreshDorm.setStatus(1);
					tvTime.setText(refreshDorm.getShowTime());
					dormInfos.set(i, refreshDorm);
					adapter.notifyDataSetChanged();
					break;
				}
			}
		}
	}

	private void refreshAttendUser(int i, DormInfo refreshDorm,
			List<DormUser> users) {
		refreshDorm.setUsers(users);
		dormInfos.set(i, refreshDorm);
		GridView gv = (GridView) viewPager.findViewWithTag("gv" + i);
		TextView tvTime = (TextView) viewPager.findViewWithTag("tv" + i);
		if(tvTime!=null){
			if(users.size()>0&&refreshDorm.getStatus()==0){
				tvTime.setText("正在排队中");
			}else{
				tvTime.setText(refreshDorm.getShowTime());
			}
		}
		
		DornAttendUserAdapter userAdapter = new DornAttendUserAdapter(
				AtyDormList.this, users, refreshDorm);
		gv.setAdapter(userAdapter);
		userAdapter.notifyDataSetChanged();
	}

	private void getChatDorm() {
		new GetTask(UrlHelper.DORM_CHAT_NOW,
				new UpdateViewHelper.UpdateViewListener(DormInfo.class) {
					@Override
					public void onFail() {
						SettingUtility.setDormId("");
					}

					@Override
					public void onComplete(Object obj) {
						DormInfo chatDorm = (DormInfo)obj;
						if (chatDorm != null) {
							SettingUtility.setDormId(chatDorm.getId());
							llBottom.setVisibility(View.VISIBLE);
						} else {
							llBottom.setVisibility(View.GONE);
						}
					}
				});
	}

	private void getQueue() {
		new GetTask(UrlHelper.DORM_ATTEND_QUEUE,
				new UpdateViewHelper.UpdateViewListener(AttendDormQueue.class) {
					@Override
					public void onComplete(Object obj) {
						queueDorms.clear();
						queueData = (AttendDormQueue)obj;
						queueDorms.addAll(queueData.getItems());
						DormQueueCache.getInstance().refleshQueue(
								queueData.getItems());
						queueAdapter.notifyDataSetChanged();
					}
				});
	}

	private OnTouchListener nomalOntouch = new OnTouchListener() {

		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			L.i(TAG, "nomalOntouch");
			final int Y = (int) event.getRawY();
			switch (event.getAction() & MotionEvent.ACTION_MASK) {
			case MotionEvent.ACTION_DOWN:
				RelativeLayout.LayoutParams lParams = (RelativeLayout.LayoutParams) v
						.getLayoutParams();
				_yDelta = Y - lParams.topMargin;
				break;
			case MotionEvent.ACTION_UP:
				if (Y - _yDelta < -120) {
					move(Y - _yDelta);
				} else {
					restoreNomal(v);
				}
				break;
			case MotionEvent.ACTION_POINTER_DOWN:
				break;
			case MotionEvent.ACTION_POINTER_UP:
				break;
			case MotionEvent.ACTION_MOVE:
				RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) v
						.getLayoutParams();
				layoutParams.topMargin = Y - _yDelta;
				layoutParams.bottomMargin = _yDelta - Y;
				v.setLayoutParams(layoutParams);
				break;
			}
			v.invalidate();
			return true;
		}
	};

	private void restoreNomal(View v) {
		RelativeLayout.LayoutParams upParams = (RelativeLayout.LayoutParams) v
				.getLayoutParams();
		upParams.topMargin = 0;
		upParams.bottomMargin = 0;
		upParams.rightMargin = 0;
		upParams.leftMargin = 0;
		v.setLayoutParams(upParams);
	}

	@SuppressLint("ClickableViewAccessibility")
	private void initUI() {
		llBottom = (LinearLayout) findViewById(R.id.ll_dorm_list_buttom);
		container = (RelativeLayout) findViewById(R.id.rl_dorm_list_nomal);
		queue = (LinearLayout) findViewById(R.id.rl_dorm_list_queue);
		container.setOnTouchListener(nomalOntouch);
		btnSingle = (Button) findViewById(R.id.btn_dorm_list_single);
		btnGroup = (Button) findViewById(R.id.btn_dorm_list_group);
		btnSingle.setOnClickListener(this);
		btnGroup.setOnClickListener(this);

		lv = (ListView) findViewById(R.id.lv_dorm_list_queue);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				new GetTask(UrlHelper.DORM_CURRENT,
						new UpdateViewHelper.UpdateViewListener(DormInfo.class) {
							@Override
							public void onComplete(Object obj) {
								removeQueue();
								dormInfos.clear();
								dorm = (DormInfo)obj;
								dormInfos.add(dorm);
								adapter.notifyDataSetChanged();
							}
						}, "Id", queueDorms.get(position).getId());

			}
		});
		ibMore = (ImageButton) findViewById(R.id.ib_dorm_list_up);
		ibMore.setOnClickListener(this);
		ibNomal = (ImageButton) findViewById(R.id.ib_dorm_list_down);
		ibNomal.setOnClickListener(this);
		tvSingle = (TextView) findViewById(R.id.dorm_list_flag_single);
		tvGroup = (TextView) findViewById(R.id.dorm_list_flag_group);
		ibBack = (ImageButton) findViewById(R.id.btn_dorm_back);
		ibSend = (ImageButton) findViewById(R.id.btn_dorm_send);
		ibBack.setOnClickListener(this);
		ibSend.setOnClickListener(this);

		btnBackToDorm = (Button) findViewById(R.id.btn_dorm_list_back_dorm);
		btnBackToDorm.setOnClickListener(this);
		btnExitChatDorm = (Button) findViewById(R.id.btn_dorm_list_out);
		btnExitChatDorm.setOnClickListener(this);
		viewPager = (ViewPager) findViewById(R.id.view_pager);
		viewPager.setPageTransformer(true, new DepthPageTransformer());
		viewPager.setOnPageChangeListener(this);
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (preState == 1 && arg0 == 0 && curPage == 0) {
			T.showShort(AtyDormList.this, "第一页左滑！");
		} else if (preState == 1 && arg0 == 0
				&& curPage == dormInfos.size() - 1) {
			T.showShort(AtyDormList.this, "最后一页右滑！");
		}
		preState = arg0;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		curPage = arg0;
	}

	@Override
	public void onPageSelected(int arg0) {

	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.btn_dorm_back:
			onBackPressed();
			break;
		case R.id.btn_dorm_send:
			intent.setClass(AtyDormList.this, AtyDormCreate.class);
			startActivity(intent);
			break;
		case R.id.btn_dorm_list_single:
			tvGroup.setVisibility(View.GONE);
			tvSingle.setVisibility(View.VISIBLE);
			selectData(1);
			break;
		case R.id.btn_dorm_list_group:
			tvGroup.setVisibility(View.VISIBLE);
			tvSingle.setVisibility(View.GONE);
			selectData(2);
			break;
		case R.id.btn_dorm_list_back_dorm:
			if (!StrUtils.isEmpty(SettingUtility.getDromId())) {
				intent.setClass(AtyDormList.this, AtyDormChat.class);
				intent.putExtra(AtyDormChat.DORM_ID, SettingUtility.getDromId());
				startActivity(intent);
			} else {
				L.i(TAG, "SettingUtility.getDromId() is empty");
			}
			break;
		case R.id.btn_dorm_list_out:
			new PostTask(UrlHelper.DORM_EXIT, new UpdateViewListener() {
				@Override
				public void onComplete(Object obj) {
					MqttHelper.getInstance().unSubscribe(
							com.baoluo.im.Configs.MQ_DORM
									+ SettingUtility.getDromId());
					SettingUtility.setDormId("");
					llBottom.setVisibility(View.GONE);
				}
			}, "Id", SettingUtility.getDromId());
			break;
		case R.id.ib_dorm_list_up:
			move(0);
			break;
		case R.id.ib_dorm_list_down:
			removeQueue();
			break;
		default:
			break;
		}
	}

	private void selectData(int sort) {
		new GetTask(UrlHelper.DORM_SELECT_CHAT_TYPE,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onCompleteExecute(String responseStr) {
						if (!Configs.RESPONSE_ERROR.equals(responseStr)) {
							dorms = (DormInfoList) GsonUtil.getInstance()
									.str2Obj(responseStr, DormInfoList.class);
							if (dorms != null && dorms.getItems() != null
									&& dorms.getItems().size() > 0) {
								dormInfos.clear();
								dormInfos.addAll(dorms.getItems());
								adapter = new DormPagerAdapter(dormInfos,
										AtyDormList.this);
								viewPager.setAdapter(adapter);
								adapter.notifyDataSetChanged();
							}
						}
					}
				}, "pageIndex", String.valueOf(pageIndex), "pageSize",
				String.valueOf(pageSize), "sort", String.valueOf(sort));
	}

	public void move(float y) {
		final float showheight = container.getHeight();
		Animation mTranslateAnimation = new TranslateAnimation(0, 0, y,
				-showheight);// 移动

		mTranslateAnimation.setDuration(500);
		mTranslateAnimation
				.setAnimationListener(new Animation.AnimationListener() {
					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
						getQueue();
						container.clearAnimation();
						container.setVisibility(View.GONE);
						queue.setVisibility(View.VISIBLE);
						restoreNomal(container);
					}

					public void onAnimationRepeat(Animation animation) {

					}
				});
		container.startAnimation(mTranslateAnimation);
	}

	private void removeQueue() {
		float showheight = queue.getHeight();
		Animation mTranslateAnimation = new TranslateAnimation(0, 0, 0,
				2 * showheight);// 移动
		mTranslateAnimation.setDuration(500);
		mTranslateAnimation
				.setAnimationListener(new Animation.AnimationListener() {
					public void onAnimationStart(Animation animation) {

					}

					public void onAnimationEnd(Animation animation) {
						queue.clearAnimation();
						queue.setVisibility(View.GONE);
						container.setVisibility(View.VISIBLE);
						restoreNomal(queue);
					}

					public void onAnimationRepeat(Animation animation) {

					}
				});
		queue.startAnimation(mTranslateAnimation);
	}
}
