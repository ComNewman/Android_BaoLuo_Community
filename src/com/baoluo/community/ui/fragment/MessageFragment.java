package com.baoluo.community.ui.fragment;

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
import android.widget.RelativeLayout;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.SlideCutListView;
import com.baoluo.community.ui.customview.SlideCutListView.SlideCutViewListener;
import com.baoluo.community.ui.customview.SlideCutView;
import com.baoluo.community.ui.customview.SlideCutView.OnSlideListener;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.dao.helper.MessageHelper;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.Message;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.AtyChatMqtt;
import com.baoluo.im.ui.AtyIMSearch;
import com.baoluo.im.ui.AtyMultiChatMqtt;
import com.baoluo.im.ui.adapter.MsgListAdapter;
import com.baoluo.im.util.PopupWindowHelp;

import de.greenrobot.event.EventBus;

/**
 * 消息模块
 * 
 * @author tao.lai
 */
@SuppressLint("NewApi")
public class MessageFragment extends Fragment implements SlideCutViewListener,
		OnSlideListener, HeadViewListener,OnClickListener {
	private static final String TAG = "MessageFragment";

	private RelativeLayout rlSearch;
	private HeadView headView;
	private SlideCutListView slv;
	
	private View view;

	private SlideCutView scv;
	private MsgListAdapter adapter;
	private List<Message> msgList;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		EventBus.getDefault().register(this);
		view = inflater.inflate(R.layout.fragment_message, container, false);
		initView();
		initData();
		return view;
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
	
	public void onEventMainThread(NotifyMessageUpdate event){
		msgList.clear();
		msgList.addAll(MessageHelper.getInstance().getAllMessageItem());
		adapter.notifyDataSetChanged();
	}

	private void initView() {
		rlSearch = (RelativeLayout) view.findViewById(R.id.rl_search);
		headView = (HeadView) view.findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		slv = (SlideCutListView) view.findViewById(R.id.lv_msgs);
		slv.setSlideCutViewListener(this);
		rlSearch.setOnClickListener(this);
	}

	private void initData() {
		msgList = MessageHelper.getInstance().getAllMessageItem();
		adapter = new MsgListAdapter(getActivity(),this,msgList);
		slv.setAdapter(adapter);
		slv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SlideCutView slideView = msgList.get(position).getSlideCutView();
				if (slideView.ismIsMoveClick()) {
					return;
				}
				if (slideView.close() && slideView.getScrollX() == 0) {
					if (scv == null || scv.getScrollX() == 0) {
						Message mea = msgList.get(position);
						if(mea.getMsgType() == Msg.MSG_GROUP){
							handleGroup(mea);
						}else{
							handlePerson(mea);
						}
					}
				}
			}
		});
	}
	
	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.rl_search:
			startActivity(new Intent(getActivity(),AtyIMSearch.class));
			break;
			default:break;
		}
	}
	
	private void handlePerson(Message message){
		ContactsInfo ci = ContactsHelper.getInstance().getContactsById(message.getUid());
		Intent it = new Intent();
		it.putExtra(AtyChatMqtt.EXTRA_CONTACTS, ci);
		it.setClass(getActivity(), AtyChatMqtt.class);
		startActivity(it);
	}
	
	private void handleGroup(Message message) {
		GroupInfo gd = GroupHelper.getInstance()
				.getGroupByGid(message.getUid());
		Intent it = new Intent();
		it.putExtra(AtyMultiChatMqtt.EXTRA_GROUP, gd);
		it.setClass(getActivity(), AtyMultiChatMqtt.class);
		startActivity(it);
	}

	@Override
	public void leftListener() {
		
	}

	@Override
	public void rightListener() {
		new PopupWindowHelp(getActivity()).showPopupWindow(
				headView.getRightView(), 20);
	}
	
	@Override
	public SlideCutView getSlideCutView(int position) {
		return msgList.get(position).getSlideCutView();
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
}
