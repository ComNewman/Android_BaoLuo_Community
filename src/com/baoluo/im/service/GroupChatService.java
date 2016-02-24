package com.baoluo.im.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smackx.muc.HostedRoom;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.xdata.Form;
import org.jivesoftware.smackx.xdata.FormField;

import com.baoluo.community.util.L;
import com.baoluo.im.Configs;
import com.baoluo.im.XmppConnection;
import com.baoluo.im.XmppUtil;

/**
 * 
 * @author tao.lai
 * 
 */
public class GroupChatService {

	private static final String TAG = "GroupChatService";

	private static GroupChatService groupChatService;

	public static GroupChatService getInstance() {
		if (groupChatService == null) {
			groupChatService = new GroupChatService();
		}
		return groupChatService;
	}

	public MultiUserChat getMultiChat(String groupName) {
		return new MultiUserChat(XmppConnection.getConnection(), groupName);
	}

	/**
	 * 
	 * @param conn
	 * @param user
	 *            创建者 Jid
	 * @param groupName
	 *            群名称
	 */
	public boolean createGroupChat(String jid, String groupName) {
		MultiUserChat mu = new MultiUserChat(XmppConnection.getConnection(),
				groupName + "@" + Configs.SERVER_GOUP_NAME);
		try {
			mu.create(XmppUtil.getJidToUsername(jid));
			Form form = mu.getConfigurationForm();
			Form submitForm = form.createAnswerForm();
			for (FormField field : form.getFields()) {
				if (!FormField.TYPE_HIDDEN.equals(field.getType())
						&& field.getVariable() != null) {
					submitForm.setDefaultAnswer(field.getVariable());// 设置默认值作为答复
				}
			}
			List<String> owners = new ArrayList<String>(); // 设置群拥有者
			owners.add(jid);
			submitForm.setAnswer("muc#roomconfig_roomowners", owners);
			submitForm.setAnswer("muc#roomconfig_persistentroom", true); // 持久化
			mu.sendConfigurationForm(submitForm);
			return true;
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取群列表
	 */
	public Collection<HostedRoom> getGroupChatList() {
		Collection<HostedRoom> list = null;
		try {
			list = MultiUserChat.getHostedRooms(XmppConnection.getConnection(),
					Configs.SERVER_GOUP_NAME);
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		if (list != null) {
			for (Iterator<HostedRoom> rooms = list.iterator(); rooms.hasNext();) {
				HostedRoom room = rooms.next();
				L.i(TAG, "" + room.getName() + "/Jid=" + room.getJid());
			}
		}
		return list;
	}

	/**
	 * 加入群
	 * 
	 * @param conn
	 * @param jid
	 * @param groupName
	 * @return
	 */
	public boolean joinGroup(String jid, String groupName) {
		MultiUserChat mu = new MultiUserChat(XmppConnection.getConnection(),
				groupName);
		try {
			mu.join(XmppUtil.getJidToUsername(jid));
			return true;
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取已经加入群
	 * 
	 * @param conn
	 * @param jid
	 * @return
	 */
	public List<String> getJoinedGroup(String jid) {
		List<String> joinedGroups = null;
		try {
			joinedGroups = MultiUserChat
					.getJoinedRooms(XmppConnection.getConnection(), jid
							+ Configs.SERVER_SUFFIX);
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return joinedGroups;
	}

}
