package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 举报
 * @author xiangyang.fu
 *
 */
public class Inform implements Serializable{
	private static final long serialVersionUID = 1L;
	/**	 心情 0 活动 1 话题 2	**/
	private int Modules;
	/**	 色情0盗版1垃圾2暴力3政治4欺骗5其它6  	**/
	private int Type;
	private BlogUser User;
	private String Content;
	private String Mid;
	public Inform() {
		 
	}
	public Inform(int modules, int type, BlogUser user, String content,
			String mid) {
		super();
		Modules = modules;
		Type = type;
		User = user;
		Content = content;
		Mid = mid;
	}
	public int getModules() {
		return Modules;
	}
	public void setModules(int modules) {
		Modules = modules;
	}
	public int getType() {
		return Type;
	}
	public void setType(int type) {
		Type = type;
	}
	public BlogUser getUser() {
		return User;
	}
	public void setUser(BlogUser user) {
		User = user;
	}
	public String getContent() {
		return Content;
	}
	public void setContent(String content) {
		Content = content;
	}
	public String getMid() {
		return Mid;
	}
	public void setMid(String mid) {
		Mid = mid;
	}
}
