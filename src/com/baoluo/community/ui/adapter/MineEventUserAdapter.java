package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.MineEventUser;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;

public class MineEventUserAdapter extends CommonAdapter<MineEventUser> {

	public MineEventUserAdapter(Context context, List<MineEventUser> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, MineEventUser item) {
		if (item != null) {
			if (!StrUtils.isEmpty(item.getFace())) {
				helper.setNetImageByUrl(R.id.rv_avatar, item.getFace());
			} else {
				helper.setImageResource(R.id.rv_avatar, R.drawable.head_default);
			}
		}

	}
}