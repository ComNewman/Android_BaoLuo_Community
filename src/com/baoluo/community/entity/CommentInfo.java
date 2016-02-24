package com.baoluo.community.entity;

import java.io.Serializable;

/**
 * 评论
 * @author Ryan_Fu
 *  2015-5-18
 */
public class CommentInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	// 当前评论ID
	private String Id;
	//评论人头像地址
	private  String Face;
	//
	private CommentInfo QuoteCommit;
	// 评论的评论ID
	private String CommitId;
	// 评论用户
	private BlogUser BlogUser;
	// 内容
	private String Content;
	// 赞数
	private int PraiseNum;
	// 显示时间
	private String ShowTime;
	//评论时间
	private String CreateTime;
	
	public CommentInfo() {
		super();
	}

	
	public CommentInfo(String id, String face, CommentInfo quoteCommit,
			String commitId, com.baoluo.community.entity.BlogUser blogUser,
			String content, int praiseNum, String showTime, String createTime) {
		super();
		Id = id;
		Face = face;
		QuoteCommit = quoteCommit;
		CommitId = commitId;
		BlogUser = blogUser;
		Content = content;
		PraiseNum = praiseNum;
		ShowTime = showTime;
		CreateTime = createTime;
	}


	public CommentInfo getQuoteCommit() {
		return QuoteCommit;
	}

	public void setQuoteCommit(CommentInfo quoteCommit) {
		QuoteCommit = quoteCommit;
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

	public String getCommitId() {
		return CommitId;
	}

	public void setCommitId(String commitId) {
		CommitId = commitId;
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

	public int getPraiseNum() {
		return PraiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		PraiseNum = praiseNum;
	}

	public String getShowTime() {
		return ShowTime;
	}

	public void setShowTime(String showTime) {
		ShowTime = showTime;
	}


	public String getCreateTime() {
		return CreateTime;
	}


	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	
}
