package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;

/**
 * 心情
 * 
 * @author Ryan_Fu 2015-5-14
 */
public class HumorInfo implements Serializable {
	
	private static final long serialVersionUID = 1L;

	// ID
	private String Id;
	
	private boolean IsOwer;
	private boolean IsCollect;
	private boolean IsPraise;
	// RelayId 原文 一转ID
	private String RelayId;
	// RawRelayId 二转ID
	private String RawRelayId;
	// 类型 心情1 话题2 活动 4 约5 小树林8
	private int BlogType;
	// 心情原文
	private HumorInfo RawRelay;
	// 内容
	private String Content;
	// 手机型号
	private String Device;
	// 发表时间
	private String PostTime;
	// 更新时间
	private String UpdateTime;
	// 作者
	private BlogUser BlogUser;
	// 转发数
	private int RelayNum;
	// 评论数
	private int CommentNum;
	// 点赞数
	private int PraiseNum;
	// 收藏数
	private int CollectNum;
	
	private int ViewNum;
	// LBS 地理位置
	private LocationInfo Location;
	// 图片集合
	private List<PictureInfo> Pictures;
	//
	private int CollectType;
	//
	private SchoolInfo School;
	//
	private String SexType;
	// 推送范围
	private String PublishType;
	// 语音
	private VoiceInfo Voice;
	// 标题
	private String Title;
	// 是否显示我的位置
	private boolean IsShowLocation;
	//
	private String StartTime;
	//
	private String EndTime;
	//
	private boolean OneSelf;
	//
	private String OriginId;
	//
	private String OriginInfo;

	// 评论集合
	private List<CommentInfo> Comments;
	// 点赞集合
	private List<Praise> Praises;
	// 转发集合
	private List<RelayInfo> RelayInfos;
	private String Distance;

    public HumorInfo(){
    	
    }
	
	public HumorInfo(String id, boolean isOwer, boolean isCollect,
			boolean isPraise, String relayId, String rawRelayId, int blogType,
			HumorInfo rawRelay, String content, String device, String postTime,
			String updateTime, com.baoluo.community.entity.BlogUser blogUser,
			int relayNum, int commentNum, int praiseNum, int collectNum,
			LocationInfo location, List<PictureInfo> pictures, int collectType,
			SchoolInfo school, String sexType, String publishType,
			VoiceInfo voice, String title, boolean isShowLocation,
			String startTime, String endTime, boolean oneSelf, String originId,
			String originInfo, List<CommentInfo> comments,
			List<Praise> praises, List<RelayInfo> relayInfos) {
		super();
		Id = id;
		IsOwer = isOwer;
		IsCollect = isCollect;
		IsPraise = isPraise;
		RelayId = relayId;
		RawRelayId = rawRelayId;
		BlogType = blogType;
		RawRelay = rawRelay;
		Content = content;
		Device = device;
		PostTime = postTime;
		UpdateTime = updateTime;
		BlogUser = blogUser;
		RelayNum = relayNum;
		CommentNum = commentNum;
		PraiseNum = praiseNum;
		CollectNum = collectNum;
		Location = location;
		Pictures = pictures;
		CollectType = collectType;
		School = school;
		SexType = sexType;
		PublishType = publishType;
		Voice = voice;
		Title = title;
		IsShowLocation = isShowLocation;
		StartTime = startTime;
		EndTime = endTime;
		OneSelf = oneSelf;
		OriginId = originId;
		OriginInfo = originInfo;
		Comments = comments;
		Praises = praises;
		RelayInfos = relayInfos;
	}
	
	
	
	public String getDistance() {
		return Distance;
	}

	public void setDistance(String distance) {
		Distance = distance;
	}

	public int getViewNum() {
		return ViewNum;
	}

	public void setViewNum(int viewNum) {
		ViewNum = viewNum;
	}

	public VoiceInfo getVoice() {
		return Voice;
	}

	public void setVoice(VoiceInfo voice) {
		Voice = voice;
	}

	public boolean isIsCollect() {
		return IsCollect;
	}


	public void setIsCollect(boolean isCollect) {
		IsCollect = isCollect;
	}


	public boolean isIsPraise() {
		return IsPraise;
	}


	public void setIsPraise(boolean isPraise) {
		IsPraise = isPraise;
	}


	public boolean isIsOwer() {
		return IsOwer;
	}

	public void setIsOwer(boolean isOwer) {
		IsOwer = isOwer;
	}
	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getRelayId() {
		return RelayId;
	}

	public void setRelayId(String relayId) {
		RelayId = relayId;
	}

	public String getRawRelayId() {
		return RawRelayId;
	}

	public void setRawRelayId(String rawRelayId) {
		RawRelayId = rawRelayId;
	}

	public int getBlogType() {
		return BlogType;
	}

	public void setBlogType(int blogType) {
		BlogType = blogType;
	}

	public HumorInfo getRawRelay() {
		return RawRelay;
	}

	public void setRawRelay(HumorInfo rawRelay) {
		RawRelay = rawRelay;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getDevice() {
		return Device;
	}

	public void setDevice(String device) {
		Device = device;
	}

	public String getPostTime() {
		return PostTime;
	}

	public void setPostTime(String postTime) {
		PostTime = postTime;
	}

	public String getUpdateTime() {
		return UpdateTime;
	}

	public void setUpdateTime(String updateTime) {
		UpdateTime = updateTime;
	}

	public BlogUser getBlogUser() {
		return BlogUser;
	}

	public void setBlogUser(BlogUser blogUser) {
		BlogUser = blogUser;
	}

	public int getRelayNum() {
		return RelayNum;
	}

	public void setRelayNum(int relayNum) {
		RelayNum = relayNum;
	}

	public int getCommentNum() {
		return CommentNum;
	}

	public void setCommentNum(int commentNum) {
		CommentNum = commentNum;
	}

	public int getPraiseNum() {
		return PraiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		PraiseNum = praiseNum;
	}

	public int getCollectNum() {
		return CollectNum;
	}

	public void setCollectNum(int collectNum) {
		CollectNum = collectNum;
	}

	public LocationInfo getLocation() {
		return Location;
	}

	public void setLocation(LocationInfo location) {
		Location = location;
	}

	public List<PictureInfo> getPictures() {
		return Pictures;
	}

	public void setPictures(List<PictureInfo> pictures) {
		Pictures = pictures;
	}


	public int getCollectType() {
		return CollectType;
	}

	public void setCollectType(int collectType) {
		CollectType = collectType;
	}

	public SchoolInfo getSchool() {
		return School;
	}

	public void setSchool(SchoolInfo school) {
		School = school;
	}

	public String getSexType() {
		return SexType;
	}

	public void setSexType(String sexType) {
		SexType = sexType;
	}

	public String getPublishType() {
		return PublishType;
	}

	public void setPublishType(String publishType) {
		PublishType = publishType;
	}


	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public boolean isIsShowLocation() {
		return IsShowLocation;
	}

	public void setIsShowLocation(boolean isShowLocation) {
		IsShowLocation = isShowLocation;
	}

	public String getStartTime() {
		return StartTime;
	}

	public void setStartTime(String startTime) {
		StartTime = startTime;
	}

	public String getEndTime() {
		return EndTime;
	}

	public void setEndTime(String endTime) {
		EndTime = endTime;
	}

	public boolean isOneSelf() {
		return OneSelf;
	}

	public void setOneSelf(boolean oneSelf) {
		OneSelf = oneSelf;
	}

	public String getOriginId() {
		return OriginId;
	}

	public void setOriginId(String originId) {
		OriginId = originId;
	}

	public String getOriginInfo() {
		return OriginInfo;
	}

	public void setOriginInfo(String originInfo) {
		OriginInfo = originInfo;
	}

	public List<CommentInfo> getComments() {
		return Comments;
	}

	public void setComments(List<CommentInfo> comments) {
		Comments = comments;
	}

	public List<Praise> getPraises() {
		return Praises;
	}

	public void setPraises(List<Praise> praises) {
		Praises = praises;
	}

	public List<RelayInfo> getRelayInfos() {
		return RelayInfos;
	}

	public void setRelayInfos(List<RelayInfo> relayInfos) {
		RelayInfos = relayInfos;
	}
}
