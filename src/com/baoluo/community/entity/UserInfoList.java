package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 用户列表
 * @author xiangyang.fu
 *
 */
public class UserInfoList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<BlogUser> Items;
	
	private int Count;

	public UserInfoList() {
		super();
	}

	public UserInfoList(List<BlogUser> items, int count) {
		super();
		Items = items;
		Count = count;
	}

	public List<BlogUser> getItems() {
		return Items;
	}

	public void setItems(List<BlogUser> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}
	
	
}
