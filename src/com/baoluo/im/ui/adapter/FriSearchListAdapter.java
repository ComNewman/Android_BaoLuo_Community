package com.baoluo.im.ui.adapter;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.SlideCutView;
import com.baoluo.community.ui.customview.SlideCutView.OnSlideListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.ContactSearch;
import com.baoluo.im.ui.AtyFriMenu;

public class FriSearchListAdapter extends BaseAdapter {

	private Context ctx;
	private OnSlideListener onSlideListener;
	private List<ContactSearch> mList;

	private LayoutInflater mInflater;

	public FriSearchListAdapter(Context ctx, OnSlideListener onSlideListener,
			List<ContactSearch> mList) {
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
			View itemView = mInflater.inflate(R.layout.item_add_fri, null);
            slideView = new SlideCutView(ctx);
            slideView.setContentView(itemView);
            
            holder = new ViewHolder(slideView);
            slideView.setOnSlideListener(onSlideListener);
            slideView.setTag(holder);
        } else {
            holder = (ViewHolder) slideView.getTag();
        }
        ContactSearch item = mList.get(position);
        item.setSlideCutView(slideView);
        item.getSlideCutView().reset();
        holder.tvAccount.setText(item.getAccountName());
        if(!StrUtils.isEmpty(item.getFace())){
        	GlobalContext.getInstance().imageLoader.displayImage(item.getFace(), holder.ivAvatar);
        }

        holder.rightHolder.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				((Activity) ctx).startActivityForResult(new Intent(ctx,
						AtyFriMenu.class), AtyFriMenu.OPTION_CODE);
				mList.get(position).getSlideCutView().shrink();
			}
		});
		return slideView;
	}

	private static class ViewHolder {
		public ImageView ivAvatar;
		public TextView tvAccount;
		public ViewGroup rightHolder;

		public ViewHolder(View view) {
			ivAvatar = (ImageView) view.findViewById(R.id.iv_avatar);
			tvAccount = (TextView) view.findViewById(R.id.tv_account);
			rightHolder = (ViewGroup) view.findViewById(R.id.rl_right_holder);
		}
	}
}
