package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 话题列表
 * 
 * @author xiangyang.fu
 * 
 */
public class TopicInfoList implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<TopicInfo> Items;
	private int Count;

	public TopicInfoList() {
		super();
	}

	public TopicInfoList(List<TopicInfo> items, int count) {
		super();
		Items = items;
		Count = count;
	}

	public List<TopicInfo> getItems() {
		return Items;
	}

	public void setItems(List<TopicInfo> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

}
