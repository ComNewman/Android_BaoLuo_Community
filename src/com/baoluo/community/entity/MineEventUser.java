package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 个人  活动 参与头像
 * @author xiangyang.fu
 *
 */
public class MineEventUser implements Serializable{

	private static final long serialVersionUID = 1L;
	private int Count;
	private String Name;
	private String Id;
	private String Face;
	public MineEventUser(int count, String name, String id, String face) {
		super();
		Count = count;
		Name = name;
		Id = id;
		Face = face;
	}
	public MineEventUser() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getFace() {
		return Face;
	}
	public void setFace(String face) {
		Face = face;
	}
	
	
}
