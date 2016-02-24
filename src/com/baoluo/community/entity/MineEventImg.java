package com.baoluo.community.entity;

import java.io.Serializable;

/**
 * 个人  、活动 图片
 * @author xiangyang.fu
 *
 */
public class MineEventImg implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String Url;
	public MineEventImg(String url) {
		super();
		Url = url;
	}
	public MineEventImg() {
		super();
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
	
	
}
