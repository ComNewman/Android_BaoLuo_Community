package com.baoluo.im;

import org.eclipse.paho.client.mqttv3.MqttMessage;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.FileDownTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.dao.MsgDb;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.dao.helper.MsgInfoHelper;
import com.baoluo.event.DormChatEvent;
import com.baoluo.event.DormUserChangeEvent;
import com.baoluo.event.MainUnReadMsgEvent;
import com.baoluo.event.MsgEvent;
import com.baoluo.im.entity.CmdMsg;
import com.baoluo.im.entity.DormMsg;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.entity.SysMsg;
import com.baoluo.im.jsonParse.CmdMsgParse;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.util.DormQueueCache;
import com.baoluo.im.util.FileCode;
import com.baoluo.im.util.MsgPackUtil;
import com.baoluo.im.util.UUIDGenerator;
import com.baoluo.notify.MsgNotify;

import de.greenrobot.event.EventBus;

/**
 * MQtt消息处理类
 * 
 * @author tao.lai
 * 
 */
public class MsgHelper {
	private final String TAG = "MsgHelper";

	private final String CHAT_ATY_NAME = "com.baoluo.im.ui.AtyChatMqtt";
	private final String CHAT_MULTI_ATY_NAME = "com.baoluo.im.ui.AtyMultiChatMqtt";

	private String cuurentId; // 当前聊天对象uid、gid

	private static MsgHelper instance;

	public static MsgHelper getInstance() {
		if (instance == null) {
			instance = new MsgHelper();
		}
		return instance;
	}

	public void setCuurentId(String uid) {
		this.cuurentId = uid;
	}

	public void handleCmdMsg(CmdMsg cmdMsg) {
		switch (cmdMsg.getCmdType()) {
		case Configs.CODE_ADD_FRI_SUCCEED:
			ContactsHelper.getInstance().addFriInfo2Db(
					CmdMsgParse.getInstance().getAddFriEntity(cmdMsg).getUid());
			break;
		case Configs.CODE_ADD_FRI:
			MsgNotify.getInstance().addFriMsg(
					CmdMsgParse.getInstance().getAddFriEntity(cmdMsg));

			break;
		case Configs.CODE_ADD_FRI_RESULT:
			String friUid = CmdMsgParse.getInstance().getAcceptedId(cmdMsg);
			if (StrUtils.isEmpty(friUid)) {
				T.showShort(GlobalContext.getInstance(), "对方拒绝了你的好友请求");
			} else {
				ContactsHelper.getInstance().addFriInfo2Db(friUid);
			}
			break;
		case Configs.CODE_INVITE_TO_GROUP:

		case Configs.DORM_CREATE_EVENT:

		case Configs.CODE_GROUP_UPDATE:
			GroupHelper.getInstance().addOrUpdateGroupInfo(
					CmdMsgParse.getInstance().getGroupGid(cmdMsg));
			L.i(TAG, "群信息 addOrUpdate...   cmdType=" + cmdMsg.getCmdType());
			break;
		case Configs.CODE_REMOVE_GROUP:
			break;
		case Configs.CODE_GROUP_DISSOLVE:
			GroupHelper.getInstance().removeGroupByGid(
					CmdMsgParse.getInstance().getGroupGid(cmdMsg));
			break;
		case Configs.DORM_ADD_USER:
			EventBus.getDefault().post(
					new DormUserChangeEvent((byte) 1, cmdMsg.getToId(),
							ResultParse.getInstance().getAddDormUser(cmdMsg)));
			break;
		case Configs.DORM_MINUS_USER:
			EventBus.getDefault()
					.post(new DormUserChangeEvent((byte) 2, cmdMsg.getToId(),
							ResultParse.getInstance().getMinusDormUser(cmdMsg)));
			break;
		case Configs.DORM_START_CHAT:
			if (!DormQueueCache.getInstance().queuedDorm(cmdMsg.getToId())) {
				L.i(TAG, "然而我并没有排这个房间");
				return;
			}
			L.i(TAG, "排队完成，去进入宿舍聊天吧");
			SettingUtility.setDormId(cmdMsg.getToId());
			MsgNotify.getInstance().AttendDorm(cmdMsg.getToId());
			EventBus.getDefault().post(
					new DormUserChangeEvent((byte) 3, cmdMsg.getToId()));
			break;
		default:
			break;
		}
	}

	public void handleDormMsg(DormMsg msg) {
		if (msg.getData().getContentType() != Msg.MSG_TYPE_TEXT) {
			String filePath = BitmapUtil.getPath(GlobalContext.getInstance())
					+ UUIDGenerator.getUUID();
			if (msg.getData().getContentType() == Msg.MSG_TYPE_PIC) {
				filePath += ".jpg";
			} else if (msg.getData().getContentType() == Msg.MSG_TYPE_VOICE) {
				filePath += ".amr";
			} else {
				L.e(TAG, "暂时只支持图片语音");
				return;
			}
			FileCode.getInstance().base64Str2File(msg.getData().getBody(),
					filePath);
			msg.getData().setBody(filePath);
		}
		EventBus.getDefault().post(new DormChatEvent(msg));
	}

	public void handleEventGroupMsg(DormMsg msg) {
		switch (msg.getMsgType()) {
		case 43:
			GroupHelper.getInstance().removeEventGroupByGid(msg.getTo());
			break;
		default: // 处理活动群聊天信息
			handleMsg(DormMsg.DormMsg2Msg(msg));
			break;
		}
	}

	public void handleSysMsg(SysMsg sysMsg) {

	}

	public void handleMsg(final Msg msg) {
		/*if (msg.getContentType() != Msg.MSG_TYPE_TEXT) {
			String filePath = BitmapUtil.getPath(GlobalContext.getInstance())
					+ UUIDGenerator.getUUID();
			if (msg.getContentType() == Msg.MSG_TYPE_PIC) {
				filePath += ".jpg";
			} else if (msg.getContentType() == Msg.MSG_TYPE_VOICE) {
				filePath += ".amr";
			} else {
				L.e(TAG, "暂时只支持图片语音");
				return;
			}
			FileCode.getInstance().base64Str2File(msg.getBody(), filePath);
			msg.setBody(filePath);
			L.i(TAG, "文件存储成功,filePath=" + filePath);
		}*/
		if (msg.getContentType() != Msg.MSG_TYPE_TEXT) {
			String filePath = BitmapUtil.getPath(GlobalContext.getInstance())
					+ UUIDGenerator.getUUID();
			if (msg.getContentType() == Msg.MSG_TYPE_PIC) {
				filePath += ".jpg";
			} else if (msg.getContentType() == Msg.MSG_TYPE_VOICE) {
				filePath += ".amr";
			} else {
				L.e(TAG, "暂时只支持图片语音");
				return;
			}
			new FileDownTask(msg.getBody(),filePath,new UpdateViewHelper.UpdateViewListener(){
				@Override
				public void onComplete(Object obj) {
					if(StrUtils.isEmpty(obj.toString())){
						L.e(TAG,"文件下载失败...");
					}else{
						msg.setBody(obj.toString());
						handlerMsg(msg);
					}
				}
			});
		}else{
			handlerMsg(msg);
		}
	}

	private void handlerMsg(Msg msg) {
		MsgDb msgDb = null;
		MsgEvent msgEvent = null;
		boolean showNotify = false;
		String myId = GlobalContext.getInstance().getUid();
		if (msg.getMsgType() == Msg.MSG_GROUP) {
			if (myId.equals(msg.getOwner())) {
				msg.setIsOut(Msg.MSG_OUT);
			} else {
				msg.setIsOut(Msg.MSG_IN);
			}
			msgDb = MsgInfoHelper.getInstance().objSwitch(msg);
			msgEvent = new MsgEvent(msg);
			if (myId.equals(msg.getOwner())) {
				msgDb.setIsRead(0);
			}
			if (GlobalContext.getInstance().isAtyFocus(CHAT_MULTI_ATY_NAME)
					&& cuurentId.equals(msgDb.getToId())) {
				EventBus.getDefault().post(msgEvent);
			} else {
				msgDb.setIsRead(1);
				showNotify = true;
			}
		} else {
			msg.setIsOut(Msg.MSG_IN);
			msgDb = MsgInfoHelper.getInstance().objSwitch(msg);
			msgEvent = new MsgEvent(msg);
			if (GlobalContext.getInstance().isAtyFocus(CHAT_ATY_NAME)
					&& cuurentId.equals(msgDb.getToId())) {
				msgDb.setIsRead(0);
				EventBus.getDefault().post(msgEvent);
			} else {
				msgDb.setIsRead(1);
				showNotify = true;
			}
		}
		MsgInfoHelper.getInstance().insertMsg(msgDb);
		MsgNotify.getInstance().notifyMsg(msg, showNotify);
		if (msgDb.getIsRead().intValue() == 1) {
			EventBus.getDefault().post(new MainUnReadMsgEvent());
		}
	}

	public void sendDormMsg(String topic, DormMsg msg) {
		MqttMessage message = new MqttMessage(StrUtils.Str2Byte(GsonUtil
				.getInstance().obj2Str(msg)));
		MqttHelper.getInstance().sendMsg(topic, message);
	}

	public void sendMsg(String topic, Msg msg) {
		MqttMessage message = new MqttMessage(StrUtils.Str2Byte(GsonUtil
				.getInstance().obj2Str(msg)));
		MqttHelper.getInstance().sendMsg(topic, message);
	}

	public void sendPackMsg(String topic, Msg msg) {
		MqttMessage message = new MqttMessage(MsgPackUtil.write(msg));
		MqttHelper.getInstance().sendMsg(topic, message);
	}
}
