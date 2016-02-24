package com.baoluo.community.ui.barrage;

import android.graphics.Color;

/**
 * 弹幕实体类
 * 
 * @author xiangyang.fu
 * 
 */
public class TanmuBean {

	private String[] items;
	private int color;
	private int minTextSize;
	private float range;

	public TanmuBean() {
		// init default value
		color = Color.parseColor("#666666");
		minTextSize = 16;
		range = 0.5f;
	}

	public String[] getItems() {
		return items;
	}

	public void setItems(String[] items) {
		this.items = items;
	}

	public int getColor() {
		return color;
	}

	public void setColor(int color) {
		this.color = color;
	}

	/** * min textSize, in dp. */
	public int getMinTextSize() {
		return minTextSize;
	}

	public void setMinTextSize(int minTextSize) {
		this.minTextSize = minTextSize;
	}

	public float getRange() {
		return range;
	}

	public void setRange(float range) {
		this.range = range;
	}
}
