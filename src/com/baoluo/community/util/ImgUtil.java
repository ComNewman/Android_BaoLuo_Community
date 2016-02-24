package com.baoluo.community.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Build;
import android.text.TextUtils;
import android.view.Display;

/**
 * 图片操作工具类
 * 
 * @author tao.lai
 * 
 */
public class ImgUtil {
	private static final String TAG = "ImgUtil";

	public static boolean bitmap2file(Bitmap bmp, String toFilePath) {
		CompressFormat format = Bitmap.CompressFormat.JPEG;
		int quality = 100;
		OutputStream stream = null;
		try {
			stream = new FileOutputStream(toFilePath);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return bmp.compress(format, quality, stream);
	}

	public static boolean isThisBitmapCanRead(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		File file = new File(path);
		if (!file.exists()) {
			return false;
		}
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);
		int width = options.outWidth;
		int height = options.outHeight;
		if (width == -1 || height == -1) {
			return false;
		}
		return true;
	}

	@SuppressLint("NewApi")
	public static int getScreenHeight(Activity context) {
		Display display = context.getWindowManager().getDefaultDisplay();
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
			Point size = new Point();
			display.getSize(size);
			return size.y;
		}
		return display.getHeight();
	}

	@SuppressWarnings("deprecation")
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

	private static Bitmap compressImage(Bitmap image) {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		image.compress(Bitmap.CompressFormat.JPEG, 100, baos);
		int options = 100;
		while (options > 0 && baos.toByteArray().length / 1024 > 200) { // 大于200kb继续压缩
			baos.reset();
			image.compress(Bitmap.CompressFormat.JPEG, options, baos);
			options -= 10;
			L.i(TAG, "options=" + options);
		}
		ByteArrayInputStream isBm = new ByteArrayInputStream(baos.toByteArray());
		image.recycle();
		return BitmapFactory.decodeStream(isBm, null, null);
	}

	/*
	 * public static String compressImage(String filePath,String tempPath){
	 * BitmapFactory.Options bfOptions=new BitmapFactory.Options();
	 * bfOptions.inDither=false; bfOptions.inPurgeable=true;
	 * bfOptions.inTempStorage = new byte[2 * 1024 * 1024]; //2M缓存
	 * 
	 * File file = new File(filePath); L.i("ImgUtil",
	 * "图片大小="+(file.length()/1024/1024)); FileInputStream fis = null; Bitmap
	 * bmp = null; try { fis = new FileInputStream(file); } catch
	 * (FileNotFoundException e) { e.printStackTrace(); } if (fis != null){ try
	 * { bmp = BitmapFactory.decodeFileDescriptor(fis.getFD(), null, bfOptions);
	 * } catch (IOException e) { e.printStackTrace(); } finally { if (fis !=
	 * null) { try { fis.close(); } catch (IOException e) { e.printStackTrace();
	 * } } } } Bitmap bm = ImageCrop(compressImage(bmp)); return
	 * storeImage(bm,new File(tempPath)); }
	 */

	/**
	 * 保存Bitmap 到 手机
	 */
	public static String storeImage(Bitmap image, File file) {
		if (file == null) {
			L.i(TAG, "Error creating media file, check storage permissions: ");
			return "";
		}
		try {
			FileOutputStream fos = new FileOutputStream(file);
			image.compress(Bitmap.CompressFormat.PNG, 90, fos);
			fos.close();
			return file.getPath();
		} catch (FileNotFoundException e) {
			L.i(TAG, "File not found: " + e.getMessage());
		} catch (IOException e) {
			L.i(TAG, "Error accessing file: " + e.getMessage());
		}
		return "";
	}

	public static Bitmap ImageCrop(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		int wh = w > h ? h : w;
		int retX = w > h ? (w - h) / 2 : 0;
		int retY = w > h ? 0 : (h - w) / 2;
		return Bitmap.createBitmap(bitmap, retX, retY, wh, wh, null, false);
	}

	/**
	 * 截取图片上半部分
	 * 
	 * @param bitmap
	 * @return
	 */
	public static Bitmap ImageHalfCrop(Bitmap bitmap) {
		int w = bitmap.getWidth();
		int h = bitmap.getHeight();
		return Bitmap.createBitmap(bitmap, 0, 0, w, h / 2, null, false);
	}

	public static Bitmap ImageCrop(String path) {
		return ImageCrop(BitmapFactory.decodeFile(path));
	}

	private static int getSampleSize(BitmapFactory.Options options,
			int reqWidth, int reqHeight) {
		int inSampleSize = 1;
		int height = options.outHeight;
		int width = options.outWidth;
		if (height > reqHeight || width > reqWidth) {
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			inSampleSize = heightRatio < widthRatio ? widthRatio : heightRatio;
		}
		return inSampleSize;
	}

	public static String compressImage(String path, String tempPath) {
		Bitmap bm = ImageCrop(compressImage(path));
		return storeImage(bm, new File(tempPath));
	}

	public static Bitmap compressImage(String path) {
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		BitmapFactory.decodeFile(path, options);

		options.inSampleSize = getSampleSize(options, 1080, 1920);
		options.inJustDecodeBounds = false;
		Bitmap bitmap = BitmapFactory.decodeFile(path, options);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		bitmap.compress(Bitmap.CompressFormat.JPEG, 60, baos);
		return bitmap;
	}
}
