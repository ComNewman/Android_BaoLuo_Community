package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 搜索话题标签返回 数据实体
 * @author xiangyang.fu
 *
 */
public class TagListInfo implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private List<TagInfo> Items;
	private int Count;
	
	public TagListInfo() {
	}
	public TagListInfo(List<TagInfo> items, int count) {
		super();
		Items = items;
		Count = count;
	}
	public List<TagInfo> getItems() {
		return Items;
	}
	public void setItems(List<TagInfo> items) {
		Items = items;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
}
