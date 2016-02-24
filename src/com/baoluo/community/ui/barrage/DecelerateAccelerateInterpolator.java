package com.baoluo.community.ui.barrage;

import android.view.animation.Interpolator;
/**
 * 动画变化率
 * @author xiangyang.fu
 *
 */
public class DecelerateAccelerateInterpolator implements Interpolator { 
	@Override 
	public float getInterpolation(float input) { 
		return (float) (Math.tan((input * 2 - 1) / 4 * Math.PI)) / 2.0f + 0.5f; 
		}
}