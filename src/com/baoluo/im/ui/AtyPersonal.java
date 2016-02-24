package com.baoluo.im.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.ImgUpLoadTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.AtySelCitys;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.clipheadpic.ClipImageLayout;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

/**
 * 个人信息
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyPersonal extends AtyDialogImgPick implements OnClickListener,
		LocationResultListener {
	private static final String TAG = "AtyPersonal";
	private static final int REQUEST_CODE_SEL_CITYS = 0X08;
	private static final int REQUEST_CODE_AMEND_NICK = 0X09;
	private static final int REQUEST_CODE_AMEND_SEX = 0X07;
	private ImageButton ibBack;
	private RelativeLayout rlPersonal, rlHead, rlArea, rlNick, rlSex;
	private RelativeLayout rlSchool;
	private RelativeLayout rlSelHead;
	private LinearLayout llPersonalInfo, llSchoolInfo;
	private RoundImageView rivHead;
	private TextView tvNick, tvSex, tvArea;
	private Button btnClipCancle, btnClipSel;
	private DisplayImageOptions options;
	private boolean isShowPersonal, isShowSchool;
	private UserSelf self;
	private ClipImageLayout mClipImageLayout;
	private String city = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_personal);
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
		mClipImageLayout = (ClipImageLayout) findViewById(R.id.clip_image_Layout);
		rlSelHead = (RelativeLayout) findViewById(R.id.rl_clip_image);
		rlArea = (RelativeLayout) findViewById(R.id.rl_personal_area);
		rlArea.setOnClickListener(this);
		rlNick = (RelativeLayout) findViewById(R.id.rl_personal_nick);
		rlNick.setOnClickListener(this);
		rlSex = (RelativeLayout) findViewById(R.id.rl_personal_sex);
		rlSex.setOnClickListener(this);
		rlHead = (RelativeLayout) findViewById(R.id.rl_personal_head_pic);
		rlHead.setOnClickListener(this);
		rivHead = (RoundImageView) findViewById(R.id.riv_personal_head);
		tvNick = (TextView) findViewById(R.id.tv_personal_nick_name);
		tvSex = (TextView) findViewById(R.id.tv_personal_sex_name);
		tvArea = (TextView) findViewById(R.id.tv_personal_area_name);

		ibBack = (ImageButton) findViewById(R.id.ib_personal_back);
		ibBack.setOnClickListener(this);
		rlPersonal = (RelativeLayout) findViewById(R.id.rl_personal_personnal);
		rlPersonal.setOnClickListener(this);
		rlSchool = (RelativeLayout) findViewById(R.id.rl_personal_school);
		rlSchool.setOnClickListener(this);
		llPersonalInfo = (LinearLayout) findViewById(R.id.rl_personal_more);
		llSchoolInfo = (LinearLayout) findViewById(R.id.ll_school_more);

		btnClipCancle = (Button) findViewById(R.id.btn_clip_image_cancle);
		btnClipSel = (Button) findViewById(R.id.btn_clip_image_sure);
		btnClipCancle.setOnClickListener(this);
		btnClipSel.setOnClickListener(this);
	}

	private void initData() {
		options = new DisplayImageOptions.Builder()//
				.showImageOnLoading(R.drawable.bg_mine_blur_test) // 加载中显示的默认图片
				.showImageOnFail(R.drawable.bg_mine_blur_test) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();
		new GetTask(UrlHelper.CURRENT_USER_INFO, null,
				new UpdateViewHelper.UpdateViewListener(UserSelf.class) {
					@Override
					public void onComplete(Object data) {
							self = (UserSelf) GsonUtil.getInstance()
									.str2Obj(data.toString(), UserSelf.class);
								GlobalContext.getInstance().imageLoader.displayImage(
										self.getFace(), rivHead, options, null);
								tvNick.setText(self.getNickName());
								switch (self.getSex()) {
								case 0:
									tvSex.setText("女");
									break;
								case 1:
									tvSex.setText("男");
									break;
								default:
									T.showShort(AtyPersonal.this, "不男不女？？？");
									break;
								}
					}
				});
		

	}

	private void setPreImg() {
		if (!SelectImg.getIntance().isSelEmpty()) {
			mClipImageLayout.setImageBitmap(BitmapUtil
					.AbbreviationsIcon(SelectImg.getIntance()
							.getSelectImgList().get(0)));
		} else {
			mClipImageLayout.setImageBitmap(null);
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
				city = data.getStringExtra("city");
				tvArea.setText(city);
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

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.rl_personal_personnal:
			isShowPersonal = !isShowPersonal;
			if (isShowPersonal) {
				llSchoolInfo.setVisibility(View.GONE);
				isShowSchool = false;
				llPersonalInfo.setVisibility(View.VISIBLE);
			} else {
				llPersonalInfo.setVisibility(View.GONE);
			}
			break;
		case R.id.rl_personal_school:
			isShowSchool = !isShowSchool;
			if (isShowSchool) {
				llPersonalInfo.setVisibility(View.GONE);
				isShowPersonal = false;
				llSchoolInfo.setVisibility(View.VISIBLE);
			} else {
				llSchoolInfo.setVisibility(View.GONE);
			}
			break;
		case R.id.ib_personal_back:
			onBackPressed();
			break;
		case R.id.rl_personal_head_pic:
			showImgPickDialog();
			break;
		case R.id.btn_clip_image_cancle:
			rlSelHead.setVisibility(View.GONE);
			break;
		case R.id.btn_clip_image_sure:
			saveAndUploadHead();
			break;
		case R.id.rl_personal_area:
			i.setClass(AtyPersonal.this, AtySelCitys.class);
			startActivityForResult(i, REQUEST_CODE_SEL_CITYS);
			break;
		case R.id.rl_personal_nick:
			i.setClass(AtyPersonal.this, AtyAmendGroup.class);
			i.putExtra("inputText", self.getNickName());
			i.putExtra("flag", 2);
			startActivityForResult(i, REQUEST_CODE_AMEND_NICK);
			break;
		case R.id.rl_personal_sex:
			i.setClass(AtyPersonal.this, AtyAmendGroup.class);
			i.putExtra("flag", 3);
			L.i(TAG, "check sex:" + self.getSex());
			i.putExtra("sex", self.getSex());
			startActivityForResult(i, REQUEST_CODE_AMEND_SEX);
			break;
		default:
			break;
		}
	}

	private void saveAndUploadHead() {
		Bitmap bitmap = mClipImageLayout.clip();
		String headPath = saveMyHeadBitmap(bitmap);
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
		Map<String, String> params = new HashMap<String, String>();
		params.put("Face", url);
		new PostTask(UrlHelper.USERINFO_AMEND, params,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtyPersonal.this, "更换头像完成");
					}
				});
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
}
