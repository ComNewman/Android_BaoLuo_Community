package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.core.Configs;
import com.baoluo.community.ui.adapter.SendPagerAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.InputFilterHelp;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.util.DepthPageTransformer;

/**
 * 活动的描述和图片
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyEventSendDesc extends AtyDialogImgPick implements
		OnClickListener, HeadViewListener, OnPageChangeListener {

	public static final String DESC = "desc";
	public static final String IMGURLS = "imgurls";
	private ViewPager vp;
	private RelativeLayout rlPic;
	private EditText etDesc;
	private TextView tvPagerFlg;
	private HeadView headView;
	private List<String> imgUrls;

	private String desc;
	private List<String> selImgs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_event_desc);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.ACTIVITY_SIZE);
		AtyDialogImgPick.setConext(this);
		desc = getIntent().getStringExtra(DESC);
		selImgs = getIntent().getStringArrayListExtra(IMGURLS);
		initUI();
		initData();
	}

	private void initData() {
		if (desc != null && selImgs != null) {
			etDesc.setText(desc);
			setPreImg(selImgs);
		}
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_event_send_desc_head);
		headView.setHeadViewListener(this);
		vp = (ViewPager) findViewById(R.id.vp__event_send_desc);
		vp.setPageTransformer(true, new DepthPageTransformer());
		rlPic = (RelativeLayout) findViewById(R.id.rl__event_send_desc_pic);
		rlPic.setOnClickListener(this);
		etDesc = (EditText) findViewById(R.id.et_desc);
		etDesc.setFilters(new InputFilter[]{new InputFilterHelp(AtyEventSendDesc.this,Configs.EVENT_CONTENT_LEN)});
		tvPagerFlg = (TextView) findViewById(R.id.tv_page_flag);
		setEditTextEnter(etDesc, AtyEventSendDesc.this);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == PHOTO_CODE) {
				if (!SelectImg.getIntance().isSelEmpty()) {
					imgUrls = SelectImg.getIntance().getSelectImgList();
					setPreImg(imgUrls);
				} else {
					tvPagerFlg.setVisibility(View.GONE);
				}
			} else if (requestCode == CAMERA_CODE) {
				if (photoFile != null && photoFile.exists()) {
					SelectImg.getIntance().getSelectImgList()
							.add(photoFile.getPath());
					if (!SelectImg.getIntance().isSelEmpty()) {
						imgUrls = SelectImg.getIntance().getSelectImgList();
						setPreImg(imgUrls);
					} else {
						tvPagerFlg.setVisibility(View.GONE);
					}
				}
			}
		}
	}

	private void setPreImg(List<String> urls) {
		LayoutInflater mInflater = getLayoutInflater();
		List<View> listViews = new ArrayList<View>();
		for (int i = 0; i < urls.size(); i++) {
			View v = mInflater.inflate(R.layout.activity_more_imgs, null);
			ImageView iv = (ImageView) v.findViewById(R.id.iv_img);
			iv.setTag(i);
			iv.setImageBitmap(BitmapUtil.AbbreviationsIcon(urls.get(i)));
			iv.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					showImgPickDialog();
				}
			});
			listViews.add(v);
		}
		vp.setAdapter(new SendPagerAdapter(listViews));
		vp.setCurrentItem(0);
		vp.setOnPageChangeListener(this);
		tvPagerFlg.setVisibility(View.VISIBLE);
		tvPagerFlg.setText("1/" + urls.size());
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void setBack() {
		if (desc != null && selImgs != null) {

		}
		String desc = etDesc.getText().toString();
		if (StrUtils.isEmpty(desc)) {
			T.showShort(AtyEventSendDesc.this, "活动描述不能为空");
			return;
		}
		if (SelectImg.getIntance().isSelEmpty()) {
			if (selImgs != null) {
				Intent i = new Intent();
				i.putExtra(DESC, desc);
				i.putStringArrayListExtra(IMGURLS, new ArrayList(selImgs));
				setResult(RESULT_OK, i);
				finish();
			}else{
				T.showShort(AtyEventSendDesc.this, "请至少选择一张图片！");
			}
			return;
		}
		Intent i = new Intent();
		i.putExtra(DESC, desc);
		i.putStringArrayListExtra(IMGURLS, new ArrayList(imgUrls));
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl__event_send_desc_pic:
			showImgPickDialog();
			break;
		}
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		setBack();
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		tvPagerFlg.setText((arg0 + 1) + "/" + imgUrls.size());
	}
}
