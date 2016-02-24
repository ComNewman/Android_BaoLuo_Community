package com.baoluo.community.ui.imgpick;

import java.util.LinkedList;
import java.util.List;

import com.baoluo.community.util.L;

public class SelectImg {

	public static final int DEFAULT_SIZE = 9;
	public static final int HUMOR_SIZE = 1;
	public static final int ACTIVITY_SIZE = 5;

	private int chooseAble = DEFAULT_SIZE;

	private int count;
	private static SelectImg selectImg = null;

	public static SelectImg getIntance() {
		if (selectImg == null) {
			selectImg = new SelectImg();
		}
		return selectImg;
	}

	public static SelectImg getEmptyIntance() {
		getIntance().clear();
		return selectImg;
	}

	private List<String> selectImgList = null; // 已选择的图片地址
	private List<String> upImgList = null; // 上传服务器图片地址

	public List<String> getUpImgList() {
		if (upImgList == null) {
			upImgList = new LinkedList<String>();
		}
		return upImgList;
	}

	public void clear() {
		if (selectImgList != null && selectImgList.size() > 0) {
			selectImgList.clear();
		}
		if (upImgList != null && upImgList.size() > 0) {
			upImgList.clear();
		}
		L.i("------all ---------clear ----");
	}

	public List<String> getSelectImgList() {
		if (selectImgList == null) {
			selectImgList = new LinkedList<String>();
		}
		return selectImgList;
	}

	/**
	 * 上传到服务器图片 是否为空
	 * 
	 * @return
	 */
	public boolean isUpImgEmpty() {
		if (getUpImgList() == null || getUpImgList().size() == 0) {
			return true;
		}
		return false;
	}

	/**
	 * 已选择图片是否为空
	 * 
	 * @return
	 */
	public boolean isSelEmpty() {
		if (getSelectImgList() == null || getSelectImgList().size() == 0) {
			return true;
		}
		return false;
	}

	public void setChooseAble(int _chooseAble) {
		this.chooseAble = _chooseAble;
	}

	public int getChooseAble() {
		return this.chooseAble;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

}
