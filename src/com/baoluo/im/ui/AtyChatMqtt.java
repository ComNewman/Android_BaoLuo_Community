package com.baoluo.im.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.ImInputView;
import com.baoluo.community.ui.customview.ImInputView.InputListener;
import com.baoluo.community.ui.customview.SpeekView;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.dao.MsgDb;
import com.baoluo.dao.helper.MsgInfoHelper;
import com.baoluo.event.IMSendMsgEvent;
import com.baoluo.event.MsgEvent;
import com.baoluo.event.PersonMsgClearEvent;
import com.baoluo.im.MsgHelper;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.adapter.ChatAdapterMqtt;
import com.baoluo.im.util.InputBoxUtil;

import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * 聊天页面
 * 
 * @author tao.lai
 */
public class AtyChatMqtt extends AtyBase implements InputListener,
		HeadViewListener, OnScrollListener {
	public static final String TAG = "AtyChatMqtt";

	public static final String EXTRA_CONTACTS = "contact";
	
	public static final int SELECTER_INDEX = 0x1;

	private HeadView headView;
	private ListView lvMsg;
	private ImInputView inputView;
	private SpeekView speekView;

	private ChatAdapterMqtt chatAdapter;
	private List<Msg> listMsg;
	private ContactsInfo friInfo;
	private String scrollStr;

	private ProgressBar loadInfo;
	private LinearLayout loadLayout;

	private final int pageSize = 10; // 每页条目数
	private int totalNums = 0; // 总条目数
	private int firstItem = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.im_aty_chat);
		EventBus.getDefault().register(this);
		SelectImg.getEmptyIntance().setChooseAble(1);
		Intent intent = this.getIntent();
		friInfo = (ContactsInfo) intent.getSerializableExtra(EXTRA_CONTACTS);
		scrollStr = intent.getStringExtra(AtyIMSearch.CHAT_SCROLL_TO);
		MsgHelper.getInstance().setCuurentId(friInfo.getAccountID());
		MsgInfoHelper.getInstance().clearUnReadMsg(friInfo.getAccountID());
		initView();
		loadLayout = new LinearLayout(this);
		loadLayout.setGravity(Gravity.CENTER);
		loadInfo = new ProgressBar(this, null,
				android.R.attr.progressBarStyleSmall);
		loadLayout.addView(loadInfo, new LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.WRAP_CONTENT));
		lvMsg.addHeaderView(loadLayout);
		lvMsg.setOnScrollListener(this);
		initDatas();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		initListMsg();
		chatAdapter.notifyDataSetChanged();
	}

	public void onEventMainThread(MsgEvent event) {
		L.i(TAG, "from service Msg :msg=" + event.getMsg().getBody());
		if (event.getMsg() != null) {
			listMsg.add(event.getMsg());
			chatAdapter.notifyDataSetChanged();
			totalNums ++;
		}
	}

	public void onEventMainThread(PersonMsgClearEvent event) {
		listMsg.clear();
		chatAdapter.notifyDataSetChanged();
		totalNums = 0;
	}
	
	private void initListMsg(){
		if(listMsg == null){
			if(!StrUtils.isEmpty(scrollStr)){
				listMsg = MsgInfoHelper.getInstance().getMsgs(friInfo.getAccountID(),
						scrollStr);
			}else{
				listMsg = MsgInfoHelper.getInstance().getMsgs(friInfo.getAccountID(), 0,
						pageSize);
				Collections.reverse(listMsg);
			}
		}else{
			listMsg.clear();
			if(!StrUtils.isEmpty(scrollStr)){
				listMsg.addAll(MsgInfoHelper.getInstance().getMsgs(friInfo.getAccountID(),
						scrollStr));
			}else{
				listMsg.addAll(MsgInfoHelper.getInstance().getMsgs(friInfo.getAccountID(), 0,
						pageSize));
				Collections.reverse(listMsg);
			}
		}
	}
	
	@SuppressLint("HandlerLeak")
	Handler handler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			switch(msg.what){
			case SELECTER_INDEX:
				L.i(TAG, "setSelection = "+msg.obj);
				lvMsg.setSelection((Integer)msg.obj);
				break;
			default:break;
			}
		}
	};

	private void initDatas() {
		totalNums = MsgInfoHelper.getInstance().getTotalNum(
				friInfo.getAccountID());
		initListMsg();
		if (totalNums == listMsg.size()) {
			lvMsg.removeHeaderView(loadLayout);
		}
		L.i(TAG, "initDatas listMsg.size=" + listMsg.size() + "   totalNums="
				+ totalNums);
		headView.setHeadTitle(friInfo.getDisplayName());
		inputView.setInputListener(this);
		inputView.setToId(friInfo.getAccountID());
		chatAdapter = new ChatAdapterMqtt(this, friInfo, listMsg,
				R.layout.im_chat_item);
		lvMsg.setAdapter(chatAdapter);
		if (!StrUtils.isEmpty(scrollStr)) {
			lvMsg.setSelection(0);
			//lvMsg.setSelection(getScrollStrIndex());
			L.i(TAG, "set lvMsg.setSelection =0");
		} else {
			lvMsg.setSelection(lvMsg.getCount() - 1);
			L.i(TAG, "setSelection last count()");
		}
	}

	private void nextPage() {
		List<Msg> datas = MsgInfoHelper.getInstance().getMsgs(
				friInfo.getAccountID(), listMsg.size(), pageSize);
		Collections.reverse(datas);
		listMsg.addAll(0, datas);
		chatAdapter.notifyDataSetChanged();
		L.e(TAG, "nextPage  listMsg.size()=" + listMsg.size() + ",totalNums="
				+ totalNums + ",datas.size()=" + datas.size());
		if (listMsg.size() == totalNums) {
			lvMsg.removeHeaderView(loadLayout);
		}
		Message msg = new Message();
		msg.what = SELECTER_INDEX;
		msg.obj = datas.size();
		handler.sendMessage(msg);
		//lvMsg.setSelection(datas.size());
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (firstItem == 0 && listMsg.size() < totalNums
				&& scrollState == OnScrollListener.SCROLL_STATE_IDLE) {
			nextPage();
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		firstItem = firstVisibleItem;
	}

	private int getScrollStrIndex() {
		if (listMsg.size() == 0) {
			return -1;
		}
		for (int i = 0; i < listMsg.size(); i++) {
			Msg m = listMsg.get(i);
			if (m.getBody().contains(scrollStr)) {
				return i;
			}
		}
		return -1;
	}

	public void onEventMainThread(final IMSendMsgEvent event) {
		final Msg msg = event.getMsg();
		if(msg.getMsgType() == Msg.MSG_GROUP){
			return ;
		}
		new PostObjTask(UrlHelper.MSG_USER_SEND, GsonUtil.getInstance()
				.obj2Str(msg), new UpdateViewHelper.UpdateViewListener() {
			@Override
			public void onComplete(Object obj) {
				msg.setIsOut(Msg.MSG_OUT);
				msg.setExpired((new Date()).getTime());
				if(msg.getContentType() != Msg.MSG_TYPE_TEXT){
					msg.setBody(event.getFileLocalUrl());
				}
				MsgDb msgDb = MsgInfoHelper.getInstance().objSwitch(msg);
				msgDb.setIsRead(0);
				MsgInfoHelper.getInstance().insertMsg(msgDb);

				listMsg.add(msg);
				chatAdapter.notifyDataSetChanged();
				totalNums++;
			}
		});
	}

	@Override
	public void doSend(final Msg msg) {
		InputBoxUtil.getInstance().prepareSendMsg(msg);
		/*
		 * Msg sendMsg = InputBoxUtil.getInstance().getSendMsg(msg);
		 * MsgHelper.getInstance().sendMsg(Configs.MQ_USER_PRE + toId, sendMsg);
		 */
		/*Msg sendMsg = InputBoxUtil.getInstance().getSendMsg(msg);
		new PostObjTask(UrlHelper.MSG_USER_SEND, GsonUtil.getInstance()
				.obj2Str(sendMsg), new UpdateViewHelper.UpdateViewListener() {
			@Override
			public void onComplete(String rsStr) {
				msg.setIsOut(Msg.MSG_OUT);
				msg.setExpired((new Date()).getTime());
				msg.setShowTimed(TimeUtil.msgShowTimed(friInfo.getAccountID(),
						msg.getExpired()));
				MsgDb msgDb = MsgInfoHelper.getInstance().objSwitch(msg);
				msgDb.setIsRead(0);
				MsgInfoHelper.getInstance().insertMsg(msgDb);

				listMsg.add(msg);
				chatAdapter.notifyDataSetChanged();
				totalNums++;
			}
		});*/
	}

	private void initView() {
		headView = (HeadView) findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		lvMsg = (ListView) findViewById(R.id.lv_msg_list);
		inputView = (ImInputView) findViewById(R.id.input_box);
		speekView = (SpeekView) findViewById(R.id.view_speek);
		inputView.setSpeakListener(speekView);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == ImInputView.REQUEST_CODE_PHOTO) {
				inputView.sendImg();
			}
		}
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			if (isShouldHideInput(inputView, ev)) {
				inputView.restoreInputView();
			}
			return super.dispatchTouchEvent(ev);
		}
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof ImInputView)) {
			int[] leftTop = { 0, 0 };
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		Intent it = new Intent();
		it.putExtra(AtyChatSet.FRI_UID_PARAM, friInfo.getAccountID());
		it.setClass(this, AtyChatSet.class);
		startActivity(it);
	}
}
