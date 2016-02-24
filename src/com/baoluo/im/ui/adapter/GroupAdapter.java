package com.baoluo.im.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.GroupInfo;

/**
 * 群列表适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class GroupAdapter extends CommonAdapter<GroupInfo> {

	public GroupAdapter(Context context, List<GroupInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.mList = mDatas;
	}

	private List<GroupInfo> mList;

	@Override
	public void convert(ViewHolder helper, GroupInfo item) {
		TextView tvTitle = helper.getView(R.id.tv_title);
		TextView tvLetter = helper.getView(R.id.tv_catalog);
		int section = item.getSortLetters().charAt(0);
		if (mList != null && mList.size() > 0 && item != null) {
			int position = mList.indexOf(item);
			if (position == getPositionForSection(section)) {
				tvLetter.setVisibility(View.VISIBLE);
			} else {
				tvLetter.setVisibility(View.GONE);
			}
		}
		tvLetter.setText(item.getSortLetters());
		tvTitle.setText(item.getName());
		if(StrUtils.isEmpty(item.getGroupAvatar())){
			helper.setImageResource(R.id.img_contacts_head, R.drawable.default_head);
		}else{
			helper.setLocalImageByUrl(R.id.img_contacts_head, item.getGroupAvatar());
		}
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	@SuppressLint("DefaultLocale")
	public int getPositionForSection(int section) {
		if (getCount()>0) {
			for (int i = 0; i < getCount(); i++) {
				String sortStr = mList.get(i).getSortLetters();
				char firstChar = sortStr.toUpperCase().charAt(0);
				if (firstChar == section) {
					return i;
				}
			}
		}
		return -1;
	}

	private String getAlpha(String str) {
		String sortStr = str.trim().substring(0, 1).toUpperCase();
		// 正则表达式，判断首字母是否是英文字母
		if (sortStr.matches("[A-Z]")) {
			return sortStr;
		} else {
			return "#";
		}
	}
}