package com.baoluo.im.entity;

import java.util.LinkedList;
import java.util.List;

import android.content.Context;

import com.baoluo.community.ui.R;
import com.baoluo.im.util.PropertiesUtil;
/**
 * 群头像Bitmap实体
 * @author xiangyang.fu
 *
 */
public class MyBitmapEntity {
	private float x;
	private float y;
	float width;
	float height;
	static int devide = 1;
	int index = -1;
	private Context context;
	
	public float getX() {
		return x;
	}

	public void setX(float x) {
		this.x = x;
	}

	public float getY() {
		return y;
	}

	public void setY(float y) {
		this.y = y;
	}

	public float getWidth() {
		return width;
	}

	public void setWidth(float width) {
		this.width = width;
	}

	public float getHeight() {
		return height;
	}

	public void setHeight(float height) {
		this.height = height;
	}

	public static int getDevide() {
		return devide;
	}

	public static void setDevide(int devide) {
		MyBitmapEntity.devide = devide;
	}

	public int getIndex() {
		return index;
	}

	public void setIndex(int index) {
		this.index = index;
	}

	public MyBitmapEntity(Context context) {
		this.context = context;
	}

	@Override
	public String toString() {
		return "MyBitmap [x=" + x + ", y=" + y + ", width=" + width
				+ ", height=" + height + ", devide=" + devide + ", index="
				+ index + "]";
	}

	public List<MyBitmapEntity> getBitmapEntitys(int count) {
		List<MyBitmapEntity> mList = new LinkedList<MyBitmapEntity>();
		String value = PropertiesUtil.readData(context, String.valueOf(count),
				R.raw.data);
		String[] arr1 = value.split(";");
		int length = arr1.length;
		for (int i = 0; i < length; i++) {
			String content = arr1[i];
			String[] arr2 = content.split(",");
			MyBitmapEntity entity = null;
			for (int j = 0; j < arr2.length; j++) {
				entity = new MyBitmapEntity(context);
				entity.x = Float.valueOf(arr2[0]);
				entity.y = Float.valueOf(arr2[1]);
				entity.width = Float.valueOf(arr2[2]);
				entity.height = Float.valueOf(arr2[3]);
			}
			mList.add(entity);
		}
		return mList;
	}
}