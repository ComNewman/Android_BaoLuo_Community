package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.ContactsInfo;

public class AddUser2GroupSelAdapter extends CommonAdapter<ContactsInfo> {

	public AddUser2GroupSelAdapter(Context context, List<ContactsInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, ContactsInfo item) {
		if (!StrUtils.isEmpty(item.getFace())) {
			helper.setNetImageByUrl(R.id.iv_avatar, item.getFace());
		} else {
			helper.setImageResource(R.id.iv_avatar, R.drawable.default_head);
		}
		// helper.setImageResource(R.id.iv_avatar, R.drawable.bitboy);
	}

	/*
	 * private int getFace(String id){ int i = (Integer.parseInt(id));
	 * switch(i){ case 0: return R.drawable.bitboy; case 1: return
	 * R.drawable.arrow_down; case 2: return R.drawable.arrows_right; case
	 * 3:return R.drawable.bg_chat_out; case 4:return
	 * R.drawable.bg_event_item_test; case 5:return
	 * R.drawable.voicesearch_feedback001; } return R.drawable.bitboy; }
	 */
}
