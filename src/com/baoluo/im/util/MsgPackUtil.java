package com.baoluo.im.util;

import java.io.IOException;

import org.msgpack.MessagePack;

import com.baoluo.community.util.L;
import com.baoluo.im.entity.Msg;

/**
 * messagePack 工具类
 * 
 * @author tao.lai
 * 
 */
public class MsgPackUtil {
	private static final String TAG = "MsgPackUtil";

	private static MessagePack instance;

	private static MessagePack getInstance() {
		if (instance == null) {
			instance = new MessagePack();
		}
		return instance;
	}

	public static byte[] write(Msg msg) {
		try {
			return getInstance().write(msg);
		} catch (IOException e) {
			e.printStackTrace();
			L.e(TAG, "msgpack write error");
			return null;
		}
	}

	public static Msg read(byte[] data) {
		try {
			return getInstance().read(data, Msg.class);
		} catch (IOException e) {
			e.printStackTrace();
			L.e(TAG, "msgpack read error");
			return null;
		}
	}
}
