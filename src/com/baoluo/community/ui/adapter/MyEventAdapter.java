package com.baoluo.community.ui.adapter;

import java.sql.Date;
import java.util.List;

import android.content.Context;

import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.community.util.TimeUtil;

/**
 * 我发布的活动适配器
 * 
 * @author xiangyang.fu
 * 
 */
public class MyEventAdapter extends CommonAdapter<EventInfo> {

	public MyEventAdapter(Context context, List<EventInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(ViewHolder helper, EventInfo item) {
		L.i("MyEventAdapter eventInfo :", item.getName() + "/n headPic:"
				+ item.getOwner().getFace());
		if (item.getOwner().getFace() != null
				&& !item.getOwner().getFace()
						.equals("/assets/images/huati-user.jpg")) {
			helper.setNetImageByUrl(R.id.img_mytask_item_pic, item.getOwner()
					.getFace());
		}else{
			helper.setImageResource(R.id.img_mytask_item_pic, R.drawable.head_defult);
		}
		helper.setText(R.id.tv_mytask_item_title, item.getName());
		String status = null;
		 // 比较
		 Date endDate = (Date) TimeUtil.stringToDate(item.getEntDate(), TimeUtil.FORMAT_TIME9);
		 
		 L.i("endDate.getTime():::", ""+endDate.getTime());
		 TimeUtil.dateToString(endDate, "yyyy-MM-dd HH:mm:ss");
		 L.i("date  endDate::" + endDate);
		// Date startDate = item.getStartTime();
		// Date currentDate = new Date();
		//
		// if ((currentDate.getTime() - endDate.getTime())>= 0) {
		// status = "完成";
		// System.out.println(status);
		// } else if ((currentDate.getTime() - startDate.getTime()) < 0) {
		// status = "未开始";
		// System.out.println(status);
		// } else {
		// status = "正在进行中";
		// System.out.println(status);
		// }
		helper.setText(R.id.tv_mytask_item_statu, status);
	}

}
