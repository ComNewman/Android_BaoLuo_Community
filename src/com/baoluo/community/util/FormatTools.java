package com.baoluo.community.util;

import java.io.InputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

/**
 * Bitmap与DrawAble与byte[]与InputStream之间的转换工具类
 * 
 * @author tao.lai
 * 
 */
public class FormatTools {

	private static FormatTools formatTools;

	public static FormatTools getInstance() {
		if (formatTools == null) {
			formatTools = new FormatTools();
		}
		return formatTools;
	}

	public Drawable InputStream2Drawable(InputStream is) {
		Bitmap bitmap = this.InputStream2Bitmap(is);
		return this.bitmap2Drawable(bitmap);
	}

	public Bitmap InputStream2Bitmap(InputStream is) {
		return BitmapFactory.decodeStream(is);
	}

	public Drawable bitmap2Drawable(Bitmap bitmap) {
		BitmapDrawable bd = new BitmapDrawable(null, bitmap);
		return (Drawable) bd;
	}
	
	
}
