package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 请求心情评论列表返回数据
 * @author Ryan_Fu
 *  2015-6-4
 */
public class CommentInfoList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<CommentInfo> Items;
	private int Count;
	public CommentInfoList() {
		super();
	}
	
	public CommentInfoList(List<CommentInfo> items, int count) {
		super();
		Items = items;
		Count = count;
	}

	public List<CommentInfo> getItems() {
		return Items;
	}
	public void setItems(List<CommentInfo> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}
	
	
}
