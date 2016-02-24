package com.baoluo.im.util;

import java.io.File;

import android.content.Context;
import android.os.Environment;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.util.StrUtils;

public class FileUtil {

	public static String getBasePath(Context context) {
		String path = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		String packageName = context.getPackageName();
		if (hasSDCard) {
			path = "/sdcard/apps/" + packageName;
		} else {
			path = "/data/data/" + packageName;
		}
		File file = new File(path);
		boolean isExist = file.exists();
		if (!isExist) {
			file.mkdirs();
		}
		return file.getPath();
	}

	/**
	 * 获取图片缓存路径
	 * 
	 * @return
	 */
	public static String getImageTemp() {
		String path = getBasePath(GlobalContext.getInstance());
		path += "/Temp/";
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		return file.getPath() + "/";
	}

	public static boolean delFile(String path) {
		if(StrUtils.isEmpty(path)){
			return false;
		}
		File file = new File(path);
		if (file.exists()) {
			return file.delete();
		}
		return false;
	}

}
