package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.ui.R;

/**
 * 话题搜索标签适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class TopicSearchTagAdapter extends CommonAdapter<TagInfo> {

	public TopicSearchTagAdapter(Context context, List<TagInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, TagInfo item) {
		helper.setText(R.id.tv_tagname, item.getName());
	}

}
