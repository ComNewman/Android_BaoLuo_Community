package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

/**
 * 好友信息页面的举报
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyFriInfoInform extends AtyBase implements OnClickListener {
	
	public static final String OPTION_TYPE = "option_type";
	private Button btnInform, btnCancle;
	public static final int FRI_INFORM = 0x0;
	public static final int FRI_CANCLE = 0x1;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_fri_info_inform);
		initUi();
	}

	private void initUi() {
		btnInform = (Button) findViewById(R.id.btn_fri_info_inform);

		btnCancle = (Button) findViewById(R.id.btn_fri_info_cancle);

		btnInform.setOnClickListener(this);
		btnCancle.setOnClickListener(this);

	}
	private void doOption(int type) {
		Intent i = new Intent();
		i.putExtra(OPTION_TYPE, type);
		setResult(RESULT_OK, i);
		finish();
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fri_info_inform:
			doOption(FRI_INFORM);
			break;
		case R.id.btn_fri_info_cancle:
			doOption(FRI_CANCLE);
			break;

		default:
			break;
		}

	}
}
