package com.baoluo.community.util;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.baoluo.im.entity.MsgInfo;

/**
 * 数据库帮助类
 * 
 * @author Ryan_Fu 2015-5-7
 */
public class DatabaseUtil extends SQLiteOpenHelper {

	private final String TAG = "DatabaseUtil";
	private final String TABLE_NAME = "message";
	private final static String DB_NAME = "buluo_db";

	public DatabaseUtil(Context context) {
		this(context, DB_NAME, null, 1);
	}

	public DatabaseUtil(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		String sql = "create table "
				+ TABLE_NAME
				+ "(fromUid varchar2(20), toUid varchar2(20),  messageType int, itime varchar2(50), content varchar2(240),address varchar2(100))";
		Log.i(TAG, "sql=" + sql);
		db.execSQL(sql);
		String userSql = "create table friend(accId varchar2(20), friendId varchar2(20), remark varchar2(20))";
		Log.i(TAG, "userSql=" + userSql);
		db.execSQL(userSql);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

	public ArrayList<MsgInfo> queryMessages(String fromUid, String toUid) {
		ArrayList<MsgInfo> list = new ArrayList<MsgInfo>();
		SQLiteDatabase db = getReadableDatabase();
		String selection = "fromUid=? or toUid=?";
		String[] selectionArgs = new String[] { "'" + fromUid + "'",
				"'" + toUid + "'" };
		Cursor cursor = db.query("message", null, selection, selectionArgs,
				null, null, null);

		// 如果游标为空（查找失败）或查到的信息数位0，返回null
		if (cursor == null || cursor.getCount() == 0) {
			return list;
		}
		Log.i(TAG, "queryMessages() cursor.count=" + cursor.getCount());

		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			MsgInfo message = new MsgInfo();
			/*
			 * message.setFromUid(cursor.getString(cursor.getColumnIndex("fromUid"
			 * )));
			 * message.setToUid(cursor.getString(cursor.getColumnIndex("toUid"
			 * ))); message.setMessageType(cursor.getInt(cursor.getColumnIndex(
			 * "messageType")));
			 * 
			 * message.setContent(cursor.getString(cursor.getColumnIndex("content"
			 * )));
			 * message.setUrl(cursor.getString(cursor.getColumnIndex("address"
			 * ))); list.add(message);
			 */
			cursor.moveToNext();
			// Log.i(TAG, "queryMessages()：查到一条消息");
		}

		cursor.close();
		db.close();
		return list;
	}

	/**
	 * 查询用户自己的信息
	 */
	/*
	 * public FriendInfo queryFriend(String selfId){ SQLiteDatabase
	 * db=getReadableDatabase(); FriendInfo friend=new FriendInfo();
	 * 
	 * return friend; }
	 */

	// //查询所有人的头像信息
	// public ArrayList<Map<String, String>> queryFriendsHead(String selfId){
	// ArrayList<Map<String, String>> list=new ArrayList<Map<String, String>>();
	// SQLiteDatabase db=getReadableDatabase();
	// String[] columns={"friendId","modifyTime"};
	// String selection=" selfId='"+selfId+"'";
	// Cursor cursor=db.query("friend", columns, selection, null, null, null,
	// null);
	//
	// //如果游标为空（查找失败）或查到的信息数位0，返回null
	// if(cursor==null || cursor.getCount()==0){
	// db.close();
	// Log.i(TAG, "DatabaseUtil queryFriends() 异常：游标为空");
	// return list;
	// }
	// // Log.i(TAG, "queryFriends() cursor.count="+cursor.getCount());
	// cursor.moveToFirst();
	// while(!cursor.isAfterLast()){
	// HashMap<String, String> map=new HashMap<String, String>();
	// FriendInfo friend=new FriendInfo();
	// map.put("friendId", cursor.getString(cursor.getColumnIndex("friendId")));
	// map.put("HeadModifyTime",
	// cursor.getString(cursor.getColumnIndex("modifyTime")));
	//
	// list.add(map);
	// cursor.moveToNext();
	// }
	// db.close();
	// return list;
	// }

	/**
	 * 查询好友信息
	 */
	/*
	 * public ArrayList<FriendInfo> queryFriends(int selfId){
	 * ArrayList<FriendInfo> list=new ArrayList<FriendInfo>(); SQLiteDatabase
	 * db=getReadableDatabase(); String[] columns={"accId","friendId","remark"};
	 * String selection=" accId ='"+selfId+"' and friendId!='"+selfId+"'";
	 * Cursor cursor=db.query("friend", columns, selection, null, null, null,
	 * null);
	 * 
	 * //如果游标为空（查找失败）或查到的信息数位0，返回null if(cursor==null || cursor.getCount()==0){
	 * db.close(); Log.i(TAG, "DatabaseUtil queryFriends() 异常：游标为空"); return
	 * list; } // Log.i(TAG, "queryFriends() cursor.count="+cursor.getCount());
	 * cursor.moveToFirst(); while(!cursor.isAfterLast()){ FriendInfo friend=new
	 * FriendInfo();
	 * friend.setAccId(cursor.getString(cursor.getColumnIndex("accId")));
	 * friend.setFriends(cursor.getString(cursor.getColumnIndex("friends")));
	 * friend.setRemark(cursor.getString(cursor.getColumnIndex("remark")));
	 * list.add(friend); cursor.moveToNext(); } db.close(); return list; }
	 */
	/**
	 * 插入消息的步骤：1.往message表中插入一条新消息 2.更新friend表的type,content,time字段
	 * 
	 * @param values
	 */
	public void insertMessage(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();

		// 插入一条新消息
		db.insert(TABLE_NAME, null, values);

		String fromUid = values.getAsString("fromUid");
		String toUid = values.getAsString("toUid");
		int messageType = values.getAsInteger("messageType");

		String itime = values.getAsString("itime");
		String content = values.getAsString("content");
		String address = values.getAsString("address");

		Log.i(TAG, "InsertMessage type=" + messageType + " content=" + content
				+ " time=" + itime + " self_If=" + fromUid + " friend_Id="
				+ toUid);
		//
		// //更新同好友最后的一条聊天消息的时间、内容、类型
		// ContentValues updates=new ContentValues();
		// updates.put("messageType", messageType);
		// updates.put("content", content);
		// updates.put("itime", itime);
		// String where = "accId ="+fromUid+" and friends = "+toUid;
		// int number = db.update("friend", updates, where, null);
		// Log.i(TAG, "insertMessage update friend number="+number);
		db.close();
	}

	/**
	 * 插入一个好友信息
	 * 
	 * @param values
	 */
	public void insertFriend(ContentValues values) {
		SQLiteDatabase db = getWritableDatabase();
		long id = db.insert("friend", null, values);
		if (id != -1) {
			Log.i(TAG, "向friend表插入" + values.get("friendId") + "成功");
		}
		db.close();
	}

	/**
	 * 单纯地删除一套消息，由于该消息不是列表中的最后一条，所以无需更新friend表中的字段 删除某一条消息的步骤：1.在message表中删除这条消息
	 * 2.如果被删除的消息类型为图片或语音，则应删除相应的图片、语音文件
	 * 
	 * @param ChatMessage
	 *            msg :待删除的消息
	 */
	/**
	 * 删除一条消息,由于该消息位于列表的最后一条，所以需要更新friend表的type,content,time字段
	 * 删除某一条消息的步骤：1.在message表中删除这条消息 2.获取message表中time字段值最大的消息
	 * 3.用time字段值最大的消息更新friend表的type,content,time字段
	 * 4.如果被删除的消息类型为图片或语音，则应删除相应的图片、语音文件
	 * 
	 * @param lastMsg
	 *            : 待删除的消息，位于消息列表的最后一条
	 * @param preMsg
	 *            : 待删除消息的前一条消息,当待删除消息被成功删除后，preMsg就成为消息列表中的最后一条消息了
	 */

	/**
	 * 查找出message表中time字段最大的记录
	 * 
	 * @param self
	 * @param friend
	 * @return
	 */
	/**
	 * 更新头像
	 * 
	 * @param selfId
	 * @param friendId
	 * @param headPath
	 * @param modifyTime
	 */

	/**
	 * 更新好友的type,content,time字段
	 * 
	 * @param message
	 * @return
	 */

	/**
	 * 删除与某人的会话———删除同某好友的全部聊天记录
	 * 
	 * @param self
	 * @param friend
	 */
}