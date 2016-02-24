package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
/**
 * 宝落首页数据
 * @author xiangyang.fu
 *
 */
public class BaoLuoHome implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private List<BaoLuoHumorInfo> TodayMicroblog;
	private List<BaoLuoEventInfo> EventList;
	private List<BaoLuoTopicInfo> HomeTopic;
	
	public BaoLuoHome() {
		super();
	}
	public BaoLuoHome(List<BaoLuoHumorInfo> todayMicroblog,
			List<BaoLuoEventInfo> eventList, List<BaoLuoTopicInfo> homeTopic) {
		super();
		TodayMicroblog = todayMicroblog;
		EventList = eventList;
		HomeTopic = homeTopic;
	}
	public List<BaoLuoHumorInfo> getTodayMicroblog() {
		return TodayMicroblog;
	}
	public void setTodayMicroblog(List<BaoLuoHumorInfo> todayMicroblog) {
		TodayMicroblog = todayMicroblog;
	}
	public List<BaoLuoEventInfo> getEventList() {
		return EventList;
	}
	public void setEventList(List<BaoLuoEventInfo> eventList) {
		EventList = eventList;
	}
	public List<BaoLuoTopicInfo> getHomeTopic() {
		return HomeTopic;
	}
	public void setHomeTopic(List<BaoLuoTopicInfo> homeTopic) {
		HomeTopic = homeTopic;
	}
	
	
}
