package com.baoluo.community.util;

import android.content.Context;
import android.content.Intent;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.AtyFriInfo;
import com.baoluo.im.ui.AtyPerson;

/**
 * 头像跳转帮助
 * 
 * @author xiangyang.fu
 * 
 */
public class ToFriendInfoUtil {
	private static ToFriendInfoUtil instance;

	public static ToFriendInfoUtil getInstance() {
		if (instance == null) {
			instance = new ToFriendInfoUtil();
		}
		return instance;
	}

	/**
	 * 跳转到好友信息页
	 * 
	 * @param position
	 */
	public void toFriendInfo(Context mContext, String userId) {
		Intent i = new Intent();
		if (!StrUtils.isEmpty(userId)
				&& !GlobalContext.getInstance().getUid().equals(userId)) {
			i.setClass(mContext, AtyFriInfo.class);
			i.putExtra("id", userId);
		} else {
			i.setClass(mContext, AtyPerson.class);
		}
		mContext.startActivity(i);
	}
}
