package com.baoluo.im.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.DormInfo;
import com.baoluo.community.entity.DormUser;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.ui.AtyDormChat;
/**
 * 等待排队人的adapter
 * @author xiangyang.fu
 *
 */
public class DornAttendUserAdapter extends BaseAdapter {
	private static final String TAG = "DornAttendUserAdapter";

	private List<DormUser> dormList;
	private DormInfo dorm;
	private Context context;
	private LayoutInflater inflater;
	private int chatType;
	private int chatUserNum = 0;
	private boolean isAttend = false;

	public DornAttendUserAdapter(Context context, List<DormUser> dormList,
			DormInfo dorm) {
		super();
		this.dormList = dormList;
		this.context = context;
		this.dorm = dorm;
		inflater = LayoutInflater.from(context);
		chatType = dorm.getDormType();
		isAttend = dorm.isIsAttend();
	}
	
	public boolean isAttend() {
		return isAttend;
	}

	public void setAttend(boolean isAttend) {
		this.isAttend = isAttend;
	}

	@Override
	public int getCount() {
		switch (chatType) {
		case 1:
			chatUserNum = 2;
			break;
		case 2:
			chatUserNum = 8;
			break;
		default:
			chatUserNum = 8;
			break;
		}
		if (dormList == null) {

			return 0;
		} else if (isAttend || dormList.size() == chatUserNum) {

			return dormList.size();
		}
		return dormList.size() + 1;
	}

	@Override
	public Object getItem(int position) {
		return dormList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("InflateParams")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (!isAttend && position == dormList.size()) {
			View addView = inflater.inflate(R.layout.item_dorm_vp_item_add,
					null);
			addView.findViewById(R.id.dorm_attend_add).setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
							new PostTask(UrlHelper.DORM_ATTEND,
									new UpdateViewListener() {
										@Override
										public void onComplete(Object obj) {
											int resultCode = ResultParse.getInstance().getResCode(obj.toString());
											String res = ResultParse.getInstance().getResStr(obj.toString());
											if(resultCode == 400){
												T.showShort(context, res);
												return;
											}
											L.i(TAG,"dorm_attend_add");
											MqttHelper.getInstance();
											isAttend = true;
											notifyDataSetChanged();
											// 0 未开启  1 开启  2满人 3关闭
											if(dorm.getStatus()==1){
												Intent i = new Intent();
												i.setClass(context,AtyDormChat.class);
												i.putExtra(AtyDormChat.DORM_ID, dorm.getId());
												context.startActivity(i);
											}
										}
									}, "Id", dorm.getId());
						}
					});
			return addView;
		} else {
			View picView = inflater.inflate(R.layout.item_dorm_vp_item_head,
					null);
			DormUser user = dormList.get(position);
			TextView nick = (TextView) picView
					.findViewById(R.id.tv_dorm_attend_nick);
			if(user.getName()!= null){
				nick.setText(user.getName());
			}else{
				nick.setText("肖平志");
			}

			RoundImageView picIBtn = (RoundImageView) picView
					.findViewById(R.id.dorm_attend_user_pic);
			String face = dormList.get(position).getFace();
			if (!StrUtils.isEmpty(face)) {
				GlobalContext.getInstance().imageLoader.displayImage(face,
						picIBtn, GlobalContext.options, null);
			} else {
				picIBtn.setImageResource(R.drawable.head_test);
			}
			if(user.getSex()==0){
				picIBtn.setBackgroundResource(R.drawable.bg_dorm_head_girl);
			}else if(user.getSex()==1){
				picIBtn.setBackgroundResource(R.drawable.bg_dorm_head_man);
			}
			picIBtn.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
				}
			});
			
			ImageButton del = (ImageButton) picView.findViewById(R.id.dorm_attend_user_delete);
			if(isAttend&&user.getId().equals(GlobalContext.getInstance().getUid())){
				del.setVisibility(View.VISIBLE);
				nick.setTextColor(Color.parseColor("#2b9ac5"));
				notifyDataSetChanged();
			} else {
				del.setVisibility(View.GONE);
				nick.setTextColor(Color.parseColor("#ffffff"));
				notifyDataSetChanged();
			}
			del.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					new PostTask(UrlHelper.DORM_EXIT, new UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							L.i("MyClickListener", "dorm_attend_user_delete");
							isAttend = false;
//							dormList.remove(position);
							notifyDataSetChanged();
						}
					}, "Id", dorm.getId());
				}
			});
			return picView;
		}
	}
}