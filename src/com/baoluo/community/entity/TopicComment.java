package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 话题评论实体
 * @author xiangyang.fu
 *
 */
public class TopicComment implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Id;
	private String OriginalTitle;
	private BlogUser BlogUser;
	private String Content;
	private String ShowTime;
	// 高逼格	1    普通回复	2
	private int TopicType;
	private boolean IsOpposes;
	private boolean IsPraise;
	private List<PictureInfo>  Picture;
	
	public TopicComment() {
		
	}
	public TopicComment(String id, String originalTitle,
			com.baoluo.community.entity.BlogUser blogUser, String content,
			String showTime, int topicType, boolean isOpposes,
			boolean isPraise, List<PictureInfo> picture) {
		super();
		Id = id;
		OriginalTitle = originalTitle;
		BlogUser = blogUser;
		Content = content;
		ShowTime = showTime;
		TopicType = topicType;
		IsOpposes = isOpposes;
		IsPraise = isPraise;
		Picture = picture;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getOriginalTitle() {
		return OriginalTitle;
	}
	public void setOriginalTitle(String originalTitle) {
		OriginalTitle = originalTitle;
	}
	public BlogUser getBlogUser() {
		return BlogUser;
	}
	public void setBlogUser(BlogUser blogUser) {
		BlogUser = blogUser;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getShowTime() {
		return ShowTime;
	}
	public void setShowTime(String showTime) {
		ShowTime = showTime;
	}
	public int getTopicType() {
		return TopicType;
	}
	public void setTopicType(int topicType) {
		TopicType = topicType;
	}
	public boolean isIsOpposes() {
		return IsOpposes;
	}
	public void setIsOpposes(boolean isOpposes) {
		IsOpposes = isOpposes;
	}
	public boolean isIsPraise() {
		return IsPraise;
	}
	public void setIsPraise(boolean isPraise) {
		IsPraise = isPraise;
	}

	public List<PictureInfo> getPicture() {
		return Picture;
	}

	public void setPicture(List<PictureInfo> picture) {
		Picture = picture;
	}
	
}
