package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.entity.TopicInfo;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.ImgUpLoadTask;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.adapter.TopicGridViewAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.ImInputView;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.customview.ThumbnailGridView;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.NetUtil;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 发布话题
 * 
 * @author xiangyang.fu
 * 
 */
public class PublishTopicActivity extends AtyDialogImgPick implements
		OnClickListener, LocationResultListener, HeadViewListener {
	private static final String TAG = "PublishTopicActivity";

	private HeadView headView;
	private Dialog dialog;
	private ImageButton btnPic, btnTag, btnFace;
	private EditText etTitle, etContent;
	private RoundImageView head;
	private RelativeLayout rlBottom;
	private ThumbnailGridView gvPic;
	private TextView tvNick, tvHight;
	private TopicGridViewAdapter adapter;

	private LatLng mLocation;
	private String title;
	private String content;
	private String repId;
	private String repTitle;
	private List<String> loadImgsResult = new ArrayList<String>();
	private List<String> picPaths = new ArrayList<String>();
	/**
	 * gridview 内部点击事件
	 */
	private MyClickListener linstener = new MyClickListener() {

		@Override
		public void myOnClick(int position, View v) {
			switch (v.getId()) {
			case R.id.add:
				if (loadImgsResult.size() == 9) {
					T.showShort(PublishTopicActivity.this, "已达到最多图片数量");
					return;
				} else {
					showImgPickDialog();
				}
				break;
			case R.id.delete:
				loadImgsResult.remove(position);
				adapter.notifyDataSetChanged();
				break;
			default:
				break;
			}

		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_publish_topic);
		Intent intent = getIntent();
		repId = intent.getStringExtra(TopicDetailsActivity.ID);
		repTitle = intent.getStringExtra(TopicDetailsActivity.TITLE);
		L.i(TAG, repId + " _____________________" + repTitle);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.DEFAULT_SIZE);
		AtyDialogImgPick.setConext(this);
		setLocationResultListener(this);
		initUI();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initData();
	}

	private void initData() {
		String nickName = SettingUtility.getUserSelf().getNickName();
		String face = SettingUtility.getUserSelf().getFace();
		tvNick.setText(nickName);
		if (!StrUtils.isEmpty(face)) {
			GlobalContext.getInstance().imageLoader.displayImage(face, head,
					null, null);
		} else {
			head.setImageResource(R.drawable.head_test);
		}
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("NewApi")
	private void initUI() {
		rlBottom = (RelativeLayout) findViewById(R.id.rl_topic_create_bottom);
		rlBottom.setAlpha(0.6f);
		dialog = MyProgress.getInstance(PublishTopicActivity.this,
				"发布中...");
		headView = (HeadView) findViewById(R.id.hv_head);
		btnFace = (ImageButton) findViewById(R.id.btn_topic_create_face);
		btnTag = (ImageButton) findViewById(R.id.btn_topic_create_tag);
		btnPic = (ImageButton) findViewById(R.id.btn_topic_create_pic);

		headView.setHeadViewListener(this);
		btnFace.setOnClickListener(this);
		btnTag.setOnClickListener(this);
		btnPic.setOnClickListener(this);

		tvNick = (TextView) findViewById(R.id.tv_topic_create_nick);
		tvHight = (TextView) findViewById(R.id.tv_topic_create_hight);
		if (!StrUtils.isEmpty(repTitle)) {
			L.i(TAG, "评论#" + repTitle + "#这个话题");
			tvHight.setText("评论#" + repTitle + "#这个话题");
		} else {
			tvHight.setText("");
		}
		head = (RoundImageView) findViewById(R.id.rv_topic_create_avatar);

		etTitle = (EditText) findViewById(R.id.et_topic_create_name);
		etTitle.setOnFocusChangeListener(new View.OnFocusChangeListener() {

			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if (hasFocus) {// 获得焦点
					rlBottom.setVisibility(View.GONE);
				} else {// 失去焦点
					rlBottom.setVisibility(View.VISIBLE);
				}
			}
		});
		etContent = (EditText) findViewById(R.id.et_topic_create_content);
		setEditTextEnter(etContent, this);
		setEditTextEnter(etTitle, this);
		gvPic = (ThumbnailGridView) findViewById(R.id.gv_topic_create_pic);
		adapter = new TopicGridViewAdapter(this, loadImgsResult, linstener);
		gvPic.setAdapter(adapter);

	}

	/**
	 * 非空验证
	 * 
	 * @return
	 */
	private boolean verify() {
		content = etContent.getText().toString();
		title = etTitle.getText().toString();
		if (StrUtils.isEmpty(content)) {
			T.showShort(this, "活动内容不能为空");
			return false;
		}
		if (StrUtils.isEmpty(title)) {
			T.showShort(this, "活动标题不能为空");
			return false;
		}
		/*
		 * if (loadImgsResult.size() < 1) { T.showShort(this, "请选择上传图片"); return
		 * false; }
		 */
		return true;
	}

	/**
	 * 发布话题
	 * 
	 * @param range
	 */
	private void sendTopicContent() {
		if (!verify()) {
			return;
		}
		List<PictureInfo> pictures = new ArrayList<PictureInfo>();
		PictureInfo pic;
		if (picPaths.size() > 0) {
			L.i(TAG, "pics size : " + picPaths.size() + "     check first:"
					+ picPaths.get(0));
			for (int i = 0; i < picPaths.size(); i++) {
				pic = new PictureInfo();
				pic.setName("");
				pic.setUrl(picPaths.get(i));
				pictures.add(pic);
			}
		}
		List<TagInfo> tags = new ArrayList<TagInfo>();
		TagInfo tagInfor = new TagInfo();
		tagInfor.setName(title);
		tags.add(tagInfor);
		List<String> tagList = StrUtils.getTags(content);
		if (tagList != null && tagList.size() > 0) {
			L.i(TAG, "taglist size : " + tagList.size()
					+ "        first  tag :" + tagList.get(0));
			TagInfo tag;
			for (String string : tagList) {
				tag = new TagInfo();
				tag.setName(string);
				tags.add(tag);
			}
		}
		TopicInfo topicInfo = new TopicInfo();
		topicInfo.setTitle(title);
		topicInfo.setContent(content);
		topicInfo.setPictures(pictures);
		topicInfo.setTags(tags);
		String paramsObj = GsonUtil.getInstance().obj2Str(topicInfo);
		new PostObjTask(UrlHelper.TOPIC_POST, paramsObj,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						SelectImg.getIntance().clear();
						T.showShort(getApplicationContext(), "话题发布成功");
						dialog.dismiss();
						PublishTopicActivity.this.finish();
					}
				});
	}

	private void sendTopicContent(String repId) {
		if (!verify()) {
			return;
		}
		List<PictureInfo> pictures = new ArrayList<PictureInfo>();
		PictureInfo pic;
		if (picPaths.size() > 0) {
			L.i(TAG, "pics size : " + picPaths.size() + "     check first:"
					+ picPaths.get(0));
			for (int i = 0; i < picPaths.size(); i++) {
				pic = new PictureInfo();
				pic.setName("");
				pic.setUrl(picPaths.get(i));
				pictures.add(pic);
			}
		}
		List<TagInfo> tags = new ArrayList<TagInfo>();
		List<String> tagList = StrUtils.getTags(content);
		if (tagList != null && tagList.size() > 0) {
			L.i(TAG, "taglist size : " + tagList.size()
					+ "        first  tag :" + tagList.get(0));
			TagInfo tag;
			for (String string : tagList) {
				tag = new TagInfo();
				tag.setName(string);
				tags.add(tag);
			}
		}
		TopicInfo topicInfo = new TopicInfo();
		topicInfo.setTopicType(1);
		topicInfo.setTitle(title);
		topicInfo.setContent(content);
		topicInfo.setPictures(pictures);
		topicInfo.setTags(tags);
		topicInfo.setRepId(repId);
		String paramsObj = GsonUtil.getInstance().obj2Str(topicInfo);
		new PostObjTask(UrlHelper.TOPIC_HIGHT_REPLY, paramsObj,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						SelectImg.getIntance().clear();
						T.showShort(getApplicationContext(), "高逼格回复成功");
						dialog.dismiss();
						PublishTopicActivity.this.finish();
					}
				});
	}

	/**
	 * 上传图片完成后发布话题
	 * 
	 * @param range
	 */
	private void sendTopic() {
		if (!verify()) {
			return;
		}
		try {
			if (!(loadImgsResult.size() < 1) && NetUtil.isConnnected(this)) {
				for (int i = 0; i < loadImgsResult.size(); i++) {
					new ImgUpLoadTask(UrlHelper.FILE_URL,
							loadImgsResult.get(i),
							new UpdateViewHelper.UpdateViewListener() {
								@Override
								public void onComplete(Object data) {
										picPaths.add(StrUtils
												.parseImgBackUrl((String) data));
										if (picPaths.size() == loadImgsResult
												.size()) {
											if (!StrUtils.isEmpty(repId)
													&& !StrUtils
															.isEmpty(repTitle)) {
												sendTopicContent(repId);
											} else {
												sendTopicContent();
											}
										}
								}
							});
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "upimg err !");
		}
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
			} 
		}
	}

	/**
	 * 添加图片返回
	 */
	private void setPreImg() {
		if (!SelectImg.getIntance().isSelEmpty()
				&& SelectImg.getIntance().getSelectImgList().size() > 0) {
			for (String imgUrl : SelectImg.getIntance().getSelectImgList()) {
				if (loadImgsResult.size() < 9) {
					loadImgsResult.add(imgUrl);
				} else {
					T.showShort(PublishTopicActivity.this, "已达到最多图片数量");
				}
				L.i(TAG, "loadImgsResult  size :" + loadImgsResult.size());
				adapter.notifyDataSetChanged();
			}
			SelectImg.getIntance().clear();
		}
	}

	/**
	 * 话题内容中添加标签 标识
	 */
	private void setTags() {
		int index = etContent.getSelectionStart();// 获取光标所在位置
		String text = "##";
		Editable edit = etContent.getEditableText();// 获取EditText的文字
		if (index < 0 || index >= edit.length()) {
			edit.append(text);

		} else {
			edit.insert(index, text);// 光标所在位置插入文字
		}
		etContent.setSelection(index + 1);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_topic_create_face:
			T.showShort(this, "~.~ , 表情包还没有!");
			break;
		case R.id.btn_topic_create_pic:
			if (loadImgsResult.size() == 9) {
				T.showShort(PublishTopicActivity.this, "已达到最多图片数量");
				return;
			} else {
				showImgPickDialog();
			}
			break;
		case R.id.btn_topic_create_tag:
			setTags();
			break;
		default:
			break;
		}
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
		// 必不可少，否则所有的组件都不会有TouchEvent了
		if (getWindow().superDispatchTouchEvent(ev)) {
			return true;
		}
		return onTouchEvent(ev);
	}

	/**
	 * 是否点击输入框区域
	 * 
	 * @param v
	 * @param event
	 * @return
	 */
	public boolean isShouldHideInput(View v, MotionEvent event) {
		if (v != null && (v instanceof ImInputView)) {
			int[] leftTop = { 0, 0 };
			// 获取输入框当前的location位置
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
	public void locationChanged() {
		mLocation = getLatLng();
	}

	@Override
	public void locationFail() {
		mLocation = null;
	}

	@Override
	public void leftListener() {
		finish();
	}

	@Override
	public void rightListener() {
		dialog.show();
		if (!(loadImgsResult.size() < 1)) {
			sendTopic();
		} else {
			if (!StrUtils.isEmpty(repId) && !StrUtils.isEmpty(repTitle)) {
				sendTopicContent(repId);
			} else {
				sendTopicContent();
			}
		}
		/*Intent i = new Intent();
		i.setClass(this, AtySelPublishRange.class);
		startActivityForResult(i, AtySelPublishRange.PUBLISH_RANGE_RESULT_CODE);*/
	}
}
