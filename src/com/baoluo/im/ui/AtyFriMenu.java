package com.baoluo.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;

/**
 * 加好友操作
 * 
 * @author tao.lai
 * 
 */
public class AtyFriMenu extends AtyBase implements OnClickListener {
	public static final int OPTION_CODE = 0x5;
	public static final String OPTION_TYPE = "option_type";
	public static final int FRI_JUBAO = 0x0;
	public static final int FRI_LAHEI = 0x1;
	public static final int SETTING_CLEAR_CACHE = 0X2;
	public static final int SETTING_CLEAR_CACHE_CANCLE = 0X3;
	private Button btnJubao, btnLahei;
	private int flag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_fri_menu);
		flag = getIntent().getIntExtra("flag", -1);
		btnJubao = (Button) findViewById(R.id.btn_jubao);
		btnLahei = (Button) findViewById(R.id.btn_lahei);
		if (flag == AtyMsgSetting.FLAG_CACHE) {
			btnJubao.setText("清除");
			btnLahei.setText("取消");
		}
		btnJubao.setOnClickListener(this);
		btnLahei.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_jubao:
			if (btnJubao.getText().equals("举报")) {
				setResult(RESULT_OK);
				doOption(FRI_JUBAO);
			} else if (btnJubao.getText().equals("清除")) {
				doOption(SETTING_CLEAR_CACHE);
			}
			break;
		case R.id.btn_lahei:
			if (btnLahei.getText().equals("拉黑")) {
				doOption(FRI_LAHEI);
			} else if (btnLahei.getText().equals("取消")) {
				doOption(SETTING_CLEAR_CACHE_CANCLE);
			}
			break;
		default:
			break;
		}
	}

	private void doOption(int type) {
		Intent i = new Intent();
		i.putExtra(OPTION_TYPE, type);
		setResult(RESULT_OK, i);
		finish();
	}
}
