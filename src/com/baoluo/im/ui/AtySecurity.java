package com.baoluo.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TokenInfo;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.aty.user.AtyLogin;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.T;
import com.baoluo.im.MqttHelper;

/**
 * 隐私与安全
 * 
 * @author tao.lai
 * 
 */
public class AtySecurity extends AtyBase implements HeadViewListener,
		OnClickListener {

	private static final String TAG = "AtySecurity";

	private HeadView headView;
	private ImageButton ibMyLocation, ibVerify, ibByPhone; 
	private Button btnExit;

	private boolean showLocation, isVerify, isByPhone,isByAccount;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_security);
		initUI();
		initData();
	}

	private void initData() {
		isByAccount = SettingUtility.getFindMeBaoluoBoolean();
		isByPhone = SettingUtility.getFindMePhoneBoolean();
		isVerify = SettingUtility.getAddMeCheckBoolean();
		showLocation = SettingUtility.getLocationBoolean();
		
		ibByPhone.setSelected(isByPhone);
		ibVerify.setSelected(isVerify);
		ibMyLocation.setSelected(showLocation);
		
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_security_head);
		headView.setHeadViewListener(this);
		ibMyLocation = (ImageButton) findViewById(R.id.ib_my_location);
		ibVerify = (ImageButton) findViewById(R.id.ib_verify);
		ibByPhone = (ImageButton) findViewById(R.id.ib_by_phone);
		btnExit = (Button) findViewById(R.id.btn_exit);

		ibByPhone.setOnClickListener(this);
		ibMyLocation.setOnClickListener(this);
		ibVerify.setOnClickListener(this);
		btnExit.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_my_location:
			showLocation = !showLocation;
			ibSwitch(showLocation, ibMyLocation);
			SettingUtility.setLocationBoolean(showLocation);
			break;
		case R.id.ib_verify:
			isVerify = !isVerify;
			ibSwitch(isVerify, ibVerify);
			SettingUtility.setAddMeBoolean(isVerify);
			if(isVerify){
				addMeBoolean(1);
			}else{
				addMeBoolean(0);
			}
			break;
		case R.id.ib_by_phone:
			isByPhone = !isByPhone;
			ibSwitch(isByPhone, ibByPhone);
			SettingUtility.setFindMePhoneBoolean(isByPhone);
			if(isByPhone){
				findMePhone(1);
			}else{
				findMePhone(0);
			}
			break;
		case R.id.btn_exit:
			exitApp();
			break;
		default:
			break;
		}
	}
	private void addMeBoolean(int addMe) {
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtySecurity.this, "更改加好友验证");
					}
				},"IsValidateFriend",String.valueOf(addMe));
	}
	private void findMePhone(int findMe) {
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtySecurity.this, "通过手机找到我");
					}
				},"AllowPhoneSearch",String.valueOf(findMe));
	}
	private void ibSwitch(boolean isSel, ImageButton ib) {
		ib.setSelected(isSel);
	}

	private void exitApp() {
		new PostTask(UrlHelper.SET_PING, new UpdateViewListener() {
			@Override
			public void onComplete(Object obj) {
				exit();
				MqttHelper.getInstance().disConnection();
				SettingUtility.setTokenInfo(new TokenInfo());
				startActivity(new Intent(AtySecurity.this, AtyLogin.class));
			}
		},"Status", 0 + "");
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {

	}
}
