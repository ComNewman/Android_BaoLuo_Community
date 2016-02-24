package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.List;

import com.baoluo.community.entity.LocationArea;
import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.entity.SchoolInfo;
/**
 * 用户信息
 * @author xiangyang.fu
 *
 */
public class UserInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Id;
	private String NickName;
	private String Face;
	//封面图片的URL
	private String Cover;
	//关注数
	private int FollowingNum;
	//粉丝数
	private int FollowersNum;
	//微博数
	private int BlogNum;
	//女	0    男	1    保密  2
	private int Sex;
	private String UserName;
	private String Email;
	private LocationArea LocationArea;
	private SchoolInfo School;
	private boolean IsFriends;
	private boolean IsFollowing;
	
	private String QRCode;
	private int Point;
	private List<PictureInfo> AlbumList;
	
	public UserInfo() {
		
	}
	
	public UserInfo(String id, String nickName, String face, String cover,
			int followingNum, int followersNum, int blogNum, int sex,
			String userName, String email,
			com.baoluo.community.entity.LocationArea locationArea,
			SchoolInfo school, boolean isFriends, boolean isFollowing,
			String qRCode, int point, List<PictureInfo> albumList) {
		super();
		Id = id;
		NickName = nickName;
		Face = face;
		Cover = cover;
		FollowingNum = followingNum;
		FollowersNum = followersNum;
		BlogNum = blogNum;
		Sex = sex;
		UserName = userName;
		Email = email;
		LocationArea = locationArea;
		School = school;
		IsFriends = isFriends;
		IsFollowing = isFollowing;
		QRCode = qRCode;
		Point = point;
		AlbumList = albumList;
	}
	
	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}

	public int getPoint() {
		return Point;
	}

	public void setPoint(int point) {
		Point = point;
	}

	public List<PictureInfo> getAlbumList() {
		return AlbumList;
	}

	public void setAlbumList(List<PictureInfo> albumList) {
		AlbumList = albumList;
	}

	public LocationArea getLocationArea() {
		return LocationArea;
	}


	public void setLocationArea(LocationArea locationArea) {
		LocationArea = locationArea;
	}


	public String getUserName() {
		return UserName;
	}

	public void setUserName(String userName) {
		UserName = userName;
	}

	public String getEmail() {
		return Email;
	}

	public void setEmail(String email) {
		Email = email;
	}


	public SchoolInfo getSchool() {
		return School;
	}

	public void setSchool(SchoolInfo school) {
		School = school;
	}

	public boolean isIsFriends() {
		return IsFriends;
	}

	public void setIsFriends(boolean isFriends) {
		IsFriends = isFriends;
	}

	public boolean isIsFollowing() {
		return IsFollowing;
	}

	public void setIsFollowing(boolean isFollowing) {
		IsFollowing = isFollowing;
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

	public String getFace() {
		return Face;
	}

	public void setFace(String face) {
		Face = face;
	}

	public String getCover() {
		return Cover;
	}

	public void setCover(String cover) {
		Cover = cover;
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

	public int getSex() {
		return Sex;
	}

	public void setSex(int sex) {
		Sex = sex;
	}
}
