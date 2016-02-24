package com.baoluo.im.entity;

import java.io.Serializable;

import com.baoluo.community.entity.BlogUser;
/**
 * 已经排队的宿舍
 * @author xiangyang.fu
 *
 */
public class AttendDorm implements Serializable{

	private static final long serialVersionUID = 1L;
	private BlogUser Owner;
	private String Id;
	private String Name;
	private String ShowTime;
	private int UpNum;
	private int CurrentNumber;
	
	public AttendDorm() {
	 
	}
	public AttendDorm(BlogUser owner, String id, String name, String showTime,
			int upNum, int currentNumber) {
		super();
		Owner = owner;
		Id = id;
		Name = name;
		ShowTime = showTime;
		UpNum = upNum;
		CurrentNumber = currentNumber;
	}
	public BlogUser getOwner() {
		return Owner;
	}
	public void setOwner(BlogUser owner) {
		Owner = owner;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getName() {
		return Name;
	}
	public void setName(String name) {
		Name = name;
	}
	public String getShowTime() {
		return ShowTime;
	}
	public void setShowTime(String showTime) {
		ShowTime = showTime;
	}
	public int getUpNum() {
		return UpNum;
	}
	public void setUpNum(int upNum) {
		UpNum = upNum;
	}
	public int getCurrentNumber() {
		return CurrentNumber;
	}
	public void setCurrentNumber(int currentNumber) {
		CurrentNumber = currentNumber;
	}
}
