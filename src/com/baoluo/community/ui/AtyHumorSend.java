package com.baoluo.community.ui;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
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
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.entity.LocationInfo;
import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.entity.VoiceInfo;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.ImgUpLoadTask;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.support.task.VoiceUpLoadTask;
import com.baoluo.community.support.voice.RecordHelp;
import com.baoluo.community.ui.AtyBaseLBS.LocationResultListener;
import com.baoluo.community.ui.adapter.SendPagerAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.ImageTagView;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.ui.listener.EditTextWatcher;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.Counter;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

public class AtyHumorSend extends AtyDialogImgPick implements OnClickListener,
		LocationResultListener, HeadViewListener {
	private static final String TAG = "AtyHumorSend";
	private static final int HUMOR_TAGS_RESULT_CODE = 0x08;
	private ViewPager mPager;
	private HeadView headView;
	private EditText etContent;
	private Dialog  dialog;
	private RoundImageView head;
	private Button btnPager1, btnPager2, btnShowLocation, btnTagAdd;
	private ImageView ivImg, imgDel, ivVoice;
	private Button imgRecord, imgPlay, imgPause;
	private TextView tvTime,tvNick;
	private LinearLayout llPagers;
	private ImageTagView itvTags;
	private VoiceInfo voice;
	private EditTextWatcher contentWatcher;

	private List<View> listViews;
	private List<String> tags = new ArrayList<String>();
	private boolean showLocation;
	private int pagerIndex = 0;
	private LatLng mLocation;
	private String audioPath;
	private String voiceUrl;
	private int lenght;
	private Counter c;

	private AnimationDrawable animationDrawable;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_humor_send);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.HUMOR_SIZE);
		AtyDialogImgPick.setConext(this);
		setLocationResultListener(this);
		initUI();
		initUser();
		initPagersUI();
		initLocationBtn(true);
	}

	private void initUser() {
		UserSelf self = SettingUtility.getUserSelf();
		if(self!=null){
			tvNick.setText(self.getNickName());
			if(!StrUtils.isEmpty(self.getFace())){
				GlobalContext.getInstance().imageLoader.displayImage(self.getFace(), head, GlobalContext.options, null);
			}else{
				head.setImageResource(R.drawable.head_default);
			}
		}
	}

	private void initUI() {
		dialog = MyProgress.getInstance(AtyHumorSend.this,
				"发布中...");
		head = (RoundImageView) findViewById(R.id.rv_avatar);
		tvNick = (TextView) findViewById(R.id.tv_humor_send_nick);
		head.setOnClickListener(this);
		mPager = (ViewPager) findViewById(R.id.vp_content);
		headView = (HeadView) findViewById(R.id.hv_head);
		btnPager1 = (Button) findViewById(R.id.btn_page1);
		btnPager2 = (Button) findViewById(R.id.btn_page2);
		llPagers = (LinearLayout) findViewById(R.id.rl_pages);
		ivImg = (ImageView) findViewById(R.id.iv_img);
		itvTags = (ImageTagView) findViewById(R.id.itv_tags);
		ivVoice = (ImageView) findViewById(R.id.iv_voice);
		animationDrawable = (AnimationDrawable) ivVoice.getBackground();
		animationDrawable.setOneShot(false);
		headView.setHeadViewListener(this);
		btnPager1.setOnClickListener(this);
		btnPager2.setOnClickListener(this);
		ivImg.setOnClickListener(this);

		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.aty_humor_send_1, null));
		listViews.add(mInflater.inflate(R.layout.aty_humor_send_2, null));
		mPager.setAdapter(new SendPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new PageSelChangeListener());
	}

	private void initPagersUI() {
		etContent = (EditText) listViews.get(0).findViewById(R.id.et_content);
		setEditTextEnter(etContent, this);
		View view2 = listViews.get(1);
		btnShowLocation = (Button) view2.findViewById(R.id.btn_local);
		btnTagAdd = (Button) view2.findViewById(R.id.btn_add_tag);
		imgRecord = (Button) view2.findViewById(R.id.iv_voice);
		imgPlay = (Button) view2.findViewById(R.id.iv_voice_play);
		imgDel = (ImageView) view2.findViewById(R.id.iv_voice_del);
		tvTime = (TextView) view2.findViewById(R.id.tv_voice_time);
		imgPause = (Button) view2.findViewById(R.id.iv_voice_pause);
		imgRecord.setOnClickListener(this);
		imgDel.setOnClickListener(this);
		imgPlay.setOnClickListener(this);
		tvTime.setOnClickListener(this);
		imgPause.setOnClickListener(this);

		btnShowLocation.setOnClickListener(this);
		btnTagAdd.setOnClickListener(this);

		contentWatcher = new EditTextWatcher(this, etContent, 200);
		etContent.addTextChangedListener(contentWatcher.getTextWatcher());
		c = new Counter(1 * 60 * 1000, 1000, tvTime);
	}

	private String content;

	private boolean verify() {
		content = etContent.getText().toString();
		if (StrUtils.isEmpty(content)) {
			T.showShort(this, "心情内容不能为空");
			return false;
		}
		if (SelectImg.getIntance().isSelEmpty()) {
			T.showShort(this, "请选择上传图片");
			return false;
		}
		return true;
	}

	private void sendHumor() {
		if (!verify()) {
			return;
		}
		try {
			if (!SelectImg.getIntance().isSelEmpty()) {
				new ImgUpLoadTask(UrlHelper.FILE_URL, SelectImg.getIntance()
						.getSelectImgList().get(0),
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onComplete(Object data) {
								upLoadVoice();
							}
						});
			}
			AtyHumorSend.this.finish();
		} catch (Exception e) {
			e.printStackTrace();
			L.e(TAG, "upimg err !");
		}
	}

	private void upLoadVoice() {
		if (StrUtils.isEmpty(audioPath) || !(new File(audioPath)).exists()) {
			sendHumorCtx();
			return;
		}
		new VoiceUpLoadTask(UrlHelper.UPLOAD_VOICE, audioPath,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							voiceUrl = StrUtils.parseImgBackUrl((String) data);
							L.i(TAG, "voiceUrl = " + voiceUrl);
							sendHumorCtx();
					}
				});
	}

	private void sendHumorCtx() {
		List<PictureInfo> pictures = new ArrayList<PictureInfo>();
		PictureInfo pic;
		if (SelectImg.getIntance().getUpImgList().size() > 0) {
			pic = new PictureInfo();
			pic.setName("");
			pic.setUrl(SelectImg.getIntance().getUpImgList().get(0));
			pic.setTags(itvTags.getTagList());
			pictures.add(pic);
		}
		if (!StrUtils.isEmpty(voiceUrl) && lenght > 0) {
			voice = new VoiceInfo();
			voice.setFormat(".amr");
			voice.setLenght(lenght);
			voice.setVoiceUri(voiceUrl);
		}
		HumorInfo humor = new HumorInfo();
		humor.setBlogType(1);
		humor.setContent(content);
//		humor.setPublishType(publicType + "");
		humor.setIsShowLocation(showLocation);
		humor.setDevice(android.os.Build.MODEL);
		humor.setPictures(pictures);
		humor.setVoice(voice);
		if (showLocation && mLocation != null) {
			LocationInfo location = new LocationInfo();
			location.setName(aMapLocation.getCity());
			location.setAddress(aMapLocation.getAddress());
			location.setLat(mLocation.latitude);
			location.setLon(mLocation.longitude);
			humor.setLocation(location);
		}
		JSONObject obj = null;
		try {
			obj = new JSONObject(GsonUtil.getInstance().obj2Str(humor));
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new PostObjTask(UrlHelper.HUMOR_PUT, obj.toString(),
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						SelectImg.getIntance().clear();
						SelectImg.getIntance().setChooseAble(
								SelectImg.DEFAULT_SIZE);
						T.showShort(AtyHumorSend.this, "心情发布成功");
					}
				});
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
		case R.id.iv_img:
			showImgPickDialog();
			break;
		case R.id.btn_local:
			initLocationBtn(!showLocation);
			break;
		case R.id.btn_add_tag:
			Intent intent = new Intent();
			intent.setClass(this, AtySelTags.class);
			intent.putStringArrayListExtra("tags", (ArrayList<String>) tags);
			startActivityForResult(intent, HUMOR_TAGS_RESULT_CODE);
			break;
		case R.id.iv_voice_del:
			imgPlay.setVisibility(View.GONE);
			imgRecord.setVisibility(View.VISIBLE);
			RecordHelp.getInstance().deleteFile(new File(audioPath));
			ivVoice.setVisibility(View.INVISIBLE);
			imgDel.setVisibility(View.GONE);
			break;
		case R.id.iv_voice_play:
			imgPlay.setVisibility(View.GONE);
			imgPause.setVisibility(View.VISIBLE);
			animationDrawable.start();
			palyLocVoice();
			break;
		case R.id.iv_voice_pause:
			imgPause.setVisibility(View.GONE);
			imgPlay.setVisibility(View.VISIBLE);
			stopPlayVoice();
			animationDrawable.stop();
			animationDrawable.selectDrawable(0);
			break;
		case R.id.tv_voice_time:
			RecordHelp.getInstance().stopAudio();
			c.cancel();
			tvTime.setVisibility(View.GONE);
			imgPlay.setVisibility(View.VISIBLE);
			String time = tvTime.getText().toString();
			String second = time.substring(0, 2);
			lenght = 60 - Integer.parseInt(second);
			// TODO after click del click animotion will play!
			ivVoice.setVisibility(View.VISIBLE);
			imgDel.setVisibility(View.VISIBLE);
			break;
		case R.id.iv_voice:
			imgRecord.setVisibility(View.GONE);
			tvTime.setVisibility(View.VISIBLE);
			c.start();
			audioPath = RecordHelp.getInstance().startAudio();
			break;
		default:
			break;
		}
	}

	private MediaPlayer player;

	private void stopPlayVoice() {
		if (null != player) {
			player.stop();
			player.release();
			player = null;
		}
	}

	private void palyLocVoice() {
		player = new MediaPlayer();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (animationDrawable.isRunning()) {
					stopPlayVoice();
					animationDrawable.stop();
					animationDrawable.selectDrawable(0);
				}
			}
		});
		if (player != null) {
			try {
				player.setDataSource(audioPath);
				player.prepare();
				player.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	private void initLocationBtn(boolean b) {
		showLocation = b;
		btnShowLocation.setSelected(showLocation);
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
			} else if (requestCode == HUMOR_TAGS_RESULT_CODE) {
				List<String> tagsList = data.getStringArrayListExtra("tags");
				tags.addAll(tagsList);
				L.i(TAG, "check tags: " + tags.size());
				tagListener(tagsList);
			}
		}
	}

	private void setPreImg() {
		if (!SelectImg.getIntance().isSelEmpty()) {
			ivImg.setImageBitmap(BitmapUtil.AbbreviationsIcon(SelectImg
					.getIntance().getSelectImgList().get(0)));
		} else {
			ivImg.setImageBitmap(null);
		}
	}

	/**
	 * 移除语音标签
	 * 
	 * @param tag
	 */
//
//	 * private void removeVoiceTag(String tag) { L.i(TAG, "remove Tag=" + tag);
//	 * if (tags.contains(tag)) { L.i(TAG, "contains=" + tags.indexOf(tag));
//	 * itvTags.removeViewAt(tags.indexOf(tag)); tags.remove(tag); } }
//	 *
//	 * private void addVoiceTag(String filePath) { if
//	 * (!StrUtils.isEmpty(filePath)) { tags.add(filePath);
//	 * itvTags.addTag(filePath, 980, 80); } }
//

	private void tagListener(List<String> tags) {
		int i = 50;
		if (tags != null) {
			for (String string : tags) {
				i += 50;
				itvTags.addTag(string, 150, 150 + i);
			}
		}
	}

	private void setPager(int index) {
		pagerIndex = index;
		mPager.setCurrentItem(pagerIndex);
		switch (pagerIndex) {
		case 0:
			llPagers.setBackgroundResource(R.drawable.btn_humor_send_1);
			break;
		case 1:
			llPagers.setBackgroundResource(R.drawable.btn_humor_send_2);
			break;
		default:
			llPagers.setBackgroundResource(R.drawable.btn_humor_send_1);
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
		L.i(TAG, "locationChanged 定位成功   mLocation==null:"
				+ (mLocation == null) + " /" + mLocation.latitude);
	}

	@Override
	public void locationFail() {
		mLocation = null;
		L.e(TAG, "mLocation = null");
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		awakenLoction();
		sendHumor();
		/*startActivityForResult(new Intent(this, AtySelPublishRange.class),
				AtySelPublishRange.PUBLISH_RANGE_RESULT_CODE);*/
	}
}
