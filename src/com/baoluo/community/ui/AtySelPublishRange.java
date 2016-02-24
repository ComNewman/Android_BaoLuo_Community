package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baoluo.community.core.Configs;

/**
 * 推送范围
 * 
 * @author xiangyang.fu
 * 
 */
public class AtySelPublishRange extends AtyBase implements OnClickListener {

	public static final int PUBLISH_RANGE_RESULT_CODE = 0x6;
	public Button btnPublic, btnAppoint, btnSchool, btnGroup, btnFriends;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_publish_range);
		initUI();
	}

	private void initUI() {

		btnPublic = (Button) findViewById(R.id.btn_publish_range_public);
		btnAppoint = (Button) findViewById(R.id.btn_publish_range_appoint);
		btnSchool = (Button) findViewById(R.id.btn_publish_range_school);
		btnGroup = (Button) findViewById(R.id.btn_publish_range_group);
		btnFriends = (Button) findViewById(R.id.btn_publish_range_friends);

		btnPublic.setOnClickListener(this);
		btnAppoint.setOnClickListener(this);
		btnSchool.setOnClickListener(this);
		btnGroup.setOnClickListener(this);
		btnFriends.setOnClickListener(this);
		btnPublic.setSelected(true);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_publish_range_public:
			btnPublic.setSelected(true);
			doSelect(Configs.PUBLISH_RANGE_PUBLIC);
			break;
		case R.id.btn_publish_range_appoint:
			btnAppoint.setSelected(true);
			btnPublic.setSelected(false);
			doSelect(Configs.PUBLISH_RANGE_APPOINT);
			break;
		case R.id.btn_publish_range_school:
			btnSchool.setSelected(true);
			btnPublic.setSelected(false);
			doSelect(Configs.PUBLISH_RANGE_SCHOOL);
			break;
		case R.id.btn_publish_range_group:
			btnGroup.setSelected(true);
			btnPublic.setSelected(false);
			doSelect(Configs.PUBLISH_RANGE_GROUP);
			break;
		case R.id.btn_publish_range_friends:
			btnFriends.setSelected(true);
			btnPublic.setSelected(false);
			doSelect(Configs.PUBLISH_RANGE_FRIENDS);
			break;
		default:
			break;
		}
	}

	private void doSelect(int tag) {
		Intent i = new Intent();
		i.putExtra("range", tag);
		setResult(RESULT_OK, i);
		finish();
	}
}
