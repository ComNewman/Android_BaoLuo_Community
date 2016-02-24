package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.widget.TextView;

import com.baoluo.community.entity.RelayInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.TimeUtil;

/**
 * 心情转发适配器
 * 
 * @author Ryan_Fu 2015-5-22
 */
public class HumorRelayAdapter extends CommonAdapter<RelayInfo> {

	public HumorRelayAdapter(Context context, List<RelayInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, RelayInfo item) {
		String url = item.getBlogUser().getFace();
		if (url != null) {
			helper.setLocalImageByUrl(R.id.img_lv_head, url);
		}
		helper.setText(R.id.tv_lv_nick, item.getBlogUser().getName());
		helper.setText(
				R.id.tv_lv_time,
				TimeUtil.getRelativeTime(TimeUtil.dateToString(
						item.getPostTime(), "yyMMddHHmmss")));
		String content = item.getContent();
		TextView tvContent = helper.getView(R.id.tv_lv_content);
		tvContent.setText(content);

	}

}
