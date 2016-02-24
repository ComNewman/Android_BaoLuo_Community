package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.baoluo.community.entity.TopicTag;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
/**
 * 话题主题列表
 * @author xiangyang.fu
 *
 */
public class TopicTagAdapter extends CommonAdapter<TopicTag>{
	
	public TopicTagAdapter(Context context, List<TopicTag> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, TopicTag item) {
		if (item != null) {
			String  titleFormat = mContext.getResources().getString(R.string.topic_title);
			String numFormat = mContext.getResources().getString(R.string.topic_participator);
			
			helper.setText(R.id.tv_topictag_tag, String.format(titleFormat, item.getTitle()));
			helper.setText(R.id.tv_topictag_content, item.getDesc());
			TextView tvNum = helper.getView(R.id.tv_topictag_participator);
			tvNum.setText(StrUtils.setPointColor(String.format(numFormat, item.getCount()), "#ff0909"));
//			helper.setText(R.id.tv_topictag_participator, String.format(numFormat, item.getCount()));
			if(!StrUtils.isEmpty(item.getFace())){
				helper.setNetImageByUrl(R.id.iv_topictag_pic, item.getFace());
			}
		}
		
	}

}
