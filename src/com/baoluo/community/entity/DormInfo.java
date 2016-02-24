package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 宿舍
 * @author xiangyang.fu
 *
 */
public class DormInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	//	未开启0	开启1		满人2		关闭3
	private int Status;
	private boolean IsAttend;
	private String Id;
	private String Desc;
	private int DormType;
	private BlogUser Owner;
	private PictureInfo Pictures;
	private String ShowTime;
	private List<DormUser> Users;
	
	
	public DormInfo() {
		
	}
	public DormInfo(int status, boolean isAttend, String id, String desc,
			int dormType, BlogUser owner, PictureInfo pictures,
			String showTime, List<DormUser> users) {
		super();
		Status = status;
		IsAttend = isAttend;
		Id = id;
		Desc = desc;
		DormType = dormType;
		Owner = owner;
		Pictures = pictures;
		ShowTime = showTime;
		Users = users;
	}
	
	public int getStatus() {
		return Status;
	}
	public void setStatus(int status) {
		Status = status;
	}
	public boolean isIsAttend() {
		return IsAttend;
	}
	public void setIsAttend(boolean isAttend) {
		IsAttend = isAttend;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	public int getDormType() {
		return DormType;
	}
	public void setDormType(int dormType) {
		DormType = dormType;
	}
	public BlogUser getOwner() {
		return Owner;
	}
	public void setOwner(BlogUser owner) {
		Owner = owner;
	}
	public PictureInfo getPictures() {
		return Pictures;
	}
	public void setPictures(PictureInfo pictures) {
		Pictures = pictures;
	}
	public String getShowTime() {
		return ShowTime;
	}
	public void setShowTime(String showTime) {
		ShowTime = showTime;
	}
	public List<DormUser> getUsers() {
		return Users;
	}
	public void setUsers(List<DormUser> users) {
		Users = users;
	}
	
}
