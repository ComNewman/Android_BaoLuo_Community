package com.baoluo.community.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.entity.TopicInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.util.InputBoxUtil;
/**
 * 话题主题列表适配器
 * @author xiangyang.fu
 *
 */
public class TopicThemeAdapter extends CommonAdapter<TopicInfo>{

	public TopicThemeAdapter(Context context, List<TopicInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@SuppressLint("NewApi")
	@Override
	public void convert(ViewHolder helper, TopicInfo item) {
		if(item!=null){
			final String numFormat = mContext.getResources().getString(R.string.topic_praise_comment);
			TextView tvSex = helper.getView(R.id.tv_topic_theme_sex);
			TextView tvLv =  helper.getView(R.id.tv_topic_theme_lv);
			ImageView ivPic = helper.getView(R.id.iv_topic_theme_pic);
			final Button btnPraise = helper.getView(R.id.btn_topic_theme_praise);
			final Button btnComment = helper.getView(R.id.btn_topic_theme_comment);
			
			helper.setText(R.id.tv_topic_theme_time, item.getShowTime());
			if(item.getLocation()!=null&&!StrUtils.isEmpty(item.getLocation().getAddress())){
				helper.setText(R.id.tv_topic_theme_location, item.getLocation().getAddress());
			}else{
				helper.setText(R.id.tv_topic_theme_location, "位置保密");
			}
			TextView tvContent = helper.getView(R.id.tv_topic_theme_content);
			tvContent.setText(InputBoxUtil.getInstance().changeTextColor(item.getContent()));
			helper.setText(R.id.tv_topic_theme_nick, item.getOwer().getName());
			if(item.getOwer().getSex()==0){
				tvSex.setBackground(mContext.getResources().getDrawable(R.drawable.bg_topic_nv));
				tvSex.setTextColor(Color.parseColor("#ff0909"));
				tvSex.setText("女");
			}
			// TODO  Level color
			if(StrUtils.isEmpty(item.getFace())){
				ivPic.setVisibility(View.GONE);
			}else{
				helper.setNetImageByUrl(R.id.iv_topic_theme_pic, item.getFace());
			}
			btnPraise.setText(String.format(numFormat, item.getPraiseNum()));
			btnComment.setText(String.format(numFormat, item.getCommmitNum()));
			if (item.isIsPraise()) {
				btnPraise.setSelected(true);
			}else{
				btnPraise.setSelected(false);
			}
//			btnPraise.setOnClickListener(new OnClickListener() {
//				
//				@Override
//				public void onClick(View v) {
//					v.setSelected(!v.isSelected());
//					if(v.isSelected()){
//						btnPraise.setText(String.format(numFormat, Integer.parseInt(btnPraise.getText().toString().trim())+1));
//					}else{
//						btnPraise.setText(String.format(numFormat, Integer.parseInt(btnPraise.getText().toString().trim())-1));
//					}
//				}
//			});
		}
	}

}
