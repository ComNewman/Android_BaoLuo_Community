package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.Date;
/**
 * 转发
 * @author Ryan_Fu
 *  2015-5-22
 */
public class RelayInfo  implements Serializable{

	private static final long serialVersionUID = -1721247628288176601L;
	private BlogUser blogUser;
	private String _id;
	private Date PostTime;
	private String Content;
	
	public BlogUser getBlogUser() {
		return blogUser;
	}
	public void setBlogUser(BlogUser blogUser) {
		this.blogUser = blogUser;
	}
	public Date getPostTime() {
		return PostTime;
	}
	public void setPostTime(Date postTime) {
		PostTime = postTime;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String get_id() {
		return _id;
	}
	public void set_id(String _id) {
		this._id = _id;
	}
	public RelayInfo() {
	}
	public RelayInfo(BlogUser blogUser, String _id, Date postTime,
			String content) {
		super();
		this.blogUser = blogUser;
		this._id = _id;
		PostTime = postTime;
		Content = content;
	}
	
}