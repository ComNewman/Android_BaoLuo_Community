package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.List;
/**
 *  附近的人列表
 * @author xiangyang.fu
 *
 */
public class NearByList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<NearBy> Items;
	private int  Count;
	public NearByList() {
		  
	}
	public NearByList(List<NearBy> items, int count) {
		super();
		Items = items;
		Count = count;
	}
	public List<NearBy> getItems() {
		return Items;
	}
	public void setItems(List<NearBy> items) {
		Items = items;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	
}
