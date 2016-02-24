package com.baoluo.im.entity;

import java.util.List;

public class MsgList {

	private List<Msg> Items;
	private int Count;

	public List<Msg> getItems() {
		return Items;
	}

	public void setItems(List<Msg> items) {
		Items = items;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}
}
