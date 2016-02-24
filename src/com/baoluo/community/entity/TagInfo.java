package com.baoluo.community.entity;

import java.io.Serializable;

/**
 * 标签
 * 
 * @author Ryan_Fu 2015-5-25
 */
public class TagInfo implements Serializable {

	private static final long serialVersionUID = 1049705753029530405L;
	// 标签ID
	private String Id;
	// 标签名
	private String Name;
	// 图片中标签的X坐标
	private double X; // 宽度占 图片 百分比 左顶点位置
	// 图片中标签的Y坐标
	private double Y;
	// 0 white 1 green 2 blue 3 purple
	private int Level;

	public int getLevel() {
		return Level;
	}

	public void setLevel(int level) {
		Level = level;
	}

	public TagInfo() {
		super();
	}

	public TagInfo(String name) {
		this.Name = name;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getName() {
		return Name;
	}

	public void setName(String name) {
		Name = name;
	}

	public double getX() {
		return X;
	}

	public void setX(double x) {
		X = x;
	}

	public double getY() {
		return Y;
	}

	public void setY(double y) {
		Y = y;
	}
}
