package com.baoluo.community.ui.aty.user;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TokenInfo;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.NetUtil;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.util.GetCodeTimer;
/**
 * 注册
 * @author xiangyang.fu
 *
 */
public class AtyRegister extends AtyBase implements OnClickListener,HeadViewListener {
	
	private final String TAG = this.getClass().getName();
	
	private HeadView headView;
	private EditText etPhone, etCode, etPwd;
	private Button  btnReg;
	private TextView tvGetCode;
	private TextView clause;
	private CheckBox cbClause;
	private GetCodeTimer c;
	
	private boolean isAggree = true;
	private String phoneNum;
	private String code;
	private String pwd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_register);
		initUI();
		initData();
	}

	private void initData() {
		clause = (TextView) findViewById(R.id.tv_register_privacy_clause);
	        String url_0_text = "隐私条款";
	        clause.setText("点击注册表示同意");

	        SpannableString spStr = new SpannableString(url_0_text);

	        spStr.setSpan(new ClickableSpan() {
	            @Override
	            public void updateDrawState(TextPaint ds) {
	                super.updateDrawState(ds);
	                ds.setColor(Color.parseColor("#999999"));
	                ds.setUnderlineText(true);      
	            }

	            @Override
	            public void onClick(View widget) {
	            	startActivity(new Intent(AtyRegister.this,AtyPrivacyTreaty.class));
	            }
	        }, 0, url_0_text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

	        clause.setHighlightColor(Color.TRANSPARENT); 
	        clause.append(spStr);
	        clause.setMovementMethod(LinkMovementMethod.getInstance());
		
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_register_head);
		headView.setHeadViewListener(this);
		tvGetCode = (TextView) findViewById(R.id.tv_get_check_code);
		btnReg = (Button) findViewById(R.id.btn_register);
		etPhone = (EditText) findViewById(R.id.et_register_account);
		etCode = (EditText) findViewById(R.id.et_reg_check_code);
		etPwd = (EditText) findViewById(R.id.et_register_psd);
		cbClause = (CheckBox) findViewById(R.id.cb_register_privacy_clause);
		cbClause.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){ 
            @Override
            public void onCheckedChanged(CompoundButton buttonView, 
                    boolean isChecked) { 
                if(isChecked){ 
                	isAggree = true;
                }else{
                	isAggree = false;
                }
            } 
        }); 
		tvGetCode.setOnClickListener(this);
		btnReg.setOnClickListener(this);
		
		c = new GetCodeTimer(1 * 59 * 1000, 1000, tvGetCode);
	}
	
	private boolean verify() {
		phoneNum = etPhone.getText().toString();
		if (StrUtils.isEmpty(phoneNum)) {
			T.showShort(this, "手机号码不能为空!");
			return false;
		}
		code = etCode.getText().toString();
		pwd = etPwd.getText().toString();
		if(StrUtils.isEmpty(code)){
			T.showShort(this,"验证码不能为空!");
			return false;
		}
		if (StrUtils.isEmpty(pwd)) {
			T.showShort(this, "密码不能为空哦!");
			return false;
		}
		if (pwd.length()<6) {
			T.showShort(this, "密码最少6位！");
			return false;
		}
		return true;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_get_check_code:
			if (!StrUtils.isEmpty(etPhone.getText().toString())) {
				getRegCode();
			} else {
				T.showShort(this, "请先输入手机号码！");
			}
			break;
		case R.id.btn_register:
			if (NetUtil.isConnnected(AtyRegister.this)) {
				if (verify() && isAggree) {
					doReg();
				}
			}
			break;
		}
	}

	private void getRegCode() {
		phoneNum = etPhone.getText().toString();
	/*	if (StrUtils.isEmpty(phoneNum) || !StrUtils.isPhoneNum(phoneNum)) {
			T.showShort(this, "请输入正确的手机号");
			return;
		}*/
		c.start();
		tvGetCode.setClickable(false);
		new GetTask(UrlHelper.GET_REG_CODE,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						T.showShort(AtyRegister.this, "验证码已经发送");
					}
				},"phone", phoneNum);
	}

	private void doReg() {
		new PostTask(false, UrlHelper.REGISTER,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onCompleteExecute(String responseStr) {
						if (!responseStr.equals(Configs.RESPONSE_ERROR)) {
							T.showShort(AtyRegister.this, "账号注册成功");
							Intent i = new Intent();
							i.setClass(AtyRegister.this, AtySetNick.class);
							startActivity(i);
							doLogin();
						} else {
							String result = ResultParse.getInstance().getResStr(responseStr);
							T.showShort(AtyRegister.this, result);
						}
					}
				}, "Phone", phoneNum, "Password", pwd, "Code", code);
	}

	private void doLogin() {
		new PostTask(false,UrlHelper.LOGIN,new UpdateViewHelper.UpdateViewListener(TokenInfo.class){
			@Override
			public void onComplete(Object obj) {
					TokenInfo token = (TokenInfo) obj;
					SettingUtility.setTokenInfo(token);
					saveAccount(phoneNum, pwd);
					Intent i = new Intent();
					i.setClass(AtyRegister.this, AtySetNick.class);
					startActivity(i);
					AtyRegister.this.finish();

			}

			@Override
			public void onFail() {
				T.showShort(AtyRegister.this, "登录失败！账号密码不对");
			}
		},"grant_type", "password","username", phoneNum,"password", pwd);
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
		startActivity(new Intent(AtyRegister.this,AtyLogin.class));
		onBackPressed();
		
	}

	@Override
	public void rightListener() {
	}
}
