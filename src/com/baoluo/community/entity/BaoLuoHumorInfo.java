package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 首页 今日心情
 * @author xiangyang.fu
 *
 */
public class BaoLuoHumorInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Id;
	private String Pictures;
	private int PraiseNum;
	private int CommitNum;
	private boolean IsPraise;
	
	public BaoLuoHumorInfo() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public BaoLuoHumorInfo(String id, String pictures, int praiseNum,
			int commitNum, boolean isPraise) {
		super();
		Id = id;
		Pictures = pictures;
		PraiseNum = praiseNum;
		CommitNum = commitNum;
		IsPraise = isPraise;
	}
	
	

	public int getPraiseNum() {
		return PraiseNum;
	}

	public void setPraiseNum(int praiseNum) {
		PraiseNum = praiseNum;
	}

	public boolean isIsPraise() {
		return IsPraise;
	}

	public void setIsPraise(boolean isPraise) {
		IsPraise = isPraise;
	}

	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getPictures() {
		return Pictures;
	}
	public void setPictures(String pictures) {
		Pictures = pictures;
	}
	public int getCommitNum() {
		return CommitNum;
	}
	public void setCommitNum(int commitNum) {
		CommitNum = commitNum;
	}
	
	
}
