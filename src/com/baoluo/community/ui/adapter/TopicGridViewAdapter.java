package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.BitmapUtil;

/**
 * 话题 图片适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class TopicGridViewAdapter extends BaseAdapter {

	private List<String> viewList;
	private Context context;
	private LayoutInflater inflater;
	private MyClickListener linstener;

	public TopicGridViewAdapter(Context context, List<String> viewList,
			MyClickListener linstener) {
		super();
		this.viewList = viewList;
		this.context = context;
		this.linstener = linstener;
	}

	@Override
	public int getCount() {
		if (viewList == null) {
			return 0;
		}else if(viewList.size()>=9){
			return 9;
		}
		return viewList.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return viewList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressWarnings("static-access")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		if (position == viewList.size()) {
			View addView = inflater.inflate(R.layout.topic_create_gv_item_add,
					null);
			addView.findViewById(R.id.add).setOnClickListener(linstener);
			addView.findViewById(R.id.add).setTag(position);
			return addView;
		} else {
			View picView = inflater.inflate(R.layout.topic_create_gv_item_pic,
					null);
			ImageView picIBtn = (ImageView) picView.findViewById(R.id.pic);
			picIBtn.setImageBitmap(BitmapUtil.AbbreviationsIcon(viewList.get(position)));
			picIBtn.setOnClickListener(linstener);
			picIBtn.setTag(position);
			picView.findViewById(R.id.delete).setOnClickListener(linstener);
			picView.findViewById(R.id.delete).setTag(position);
			return picView;
		}
	}
}