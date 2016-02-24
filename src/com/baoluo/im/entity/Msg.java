package com.baoluo.im.entity;

import org.msgpack.annotation.Message;

/**
 * MQtt 消息实体
 * 
 * @author tao.lai
 * 
 */
@Message
public class Msg implements Cloneable{

	private String Id;
	private String Owner;
	private String ToId;
	private byte MsgType; // 消息 1在线； 2离线      4个人消息， 8群消息      43活动群解散
	private byte ContentType; // 消息类型 1文本 ；2图片 3语音；4视频
	private String Body;
	private long Expired; // 时间s
	
	private byte isOut;     //消息进出            0：发出      1：收到
	private boolean showTimed; 

	public static final byte MSG_ON_LINE = 1;
	public static final byte MSG_OFF_LINE = 2;
	public static final byte MSG_PERSON = 4;
	public static final byte MSG_GROUP = 8;
	
	public static final byte MSG_IN = 1;
	public static final byte MSG_OUT = 0;

	public static final byte MSG_TYPE_TEXT = 1;
	public static final byte MSG_TYPE_PIC = 2;
	public static final byte MSG_TYPE_VOICE = 3;
	public static final byte MSG_TYPE_VIDEO = 4;

	public Msg() {
	}

	public Msg(String toId, byte ContentType, String Body) {
		this.ToId = toId;
		this.ContentType = ContentType;
		this.Body = Body;
		//this.isOut = isOut;
	}

	public boolean isShowTimed() {
		return showTimed;
	}

	public void setShowTimed(boolean showTimed) {
		this.showTimed = showTimed;
	}

	public byte getIsOut() {
		return isOut;
	}

	public void setIsOut(byte isOut) {
		this.isOut = isOut;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String owner) {
		Owner = owner;
	}

	public String getToId() {
		return ToId;
	}

	public void setToId(String toId) {
		ToId = toId;
	}

	public byte getMsgType() {
		return MsgType;
	}

	public void setMsgType(byte msgType) {
		MsgType = msgType;
	}

	public byte getContentType() {
		return ContentType;
	}

	public void setContentType(byte contentType) {
		ContentType = contentType;
	}

	public String getBody() {
		return Body;
	}

	public void setBody(String body) {
		Body = body;
	}

	public long getExpired() {
		return Expired;
	}

	public void setExpired(long expired) {
		Expired = expired;
	}
	
	
	@Override  
    public Object clone() throws CloneNotSupportedException  
    {  
        return super.clone();  
    }  
}
