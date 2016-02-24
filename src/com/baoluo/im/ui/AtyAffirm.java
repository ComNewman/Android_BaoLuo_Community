package com.baoluo.im.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.im.jsonParse.ResultParse;

/**
 * 删除好友操作确认 页
 * @author tao.lai
 *
 */
public class AtyAffirm extends AtyBase implements OnClickListener{
	
	private static final String TAG = "AtyAffirm";
	
	public static final int OPTION_CODE = 0x7;
	public static final String OPTION_TYPE = "optionType";
	public static final String FRI_UID = "friUid";
	public static final int SURE = 0x1;
	public static final int CANCEL = 0x2;
	
	private Button btnCancel,btnSure;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_affirm);
		
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnSure = (Button) findViewById(R.id.btn_sure);
		
		btnCancel.setOnClickListener(this);
		btnSure.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_sure:
			doDeleter(SURE);
			break;
			default:break;
		}
	}
	
	private void doDeleter(int type) {
		L.i(TAG, "  doDeleter  ");
		final String friUid = getIntent().getStringExtra(FRI_UID);
		if (StrUtils.isEmpty(friUid)) {
			T.showShort(this, "好友删除失败！");
			return;
		}
		finish();
		new PostTask(UrlHelper.DEL_FRI,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (ResultParse.getInstance().getResCode(obj.toString()) == ResultParse.RESPONSE_OK) {
							T.showShort(AtyAffirm.this, "成功删除");
							ContactsHelper.getInstance().removeByUid(friUid);
						}
					}
				}, "id", friUid);
	}
}
