package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.LocationInfo;
import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.TagsView;
import com.baoluo.community.util.StrUtils;

/**
 * 活动展示适配器
 * 
 * @author Ryan_Fu 2015-5-27
 */
public class EventAdapter extends CommonAdapter<EventInfo> {
	private static final String TAG = "EventAdapter";

	private Context context;

	public EventAdapter(Context context, List<EventInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.context = context;
	}

	@Override
	public void convert(ViewHolder helper, EventInfo item) {
		if (item.getPictures() != null && item.getPictures().size() > 0) {
			final String firstUrl = item.getPictures().get(0).getUrl();
			if (!StrUtils.isEmpty(firstUrl)) {
				helper.setNetImageByUrl(R.id.img_event_item, firstUrl);
			}
		} else {
			helper.setImageResource(R.id.img_event_item,
					R.drawable.img_event_item_test);
		}
		
		helper.setText(R.id.tv_event_send_time, item.getShowTime());
		helper.setText(R.id.tv_event_item_title, item.getName());
		helper.setText(R.id.tv_event_item_location,getLocationName(item.getLocation()));
		helper.setText(R.id.tv_event_item_start_time, item.getStartDate());
		TagsView tags = helper.getView(R.id.tagsview_event_item_tags);
		tags.setEventListTags(null);
		List<TagInfo> tagList = item.getTags();
		if (tagList != null && tagList.size() > 0) {
				tags.setEventListTags(tagList);
		}
	}

	private String getLocationName(LocationInfo location) {
		if (location == null) {
			return "未知位置";
		}
		if (StrUtils.isEmpty(location.getName())) {
			return "未知位置";
		} else {
			return location.getName();
		}
	}

}
