package com.baoluo.community.ui.adapter;

import java.io.IOException;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyInform;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.ToFriendInfoUtil;

public class HumorListAdapter extends CommonAdapter<HumorInfo> {
	private static final String TAG = "HumorListAdapter";
	private AnimationDrawable animationDrawable;
	private boolean isPraised;
	private MediaPlayer player;
	public HumorListAdapter(Context context, List<HumorInfo> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
	}

	@Override
	public void convert(final ViewHolder helper, final HumorInfo item) {
		if (item.getPictures().size() > 0) {
			helper.setNetImageByUrl(R.id.iv_humor_item_img, item.getPictures()
					.get(0).getUrl());
		} else {
			helper.setImageResource(R.id.iv_humor_item_img, R.drawable.bg_no_pic);
		}
		if (item.getLocation() != null) {
			helper.setText(R.id.tv_humor_item_location, item.getDistance());
		}else{
			helper.setText(R.id.tv_humor_item_location, "未公开");
		}

		helper.setText(R.id.tv_humor_item_content, item.getContent());
		if (!StrUtils.isEmpty(item.getBlogUser().getFace())) {
			helper.setNetImageByUrl(R.id.riv_humor_item_avatar, item
					.getBlogUser().getFace());
		} else {
			helper.setImageResource(R.id.riv_humor_item_avatar,
					R.drawable.head_default);
		}
		helper.getView(R.id.riv_humor_item_avatar).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				ToFriendInfoUtil.getInstance().toFriendInfo(mContext,item.getBlogUser().getId());
			}
		});
		
		final ImageView ivVoice = helper.getView(R.id.iv_humor_item_voice);
		ivVoice.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (animationDrawable.isRunning()) {
					animationDrawable.stop();
					stopPlayVoice();
					animationDrawable.selectDrawable(0);
				} else {
					animationDrawable.start();
					palyNetVoice(item.getVoice().getVoiceUri());
				}
			}
		});
		if(item.getVoice()!=null&& !StrUtils.isEmpty(item.getVoice().getVoiceUri())){
			ivVoice.setVisibility(View.VISIBLE);
			animationDrawable = (AnimationDrawable) ivVoice
					.getBackground();
			animationDrawable.setOneShot(false);
			animationDrawable.stop();
			animationDrawable.selectDrawable(0);
			
		}else{
			ivVoice.setVisibility(View.GONE);
		}
		
		helper.setText(R.id.tv_humor_item_user, item.getBlogUser().getName());
		helper.setText(R.id.tv_humor_item_time, item.getPostTime());
		ImageButton ibShare = helper.getView(R.id.ib_humor_share);
		final TextView tvPraise = helper.getView(R.id.tv_humor_list_praise);
//		TextView tvComment = helper.getView(R.id.tv_humor_list_comment);
		isPraised = item.isIsPraise();
		if (item.isIsPraise()) {
			tvPraise.setSelected(true);
		} else {
			tvPraise.setSelected(false);
		}
		tvPraise.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new PostTask(UrlHelper.HUMOR_PRAISE,
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onComplete(Object obj) {
								if (!isPraised) {
									tvPraise.setSelected(true);
								} else {
									tvPraise.setSelected(false);
								}
								isPraised = !isPraised;
							}
						}, "Id", item.getId());
				T.showShort(mContext, "Praise");
			}
		});
		ibShare.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = new Intent();
				i.setClass(mContext,AtyInform.class);
				Bundle b = new Bundle();
				b.putSerializable("user", item.getBlogUser());
				b.putString("Mid", item.getId());
				b.putInt("Modules", 0);
				i.putExtras(b);
				mContext.startActivity(i);
			}
		});
	}
	/**
	 * 停止播放
	 */
	private void stopPlayVoice() {
		if (null != player) {
			player.stop();
			player.release();
			player = null;
		}
	}

	/**
	 * 通过url播放语音
	 */
	private void palyNetVoice(String url) {
		player = new MediaPlayer();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (animationDrawable.isRunning()) {
					animationDrawable.stop();
					animationDrawable.selectDrawable(0);
					stopPlayVoice();
				}
			}
		});
		if (player != null) {
			try {
				player.setDataSource(url);
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

	/*
	 * class MyImageListener extends SimpleImageLoadingListener{ private
	 * ImageView imgView; public MyImageListener(ImageView imgView){
	 * this.imgView = imgView; }
	 * 
	 * @Override public void onLoadingStarted(String imageUri, View view) {
	 * super.onLoadingStarted(imageUri, view); }
	 * 
	 * @Override public void onLoadingFailed(String imageUri, View view,
	 * FailReason failReason) { super.onLoadingFailed(imageUri, view,
	 * failReason); }
	 * 
	 * @Override public void onLoadingComplete(String imageUri, View view,
	 * Bitmap loadedImage) { L.i(TAG, "tagView ="+imgView.getTag()+"  if sure="
	 * +(imgView.getTag() != null &&
	 * imgView.getTag().toString().equals(imageUri))); if(imgView.getTag() !=
	 * null && imgView.getTag().toString().equals(imageUri)){
	 * imgView.setImageBitmap(loadedImage); } super.onLoadingComplete(imageUri,
	 * view, loadedImage); } }
	 */
}
