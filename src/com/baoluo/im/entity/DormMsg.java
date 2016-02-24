package com.baoluo.im.entity;

import java.util.Date;

import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;


public class DormMsg {
	
	private String Id;
	private String To;
	private byte MsgType;
	private long Expired;
	private Data Data;

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTo() {
		return To;
	}

	public void setTo(String to) {
		To = to;
	}

	public byte getMsgType() {
		return MsgType;
	}

	public void setMsgType(byte msgType) {
		MsgType = msgType;
	}

	public long getExpired() {
		return Expired;
	}

	public void setExpired(long expired) {
		Expired = expired;
	}

	public Data getData() {
		return Data;
	}

	public void setData(Data data) {
		Data = data;
	}
	
	public static DormMsg Msg2DormMsg(Msg msg,String toId){
		DormMsg dormMsg = new DormMsg();
		dormMsg.setTo(toId);
		dormMsg.setMsgType(msg.getMsgType());
		dormMsg.setExpired((new Date()).getTime());
		
		Form form = new Form();
		UserSelf mySelf = SettingUtility.getUserSelf();
		form.setId(mySelf.getId());
		form.setName(mySelf.getUserName());
		form.setSex((byte)mySelf.getSex());
		form.setFace(mySelf.getFace());
		
		Data data = new Data();
		data.setBody(msg.getBody());
		data.setContentType(msg.getContentType());
		data.setForm(form);
		dormMsg.setData(data);
		return dormMsg;
	}
	
	public static Msg DormMsg2Msg(DormMsg dormMsg){
		Msg msg = new Msg();
		msg.setOwner(dormMsg.getData().getForm().getId());
		msg.setToId(dormMsg.getTo());
		msg.setMsgType(dormMsg.getMsgType());
		msg.setContentType(dormMsg.getData().getContentType());
		msg.setBody(dormMsg.getData().getBody());
		msg.setExpired(dormMsg.getExpired());
		return msg;
	}

	public static class Data{
		private Form Form;
		private byte ContentType;
		private String Body;
		
		public Form getForm() {
			return Form;
		}
		public void setForm(Form form) {
			Form = form;
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
	}
	
	public static class Form{
		private String Id;
		private String Name;
		private String Face;
		private byte Sex;
		
		public String getId() {
			return Id;
		}
		public void setId(String id) {
			Id = id;
		}
		public String getName() {
			return Name;
		}
		public void setName(String name) {
			Name = name;
		}
		public String getFace() {
			return Face;
		}
		public void setFace(String face) {
			Face = face;
		}
		public byte getSex() {
			return Sex;
		}
		public void setSex(byte sex) {
			Sex = sex;
		}
	}
}
