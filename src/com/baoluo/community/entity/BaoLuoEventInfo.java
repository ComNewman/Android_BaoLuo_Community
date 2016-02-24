package com.baoluo.community.entity;

import java.io.Serializable;

/**
 * 首页 公开活动
 * 
 * @author xiangyang.fu
 * 
 */
public class BaoLuoEventInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String Id;
	private String Pictures;
	private String Title;

	public BaoLuoEventInfo() {
		super();
		// TODO Auto-generated constructor stub
	}

	public BaoLuoEventInfo(String id, String pictures, String title) {
		super();
		Id = id;
		Pictures = pictures;
		Title = title;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getPictures() {
		return Pictures;
	}

	public void setPictures(String pictures) {
		Pictures = pictures;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

}
