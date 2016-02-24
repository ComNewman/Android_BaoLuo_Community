package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * 活动实体类
 * 
 * @author Ryan_Fu 2015-5-26
 */
public class EventInfo implements Serializable {
	private static final long serialVersionUID = 8904610169634175767L;
	// 活动ID
	private String Id;
	// / 活动名称
	private String Name;
	// / 活动简介
	private String Content;
	// / 活动类型
	private String EventType;
	// / 截止报名时间
	private Date AbortTime;
	private Date StartTime;
	private Date EndTime;

	// / 活动开始时间
	private String StartDate;
	// / 活动结束时间
	private String EntDate;
	// / 活动上限人数(为0不限人数)
	private int UpNum;
	// / 活动下限人数(为0不限人数)
	private int LowerNum;

	private int CommmitNum;

	private String DistanceDate;

	private LocationInfo Location;

	// 活动粉丝数量
	private int FollowerNum;
	// 联系方式
	private String Contact;
	// 作者
	private BlogUser Owner;
	// 活动头像
	private String Face;
	// 活动图片
	private List<PictureInfo> Pictures;
	// 所属校区
	private SchoolInfo School;
	// 活动的粉丝,报名人的集合
	private List<BlogUser> Users;
	// 活动标签
	private List<TagInfo> Tags;
	// 活动帖子列表
	private List<HumorInfo> MicroBlogPosts;
	// 活动创建时间
	private String ShowTime;
	private Date CreateTime;
	private boolean IsAttend;
	private boolean IsFollow;
	private boolean IsOpen;
	private boolean IsEnd;
	private int AttendNum;
	
	
	public boolean isIsEnd() {
		return IsEnd;
	}

	public void setIsEnd(boolean isEnd) {
		IsEnd = isEnd;
	}

	public String getShowTime() {
		return ShowTime;
	}

	public void setShowTime(String showTime) {
		ShowTime = showTime;
	}
	public boolean isIsOpen() {
		return IsOpen;
	}

	public void setIsOpen(boolean isOpen) {
		IsOpen = isOpen;
	}

	public Date getStartTime() {
		return StartTime;
	}

	public void setStartTime(Date startTime) {
		StartTime = startTime;
	}

	public Date getEndTime() {
		return EndTime;
	}

	public void setEndTime(Date endTime) {
		EndTime = endTime;
	}

	public String getDistanceDate() {
		return DistanceDate;
	}

	public void setDistanceDate(String distanceDate) {
		DistanceDate = distanceDate;
	}

	public int getCommmitNum() {
		return CommmitNum;
	}

	public void setCommmitNum(int commmitNum) {
		CommmitNum = commmitNum;
	}

	public boolean isIsAttend() {
		return IsAttend;
	}

	public void setIsAttend(boolean isAttend) {
		IsAttend = isAttend;
	}

	public boolean isIsFollow() {
		return IsFollow;
	}

	public void setIsFollow(boolean isFollow) {
		IsFollow = isFollow;
	}

	public int getAttendNum() {
		return AttendNum;
	}

	public void setAttendNum(int attendNum) {
		AttendNum = attendNum;
	}

	public LocationInfo getLocation() {
		return Location;
	}

	public void setLocation(LocationInfo location) {
		Location = location;
	}

	public Date getCreateTime() {
		return CreateTime;
	}

	public void setCreateTime(Date createTime) {
		CreateTime = createTime;
	}

	public List<PictureInfo> getPictures() {
		return Pictures;
	}

	public void setPictures(List<PictureInfo> pictures) {
		Pictures = pictures;
	}

	public String getId() {
		return Id;
	}

	public void setId(String Id) {
		this.Id = Id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getEventType() {
		return EventType;
	}

	public void setEventType(String eventType) {
		EventType = eventType;
	}

	public Date getAbortTime() {
		return AbortTime;
	}

	public void setAbortTime(Date abortTime) {
		AbortTime = abortTime;
	}

	public String getStartDate() {
		return StartDate;
	}

	public void setStartDate(String startDate) {
		StartDate = startDate;
	}

	public String getEntDate() {
		return EntDate;
	}

	public void setEntDate(String entDate) {
		EntDate = entDate;
	}

	public int getUpNum() {
		return UpNum;
	}

	public void setUpNum(int upNum) {
		UpNum = upNum;
	}

	public int getLowerNum() {
		return LowerNum;
	}

	public void setLowerNum(int lowerNum) {
		LowerNum = lowerNum;
	}

	public String getContact() {
		return Contact;
	}

	public void setContact(String contact) {
		Contact = contact;
	}

	public BlogUser getOwner() {
		return Owner;
	}

	public void setOwner(BlogUser owner) {
		Owner = owner;
	}

	public String getFace() {
		return Face;
	}

	public void setFace(String face) {
		Face = face;
	}

	public SchoolInfo getSchool() {
		return School;
	}

	public void setSchool(SchoolInfo school) {
		School = school;
	}

	public List<BlogUser> getUsers() {
		return Users;
	}

	public void setUsers(List<BlogUser> users) {
		Users = users;
	}

	public int getFollowerNum() {
		return FollowerNum;
	}

	public void setFollowerNum(int followerNum) {
		FollowerNum = followerNum;
	}

	public List<TagInfo> getTags() {
		return Tags;
	}

	public void setTags(List<TagInfo> tags) {
		Tags = tags;
	}

	public List<HumorInfo> getMicroBlogPosts() {
		return MicroBlogPosts;
	}

	public void setMicroBlogPosts(List<HumorInfo> microBlogPosts) {
		MicroBlogPosts = microBlogPosts;
	}

	public EventInfo() {
		super();
	}

	public EventInfo(String id, String name, String content, String eventType,
			Date abortTime, Date startTime, Date endTime, String startDate,
			String entDate, int upNum, int lowerNum, int commmitNum,
			String distanceDate, LocationInfo location, int followerNum,
			String contact, BlogUser owner, String face,
			List<PictureInfo> pictures, SchoolInfo school,
			List<BlogUser> users, List<TagInfo> tags,
			List<HumorInfo> microBlogPosts, String showTime, Date createTime,
			boolean isAttend, boolean isFollow, boolean isOpen, boolean isEnd,
			int attendNum) {
		super();
		Id = id;
		Name = name;
		Content = content;
		EventType = eventType;
		AbortTime = abortTime;
		StartTime = startTime;
		EndTime = endTime;
		StartDate = startDate;
		EntDate = entDate;
		UpNum = upNum;
		LowerNum = lowerNum;
		CommmitNum = commmitNum;
		DistanceDate = distanceDate;
		Location = location;
		FollowerNum = followerNum;
		Contact = contact;
		Owner = owner;
		Face = face;
		Pictures = pictures;
		School = school;
		Users = users;
		Tags = tags;
		MicroBlogPosts = microBlogPosts;
		ShowTime = showTime;
		CreateTime = createTime;
		IsAttend = isAttend;
		IsFollow = isFollow;
		IsOpen = isOpen;
		IsEnd = isEnd;
		AttendNum = attendNum;
	}
}
