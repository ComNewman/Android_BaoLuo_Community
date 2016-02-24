package com.baoluo.notify;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.Notification.Builder;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.im.entity.AddFriEntity;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.AtyChatMqtt;
import com.baoluo.im.ui.AtyDormChat;
import com.baoluo.im.ui.AtyMultiChatMqtt;
import com.baoluo.im.ui.AtyNewFri;

@SuppressLint("NewApi")
public class MsgNotify {
	private static final String TAG = "MsgNotify";

	private static final int NEW_MSG_ID = 101;
	private static final int OTHER_MSG_ID = 102;

	private static MsgNotify instance;
	private static NotificationManager nm;

	
	public static MsgNotify getInstance(){
		if(instance == null){
			instance = new MsgNotify();
			Context ctx = GlobalContext.getInstance();
			nm = (NotificationManager) ctx.getSystemService(ctx.NOTIFICATION_SERVICE);
		}
		return instance;
	}
	
	public void notifyMsg(Msg msg, boolean showNotify) {
		boolean dnded = dnded(msg);
		L.i(TAG, "showNotify="+showNotify+","+"dnded = " + dnded);
		if (!dnded && (msg.getIsOut() == Msg.MSG_IN)) {
			ring();
			if (showNotify) {
				showStatusNotify(msg);
			}
		}
	}

	/**
	 * 清除消息通知
	 */
	public void clearMsgNotify(){
		nm.cancel(NEW_MSG_ID);
	}

	/**
	 * 清楚其他 通知
	 */
	public void clearOtherNotify(){
		nm.cancel(OTHER_MSG_ID);
	}

	/**
	 * 通知栏提醒
	 * @param msg
	 */
	private void showMsgNotify(Msg msg) {
		Context ctx = GlobalContext.getInstance();
		ContactsInfo ci = null;
		GroupInfo gi = null;
		boolean isMultiChat = (Msg.MSG_GROUP == msg.getMsgType() ? true : false);
		if (isMultiChat) {
			gi = GroupHelper.getInstance().getGroupByGid(msg.getToId());
		} else {
			ci = ContactsHelper.getInstance().getContactsById(msg.getOwner());
		}
		Intent it = new Intent();
		it.setClass(ctx, isMultiChat ? AtyMultiChatMqtt.class
				: AtyChatMqtt.class);
		it.putExtra(isMultiChat ? AtyMultiChatMqtt.EXTRA_GROUP
				: AtyChatMqtt.EXTRA_CONTACTS, isMultiChat ? gi : ci);
		//Bitmap bm = GlobalContext.getInstance().imageLoader.loadImageSync(ci.getFace());
		showNotify(it, isMultiChat ? gi.getName() : ci.getAccountName(), msg.getBody(), null);
	}

	/**
	 * 状态栏提醒
	 * @param msg
	 */
	private void showStatusNotify(Msg msg){
		Context ctx = GlobalContext.getInstance();
		ContactsInfo ci = null;
		GroupInfo gi = null;
		boolean isMultiChat = (Msg.MSG_GROUP == msg.getMsgType() ? true : false);
		if (isMultiChat) {
			gi = GroupHelper.getInstance().getGroupByGid(msg.getToId());
		} else {
			ci = ContactsHelper.getInstance().getContactsById(msg.getOwner());
		}
		Intent it = new Intent();
		/*it.setClass(ctx, isMultiChat ? AtyMultiChatMqtt.class : AtyChatMqtt.class);
		it.putExtra(isMultiChat ? AtyMultiChatMqtt.EXTRA_GROUP : AtyChatMqtt.EXTRA_CONTACTS,isMultiChat ? gi : ci);*/
		it.setClass(ctx, MainActivity.class);
		PendingIntent pd = PendingIntent.getActivity(ctx, 0, it, 0);

		Notification newMsgNotify = new Notification.Builder(ctx)
				.setContentTitle(isMultiChat ? gi.getName() : ci.getAccountName())
				.setContentText(msg.getBody())
				.setSmallIcon(R.drawable.bl_logo)
				.setDefaults(Notification.DEFAULT_SOUND)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setDefaults(Notification.DEFAULT_LIGHTS)
				.setTicker((isMultiChat ? gi.getName() : ci.getAccountName()) + ":" + msg.getBody())
				.setAutoCancel(true)
				.setContentIntent(pd)
				.build();
		nm.notify(NEW_MSG_ID, newMsgNotify);
	}


	public void addFriMsg(AddFriEntity entity) {
		ring();
		Context ctx = GlobalContext.getInstance();
		Intent it = new Intent();
		it.setClass(ctx, AtyNewFri.class);
		it.putExtra("newFri", entity);
		showNotify(it,entity.getName() + "请求加你为好友","");
	}
	
	/**
	 * 排队完成，进入宿舍聊天通知
	 */
	public void AttendDorm(String dormId){
		ring();
		Context ctx = GlobalContext.getInstance();
		Intent it = new Intent();
		it.setClass(ctx, AtyDormChat.class);
		it.putExtra(AtyDormChat.DORM_ID, dormId);
		showNotify(it,"宿舍排队完成，点击进入","");//R.drawable.ic_launcher
	}
	
	private void showNotify(Intent it,String title,String content){
		showNotify(it,title,content,R.drawable.ic_launcher,null);
	}
	
	private void showNotify(Intent it,String title,String content,Bitmap largeIcon){
		showNotify(it,title,content,R.drawable.ic_launcher,largeIcon);
	}
	
	private void showNotify(Intent it,String title,String content,int iconRes,Bitmap largeIcon){
		Context ctx = GlobalContext.getInstance();
		NotificationManager nm = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		PendingIntent contentIndent = PendingIntent.getActivity(ctx, 0, it,
				PendingIntent.FLAG_UPDATE_CURRENT);
		Builder build = new Notification.Builder(ctx)
				.setContentTitle(title)
				.setContentText(content)
				.setSmallIcon(R.drawable.bl_logo)
				.setDefaults(Notification.DEFAULT_SOUND)
				.setDefaults(Notification.DEFAULT_VIBRATE)
				.setDefaults(Notification.DEFAULT_LIGHTS)
				.setTicker(content)
				.setAutoCancel(true)
				.setContentIntent(contentIndent)
				.setLargeIcon(largeIcon);
		if(largeIcon != null) {
			build.setLargeIcon(largeIcon);
		}
		Notification newMsgNotify = build.build();
		nm.notify(OTHER_MSG_ID, newMsgNotify);
	}

	private void ring() {
		Uri notification = RingtoneManager
				.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
		Ringtone r = RingtoneManager.getRingtone(GlobalContext.getInstance(),
				notification);
		r.play();
	}
	
	private boolean dnded(Msg msg){
		if(msg.getMsgType() == Msg.MSG_GROUP){
			return GroupHelper.getInstance().getDnded(msg.getToId());
		}else{
			return ContactsHelper.getInstance().getDnded(msg.getOwner());
		}
	}
}
