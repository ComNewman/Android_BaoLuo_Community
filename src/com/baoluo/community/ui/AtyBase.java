package com.baoluo.community.ui;

import java.util.LinkedList;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baoluo.community.entity.AccountInfo;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.util.L;

public class AtyBase extends FragmentActivity {

	private static final String TAG = "AtyBase";

	private static LinkedList<AtyBase> queueAtys = new LinkedList<AtyBase>();

	private AccountInfo account;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		if (!queueAtys.contains(this)) {
			queueAtys.add(this);
		}
	}

	protected AtyBase getActivity(int index) {
		if (index < 0 || index >= queueAtys.size()) {
			throw new IllegalArgumentException("out of queueAtys");
		}
		return queueAtys.get(index);
	}

	protected AtyBase getCurrentActivity() {
		return queueAtys.getLast();
	}

	@Override
	public void onBackPressed() {
		if (queueAtys.size() == 1) {
			L.i(TAG, "当前activity是最后一个了！");
		} else {
			queueAtys.getLast().finish();
		}
	}

	@Override
	public void finish() {
		super.finish();
		if (!queueAtys.isEmpty()) {
			queueAtys.removeLast();
		}
	}
	
	public void finish(int nums) {
		for (int i = 0; i < nums; i++) {
			if (queueAtys.size() > 1) {
				queueAtys.getLast().finish();
			}
		}
	}

	public void exit() {
		while (queueAtys.size() > 0) {
			queueAtys.getLast().finish();
		}
		this.finish();
	}

	protected AccountInfo getAccountInfo() {
		return this.account;
	}

	protected void setAccountInfo(AccountInfo account) {
		this.account = account;
	}

	protected void saveAccount(String account, String psd) {
		SettingUtility.setAccount(account);
		SettingUtility.setPassword(psd);
	}

	protected void setEditTextEnter(final EditText etContent,
			final Context context) {
		etContent.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				InputMethodManager imm = (InputMethodManager) context
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etContent.getWindowToken(), 0);
				return true;
			}
		});
	}

	protected AccountInfo getAccountFromSharePreference() {
		AccountInfo acc = new AccountInfo();
		acc.setAccount(SettingUtility.getAccount());
		acc.setPassword(SettingUtility.getPassword());
		return acc;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev) {
		return super.dispatchTouchEvent(ev);
	}
}
