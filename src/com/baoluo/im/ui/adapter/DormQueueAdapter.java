package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.entity.AttendDorm;

/**
 * 已经排队宿舍的队列
 * 
 * @author xiangyang.fu
 * 
 */
public class DormQueueAdapter extends CommonAdapter<AttendDorm> {
	
	private List<AttendDorm> mDatas;
	private Context context;
	public DormQueueAdapter(Context context, List<AttendDorm> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.mDatas = mDatas;
		this.context = context;
	}

	@Override
	public void convert(ViewHolder helper, final AttendDorm item) {
		RelativeLayout rlRoot = helper.getView(R.id.rl_dorm_queue_root);
		int upNum = item.getUpNum();
		switch (upNum) {
		case 2:
			rlRoot.setBackgroundResource(R.drawable.bg_dorm_queue_item_single);
			break;
		case 8:
			rlRoot.setBackgroundResource(R.drawable.bg_dorm_queue_item);
			break;
		default:
			break;
		}
		helper.setText(R.id.tv_dorm_queue_ownew, item.getOwner().getName());
		String stringFormat = "等待人数 %s/%s";
		helper.setText(
				R.id.tv_dorm_queue_waitNum,
				String.format(stringFormat, String.valueOf(item.getCurrentNumber()),
						String.valueOf(item.getUpNum())));
		helper.setText(R.id.tv_dorm_queue_waittime, item.getShowTime());
		String face  = item.getOwner().getFace();
		if(!StrUtils.isEmpty(face)){
			helper.setNetImageByUrl(R.id.rv_dorm_queue_avatar, face);
		}else{
			helper.setImageResource(R.id.rv_dorm_queue_avatar, R.drawable.head_test);
		}
		
		ImageButton ibCancle = helper.getView(R.id.ib_dorm_queue_cancle);
		ibCancle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				new PostTask(UrlHelper.DORM_EXIT, new UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						mDatas.remove(item);
						notifyDataSetChanged();
						T.showShort(context, "移除等待队列完成！");
					}
				}, "Id", item.getId());
			}
		});
	}
}
