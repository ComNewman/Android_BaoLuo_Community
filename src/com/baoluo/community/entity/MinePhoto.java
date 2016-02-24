package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 个人       相册  数据
 * @author xiangyang.fu
 *
 */
public class MinePhoto implements Serializable{

	private static final long serialVersionUID = 1L;
	private int Month;
	private int Integral;
	private int TotalNumber;
	private List<MineEventImg> MicroBlogUrl;
	private List<MineEventImg> EventimgList;
	private List<MineEventUser> UserTotalLists;
	public MinePhoto(int month, int integral, int totalNumber,
			List<MineEventImg> microBlogUrl, List<MineEventImg> eventimgList,
			List<MineEventUser> userTotalLists) {
		super();
		Month = month;
		Integral = integral;
		TotalNumber = totalNumber;
		MicroBlogUrl = microBlogUrl;
		EventimgList = eventimgList;
		UserTotalLists = userTotalLists;
	}
	public MinePhoto() {
		super();
	}
	public int getMonth() {
		return Month;
	}
	public void setMonth(int month) {
		Month = month;
	}
	public int getIntegral() {
		return Integral;
	}
	public void setIntegral(int integral) {
		Integral = integral;
	}
	public int getTotalNumber() {
		return TotalNumber;
	}
	public void setTotalNumber(int totalNumber) {
		TotalNumber = totalNumber;
	}
	public void setMicroBlogUrl(List<MineEventImg> microBlogUrl) {
		MicroBlogUrl = microBlogUrl;
	}
	public List<MineEventImg> getMicroBlogUrl() {
		return MicroBlogUrl;
	}
	public List<MineEventImg> getEventimgList() {
		return EventimgList;
	}
	public void setEventimgList(List<MineEventImg> eventimgList) {
		EventimgList = eventimgList;
	}
	public List<MineEventUser> getUserTotalLists() {
		return UserTotalLists;
	}
	public void setUserTotalLists(List<MineEventUser> userTotalLists) {
		UserTotalLists = userTotalLists;
	}
}
