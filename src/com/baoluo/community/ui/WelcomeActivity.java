package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;

import com.baoluo.community.core.InitDatas;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TokenInfo;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.aty.user.AtyLogin;
import com.baoluo.community.ui.aty.user.AtySetNick;
import com.baoluo.community.util.NetUtil;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.util.DormQueueCache;

/**
 * 欢迎界面
 * 
 * @author Ryan_Fu 2015-5-5
 */
public class WelcomeActivity extends AtyBase {

	private String user, pwd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_welcome);
		init();
	}

	private void init() {
		user = SettingUtility.getAccount();
		pwd = SettingUtility.getPassword();

		if (verify()) {
			if (NetUtil.isConnnected(this)) {
				doLogin();
			}
		} else {
			startActivity(new Intent(WelcomeActivity.this, GuideActivity.class));
			WelcomeActivity.this.finish();
		}

	}

	private boolean verify() {
		if (StrUtils.isEmpty(user) || StrUtils.isEmpty(pwd)) {
			return false;
		}
		return true;
	}

	private void doLogin() {
		new PostTask(false, UrlHelper.LOGIN,
				new UpdateViewHelper.UpdateViewListener(TokenInfo.class) {
					@Override
					public void onComplete(Object obj) {
							TokenInfo token = (TokenInfo)obj;
							if (token != null) {
								SettingUtility.setTokenInfo(token);
								saveAccount(user, pwd);
								setUserInfo();
							} else {
								T.showShort(WelcomeActivity.this, "服务器异常...");
							}
					}

					@Override
					public void onFail() {
						T.showShort(WelcomeActivity.this, "登录失败！");
						startActivity(new Intent(WelcomeActivity.this, AtyLogin.class));
						WelcomeActivity.this.finish();
					}
				}, "grant_type", "password", "username", user, "password", pwd);
	}

	private void setUserInfo() {
		new GetTask(UrlHelper.CURRENT_USER_INFO,
				new UpdateViewHelper.UpdateViewListener(UserSelf.class) {
					@Override
					public void onComplete(Object obj) {
						UserSelf sel = (UserSelf)obj;
						SettingUtility.setUserSelf(sel);
						SettingUtility.setUid(sel.getId());
						String nickName = SettingUtility.getUserSelf()
								.getNickName();
						Intent i = new Intent();
						if (!StrUtils.isEmpty(nickName)) {
							i.setClass(WelcomeActivity.this, MainActivity.class);
							startActivity(i);
						} else {
							i.setClass(WelcomeActivity.this, AtySetNick.class);
							startActivity(i);
						}
						WelcomeActivity.this.finish();
						MqttHelper.getInstance();
						DormQueueCache.getInstance().initQueue();
						InitDatas.getInstance().initDatas();
						WelcomeActivity.this.startService(new Intent(
								"com.baoluo.community.service.BackService"));
					}
				});
	}
}
