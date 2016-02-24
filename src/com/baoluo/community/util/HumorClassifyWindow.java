package com.baoluo.community.util;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.ui.R;

/**
 * 心情的筛选弹出窗
 * 
 * @author xiangyang.fu
 * 
 */
public class HumorClassifyWindow extends PopupWindow implements OnClickListener {
	private String TAG = MoreWindow.class.getSimpleName();
	private Activity mContext;
	private TextView tvSexM, tvSexG, tvSexAll, tvRangeNear, tvRangeCity,
			tvRangeAll, tvHotOne, tvHotThree, tvHotSeven;
	private int mWidth;
	private int sex;
	private int range;
	private int hot;

	public int getSex() {
		return sex;
	}

	public void setSex(int sex) {
		this.sex = sex;
	}

	public int getRange() {
		return range;
	}

	public void setRange(int range) {
		this.range = range;
	}

	public int getHot() {
		return hot;
	}

	public void setHot(int hot) {
		this.hot = hot;
	}

	public HumorClassifyWindow(Activity context) {
		mContext = context;
	}

	public void showPopupWindow(View parent) {
		final RelativeLayout layout = (RelativeLayout) LayoutInflater.from(
				mContext).inflate(R.layout.humor_classify_window, null);
		DisplayMetrics metrics = new DisplayMetrics();
		mContext.getWindowManager().getDefaultDisplay().getMetrics(metrics);
		mWidth = metrics.widthPixels;
		setContentView(layout);

		tvSexM = (TextView) layout.findViewById(R.id.tv_classify_m);
		tvSexG = (TextView) layout.findViewById(R.id.tv_classify_g);
		tvSexAll = (TextView) layout.findViewById(R.id.tv_classify_all);
		tvRangeNear = (TextView) layout
				.findViewById(R.id.tv_classify_range_near);
		tvRangeCity = (TextView) layout
				.findViewById(R.id.tv_classify_range_city);
		tvRangeAll = (TextView) layout.findViewById(R.id.tv_classify_range_all);
		tvHotOne = (TextView) layout.findViewById(R.id.tv_classify_hot_one);
		tvHotThree = (TextView) layout.findViewById(R.id.tv_classify_hot_three);
		tvHotSeven = (TextView) layout.findViewById(R.id.tv_classify_hot_seven);
		tvSexAll.setOnClickListener(this);
		tvSexG.setOnClickListener(this);
		tvSexM.setOnClickListener(this);
		tvRangeNear.setOnClickListener(this);
		tvRangeCity.setOnClickListener(this);
		tvRangeAll.setOnClickListener(this);
		tvHotOne.setOnClickListener(this);
		tvHotThree.setOnClickListener(this);
		tvHotSeven.setOnClickListener(this);
		setHot(0);
		setRange(0);
		setSex(0);
		this.setWidth(mWidth);
		this.setHeight(LayoutParams.WRAP_CONTENT);
		this.setFocusable(true);
		this.setTouchable(true);
		this.update();
		this.setBackgroundDrawable(new ColorDrawable(Color
				.parseColor("#ffffff")));
		 getBackground().setAlpha(188);
		this.setOutsideTouchable(true);
		this.setAnimationStyle(R.style.AnimationPreview);  
		if (!this.isShowing()) {
			this.showAsDropDown(parent, parent.getLayoutParams().width / 2, 0);
		} else {
			this.dismiss();
		}
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_classify_hot_one:
			tvHotOne.setSelected(!tvHotOne.isSelected());
			tvHotSeven.setSelected(false);
			tvHotThree.setSelected(false);
			if (tvHotOne.isSelected()) {
				setHot(128);
			} else {
				setHot(0);
			}
			break;
		case R.id.tv_classify_hot_three:
			tvHotOne.setSelected(false);
			tvHotSeven.setSelected(false);
			tvHotThree.setSelected(!tvHotThree.isSelected());
			if (tvHotThree.isSelected()) {
				setHot(256);
			} else {
				setHot(0);
			}
			break;
		case R.id.tv_classify_hot_seven:
			tvHotOne.setSelected(false);
			tvHotSeven.setSelected(!tvHotSeven.isSelected());
			tvHotThree.setSelected(false);
			if (tvHotSeven.isSelected()) {
				setHot(512);
			} else {
				setHot(0);
			}
			break;
		case R.id.tv_classify_m:
			tvSexAll.setSelected(false);
			tvSexG.setSelected(false);
			tvSexM.setSelected(!tvSexM.isSelected());
			if (tvSexM.isSelected()) {
				setSex(1);
			} else {
				setSex(0);
			}
			break;
		case R.id.tv_classify_g:
			tvSexAll.setSelected(false);
			tvSexG.setSelected(!tvSexG.isSelected());
			tvSexM.setSelected(false);
			if (tvSexG.isSelected()) {
				setSex(2);
			} else {
				setSex(0);
			}
			break;
		case R.id.tv_classify_all:
			tvSexAll.setSelected(!tvSexAll.isSelected());
			tvSexG.setSelected(false);
			tvSexM.setSelected(false);
			if (tvSexAll.isSelected()) {
				setSex(4);
			} else {
				setSex(0);
			}
			break;
		case R.id.tv_classify_range_all:
			tvRangeAll.setSelected(!tvRangeAll.isSelected());
			tvRangeCity.setSelected(false);
			tvRangeNear.setSelected(false);
			if (tvRangeAll.isSelected()) {
				setRange(64);
			} else {
				setRange(0);
			}
			break;
		case R.id.tv_classify_range_city:
			tvRangeAll.setSelected(false);
			tvRangeCity.setSelected(!tvRangeCity.isSelected());
			tvRangeNear.setSelected(false);
			if (tvRangeCity.isSelected()) {
				setRange(32);
			} else {
				setRange(0);
			}
			break;
		case R.id.tv_classify_range_near:
			tvRangeAll.setSelected(false);
			tvRangeCity.setSelected(false);
			tvRangeNear.setSelected(!tvRangeNear.isSelected());
			if (tvRangeNear.isSelected()) {
				setRange(16);
			} else {
				setRange(0);
			}
			break;
		}
	}

}
