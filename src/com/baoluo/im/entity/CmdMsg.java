package com.baoluo.im.entity;

/**
 * 个人指令
 * 
 * @author tao.lai
 */
public class CmdMsg {
	private int CmdType;
	private String ToId;
	private long Expired;
	private Object Data;

	public int getCmdType() {
		return CmdType;
	}

	public void setCmdType(int cmdType) {
		CmdType = cmdType;
	}

	public String getToId() {
		return ToId;
	}

	public void setToId(String toId) {
		ToId = toId;
	}

	public long getExpired() {
		return Expired;
	}

	public void setExpired(long expired) {
		Expired = expired;
	}

	public Object getData() {
		return Data;
	}

	public void setData(Object data) {
		Data = data;
	}
}
