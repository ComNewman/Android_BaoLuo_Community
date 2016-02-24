package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v4.view.ViewPager.PageTransformer;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baoluo.community.ui.adapter.SendPagerAdapter;
import com.baoluo.community.ui.aty.user.AtyRegister;
import com.baoluo.im.util.ZoomOutPageTransformer;

/**
 * 第一次进入应用的引导界面
 * 
 * @author Ryan_Fu 2015-5-5
 */
public class GuideActivity extends AtyBase implements OnPageChangeListener {

	private ViewPager vpGuide;
	private List<View> views;
	private int curPage, preState;
	private static final int[] pics = { R.drawable.bg_guide_01,
			R.drawable.bg_guide_02, R.drawable.bg_guide_03 };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_guide);

		initUI();
	}

	private void initUI() {
		views = new ArrayList<View>();
		vpGuide = (ViewPager) findViewById(R.id.vp_guide);
		LinearLayout.LayoutParams mParams = new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT,
				LinearLayout.LayoutParams.WRAP_CONTENT);
		for (int i = 0; i < pics.length; i++) {
			ImageView iv = new ImageView(this);
			iv.setLayoutParams(mParams);
			iv.setImageResource(pics[i]);
			iv.setScaleType(ImageView.ScaleType.FIT_XY);
			views.add(iv);
		}
		vpGuide.setAdapter(new SendPagerAdapter(views));
		vpGuide.setCurrentItem(0);
		vpGuide.setOnPageChangeListener(this);
		vpGuide.setPageTransformer(true, (PageTransformer) new ZoomOutPageTransformer());  
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {
		if (preState == 1 && arg0 == 0 && curPage == 0) {
			vpGuide.setCurrentItem(2);
		} else if (preState == 1 && arg0 == 0
				&& curPage ==pics.length - 1) {
			startActivity(new Intent(GuideActivity.this,AtyRegister.class));
			onBackPressed();
		}
		preState = arg0;
	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		curPage = arg0;

	}

	@Override
	public void onPageSelected(int arg0) {
		// TODO
	}
}
