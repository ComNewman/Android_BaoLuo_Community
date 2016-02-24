package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 宿舍列表返回
 * @author xiangyang.fu
 *
 */
public class DormInfoList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<DormInfo> Items;
	
	private int Count;

	public DormInfoList() {
		super();
	}

	public DormInfoList(List<DormInfo> items, int count) {
		super();
		Items = items;
		Count = count;
	}

	public List<DormInfo> getItems() {
		return Items;
	}

	public void setItems(List<DormInfo> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}
}
