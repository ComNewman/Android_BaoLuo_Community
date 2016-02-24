package com.baoluo.community.ui.customview;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoluo.community.ui.R;

public class MyProgress {

	public static Dialog getInstance(Context ctx,String msg) {
		//Context ctx = GlobalContext.getInstance();
		LayoutInflater inflater = LayoutInflater.from(ctx);
		View v = inflater.inflate(R.layout.my_progress, null);
		LinearLayout layout = (LinearLayout) v.findViewById(R.id.dialog_view);// 加载布局

		ImageView spaceshipImage = (ImageView) v.findViewById(R.id.img);
		TextView tipTextView = (TextView) v.findViewById(R.id.tip_text);

		Animation hyperspaceJumpAnimation = AnimationUtils.loadAnimation(ctx,
				R.anim.my_progress);
		
		spaceshipImage.startAnimation(hyperspaceJumpAnimation);
		tipTextView.setText(msg);// 设置加载信息

		Dialog loadingDialog = new Dialog(ctx, R.style.my_progress);

		loadingDialog.setCancelable(false);
		loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.MATCH_PARENT,
				LinearLayout.LayoutParams.MATCH_PARENT));
		return loadingDialog;
	}

}
