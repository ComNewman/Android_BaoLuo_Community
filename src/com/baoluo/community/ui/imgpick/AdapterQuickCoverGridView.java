package com.baoluo.community.ui.imgpick;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

public class AdapterQuickCoverGridView extends BaseAdapter{
	private Context context;
	private List<Bitmap> data;
	private int height;
	public AdapterQuickCoverGridView(Context context, List<Bitmap> data,int height) {
		this.context = context;
		this.data = data;
		this.height=height;
	}
	
	@Override
	public int getCount() {
		return data.size();
	}
	@Override
	public Object getItem(int position) {
		return data.get(position);
	}
	@Override
	public long getItemId(int position) {
		return position;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ImageView iv = new ImageView(context);
		iv.setLayoutParams(new GridView.LayoutParams(GridView.LayoutParams.MATCH_PARENT,height));
//		Bitmap bitmap = Utils.AbbreviationsIcon(data.get(position));
		iv.setImageBitmap(data.get(position));
		iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
		return iv;
	}

}
