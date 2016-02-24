package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 学校信息
 * @author xiangyang.fu
 *
 */
public class SchoolInfo implements Serializable{

	private static final long serialVersionUID = 959817227472184354L;
	
	private String Id;
	private String Name;
	private String Department;
	private String Class;
	private String StudentNum;
	private String SchoolYear;
	
	public SchoolInfo() {
	}
	
}
