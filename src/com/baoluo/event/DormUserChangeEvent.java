package com.baoluo.event;

import com.baoluo.community.entity.DormUser;

/**
 * 宿舍成员变动通知
 * 
 * @author tao.lai
 * 
 */
public class DormUserChangeEvent {

	private byte optionType; // 1:增加， 2：减少用户      3:房间开启
	private String dormId;
	private long expired;
	private DormUser userInfo;
	
	public DormUserChangeEvent(byte optionType,String dormId,DormUser userInfo){
		this.optionType = optionType;
		this.dormId = dormId;
		this.userInfo = userInfo;
	}
	
	public DormUserChangeEvent(byte optionType,String dormId){
		this.optionType = optionType;
		this.dormId = dormId;
	}

	public byte getOptionType() {
		return optionType;
	}

	public void setOptionType(byte optionType) {
		this.optionType = optionType;
	}

	public String getDormId() {
		return dormId;
	}

	public void setDormId(String dormId) {
		this.dormId = dormId;
	}

	public long getExpired() {
		return expired;
	}

	public void setExpired(long expired) {
		this.expired = expired;
	}

	public DormUser getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(DormUser userInfo) {
		this.userInfo = userInfo;
	}
}
