package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TopicComment;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.TopicDetailsActivity;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.baoluo.im.util.InputBoxUtil;

/**
 * 话题评论适配器（新）
 * 
 * @author xiangyang.fu
 * 
 */
public class TopicDiscussAdapter extends BaseAdapter {

	private List<TopicComment> comments;
	private LayoutInflater inflater;
	private boolean isShowComment;
	private boolean isShowNomalComment;
	private Context mContext;
	private MyClickListener linstener;
	final int VIEW_TYPE = 2;
	final int TYPE_1 = 0;
	final int TYPE_2 = 1;

	public TopicDiscussAdapter(List<TopicComment> comments, Context mContext,MyClickListener linstener) {
		super();
		this.comments = comments;
		this.mContext = mContext;
		this.linstener = linstener;
		inflater = LayoutInflater.from(mContext);
	}

	@Override
	public int getCount() {
		return comments == null ? 0 : comments.size();
	}

	@Override
	public int getViewTypeCount() {
		return VIEW_TYPE;
	}

	@Override
	public int getItemViewType(int position) {
		int type = comments.get(position).getTopicType();
		switch (type) {
		case 1:
			return TYPE_1;
		case 2:
			return TYPE_2;
		default:
			return TYPE_1;
		}
	}

	@Override
	public Object getItem(int position) {

		return comments.get(position);
	}

	@Override
	public long getItemId(int position) {

		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolderNomal holderNomal = null;
		ViewHolderHight holderHight = null;
		final TopicComment item = comments.get(position);
		int type = getItemViewType(position);
		if (convertView == null) {
			switch (type) {
			case TYPE_2:
				convertView = inflater.inflate(
						R.layout.topic_detail_comment_nomal, parent, false);
				holderNomal = new ViewHolderNomal();
				holderNomal.content = (TextView) convertView
						.findViewById(R.id.tv_topic_nomal_comment_content);
				holderNomal.head = (RoundImageView) convertView
						.findViewById(R.id.iv_topic_nomal_comment_head);
				holderNomal.time = (TextView) convertView
						.findViewById(R.id.tv_topic_nomal_comment_time);
				holderNomal.title = (TextView) convertView
						.findViewById(R.id.tv_topic_nomal_comment_nick);
				holderNomal.btnPraise = (ImageButton) convertView
						.findViewById(R.id.btn_topic_nomal_comment_praise);
				holderNomal.btnTrample = (ImageButton) convertView
						.findViewById(R.id.btn_topic_nomal_comment_trample);
				holderNomal.ibShow = (ImageButton) convertView
						.findViewById(R.id.ib_topic_nomal_comment_show);
				holderNomal.rlComment = (RelativeLayout) convertView
						.findViewById(R.id.rl_topic_nomal_comment_praiseandcomment);
				convertView.setTag(holderNomal);
				break;
			case TYPE_1:
				convertView = inflater.inflate(
						R.layout.topic_detail_comment_hight, parent, false);
				holderHight = new ViewHolderHight();
				holderHight.content = (TextView) convertView
						.findViewById(R.id.tv_topic_hight_comment_tag);
				holderHight.head = (RoundImageView) convertView
						.findViewById(R.id.iv_topic_hight_comment_head);
				holderHight.time = (TextView) convertView
						.findViewById(R.id.tv_topic_hight_comment_time);
				holderHight.title = (TextView) convertView
						.findViewById(R.id.tv_topic_hight_comment_nick);
				holderHight.btnComment = (ImageButton) convertView
						.findViewById(R.id.btn_topic_hight_comment_comment);
				holderHight.btnPraise = (ImageButton) convertView
						.findViewById(R.id.btn_topic_hight_comment_praise);
				holderHight.btnTrample = (ImageButton) convertView
						.findViewById(R.id.btn_topic_hight_comment_trample);
				holderHight.rlComment = (RelativeLayout) convertView
						.findViewById(R.id.rl_topic_hight_comment_praiseandcomment);
				holderHight.ibShow = (ImageButton) convertView
						.findViewById(R.id.ib_topic_hight_comment_show);
				holderHight.picBg = (ImageView) convertView
						.findViewById(R.id.iv_topic_hight_comment_pics);
				holderHight.pic = (ImageView) convertView
						.findViewById(R.id.iv_topic_hight_comment_pic);
				convertView.setTag(holderHight);
				break;
			}
		} else {
			switch (type) {
			case TYPE_2:
				holderNomal = (ViewHolderNomal) convertView.getTag();
				break;
			case TYPE_1:
				holderHight = (ViewHolderHight) convertView.getTag();
				break;
			}
		}
		switch (type) {
		case TYPE_2:
			holderNomal.content.setText(InputBoxUtil.getInstance().handler(mContext, item.getContent()));
			holderNomal.time.setText(item.getShowTime());
			holderNomal.title.setText(item.getBlogUser().getName());
			String nomalFace = item.getBlogUser().getFace();
			final String id = item.getId();
			if (!StrUtils.isEmpty(nomalFace)) {
				GlobalContext.getInstance().imageLoader.displayImage(nomalFace,
						holderNomal.head, GlobalContext.options, null);
			} else {
				holderNomal.head.setImageResource(R.drawable.head_default);
			}
			holderNomal.head.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ToFriendInfoUtil.getInstance().toFriendInfo(mContext, item.getBlogUser().getId());
				}
			});
			if (item.isIsOpposes()) {
				holderNomal.btnTrample.setSelected(true);
			} else {
				holderNomal.btnTrample.setSelected(false);
			}
			if (item.isIsPraise()) {
				holderNomal.btnPraise.setSelected(true);
			} else {
				holderNomal.btnPraise.setSelected(false);
			}
			final RelativeLayout showNomalComment = holderNomal.rlComment;
			holderNomal.ibShow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isShowNomalComment = !isShowNomalComment;
					if (isShowNomalComment) {
						showNomalComment.setVisibility(View.VISIBLE);
					} else {
						showNomalComment.setVisibility(View.GONE);
					}
				}
			});
			final ImageButton ibNomalPraise = holderNomal.btnPraise;
			final ImageButton ibNumolTrample = holderNomal.btnTrample;
			holderNomal.btnPraise.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new PostTask(UrlHelper.TOPIC_PRAISE,
							new UpdateViewHelper.UpdateViewListener() {
								@Override
								public void onComplete(Object obj) {
									String rsStr = obj.toString();
									if(rsStr.equals("false")){
										ibNomalPraise.setSelected(false);
									}else if(rsStr.equals("true")){
										ibNomalPraise.setSelected(true);
										if(ibNumolTrample.isSelected()){
											ibNumolTrample.setSelected(false);
										}
									}else{
										T.showShort(mContext, "D 赞失败！");
									}
									showNomalComment.setVisibility(View.GONE);
									isShowNomalComment = false;
								}
							},"Id",id);
				}
			});
			holderNomal.btnPraise.setTag(position);
			holderNomal.btnTrample.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new PostTask(UrlHelper.TOPIC_LOW,
							new UpdateViewHelper.UpdateViewListener() {
								@Override
								public void onComplete(Object obj) {
									String rsStr = obj.toString();
									if(rsStr.equals("false")){
										ibNumolTrample.setSelected(false);
									}else if(rsStr.equals("true")){
										ibNumolTrample.setSelected(true);
										if(ibNomalPraise.isSelected()){
											ibNomalPraise.setSelected(false);
										}
									}else{
										T.showShort(mContext, "D 踩失败！");
									}
									showNomalComment.setVisibility(View.GONE);
									isShowNomalComment = false;
								}
							},"Id",id);
				}
			});
			holderNomal.btnTrample.setTag(position);
			holderNomal.head.setOnClickListener(linstener);
			holderNomal.head.setTag(position);
			break;
		case TYPE_1:
			SpannableStringBuilder ssb = InputBoxUtil.getInstance().changeTextColor(mContext,item
					.getContent());
			holderHight.content.setText(ssb);
			holderHight.time.setText(item.getShowTime());
			holderHight.title.setText(item.getBlogUser().getName());
			String hightFace = item.getBlogUser().getFace();
			final String commentId  = item.getId();
			if (!StrUtils.isEmpty(hightFace)) {
				GlobalContext.getInstance().imageLoader.displayImage(hightFace,
						holderHight.head, GlobalContext.options, null);
			} else {
				holderHight.head.setImageResource(R.drawable.head_default);
			}
			holderHight.head.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					ToFriendInfoUtil.getInstance().toFriendInfo(mContext, item.getBlogUser().getId());
				}
			});
			if (item.getPicture() != null && item.getPicture().size() > 0) {
				holderHight.pic.setVisibility(View.VISIBLE);
				String picPath = item.getPicture().get(0).getUrl();
				if(!StrUtils.isEmpty(picPath)){
					GlobalContext.getInstance().imageLoader.displayImage(picPath,
							holderHight.pic, GlobalContext.options, null);
				}else{
					holderHight.pic.setImageResource(R.drawable.bg_no_pic);
				}
				if(item.getPicture().size()> 1){
					holderHight.picBg.setVisibility(View.VISIBLE);
				}
			}else{
				holderHight.pic.setVisibility(View.GONE);
				holderHight.picBg.setVisibility(View.GONE);
			}
			if (item.isIsOpposes()) {
				holderHight.btnTrample.setSelected(true);
			} else {
				holderHight.btnTrample.setSelected(false);
			}
			if (item.isIsPraise()) {
				holderHight.btnPraise.setSelected(true);
			} else {
				holderHight.btnPraise.setSelected(false);
			}
			final RelativeLayout showComment = holderHight.rlComment;
			holderHight.ibShow.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					isShowComment = !isShowComment;
					if (isShowComment) {
						showComment.setVisibility(View.VISIBLE);
					} else {
						showComment.setVisibility(View.GONE);
					}
				}
			});
			final ImageButton ibHightPraise = holderHight.btnPraise;
			final ImageButton ibHightTrample = holderHight.btnTrample;
			holderHight.btnPraise.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new PostTask(UrlHelper.TOPIC_PRAISE,
							new UpdateViewHelper.UpdateViewListener() {
								@Override
								public void onComplete(Object obj) {
									String rsStr = obj.toString();
									if(rsStr.equals("false")){
										ibHightPraise.setSelected(false);
									}else if(rsStr.equals("true")){
										ibHightPraise.setSelected(true);
										if(ibHightTrample.isSelected()){
											ibHightTrample.setSelected(false);
										}
									}else{
										T.showShort(mContext, "D 赞失败！");
									}
									showComment.setVisibility(View.GONE);
									isShowComment = false;
								}
							},"Id",commentId);
					
				}
			});
			holderHight.btnPraise.setTag(position);
			holderHight.btnTrample.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					new PostTask(UrlHelper.TOPIC_PRAISE,
							new UpdateViewHelper.UpdateViewListener() {
								@Override
								public void onComplete(Object obj) {
									String rsStr = obj.toString();
									if(rsStr.equals("false")){
										ibHightTrample.setSelected(false);
									}else if(rsStr.equals("true")){
										ibHightTrample.setSelected(true);
										if(ibHightPraise.isSelected()){
											ibHightPraise.setSelected(false);
										}
									}else{
										T.showShort(mContext, "D 赞失败！");
									}
									showComment.setVisibility(View.GONE);
									isShowComment = false;
								}
							},"Id",commentId);
					
				}
			});
			holderHight.btnTrample.setTag(position);
			holderHight.head.setOnClickListener(linstener);
			holderHight.head.setTag(position);
			holderHight.btnComment.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent i = new Intent();
					i.setClass(mContext, TopicDetailsActivity.class);
					i.putExtra("id", item.getId());
					mContext.startActivity(i);
				}
			});
			break;
		}
		return convertView;
	}

	@SuppressWarnings("ucd")
	class ViewHolderNomal {
		RoundImageView head;
		TextView title;
		TextView time;
		TextView content;
		RelativeLayout rlComment;
		ImageButton ibShow;
		ImageButton btnPraise;
		ImageButton btnTrample;
	}

	@SuppressWarnings("ucd")
	class ViewHolderHight {
		RoundImageView head;
		TextView title;
		TextView time;
		TextView content;
		ImageView pic;
		ImageView picBg;
		RelativeLayout rlComment;
		ImageButton ibShow;
		ImageButton btnPraise;
		ImageButton btnTrample;
		ImageButton btnComment;
	}

}
