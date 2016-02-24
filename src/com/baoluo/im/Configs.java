package com.baoluo.im;


public class Configs {
	
	public static int SERVER_PORT = 5222;  
	public static String SERVER_HOST = "192.168.82.2";   //"openfire1.baoluo.dev";    
	public static String SERVER_NAME = "baoluo";   //"openfire";    
	public static String SERVER_GOUP_NAME = "conference.openfire"; 
	public static String SERVER_SUFFIX = "/Smack";
	
	/** Mqtt 配置  **/
//	public static final String MQ_HOST        =  "tcp://mqttd.baoluo.com:1883";
	public static final String MQ_HOST        =  "tcp://mqttd.baoluo.dev:1883";
	public static final int    MQ_PORT        =  12;
	public static final String MQ_USERNAME    =  "admin";
	public static final String MQ_PWD         =  "admin";
	
	public static final String MQ_CLIENT_ID_PRE   =  "baoluo.im.id.";
	public static final String MQ_USER_PRE    =  "baoluo/im/uid/";
	public static final String MQ_GROUP_PRE   =  "baoluo/im/gid/";
	public static final String MQ_CMD_PRE     =  "baoluo/im/cmd/";
	public static final String MQ_DORM        =  "baoluo/dorm/id/";
	public static final String MQ_SYS_CHANNEL =  "baoluo/im/sys";
	public static final String MQ_DORM_GLOBAL =  "baoluo/dorm";
	public static final String MQ_EVENT_PRE   =  "baoluo/event/id/";
	
	public static final String QR_CODE_USER_ID	= 	"baoluo.im.gid.";
	public static final String QR_CODE_GROUP_ID	= 	"baoluo.im.Uid.";
	
	
	/**
	 * cmd 指令
	 */
	public static final int CODE_ADD_FRI_SUCCEED = 1001;
	public static final int CODE_ADD_FRI = 1002;
	public static final int CODE_ADD_FRI_RESULT = 1003;
	
	public static final int CODE_INVITE_TO_GROUP = 2002; // 邀请加入群
	public static final int CODE_REMOVE_GROUP = 2003; //
	public static final int CODE_GROUP_UPDATE = 2004;    // 群信息更新
	public static final int CODE_GROUP_DISSOLVE = 2005;  //
	
	public static final int DORM_ADD_USER = 3001; // 房间成员增加
	public static final int DORM_MINUS_USER = 3002; //房间成员减少
	public static final int DORM_START_CHAT = 3003;  //进入聊天房间
	public static final int DORM_CREATE_EVENT = 4000;//新建活动群 通知
}
