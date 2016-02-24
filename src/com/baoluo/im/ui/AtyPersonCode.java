package com.baoluo.im.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.zxing.decoding.EncodeQR;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.StrUtils;
import com.google.zxing.WriterException;

/**
 * 个人二维码
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyPersonCode extends AtyBase implements OnClickListener ,HeadViewListener{

	private ImageView ivHead, ivSex, ivCode;
	private TextView tvName, tvArea;
	private Button btnSave;
	private LinearLayout llCodeView;
	private HeadView headView;
	
	private Bitmap bmCode;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_code_person);
		initUI();
		initData();
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_person_code);
		headView.setHeadViewListener(this);
		llCodeView = (LinearLayout) findViewById(R.id.ll_person_code_view);
		ivHead = (ImageView) findViewById(R.id.iv_person_code_head);
		ivSex = (ImageView) findViewById(R.id.iv_person_code_sex);
		ivCode = (ImageView) findViewById(R.id.iv_person_code);
		tvName = (TextView) findViewById(R.id.tv_person_code_name);
		tvArea = (TextView) findViewById(R.id.tv_person_code_area);
		btnSave = (Button) findViewById(R.id.btn_save_person_code);
		btnSave.setOnClickListener(this);
	}

	private void initData() {
		UserSelf self = SettingUtility.getUserSelf();
		if (self != null) {
			if(!StrUtils.isEmpty(self.getQRCode())){
				try {
					bmCode = EncodeQR.getInstance().encode(self.getQRCode());
				} catch (WriterException e) {
					e.printStackTrace();
				}
			}
			if (bmCode != null) {
				ivCode.setImageBitmap(bmCode);
			}
			if (self.getSex() == 1) {
				ivSex.setImageResource(R.drawable.ic_person_code_nan);
			}
			tvName.setText(self.getNickName());
			tvArea.setText(self.getLocationArea().getProvince() + "  "
					+ self.getLocationArea().getCity());
			if(!StrUtils.isEmpty(self.getFace())){
				GlobalContext.getInstance().imageLoader.displayImage(self.getFace(), ivHead, GlobalContext.options, null);
			}else{
				ivHead.setImageResource(R.drawable.head_default);
			}
			
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_save_person_code:
			btnSave.setVisibility(View.GONE);
			Bitmap bmp = EncodeQR.getInstance().convertViewToBitmap(llCodeView);
			EncodeQR.getInstance().saveImageToGallery(this, bmp);
			btnSave.setVisibility(View.VISIBLE);
			break;
		}

	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		
	}
}
