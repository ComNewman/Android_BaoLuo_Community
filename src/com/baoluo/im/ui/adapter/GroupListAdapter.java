package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;

public class GroupListAdapter extends CommonAdapter<String>{

	public GroupListAdapter(Context context, List<String> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, String item) {
		helper.setText(R.id.tv_group_name, item);
	}

}
