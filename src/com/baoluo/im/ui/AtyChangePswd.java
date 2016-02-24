package com.baoluo.im.ui;

import android.os.Bundle;
import android.widget.EditText;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.setting.SettingHelper;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.EventDetailsActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.jsonParse.ResultParse;

/**
 * 修改密码
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyChangePswd extends AtyBase implements HeadViewListener {

	private HeadView headView;
	private EditText etOld, etNew;
	private String oldPswd, newPswd;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		setContentView(R.layout.aty_change_pswd);
		initUI();
		super.onCreate(savedInstanceState);
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_change_pswd);
		headView.setHeadViewListener(this);
		etOld = (EditText) findViewById(R.id.et_change_pswd_old);
		etNew = (EditText) findViewById(R.id.et_change_pswd_new);

	}

	private boolean verify() {
		oldPswd = etOld.getText().toString();
		newPswd = etNew.getText().toString();
		if (StrUtils.isEmpty(oldPswd) || StrUtils.isEmpty(newPswd)) {
			T.showShort(this, "旧密码或者新密码不能为空！");
			return false;
		}
		if (oldPswd.length() < 6 || newPswd.length() < 6) {
			T.showShort(this, "旧密码或者新密码至少6位！");
			return false;
		}
		if (oldPswd.equals(newPswd)) {
			T.showShort(this, "旧密码与新密码不能一致！");
			return false;
		}
		return true;
	}

	@Override
	public void leftListener() {
		onBackPressed();

	}

	@Override
	public void rightListener() {
		if (verify()) {
			new PostTask(UrlHelper.CHANGE_PSWD,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							int code = ResultParse.getInstance().getResCode((String) obj);
							String result = ResultParse.getInstance().getResStr((String) obj);
							T.showShort(AtyChangePswd.this, result);
							if(code == 200){
								saveAccount(SettingUtility.getAccount(), newPswd);
								AtyChangePswd.this.finish();
							}
							super.onComplete(obj);
						}
					}, "OldPassword", oldPswd, "NewPassword", newPswd);
		}
	}

}
