package com.baoluo.community.entity;

import java.io.Serializable;

/**
 * 用户信息
 * @author Ryan_Fu
 *  2015-5-5
 */
public class AccountInfo implements Serializable{
	
	private static final long serialVersionUID = -5602957686594971349L;
	private String id;
	private String account;
	private String password;
	//
	private int pageIndex;
	
	
	
	public AccountInfo() {
		
	}
	public AccountInfo(String id, String account, String password, int pageIndex) {
		super();
		this.id = id;
		this.account = account;
		this.password = password;
		this.pageIndex = pageIndex;
	}
	public int getPageIndex() {
		return pageIndex;
	}
	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getAccount() {
		return account;
	}
	public void setAccount(String account) {
		this.account = account;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
}
