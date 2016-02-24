package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 联系人列表实体
 * 
 * @author xiangyang.fu
 * 
 */
public class ContactsInfoList implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<ContactsInfo> Items;
	private int Count;

	public ContactsInfoList() {

	}

	public ContactsInfoList(List<ContactsInfo> items, int count) {
		super();
		Items = items;
		this.Count = count;
	}

	public List<ContactsInfo> getItems() {
		return Items;
	}

	public void setItems(List<ContactsInfo> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		this.Count = count;
	}

}
