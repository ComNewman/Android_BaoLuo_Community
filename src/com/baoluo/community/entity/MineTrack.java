package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 个人  足迹
 * @author xiangyang.fu
 *
 */
public class MineTrack implements Serializable{

	private static final long serialVersionUID = 1L;
	private String Month;
	private String UserFace;
	private String UserName;
	private int Points;
	private String CreateTime;
	private int IsSelf;
	private String Title;
	private String Desc;
	
	public MineTrack(String month, String userFace, String userName,
			int points, String createTime, int isSelf, String title, String desc) {
		super();
		Month = month;
		UserFace = userFace;
		UserName = userName;
		Points = points;
		CreateTime = createTime;
		IsSelf = isSelf;
		Title = title;
		Desc = desc;
	}
	public MineTrack() {
	}
	public String getMonth() {
		return Month;
	}
	public void setMonth(String month) {
		Month = month;
	}
	public String getUserFace() {
		return UserFace;
	}
	public void setUserFace(String userFace) {
		UserFace = userFace;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public int getPoints() {
		return Points;
	}
	public void setPoints(int points) {
		Points = points;
	}
	public String getCreateTime() {
		return CreateTime;
	}
	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}
	public int getIsSelf() {
		return IsSelf;
	}
	public void setIsSelf(int isSelf) {
		IsSelf = isSelf;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getDesc() {
		return Desc;
	}
	public void setDesc(String desc) {
		Desc = desc;
	}
	
}
