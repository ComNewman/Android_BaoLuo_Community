package com.baoluo.im.entity;

import java.io.Serializable;

/**
 * cmd 指令 （好友添加请求实体）
 * @author tao.lai
 *
 */
public class AddFriEntity implements Serializable{
	private static final long serialVersionUID = 1L;
	
	private String Uid;
	private String Name;
	private String Face;
	private int Ret;
	
	public int getRet() {
		return Ret;
	}
	public void setRet(int ret) {
		Ret = ret;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
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

}
