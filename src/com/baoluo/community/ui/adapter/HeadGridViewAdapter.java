package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.BlogUser;
import com.baoluo.community.ui.R;

public class HeadGridViewAdapter extends CommonAdapter<BlogUser> {
	
	public HeadGridViewAdapter(Context context, List<BlogUser> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(com.baoluo.community.ui.adapter.ViewHolder helper,
			BlogUser item) {
		
		if(item.getFace()!=null&&item.getFace().length()>0){
			helper.setNetImageByUrl(R.id.img_event_gv_heads, item.getFace());
		}else{
			helper.setImageResource(R.id.img_event_gv_heads, R.drawable.head_test);
		}
	}

}
