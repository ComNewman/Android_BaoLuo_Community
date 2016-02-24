package com.baoluo.event;

import org.jivesoftware.smack.packet.Message;

public class ChatEvent {
	private Message chatMsg;
	
	public ChatEvent(Message msg){
		this.chatMsg = msg;
	}
	
	public Message getChatMsg(){
		return this.chatMsg;
	}
	
	public String getMsgBody(){
		return this.chatMsg.getBody();
	}
}
