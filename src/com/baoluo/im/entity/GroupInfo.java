package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.Comparator;
import java.util.List;

import com.baoluo.im.Configs;
/**
 * 	群组实体
 * @author xiangyang.fu
 *
 */
public class GroupInfo implements Serializable,Comparator<GroupInfo>{

	private static final long serialVersionUID = 1L;
	private String Id;
	private String Name;
	//描述 介绍
	private String Dec;
	// 0 普通群  1 部落 2活动群
	private int GroupType;
	private String Owner;
	//成员
	private List<GroupUser> Users;
	private String CreateTime;
	private String QRCode;
	private String sortLetters;
	
	
	private boolean IsTop;
	private boolean IsHide;
	private String MyGroupName;
	private String GroupAvatar;
	
	
	public GroupInfo() {
		
	}
	
	
	public GroupInfo(String id, String name, String dec, int groupType,
			String owner, List<GroupUser> users, String createTime,
			String qRCode, String sortLetters, boolean isTop, boolean isHide,
			String myGroupName) {
		super();
		Id = id;
		Name = name;
		Dec = dec;
		GroupType = groupType;
		Owner = owner;
		Users = users;
		CreateTime = createTime;
		QRCode = qRCode;
		this.sortLetters = sortLetters;
		IsTop = isTop;
		IsHide = isHide;
		MyGroupName = myGroupName;
	}
	
	public String getGroupAvatar() {
		return GroupAvatar;
	}


	public void setGroupAvatar(String groupAvatar) {
		GroupAvatar = groupAvatar;
	}
	public boolean isIsTop() {
		return IsTop;
	}
	public void setIsTop(boolean isTop) {
		IsTop = isTop;
	}
	public boolean isIsHide() {
		return IsHide;
	}
	public void setIsHide(boolean isHide) {
		IsHide = isHide;
	}
	public String getMyGroupName() {
		return MyGroupName;
	}
	public void setMyGroupName(String myGroupName) {
		MyGroupName = myGroupName;
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

	public String getDec() {
		return Dec;
	}

	public void setDec(String dec) {
		Dec = dec;
	}

	public int getGroupType() {
		return GroupType;
	}

	public void setGroupType(int groupType) {
		GroupType = groupType;
	}

	public String getOwner() {
		return Owner;
	}

	public void setOwner(String owner) {
		Owner = owner;
	}

	
	public List<GroupUser> getUsers() {
		return Users;
	}


	public void setUsers(List<GroupUser> users) {
		Users = users;
	}


	public String getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(String createTime) {
		CreateTime = createTime;
	}

	public String getQRCode() {
		return QRCode;
	}

	public void setQRCode(String qRCode) {
		QRCode = qRCode;
	}
	
	public String getJid(){
		if (Id == null) return null;
		return Id + "@" + Configs.SERVER_NAME;
	}
	
	public String getSortLetters() {
		return sortLetters;
	}


	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}


	@Override
	public int compare(GroupInfo o1, GroupInfo o2) {
		
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
