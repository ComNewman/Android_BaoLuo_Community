package com.baoluo.im.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smackx.offline.OfflineMessageManager;

import com.baoluo.im.XmppConnection;

public class XmppService {

	private static XmppService xmppService;

	public static XmppService getInstance() {
		if (xmppService == null) {
			xmppService = new XmppService();
		}
		return xmppService;
	}

	/**
	 * 获取离线消息
	 * 
	 * @return
	 */
	public Map<String, List<HashMap<String, String>>> getHisMessage() {
		XMPPConnection conn = XmppConnection.getConnection();
		if (conn == null) {
			return null;
		}
		Map<String, List<HashMap<String, String>>> offlineMsgs = null;
		try {
			OfflineMessageManager offlineManager = new OfflineMessageManager(
					conn);
			List<Message> it = offlineManager.getMessages();

			int count = offlineManager.getMessageCount();
			if (count <= 0) {
				return null;
			}
			offlineMsgs = new HashMap<String, List<HashMap<String, String>>>();

			for (Message message : it) {
				String fromUser = message.getFrom();
				HashMap<String, String> histrory = new HashMap<String, String>();
				histrory.put("useraccount", conn.getUser());
				histrory.put("friendaccount", fromUser);
				histrory.put("info", message.getBody());
				histrory.put("type", "left");
				if (offlineMsgs.containsKey(fromUser)) {
					offlineMsgs.get(fromUser).add(histrory);
				} else {
					List<HashMap<String, String>> temp = new ArrayList<HashMap<String, String>>();
					temp.add(histrory);
					offlineMsgs.put(fromUser, temp);
				}
			}
			offlineManager.deleteMessages();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return offlineMsgs;
	}

}
