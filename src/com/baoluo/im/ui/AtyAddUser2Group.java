package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import android.os.Bundle;
import android.widget.ListView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.HorizontalListViewIm;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.im.entity.AddToGroupEntity;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.GroupUser;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.ui.adapter.AddUser2GroupAdapter;
import com.baoluo.im.ui.adapter.AddUser2GroupAdapter.ItemOnClickListener;
import com.baoluo.im.ui.adapter.AddUser2GroupSelAdapter;
import com.baoluo.im.util.CharacterParser;

/**
 * 选择好友加群
 * @author tao.lai
 *
 */
public class AtyAddUser2Group extends AtyBase implements ItemOnClickListener,
		HeadViewListener {
	private static final String TAG = "AtyAddUser2Group";
	
	public static final String CODE_SELECTED_USER = "selected_users";
	public static final String CODE_OPTION_TYPE = "option_type";
	
	public static final int OPTION_GROUP_CREATE = 0x1;    //建群操作
	public static final int OPTION_USER_2_GROUP = 0x2;    //个人转群
	public static final int OPTION_GROUP_ADD_USER = 0x3;  //邀请好友入群
	
	private int selCount = 0;
	
	private HeadView headView;
	private ListView lvContact;
	private HorizontalListViewIm lvSelContact;
	
	private List<ContactsInfo> contacts;
	private List<ContactsInfo> selContacts;
	
	private AddUser2GroupSelAdapter selAdapter;
	private AddUser2GroupAdapter adapter;
	private List<String> selectedUsers;
	
	
	private CharacterParser characterParser;
	private String groupId;
	private int optionType;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_add_user_to_group);
		groupId = getIntent().getStringExtra("groupId");
		optionType = getIntent().getIntExtra(CODE_OPTION_TYPE, OPTION_GROUP_CREATE);
		initUI();
		initData();
	}
	
	private void initUI(){
		headView = (HeadView) findViewById(R.id.hv_head);
		lvContact = (ListView) findViewById(R.id.lv_contacts);
		lvSelContact = (HorizontalListViewIm) findViewById(R.id.hlv_select);
		headView.setHeadViewListener(this);
	}
	
	private void initData(){
		characterParser = CharacterParser.getInstance();
		contacts = filledData(ContactsHelper.getInstance().getAllContacts());
		Collections.sort(contacts, new ContactsInfo());
		initSelUsers();
		
		L.i(TAG, "selectedUsers:"+GsonUtil.getInstance().obj2Str(selectedUsers));
		selContacts = new ArrayList<ContactsInfo>();
		selAdapter = new AddUser2GroupSelAdapter(this,selContacts,R.layout.item_add_user_sel);
		lvSelContact.setAdapter(selAdapter);
		
		adapter = new AddUser2GroupAdapter(this, contacts, selectedUsers,
				R.layout.item_add_user_to_group);
		lvContact.setAdapter(adapter);
		adapter.setItemOnClickListener(this);
	}
	
	private void addUserToGroup(){
		List<String> selUids = adapter.getSelUids();
		if(selCount < 1 || selUids.size() < 1){
			T.showShort(this, "选择好友失败");
			return;
		}
		List<GroupUser> groupUsers = getSelUsers(selUids);
		AddToGroupEntity addInfo = new  AddToGroupEntity();
		addInfo.setGid(groupId);
		addInfo.setUsers(groupUsers);
		new PostObjTask(UrlHelper.GROUP_ADD_USER, GsonUtil.getInstance()
				.obj2Str(addInfo), new UpdateViewHelper.UpdateViewListener() {
			@Override
			public void onComplete(Object data) {
					T.showShort(AtyAddUser2Group.this, "邀请好友成功");
					setResult(RESULT_OK);
					AtyAddUser2Group.this.finish();
			}
		});
	}
	
	private void doCreate(){
		L.e(TAG, "---------do Create --------------");
		List<String> selUids = adapter.getSelUids();
		if(optionType ==  OPTION_USER_2_GROUP){
			selUids.add(0, selectedUsers.get(0));
		}
		if(selCount < 1 || selUids.size() < 1){
			T.showShort(this, "");
			return;
		}
		GroupInfo groupInfo = new GroupInfo();
		groupInfo.setName("我的部落");
		groupInfo.setGroupType(0);
		groupInfo.setDec("我的群");
		groupInfo.setUsers(getSelUsers(selUids));
		new PostObjTask(UrlHelper.GROUP_CREATE, GsonUtil.getInstance().obj2Str(
				groupInfo), new UpdateViewHelper.UpdateViewListener() {
			@Override
			public void onComplete(Object data) {
				AtyAddUser2Group.this.finish();
				T.showShort(AtyAddUser2Group.this, "群创建成功");
				String gid = ResultParse.getInstance().getGidCreated(
						data.toString());
				L.i(TAG, "建群返回 Gid = " + gid);
			}
		});
	}
	
	private List<GroupUser> getSelUsers(List<String> selUids){
		List<GroupUser> users = new ArrayList<GroupUser>();
		GroupUser gu;
		for(ContactsInfo c : contacts){
			if(selUids.contains(c.getAccountID())){
				gu = new GroupUser();
				gu.setId(c.getAccountID());
				gu.setDisplayName(c.getDisplayName());
				users.add(gu);
			}
		}
		return users;
	}
	
	@Override
	public void doAdd(ContactsInfo c) {
		selContacts.add(c);
		selCount ++;
		initSure();
		notifyChange();
	}

	@Override
	public void doRemove(String uid) {
		selContacts.remove(getSelPositionByUid(uid));
		selCount --;
		initSure();
		notifyChange();
	}
	
	private void notifyChange(){
		adapter.notifyDataSetChanged();
		selAdapter.notifyDataSetChanged();
		lvSelContact.setSelection(selAdapter.getCount() - 1);
	}
	
	private void initSure(){
		String str = "确定";
		if(selCount > 0){
			str = "("+selCount+")" + str;
		}
		headView.setRightText(str);
	}
	
	private int getSelPositionByUid(String uid) {
		int i = -1;
		for (ContactsInfo c : selContacts) {
			i++;
			if (uid.equals(c.getAccountID())) {
				return i;
			}
		}
		return -1;
	}
	
	private List<ContactsInfo> filledData(List<ContactsInfo> list) {
		List<ContactsInfo> mSortContacts = new ArrayList<ContactsInfo>();
		for (int i = 0; i < list.size(); i++) {
			ContactsInfo contact = list.get(i);
			String pinyin = characterParser.getSelling(contact
					.getAccountName());
			String sortString = pinyin.substring(0, 1).toUpperCase();
			if (sortString.matches("[A-Z]")) {
				contact.setSortLetters(sortString.toUpperCase());
				
			} else {
				contact.setSortLetters("#");
			}
			mSortContacts.add(contact);
		}
		return mSortContacts;
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		if(groupId != null && optionType == OPTION_GROUP_ADD_USER){
			addUserToGroup();
		}else{
			doCreate();
		}
	}
	
	private void initSelUsers(){
		String [] st = getIntent().getStringArrayExtra(CODE_SELECTED_USER);
		if(st != null && st.length > 0){
			selectedUsers = Arrays.asList(st);
		}
	}
}
