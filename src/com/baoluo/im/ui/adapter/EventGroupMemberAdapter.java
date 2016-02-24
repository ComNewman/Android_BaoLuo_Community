package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.GroupUser;

public class EventGroupMemberAdapter extends BaseAdapter {
	private List<GroupUser> menbers;
	private LayoutInflater inflater;
	private MyClickListener linstener;
	private String uid;
	
	private ImageView imgDelete;
	private boolean isShowDelete;

	public EventGroupMemberAdapter(List<GroupUser> menbers, Context context,
			MyClickListener linstener, String uid) {
		super();
		this.menbers = menbers;
		this.linstener = linstener;
		this.uid = uid;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		if (GlobalContext.getInstance().getUid().equals(uid)) {
			return menbers.size() + 1;
		}
		return menbers==null?0:menbers.size();
	}

	@Override
	public Object getItem(int position) {

		if (position < menbers.size()) {
			return menbers.get(position);
		}
		return null;
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (position == menbers.size()) {
			View deleteView = inflater.inflate(
					R.layout.groupinfo_gv_item_delete, null);
			imgDelete = (ImageView) deleteView
					.findViewById(R.id.group_delete_menber);
			imgDelete.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					if (isShowDelete) {
						isShowDelete = false;
					} else {
						isShowDelete = true;
					}
					setIsShowDelete(isShowDelete);
				}
			});
			return deleteView;
		} else {
				View headView = inflater.inflate(R.layout.groupinfo_gv_item_pic,
						null);
				TextView tvNick = (TextView) headView.findViewById(R.id.group_menber_nick);
				ImageButton deleteMember = (ImageButton) headView.findViewById(R.id.groupinfo_delete_menber);
				ImageView head = (ImageView) headView.findViewById(R.id.group_menber_head);
			if (!StrUtils.isEmpty(menbers.get(position).getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(menbers
						.get(position).getFace(), head,
						GlobalContext.options, null);
			} else {
				head.setImageResource(R.drawable.default_head);
			}
			if(!StrUtils.isEmpty(menbers.get(position).getDisplayName())){
				tvNick.setText(menbers.get(position).getDisplayName());
			}
			head.setOnClickListener(linstener);
			head.setTag(position);
			deleteMember.setOnClickListener(linstener);
			deleteMember.setTag(position);
			if(position!=0){
				deleteMember.setVisibility(isShowDelete ? View.VISIBLE : View.GONE);
			}
			return headView;
		}
	}

	public void setIsShowDelete(boolean isShowDelete) {
		this.isShowDelete = isShowDelete;
		notifyDataSetChanged();
	}
	class ViewHoder{
		ImageView head;
		ImageButton deleteMember;
		TextView tvNick;
	}
}
