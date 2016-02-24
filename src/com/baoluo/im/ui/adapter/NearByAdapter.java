package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.NearBy;

/**
 * 附近的人适配器
 * @author xiangyang.fu
 *
 */
public class NearByAdapter extends CommonAdapter<NearBy>{

	public NearByAdapter(Context context, List<NearBy> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, NearBy item) {
		helper.setText(R.id.tv_nearby_nick, item.getUserNick());
		helper.setText(R.id.tv_nearby_address, item.getDisDesc());
		helper.setText(R.id.tv_nearby_location, item.getDis()+"m");
		helper.setText(R.id.tv_nearby_showtime, item.getShowTime());
		if(!StrUtils.isEmpty(item.getFace())){
			helper.setNetImageByUrl(R.id.iv_nearby_head, item.getFace());
		}else{
			helper.setImageResource(R.id.iv_nearby_head, R.drawable.default_head);
		}
	}

}
