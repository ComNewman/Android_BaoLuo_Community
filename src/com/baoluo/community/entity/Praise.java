package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.Date;

/**
 * 点赞
 * 
 * @author Ryan_Fu 2015-5-18
 */
public class Praise implements Serializable {

	private static final long serialVersionUID = 5538660128914187843L;
	// UserId
	public String _id;
	// 点赞时间
	public Date PraiseTime;

	public Praise() {
		super();
	}

	public Praise(String _id, Date praiseTime) {
		super();
		this._id = _id;
		PraiseTime = praiseTime;
	}

	public String get_id() {
		return _id;
	}

	public void set_id(String _id) {
		this._id = _id;
	}

	public Date getPraiseTime() {
		return PraiseTime;
	}

	public void setPraiseTime(Date praiseTime) {
		PraiseTime = praiseTime;
	}

}
