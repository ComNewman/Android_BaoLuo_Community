package com.baoluo.community.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.DormInfo;
import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.support.task.ImgUpLoadTask;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 创建宿舍
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyDormCreate extends AtyDialogImgPick implements OnClickListener,
		LocationResultListener {

	private ImageView imgPic;
	private EditText etDesc;
	private ImageButton btnBack, btnSend;
	
	private String content;
	private int publishType = 6;
	private LatLng mLocation;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_dorm_send);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.HUMOR_SIZE);
		AtyDialogImgPick.setConext(this);
		setLocationResultListener(this);
		initView();
	}

	private void initView() {

		etDesc = (EditText) findViewById(R.id.et_dorm_create_desc);
		imgPic = (ImageView) findViewById(R.id.iv_dorm_img);
		btnBack = (ImageButton) findViewById(R.id.btn_dorm_back);
		btnSend = (ImageButton) findViewById(R.id.btn_dorm_send);

		imgPic.setOnClickListener(this);
		btnBack.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		setEditTextEnter(etDesc, this);
		//TOD
		etDesc.addTextChangedListener(new TextWatcher() {
			
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PHOTO_CODE) {
				setPreImg();
			} else if (requestCode == CAMERA_CODE) {
				if (photoFile != null && photoFile.exists()) {
					SelectImg.getIntance().getSelectImgList()
							.add(photoFile.getPath());
					setPreImg();
				}
			} else if (requestCode == AtyDormSelType.TYPE) {
				publishType = data.getIntExtra("type", 2);
				sendDorm();
			}
		}
	}

	private void setPreImg() {
		if (!SelectImg.getIntance().isSelEmpty()) {
			imgPic.setImageBitmap(BitmapUtil.AbbreviationsIcon(SelectImg
					.getIntance().getSelectImgList().get(0)));
		} else {
			imgPic.setImageBitmap(null);
		}
	}

	private boolean verify() {
		content = etDesc.getText().toString();
		if (StrUtils.isEmpty(content)) {
			T.showShort(this, "内容不能为空");
			return false;
		}
		if (SelectImg.getIntance().isSelEmpty()) {
			T.showShort(this, "请选择上传图片");
			return false;
		}
		return true;
	}

	private void sendDorm() {
		if (!verify()) {
			return;
		}
		try {
			if (!SelectImg.getIntance().isSelEmpty()) {
				new ImgUpLoadTask(UrlHelper.FILE_URL, SelectImg.getIntance()
						.getSelectImgList().get(0),
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onComplete(Object obj) {
								sendDormCtx();
								AtyDormCreate.this.finish();
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
			L.e("TAG", "upimg err !");
		}
	}

	private void sendDormCtx() {
		PictureInfo pic = new PictureInfo();
		if (SelectImg.getIntance().getUpImgList().size() > 0) {
			pic.setName("");
			pic.setUrl(SelectImg.getIntance().getUpImgList().get(0));
		}
		DormInfo dorm = new DormInfo();
		dorm.setDesc(content);
		dorm.setDormType(publishType);
		dorm.setPictures(pic);
		
		JSONObject obj = null;
		try {
			obj = new JSONObject(GsonUtil.getInstance().obj2Str(dorm));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new PostObjTask(UrlHelper.DORM,obj.toString(),new UpdateViewHelper.UpdateViewListener(){
			@Override
			public void onComplete(Object obj) {
				SelectImg.getIntance().clear();
				SelectImg.getIntance().setChooseAble(
						SelectImg.DEFAULT_SIZE);
			}
		});
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_dorm_back:
			onBackPressed();
			break;
		case R.id.btn_dorm_send:
			startActivityForResult(
					new Intent(this, AtyDormSelType.class),
					AtyDormSelType.TYPE);
			break;
		case R.id.iv_dorm_img:
			showImgPickDialog();
			break;
		default:
			break;
		}
	}

	@Override
	public void locationChanged() {
		mLocation = getLatLng();
	}

	@Override
	public void locationFail() {
		mLocation = null;
	}

}
