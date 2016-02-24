package com.baoluo.im.entity;

import java.io.Serializable;

/**
 * 推送实体
 * 
 * @author xiangyang.fu
 * 
 */
public class NotificationInfo implements Serializable {

	private static final long serialVersionUID = 1L;

	private String picPath;
	private String title;
	private String content;

	public NotificationInfo() {

	}

	public NotificationInfo(String picPath, String title, String content) {
		super();
		this.picPath = picPath;
		this.title = title;
		this.content = content;
	}

	public String getPicPath() {
		return picPath;
	}

	public void setPicPath(String picPath) {
		this.picPath = picPath;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

}
