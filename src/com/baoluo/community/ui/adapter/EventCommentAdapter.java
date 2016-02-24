package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.baoluo.im.util.InputBoxUtil;

/**
 * 活动的评论listview  适配器
 * @author Ryan_Fu
 *  2015-5-28
 */
public class EventCommentAdapter extends CommonAdapter<HumorInfo>{

	public EventCommentAdapter(Context context, List<HumorInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, final HumorInfo item) {
		String face = item.getBlogUser().getFace();
		if(!StrUtils.isEmpty(face)){
			helper.setNetImageByUrl(R.id.img_event_comment_head, face);
		}else{
			helper.setImageResource(R.id.img_event_comment_head, R.drawable.head_default);
		}
		helper.getView(R.id.img_event_comment_head).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToFriendInfoUtil.getInstance().toFriendInfo(mContext, item.getBlogUser().getId());
			}
		});
		helper.setText(R.id.tv_event_comment_nick, item.getBlogUser().getName());
		helper.setText(R.id.tv_event_comment_time, item.getPostTime());
		TextView tvContent = helper.getView(R.id.tv_event_comment_content);
		tvContent.setText(InputBoxUtil.getInstance().handler(mContext, item.getContent()));
	}
	
}
