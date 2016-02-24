package com.baoluo.event;

import com.baoluo.im.entity.Msg;

public class MsgEvent {
	private Msg msg;
	private String filePath;

	public Msg getMsg() {
		return msg;
	}

	public String getFilePath() {
		return filePath;
	}

	public MsgEvent(Msg msg) {
		this.msg = msg;
	}

	public MsgEvent(Msg msg, String filePath) {
		this.msg = msg;
		this.filePath = filePath;
	}

}
