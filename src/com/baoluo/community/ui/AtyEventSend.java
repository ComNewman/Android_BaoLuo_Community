package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.LocationInfo;
import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.support.task.ImgUpLoadTask;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.customview.DateTimePickDialog;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.TagsView;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.InputFilterHelp;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.TimeUtil;

/**
 * 最新活动创建页面
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyEventSend extends AtyDialogImgPick implements OnClickListener,
		LocationResultListener, HeadViewListener {

	private static final int TAGS_RESULT_CODE = 0x06;
	private static final int IMGS_RESULT_CODE = 0x07;
	private HeadView headView;
	private Dialog dialog;
	private RelativeLayout rlDesc, rlTags;
	private EditText etTitle, etPeopleNum,etLocation;
	private TextView tvDesc, tvStartTime, tvEndTime,tvTag,tvTime;
	private ImageView ivPic,ivLct;
	private TagsView tagsView;
	private LatLng mLocation;
	private Calendar startCalendar, endCalendar;
	private String placeName, startTime, endTime, peopleNum, title, content;
	private List<String> imgUrls;
	private List<TagInfo> tags;
	private List<String> selTags;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_event_send);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.ACTIVITY_SIZE);
		AtyDialogImgPick.setConext(this);
		setLocationResultListener(this);
		initUI();
		initData();
	}

	private void initData() {
		tags = new ArrayList<TagInfo>();
		dialog = MyProgress.getInstance(AtyEventSend.this, "发布中...");
		startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.MINUTE, DateTimePickDialog
				.getSelMin(startCalendar.get(Calendar.MINUTE)));
		String now = TimeUtil.dateToString(startCalendar.getTime(),
				TimeUtil.FORMAT_TIME7);
		endCalendar = startCalendar;
		tvStartTime.setText(now);
		tvEndTime.setText(now);
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_event_send_head);
		headView.setHeadViewListener(this);
		rlDesc = (RelativeLayout) findViewById(R.id.rl_event_send_describe);
		etLocation = (EditText) findViewById(R.id.et_event_send_location);
		etLocation.setFilters(new InputFilter[]{new InputFilterHelp(AtyEventSend.this,Configs.TITLE_LCT_LEN)});
		rlTags = (RelativeLayout) findViewById(R.id.rl_event_send_tags);
		rlDesc.setOnClickListener(this);
		rlTags.setOnClickListener(this);
		ivLct = (ImageView) findViewById(R.id.iv_event_location);
		ivLct.setOnClickListener(this);
		tvTime = (TextView) findViewById(R.id.et_event_send_time);
		tvTag = (TextView) findViewById(R.id.et_event_send_tags);
		tagsView = (TagsView) findViewById(R.id.tags_event_send);
		ivPic = (ImageView) findViewById(R.id.iv_event_pic);
		etTitle = (EditText) findViewById(R.id.et_event_send_title);
		etTitle.setFilters(new InputFilter[]{new InputFilterHelp(AtyEventSend.this,Configs.TITLE_LCT_LEN)});
		etPeopleNum = (EditText) findViewById(R.id.et_event_send_peoplenum);
		tvDesc = (TextView) findViewById(R.id.et_event_send_describe);
		tvStartTime = (TextView) findViewById(R.id.tv_event_send_start_time);
		tvEndTime = (TextView) findViewById(R.id.tv_event_send_end_time);
		tvStartTime.setOnClickListener(this);
		tvEndTime.setOnClickListener(this);
	}

	private boolean verify() {
		placeName = etLocation.getText().toString();
		if (StrUtils.isEmpty(placeName)) {
			T.showShort(this, "活动地点不能为空！");
			dialog.dismiss();
			return false;
		}
		startTime = tvStartTime.getText().toString();
		endTime = tvEndTime.getText().toString();
		String now = TimeUtil.dateToString(new Date(), TimeUtil.FORMAT_TIME7);
		if (TimeUtil.compareTimeStr(startTime, now, TimeUtil.FORMAT_TIME7)) {
			T.showShort(this, "开始时间不能早于当前时间");
			dialog.dismiss();
			return false;
		}
		if (!TimeUtil.compareTimeStr(startTime, endTime, TimeUtil.FORMAT_TIME7)) {
			T.showShort(this, "结束时间不能早于开始时间");
			dialog.dismiss();
			return false;
		}
		peopleNum = etPeopleNum.getText().toString();
		if (!StrUtils.isNumeric(peopleNum)) {
			T.showShort(this, "请输入正确的人数格式");
			dialog.dismiss();
			return false;
		}
		title = etTitle.getText().toString();
		if (StrUtils.isEmpty(title)) {
			T.showShort(this, "标题不能为空");
			dialog.dismiss();
			return false;
		}
		if (StrUtils.isEmpty(content)) {
			T.showShort(this, "活动内容不能为空");
			dialog.dismiss();
			return false;
		}
		 if (tags == null || tags.size() == 0) {
		 T.showShort(this, "标签不能为空");
		 return false;
		 }
		if (imgUrls == null || imgUrls.size() == 0) {
			T.showShort(this, "至少选择上传一张图片");
			dialog.dismiss();
			return false;
		}
		return true;
	}

	private void sendEvent() {
		if (!verify()) {
			return;
		}
		try {
			for (String imgUrl : imgUrls) {
				new ImgUpLoadTask(UrlHelper.FILE_URL, imgUrl,
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onComplete(Object obj) {
								if (imgUrls.size() == SelectImg.getIntance()
										.getUpImgList().size()) {
									sendActivityCtx();
								}
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void sendActivityCtx() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		Date startDate = TimeUtil.stringToDate(year + "/" + startTime,
				TimeUtil.FORMAT_TIME8);
		Date endDate = TimeUtil.stringToDate(year + "/" + endTime,
				TimeUtil.FORMAT_TIME8);

		List<PictureInfo> pics = new ArrayList<PictureInfo>();
		PictureInfo p;
		for (String imgUrl : SelectImg.getIntance().getUpImgList()) {
			p = new PictureInfo();
			p.setUrl(imgUrl);
			pics.add(p);
		}

		EventInfo event = new EventInfo();
		event.setName(title);
		event.setContent(content);
		event.setEventType("2");
		event.setStartTime(startDate);
		event.setEndTime(endDate);
		event.setUpNum(Integer.parseInt(peopleNum));
		event.setTags(tags);
		event.setPictures(pics);
		LocationInfo location = new LocationInfo();
		location.setName(placeName);
		location.setAddress("");
		location.setLat(mLocation.latitude);
		location.setLon(mLocation.longitude);
		event.setLocation(location);

		String paramsObj = GsonUtil.getInstance().obj2Str(event);
		new PostObjTask(UrlHelper.EVENT_POST, paramsObj,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						SelectImg.getIntance().clear();
						T.showShort(AtyEventSend.this, "活动发布成功");
						dialog.dismiss();
						AtyEventSend.this.finish();
					}
				});
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		dialog.show();
		sendEvent();
	}

	@Override
	public void locationChanged() {
		mLocation = getLatLng();

	}

	@Override
	public void locationFail() {
		mLocation = new LatLng(Configs.latitude, Configs.longitude);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == AtySelPlaceDialog.PLACE_CODE) {
				placeName = data.getStringExtra("placeName");
				mLocation = new LatLng(data.getDoubleExtra("latitude",
						Configs.latitude), data.getDoubleExtra("longitude",
						Configs.longitude));
				etLocation.setText(placeName);
			} else if (requestCode == IMGS_RESULT_CODE) {
				content = data.getStringExtra(AtyEventSendDesc.DESC);
				imgUrls = data
						.getStringArrayListExtra(AtyEventSendDesc.IMGURLS);
				ivPic.setImageBitmap(BitmapUtil.AbbreviationsIcon(imgUrls
						.get(0)));
				tvDesc.setText(content);
			} else if (requestCode == TAGS_RESULT_CODE) {
				List<String> tagList = data
						.getStringArrayListExtra(AtyEventTags.EVENT_TAG);
				if (tagList != null && tagList.size() > 0) {
					tvTag.setVisibility(View.GONE);
					tagsView.setVisibility(View.VISIBLE);
					tags.clear();
					tagsView.removeAllViews();
					for (int i = 0; i < tagList.size(); i++) {
						TagInfo tag = new TagInfo(tagList.get(i));
						tags.add(tag);
					}
					tagsView.setEventTags(tags);
				}
			}
		}
	}

	@Override
	public void onClick(View v) {
		Intent i = new Intent();
		switch (v.getId()) {
		case R.id.iv_event_location:
			i.setClass(this, AtySelPlaceDialog.class);
			i.putExtra("latitude", mLocation.latitude);
			i.putExtra("longitude", mLocation.longitude);
			startActivityForResult(i, AtySelPlaceDialog.PLACE_CODE);
			break;
		case R.id.rl_event_send_tags:
			i.setClass(this, AtyEventTags.class);
			if (tags != null && tags.size() > 0) {
				selTags = new ArrayList<String>();
				for (int j = 0; j < tags.size(); j++) {
					selTags.add(tags.get(j).getName());
				}
				i.putStringArrayListExtra(AtyEventTags.SEL_TAG,
						(ArrayList<String>) selTags);
			}
			startActivityForResult(i, TAGS_RESULT_CODE);
			break;
		case R.id.tv_event_send_start_time:
			new DateTimePickDialog(AtyEventSend.this, startCalendar,
					TimeUtil.FORMAT_TIME7, tvStartTime);
			tvTime.setVisibility(View.GONE);
			break;
		case R.id.tv_event_send_end_time:
			new DateTimePickDialog(AtyEventSend.this, endCalendar,
					TimeUtil.FORMAT_TIME7, tvEndTime);
			break;
		case R.id.rl_event_send_describe:
			i.setClass(this, AtyEventSendDesc.class);
			if (content != null && imgUrls != null) {
				i.putExtra(AtyEventSendDesc.DESC, content);
				i.putStringArrayListExtra(AtyEventSendDesc.IMGURLS,
						(ArrayList<String>) imgUrls);
			}
			startActivityForResult(i, IMGS_RESULT_CODE);
			break;
		}

	}
}