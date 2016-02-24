package com.baoluo.im.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.LocationArea;
import com.baoluo.community.entity.ModifyInfoModel;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.ImgUpLoadTask;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.AtySelCitys;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.clipheadpic.ClipImageLayout;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

public class AtyPerson extends AtyDialogImgPick implements OnClickListener,
		LocationResultListener, HeadViewListener {

	private static final String TAG = "AtyPerson";
	private static final int REQUEST_CODE_SEL_CITYS = 0X08;
	private static final int REQUEST_CODE_AMEND_NICK = 0X09;
	private static final int REQUEST_CODE_AMEND_SEX = 0X07;
	private HeadView headView;
	private RelativeLayout rlArea, rlHead, rlNick, rlSex, rlSelHead,rlCode;
	private RoundImageView rivHead;
	private TextView tvNick, tvSex, tvArea;
	private Button btnClipCancle, btnClipSel;
	private DisplayImageOptions options;
	private UserSelf self;
	private ClipImageLayout mClipImageLayout;
	private String city = "";
	private String province = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_person);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.HUMOR_SIZE);
		AtyDialogImgPick.setConext(this);
		setLocationResultListener(this);
		initUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initUI() {
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.clip_image_Layout_person);
		rlSelHead = (RelativeLayout) findViewById(R.id.rl_clip_image);
		rlArea = (RelativeLayout) findViewById(R.id.rl_person_area);
		rlArea.setOnClickListener(this);
		rlCode = (RelativeLayout) findViewById(R.id.rl_person_qr_code);
		rlCode.setOnClickListener(this);
		rlNick = (RelativeLayout) findViewById(R.id.rl_person_nick);
		rlNick.setOnClickListener(this);
		rlSex = (RelativeLayout) findViewById(R.id.rl_person_sex);
		rlSex.setOnClickListener(this);
		rlHead = (RelativeLayout) findViewById(R.id.rl_person_head_pic);
		rlHead.setOnClickListener(this);
		rivHead = (RoundImageView) findViewById(R.id.riv_person_head);
		tvNick = (TextView) findViewById(R.id.tv_person_nick_name);
		tvSex = (TextView) findViewById(R.id.tv_person_sex_name);
		tvArea = (TextView) findViewById(R.id.tv_person_area_name);
		headView = (HeadView) findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		btnClipCancle = (Button) findViewById(R.id.btn_clip_image_cancle);
		btnClipSel = (Button) findViewById(R.id.btn_clip_image_sure);
		btnClipCancle.setOnClickListener(this);
		btnClipSel.setOnClickListener(this);
	}

	private void initData() {
		options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.head_default) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.bg_mine_blur_test) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();
		new GetTask(UrlHelper.CURRENT_USER_INFO, null,
				new UpdateViewHelper.UpdateViewListener(UserSelf.class) {
					@Override
					public void onComplete(Object data) {
							self = (UserSelf)data;
								tvArea.setText(self.getLocationArea()
										.getProvince()
										+ "-"
										+ self.getLocationArea().getCity());
								if (!StrUtils.isEmpty(self.getFace())) {
									GlobalContext.getInstance().imageLoader
											.displayImage(self.getFace(),
													rivHead, options, null);
								} else {
									rivHead.setImageResource(R.drawable.head_default);
								}
								tvNick.setText(self.getNickName());
								switch (self.getSex()) {
								case 0:
									tvSex.setText("女");
									break;
								case 1:
									tvSex.setText("男");
									break;
								}
					}
				});
	}

	private void setPreImg() {
		L.i(TAG, "mClipImageLayout   set imagebitmap");
		if (!SelectImg.getIntance().isSelEmpty()) {
			mClipImageLayout.setImageBitmap(BitmapUtil
					.AbbreviationsIcon(SelectImg.getIntance()
							.getSelectImgList().get(0)));
		} else {
			mClipImageLayout.setImageBitmap(null);
			L.i(TAG, "mClipImageLayout   set imagebitmap is null");
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PHOTO_CODE) {
				rlSelHead.setVisibility(View.VISIBLE);
				setPreImg();
			} else if (requestCode == CAMERA_CODE) {
				if (photoFile != null && photoFile.exists()) {
					SelectImg.getIntance().getSelectImgList()
							.add(photoFile.getPath());
					rlSelHead.setVisibility(View.VISIBLE);
					setPreImg();
				}
			} else if (requestCode == REQUEST_CODE_SEL_CITYS) {
				updateLocationArea(data);
			} else if (requestCode == REQUEST_CODE_AMEND_NICK) {
				String nick = data.getStringExtra("nickName");
				L.i(TAG, nick);
				tvNick.setText(nick);
			} else if (requestCode == REQUEST_CODE_AMEND_SEX) {
				int sexFlag = data.getIntExtra("sex", -1);
				L.i(TAG, "sex:" + sexFlag);
				switch (sexFlag) {
				case 0:
					tvSex.setText("女");
					break;
				case 1:
					tvSex.setText("男");
					break;
				default:
					L.i(TAG, "不能不男不女");
					break;
				}
			}
		}
	}

	private void updateLocationArea(Intent data) {
		province = data.getStringExtra("province");
		city = data.getStringExtra("city");
		LocationArea area = new LocationArea(province, city);
		ModifyInfoModel modifyInfo = new ModifyInfoModel(area);

		JSONObject obj = null;
		try {
			obj = new JSONObject(GsonUtil.getInstance().obj2Str(modifyInfo));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new PostObjTask(UrlHelper.USERINFO_AMEND, obj.toString(),
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						tvArea.setText(province + "—" + city);
						T.showShort(AtyPerson.this, "更改地区完成");
					}
				});
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.rl_person_head_pic:
			showImgPickDialog();
			break;
		case R.id.btn_clip_image_cancle:
			rlSelHead.setVisibility(View.GONE);
			break;
		case R.id.btn_clip_image_sure:
			saveAndUploadHead();
			break;
		case R.id.rl_person_area:
			i.setClass(AtyPerson.this, AtySelCitys.class);
			startActivityForResult(i, REQUEST_CODE_SEL_CITYS);
			break;
		case R.id.rl_person_nick:
			i.setClass(AtyPerson.this, AtyAmendGroup.class);
			i.putExtra("inputText", self.getNickName());
			i.putExtra("flag", 2);
			startActivityForResult(i, REQUEST_CODE_AMEND_NICK);
			break;
		case R.id.rl_person_qr_code:
			i.setClass(AtyPerson.this, AtyPersonCode.class);
			startActivity(i);
			break;
		case R.id.rl_person_sex:
			i.setClass(AtyPerson.this, AtyAmendGroup.class);
			i.putExtra("flag", 3);
			i.putExtra("sex", self.getSex());
			startActivityForResult(i, REQUEST_CODE_AMEND_SEX);
			break;
		}
	}

	private void saveAndUploadHead() {
		Bitmap bitmap = mClipImageLayout.clip();
		String headPath = saveMyHeadBitmap(bitmap);
		L.i(TAG, headPath);
		new ImgUpLoadTask(UrlHelper.FILE_URL, headPath,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
						L.i(TAG, (String) data);
							String url = StrUtils
									.parseImgBackUrl((String) data);
							amendUserFace(url);
					}
				});
		SelectImg.getIntance().clear();
		rlSelHead.setVisibility(View.GONE);
		rivHead.setImageBitmap(bitmap);
	}

	private void amendUserFace(String url) {
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
						L.i(TAG, (String) data);
						if (!data.equals(Configs.RESPONSE_ERROR)) {
							T.showShort(AtyPerson.this, "更换头像完成");
						}
					}
				},"Face", url);
	}

	public String getPicPath() {
		String path = null;
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().toString();// 获得根目录

		}
		return path + "/" + "baoluo" + "/" + "pic";
	}

	public String saveMyHeadBitmap(Bitmap bitmap) {
		try {
			File extStorage = new File(getPicPath() + "/head");
			if (!extStorage.exists()) {
				extStorage.mkdirs();
			}
			File file = new File(extStorage, System.currentTimeMillis()
					+ ".png");
			FileOutputStream fOut = new FileOutputStream(file);
			bitmap.compress(Bitmap.CompressFormat.PNG, 90, fOut);// 压缩图片
			fOut.flush();
			fOut.close();
			L.i(TAG, "filePath: " + file.getAbsolutePath());
			return file.getAbsolutePath();
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	@Override
	public void locationChanged() {

	}

	@Override
	public void locationFail() {

	}

	@Override
	public void leftListener() {
		onBackPressed();

	}

	@Override
	public void rightListener() {

	}
}
