package com.baoluo.community.entity;

import java.io.Serializable;

/**
 * 用户
 * 
 * @author Ryan_Fu 2015-5-18
 */
public class BlogUser implements Serializable {
	private static final long serialVersionUID = -5152771016221268038L;
	// userId
	private String Id;
	// 名称
	private String Name;
	// 头像地址
	private String Face;
	//女0 男 1 保密2
	private int Sex;
	
	private int Level;
	
	public BlogUser() {
		super();
	}
	
	public BlogUser(String id, String name, String face, int sex, int level) {
		super();
		Id = id;
		Name = name;
		Face = face;
		Sex = sex;
		Level = level;
	}
	
	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
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

	@Override
	public String toString() {
		return "BlogUser [Id=" + Id + ", Name=" + Name + ", Face=" + Face
				+ ", Sex=" + Sex + ", Level=" + Level + "]";
	}
	
	
}