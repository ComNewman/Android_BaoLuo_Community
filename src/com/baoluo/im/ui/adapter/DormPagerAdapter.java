package com.baoluo.im.ui.adapter;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.entity.DormInfo;
import com.baoluo.community.entity.DormUser;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.customview.ThumbnailGridView;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;

/**
 * 宿舍pagerAdapter
 * 
 * @author xiangyang.fu
 * 
 */
public class DormPagerAdapter extends PagerAdapter {

	private List<DormInfo> dormInfos;
	private Context context;
	private LayoutInflater inflater;
	private DornAttendUserAdapter adapter;
	private List<DormUser> users;

	public DormPagerAdapter(List<DormInfo> dormInfos, Context context) {
		super();
		this.dormInfos = dormInfos;
		this.context = context;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {

		return dormInfos == null ? 0 : dormInfos.size();
	}

	@Override
	public boolean isViewFromObject(View view, Object object) {
		return view == object;
	}

	@SuppressLint("InflateParams")
	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		final DormInfo item = dormInfos.get(position);
		View convertView = inflater.inflate(R.layout.item_dorm_vp, null);
		L.i("instantiateItem", "convertView==null?:  " + (convertView == null));
		TextView desc = (TextView) convertView.findViewById(R.id.tv_dorm_desc);
		RoundImageView head = (RoundImageView) convertView
				.findViewById(R.id.rv_dorm_avatar);
		TextView nick = (TextView) convertView.findViewById(R.id.tv_dorm_nick);
		TextView time = (TextView) convertView.findViewById(R.id.tv_dorm_time);
		ThumbnailGridView gv = (ThumbnailGridView) convertView
				.findViewById(R.id.gv_dorm_item_attenduser);
		ImageView pic = (ImageView) convertView
				.findViewById(R.id.iv_dorm_list_pic);
		String gvKey = "gv" + position;
		String tvKey = "tv" + position;
		gv.setTag(gvKey);
		time.setTag(tvKey);
		if (item != null) {
			users = item.getUsers();
			adapter = new DornAttendUserAdapter(context, users, item);
			gv.setAdapter(adapter);
			if (item.isIsAttend()) {
				adapter.setAttend(true);
				adapter.notifyDataSetChanged();
			} else {
				adapter.setAttend(false);
				adapter.notifyDataSetChanged();
			}
		/*	gv.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
				}
			});*/
			desc.setText(item.getDesc());
			nick.setText(item.getOwner().getName());
			if(item.getUsers().size()>0&&item.getStatus()==0){
				time.setText("正在排队中");
			}else{
				time.setText(item.getShowTime());
			}
			if (!StrUtils.isEmpty(item.getOwner().getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(item
						.getOwner().getFace(), head, GlobalContext.options,
						null);
			}
			if (!StrUtils.isEmpty(item.getPictures().getUrl())) {
				GlobalContext.getInstance().imageLoader.displayImage(item
						.getPictures().getUrl(), pic, GlobalContext.options,
						null);
			}
			if (item.getOwner().getSex() == 0) {
				head.setBackgroundResource(R.drawable.bg_dorm_head_girl);
			} else if (item.getOwner().getSex() == 1) {
				head.setBackgroundResource(R.drawable.bg_dorm_head_man);
			}
		}
		container.addView(convertView);
		return convertView;
	}

	/*
	 * @Override public int getItemPosition(Object object) { return
	 * POSITION_NONE; }
	 */
	/*
	 * @Override public void notifyDataSetChanged() { int key = 0; for(int i =
	 * 0; i < dormInfos.size(); i++) { // TODO } super.notifyDataSetChanged(); }
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
		container.removeView((View) object);
	}
}
