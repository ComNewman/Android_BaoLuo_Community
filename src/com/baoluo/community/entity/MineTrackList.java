package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 足迹列表
 * @author xiangyang.fu
 *
 */
public class MineTrackList implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<MineTrack> Items;
	private int Count;
	
	public MineTrackList(List<MineTrack> items, int count) {
		super();
		Items = items;
		Count = count;
	}
	public MineTrackList() {
		super();
	}
	public List<MineTrack> getItems() {
		return Items;
	}
	public void setItems(List<MineTrack> items) {
		Items = items;
	}
	public int getCount() {
		return Count;
	}
	public void setCount(int count) {
		Count = count;
	}
	
}	
