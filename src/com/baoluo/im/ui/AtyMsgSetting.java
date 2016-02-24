package com.baoluo.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.ClearDataManager;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;

/**
 * 消息设置
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyMsgSetting extends AtyBase implements OnClickListener,HeadViewListener {

	private static final String TAG = "AtyMsgSetting";
	public static final int FLAG_CACHE = 0x0;
	private ImageButton ibPublish, ibStranger,ibVoice;
	private boolean isReceivePublish, isShieldStranger,isVoiceOn;
	private HeadView headView;
	private RelativeLayout rlClearCache;
	private TextView tvCacheNum;
	private String cacheNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_msg_setting);
		initUi();
		initData();
	}

	private void initUi() {
		headView = (HeadView) findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		rlClearCache = (RelativeLayout) findViewById(R.id.rl_setting_clear_cache);
		rlClearCache.setOnClickListener(this);
		tvCacheNum = (TextView) findViewById(R.id.tv_setting_clear_mb);

		ibPublish = (ImageButton) findViewById(R.id.ib_setting_recive_publish);
		ibStranger = (ImageButton) findViewById(R.id.ib_setting_stranger);
		ibVoice = (ImageButton) findViewById(R.id.ib_setting_voice);
		
		ibPublish.setOnClickListener(this);
		ibStranger.setOnClickListener(this);
		ibVoice.setOnClickListener(this);
	}

	private void initData() {
		isReceivePublish = SettingUtility.getPushBoolean();
		isShieldStranger = SettingUtility.getStrangerBoolean();
		isVoiceOn = SettingUtility.getVoiceBoolean();
		
		ibPublish.setSelected(isReceivePublish);
		ibStranger.setSelected(isShieldStranger);
		ibVoice.setSelected(isVoiceOn);
		
		try {
			cacheNum = ClearDataManager
					.getTotalCacheSize(getApplicationContext());
			L.i(TAG, cacheNum);
			if (cacheNum != null) {
				tvCacheNum.setText(cacheNum);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_setting_recive_publish:
			isReceivePublish = !isReceivePublish;
			ibPublish.setSelected(isReceivePublish);
			SettingUtility.setPushBoolean(isReceivePublish);
			break;
		case R.id.ib_setting_stranger:
			isShieldStranger = !isShieldStranger;
			ibStranger.setSelected(isShieldStranger);
			SettingUtility.setStrangerBoolean(isShieldStranger);
			if(isShieldStranger){
				strangerBoolean(1);
			}else{
				strangerBoolean(0);
			}
			break;
		case R.id.ib_setting_voice:
			isVoiceOn = !isVoiceOn;
			ibVoice.setSelected(isVoiceOn);
			SettingUtility.setVoiceBoolean(isVoiceOn);
			if(isVoiceOn){
				voiceBoolean(1);
			}else{
				voiceBoolean(0);
			}
			break;
		case R.id.rl_setting_clear_cache:
			if (!(tvCacheNum.getText()).equals("0K")) {
				Intent intent = new Intent();
				intent.setClass(AtyMsgSetting.this, AtyFriMenu.class);
				intent.putExtra("flag", FLAG_CACHE);
				startActivityForResult(intent, AtyMsgSetting.FLAG_CACHE);
			} else {
				T.showShort(this, "缓存已经清除");
			}
			break;
		default:
			break;
		}
	}
	private void strangerBoolean(int stranger) {
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtyMsgSetting.this, "屏蔽陌生人开关");
					}
				},"IsShieldStranger",String.valueOf(stranger));
	}
	private void voiceBoolean(int voice) {
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtyMsgSetting.this, "更改声音开关");
					}
				},"IsOpenSound",String.valueOf(voice));
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == AtyMsgSetting.FLAG_CACHE) {
				int optionType = data.getIntExtra(AtyFriMenu.OPTION_TYPE, -1);
				switch (optionType) {
				case AtyFriMenu.SETTING_CLEAR_CACHE:
					clearCache();
					break;
				case AtyFriMenu.SETTING_CLEAR_CACHE_CANCLE:
					T.showShort(this, "取消");
					break;
				}
			}
		}
	}
	/**
	 * 清除缓存
	 */
	private void clearCache() {
		try {
			ClearDataManager.clearAllCache(getApplicationContext());
			tvCacheNum.setText("0K");
		} catch (Exception e) {
			e.printStackTrace();
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
