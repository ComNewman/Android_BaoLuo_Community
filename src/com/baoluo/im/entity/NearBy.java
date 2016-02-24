package com.baoluo.im.entity;

import java.io.Serializable;
/**
 * 附近的人
 * @author xiangyang.fu
 *
 */
public class NearBy implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Uid;
	private String UserNick;
	private String Face;
	private String DisDesc;
	private String ShowTime;
	private double Dis;
	public NearBy() {
		 
	}
	public NearBy(String uid, String userNick, String face, String disDesc,
			String showTime, double dis) {
		super();
		Uid = uid;
		UserNick = userNick;
		Face = face;
		DisDesc = disDesc;
		ShowTime = showTime;
		Dis = dis;
	}
	public String getUid() {
		return Uid;
	}
	public void setUid(String uid) {
		Uid = uid;
	}
	public String getUserNick() {
		return UserNick;
	}
	public void setUserNick(String userNick) {
		UserNick = userNick;
	}
	public String getFace() {
		return Face;
	}
	public void setFace(String face) {
		Face = face;
	}
	public String getDisDesc() {
		return DisDesc;
	}
	public void setDisDesc(String disDesc) {
		DisDesc = disDesc;
	}
	public String getShowTime() {
		return ShowTime;
	}
	public void setShowTime(String showTime) {
		ShowTime = showTime;
	}
	public double getDis() {
		return Dis;
	}
	public void setDis(double dis) {
		Dis = dis;
	}
	
	
}
