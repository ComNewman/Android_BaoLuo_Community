package com.baoluo.community.ui.aty.user;

import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.util.GetCodeTimer;

/**
 * 找回密码
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyFindPswd extends AtyBase implements OnClickListener,HeadViewListener{
	
	private final String TAG = this.getClass().getName();
	
	private HeadView headView;
	private EditText etPhoneNum, etCode, etNewPwd, etSurePwd;
	private TextView tvGetCode;
	private Button btnSure;

	private GetCodeTimer c;
	private String code;
	private String phone;
	private String newPassword;
	private String confirmPassword;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_find_pswd);
		initUi();
	}

	private void initUi() {
		
		headView = (HeadView) findViewById(R.id.hv_find_psd_head);
		headView.setHeadViewListener(this);
		etPhoneNum = (EditText) findViewById(R.id.et_find_psd_account);
		etCode = (EditText) findViewById(R.id.et_find_psd_check_code);
		etNewPwd = (EditText) findViewById(R.id.et_find_psd_psd);
		etSurePwd = (EditText) findViewById(R.id.et_find_psd_psd_sure);

		tvGetCode = (TextView) findViewById(R.id.tv_find_psd_get_check_code);
		btnSure = (Button) findViewById(R.id.btn_find_psd_sure);
		tvGetCode.setOnClickListener(this);
		btnSure.setOnClickListener(this);
		c = new GetCodeTimer(1 * 60 * 1000, 1000, tvGetCode);
	}

	private void getCode() {
		phone = etPhoneNum.getText().toString();
		if (StrUtils.isEmpty(phone)) {
			T.showShort(this, "请先输入手机号码！");
			return;
		}
		c.start();
		tvGetCode.setClickable(false);
		new GetTask(UrlHelper.GET_FIND_CODE,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
						T.showShort(AtyFindPswd.this, "验证码已经发送");
					}
				},"phone", phone);
	}

	private boolean verify() {
		phone = etPhoneNum.getText().toString();
		code = etCode.getText().toString();
		newPassword = etNewPwd.getText().toString();
		confirmPassword = etSurePwd.getText().toString();
		if (StrUtils.isEmpty(phone)) {
			T.showShort(this, "手机号码不能为空");
			return false;
		}
		if (StrUtils.isEmpty(code)) {
			T.showShort(this, "验证码不能为空");
			return false;
		}
		if (StrUtils.isEmpty(newPassword)) {
			T.showShort(this, "新密码不能为空哦");
			return false;
		}
		if (StrUtils.isEmpty(confirmPassword)) {
			T.showShort(this, "确认密码不能为空哦");
			return false;
		}
		return true;
	}

	private void doNewPwd() {
		if (!newPassword.equals(confirmPassword)) {
			T.showShort(this, "新密码和确认密码不一致");
			return;
		}

		new PostTask(UrlHelper.FIND_PASSWORD,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtyFindPswd.this, "新密码更改完成");
							saveAccount(phone, newPassword);
							AtyFindPswd.this.finish();
					}
				},"phone", phone,
				"code", code,
				"newPassword", newPassword,
				"confirmPassword", confirmPassword);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_find_psd_get_check_code:
			getCode();
			break;
		case R.id.btn_find_psd_sure:
			if (verify()) {
				doNewPwd();
			}
			break;
		}

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
		onBackPressed();
	}

	@Override
	public void rightListener() {
	}

}
