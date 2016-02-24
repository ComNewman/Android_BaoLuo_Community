package com.baoluo.community.core;

import java.util.List;

import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.dao.helper.MessageHelper;
import com.baoluo.dao.helper.MsgInfoHelper;
import com.baoluo.event.NotifyContactUpdate;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.MsgHelper;
import com.baoluo.im.entity.ContactsInfoList;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.entity.MsgList;
import com.baoluo.im.task.GenerateGroupsAvatarTask;

import de.greenrobot.event.EventBus;

/**
 * 初始化数据
 * 
 * @author tao.lai
 * 
 */
public class InitDatas {
	private static final String TAG = "InitDatas";

	private static InitDatas instance;

	public static InitDatas getInstance() {
		if (instance == null) {
			instance = new InitDatas();
		}
		return instance;
	}

	public void initDatas() {
		initContacts();
	}
	
	private void initContacts() {
		new GetTask(UrlHelper.CONTACTS,
				new UpdateViewHelper.UpdateViewListener(ContactsInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						ContactsInfoList cl = (ContactsInfoList) obj;
						ContactsHelper.getInstance().insertContacts(
								cl.getItems());
						initOffLineMsg();
						EventBus.getDefault().post(new NotifyMessageUpdate());
						EventBus.getDefault().post(new NotifyContactUpdate());
					}
				});
	}

	private void initOffLineMsg() {
		new GetTask(UrlHelper.MSG_OFF_LINE,
				new UpdateViewHelper.UpdateViewListener(MsgList.class) {
					@Override
					public void onComplete(Object obj) {
						MsgList list = (MsgList) obj;
						if (list != null && list.getCount() > 0) {
							for (Msg msg : list.getItems()) {
								MsgHelper.getInstance().handleMsg(msg);
							}
						}
					}
				});
	}

	public void subscibeGroups(List<GroupInfo> list){
		if(list == null || list.size()==0){
			return ;
		}
		for (GroupInfo item : list) {
			MqttHelper.getInstance().subscribe((item.getGroupType() == 2 ? com.baoluo.im.Configs.MQ_EVENT_PRE
					: com.baoluo.im.Configs.MQ_GROUP_PRE) + item.getId());
		}
		GroupHelper.getInstance().insertGroups(list);
		new GenerateGroupsAvatarTask(list);
	}

	/**
	 * 清空数据库数据
	 */
	public void cleanDBData(){
		ContactsHelper.getInstance().cleanData();
		GroupHelper.getInstance().cleanData();
		MessageHelper.getInstance().cleanData();
		MsgInfoHelper.getInstance().cleanData();
	}
}
