package com.baoluo.community.application;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.graphics.Bitmap.Config;

import com.baoluo.community.core.Configs;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.dao.DaoMaster;
import com.baoluo.dao.DaoMaster.OpenHelper;
import com.baoluo.dao.DaoSession;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * 自定义Application
 * 
 * @author Ryan_Fu 2015-5-14
 */
public class GlobalContext extends Application {

	private static GlobalContext globalContext;

	public String getUid() {
		return SettingUtility.getUid();
	}

	public String getToken() {
		return SettingUtility.getToken();
	}

	public ImageLoader imageLoader = ImageLoader.getInstance();
	public static DisplayImageOptions options;
	public static DisplayImageOptions defaultOptions;

	@Override
	public void onCreate() {
		super.onCreate();
		globalContext = this;

		initImageLoader(getApplicationContext());

		options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.bl_logo) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.bg_no_pic) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();

		defaultOptions = new DisplayImageOptions.Builder() //
				.showImageForEmptyUri(R.drawable.ic_launcher) //
				.showImageOnFail(R.drawable.ic_launcher) //
				.cacheInMemory(true) //
				.cacheOnDisk(true) //
				.build();//
	}

	public static GlobalContext getInstance() {
		return globalContext;
	}

	/**
	 * 初始化ImageLoader
	 * 
	 * @param context
	 */
	public static void initImageLoader(Context context) {
		@SuppressWarnings("deprecation")
		ImageLoaderConfiguration config = new ImageLoaderConfiguration//
		.Builder(context)//
				.defaultDisplayImageOptions(defaultOptions)//
				.discCacheSize(50 * 1024 * 1024)//
				.discCacheFileCount(100)// 缓存一百张图片
				.writeDebugLogs()//
				.build();//
		ImageLoader.getInstance().init(config);
	}

	public boolean isAtyFocus(String className) {
		if (StrUtils.isEmpty(className)) {
			return false;
		}
		ActivityManager am = (ActivityManager) this
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningTaskInfo> list = am.getRunningTasks(1);
		if (list != null && list.size() > 0) {
			ComponentName cpn = list.get(0).topActivity;
			if (className.equals(cpn.getClassName())) {
				return true;
			}
		}
		return false;
	}

	private static DaoMaster daoMaster;
	private static DaoSession daoSession;

	public static DaoMaster getDaoMaster(Context context) {
		if (daoMaster == null) {
			OpenHelper helper = new DaoMaster.DevOpenHelper(context,
					Configs.DB_NAME, null);
			daoMaster = new DaoMaster(helper.getWritableDatabase());
		}
		return daoMaster;
	}

	public static DaoSession getDaoSession(Context context) {
		if (daoSession == null) {
			if (daoMaster == null) {
				daoMaster = getDaoMaster(context);
			}
			daoSession = daoMaster.newSession();
		}
		return daoSession;
	}
}
