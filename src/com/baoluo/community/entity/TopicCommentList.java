package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 话题评论列表
 * @author xiangyang.fu
 *
 */
public class TopicCommentList implements Serializable{

	private static final long serialVersionUID = 1L;
	private List<TopicComment> Items;
	private int Count;
	public TopicCommentList() {
		 
	}
	public TopicCommentList(List<TopicComment> items, int count) {
		super();
		Items = items;
		Count = count;
	}
	public List<TopicComment> getItems() {
		return Items;
	}
	public void setItems(List<TopicComment> items) {
		Items = items;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
}
