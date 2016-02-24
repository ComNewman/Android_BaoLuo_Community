package com.baoluo.community.core;

public class Configs {
	/**
	 * 版本号
	 */
	public static final int VERSION = 1;

	/**
	 * 与服务器连接状态
	 */
	public static final int CONNECTION = 1;
	public static final int DISCONNECTION = 0;

	/**
	 * 客户端线程池设置
	 */
	public static final int MIN_THREAD_NUM = 2;
	public static final int MAX_THREAD_NUM = 4;

	/**
	 * 字符编码设置
	 */
	public static final String CHAR_SET = "UTF-8";

	public static final String RESPONSE_ERROR = "fail";
	public static final int RESPONSE_OK = 200;

	// 活动默认经纬度
	public static final double latitude = 22.566939;
	public static final double longitude = 113.870201;

	/**
	 * 收藏类型 tpye 心情 0 话题 1 活动2
	 */
	public static final int COLLECT_TYPE_HUMOR = 0;
	public static final int COLLECT_TYPE_EVENT = 2;
	public static final int COLLECT_TYPE_TOPIC = 1;

	public static final String DB_NAME = "baoluo";

	/**
	 * 推送范围
	 */
	public static final int PUBLISH_RANGE_PUBLIC = 0;
	public static final int PUBLISH_RANGE_APPOINT = 1;
	public static final int PUBLISH_RANGE_SCHOOL = 2;
	public static final int PUBLISH_RANGE_GROUP = 3;
	public static final int PUBLISH_RANGE_FRIENDS = 4;
	public static final int PUBLISH_RANGE_SELF = 2;
	/**
	 * 推送类型
	 */
	public static final int PUBLISH_TYPE_SINGLE = 1;
	public static final int PUBLISH_TYPE_GROUP = 2;
	/**标签长度**/
	public static final int TAG_LEN = 12;
	/**活动标题、地址文字的长度**/
	public static final int TITLE_LCT_LEN = 24;
	/**活动内容的长度**/
	public static final int EVENT_CONTENT_LEN = 600;
	/**评论内容的长度**/
	public static final int COMMENT_LEN = 60;
}
