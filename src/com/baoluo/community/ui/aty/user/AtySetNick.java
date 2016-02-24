package com.baoluo.community.ui.aty.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.community.util.NetUtil;
import com.baoluo.community.util.T;
import com.baoluo.im.MqttHelper;

/**
 * 设置昵称和性别
 * 
 * @author xiangyang.fu
 * 
 */
public class AtySetNick extends AtyBase implements OnClickListener {
	
	private static final String TAG = "AtySetNick";
	private EditText etNick;
	private CheckBox cbMan, cbGril;
	private Button btnSure;
	private int nickMaxLen = 8;


	private boolean isGirl = false;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_name_sex);
		initUI();
	}

	private void initUI() {
		etNick = (EditText) findViewById(R.id.et_name_sex_nick);
		cbGril = (CheckBox) findViewById(R.id.cb_name_sex_nv);
		cbMan = (CheckBox) findViewById(R.id.cb_name_sex_man);
		btnSure = (Button) findViewById(R.id.btn_name_sex_sure);
		btnSure.setOnClickListener(this);

		cbMan.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
										 boolean isChecked) {
				if (isChecked) {
					L.i("onCheckedChanged", "man ischecked");
					isGirl = false;
					cbGril.setChecked(false);
				} else {
					isGirl = true;
				}
			}
		});
		cbGril.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				if (isChecked) {
					L.i("onCheckedChanged", "girl ischecked");
					isGirl = true;
					cbMan.setChecked(false);
				} else{
					isGirl = false;
				}
			}
		});
		etNick.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			@Override
			public void afterTextChanged(Editable s) {
				Editable editable = etNick.getText();
				int len = editable.length();
				if (len > nickMaxLen) {
					T.showShort(AtySetNick.this, "最多可输入"+nickMaxLen+"个字！");
					int selEndIndex = Selection.getSelectionEnd(editable);
					String str = editable.toString();
					// 截取新字符串
					String newStr = str.substring(0, nickMaxLen);
					etNick.setText(newStr);
					editable = etNick.getText();
					// 新字符串的长度
					int newLen = editable.length();
					// 旧光标位置超过字符串长度
					if (selEndIndex > newLen) {
						selEndIndex = editable.length();
					}
					// 设置新光标所在的位置
					Selection.setSelection(editable, selEndIndex);
				}
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_name_sex_sure:
			if(NetUtil.isConnnected(this)){
				setNickSex();
			}
			break;

		default:
			break;
		}
	}
	private void setNickSex() {
		String name = etNick.getText().toString();
		String sex = "2";

		if(isGirl){
			sex = "0";
		}else{
			sex = "1";
		}
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						setUserInfo();
						T.showShort(AtySetNick.this, "填写昵称和性别完成");
						AtySetNick.this.finish();
					}
				},"NickName", name,"Sex", sex);
	}
	
	private void setUserInfo() {
		new GetTask(UrlHelper.CURRENT_USER_INFO, null,
				new UpdateViewHelper.UpdateViewListener(UserSelf.class) {
					@Override
					public void onComplete(Object obj) {
						UserSelf sel = (UserSelf)obj;
						L.i(TAG, "myId=" + sel.getId());
						SettingUtility.setUserSelf(sel);
						SettingUtility.setUid(sel.getId());
						MqttHelper.getInstance();
						AtySetNick.this.startService(new Intent(
								"com.baoluo.community.service.BackService"));
						onBackPressed();
						Intent i = new Intent();
						i.setClass(AtySetNick.this, MainActivity.class);
						startActivity(i);
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
}
