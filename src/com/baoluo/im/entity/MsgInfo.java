package com.baoluo.im.entity;

/**
 * 消息
 * @author tao.lai
 *
 */
public class MsgInfo {
	private Long id;
	private String friJid;
	private String jid;
	private String msg;
	private String itime;   
	private int type;   //消息状态    1:in 2:out
	private int msgType = MSG_TYPE_NORMAL;  //消息类型  1：普通文本   2：录音消息    3：图片消息
	private int status;  //文件接收状态            -1:拒绝接收    1:等待      2：成功   3：失败
	private String recordTime;  //语音时长
	private String filePath;
	
	public static int TYPE_IN = 1;
	public static int TYPE_OUT = 2;
	
	public static int MSG_TYPE_NORMAL = 1;
	public static int MSG_TYPE_RECORD = 2;
	public static int MSG_TYPE_PHOTO = 3;
	
	public static int STATUS_REFUSED = -1;
	public static int STATUS_WAIT = 1;
	public static int STATUS_SUCCESS = 2;
	public static int STATUS_FAIL = 3;
	
	public MsgInfo(){
	}
	
	public MsgInfo(Long id) {
        this.id = id;
    }

    public MsgInfo(Long id, String friJid, String jid, String msg, Integer type, String itime, Integer msgType, Integer status, String recordTime, String filePath) {
        this.id = id;
        this.friJid = friJid;
        this.jid = jid;
        this.msg = msg;
        this.type = type;
        this.itime = itime;
        this.msgType = msgType;
        this.status = status;
        this.recordTime = recordTime;
        this.filePath = filePath;
    }
	
	public MsgInfo(String jid, String msg, String itime, int type){
		this.jid = jid;
		this.msg = msg;
		this.itime = itime;
		this.type = type;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public String getFriJid() {
		return friJid;
	}

	public void setFriJid(String friJid) {
		this.friJid = friJid;
	}

	public String getJid() {
		return jid;
	}
	public void setJid(String userId) {
		this.jid = userId;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public String getItime() {
		return itime;
	}
	public void setItime(String itime) {
		this.itime = itime;
	}
	public int getType() {
		return type;
	}
	public void setType(int type) {
		this.type = type;
	}
	public int getMsgType() {
		return msgType;
	}
	public void setMsgType(int msgType) {
		this.msgType = msgType;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int status) {
		this.status = status;
	}
	public String getRecordTime() {
		return recordTime;
	}
	public void setRecordTime(String recordTime) {
		this.recordTime = recordTime;
	}
	public String getFilePath() {
		return filePath;
	}
	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}
}
