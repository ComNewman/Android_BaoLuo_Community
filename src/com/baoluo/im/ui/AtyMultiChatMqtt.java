package com.baoluo.im.ui;

import java.util.Collections;
import java.util.List;

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
import com.baoluo.community.support.task.GetTask;
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
import com.baoluo.dao.helper.MsgInfoHelper;
import com.baoluo.event.IMSendMsgEvent;
import com.baoluo.event.MsgEvent;
import com.baoluo.im.Configs;
import com.baoluo.im.MsgHelper;
import com.baoluo.im.entity.DormMsg;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.adapter.MultiChatAdapterMqtt;
import com.baoluo.im.util.InputBoxUtil;

import de.greenrobot.event.EventBus;

/**
 * 群聊页面
 * 
 * @author tao.lai
 */
public class AtyMultiChatMqtt extends AtyBase implements InputListener,
		HeadViewListener, OnScrollListener {
	public static final String TAG = "AtyMultiChatMqtt";

	public static final String EXTRA_GROUP = "groupInfo";
	//public static final String GROUP_TYPE = "groupType";

	public static final int SELECT_INDEX = 0x1;

	private HeadView headView;
	private ListView lvMsg;
	private ImInputView inputView;
	private SpeekView speekView;

	private MultiChatAdapterMqtt adapter;
	private List<Msg> listMsg;

	private GroupInfo groupInfo;
	private String scrollStr;
	private int groupType;

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
		groupInfo = (GroupInfo) getIntent().getSerializableExtra(EXTRA_GROUP);
		scrollStr = getIntent().getStringExtra(AtyIMSearch.CHAT_SCROLL_TO);
		//groupType = getIntent().getIntExtra(GROUP_TYPE, 0);
		MsgHelper.getInstance().setCuurentId(groupInfo.getId());
		MsgInfoHelper.getInstance().clearUnReadMsg(groupInfo.getId());
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
	protected void onResume() {
		super.onResume();
		initMsgData();
		adapter.notifyDataSetChanged();
		L.i(TAG, "onResume...");
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(MsgEvent event) {
		L.i(TAG, "from service Msg :msg=" + event.getMsg().getBody());
		if (event.getMsg() != null) {
			listMsg.add(event.getMsg());
			adapter.notifyDataSetChanged();
			totalNums++;
		}
	}

	/**
	 * send msg
	 * @param event
	 */
	public void onEventMainThread(IMSendMsgEvent event) {
		if(event.getMsg().getMsgType() != Msg.MSG_GROUP){
			return;
		}
		if (event.getMsg() != null) {
			if(groupInfo.getGroupType() == 2){
				doSendEventGroupMsg(DormMsg.Msg2DormMsg(event.getMsg(),groupInfo.getId()));
			}else{
				doSendNormalMsg(event.getMsg());
			}
		}
	}

	private void initMsgData() {
		if (listMsg == null) {
			if (!StrUtils.isEmpty(scrollStr)) {
				listMsg = MsgInfoHelper.getInstance().getMsgs(
						groupInfo.getId(), scrollStr);
			} else {
				listMsg = MsgInfoHelper.getInstance().getMsgs(
						groupInfo.getId(), 0, pageSize);
				Collections.reverse(listMsg);
			}
		} else {
			listMsg.clear();
			if (!StrUtils.isEmpty(scrollStr)) {
				listMsg.addAll(MsgInfoHelper.getInstance().getMsgs(
						groupInfo.getId(), scrollStr));
			} else {
				listMsg.addAll(MsgInfoHelper.getInstance().getMsgs(
						groupInfo.getId(), 0, pageSize));
				Collections.reverse(listMsg);
			}
		}
	}

	@SuppressLint("HandlerLeak")
	Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			if (msg.what == SELECT_INDEX) {
				lvMsg.setSelection((Integer) msg.obj);
			}
		}
	};

	private void initDatas() {
		totalNums = MsgInfoHelper.getInstance().getTotalNum(groupInfo.getId());
		initMsgData();
		if (totalNums == listMsg.size()) {
			lvMsg.removeHeaderView(loadLayout);
		}
		headView.setHeadTitle(groupInfo.getName());
		inputView.setInputListener(this);
		inputView.setToId(groupInfo.getId());
		if (groupInfo.getUsers() == null || groupInfo.getUsers().size() == 0) {
			initGroupUser();
		}
		adapter = new MultiChatAdapterMqtt(this, groupInfo, listMsg,
				R.layout.im_chat_item);
		lvMsg.setAdapter(adapter);
		if (!StrUtils.isEmpty(scrollStr)) {
			lvMsg.setSelection(0);
			// lvMsg.setSelection(getScrollStrIndex());
		} else {
			lvMsg.setSelection(lvMsg.getCount() - 1);
		}
	}

	private void nextPage() {
		List<Msg> datas = MsgInfoHelper.getInstance().getMsgs(
				groupInfo.getId(), listMsg.size(), pageSize);
		Collections.reverse(datas);
		listMsg.addAll(0, datas);
		adapter.notifyDataSetChanged();
		// lvMsg.setSelection(datas.size());
		L.e(TAG, "nextPage  listMsg.size()=" + listMsg.size());
		if (listMsg.size() == totalNums) {
			lvMsg.removeHeaderView(loadLayout);
		}
		Message msg = new Message();
		msg.what = SELECT_INDEX;
		msg.obj = datas.size();
		handler.sendMessage(msg);
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

	/*private int getScrollStrIndex() {
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
	}*/

	private void initGroupUser() {
		new GetTask(UrlHelper.GROUP_LIST,
				new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
					@Override
					public void onComplete(Object obj) {
						GroupInfo group = (GroupInfo)obj;
						if (group != null) {
							groupInfo.setUsers(group.getUsers());
							adapter = new MultiChatAdapterMqtt(
									AtyMultiChatMqtt.this, groupInfo, listMsg,
									R.layout.im_chat_item);
							lvMsg.setAdapter(adapter);
							lvMsg.setSelection(lvMsg.getCount() - 1);
						}
					}
				}, "id", groupInfo.getId());
	}

	public void doSendEventGroupMsg(DormMsg msg) {
		MsgHelper.getInstance().sendDormMsg(
				Configs.MQ_EVENT_PRE + groupInfo.getId(), msg);
	}

	public void doSendNormalMsg(Msg msg) {
		new PostObjTask(UrlHelper.MSG_GROUP_SEND, GsonUtil.getInstance()
				.obj2Str(msg), new UpdateViewHelper.UpdateViewListener() {
			@Override
			public void onComplete(Object obj) {
			}
		});
	}

	@Override
	public void doSend(final Msg msg) {
		msg.setMsgType(Msg.MSG_GROUP);
		InputBoxUtil.getInstance().prepareSendMsg(msg);
		/*Msg sendMsg = InputBoxUtil.getInstance().getSendMsg(msg);
		if (groupInfo.getGroupType() == 2) {
			sendMsg.setMsgType(Msg.MSG_GROUP);
			doSendEventGroupMsg(DormMsg.Msg2DormMsg(sendMsg, groupInfo.getId()));
		} else {
			doSendNormalMsg(sendMsg);
		}*/
	}

	private void initView() {
		headView = (HeadView) findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		lvMsg = (ListView) findViewById(R.id.lv_msg_list);
		inputView = (ImInputView) findViewById(R.id.input_box);
		speekView = (SpeekView) findViewById(R.id.view_speek);
		inputView.setSpeakListener(speekView);
		headView.setRightIcon(R.drawable.ic_quanliao_setting);
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
		Intent i = new Intent();
		i.putExtra("groupInfo", groupInfo);
		switch (groupInfo.getGroupType()) {
		case 0:
			i.setClass(AtyMultiChatMqtt.this, AtyGroupInfo.class);
			break;
		case 1:
			i.setClass(AtyMultiChatMqtt.this, AtyGroupInfo.class);
			break;
		case 2:
			i.setClass(AtyMultiChatMqtt.this, AtyEventGroupInfo.class);
			break;
		default:
			break;
		}
		startActivity(i);
	}
}
