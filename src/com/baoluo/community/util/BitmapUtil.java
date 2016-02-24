package com.baoluo.community.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.media.ThumbnailUtils;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.view.Display;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.R;
import com.baoluo.im.entity.GroupUser;
import com.baoluo.im.entity.MyBitmapEntity;
import com.baoluo.im.util.UUIDGenerator;

/**
 * Bitmap 帮助类
 * 
 * @author Ryan_Fu 2015-5-16
 */
public class BitmapUtil {

	private static final String TAG = "BitmapUtil";

	/**
	 * 获取uri指定的图片的缩略图
	 * 
	 * @param uri
	 *            指向图片的URI
	 * @param width
	 *            图片想要显示的宽度（像素）
	 * @return bitmap
	 * @throws FileNotFoundException
	 */
	public static Bitmap getThumbNail(String filePath, int width)
			throws FileNotFoundException {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(filePath, options);
		int be = (int) (options.outWidth / (float) width);
		options.inSampleSize = be;
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(filePath, options);
		return bitmap;
	}

	public static Bitmap getThumbNail(Bitmap bm,int width){
		Matrix matrix = new Matrix();
		float fa = bm.getWidth()/width;
		matrix.postScale(fa,fa);
		Bitmap resizeBmp = Bitmap.createBitmap(bm, 0, 0, bm.getWidth(), bm.getHeight(), matrix, true);
		return resizeBmp;
	}

	/**
	 * 网络可用状态下，下载图片并保存在本地
	 * 
	 * @param strUrl
	 *            图片网址
	 * @param file
	 *            本地保存的图片文件
	 * @param context
	 *            上下文
	 * @return 图片
	 */
	public static Bitmap getNetBitmap(String strUrl, File file, Context context) {
		L.i(TAG, "getBitmap from net");
		Bitmap bitmap = null;
		// 判断网络连接是否有效
		if (NetUtil.isConnnected(context)) {
			try {
				URL url = new URL(strUrl);
				HttpURLConnection con = (HttpURLConnection) url
						.openConnection();
				con.setDoInput(true);
				con.connect();
				InputStream in = con.getInputStream();
				bitmap = BitmapFactory.decodeStream(in);
				FileOutputStream out = new FileOutputStream(file.getPath());
				bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
				out.flush();
				out.close();
				in.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			} finally {

			}
		}
		return bitmap;
	}

	/**
	 * 获取图片的存储目录，在有sd卡的情况下为 “/sdcard/apps_images/本应用包名/cach/images/”
	 * 没有sd的情况下为“/data/data/本应用包名/cach/images/”
	 * 
	 * @param context
	 *            上下文
	 * @return 本地图片存储目录
	 */
	public static String getPath(Context context) {
		String path = null;
		boolean hasSDCard = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
		String packageName = context.getPackageName() + "/cach/images/";
		if (hasSDCard) {
			path = "/sdcard/apps_images/" + packageName;
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
	
	public static String getPath() {
		return getPath(GlobalContext.getInstance());
	}

	/**
	 * 根据传入的list中保存的图片网址，获取相应的图片列表
	 * 
	 * @param list
	 *            保存图片网址的列表
	 * @param context
	 *            上下文
	 * @return 图片列表
	 */
	public static List<Bitmap> getBitmap(List<String> list, Context context) {
		List<Bitmap> result = new ArrayList<Bitmap>();
		for (String strUrl : list) {
			Bitmap bitmap = getBitmap(strUrl, context);
			if (bitmap != null) {
				result.add(bitmap);
			}
		}
		return result;
	}

	/**
	 * 根据网址获得图片，优先从本地获取，本地没有则从网络下载
	 * 
	 * @param url
	 *            图片网址
	 * @param context
	 *            上下文
	 * @return 图片
	 */
	public static Bitmap getBitmap(String url, Context context) {
		L.i(TAG, "------url=" + url);
		String imageName = url
				.substring(url.lastIndexOf("/") + 1, url.length());
		File file = new File(getPath(context), imageName);
		if (file.exists()) {
			L.i(TAG, "getBitmap from Local");
			return BitmapFactory.decodeFile(file.getPath());
		}
		return getNetBitmap(url, file, context);
	}

	public static Bitmap AbbreviationsIcon(String str) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高
		Bitmap bitmap = BitmapFactory.decodeFile(str, options); // 此时返回bm为空
		options.inJustDecodeBounds = false;
		// 计算缩放比
		int be = (int) (options.outHeight / (float) 200);
		if (be <= 0)
			be = 1;
		options.inSampleSize = be;
		bitmap = BitmapFactory.decodeFile(str, options);
		return bitmap;
	}

	@SuppressLint("NewApi")
	public static int getScreenWidth(Activity context) {

		Display display = context.getWindowManager().getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			return size.x;
		}
		return display.getWidth();
	}
	
	
	/**
	 * 获取群头像
	 * @param users
	 * @return
	 */
	public static String getGroupAvatar(List<GroupUser> users) {
		if (users == null || users.size() == 0) {
			return null;
		}
		Context ctx = GlobalContext.getInstance();
		MyBitmapEntity mBmpEntity = new MyBitmapEntity(ctx);
		List<MyBitmapEntity> mEntityList;
		int userNum = users.size();
		if (userNum > 9) {
			userNum = 9;
		}
		mEntityList = mBmpEntity.getBitmapEntitys(userNum);
		Bitmap[] mBitmaps = new Bitmap[userNum];
		for (int i = 0; i < userNum; i++) {
			L.i(TAG, "facePath = "+users.get(i).getFace());
			if (!StrUtils.isEmpty(users.get(i).getFace())) {
				mBitmaps[i] = ThumbnailUtils.extractThumbnail(GlobalContext
						.getInstance().imageLoader.loadImageSync(users.get(i)
						.getFace()), (int) mEntityList.get(i).getWidth(),
						(int) mEntityList.get(i).getWidth());
			} else {
				mBitmaps[i] = ThumbnailUtils.extractThumbnail(BitmapUtil
						.getScaleBitmap(ctx.getResources(),
								R.drawable.default_head),
						(int) mEntityList.get(i).getWidth(), (int) mEntityList
								.get(i).getWidth());
			}
		}
		Bitmap b = BitmapUtil.getCombineBitmaps(mEntityList, mBitmaps);
		String filePath = getPath()+"/"+UUIDGenerator.getUUID()+".png";
        if(b == null){
        	b = BitmapFactory.decodeResource(ctx.getResources(), R.drawable.default_head);
        }
		L.i(TAG, "bitmap==null  ?" +(b==null)+",filePath="+filePath);
		boolean rs = ImgUtil.bitmap2file(b, filePath);
		if(rs){
			return filePath;
		}else{
			return "";
		}
	}

	
	
	/**-----------------------群头像生成 专用 开始-----------------------------**/
	public static boolean saveMyBitmap(Context mContext, Bitmap bitmap,
			String desName) throws IOException {
		FileOutputStream fOut = null;

		if (Environment.getExternalStorageState() != Environment.MEDIA_MOUNTED) {
			fOut = mContext.openFileOutput(UUIDGenerator.getUUID() + ".png",
					Context.MODE_PRIVATE);
		} else {
			File f = new File(Environment.getExternalStorageDirectory()
					.getPath() + "/" + UUIDGenerator.getUUID() + ".png");
			f.createNewFile();
			fOut = new FileOutputStream(f);
		}
		boolean rs = bitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
		try {
			fOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			fOut.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rs;
	}
	public static Bitmap getCombineBitmaps(List<MyBitmapEntity> mEntityList,
			Bitmap... bitmaps) {
		int[] colors = new int[200 * 200];
		for(int i = 0; i < 200 * 200; i++){
			colors[i] = 0XFFE3E3E3;//Color.WHITE;
		}
		Bitmap newBitmap = Bitmap.createBitmap(colors, 200, 200,
				Config.ARGB_8888);
		for (int i = 0; i < mEntityList.size(); i++) {
			newBitmap = mixtureBitmap(newBitmap, bitmaps[i], new PointF(
					mEntityList.get(i).getX(), mEntityList.get(i).getY()));
		}
		return newBitmap;
	}
	
	public static Bitmap mixtureBitmap(Bitmap first, Bitmap second,
			PointF fromPoint) {
		if (first == null || second == null || fromPoint == null) {
			return null;
		}
		Bitmap newBitmap = Bitmap.createBitmap(first.getWidth(),
				first.getHeight(), Config.ARGB_8888);
		Canvas cv = new Canvas(newBitmap);
		cv.drawBitmap(first, 0, 0, null);
		cv.drawBitmap(second, fromPoint.x, fromPoint.y, null);
		cv.save(Canvas.ALL_SAVE_FLAG);
		cv.restore();
		return newBitmap;
	}
	
	 public static Bitmap getScaleBitmap(Resources res, int id) {
			Bitmap bitmap = null;
			try {
				BitmapFactory.Options opts = new BitmapFactory.Options();
				opts.inJustDecodeBounds = true;
				BitmapFactory.decodeResource(res, id, opts);

				opts.inSampleSize = computeSampleSize(opts, -1, 800 * 480);
				opts.inJustDecodeBounds = false;
				bitmap = BitmapFactory.decodeResource(res, id, opts);
			} catch (OutOfMemoryError err) {
				Log.d("BitmapUtil", "[getScaleBitmap] out of memory");

			}
			return bitmap;
		}

		public static int computeSampleSize(BitmapFactory.Options options,
				int minSideLength, int maxNumOfPixels) {

			int initialSize = computeInitialSampleSize(options, minSideLength,
					maxNumOfPixels);
			int roundedSize;
			if (initialSize <= 8) {
				roundedSize = 1;
				while (roundedSize < initialSize) {
					roundedSize <<= 1;
				}
			} else {
				roundedSize = (initialSize + 7) / 8 * 8;
			}
			return roundedSize;
		}
		private static int computeInitialSampleSize(BitmapFactory.Options options,
				int minSideLength, int maxNumOfPixels) {

			double w = options.outWidth;
			double h = options.outHeight;
			int lowerBound = (maxNumOfPixels == -1) ? 1 : (int) Math.ceil(Math
					.sqrt(w * h / maxNumOfPixels));
			int upperBound = (minSideLength == -1) ? 128 : (int) Math.min(
					Math.floor(w / minSideLength), Math.floor(h / minSideLength));

			if (upperBound < lowerBound) {
				return lowerBound;
			}

			if ((maxNumOfPixels == -1) && (minSideLength == -1)) {
				return 1;
			} else if (minSideLength == -1) {
				return lowerBound;
			} else {
				return upperBound;
			}

		}
		/**-----------------------群头像生成 专用 结束-----------------------------**/
}
