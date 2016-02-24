package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 新生报到 实体
 * @author xiangyang.fu
 *
 */
public class BlogUserListInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<BlogUser> Items;
	private int Count;
	
	public BlogUserListInfo() {
		
	}
	public BlogUserListInfo(List<BlogUser> items, int count) {
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
