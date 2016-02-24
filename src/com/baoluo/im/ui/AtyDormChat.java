package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ListView;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.ImInputView;
import com.baoluo.community.ui.customview.ImInputView.InputListener;
import com.baoluo.community.ui.customview.SpeekView;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.L;
import com.baoluo.event.DormChatEvent;
import com.baoluo.im.Configs;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.MsgHelper;
import com.baoluo.im.entity.DormMsg;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.adapter.DormChatAdapter;
import com.baoluo.im.util.InputBoxUtil;

import de.greenrobot.event.EventBus;

public class AtyDormChat extends AtyBase implements InputListener, HeadViewListener{
	private static final String TAG = "AtyDormChat";

	public static final String DORM_ID = "dorm_id";
	
	private HeadView headView;
	private ListView lvMsg;
	private ImInputView inputView;
	private SpeekView speekView;

	private DormChatAdapter adapter;
	private List<DormMsg> listMsg;
	
	private String dormId;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.im_aty_chat);
		EventBus.getDefault().register(this);
		SelectImg.getEmptyIntance().setChooseAble(1);
		dormId = getIntent().getStringExtra(DORM_ID);
		L.i(TAG, " chat dormId="+dormId);
		MqttHelper.getInstance().subscribe(Configs.MQ_DORM + dormId);
		initView();
		initDatas();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}

	public void onEventMainThread(DormChatEvent event) {
		if (event.getDormMsg() != null) {
			listMsg.add(event.getDormMsg());
			adapter.notifyDataSetChanged();
		}
	}
	
	private void initDatas() {
		headView.setHeadTitle("基于奇葩宿舍的在线勾搭系统V1.0");
		inputView.setInputListener(this);
		inputView.setToId(dormId);
		
		listMsg = new ArrayList<DormMsg>();
		adapter = new DormChatAdapter(this, listMsg, R.layout.im_chat_item);
		lvMsg.setAdapter(adapter);
	}

	@Override
	public void doSend(final Msg msg) {
		Msg sendMsg = InputBoxUtil.getInstance().getSendMsg(msg);
		MsgHelper.getInstance().sendDormMsg(Configs.MQ_DORM + dormId,
				DormMsg.Msg2DormMsg(sendMsg,dormId));
		/*try {
			Msg sendMsg = InputBoxUtil.getInstance().getSendMsg(msg);
			new PostTask(UrlHelper.DORM_MSG_SEND,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							L.i(TAG, "宿舍消息发送成功");
						}
					}, "Id", dormId, 
					   "ContentType", 
					    sendMsg.getContentType()+ "",
					    "Body", sendMsg.getBody());
		} catch (Exception e) {
			L.e(TAG, "信息发送失败");
			e.printStackTrace();
		}*/
	}

	private void initView() {
		headView = (HeadView) findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		lvMsg = (ListView) findViewById(R.id.lv_msg_list);
		inputView = (ImInputView) findViewById(R.id.input_box);
		speekView = (SpeekView) findViewById(R.id.view_speek);
		inputView.setSpeakListener(speekView);
		headView.setRightIcon(R.drawable.icon_num_people_normal);
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
		/*Intent i = new Intent();
		startActivity(i);*/
	}
}
