package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.util.DensityUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.TimeUtil;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.AtyChatSet;
import com.baoluo.im.ui.listener.VoiceAnimListener;
import com.baoluo.im.util.ChatAdapterUtil;

public class ChatAdapterMqtt extends CommonAdapter<Msg> {
	private static final String TAG = "ChatAdapterMqtt";

	private Context ctx;
	private String myId;
	private ContactsInfo contact;

	public ChatAdapterMqtt(Context context, ContactsInfo contact,
			List<Msg> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.ctx = context;
		this.myId = GlobalContext.getInstance().getUid();
		this.contact = contact;
	}

	@Override
	public void convert(ViewHolder helper, final Msg item) {
		LayoutParams lp;
		ImageView ivAvatar = (ImageView) helper.getView(R.id.iv_avatar);
		final TextView tvMsg = (TextView) helper.getView(R.id.tv_msg);
		TextView tvTime = (TextView) helper.getView(R.id.tv_time);
		if (item.isShowTimed()) {
			tvTime.setVisibility(View.VISIBLE);
			tvTime.setText(TimeUtil.getShowTime(item.getExpired()));
		}else{
			tvTime.setVisibility(View.GONE);
		}
		SpannableStringBuilder ssb = ChatAdapterUtil.getInstance().getContent(
				tvMsg, item.getContentType(), item.getBody());
		int pxValue = DensityUtil.dip2px(42);
		if (isOutMsg(item.getOwner())) {
			lp = new LayoutParams(pxValue, pxValue);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT, RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.BELOW, tvTime.getId());
			lp.setMargins(0, 0, DensityUtil.dip2px(9), 0);
			ivAvatar.setLayoutParams(lp);
			if (!StrUtils.isEmpty(SettingUtility.getUserSelf().getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(
						SettingUtility.getUserSelf().getFace(), ivAvatar);
			} else {
				ivAvatar.setImageResource(R.drawable.default_head);
			}
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.LEFT_OF, ivAvatar.getId());
			lp.addRule(RelativeLayout.ALIGN_TOP, ivAvatar.getId());
			lp.setMargins(DensityUtil.dip2px(50), 0, DensityUtil.dip2px(5), 0);
			tvMsg.setLayoutParams(lp);
			tvMsg.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			if (item.getContentType() != Msg.MSG_TYPE_PIC) {
				tvMsg.setPadding(DensityUtil.dip2px(10), DensityUtil.dip2px(10),
						DensityUtil.dip2px(15), DensityUtil.dip2px(15));
			} else {
				tvMsg.setPadding(0, 0, DensityUtil.dip2px(5), 0);
			}
			tvMsg.setBackgroundResource(R.drawable.bg_chat_out);
			tvMsg.setText(ssb);
		} else {
			lp = new LayoutParams(pxValue, pxValue);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.BELOW, tvTime.getId());
			lp.setMargins(DensityUtil.dip2px(9), 0, 0, 0);
			ivAvatar.setLayoutParams(lp);
			if (!StrUtils.isEmpty(contact.getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(
						contact.getFace(), ivAvatar);
			} else {
				ivAvatar.setImageResource(R.drawable.default_head);
			}
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.RIGHT_OF, ivAvatar.getId());
			lp.addRule(RelativeLayout.ALIGN_TOP, ivAvatar.getId());
			lp.setMargins(DensityUtil.dip2px(5), 0, DensityUtil.dip2px(50), 0);
			tvMsg.setLayoutParams(lp);
			tvMsg.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
			tvMsg.setPadding(DensityUtil.dip2px(15), 0, DensityUtil.dip2px(10),
					0);
			tvMsg.setBackgroundResource(R.drawable.bg_chat_in);
			tvMsg.setText(ssb);
		}
		if (item.getContentType() == Msg.MSG_TYPE_VOICE) {
			AnimationDrawable imVoiceAnimation = null;
			try{
				if(isOutMsg(item.getOwner())){
					imVoiceAnimation = (AnimationDrawable)AnimationDrawable.createFromXml(ctx.getResources(),
							ctx.getResources().getAnimation(R.anim.animation_im_out_voice));
					tvMsg.setCompoundDrawables(imVoiceAnimation,null,null,null);
				}else{
					imVoiceAnimation = (AnimationDrawable)AnimationDrawable.createFromXml(ctx.getResources(),
							ctx.getResources().getAnimation(R.anim.animation_im_in_voice));
					tvMsg.setCompoundDrawables(null,null,imVoiceAnimation,null);
				}
				imVoiceAnimation.setBounds(0,0,imVoiceAnimation.getMinimumWidth(),imVoiceAnimation.getMinimumHeight());
				tvMsg.setCompoundDrawables(imVoiceAnimation, null, null, null);
				imVoiceAnimation.setOneShot(false);
				imVoiceAnimation.stop();
				imVoiceAnimation.selectDrawable(0);
			}catch (Exception e){
				e.printStackTrace();
			}
			tvMsg.setOnClickListener(new VoiceAnimListener(item.getBody(),imVoiceAnimation));
		} else {
			tvMsg.setCompoundDrawables(null,null,null,null);
			tvMsg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					L.i("ChatAdapter", "voice other ------- ");
				}
			});
		}
		ivAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (isOutMsg(item.getOwner())) {
					return;
				}
				Intent it = new Intent();
				it.putExtra(AtyChatSet.FRI_UID_PARAM, contact.getAccountID());
				it.setClass(ctx, AtyChatSet.class);
				ctx.startActivity(it);
			}
		});
	}

	private boolean isOutMsg(String ownerId) {
		return (StrUtils.isEmpty(ownerId) || ownerId.equals(myId));
	}
}
