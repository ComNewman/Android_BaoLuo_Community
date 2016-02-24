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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps2d.model.LatLng;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.entity.LocationInfo;
import com.baoluo.community.entity.PictureInfo;
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
import com.baoluo.community.ui.imgpick.AtyDialogImgPick;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.Counter;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
/**
 * 最新心情发布页面
 * @author xiangyang.fu
 *
 */
public class AtyHumorSendNew extends AtyDialogImgPick implements
		OnClickListener, LocationResultListener, HeadViewListener {
	
	private static final int HUMOR_TAG_RESULT_CODE = 0x08;
	private ViewPager mPager;
	private	ImageView ivPic,ivVoice;
	private HeadView headView;
	private List<View> listViews;
	private ImageButton ibTag, ibVoice, ibContent;
	private ImageTagView itvTags;
	private AnimationDrawable animationDrawable;
	
	private TextView tvTags,tvVoiceTime;
	private ImageButton ibVoiceDel,ibVoiceRecord,ibVoicePause,ibVoicePlay;
	private EditText etContent;
	private Dialog dialog;
	private Counter c;	
	private String audioPath;
	private String voiceUrl;
	private List<String> tags = new ArrayList<String>();
	private LatLng mLocation;
	private int lenght;
	private VoiceInfo voice;
	private String content;
	private MediaPlayer player;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_humor_send_new);
		SelectImg.getEmptyIntance().setChooseAble(SelectImg.HUMOR_SIZE);
		AtyDialogImgPick.setConext(this);
		setLocationResultListener(this);
		initUi();
		initPagersUI();
	}

	private void initPagersUI() {
		tvTags = (TextView) listViews.get(0).findViewById(R.id.tv_humor_send_tags);
		tvTags.setOnClickListener(this);
		
		ibVoiceRecord = (ImageButton) listViews.get(1).findViewById(R.id.ib_humor_send_voice_record);
		ibVoiceDel = (ImageButton) listViews.get(1).findViewById(R.id.ib_humor_send_voice_del);
		tvVoiceTime = (TextView) listViews.get(1).findViewById(R.id.tv_humor_send_voice_time);
		ibVoicePause = (ImageButton) listViews.get(1).findViewById(R.id.ib_humor_send_voice_pause);
		ibVoicePlay = (ImageButton) listViews.get(1).findViewById(R.id.ib_humor_send_voice_play);
		ibVoicePause.setOnClickListener(this);
		ibVoicePlay.setOnClickListener(this);
		tvVoiceTime.setOnClickListener(this);
		ibVoiceRecord.setOnClickListener(this);
		ibVoiceDel.setOnClickListener(this);
		
		etContent = (EditText) listViews.get(2).findViewById(R.id.et_humor_send_content);
		
		c = new Counter(1 * 60 * 1000, 1000, tvVoiceTime);
	}

	private void initUi() {
		headView = (HeadView) findViewById(R.id.hv_humor_send_head);
		headView.setHeadViewListener(this);
		
		dialog = MyProgress.getInstance(AtyHumorSendNew.this,
				"发布中...");
		ivPic = (ImageView) findViewById(R.id.iv_humor_send_img);
		ivPic.setOnClickListener(this);
		
		itvTags = (ImageTagView) findViewById(R.id.itv_humor_send_tags);
		
		ivVoice = (ImageView) findViewById(R.id.iv_humor_send_voice);
		
		animationDrawable = (AnimationDrawable) ivVoice.getBackground();
		animationDrawable.setOneShot(false);
		animationDrawable.stop();
		animationDrawable.selectDrawable(0);
		
		ibTag = (ImageButton) findViewById(R.id.ib_humor_send_tags);
		ibVoice = (ImageButton) findViewById(R.id.ib_humor_send_voice);
		ibContent = (ImageButton) findViewById(R.id.ib_humor_send_content);
		ibTag.setOnClickListener(this);
		ibVoice.setOnClickListener(this);
		ibContent.setOnClickListener(this);

		mPager = (ViewPager) findViewById(R.id.vp_humor_send);
		listViews = new ArrayList<View>();
		LayoutInflater mInflater = getLayoutInflater();
		listViews.add(mInflater.inflate(R.layout.aty_humor_send_tags, null));
		listViews.add(mInflater.inflate(R.layout.aty_humor_send_voice, null));
		listViews.add(mInflater.inflate(R.layout.aty_humor_send_content, null));
		mPager.setAdapter(new SendPagerAdapter(listViews));
		mPager.setCurrentItem(0);
		mPager.setOnPageChangeListener(new PageSelChangeListener());
		setPager(0);
	}

	private void setPager(int index) {
		mPager.setCurrentItem(index);
		switch (index) {
		case 0:
			ibTag.setSelected(true);
			ibVoice.setSelected(false);
			ibContent.setSelected(false);
			break;
		case 1:
			ibVoice.setSelected(true);
			ibContent.setSelected(false);
			ibTag.setSelected(false);
			break;
		case 2:
			ibContent.setSelected(true);
			ibTag.setSelected(false);
			ibVoice.setSelected(false);
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
			} else if (requestCode == HUMOR_TAG_RESULT_CODE) {
				if(data!=null){
					// TODO
					tags.clear();
					List<String> tagsList = data.getStringArrayListExtra(AtyTags.HUMOR_TAG);
					tags.addAll(tagsList);
					tagListener(tagsList);
				}
			}
		}
	}
	private void tagListener(List<String> tags) {
		int i = 50;
		if (tags != null) {
			itvTags.removeAll();
			for (String string : tags) {
				i += 50;
				itvTags.addTag(string, 150, 150 + i);
			}
		}
	}

	private void setPreImg() {
		if (!SelectImg.getIntance().isSelEmpty()) {
			ivPic.setImageBitmap(BitmapUtil.AbbreviationsIcon(SelectImg
					.getIntance().getSelectImgList().get(0)));
		} else {
			ivPic.setImageBitmap(null);
		}
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		dialog.show();
		sendHumor();
	}

	@Override
	public void locationChanged() {
		mLocation = getLatLng();
	}

	@Override
	public void locationFail() {
		mLocation = null;
	}
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
			dialog.dismiss();
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
		} catch (Exception e) {
			e.printStackTrace();
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
							sendHumorCtx();
					}
				});
	}

	private void sendHumorCtx() {
		HumorInfo humor = new HumorInfo();
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
			humor.setVoice(voice);
		}
		humor.setBlogType(1);
		humor.setContent(content);
		humor.setDevice(android.os.Build.MODEL);
		humor.setPictures(pictures);
		boolean showLocation = SettingUtility.getLocationBoolean();
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
						T.showShort(AtyHumorSendNew.this, "心情发布成功");
						dialog.dismiss();
						AtyHumorSendNew.this.finish();
					}
				});
	}
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
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_humor_send_tags:
			Intent i = new Intent();
			i.setClass(AtyHumorSendNew.this,AtyTags.class);
			i.putStringArrayListExtra(AtyTags.SEL_TAG, (ArrayList<String>) tags );
			startActivityForResult(i, HUMOR_TAG_RESULT_CODE);
			break;
		case R.id.ib_humor_send_voice_record:
			ibVoiceRecord.setVisibility(View.GONE);
			tvVoiceTime.setVisibility(View.VISIBLE);
			c.start();
			audioPath = RecordHelp.getInstance().startAudio();
			break;
		case R.id.tv_humor_send_voice_time:
			RecordHelp.getInstance().stopAudio();
			tvVoiceTime.setVisibility(View.GONE);
			ibVoicePlay.setVisibility(View.VISIBLE);
			ivVoice.setVisibility(View.VISIBLE);
			ibVoiceDel.setVisibility(View.VISIBLE);
			c.cancel();
			animationDrawable.stop();
			animationDrawable.selectDrawable(0);
			String time = tvVoiceTime.getText().toString();
			String second = time.substring(0, 2);
			lenght = 60 - Integer.parseInt(second);
			break;
		case R.id.ib_humor_send_voice_play:
			ibVoicePlay.setVisibility(View.GONE);
			ibVoicePause.setVisibility(View.VISIBLE);
			animationDrawable.start();
			palyLocVoice();
			break;
		case R.id.ib_humor_send_voice_pause:
			ibVoicePause.setVisibility(View.GONE);
			ibVoicePlay.setVisibility(View.VISIBLE);
			stopPlayVoice();
			animationDrawable.stop();
			animationDrawable.selectDrawable(0);
			break;
		case R.id.ib_humor_send_voice_del:
			ibVoiceRecord.setVisibility(View.VISIBLE);
			tvVoiceTime.setVisibility(View.GONE);
			ibVoicePlay.setVisibility(View.GONE);
			ibVoicePause.setVisibility(View.GONE);
			ivVoice.setVisibility(View.GONE);
			ibVoiceDel.setVisibility(View.GONE);
			RecordHelp.getInstance().deleteFile(new File(audioPath));
			break;
		case R.id.ib_humor_send_tags:
			setPager(0);
			break;
		case R.id.ib_humor_send_voice:
			setPager(1);
			break;
		case R.id.ib_humor_send_content:
			setPager(2);
			break;
		case R.id.iv_humor_send_img:
			showImgPickDialog();
			break;
		}

	}

}
