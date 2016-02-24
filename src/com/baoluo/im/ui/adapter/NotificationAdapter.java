package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.ui.R;
import com.baoluo.im.entity.NotificationInfo;
import com.baoluo.im.entity.NotificationModel;

/**
 * 校园通知列表适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class NotificationAdapter extends BaseAdapter {

	private List<NotificationModel> mList;
	private Context context;
	private LayoutInflater inflater;

	public NotificationAdapter(List<NotificationModel> mList, Context context) {
		super();
		this.mList = mList;
		this.context = context;

	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public Object getItem(int position) {
		return mList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		ViewHolderOne one;
		ViewHolderMore more;
		List<NotificationInfo> list = mList.get(position).getItems();
		if (list.size() == 1) {
			one = new ViewHolderOne();
			convertView = inflater.inflate(R.layout.push_notification_item_one,
					null);
			one.imgPic = (ImageView) convertView
					.findViewById(R.id.iv_notification_first_pic);
			one.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_notification_title);
			one.tvContent = (TextView) convertView
					.findViewById(R.id.tv_notification_content);
			convertView.setTag(one);

			initDataOne(position, one);
		} else {
			convertView = inflater.inflate(
					R.layout.push_notification_item_more, null);
			more = new ViewHolderMore();
			more.imgBigPic = (ImageView) convertView
					.findViewById(R.id.iv_notification_first_pic);
			more.imgSecondPic = (ImageView) convertView
					.findViewById(R.id.iv_notification_second);
			more.imgThirdPic = (ImageView) convertView
					.findViewById(R.id.iv_notification_thrid);
			more.imgFourPic = (ImageView) convertView
					.findViewById(R.id.iv_notification_four);

			more.tvBigTitle = (TextView) convertView
					.findViewById(R.id.tv_notification_title);
			more.tvSecondTitle = (TextView) convertView
					.findViewById(R.id.tv_notification_second);
			more.tvThridTitle = (TextView) convertView
					.findViewById(R.id.tv_notification_third);
			more.tvFourTitle = (TextView) convertView
					.findViewById(R.id.tv_notification_four);

			initDataMore(position, more);
		}
		return convertView;
	}

	private void initDataMore(int position, ViewHolderMore more) {
		more.imgBigPic.setImageResource(R.drawable.bg_humor_test);
		more.imgSecondPic
				.setImageResource(R.drawable.img_notification_second_pic_test);
		more.imgThirdPic
				.setImageResource(R.drawable.img_notification_second_pic_test);
		more.imgFourPic
				.setImageResource(R.drawable.img_notification_second_pic_test);
		more.tvBigTitle.setText(mList.get(position).getItems().get(0)
				.getTitle());
		more.tvSecondTitle.setText(mList.get(position).getItems().get(1)
				.getTitle());
		more.tvThridTitle.setText(mList.get(position).getItems().get(2)
				.getTitle());
		more.tvFourTitle.setText(mList.get(position).getItems().get(3)
				.getTitle());
	}

	private void initDataOne(int position, ViewHolderOne one) {
		one.imgPic.setImageResource(R.drawable.bg_humor_test);
		one.tvContent.setText(mList.get(position).getItems().get(0)
				.getContent());
		one.tvTitle.setText(mList.get(position).getItems().get(0).getTitle());
	}

	private final class ViewHolderOne {
		ImageView imgPic;
		TextView tvTitle;
		TextView tvContent;
	}

	private final class ViewHolderMore {

		ImageView imgBigPic;
		TextView tvBigTitle;
		ImageView imgSecondPic;
		TextView tvSecondTitle;
		ImageView imgThirdPic;
		TextView tvThridTitle;
		ImageView imgFourPic;
		TextView tvFourTitle;
	}
}
