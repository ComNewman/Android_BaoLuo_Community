package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 活动列表实体
 * @author xiangyang.fu
 *
 */
public class EventInfoList implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<EventInfo> Items;
	private int Count;
	
	public EventInfoList() {
	}

	public EventInfoList(List<EventInfo> items, int count) {
		super();
		Items = items;
		this.Count = count;
	}
	
	
	public List<EventInfo> getItems() {
		return Items;
	}

	public void setItems(List<EventInfo> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		this.Count = count;
	}
}
