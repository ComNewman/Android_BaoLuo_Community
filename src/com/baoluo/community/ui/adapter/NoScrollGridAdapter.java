package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.ui.R;

/**
 * 适应listview的 gridview的适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class NoScrollGridAdapter extends CommonAdapter<PictureInfo> {

	private Context context;

	// gridView 里面的点击事件

	public NoScrollGridAdapter(Context context, List<PictureInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.context = context;
	}

	@Override
	public void convert(ViewHolder helper, PictureInfo item) {
		
		helper.setNetImageByUrl(R.id.iv_image, item.getUrl());
	}
}
