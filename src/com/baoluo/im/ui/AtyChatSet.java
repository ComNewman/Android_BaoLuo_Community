package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.MyDialog;
import com.baoluo.community.ui.customview.MyDialog.MyDialogListener;
import com.baoluo.community.ui.customview.NoScrollGridView;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.dao.helper.MessageHelper;
import com.baoluo.dao.helper.MsgInfoHelper;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.GroupUser;
import com.baoluo.im.ui.adapter.GroupMemberAdapter;

import de.greenrobot.event.EventBus;

/**
 * 单聊设置页
 * 
 * @author tao.lai
 * 
 */
public class AtyChatSet extends AtyBase implements OnClickListener,
		HeadViewListener {
	private static final String TAG = "AtyChatSet";

	public static final String FRI_UID_PARAM = "fri_uid";
	public static final int REQUEST_CODE_FRI_NICK_NAME = 0x4;
	private HeadView headView;
	private NoScrollGridView gvMembers;
	private TextView tvClear, tvSearch, tvJubao,tvNick;
	private ImageButton ibtnSetTop, ibtnDND;
	private RelativeLayout rlNick;
	
	private boolean isSetTop = false; // 置顶
	private boolean isDND = false; // 免打扰

	private String friUid;
	private GroupUser fri;
	private List<GroupUser> members;
	private GroupMemberAdapter adapter;
	private MyClickListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_chat_set);
		initUI();
		initData();
	}

	private void initUI() {
		rlNick = (RelativeLayout) findViewById(R.id.rl_amend_note_name);
		headView = (HeadView) findViewById(R.id.hv_head);
		gvMembers = (NoScrollGridView) findViewById(R.id.gv_members);
		tvClear = (TextView) findViewById(R.id.tv_clear);
		tvSearch = (TextView) findViewById(R.id.tv_search);
		tvJubao = (TextView) findViewById(R.id.tv_jubao);
		ibtnSetTop = (ImageButton) findViewById(R.id.ibtn_to_top);
		ibtnDND = (ImageButton) findViewById(R.id.ibtn_dnd);
		tvNick = (TextView) findViewById(R.id.tv_note_name);
		headView.setHeadViewListener(this);
		tvClear.setOnClickListener(this);
		tvSearch.setOnClickListener(this);
		tvJubao.setOnClickListener(this);
		ibtnSetTop.setOnClickListener(this);
		ibtnDND.setOnClickListener(this);
		rlNick.setOnClickListener(this);
	}

	private void initData() {
		friUid = getIntent().getStringExtra(FRI_UID_PARAM);
		ContactsInfo c = ContactsHelper.getInstance().getContactsById(friUid);
		tvNick.setText(c.getDisplayName());
		isSetTop = MessageHelper.getInstance().getMsgToped(friUid);
		ibtnSetTop.setSelected(isSetTop);
		isDND = ContactsHelper.getInstance().getDnded(friUid);
		ibtnDND.setSelected(isDND);
		headView.setHeadTitle(c.getDisplayName());
		fri = new GroupUser();
		fri.setId(c.getAccountID());
		fri.setFace(c.getFace());
		fri.setDisplayName(c.getDisplayName());
		members = new ArrayList<GroupUser>();
		members.add(fri);
		listener = new MyClickListener() {
			@Override
			public void myOnClick(int position, View v) {
				switch (v.getId()) {
				case R.id.group_add_menber:
					T.showShort(AtyChatSet.this, "add menbers for group");
					String[] st = { friUid };
					Intent it = new Intent();
					it.putExtra(AtyAddUser2Group.CODE_OPTION_TYPE,
							AtyAddUser2Group.OPTION_USER_2_GROUP);
					it.putExtra(AtyAddUser2Group.CODE_SELECTED_USER, st);
					it.setClass(AtyChatSet.this, AtyAddUser2Group.class);
					startActivity(it);
					break;
				case R.id.group_menber_head:
					T.showShort(AtyChatSet.this, members.get(position)
							.getDisplayName());
					break;
				default:
					break;
				}
			}
		};
		adapter = new GroupMemberAdapter(members, this, listener, "");
		gvMembers.setAdapter(adapter);
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_FRI_NICK_NAME) {
				String name = data.getStringExtra("friNickName");
				fri.setDisplayName(name);
				adapter.notifyDataSetChanged();
				tvNick.setText(name);
				headView.setHeadTitle(name);
			} 
		}
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_clear:
			clearChatMsg();
			break;
		case R.id.tv_search:
			searchMsg();
			break;
		case R.id.tv_jubao:
			new PostTask(UrlHelper.INFORM_USER, new UpdateViewHelper.UpdateViewListener(){
				@Override
				public void onComplete(Object obj) {
					T.showShort(AtyChatSet.this, "举报完成！");
					super.onComplete(obj);
				}
			}, "ToUid",friUid);
			break;
		case R.id.ibtn_to_top:
			initSetTopSwitch();
			break;
		case R.id.ibtn_dnd:
			initDNDSwitch();
			break;
		case R.id.rl_amend_note_name:
			Intent i = new Intent();
			i.setClass(this, AtyAmendGroup.class);
			i.putExtra(AtyAmendGroup.EXTRA_INPUT_TEXT, tvNick.getText());
			i.putExtra(AtyAmendGroup.EXTRA_FLAG, 4);
			i.putExtra(AtyAmendGroup.EXTRA_FRI_ID, friUid);
			startActivityForResult(i, REQUEST_CODE_FRI_NICK_NAME);
			break;
		}
	}

	private void clearChatMsg() {
		L.i(TAG, "do清空聊天记录");
		MyDialog dialog = new MyDialog(this, "确定删除和" + fri.getDisplayName()
				+ "的聊天记录吗?", new MyDialogListener() {
			@Override
			public void sure() {
				MsgInfoHelper.getInstance().clearMsg(friUid);
			}
		});
		dialog.show();
	}

	private void searchMsg() {
		L.i(TAG, "do查找聊天记录");
		Intent it = new Intent();
		it.putExtra(AtyIMSearch.UID, friUid);
		it.putExtra(AtyIMSearch.SEARCH_TYPE, AtyIMSearch.SEARCH_TYPE_CONTENT);
		it.setClass(this, AtyIMSearch.class);
		startActivity(it);
	}

	private void initSetTopSwitch() {
		isSetTop = !isSetTop;
		ibtnSetTop.setSelected(isSetTop);
		MessageHelper.getInstance().switchMsgTop(friUid, isSetTop);
		EventBus.getDefault().post(new NotifyMessageUpdate());
		L.i(TAG, "do 消息置顶");
	}

	private void initDNDSwitch() {
		isDND = !isDND;
		ibtnDND.setSelected(isDND);
		ContactsHelper.getInstance().switchDnded(friUid, isDND);
		L.i(TAG, "do 消息免打扰");
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {

	}
}
