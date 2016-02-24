package com.baoluo.community.ui;

import com.baoluo.im.entity.MsgInfo;

/**
 * 接收消息的接口,子类需实现receive(Message info)方法
 * @author Ryan_Fu
 *  2015-5-5
 */
public interface ReceiveInfoListener {
	
	public boolean receive(MsgInfo info);
}
