package com.baoluo.community.ui.barrage;

import android.content.Context;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * 动画帮助类
 * 
 * @author xiangyang.fu
 * 
 */
public class AnimationHelper {

	public static Animation createTranslateAnim(Context context, int fromX,
			int toX) {

		Log.i("AnimationHelper",
				"check getScreenWidth:" + ScreenUtils.getScreenW(context));
		TranslateAnimation tlAnim = new TranslateAnimation(fromX, toX, 0, 0);
		long duration = (long) (Math.abs(toX - fromX) * 1.0f
				/ ScreenUtils.getScreenW(context) * 4000);
		tlAnim.setDuration(duration);
		tlAnim.setInterpolator(new DecelerateAccelerateInterpolator());
		tlAnim.setFillAfter(true);
		return tlAnim;

	}
}
