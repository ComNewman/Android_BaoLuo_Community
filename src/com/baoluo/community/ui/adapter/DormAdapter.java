package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.DormInfo;
import com.baoluo.community.ui.R;

/**
 * 宿舍列表适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class DormAdapter extends CommonAdapter<DormInfo> {

	public DormAdapter(Context context, List<DormInfo> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);

	}

	@Override
	public void convert(ViewHolder helper, DormInfo item) {
		if (item != null) {
			helper.setText(R.id.tv_dorm_item_name, item.getOwner().getName());
			helper.setText(R.id.tv_dorm_item_time, item.getShowTime());
			helper.setText(R.id.tv_dorm_dsc, item.getDesc());
			helper.setImageResource(R.id.iv_dorm_item_pic,
					R.drawable.qingyu_zhang_dorm_test);
			helper.setImageResource(R.id.iv_dorm_item_avatar,
					R.drawable.head_test);
			helper.setImageResource(R.id.img_dorm_bottom,
					R.drawable.bg_dorm_bottom);
		}

	}
}
