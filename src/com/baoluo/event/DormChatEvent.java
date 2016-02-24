package com.baoluo.event;

import com.baoluo.im.entity.DormMsg;

public class DormChatEvent {

	private DormMsg dormMsg;

	public DormMsg getDormMsg() {
		return dormMsg;
	}

	public void setDormMsg(DormMsg dormMsg) {
		this.dormMsg = dormMsg;
	}

	public DormChatEvent(DormMsg dormMsg) {
		this.dormMsg = dormMsg;
	}

}
