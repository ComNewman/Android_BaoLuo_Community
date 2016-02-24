package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.LocationInfo;
import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.ImgUpLoadTask;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.adapter.SendPagerAdapter;
import com.baoluo.community.ui.customview.AddTagsView;
import com.baoluo.community.ui.customview.AddTagsView.AddTagsListener;
import com.baoluo.community.ui.customview.DateTimePickDialog;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.ui.listener.EditTextWatcher;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.NumRegion;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.TimeUtil;

/**
 * 活动发布
 * 
 * @author tao.lai
 * 
 */
public class AtyActivitySend extends AtyDialogImgPick implements
		OnClickListener, LocationResultListener, HeadViewListener {
	private static final String TAG = "AtyActivitySend";
	private static final int TAGS_RESULT_CODE = 0x06;
	private ViewPager mPager;
	private HeadView headView;
	private Button btnPager1, btnPager2, btnPager3, btnGreTag, btnBluTag,
			btnPaTag, btnTagAdd;
	private RelativeLayout rlLocation;
	private LinearLayout llPagers;
	private Dialog  dialog;
	private TextView tvLocation, tvStartTime, tvEndTime, tvNick;
	private EditText etPeopleNum, etTitle, etContent;
	private ImageView ivCover, ivImg2, ivImg3, ivImg4, ivImg5;
	private AddTagsView addTag;
	private RoundImageView head;
	EditTextWatcher titleWatcher, contentWatcher;
	// atyseltag 返回过来的标签文字 转为标签
	private List<TagInfo> tagList;
	// 当前页面选中了的标签文字
	private List<String> tags;
	// atyseltag 返回过来的标签文字
	private List<String> resultTags;
	private List<View> listViews;
	private int pagerIndex = 0;
	private LatLng mLocation;
	private int level = 0;
	private String placeName, startTime, endTime, peopleNum, title, content;
	private Calendar startCalendar,endCalendar;


	private boolean verify() {
		placeName = tvLocation.getText().toString();
		if (StrUtils.isEmpty(placeName)) {
			T.showShort(this, "活动地点不能为空！");
			return false;
		}
		startTime = tvStartTime.getText().toString();
		endTime = tvEndTime.getText().toString();
		String now = TimeUtil.dateToString(new Date(), TimeUtil.FORMAT_TIME7);
		if (TimeUtil.compareTimeStr(startTime, now, TimeUtil.FORMAT_TIME7)) {
			T.showShort(this, "开始时间不能早于当前时间");
			return false;
		}
		if (!TimeUtil.compareTimeStr(startTime, endTime, TimeUtil.FORMAT_TIME7)) {
			T.showShort(this, "结束时间不能早于开始时间");
			return false;
		}
		peopleNum = etPeopleNum.getText().toString();
		if (!StrUtils.isNumeric(peopleNum)) {
			T.showShort(this, "请输入正确的人数格式");
			return false;
		}
		title = etTitle.getText().toString();
		if (StrUtils.isEmpty(title)) {
			T.showShort(this, "标题不能为空");
			return false;
		}
		content = etContent.getText().toString();
		if (StrUtils.isEmpty(content)) {
			T.showShort(this, "活动内容不能为空");
			return false;
		}
		if (tags == null || tags.size() == 0) {
			T.showShort(this, "标签不能为空");
			return false;
		}
		if (SelectImg.getIntance().isSelEmpty()) {
			T.showShort(this, "至少选择上传一张图片");
			return false;
		}
		return true;
	}

	private void sendActivity() {
		if (!verify()) {
			return;
		}
		try {
			for (String imgUrl : SelectImg.getIntance().getSelectImgList()) {
				new ImgUpLoadTask(UrlHelper.FILE_URL, imgUrl,
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onComplete(Object obj) {
								if (SelectImg.getIntance().getSelectImgList()
										.size() == SelectImg.getIntance()
										.getUpImgList().size()) {
									sendActivityCtx();
								}
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "图片上传异常");
		}
	}

	private void sendActivityCtx() {
		Calendar ca = Calendar.getInstance();
		int year = ca.get(Calendar.YEAR);
		L.i(TAG, "startDate" + year + "/" + startTime);
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
		List<TagInfo> tagList = new ArrayList<TagInfo>();
		for (String tag : tags) {
			TagInfo t = new TagInfo();
			t.setLevel(0);
			t.setName(tag);
			tagList.add(t);
		}
		level = getLevel();
		if(level!=0){
			TagInfo levTag = new TagInfo();
			levTag.setLevel(level);
			levTag.setName("悬赏任务");
			tagList.add(levTag);
		}

		EventInfo event = new EventInfo();
		event.setName(title);
		event.setContent(content);
		event.setEventType("2");
		event.setStartTime(startDate);
		event.setEndTime(endDate);
		event.setUpNum(Integer.parseInt(peopleNum));

		event.setTags(tagList);
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
						T.showShort(AtyActivitySend.this, "活动发布成功");
						dialog.dismiss();
						AtyActivitySend.this.finish();
					}
				});
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_activity_send);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.ACTIVITY_SIZE);
		AtyDialogImgPick.setConext(this);
		setLocationResultListener(this);
		initUI();
		initViewPager();
		initPagersUI();
		initDatas();
	}

	private void initPagersUI() {
		View view1 = listViews.get(0);
		head = (RoundImageView) view1.findViewById(R.id.rv_avatar);
		tvNick = (TextView) view1.findViewById(R.id.tv_name);
		rlLocation = (RelativeLayout) view1.findViewById(R.id.rl_location);
		tvLocation = (TextView) view1.findViewById(R.id.tv_place_content);
		tvStartTime = (TextView) view1.findViewById(R.id.tv_start_time);
		tvEndTime = (TextView) view1.findViewById(R.id.tv_end_time);
		etPeopleNum = (EditText) view1.findViewById(R.id.tv_people_num);
		etTitle = (EditText) view1.findViewById(R.id.et_title);
		rlLocation.setOnClickListener(this);
		tvStartTime.setOnClickListener(this);
		tvEndTime.setOnClickListener(this);
		NumRegion region = new NumRegion(this, 0, 100);
		region.setRegion(etPeopleNum);

		etContent = (EditText) listViews.get(1).findViewById(R.id.et_content);

		View view3 = listViews.get(2);
		addTag = (AddTagsView) view3.findViewById(R.id.atv_activity_select_tag);
		addTag.setAddTagsListener(new AddTagsListener() {

			@Override
			public void addTags() {
				List<String> tags = addTag.getTags();
				L.i(TAG, "tags.size:" + tags.size());
				if (addTag.getChildCount() < 7) {
					Intent i = new Intent();
					i.setClass(AtyActivitySend.this, AtySelTags.class);
					i.putStringArrayListExtra("tags", (ArrayList<String>) tags);
					startActivityForResult(i, TAGS_RESULT_CODE);
				} else {
					T.showShort(AtyActivitySend.this, "最多可添加六个标签");
				}

			}
		});
		ivCover = (ImageView) view3.findViewById(R.id.iv_cover);
		ivImg2 = (ImageView) view3.findViewById(R.id.iv_img_2);
		ivImg3 = (ImageView) view3.findViewById(R.id.iv_img_3);
		ivImg4 = (ImageView) view3.findViewById(R.id.iv_img_4);
		ivImg5 = (ImageView) view3.findViewById(R.id.iv_img_5);
		btnGreTag = (Button) view3.findViewById(R.id.btn_tag_green);
		btnBluTag = (Button) view3.findViewById(R.id.btn_tag_blue);
		btnPaTag = (Button) view3.findViewById(R.id.btn_tag_purple);
		btnTagAdd = (Button) view3.findViewById(R.id.btn_add_tag);

		ivCover.setOnClickListener(this);
		ivImg2.setOnClickListener(this);
		ivImg3.setOnClickListener(this);
		ivImg4.setOnClickListener(this);
		ivImg5.setOnClickListener(this);
		btnGreTag.setOnClickListener(this);
		btnBluTag.setOnClickListener(this);
		btnPaTag.setOnClickListener(this);
		btnTagAdd.setOnClickListener(this);

		titleWatcher = new EditTextWatcher(this, etTitle, 15);
		etTitle.addTextChangedListener(titleWatcher.getTextWatcher());

		contentWatcher = new EditTextWatcher(this, etContent, 200);
		etContent.addTextChangedListener(contentWatcher.getTextWatcher());

		setEditTextEnter(etContent, this);
		setEditTextEnter(etPeopleNum, this);
		setEditTextEnter(etTitle, this);
	}

	private void initUI() {
		
		dialog = MyProgress.getInstance(AtyActivitySend.this,
				"发布中...");
		
		mPager = (ViewPager) findViewById(R.id.vp_content);
		headView = (HeadView) findViewById(R.id.hv_head);
		btnPager1 = (Button) findViewById(R.id.btn_page1);
		btnPager2 = (Button) findViewById(R.id.btn_page2);
		btnPager3 = (Button) findViewById(R.id.btn_page3);
		llPagers = (LinearLayout) findViewById(R.id.rl_pages);

		headView.setHeadViewListener(this);
		btnPager1.setOnClickListener(this);
		btnPager2.setOnClickListener(this);
		btnPager3.setOnClickListener(this);

	}

	private void initViewPager() {
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.aty_activity_send_1, null));
		listViews.add(mInflater.inflate(R.layout.aty_activity_send_2, null));
		listViews.add(mInflater.inflate(R.layout.aty_activity_send_3, null));
		mPager.setAdapter(new SendPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new PageSelChangeListener());
	}

	private void initDatas() {
		tags = new ArrayList<String>();
		startCalendar = Calendar.getInstance();
		startCalendar.set(Calendar.MINUTE,
				DateTimePickDialog.getSelMin(startCalendar.get(Calendar.MINUTE)));
		String now = TimeUtil.dateToString(startCalendar.getTime(), TimeUtil.FORMAT_TIME7);
		endCalendar = startCalendar;

		tvStartTime.setText(now);
		tvEndTime.setText(now);
		tvNick.setText(SettingUtility.getUserSelf().getNickName());
		if (!StrUtils.isEmpty(SettingUtility.getUserSelf().getFace())) {
			GlobalContext.getInstance().imageLoader
					.displayImage(SettingUtility.getUserSelf().getFace(), head,
							GlobalContext.options, null);
		}else{
			head.setImageResource(R.drawable.default_head);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_page1:
			setPager(0);
			break;
		case R.id.btn_page2:
			setPager(1);
			break;
		case R.id.btn_page3:
			setPager(2);
			break;
		case R.id.rl_location:
			Intent i = new Intent();
			i.setClass(this, AtySelPlaceDialog.class);
			i.putExtra("latitude", mLocation.latitude);
			i.putExtra("longitude", mLocation.longitude);
			startActivityForResult(i, AtySelPlaceDialog.PLACE_CODE);
			break;
		case R.id.tv_start_time:
			new DateTimePickDialog(AtyActivitySend.this,startCalendar,TimeUtil.FORMAT_TIME7,tvStartTime);
			break;
		case R.id.tv_end_time:
			new DateTimePickDialog(AtyActivitySend.this,endCalendar,TimeUtil.FORMAT_TIME7,tvEndTime);
			break;
		case R.id.iv_cover:
		case R.id.iv_img_2:
		case R.id.iv_img_3:
		case R.id.iv_img_4:
		case R.id.iv_img_5:
			L.i(TAG, "选择图片");
			showImgPickDialog();
			break;
		case R.id.btn_tag_green:
			btnGreTag.setSelected(!v.isSelected());
			btnBluTag.setSelected(false);
			btnPaTag.setSelected(false);
			break;
		case R.id.btn_tag_blue:
			btnGreTag.setSelected(false);
			btnBluTag.setSelected(!v.isSelected());
			btnPaTag.setSelected(false);
			break;
		case R.id.btn_tag_purple:
			btnGreTag.setSelected(false);
			btnBluTag.setSelected(false);
			btnPaTag.setSelected(!v.isSelected());
			break;
		case R.id.btn_add_tag:
			btnTagAdd.setVisibility(View.GONE);
			List<String> tags = addTag.getTags();
			L.i(TAG, "selected tags: " + tags.size());
			if (tags.size() < 6) {
				Intent intent = new Intent();
				intent.setClass(this, AtySelTags.class);
				intent.putStringArrayListExtra("tags", (ArrayList<String>) tags);
				startActivityForResult(intent, TAGS_RESULT_CODE);
			} else {
				T.showShort(this, "最多可添加六个标签");
			}
			break;
		default:
			break;
		}
	}

	/**
	 * 获取悬赏标签的级别
	 * 
	 * @return
	 */
	private int getLevel() {
		int level = 0;
		if (btnGreTag.isSelected()) {
			level = 1;
		} else if (btnBluTag.isSelected()) {
			level = 2;
		} else if (btnPaTag.isSelected()) {
			level = 3;
		}
		return level;
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
			} else if (requestCode == AtySelPlaceDialog.PLACE_CODE) {
				placeName = data.getStringExtra("placeName");
				mLocation = new LatLng(data.getDoubleExtra("latitude",
						Configs.latitude), data.getDoubleExtra("longitude",
						Configs.longitude));
				tvLocation.setText(placeName);
				L.i(TAG, "placeName = " + placeName);
			} else if (requestCode == TAGS_RESULT_CODE) {
				resultTags = data.getStringArrayListExtra("tags");
				L.i(TAG, "check resultTags: " + resultTags.size());
				if (resultTags != null && resultTags.size() > 0) {
					tagList = new ArrayList<TagInfo>();
					addTag.setVisibility(View.VISIBLE);
					tags.addAll(resultTags);
					for (String str : tags) {
						TagInfo tag = new TagInfo();
						tag.setName(str);
						tagList.add(tag);
					}
					addTag.setTags(tagList);
					addTag.setTagSelected();
				}
			}
		}
	}

	private void setPreImg() {
		if (!SelectImg.getIntance().isSelEmpty()) {
			ImageView[] imgs = { ivCover, ivImg2, ivImg3, ivImg4, ivImg5 };
			int selSize = SelectImg.getIntance().getSelectImgList().size();
			for (int i = 0; i < imgs.length; i++) {
				if (i < selSize) {
					imgs[i].setImageBitmap(BitmapUtil
							.AbbreviationsIcon(SelectImg.getIntance()
									.getSelectImgList().get(i)));
				} else {
					imgs[i].setImageBitmap(null);
				}
			}
		}
	}

	private void setPager(int index) {
		pagerIndex = index;
		mPager.setCurrentItem(pagerIndex);
		switch (pagerIndex) {
		case 0:
			llPagers.setBackgroundResource(R.drawable.btn_send_1);
			break;
		case 1:
			llPagers.setBackgroundResource(R.drawable.btn_send_2);
			break;
		case 2:
			llPagers.setBackgroundResource(R.drawable.btn_send_3);
			break;
		default:
			llPagers.setBackgroundResource(R.drawable.btn_send_1);
			break;
		}
	}

	public class PageSelChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			setPager(index);
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void locationChanged() {
		mLocation = getLatLng();
		L.i(TAG, "发布时定位  成功");
	}

	@Override
	public void locationFail() {
		mLocation = new LatLng(Configs.latitude, Configs.longitude);
		L.i(TAG, "发布时定位  失败");
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		dialog.show();
		sendActivity();
	}
}
