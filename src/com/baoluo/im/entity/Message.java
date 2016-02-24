package com.baoluo.im.entity;

import java.util.Date;

import com.baoluo.community.ui.customview.SlideCutView;


/**
 * 消息页实体
 * @author tao.lai
 *
 */
public class Message {

	private String uid;
	private String name;
	private String avatar;
	private Date itime;
	private byte MsgType;     //4个人消息， 8群消息
	private String message;
	private int unReadNum;   
	private Date setTopTime;
	private SlideCutView slideCutView;
	
	private byte searchResultType;    // 1:person   2:group    3:content
	
	public static final byte RESULT_PERSON = 1;
	public static final byte RESULT_GROUP = 2;
	public static final byte RESULT_CONTENT = 3;
	
	
	public byte getSearchResultType() {
		return searchResultType;
	}
	public void setSearchResultType(byte searchResultType) {
		this.searchResultType = searchResultType;
	}
	public String getUid() {
		return uid;
	}
	public void setUid(String uid) {
		this.uid = uid;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAvatar() {
		return avatar;
	}
	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}
	public Date getItime() {
		return itime;
	}
	public void setItime(Date itime) {
		this.itime = itime;
	}
	public byte getMsgType() {
		return MsgType;
	}
	public void setMsgType(byte msgType) {
		MsgType = msgType;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getUnReadNum() {
		return unReadNum;
	}
	public void setUnReadNum(int unReadNum) {
		this.unReadNum = unReadNum;
	}
	public Date getSetTopTime() {
		return setTopTime;
	}
	public void setSetTopTime(Date setTopTime) {
		this.setTopTime = setTopTime;
	}
	public SlideCutView getSlideCutView() {
		return slideCutView;
	}
	public void setSlideCutView(SlideCutView slideCutView) {
		this.slideCutView = slideCutView;
	}
}
