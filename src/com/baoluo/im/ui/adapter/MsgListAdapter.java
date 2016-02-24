package com.baoluo.im.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.SlideCutView;
import com.baoluo.community.ui.customview.SlideCutView.OnSlideListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.TimeUtil;
import com.baoluo.dao.helper.MessageHelper;
import com.baoluo.im.entity.Message;
import com.baoluo.im.entity.Msg;

/**
 * 消息列表适配器
 */
public class MsgListAdapter extends BaseAdapter {
	private static final String TAG = "MsgListAdapter";

	private Context ctx;
	private OnSlideListener onSlideListener;
	private List<Message> mList;

	private LayoutInflater mInflater;

	public MsgListAdapter(Context ctx, OnSlideListener onSlideListener,
			List<Message> mList) {
		super();
		this.ctx = ctx;
		this.onSlideListener = onSlideListener;
		this.mList = mList;
		this.mInflater = ((Activity) this.ctx).getLayoutInflater();
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
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		SlideCutView slideView = (SlideCutView) convertView;
		if (slideView == null) {
			View itemView = mInflater.inflate(R.layout.im_msg_item, null);
			slideView = new SlideCutView(ctx);
			slideView.setContentView(itemView);
			slideView.setButtonText("删除");

			holder = new ViewHolder(slideView);
			slideView.setOnSlideListener(onSlideListener);
			slideView.setTag(holder);
		} else {
			holder = (ViewHolder) slideView.getTag();
		}
		Message item = mList.get(position);
		item.setSlideCutView(slideView);
		item.getSlideCutView().reset();
		if(item.getSetTopTime() != null){
			holder.rlMsg.setBackgroundColor(0XFFeaeaea);
		}
		holder.tvAccount.setText(item.getName());
		holder.tvMsg.setText(item.getMessage());
		
		holder.tvTime.setText(TimeUtil.getShowTime(item.getItime()));
		if(item.getUnReadNum() > 0){
			holder.tvUnReadNum.setVisibility(View.VISIBLE);
			holder.tvUnReadNum.setText(StrUtils.getUnReadNum(item.getUnReadNum()));
		}else{
			holder.tvUnReadNum.setVisibility(View.GONE);
		}
		if (!StrUtils.isEmpty(item.getAvatar())) {
			if(item.getMsgType() == Msg.MSG_GROUP){
				holder.ivAvatar.setImageBitmap(BitmapFactory.decodeFile(item.getAvatar()));
			}else{
				GlobalContext.getInstance().imageLoader.displayImage(
						item.getAvatar(), holder.ivAvatar);
			}
		}else{
			holder.ivAvatar.setImageResource(R.drawable.default_head);
		}
		holder.rightHolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mList.get(position).getSlideCutView().shrink();
				MessageHelper.getInstance().removeByFriId(
						mList.get(position).getUid());
			}
		});
		return slideView;
	}

	private static class ViewHolder {
		public RelativeLayout rlMsg;
		public ImageView ivAvatar;
		public TextView tvAccount;
		public TextView tvMsg;
		public TextView tvUnReadNum;
		public TextView tvTime;
		public ViewGroup rightHolder;

		public ViewHolder(View view) {
			rlMsg = (RelativeLayout) view.findViewById(R.id.rl_msg);
			ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
			tvAccount = (TextView) view.findViewById(R.id.tv_account);
			tvMsg = (TextView) view.findViewById(R.id.tv_msg);
			tvUnReadNum = (TextView) view.findViewById(R.id.tv_un_read);
			tvTime = (TextView) view.findViewById(R.id.tv_time);
			rightHolder = (ViewGroup) view.findViewById(R.id.rl_right_holder);
		}
	}
}
