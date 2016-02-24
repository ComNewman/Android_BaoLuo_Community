package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 图片
 * 
 * @author Ryan_Fu 2015-5-18
 */
public class PictureInfo implements Serializable {

	private static final long serialVersionUID = -9079036358818028306L;
	// ID
	private String Id;
	// 原图地址
	private String Url;
	// 图片名称
	private String Name;
	// 图片上的标签
	private List<TagInfo> Tags;

	public PictureInfo() {
		super();
	}

	public PictureInfo(String id, String url, String name, List<TagInfo> tags) {
		super();
		Id = id;
		Url = url;
		Name = name;
		Tags = tags;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getUrl() {
		return Url;
	}

	public void setUrl(String url) {
		Url = url;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public List<TagInfo> getTags() {
		return Tags;
	}

	public void setTags(List<TagInfo> tags) {
		Tags = tags;
	}
}
