package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.BlogUser;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;

public class HorizontalListViewAdapter extends CommonAdapter<BlogUser> {

	public HorizontalListViewAdapter(Context context, List<BlogUser> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, BlogUser item) {

		if (item != null) {
			if (!StrUtils.isEmpty(item.getFace())) {
				helper.setNetImageByUrl(R.id.rv_avatar, item.getFace());
			} else {
				helper.setImageResource(R.id.rv_avatar, R.drawable.bitboy);
			}
			helper.setText(R.id.tv_name, item.getName());
		}

	}

}
