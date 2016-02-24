package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 话题列表分页
 * @author xiangyang.fu
 *
 */
public class TopicTagList implements Serializable{

	  
	private static final long serialVersionUID = 1L;
	private List<TopicTag> Items;
	private int Count;
	public TopicTagList() {
		 
	}
	public TopicTagList(List<TopicTag> items, int count) {
		super();
		Items = items;
		Count = count;
	}
	public List<TopicTag> getItems() {
		return Items;
	}
	public void setItems(List<TopicTag> items) {
		Items = items;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
}
