package com.baoluo.im;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.baoluo.community.util.L;

public class XmppUtil {

	/**
	 * 根据jid获取用户名
	 */
	public static String getJidToUsername(String jid) {
		return jid.split("@")[0];
	}

	public static String getUserNameToJid(String username) {
		return username + "@" + Configs.SERVER_NAME;
	}

	public static String getJidFromMsg(String from) {
		return from.split("/")[0];
	}

	/**
	 * 群消息 from 获取用户名
	 * 
	 * @param groupFrom
	 * @return
	 */
	public static String getUerName(String groupFrom) {
		return groupFrom.substring(groupFrom.lastIndexOf("/") + 1);
	}

	public static float getScreenDensity(Context context) {
		try {
			DisplayMetrics dm = new DisplayMetrics();
			WindowManager manager = (WindowManager) context
					.getSystemService(Context.WINDOW_SERVICE);
			manager.getDefaultDisplay().getMetrics(dm);
			return dm.density;
		} catch (Exception ex) {

		}
		return 1.0f;
	}

	public static byte[] getFileBytes(String filePath) throws IOException {
		File file = new File(filePath);
		BufferedInputStream bis = null;
		try {
			bis = new BufferedInputStream(new FileInputStream(file));
			int bytes = (int) file.length();
			byte[] buffer = new byte[bytes];
			int readBytes = bis.read(buffer);
			if (readBytes != buffer.length) {
				throw new IOException("Entire file not read");
			}
			return buffer;
		} finally {
			if (bis != null) {
				bis.close();
			}
		}
	}

	@SuppressLint("DefaultLocale")
	public static String getImgType(String imgPath) {
		String suffix = imgPath.substring(imgPath.lastIndexOf("."));
		suffix = suffix.toLowerCase();
		String imgType = "image/";
		if (suffix.equals("png") || suffix.equals("jpeg")
				|| suffix.equals("jpg") || suffix.equals("gif")
				|| suffix.equals("bmp")) {
			imgType += suffix;
		} else {
			L.e("XmppUtil", "图片类型出错！");
			return "";
		}
		return imgType;
	}

}
