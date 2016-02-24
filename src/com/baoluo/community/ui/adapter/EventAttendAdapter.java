package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.entity.BlogUser;
import com.baoluo.community.ui.AtyFriInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.ui.AtyPerson;

public class EventAttendAdapter extends BaseAdapter{
	private static final String TAG = "EventAttendAdapter";
	
	private LayoutInflater mInflater;
	private List<BlogUser> mDatas;
	private Context ctx;
	public EventAttendAdapter(Context ctx,List<BlogUser> mDatas){
		mInflater = LayoutInflater.from(ctx);
		this.mDatas = mDatas;
		this.ctx = ctx;
	}

	@Override
	public int getCount() {
		return Math.round(mDatas.size()/2.0f);
	}

	@Override
	public Object getItem(int position) {
		return mDatas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		View view = mInflater.inflate(R.layout.item_event_attend_users, null);
		ImageView face1 = (ImageView) view.findViewById(R.id.iv_face1);
		ImageView face2 = (ImageView) view.findViewById(R.id.iv_face2);
		BlogUser bu = mDatas.get(position * 2);
		if(!StrUtils.isEmpty(bu.getFace())){
			GlobalContext.getInstance().imageLoader.displayImage(
					mDatas.get(position * 2).getFace(), face1);
		}else{
			face1.setImageResource(R.drawable.default_head);
		}
		face1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				L.i(TAG, mDatas.get(position*2).getName());
				toFriendInfo(position*2);
			}

			
		});
		if((position * 2 + 1) < mDatas.size()){
			BlogUser bu1 = mDatas.get(position * 2 + 1);
			if(!StrUtils.isEmpty(bu1.getFace())){
				GlobalContext.getInstance().imageLoader.displayImage(
						bu1.getFace(), face2);
			}else{
				face2.setImageResource(R.drawable.default_head);
			}
			face2.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					L.i(TAG, mDatas.get(position*2 + 1).getName());
					toFriendInfo(position*2 + 1);
				}
			});
		}
		return view;
	}
	/**
	 * 跳转到好友信息页
	 * @param position
	 */
	private void toFriendInfo(final int position) {
		Intent i = new Intent();
		String userId = mDatas.get(position).getId();
		if (!StrUtils.isEmpty(userId)
				&& !GlobalContext.getInstance().getUid()
						.equals(userId)) {
			i.setClass(ctx, AtyFriInfo.class);
			i.putExtra("id", userId);
		} else {
			i.setClass(ctx, AtyPerson.class);
		}
		ctx.startActivity(i);
	}
}
