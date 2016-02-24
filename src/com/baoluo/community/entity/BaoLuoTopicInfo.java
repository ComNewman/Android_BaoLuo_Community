package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 首页 话题
 * @author xiangyang.fu
 *
 */
public class BaoLuoTopicInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private String Id;
	private String Title;
	
	public BaoLuoTopicInfo() {
		 
	}

	public BaoLuoTopicInfo(String id, String title) {
		super();
		Id = id;
		Title = title;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}
	
	
}
