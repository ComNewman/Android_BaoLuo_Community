package com.baoluo.im.ui;

import android.os.Bundle;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;

/**
 * 功能介绍
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyFunctionIntroduce extends AtyBase implements HeadViewListener{
	
	private HeadView headView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_function_introduction);
		initUI();
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.function_headview);
		headView.setHeadViewListener(this);
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		
	}
}
