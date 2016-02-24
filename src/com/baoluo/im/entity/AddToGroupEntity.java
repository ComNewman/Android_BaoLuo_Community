package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 邀请好友加群
 * @author xiangyang.fu
 *
 */
public class AddToGroupEntity implements Serializable{

	private static final long serialVersionUID = 1L;
	//群id
	private String Gid;
	//被邀请人 集合
	private List<GroupUser> Users;
	
	public AddToGroupEntity() {
		
	}

	public AddToGroupEntity(String gid, List<GroupUser> users) {
		super();
		Gid = gid;
		Users = users;
	}

	public String getGid() {
		return Gid;
	}

	public void setGid(String gid) {
		Gid = gid;
	}

	public List<GroupUser> getUsers() {
		return Users;
	}

	public void setUsers(List<GroupUser> users) {
		Users = users;
	}
	
}
