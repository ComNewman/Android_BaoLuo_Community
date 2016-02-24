package com.baoluo.community.entity;

import java.io.Serializable;
/**
 *  话题标签列表
 * @author xiangyang.fu
 *
 */
public class TopicTag implements Serializable{

	private static final long serialVersionUID = 1L;
	private String Id;
	private String Face;
	private String Title;
	private String Desc;
	private int Count;
	public TopicTag() {
	 
	}
	public TopicTag(String id, String face, String title, String desc, int count) {
		super();
		Id = id;
		Face = face;
		Title = title;
		Desc = desc;
		Count = count;
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
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	
	@Override
	public String toString() {
		return "TopicTag [Id=" + Id + ", Face=" + Face + ", Title=" + Title
				+ ", Desc=" + Desc + ", Count=" + Count + "]";
	}
	
}
