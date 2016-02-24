package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 推送模型
 * 
 * @author xiangyang.fu
 * 
 */
public class NotificationModel implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<NotificationInfo> Items;

	private int count;

	private String time;

	public NotificationModel() {
		
	}

	public NotificationModel(List<NotificationInfo> items, int count,
			String time) {
		super();
		Items = items;
		this.count = count;
		this.time = time;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public List<NotificationInfo> getItems() {
		return Items;
	}

	public void setItems(List<NotificationInfo> items) {
		Items = items;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
