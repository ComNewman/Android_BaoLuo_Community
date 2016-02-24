package com.baoluo.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.RelativeLayout;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;

public class AtySetting extends AtyBase implements OnClickListener,
		HeadViewListener {

	private RelativeLayout rlSetting, rlSafety, rlChangePswd,rlAbout;
	private HeadView headView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_setting);
		initView();
	}

	private void initView() {
		headView = (HeadView) findViewById(R.id.hv_setting_head);
		headView.setHeadViewListener(this);

		rlSetting = (RelativeLayout) findViewById(R.id.rl_setting_msg);
		rlSafety = (RelativeLayout) findViewById(R.id.rl_setting_safety);
		rlAbout = (RelativeLayout) findViewById(R.id.rl_setting_about);
		rlChangePswd = (RelativeLayout) findViewById(R.id.rl_setting_change_pswd);
		rlChangePswd.setOnClickListener(this);
		rlSetting.setOnClickListener(this);
		rlSafety.setOnClickListener(this);
		rlAbout.setOnClickListener(this);
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {

	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.rl_setting_about:
			i.setClass(AtySetting.this, AtyAboutBaoluo.class);
			break;
		case R.id.rl_setting_safety:
			i.setClass(AtySetting.this, AtySecurity.class);
			break;
		case R.id.rl_setting_msg:
			i.setClass(AtySetting.this, AtyMsgSetting.class);
			break;
		case R.id.rl_setting_change_pswd:
			i.setClass(AtySetting.this, AtyChangePswd.class);
			break;
		}
		startActivity(i);
	}

}
