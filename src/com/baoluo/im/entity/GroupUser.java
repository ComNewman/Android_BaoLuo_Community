package com.baoluo.im.entity;

import java.io.Serializable;
/**
 * 群成员实体
 * @author xiangyang.fu
 *
 */
public class GroupUser implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Id;
	private String DisplayName;
	private String Face;
	
	public GroupUser() {
		
	}

	public GroupUser(String id, String displayName, String face) {
		super();
		Id = id;
		DisplayName = displayName;
		Face = face;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public String getFace() {
		return Face;
	}

	public void setFace(String face) {
		Face = face;
	}
}
