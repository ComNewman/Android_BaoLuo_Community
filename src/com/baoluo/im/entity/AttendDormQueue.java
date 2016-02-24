package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 已经排队的队列
 * @author xiangyang.fu
 *
 */
public class AttendDormQueue implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<AttendDorm> Items;
	private int Count;
	public AttendDormQueue(List<AttendDorm> items, int count) {
		super();
		Items = items;
		Count = count;
	}
	public AttendDormQueue() {
		 
	}
	public List<AttendDorm> getItems() {
		return Items;
	}
	public void setItems(List<AttendDorm> items) {
		Items = items;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	
	
}
