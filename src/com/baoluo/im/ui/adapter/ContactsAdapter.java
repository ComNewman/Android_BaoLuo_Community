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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.SlideCutView;
import com.baoluo.community.ui.customview.SlideCutView.OnSlideListener;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.ContactSearch;
import com.baoluo.im.ui.AtyAffirm;

public class ContactsAdapter extends BaseAdapter {
	private static final String TAG = "ContactsAdapter";

	private List<ContactSearch> mList;
	private Context context;
	private LayoutInflater inflater;
	private OnSlideListener onSlideListener;
	private MyClickListener linstener;

	public ContactsAdapter(List<ContactSearch> mList, Context context,
			OnSlideListener onSlideListener, MyClickListener linstener) {
		super();
		this.mList = mList;
		this.context = context;
		this.linstener = linstener;
		this.onSlideListener = onSlideListener;
	}

	@Override
	public int getCount() {
		return mList==null?1:mList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		if (position > 0) {
			return mList.get(position - 1);
		} else {
			return -1;
		}
	}

	@Override
	public long getItemId(int position) {
		return position - 1;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		inflater = LayoutInflater.from(context);
		if (position == 0) {
			View head = inflater.inflate(R.layout.contact_lv_head, null);
			LinearLayout llAttend = (LinearLayout) head
					.findViewById(R.id.ll_contacts_attend);
			LinearLayout llTribe = (LinearLayout) head
					.findViewById(R.id.ll_contacts_tribe);
			llAttend.setOnClickListener(linstener);
			llAttend.setTag(position);
			llTribe.setOnClickListener(linstener);
			llTribe.setTag(position);
			return head;
		} else {
			final ContactSearch item = mList.get(position - 1);
			if(item.isCatalog()){
				View c = inflater.inflate(R.layout.view_item_catalog, null);
				((TextView) c.findViewById(R.id.tv_catalog)).setText(item.getSortLetters());
				return c;
			}
			View itemView = inflater
					.inflate(R.layout.im_friend_list_item, null);
			SlideCutView slideView = new SlideCutView(context);
			slideView.setButtonText("删除");
			slideView.setContentView(itemView);
			ImageView ivAvatar = (ImageView) slideView
					.findViewById(R.id.img_contacts_head);
			TextView tvAccount = (TextView) slideView
					.findViewById(R.id.tv_title);
			ViewGroup rightHolder = (ViewGroup) slideView
					.findViewById(R.id.rl_right_holder);
			slideView.setOnSlideListener(onSlideListener);
			item.setSlideCutView(slideView);
			item.getSlideCutView().reset();

			tvAccount.setText(item.getDisplayName());
			if (!StrUtils.isEmpty(item.getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(
						item.getFace(), ivAvatar, GlobalContext.options, null);
			} else {
				ivAvatar.setImageResource(R.drawable.default_head);
			}
			rightHolder.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					Intent it = new Intent();
					it.putExtra(AtyAffirm.FRI_UID, item.getAccountID());
					it.setClass(context,
							AtyAffirm.class);
					((Activity) context).startActivity(it);
					item.getSlideCutView().shrink();
				}
			});
			return slideView;
		}
	}
}
