package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 用户自己的信息
 * @author xiangyang.fu
 *
 */
public class UserSelf implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String Id;
	private String NickName;
	private int FollowingNum;
	private int FollowersNum;
	
	private int BlogNum;
	private String Face;
	private int Sex;  //0 nv  1 nan  2 保密
	private String Cover;
	private String QRCode;
	private String UserName;
	private SchoolInfo School;
	private LocationArea LocationArea;
	
	
	public String getQRCode() {
		return QRCode;
	}
	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}
	public String getUserName() {
		return UserName;
	}
	public void setUserName(String userName) {
		UserName = userName;
	}
	public SchoolInfo getSchool() {
		return School;
	}
	public void setSchool(SchoolInfo school) {
		School = school;
	}
	
	public LocationArea getLocationArea() {
		return LocationArea;
	}
	public void setLocationArea(LocationArea locationArea) {
		LocationArea = locationArea;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getNickName() {
		return NickName;
	}
	public void setNickName(String nickName) {
		NickName = nickName;
	}
	public int getFollowingNum() {
		return FollowingNum;
	}
	public void setFollowingNum(int followingNum) {
		FollowingNum = followingNum;
	}
	public int getFollowersNum() {
		return FollowersNum;
	}
	public void setFollowersNum(int followersNum) {
		FollowersNum = followersNum;
	}
	public int getBlogNum() {
		return BlogNum;
	}
	public void setBlogNum(int blogNum) {
		BlogNum = blogNum;
	}
	public String getFace() {
		return Face;
	}
	public void setFace(String face) {
		Face = face;
	}
	public int getSex() {
		return Sex;
	}
	public void setSex(int sex) {
		Sex = sex;
	}
	public String getCover() {
		return Cover;
	}
	public void setCover(String cover) {
		Cover = cover;
	}
	public UserSelf() {
	}
	public UserSelf(String id, String nickName, int followingNum,
			int followersNum, int blogNum, String face, int sex, String cover,
			String qRCode, String userName, SchoolInfo school,
			com.baoluo.community.entity.LocationArea locationArea) {
		super();
		Id = id;
		NickName = nickName;
		FollowingNum = followingNum;
		FollowersNum = followersNum;
		BlogNum = blogNum;
		Face = face;
		Sex = sex;
		Cover = cover;
		QRCode = qRCode;
		UserName = userName;
		School = school;
		LocationArea = locationArea;
	}
	
	
}
