package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

import com.baoluo.community.core.Configs;
/**
 * 宿舍选择推送类型
 * @author xiangyang.fu
 *
 */
public class AtyDormSelType extends AtyBase implements OnClickListener {
	
	public static final int TYPE = 0x7;
	public ImageButton  btnGroup, btnSingle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_dorm_range_type);
		initUI();
	}

	private void initUI() {

		
		btnGroup = (ImageButton) findViewById(R.id.ib_drom_group);
		btnSingle = (ImageButton) findViewById(R.id.ib_drom_single);

		
		btnGroup.setOnClickListener(this);
		btnSingle.setOnClickListener(this);
		btnGroup.setSelected(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_drom_group:
			doSelect(Configs.PUBLISH_TYPE_GROUP);
			break;
		case R.id.ib_drom_single:
			doSelect(Configs.PUBLISH_TYPE_SINGLE);
			break;
		default:
			break;
		}
	}
	
	private void doSelect(int type){
		Intent i = new Intent();
		i.putExtra("type", type);
		setResult(RESULT_OK, i);
		finish();
	}
}

