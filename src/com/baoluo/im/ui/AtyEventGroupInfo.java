package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.AtyFriInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.dao.helper.MessageHelper;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.GroupUser;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.ui.adapter.EventGroupMemberAdapter;

import de.greenrobot.event.EventBus;

/**
 * 活动群组设置
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyEventGroupInfo extends AtyBase implements OnClickListener,
		HeadViewListener {

	private static final String TAG = "AtyEventGroupInfo";

	public static final int CODE_ADD_MEMBER_UPDATE = 0x3;
	private static final int REQUEST_CODE_EVENT_GROUP_NAME = 0x1;
	private static final int REQUEST_CODE_EVENT_MY_NAME = 0x2;

	private HeadView headView;
	private ImageButton ibSetTop, ibDND, ibAuto;
	private GridView gvMembers;
	private TextView tvGroupName, tvMyNick,tvAuto;
	private RelativeLayout rlMyNick, rlQR_CODE,rlAutoAttend,rlSeachMsg;
	private Button btnExit;
	private EventGroupMemberAdapter adapter;

	private boolean isSetTop = false;
	private boolean isDND = false;
	private boolean isAuto;
	private int isOpen = 0;
	private boolean isGroupOwer = false;
	private List<GroupUser> users;
	private GroupInfo groupInfo;
	private String groupCode;
	private MyClickListener listener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_event_group_info);
		groupInfo = (GroupInfo) getIntent().getSerializableExtra("groupInfo");
		initUI();
		initData();
	}

	private void initUI() {
		rlAutoAttend = (RelativeLayout) findViewById(R.id.rl_event_group_auto_attend);
		tvAuto = (TextView) findViewById(R.id.tv_auto_attend);
		headView = (HeadView) findViewById(R.id.event_group_hv_head);
		gvMembers = (GridView) findViewById(R.id.gv_event_group_members);
		ibSetTop = (ImageButton) findViewById(R.id.ibtn_event_group_to_top);
		ibDND = (ImageButton) findViewById(R.id.ibtn_event_group_notice);
		ibAuto = (ImageButton) findViewById(R.id.ib_event_group_auto_attend);
		rlMyNick = (RelativeLayout) findViewById(R.id.rl_event_group_my_name);
		rlQR_CODE = (RelativeLayout) findViewById(R.id.rl_event_group_code);
		btnExit = (Button) findViewById(R.id.btn_event_group_exit);
		rlSeachMsg = (RelativeLayout) findViewById(R.id.rl_event_group_chat_content);
		rlSeachMsg.setOnClickListener(this);

		tvGroupName = (TextView) findViewById(R.id.tv_event_group_name_group);
		tvMyNick = (TextView) findViewById(R.id.tv_event_group_my_name);

		headView.setHeadViewListener(this);
		ibSetTop.setOnClickListener(this);
		ibDND.setOnClickListener(this);
		ibAuto.setOnClickListener(this);

		rlMyNick.setOnClickListener(this);
		rlQR_CODE.setOnClickListener(this);
		btnExit.setOnClickListener(this);
	}

	private void initData() {
		initIsOpen();
		if (groupInfo.getOwner().equals(GlobalContext.getInstance().getUid())) {
			btnExit.setText("解散当前活动群");
			isGroupOwer = true;
		}else{
			tvAuto.setTextColor(Color.parseColor("#BBBBBB"));
			rlAutoAttend.setBackgroundColor(Color.parseColor("#ededed"));
		}
		isSetTop = MessageHelper.getInstance().getMsgToped(groupInfo.getId());
		ibSetTop.setSelected(isSetTop);
		isDND = GroupHelper.getInstance().getDnded(groupInfo.getId());
		ibDND.setSelected(isDND);
		listener = new MyClickListener() {
			@Override
			public void myOnClick(int position, View v) {
				switch (v.getId()) {
				case R.id.group_menber_head:
					T.showShort(AtyEventGroupInfo.this, users.get(position)
							.getDisplayName());
					Intent intent = new Intent();
					if (users.get(position).getId()
							.equals(GlobalContext.getInstance().getUid())) {
						intent.setClass(AtyEventGroupInfo.this, AtyPerson.class);
					} else {
						intent.putExtra("id", users.get(position).getId());
						intent.setClass(AtyEventGroupInfo.this,
								AtyFriInfo.class);
					}
					startActivity(intent);
					break;
				case R.id.groupinfo_delete_menber:
					final int pst = position;
					new PostTask(UrlHelper.EVENT_GROUP_KICK,
							new UpdateViewHelper.UpdateViewListener() {
								@Override
								public void onComplete(Object data) {
										users.remove(pst);
										adapter.notifyDataSetChanged();
								}
							}, "Id", groupInfo.getId(), "Uid", users.get(
									position).getId());
					break;
				default:
					break;
				}
			}
		};
		users = new ArrayList<GroupUser>();
		adapter = new EventGroupMemberAdapter(users, this, listener,
				groupInfo.getOwner());
		gvMembers.setAdapter(adapter);
		initMembers();

	}

	private void initIsOpen() {
		new GetTask(UrlHelper.EVENT_GROUP_ISOPEN,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
						String rs = (String) data;
							try {
								JSONObject obj = new JSONObject(rs);
								boolean isOpen = obj.getBoolean("IsOpen");
								ibAuto.setSelected(isOpen);
							} catch (JSONException e) {
								e.printStackTrace();
							}
						}
				}, "id", groupInfo.getId());
	}

	private void initMembers() {
		new GetTask(UrlHelper.GROUP_LIST,
				new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
					@Override
					public void onComplete(Object data) {
							GroupInfo group = (GroupInfo) data;
								groupCode = group.getQRCode();
								tvGroupName.setText(group.getName());
								tvMyNick.setText(group.getMyGroupName());
								users.clear();
								users.addAll(group.getUsers());
								L.i(TAG, "users:"
										+ GsonUtil.getInstance().obj2Str(users));
								adapter.notifyDataSetChanged();
						}

				}, "id", groupInfo.getId());
	}

	private void dissolveGroup() {
		new PostTask(UrlHelper.EVENT_GROUP_DISSOLVE,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (ResultParse.getInstance().getResCode(obj.toString()) == Configs.RESPONSE_OK) {
							T.showShort(AtyEventGroupInfo.this, "解散群成功！");
							finish(3);
						} else {
							T.showShort(AtyEventGroupInfo.this, "解散群失败");
						}
					}
				}, "Id", groupInfo.getId());
	}

	private void exitEventGroup() {
		new PostTask(UrlHelper.EVENT_GROUP_EXIT,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (ResultParse.getInstance().getResCode(obj.toString()) == Configs.RESPONSE_OK) {
							T.showShort(AtyEventGroupInfo.this, "退活动群成功！");
							GroupHelper.getInstance().removeGroupByGid(
									groupInfo.getId());
							finish();
							onBackPressed();
						} else {
							T.showShort(AtyEventGroupInfo.this, "退活动群失败");
						}
					}
				}, "Id", groupInfo.getId());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_EVENT_GROUP_NAME) {
				String name = data.getStringExtra("name");
				tvGroupName.setText(name);
			} else if (requestCode == REQUEST_CODE_EVENT_MY_NAME) {
				String name = data.getStringExtra("name");
				tvMyNick.setText(name);
			} else if (requestCode == CODE_ADD_MEMBER_UPDATE) {
				initMembers();
			}
		}
	}

	private void setTopSwitch() {
		isSetTop = !isSetTop;
		ibSetTop.setSelected(isSetTop);
		MessageHelper.getInstance().switchMsgTop(groupInfo.getId(), isSetTop);
		EventBus.getDefault().post(new NotifyMessageUpdate());
		L.i(TAG, "消息置顶...");
	}

	private void setDNDSwitch() {
		isDND = !isDND;
		ibDND.setSelected(isDND);
		GroupHelper.getInstance().switchDnded(groupInfo.getId(), isDND);
	}
	/*private void searchMsg() {
		L.i(TAG, "do查找聊天记录");
		Intent it = new Intent();
		it.putExtra(AtyIMSearch.FRI_UID, friUid);
		it.putExtra(AtyIMSearch.SEARCH_TYPE, AtyIMSearch.SEARCH_TYPE_GROUP);
		it.setClass(this, AtyIMSearch.class);
		startActivity(it);
	}*/
	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.ibtn_event_group_to_top:
			setTopSwitch();
			break;
		case R.id.ibtn_event_group_notice:
			setDNDSwitch();
			break;
		case R.id.ib_event_group_auto_attend:
			if(isGroupOwer){
				autoAttend();
			}
			break;
		case R.id.rl_event_group_my_name:
			i.setClass(AtyEventGroupInfo.this, AtyAmendGroup.class);
			i.putExtra("inputText", tvMyNick.getText());
			i.putExtra("flag", 0);
			i.putExtra("id", groupInfo.getId());
			startActivityForResult(i, REQUEST_CODE_EVENT_MY_NAME);
			break;
		case R.id.rl_event_group_code:
			i.putExtra("groupCode", groupCode);
			i.putExtra("groupInfo", groupInfo);
			i.setClass(AtyEventGroupInfo.this, AtyGroupCode.class);
			startActivity(i);
			break;
		case R.id.btn_event_group_exit:
			if (isGroupOwer) {
				dissolveGroup();
				return;
			}
			exitEventGroup();
			break;
		case R.id.rl_event_group_chat_content:
			searchMsg();
			break;
		default:
			break;
		}
	}
	
	private void searchMsg() {
		L.i(TAG, "do查找群聊天记录");
		Intent it = new Intent();
		it.putExtra(AtyIMSearch.UID, groupInfo.getId());
		it.putExtra(AtyIMSearch.SEARCH_TYPE, AtyIMSearch.SEARCH_TYPE_CONTENT);
		it.setClass(this, AtyIMSearch.class);
		startActivity(it);
	}

	private void autoAttend() {
		isAuto = !isAuto;
		ibAuto.setSelected(isAuto);
		if (isAuto) {
			isOpen = 1;
		} else {
			isOpen = 0;
		}
		new PostTask(UrlHelper.EVENT_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						T.showShort(AtyEventGroupInfo.this, "自动报名切换完成！");
					}
				}, "Id", groupInfo.getId(), "IsOpen",
				String.valueOf(isOpen));
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {

	}
}
