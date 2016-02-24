package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.ContactSearch;
/**
 * 搜索联系人结果 返回适配器
 * @author xiangyang.fu
 *
 */
public class ContactSearchResAdapter extends BaseAdapter {
	private static final String TAG = "ContactsAdapter";

	private List<ContactSearch> mList;
	private Context context;
	private LayoutInflater inflater;

	public ContactSearchResAdapter(List<ContactSearch> mList, Context context) {
		super();
		this.mList = mList;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		return mList == null ? 0 : mList.size();
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
		ViewHolder holder = null;
		final ContactSearch item = mList.get(position);
		if(convertView ==null){
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.im_friend_list_item, null);
			holder.avatar = (ImageView) convertView.findViewById(R.id.img_contacts_head);
			holder.nickName = (TextView) convertView.findViewById(R.id.tv_title);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		if (!StrUtils.isEmpty(item.getFace())) {
			GlobalContext.getInstance().imageLoader.displayImage(
					item.getFace(), holder.avatar, GlobalContext.options, null);
		} else {
			holder.avatar.setImageResource(R.drawable.default_head);
		}
		holder.nickName.setText(item.getDisplayName());
		return convertView;
	}
	class ViewHolder{
		ImageView avatar;
		TextView nickName;
	}

}
