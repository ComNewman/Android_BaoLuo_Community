package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.List;

/**
 * api请求返回的好友列表实体
 * 
 * @author Ryan_Fu 2015-6-1
 */
public class HumorInfoList implements Serializable {

	private static final long serialVersionUID = 1L;
	// 请求Api结果 true 成功 false 失败
	private boolean result;
	// 心情列表
	private List<HumorInfo> Items;
	// 列表总页数
	private int page;
	// 总条数
	private int Count;

	public HumorInfoList() {

	}

	public HumorInfoList(boolean result, List<HumorInfo> items, int page,
			int count) {
		super();
		this.result = result;
		Items = items;
		this.page = page;
		Count = count;
	}

	public boolean isResult() {
		return result;
	}

	public void setResult(boolean result) {
		this.result = result;
	}

	public List<HumorInfo> getItems() {
		return Items;
	}

	public void setItems(List<HumorInfo> items) {
		Items = items;
	}

	public int getPage() {
		return page;
	}

	public void setPage(int page) {
		this.page = page;
	}

	public int getCount() {
		return Count;
	}

	public void setCount(int count) {
		Count = count;
	}

}
