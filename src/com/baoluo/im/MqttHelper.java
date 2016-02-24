package com.baoluo.im;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.paho.client.mqttv3.IMqttClient;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.MqttPersistenceException;
import org.eclipse.paho.client.mqttv3.MqttSecurityException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.im.entity.CmdMsg;
import com.baoluo.im.entity.DormMsg;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.GroupInfoList;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.entity.SysMsg;
import com.baoluo.im.task.GenerateGroupsAvatarTask;

public class MqttHelper {
	private static final String TAG = "MqttHelper";

	private static MqttHelper instance;
	private IMqttClient mqClient;

	public static MqttHelper getInstance() {
		if (instance == null) {
			instance = new MqttHelper();
		}
		instance.initMqClient();
		return instance;
	}

	private void initMqClient() {
		if (mqClient != null) {
			return;
		}
		MemoryPersistence persistence = new MemoryPersistence();
		try {
			this.mqClient = new MqttClient(Configs.MQ_HOST,
					Configs.MQ_CLIENT_ID_PRE + SettingUtility.getAccount(),
					persistence);
			doConnection();
		} catch (MqttException e) {
			e.printStackTrace();
		}
	}

	private MqttConnectOptions connOpts;

	private MqttConnectOptions getConnectOptions() {
		if (this.connOpts == null) {
			this.connOpts = new MqttConnectOptions();
			this.connOpts.setCleanSession(true);
			this.connOpts.setUserName(Configs.MQ_USERNAME);
			this.connOpts.setPassword(Configs.MQ_PWD.toCharArray());
			this.connOpts.setConnectionTimeout(10); // 设置超时时间
			this.connOpts.setKeepAliveInterval(60); // 设置会话心跳时间
		}
		return this.connOpts;
	}

	public void checkConnection() {
		L.i(TAG, "mqClient isConnected = " + this.mqClient.isConnected());
		if (!this.mqClient.isConnected()) {
			doConnection();
		}
	}

	private void doConnection() {
		try {
			this.mqClient.connect(getConnectOptions());
			initMqttSubscibe();
			MsgListener();
			L.i(TAG, "-------------Mqtt 连接成功------------");
		} catch (MqttSecurityException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
			L.e(TAG, "------------Mqtt 连接断开！------------");
		}
	}

	/**
	 * 断开MQtt服务连接
	 */
	public void disConnection(){
		instance = null;
		try{
			this.mqClient.disconnect();
		}catch (Exception e){
			L.e(TAG,"端开MQtt服务失败...");
		}
	}

	private void MsgListener() {
		mqClient.setCallback(new MqttCallback() {
			@Override
			public void messageArrived(String topic, MqttMessage msg)
					throws Exception {
				L.i(TAG, "from " + topic + "'s msg : " + msg);
				if (topic.startsWith(Configs.MQ_CMD_PRE)) {
					MsgHelper.getInstance().handleCmdMsg(
							getCmdMsg(msg.getPayload()));
				} else if (topic.startsWith(Configs.MQ_SYS_CHANNEL)) {
					MsgHelper.getInstance().handleSysMsg(
							getSysMsg(msg.getPayload()));
				} else if (topic.startsWith(Configs.MQ_DORM)) {
					MsgHelper.getInstance().handleDormMsg(
							getDormMsg(msg.getPayload()));
				} else if (topic.startsWith(Configs.MQ_DORM_GLOBAL)) {
					MsgHelper.getInstance().handleCmdMsg(
							getCmdMsg(msg.getPayload()));
				} else if(topic.startsWith(Configs.MQ_EVENT_PRE)){
					MsgHelper.getInstance().handleEventGroupMsg(
							getDormMsg(msg.getPayload()));
				}else {
					MsgHelper.getInstance().handleMsg(getMsg(msg.getPayload()));
				}
			}

			@Override
			public void deliveryComplete(IMqttDeliveryToken arg0) {
				L.i(TAG, "deliveryComplete---");
			}

			@Override
			public void connectionLost(Throwable arg0) {
				L.e(TAG, "errorStr=" + arg0.toString());
				L.i(TAG, "Mqtt 服务器端开，系统在尝试重连...");
				doConnection();
			}
		});
	}

	public void subscribe(String topic) {
		try {
			this.mqClient.subscribe(topic);
			L.i(TAG, "do subscibe: " + topic + " sucess");
		} catch (MqttException e) {
			e.printStackTrace();
			L.e(TAG, "subscibe " + topic + "'s error:" + e.toString());
		}
	}

	public void unSubscribe(String topic) {
		try {
			this.mqClient.unsubscribe(topic);
			L.i(TAG, "unSubscibe: " + topic + " scucess ");
		} catch (MqttException e) {
			e.printStackTrace();
			L.e(TAG, "subscibe" + topic + "'s error:" + e.toString());
		}
	}

	private void subscribe(List<String> topics) {
		try {
			this.mqClient.subscribe(topics.toArray(new String[topics.size()]));
			L.i(TAG, "subscribe topics sucess");
		} catch (MqttException e) {
			e.printStackTrace();
			L.e(TAG, "subscibe topics error:" + e.toString());
		}
	}

	private void initMqttSubscibe() {
		L.i(TAG,"initMqttSubscibe.getAllGroups().size():"+GroupHelper.getInstance().getAllGroups().size());
		if (GroupHelper.getInstance().getAllGroups().size() == 0) {
			L.i(TAG,"initMqttSubscibe...");
			new GetTask(UrlHelper.MY_GROUP_LIST,
					new UpdateViewHelper.UpdateViewListener(GroupInfoList.class) {
						@Override
						public void onComplete(Object obj) {
							GroupInfoList groupInfoList = (GroupInfoList)obj;
							List<GroupInfo> list = groupInfoList.getItems();
							initMqttChannel(list);
							GroupHelper.getInstance().insertGroups(list);
							new GenerateGroupsAvatarTask(list);
						}
					});
		} else {
			initMqttChannel(GroupHelper.getInstance().getAllGroups());
		}
	}

	private void initMqttChannel(List<GroupInfo> list) {
		List<String> topics = new ArrayList<String>();
		topics.add(Configs.MQ_SYS_CHANNEL);
		topics.add(Configs.MQ_CMD_PRE + GlobalContext.getInstance().getUid());
		topics.add(Configs.MQ_USER_PRE + GlobalContext.getInstance().getUid());
		topics.add(Configs.MQ_DORM_GLOBAL);
		if (!StrUtils.isEmpty(SettingUtility.getDromId())) {
			L.i(TAG, "init subscibe dormId:" + SettingUtility.getDromId());
			topics.add(Configs.MQ_DORM + SettingUtility.getDromId());
		}
		for (GroupInfo gi : list) {
			topics.add((gi.getGroupType() == 2 ? Configs.MQ_EVENT_PRE
					: Configs.MQ_GROUP_PRE) + gi.getId());
		}
		if (list != null && list.size() > 0) {
			L.i(TAG, "init subscibe group.size:" + list.size());
		}
		subscribe(topics);
	}

	public boolean sendMsg(String topic, MqttMessage message) {
		boolean rs = false;
		try {
			mqClient.publish(topic, message);
			rs = true;
			L.i(TAG, "消息发送成功");
		} catch (MqttPersistenceException e) {
			e.printStackTrace();
		} catch (MqttException e) {
			e.printStackTrace();
		}
		return rs;
	}

	private Msg getMsg(byte[] data) {
		return (Msg) GsonUtil.getInstance().str2Obj(StrUtils.byte2Str(data),
				Msg.class);
	}
	
	private CmdMsg getCmdMsg(byte[] data) {
		return (CmdMsg) GsonUtil.getInstance().str2Obj(StrUtils.byte2Str(data),
				CmdMsg.class);
	}

	private SysMsg getSysMsg(byte[] data) {
		return (SysMsg) GsonUtil.getInstance().str2Obj(StrUtils.byte2Str(data),
				SysMsg.class);
	}

	private DormMsg getDormMsg(byte[] data) {
		return (DormMsg) GsonUtil.getInstance().str2Obj(
				StrUtils.byte2Str(data), DormMsg.class);
	}
}
