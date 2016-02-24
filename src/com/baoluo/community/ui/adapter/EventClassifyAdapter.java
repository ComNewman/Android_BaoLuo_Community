package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.ui.R;
/**
 * 活动分类列表
 * @author xiangyang.fu
 *
 */
public class EventClassifyAdapter extends CommonAdapter<String>{

	public EventClassifyAdapter(Context context, List<String> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, String item) {
		helper.setText(R.id.tv_event_classify, item);
	}

}
