package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;
	/**
	 * 话题实体
	 * @author xiangyang.fu
	 *
	 */
public class TopicInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	private int TopicType;
	private String Id;
	private String Title;
	private String RepId;
	private String RepTitle;
	private String Content;
	private String Face;
	private String ShowTime;
	private BlogUser Ower;
	private List<TagInfo> Tags;
	private List<PictureInfo> Pictures;
	private int PraiseNum;
	private int CommmitNum;
	private int CollectNum;
	private int OpposesNum;
	private int FollowerNum;
	private int PublishType;
	private boolean IsCollect;
	private boolean IsPraise;
	private boolean IsOpposes;
	private boolean IsShowLocation;
	private LocationInfo Location;
	
	public TopicInfo() {
		super();
	}
	
	public TopicInfo(int topicType, String id, String title, String repId,
			String repTitle, String content, String face, String showTime,
			BlogUser ower, List<TagInfo> tags, List<PictureInfo> pictures,
			int praiseNum, int commmitNum, int collectNum, int opposesNum,
			int followerNum, int publishType, boolean isCollect,
			boolean isPraise, boolean isOpposes, boolean isShowLocation,
			LocationInfo location) {
		super();
		TopicType = topicType;
		Id = id;
		Title = title;
		RepId = repId;
		RepTitle = repTitle;
		Content = content;
		Face = face;
		ShowTime = showTime;
		Ower = ower;
		Tags = tags;
		Pictures = pictures;
		PraiseNum = praiseNum;
		CommmitNum = commmitNum;
		CollectNum = collectNum;
		OpposesNum = opposesNum;
		FollowerNum = followerNum;
		PublishType = publishType;
		IsCollect = isCollect;
		IsPraise = isPraise;
		IsOpposes = isOpposes;
		IsShowLocation = isShowLocation;
		Location = location;
	}
	
	public boolean isIsShowLocation() {
		return IsShowLocation;
	}

	public void setIsShowLocation(boolean isShowLocation) {
		IsShowLocation = isShowLocation;
	}

	public LocationInfo getLocation() {
		return Location;
	}

	public void setLocation(LocationInfo location) {
		Location = location;
	}

	public int getTopicType() {
		return TopicType;
	}

	public void setTopicType(int topicType) {
		TopicType = topicType;
	}

	public String getRepId() {
		return RepId;
	}
	public void setRepId(String repId) {
		RepId = repId;
	}
	public String getRepTitle() {
		return RepTitle;
	}
	public void setRepTitle(String repTitle) {
		RepTitle = repTitle;
	}






	public int getPublishType() {
		return PublishType;
	}

	public void setPublishType(int publishType) {
		PublishType = publishType;
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


	public boolean isIsOpposes() {
		return IsOpposes;
	}


	public void setIsOpposes(boolean isOpposes) {
		IsOpposes = isOpposes;
	}


	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getTitle() {
		return Title;
	}

	public void setTitle(String title) {
		Title = title;
	}

	public String getContent() {
		return Content;
	}

	public void setContent(String content) {
		Content = content;
	}

	public String getFace() {
		return Face;
	}

	public void setFace(String face) {
		Face = face;
	}

	public String getShowTime() {
		return ShowTime;
	}

	public void setShowTime(String showTime) {
		ShowTime = showTime;
	}

	public BlogUser getOwer() {
		return Ower;
	}

	public void setOwer(BlogUser ower) {
		Ower = ower;
	}

	public List<TagInfo> getTags() {
		return Tags;
	}

	public void setTags(List<TagInfo> tags) {
		Tags = tags;
	}

	public List<PictureInfo> getPictures() {
		return Pictures;
	}

	public void setPictures(List<PictureInfo> pictures) {
		Pictures = pictures;
	}

	public int getPraiseNum() {
		return PraiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		PraiseNum = praiseNum;
	}

	public int getCommmitNum() {
		return CommmitNum;
	}

	public void setCommmitNum(int commmitNum) {
		CommmitNum = commmitNum;
	}

	public int getCollectNum() {
		return CollectNum;
	}

	public void setCollectNum(int collectNum) {
		CollectNum = collectNum;
	}

	public int getOpposesNum() {
		return OpposesNum;
	}

	public void setOpposesNum(int opposesNum) {
		OpposesNum = opposesNum;
	}

	public int getFollowerNum() {
		return FollowerNum;
	}

	public void setFollowerNum(int followerNum) {
		FollowerNum = followerNum;
	}
	
}
