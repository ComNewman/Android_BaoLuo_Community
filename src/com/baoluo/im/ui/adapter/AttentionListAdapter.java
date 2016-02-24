package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.im.entity.AttentionList;

/**
 *我的关注
 * @author tao.lai
 * 
 */
public class AttentionListAdapter extends CommonAdapter<AttentionList.Attention> {
	private List<AttentionList.Attention> mList;

	public AttentionListAdapter(Context context, List<AttentionList.Attention> mDatas,
								int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		mList = mDatas;
	}

	@Override
	public void convert(ViewHolder helper, AttentionList.Attention item) {
		TextView tvTitle = helper.getView(R.id.tv_title);
		TextView tvLetter = helper.getView(R.id.tv_catalog);
		int section = item.getSortLetters().charAt(0);
		int position = mList.indexOf(item);
		// 如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
		if (position == getPositionForSection(section)) {
			tvLetter.setVisibility(View.VISIBLE);
			tvLetter.setText(item.getSortLetters());
		} else {
			tvLetter.setVisibility(View.GONE);
		}
		tvTitle.setText(item.getNickName());
		// TODO  setNetImageByUrl not do!
		helper.setNetImageByUrl(R.id.img_contacts_head, item.getFace());
		//helper.setImageResource(R.id.img_contacts_head, R.drawable.default_head);
	}

	/**
	 * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
	 */
	public int getPositionForSection(int section) {
		for (int i = 0; i < getCount(); i++) {
			String sortStr = mList.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
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
