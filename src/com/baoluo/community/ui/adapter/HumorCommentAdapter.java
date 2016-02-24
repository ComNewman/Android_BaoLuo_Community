package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baoluo.community.entity.CommentInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.baoluo.im.util.InputBoxUtil;

/**
 * 评论列表适配器
 * 
 * @author Ryan_Fu 2015-5-20
 */
public class HumorCommentAdapter extends CommonAdapter<CommentInfo> {

	private MyClickListener linstener;

	public HumorCommentAdapter(Context context, List<CommentInfo> mDatas,
			int itemLayoutId, MyClickListener linstener) {
		super(context, mDatas, itemLayoutId);
		this.linstener = linstener;
	}

	@Override
	public void convert(ViewHolder helper, final CommentInfo item) {
		String face = item.getFace();
		if (face != null && face.length() > 0) {
			helper.setNetImageByUrl(R.id.img_lv_head, face);
		}else{
			helper.setImageResource(R.id.img_lv_head, R.drawable.head_default);
		}
		helper.getView(R.id.img_lv_head).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToFriendInfoUtil.getInstance().toFriendInfo(mContext, item.getBlogUser().getId());
			}
		});
		TextView tvContent = helper.getView(R.id.tv_lv_content);
		tvContent.setText(InputBoxUtil.getInstance().handler(mContext, item.getContent()));
//		tvContent.setText(item.getContent());
		helper.setText(R.id.tv_lv_time, item.getShowTime());
		helper.setText(R.id.tv_lv_nick, item.getBlogUser().getName());
	}

	public static abstract class MyClickListener implements OnClickListener {
		/**
		 * 基类的onClick方法
		 */
		@Override
		public void onClick(View v) {
			myOnClick((Integer) v.getTag(), v);
		}

		public abstract void myOnClick(int position, View v);
	}
}
