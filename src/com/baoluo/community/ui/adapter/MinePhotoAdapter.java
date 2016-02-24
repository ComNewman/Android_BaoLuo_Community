package com.baoluo.community.ui.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.entity.MinePhoto;
import com.baoluo.community.ui.AtyImgPager;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HorizontalListView;
import com.baoluo.community.util.StrUtils;

public class MinePhotoAdapter extends CommonAdapter<MinePhoto> {
	private Context context;

	public MinePhotoAdapter(Context context, List<MinePhoto> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.context = context;
	}

	@Override
	public void convert(ViewHolder helper, final MinePhoto item) {
		String month = context.getResources().getString(R.string.mine_month);
		String points = context.getResources().getString(R.string.mine_points);
		String peopleNum = context.getResources().getString(
				R.string.mine_people_num);
		String picNum = context.getResources().getString(
				R.string.mine_event_pic);
		helper.setText(R.id.tv_mine_month,
				String.format(month, item.getMonth()));
		TextView tvPoint = helper.getView(R.id.tv_mine_jifen);
		tvPoint.setText(StrUtils.setPointColor(String.format(points,
				item.getIntegral()),"#ff7f7f"));

		helper.setText(R.id.tv_mine_people_num,
				String.format(peopleNum, item.getTotalNumber()));
		HorizontalListView hlvList = helper.getView(R.id.hlv_new_user);
		if (item.getUserTotalLists() != null
				&& item.getUserTotalLists().size() > 0) {
			MineEventUserAdapter adapter = new MineEventUserAdapter(context,
					item.getUserTotalLists(), R.layout.item_mine_user);
			hlvList.setAdapter(adapter);
		}
		if (item.getMicroBlogUrl() != null && item.getMicroBlogUrl().size() > 0
				&& !StrUtils.isEmpty(item.getMicroBlogUrl().get(0).getUrl())) {
			helper.setNetImageByUrl(R.id.iv_mine_pic, item.getMicroBlogUrl()
					.get(0).getUrl());
		} else {
			helper.setImageResource(R.id.iv_mine_pic, R.drawable.bg_no_pic);
		}
		ImageView ivPhoto = helper.getView(R.id.iv_photo);
		if (item.getEventimgList() != null && item.getEventimgList().size() > 0
				&& !StrUtils.isEmpty(item.getEventimgList().get(0).getUrl())) {
			ivPhoto.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					ArrayList<String> urls = new ArrayList<String>();
					for (int i = 0; i < item.getEventimgList().size(); i++) {
						urls.add(item.getEventimgList().get(i).getUrl());
					}
					Intent i = new Intent();
					i.setClass(context, AtyImgPager.class);
					i.putExtra(AtyImgPager.EXTRA_IMAGE_INDEX, 0);
					i.putExtra(AtyImgPager.EXTRA_IMAGE_URLS, urls);
					context.startActivity(i);
				}
			});
			helper.setNetImageByUrl(R.id.iv_photo, item.getEventimgList()
					.get(0).getUrl());
			helper.setText(R.id.tv_photo_flag,
					String.format(picNum, item.getEventimgList().size()));
		} else {
			helper.setImageResource(R.id.iv_photo, R.drawable.bg_no_pic);
			helper.setText(R.id.tv_photo_flag, context.getResources()
					.getString(R.string.mine_event_nopic));
		}
	}

}
