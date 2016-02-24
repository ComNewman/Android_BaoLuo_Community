package com.baoluo.im.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.LinearLayout.LayoutParams;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.baoluo.community.support.zxing.view.CaptureActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.T;
import com.baoluo.im.ui.AtyAddFriend;
import com.baoluo.im.ui.AtyAddUser2Group;

public class PopupWindowHelp implements OnClickListener {

	private Context mContext;
	private PopupWindow popupWindow;

	/**
	 * 二维码响应码
	 */
	public final static int SCANNIN_GREQUEST_CODE = 1;

	public PopupWindowHelp(Context mContext) {
		super();
		this.mContext = mContext;
	}
	/**
	 * 通知页面的弹出窗
	 * @param view
	 * @param yoff
	 */
	public void showNotificationMenu(View view, int yoff) {
		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(mContext).inflate(
				R.layout.pop_notification_menu, null);

		RelativeLayout rlReply = (RelativeLayout) contentView
				.findViewById(R.id.rl_pop_reply_friend);
		RelativeLayout rlCancel = (RelativeLayout) contentView
				.findViewById(R.id.rl_pop_cancle_attend);
		rlReply.setOnClickListener(this);
		rlCancel.setOnClickListener(this);

		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// 设置好参数之后再show
		// popupWindow.showAsDropDown(view);
		popupWindow.showAsDropDown(view, 0, yoff);

	}
	/**
	 * 
	 * @param view
	 * @param yoff
	 */
	public void showPopupWindow(View view, int yoff) {

		// 一个自定义的布局，作为显示的内容
		View contentView = LayoutInflater.from(mContext).inflate(
				R.layout.aty_pop_add, null);

		RelativeLayout rlGroup = (RelativeLayout) contentView
				.findViewById(R.id.rl_pop_add_group);
		RelativeLayout rlAdd = (RelativeLayout) contentView
				.findViewById(R.id.rl_pop_add_add);
		RelativeLayout rlRichScan = (RelativeLayout) contentView
				.findViewById(R.id.rl_pop_add_richscan);
		rlGroup.setOnClickListener(this);
		rlAdd.setOnClickListener(this);
		rlRichScan.setOnClickListener(this);

		popupWindow = new PopupWindow(contentView, LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT, true);

		popupWindow.setTouchable(true);

		popupWindow.setTouchInterceptor(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {

				Log.i("mengdd", "onTouch : ");

				return false;
				// 这里如果返回true的话，touch事件将被拦截
				// 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
			}
		});

		// 如果不设置PopupWindow的背景，无论是点击外部区域还是Back键都无法dismiss弹框
		// 我觉得这里是API的一个bug
		popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
		// 设置好参数之后再show
		// popupWindow.showAsDropDown(view);
		popupWindow.showAsDropDown(view, 0, yoff);

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_pop_add_add:
			T.showShort(mContext, "添加朋友");
			mContext.startActivity(new Intent(mContext, AtyAddFriend.class));
			popupWindow.dismiss();
			break;
		case R.id.rl_pop_add_group:
			T.showShort(mContext, "发起群聊");
			Intent it = new Intent();
			it.putExtra(AtyAddUser2Group.CODE_OPTION_TYPE, AtyAddUser2Group.OPTION_GROUP_CREATE);
			it.setClass(mContext, AtyAddUser2Group.class);
			mContext.startActivity(it);
			popupWindow.dismiss();
			break;
		case R.id.rl_pop_add_richscan:
			Intent intent = new Intent();
			intent.setClass(mContext, CaptureActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			((Activity) mContext).startActivity(intent);
			popupWindow.dismiss();
			break;
		case R.id.rl_pop_reply_friend:
			T.showShort(mContext, "发给朋友");
			popupWindow.dismiss();
			break;
		case R.id.rl_pop_cancle_attend:
			T.showShort(mContext, "取消关注");
			popupWindow.dismiss();
			break;
		default:
			break;
		}
	}
}
