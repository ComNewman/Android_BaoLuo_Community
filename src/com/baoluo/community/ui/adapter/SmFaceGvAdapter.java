package com.baoluo.community.ui.adapter;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.ui.R;
/**
 * 社交 表情 
 * @author xiangyang.fu
 *
 */
public class SmFaceGvAdapter extends BaseAdapter {
	private static final String TAG = "FaceGVAdapter";
	private List<String> list;
	private Context mContext;

	public SmFaceGvAdapter(List<String> list, Context mContext) {
		super();
		this.list = list;
		this.mContext = mContext;
	}

	public void clear() {
		this.mContext = null;
	}

	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHodler hodler;
		if (convertView == null) {
			hodler = new ViewHodler();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.face_image, null);
			hodler.iv = (ImageView) convertView.findViewById(R.id.face_img);
			hodler.tv = (TextView) convertView.findViewById(R.id.face_text);
			convertView.setTag(hodler);
		} else {
			hodler = (ViewHodler) convertView.getTag();
		}
		try {
			Bitmap mBitmap = BitmapFactory.decodeStream(mContext.getAssets()
					.open("face/bl/" + list.get(position)));
			hodler.iv.setImageBitmap(mBitmap);
		} catch (IOException e) {
			e.printStackTrace();
		}
		hodler.tv.setText("face/bl/" + list.get(position));

		return convertView;
	}

	class ViewHodler {
		ImageView iv;
		TextView tv;
	}
}