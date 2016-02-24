package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 宿舍用户信息
 * @author xiangyang.fu
 *
 */
public class DormUser implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private String StartTime;
	private String Id;
	private String Name;
	private String Face;
	private int Sex;
	
	
	public String getStartTime() {
		return StartTime;
	}

	
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

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

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}

	public DormUser() {
		super();
	}

	public DormUser(String startTime, String id, String name, String face, int sex) {
		super();
		StartTime = startTime;
		Id = id;
		Name = name;
		Face = face;
		Sex = sex;
	}
}
