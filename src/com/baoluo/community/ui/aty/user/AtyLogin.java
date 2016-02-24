package com.baoluo.community.ui.aty.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import com.baoluo.community.core.InitDatas;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TokenInfo;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.NetUtil;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.util.DormQueueCache;

public class AtyLogin extends AtyBase implements OnClickListener ,HeadViewListener{
	private final String TAG = this.getClass().getName();
	
	private HeadView headView;
	private EditText etUser;
	private EditText etPwd;
	private Button btnLogin,btnFind;
	private String nickName;
	private String user;
	private String password;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_login);
		initUI();
		initData();
	}

	private void initData() {
		String user = SettingUtility.getAccount();
		String pwd = SettingUtility.getPassword();
		etUser.setText(user);
		etPwd.setText(pwd);
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_login_head);
		headView.setHeadViewListener(this);
		
		etUser = (EditText) findViewById(R.id.et_login_account);
		etPwd = (EditText) findViewById(R.id.et_login_psd);
		btnLogin = (Button) findViewById(R.id.btn_login_login);
		btnFind = (Button) findViewById(R.id.btn_login_find_psd);

		btnFind.setOnClickListener(this);
		btnLogin.setOnClickListener(this);
	}

	private boolean verify() {
		user = etUser.getText().toString();
		password = etPwd.getText().toString();
		if (StrUtils.isEmpty(user)) {
			etUser.setFocusable(true);
			T.showShort(this, "用户名不能为空");
			return false;
		}
		if (StrUtils.isEmpty(password)) {
			etPwd.setFocusable(true);
			T.showShort(this, "密码不能为空");
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.btn_login_login:
			if (NetUtil.isConnnected(AtyLogin.this)) {
				if (verify()) {
					doLogin();
				}
			}
			break;
		case R.id.btn_login_find_psd:
			i.setClass(AtyLogin.this, AtyFindPswd.class);
			startActivity(i);
			break;
		default:
			break;
		}
	}

	private void doLogin() {
		new PostTask(false, UrlHelper.LOGIN,
				new UpdateViewHelper.UpdateViewListener(TokenInfo.class) {
					@Override
					public void onComplete(Object obj) {
							TokenInfo token = (TokenInfo)obj;
							if (token != null) {
								SettingUtility.setTokenInfo(token);
								saveAccount(user, password);
								setUserInfo();
							} else {
								T.showShort(AtyLogin.this, "服务器异常...");
							}
					}

					@Override
					public void onFail() {
						T.showShort(AtyLogin.this, "登录失败！账号密码不对");
					}
				}, "grant_type", "password",
				"username", user, 
				"password",
				password);
	}

	private void setUserInfo() {
		new GetTask(UrlHelper.CURRENT_USER_INFO,
				new UpdateViewHelper.UpdateViewListener(UserSelf.class) {
					@Override
					public void onComplete(Object obj) {
						UserSelf sel = (UserSelf)obj;
						SettingUtility.setUserSelf(sel);
						SettingUtility.setUid(sel.getId());
						nickName = SettingUtility.getUserSelf().getNickName();
						Intent i = new Intent();
						if (!StrUtils.isEmpty(nickName)) {
							i.setClass(AtyLogin.this, MainActivity.class);
							startActivity(i);
						} else {
							i.setClass(AtyLogin.this, AtySetNick.class);
							startActivity(i);
						}
						AtyLogin.this.finish();
						MqttHelper.getInstance();
						DormQueueCache.getInstance().initQueue();
						InitDatas.getInstance().initDatas();
						AtyLogin.this.startService(new Intent(
								"com.baoluo.community.service.BackService"));
					}
				});
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		if (ev.getAction() == MotionEvent.ACTION_DOWN) {
			View v = getCurrentFocus();
			if (isShouldHideInput(v, ev)) {
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				if (imm != null) {
					imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
				}
			}
			return super.dispatchTouchEvent(ev);
		}
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof EditText)) {
			int[] leftTop = { 0, 0 };
			v.getLocationInWindow(leftTop);
			int left = leftTop[0];
			int top = leftTop[1];
			int bottom = top + v.getHeight();
			int right = left + v.getWidth();
			if (event.getX() > left && event.getX() < right
					&& event.getY() > top && event.getY() < bottom) {
				return false;
			} else {
				return true;
			}
		}
		return false;
	}

	@Override
	public void leftListener() {
	}

	@Override
	public void rightListener() {
		Intent i = new Intent();
		i.setClass(AtyLogin.this, AtyRegister.class);
		startActivity(i);
		
	}
}
