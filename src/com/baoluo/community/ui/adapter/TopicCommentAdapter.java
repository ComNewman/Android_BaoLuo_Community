package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;

public class TopicCommentAdapter extends CommonAdapter<HumorInfo>{

	public TopicCommentAdapter(Context context, List<HumorInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, HumorInfo item) {
		if(item!=null){
			String face = item.getBlogUser().getFace();
			if(!StrUtils.isEmpty(face)&&face.length()>0){
				helper.setNetImageByUrl(R.id.img_event_comment_head,face);
			}else{
				helper.setImageResource(R.id.img_event_comment_head, R.drawable.head_default);
			}
			helper.setText(R.id.tv_event_comment_nick, item.getBlogUser().getName());
			helper.setText(R.id.tv_event_comment_time, item.getPostTime());
			helper.setText(R.id.tv_event_comment_content, item.getContent());
		}
	}
	
}