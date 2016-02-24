package com.baoluo.im.ui.adapter;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.text.Html;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.Message;
import com.baoluo.im.entity.Msg;

public class SearchAdapter extends BaseAdapter {

	private static final String TAG = "SearchAdapter";

	private List<Message> mDatas;
	private LayoutInflater mInflater;
	private String matchStr;

	public SearchAdapter(Context ctx, List<Message> mDatas) {
		this.mDatas = mDatas;
		this.mInflater = ((Activity) ctx).getLayoutInflater();
	}
	
	public void setMatchStr(String matchStr){
		this.matchStr = matchStr;
	}

	@Override
	public int getCount() {
		return mDatas.size();
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
	public View getView(int position, View convertView, ViewGroup parent) {
		Message item = mDatas.get(position);
		if (item.getSearchResultType() > 0) {
			convertView = mInflater
					.inflate(R.layout.item_im_search_title, null);
			TextView tvTitle = (TextView) convertView
					.findViewById(R.id.tv_title);
			switch (item.getSearchResultType()) {
			case Message.RESULT_PERSON:
				tvTitle.setText("联系人");
				break;
			case Message.RESULT_GROUP:
				tvTitle.setText("群");
				break;
			case Message.RESULT_CONTENT:
				tvTitle.setText("历史记录");
				break;
			default:
				break;
			}
			return convertView;
		}
		convertView = mInflater
				.inflate(R.layout.im_msg_item, null);
		ImageView ivAvatar = (ImageView) convertView.findViewById(R.id.iv_avatar);
		TextView tvName = (TextView) convertView.findViewById(R.id.tv_account);
		TextView tvContent = (TextView) convertView.findViewById(R.id.tv_msg);
		TextView tvTime = (TextView) convertView.findViewById(R.id.tv_time);
		if (!StrUtils.isEmpty(item.getAvatar())) {
			if(item.getMsgType() == Msg.MSG_GROUP){
				ivAvatar.setImageBitmap(BitmapFactory.decodeFile(item.getAvatar()));
			}else{
				GlobalContext.getInstance().imageLoader.displayImage(
						item.getAvatar(), ivAvatar);
			}
		}else{
			ivAvatar.setImageResource(R.drawable.default_head);
		}
		tvName.setText(colour(item.getName(),matchStr));
		tvContent.setText(colour(item.getMessage(),matchStr));
		//tvTime.setText(TimeUtil.getShowTime(item.getItime()));
		return convertView;
	}
	
	private Spanned colour(String str,String matchStr){
		if(str == null){
			return Html.fromHtml("");
		}
		String dest = str;
		if (str != null) {
			Pattern p = Pattern.compile(matchStr);
			Matcher m = p.matcher(str);
			dest = m.replaceAll("<font color='red'>" + matchStr + "</font>");
		}
		return Html.fromHtml(dest);
	}
}
