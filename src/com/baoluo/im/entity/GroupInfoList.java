package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 群组列表
 * 
 * @author xiangyang.fu
 * 
 */
public class GroupInfoList implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<GroupInfo> Items;
	private int Count;

	public GroupInfoList() {

	}

	public GroupInfoList(List<GroupInfo> items, int count) {
		super();
		Items = items;
		Count = count;
	}

	public List<GroupInfo> getItems() {
		return Items;
	}

	public void setItems(List<GroupInfo> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

}
