package com.baoluo.community.core;

public class UrlHelper {

	/**
	 * 注意！ URL地址以"/"结尾的，参数的param.get("id")
	 */
	// 私有key
	public static final String BAPLUO_APIKEY = "bapluo_apikey:b21095301fc3531f";
	public static final String DOMAIN = "http://api.baoluo.com/";
   //public static final String DOMAIN = "http://192.168.16.99:53634/";
	/** 图片上传 **/
	public static final String FILE_URL = DOMAIN + "api/upload/";

	/** LBS and VOICE **/
	public static final String LBS_NEAR_USER = DOMAIN + "api/LBS";
	public static final String LBS_UPDATE_LOCAITON = DOMAIN + "api/lbs/PutLbs";
	public static final String UPLOAD_VOICE = DOMAIN + "api/upload/PutVoice";

	/** 用户相关 **/
	public static final String LOGIN 				= DOMAIN + "token";
	public static final String CHECK_LOGIN		 	= DOMAIN + "api/Account";
	public static final String REGISTER 			= DOMAIN + "api/Account/Register";
	public static final String MODIFYPASS		 	= DOMAIN + "api/Account/ModifyPass";
	public static final String COLLECT 				= DOMAIN + "api/Collect";
	public static final String USER_INFO 			= DOMAIN + "api/User";
	public static final String CONTACTS 			= DOMAIN + "api/Contacts";
	public static final String CURRENT_USER_INFO 	= DOMAIN + "api/User/CurrentUserInfo";
	public static final String FRIEND_SEARCH 		= DOMAIN + "api/Contacts/Search";
	public static final String FRIEND_ADD 			= DOMAIN + "api/Contacts";
	public static final String FRIEND_ADD_ASK	 	= DOMAIN + "api/Contacts/AddContactAsk";
	public static final String QRCODE_ADD_FRI 		= DOMAIN + "api/Contacts/ScanQRcode";
	public static final String GET_REG_CODE 		= DOMAIN + "api/User/GetRegisterVerificationCode";
	public static final String GET_FRI 				= DOMAIN + "api/Contacts/";
	public static final String USERINFO_AMEND 		= DOMAIN + "api/User/ModifyInfo";
	public static final String DEL_FRI 				= DOMAIN + "api/Contacts/Delete";
	public static final String FIND_PASSWORD 		= DOMAIN + "api/Account/FindPassword";
	public static final String GET_FIND_CODE 		= DOMAIN + "api/User/GetFindPasswordVerificationCode";
	public static final String ACCOUNT_SETTING 		= DOMAIN + "api/Account/AccountSet";
	public static final String ATTENTION_USER 		= DOMAIN + "api/Follow";
	public static final String SET_PING 			= DOMAIN + "api/User/SetPing";
	public static final String ATTENTION_LIST 		= DOMAIN + "api/Follow/Following";
	public static final String MINE_PHOTO_DATA 		= DOMAIN + "api/User/UserTotalList";
	public static final String MINE_TRACK_DATA 		= DOMAIN + "api/User/TrackList";
	public static final String UPDATE_FRI_DIS_NAME 	= DOMAIN + "api/Contacts/UpdateDisplayName";
	public static final String CHANGE_PSWD 			= DOMAIN + "api/Account/ChangePassword";
	
	public static final String INFORM				= DOMAIN + "api/Inform/AddInform";
	public static final String INFORM_USER			= DOMAIN + "api/Informants/AddInformants";
	/** group **/
	public static final String GROUP_LIST 			= DOMAIN + "api/Group";
	public static final String MY_GROUP_LIST 		= DOMAIN + "api/Group/MyGroup";
	public static final String GROUP_ADD_USER 		= DOMAIN + "api/Group/GroupInvite";
	public static final String GROUP_KICK 			= DOMAIN + "api/Group/Eliminate";
	public static final String GROUP_EXIT 			= DOMAIN + "api/Group/ExitGroup";
	public static final String GROUP_AMEND 			= DOMAIN + "api/Group/PutGroup";
	public static final String GET_GROUP_INFO 		= DOMAIN + "api/Group/";
	public static final String GROUP_QRCODE 		= DOMAIN + "api/Group/ScanQRcode";
	public static final String GROUP_CREATE 		= DOMAIN + "api/Group";

	// 宝落首页相关
	public static final String BAOLUO_HOME 			= DOMAIN + "api/Home/HomeBaoluo";
	public static final String NEW_USER 			= DOMAIN + "api/Home/NewUser";

	/** 心情相关--start **/
	public static final String HUMOR_LIST 				= DOMAIN + "api/microblog";
	public static final String HUMOR_PUT 				= DOMAIN + "api/microblog";
	public static final String HUMOR_DEL				= DOMAIN + "api/microblog/";
	public static final String HUMOR_FORWARD 			= DOMAIN + "api/Forward";
	public static final String HUMOR_FORWARDINFO 		= DOMAIN + "api/Forward";
	public static final String HUMOR_COMMENT 			= DOMAIN + "api/Commit";
	public static final String HUMOR_PRAISECOMMIT 		= DOMAIN + "api/Praise/CommitPraise";
	public static final String HUMOR_COMMITPRAISELIST 	= DOMAIN + "api/Praise/CommitPraise";
	public static final String HUMOR_COMMITDELETE 		= DOMAIN + "api/Commit";
	public static final String HUMOR_PRAISE	 			= DOMAIN + "api/Praise";
	public static final String HUMOR_GET 				= DOMAIN + "api/microblog";
	public static final String HUMOR_COMMENTINFO 		= DOMAIN + "api/Commit";
	// public static final String HUMOR_LIST_CLASSIFY = DOMAIN +
	// "api/MicroBlog/GetMicroBlogClassify";
	/** 心情相关--end **/

	/** 活动相关--start **/
	public static final String EVENT_LIST 			= DOMAIN + "api/Event";
	public static final String EVENT_POST 			= DOMAIN + "api/Event";
	public static final String EVENT_ONT 			= DOMAIN + "api/Event/";
	public static final String EVENT_COMMENT_LIST 	= DOMAIN + "api/Event/Blogs";
	public static final String EVENT_COMMENT_POST 	= DOMAIN + "api/Event/PostBlog";
	public static final String EVENT_MY_LIST 		= DOMAIN + "api/Event/EventUserTimeLine";
	public static final String EVENT_ATTEND 		= DOMAIN + "api/Event/AttendEvent";
	public static final String EVENT_FOLLOW 		= DOMAIN + "api/Follow/FollowEvent";
	public static final String EVENT_ATTEND_USER 	= DOMAIN + "api/Event/AttendUserList";
	public static final String EVENT_LIST_CLASSIFY 	= DOMAIN + "api/Event/GetListOrderBy";
	public static final String EVENT_AMEND 			= DOMAIN + "api/Event/PutEvent";
	// EventGroup
	public static final String EVENT_GROUP_INFO 		= DOMAIN + "api/Event/";
	public static final String EVENT_GROUP_KICK 		= DOMAIN + "api/Event/KickingPlayer";
	public static final String EVENT_GROUP_DISSOLVE 	= DOMAIN + "api/Event/DeleteEvent";
	public static final String EVENT_GROUP_EXIT 		= DOMAIN + "api/Event/ExitEvent";
	public static final String EVENT_GROUP_ISOPEN 		= DOMAIN + "api/Event/EventGroupInfo";

	/** 话题相关 --start **/
	public static final String TOPIC_LIST 				= DOMAIN + "api/Topic";
	public static final String TOPIC_ONE 				= DOMAIN + "api/Topic";
	public static final String TOPIC_POST 				= DOMAIN + "api/Topic";
	public static final String TOPIC_ATTEND 			= DOMAIN + "api/Topic/Attend";
	public static final String TOPIC_PRAISE 			= DOMAIN + "api/Praise/TopicPraise";
	public static final String TOPIC_LOW 				= DOMAIN + "api/Praise/TopicOppose";
	public static final String TOPIC_COMMENT_LIST 		= DOMAIN + "api/Topic/Blogs";
	public static final String TOPIC_COMMENT_POST 		= DOMAIN + "api/Topic/PostBlog";
	public static final String TOPIC_HIGHT_REPLY 		= DOMAIN + "api/Topic/Topics";
	public static final String TOPIC_TAG_SEARCH 		= DOMAIN + "api/Topic/SearchTag";
	public static final String TOPIC_SEARCH 			= DOMAIN + "api/Topic/SearchTopic";
	public static final String TOPIC_COMMENT_NEW 		= DOMAIN + "api/Topic/Blogs";
	public static final String TOPIC_COMMENT_WONDERFUL 	= DOMAIN + "api/Topic/TopicsList";
	
	public static final String TOPIC_TAG_LIST			= DOMAIN + "api/TopicTag";
	public static final String TOPIC_THEME_LIST			= DOMAIN + "api/Topic/v2";
	/** 宿舍 **/
	// CREATE POST DORM_LIST GET
	public static final String DORM 					= DOMAIN + "api/Dorm";
	public static final String DORM_ATTEND 				= DOMAIN + "api/Dorm/DromAttend";
	public static final String DORM_EXIT 				= DOMAIN + "api/Dorm/DromExit";
	public static final String DORM_ATTEND_QUEUE 		= DOMAIN + "api/Dorm/MyDormList";
	public static final String DORM_SELECT_CHAT_TYPE 	= DOMAIN + "api/Dorm/ClassSort";
	public static final String DORM_MSG_SEND 			= DOMAIN + "api/Dorm/Publish";
	public static final String DORM_CURRENT 			= DOMAIN + "api/Dorm";
	public static final String DORM_CHAT_NOW 			= DOMAIN + "api/Dorm/MeDorm";
	public static final String DORM_GET_NICK 			= DOMAIN + "api/Dorm/GetRandomNick";

	/** 消息相关 **/
	public static final String MSG_USER_SEND 			= DOMAIN + "api/Im/PublishMsg";
	public static final String MSG_GROUP_SEND 			= DOMAIN + "api/Im/PublishGroupMsg";
	public static final String MSG_USER_PACK_SEND 		= DOMAIN + "api/Im/PublishByte";
	public static final String MSG_OFF_LINE 			= DOMAIN + "api/Im/GetOfflineMessage";
	/**标签相关**/
	public static final String TAG_SEARCH				= DOMAIN + "api/TagLib/GetSearchTag";
	public static final String TAG_ADD					= DOMAIN + "api/TagLib/AddTag";
	public static final String TAG_DEFAULT				= DOMAIN + "api/TagLib";
}
