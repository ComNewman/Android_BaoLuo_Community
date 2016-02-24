package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
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
import com.baoluo.im.ui.adapter.GroupMemberAdapter;

import de.greenrobot.event.EventBus;

/**
 * 群详细信息
 * @author tao.lai
 *
 */
public class AtyGroupInfo extends AtyBase implements OnClickListener,
		HeadViewListener {

	private static final String TAG = "AtyAddGroup";

	private HeadView headView;
	private ImageButton ibSetTop, ibDND;
	private GridView gvMembers;
	private TextView tvGroupName, tvMyNick;
	private RelativeLayout rlGroupName, rlMyNick, rlQR_CODE;
	private Button btnExit;
	
	private boolean isSetTop = false;
	private boolean isDND = false;
	private List<GroupUser> users;
	private GroupMemberAdapter adapter;
	private GroupInfo groupInfo;
	private String groupCode;
	private MyClickListener listener;
	
	private String [] selectedUsers;

	private static final int REQUEST_CODE_GROUP_NAME = 0x1;
	private static final int REQUEST_CODE_MY_NAME = 0x2;
	public static final int CODE_ADD_MEMBER_UPDATE = 0x3; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_add_group);
		groupInfo = (GroupInfo) getIntent().getSerializableExtra("groupInfo");
		initUI();
		initData();
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_head);
		gvMembers = (GridView) findViewById(R.id.gv_members);
		ibSetTop = (ImageButton) findViewById(R.id.ibtn_to_top);
		ibDND = (ImageButton) findViewById(R.id.ibtn_notice);
		rlGroupName = (RelativeLayout) findViewById(R.id.rl_group_name);
		rlMyNick = (RelativeLayout) findViewById(R.id.rl_my_name);
		rlQR_CODE = (RelativeLayout) findViewById(R.id.rl_code);
		btnExit = (Button) findViewById(R.id.btn_exit);

		tvGroupName = (TextView) findViewById(R.id.tv_group_name);
		tvMyNick = (TextView) findViewById(R.id.tv_my_name);
		headView.setHeadViewListener(this);
		ibSetTop.setOnClickListener(this);
		ibDND.setOnClickListener(this);

		rlGroupName.setOnClickListener(this);
		rlMyNick.setOnClickListener(this);
		rlQR_CODE.setOnClickListener(this);
		btnExit.setOnClickListener(this);
	}

	private void initData() {
		isSetTop = MessageHelper.getInstance().getMsgToped(groupInfo.getId());
		ibSetTop.setSelected(isSetTop);
		isDND =  GroupHelper.getInstance().getDnded(groupInfo.getId());
		ibDND.setSelected(isDND);
		listener = new MyClickListener() {
			@Override
			public void myOnClick(int position, View v) {
				switch (v.getId()) {
				case R.id.group_add_menber:
					Intent i = new Intent();
					T.showShort(AtyGroupInfo.this, "add menbers");
					i.putExtra("groupId", groupInfo.getId());
					i.putExtra(AtyAddUser2Group.CODE_OPTION_TYPE, AtyAddUser2Group.OPTION_GROUP_ADD_USER);
					i.putExtra(AtyAddUser2Group.CODE_SELECTED_USER, selectedUsers);
					i.setClass(AtyGroupInfo.this, AtyAddUser2Group.class);
					startActivityForResult(i, CODE_ADD_MEMBER_UPDATE);
					break;
				case R.id.group_menber_head:
					T.showShort(AtyGroupInfo.this, users.get(position)
							.getDisplayName());
					Intent intent = new Intent();
					if(users.get(position).getId().equals(GlobalContext.getInstance().getUid())){
						intent.setClass(AtyGroupInfo.this, AtyPerson.class);
					}else{
						intent.putExtra("id", users.get(position).getId());
						intent.setClass(AtyGroupInfo.this,AtyFriInfo.class);
						
					}
					startActivity(intent);
					break;
				case R.id.groupinfo_delete_menber:
					final int pst = position;
					new PostTask(UrlHelper.GROUP_KICK,new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object data) {
								users.remove(pst);
								adapter.notifyDataSetChanged();
						}
					},"Gid", groupInfo.getId(),
							"Uid", users.get(position).getId());
					break;
				default:
					break;
				}
			}
		};
		users = new ArrayList<GroupUser>();
		adapter = new GroupMemberAdapter(users, this, listener,
				groupInfo.getOwner());
		gvMembers.setAdapter(adapter);
		initMembers();
	}
	
	private void initMembers(){
		new GetTask(UrlHelper.GROUP_LIST,new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
			@Override
			public void onComplete(Object data) {
					GroupInfo group = (GroupInfo) data;
					if (group != null) {
						groupCode = group.getQRCode();
						tvGroupName.setText(group.getName());
						tvMyNick.setText(group.getMyGroupName());
						users.clear();
						users.addAll(group.getUsers());
						L.i(TAG, "users:"+GsonUtil.getInstance().obj2Str(users));
						initSelectedUserId();
						adapter.notifyDataSetChanged();
					}
			}
		},"id", groupInfo.getId());
	}
	
	private void initSelectedUserId(){
		List<String> seList = new ArrayList<String>();
		for(GroupUser gu : users){
			seList.add(gu.getId());
		}
		selectedUsers = (String[]) seList.toArray(new String[seList.size()]);
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.ibtn_to_top:
			setTopSwitch();
			break;
		case R.id.ibtn_notice:
			setDNDSwitch();
			break;
		case R.id.rl_group_name:
			i.setClass(AtyGroupInfo.this, AtyAmendGroup.class);
			i.putExtra(AtyAmendGroup.EXTRA_INPUT_TEXT, tvGroupName.getText());
			i.putExtra(AtyAmendGroup.EXTRA_FLAG, 1);
			i.putExtra(AtyAmendGroup.EXTRA_ID, groupInfo.getId());
			startActivityForResult(i, REQUEST_CODE_GROUP_NAME);
			break;
		case R.id.rl_my_name:
			i.setClass(AtyGroupInfo.this, AtyAmendGroup.class);
			i.putExtra(AtyAmendGroup.EXTRA_INPUT_TEXT, tvMyNick.getText());
			i.putExtra(AtyAmendGroup.EXTRA_FLAG, 0);
			i.putExtra(AtyAmendGroup.EXTRA_ID, groupInfo.getId());
			startActivityForResult(i, REQUEST_CODE_MY_NAME);
			break;
		case R.id.rl_code:
			i.putExtra("groupCode", groupCode);
			i.putExtra("groupInfo", groupInfo);
			i.setClass(AtyGroupInfo.this, AtyGroupCode.class);
			startActivity(i);
			break;
		case R.id.btn_exit:
			exitGroup();
			break;
		default:
			break;
		}
	}
	
	private void exitGroup() {
		new PostTask(UrlHelper.GROUP_EXIT,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (ResultParse.getInstance().getResCode(obj.toString()) == Configs.RESPONSE_OK) {
							T.showShort(AtyGroupInfo.this, "退群成功！");
							GroupHelper.getInstance().removeGroupByGid(
									groupInfo.getId());
							finish();
							onBackPressed();
						} else {
							T.showShort(AtyGroupInfo.this, "退群失败");
						}
					}
				}, "Id", groupInfo.getId());
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_CODE_GROUP_NAME) {
				String name = data.getStringExtra("name");
				tvGroupName.setText(name);
			} else if (requestCode == REQUEST_CODE_MY_NAME) {
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

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		
	}

}
