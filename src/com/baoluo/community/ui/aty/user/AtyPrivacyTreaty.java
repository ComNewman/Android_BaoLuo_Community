package com.baoluo.community.ui.aty.user;

import android.os.Bundle;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
/**
 * 隐私条约
 * @author xiangyang.fu
 *
 */
public class AtyPrivacyTreaty extends AtyBase implements HeadViewListener{
	
	private HeadView headView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_privacy_treaty);
		initUi();
	}

	private void initUi() {
		headView = (HeadView) findViewById(R.id.privacy_headview);
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
