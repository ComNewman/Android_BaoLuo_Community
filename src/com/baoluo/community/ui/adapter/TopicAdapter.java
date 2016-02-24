package com.baoluo.community.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TopicInfo;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.util.InputBoxUtil;

/**
 * 话题列表适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class TopicAdapter extends CommonAdapter<TopicInfo> {

	private Context context;
	private boolean isPraised;

	public TopicAdapter(Context context, List<TopicInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.context = context;
	}

	@Override
	public void convert(ViewHolder helper, final TopicInfo item) {

		if (item != null) {
			if(item.getPictures()!=null&&item.getPictures().size()>0){
				String url = item.getPictures().get(0).getUrl();
				if(!StrUtils.isEmpty(url)){
					helper.setNetImageByUrl(R.id.iv_topic_list_item_pic, url);
				}
			}else{
				helper.setImageResource(R.id.iv_topic_list_item_pic, R.drawable.bg_no_pic);
			}
			final String praiseNum = context.getResources().getString(R.string.topic_praise_num);
			String commentNum = context.getResources().getString(R.string.topic_comment_num);
			helper.setText(R.id.tv_topic_list_item_time, item.getShowTime());
			final TextView tvPraiseNum = helper
					.getView(R.id.tv_topic_list_item_praisenum);
			tvPraiseNum.setText(String.format(praiseNum, item.getPraiseNum()));
			
			helper.setText(R.id.tv_topic_list_item_commentnum,
					String.format(commentNum, item.getCommmitNum()));
			TextView tvContent = helper.getView(R.id.tv_topic_list_item_content);
			SpannableStringBuilder ssb = InputBoxUtil.getInstance().changeTextColor(mContext,item.getContent());
			tvContent.setText(ssb);
			String title = context.getResources().getString(R.string.topic_title);
			title = String.format(title, item.getTitle());
			helper.setText(R.id.tv_topic_list_item_title, title);
			final ImageButton btnPraise = helper
					.getView(R.id.btn_topic_list_item_praise);
			isPraised = item.isIsPraise();
			if (item.isIsPraise()) {
				btnPraise.setSelected(true);
			}else{
				btnPraise.setSelected(false);
			}
			// 设置点击事件
			btnPraise.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new PostTask(UrlHelper.TOPIC_PRAISE,new UpdateViewHelper.UpdateViewListener(){
						@Override
						public void onComplete(Object obj) {
							if (!isPraised) {
								btnPraise.setSelected(true);
								if (item.isIsPraise()) {
									tvPraiseNum.setText(String.format(praiseNum, item.getPraiseNum()));
								} else {
									tvPraiseNum.setText(String.format(praiseNum, item.getPraiseNum()+1));
								}
							} else {
								btnPraise.setSelected(false);
								if (item.isIsPraise()) {
									tvPraiseNum.setText(String.format(praiseNum, item.getPraiseNum()-1));
								} else {
									tvPraiseNum.setText(String.format(praiseNum, item.getPraiseNum()));
								}
							}
							isPraised = !isPraised;
						}
					},"Id", item.getId());
				}
			});
			// 设置tag
			btnPraise.setTag(helper.getPosition());
		}
	}
}
