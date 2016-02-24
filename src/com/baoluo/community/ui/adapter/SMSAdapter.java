package com.baoluo.community.ui.adapter;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;

import com.baoluo.community.ui.R;

/**
 * 表情（图片）适配器
 * 
 * @author hu
 * 
 */
public class SMSAdapter extends BaseAdapter {
	private static final String TAG = "SMSAdapter";
	Context context;
	public static ArrayList<Bitmap> lists; // 静态的列表，在ChattingAdapter中方便直接找相应序号的Bitmap

	public SMSAdapter(Context context) {
		super();
		this.context = context;
		Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),
				R.drawable.smileymap);
		lists = new ArrayList<Bitmap>();
		int width = bitmap.getWidth();
		int height = bitmap.getHeight();
		Log.i(TAG, "bitmap width" + width);
		Log.i(TAG, "bitmap height" + height);
		int cellWidth = width / 16;
		int cellHeight = height / 7;

		L: for (int y = 0, i = 0; y < height; y += cellHeight, i++) {
			for (int x = 0, j = 0; x < width; x += cellWidth, j++) {
				if (i == 9 && j == 7) {
					break L;
				}
				Bitmap b = Bitmap.createBitmap(bitmap, x, y, cellWidth,
						cellHeight);
				lists.add(b);
			}
		}

		Log.i(TAG, "lists.size=" + lists.size());
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int position) {
		return lists.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView img = new ImageView(context);
		Log.i(TAG, "position=" + position);
		img.setScaleType(ScaleType.FIT_CENTER);
		img.setImageBitmap(lists.get(position));
		return img;
	}
}
