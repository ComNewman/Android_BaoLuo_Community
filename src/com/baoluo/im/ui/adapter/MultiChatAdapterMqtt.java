package com.baoluo.im.ui.adapter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
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
import com.baoluo.community.util.ToFriendInfoUtil;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.GroupUser;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.listener.VoiceAnimListener;
import com.baoluo.im.util.ChatAdapterUtil;

public class MultiChatAdapterMqtt extends CommonAdapter<Msg> {
	
	private Context ctx;
	private Map<String,GroupUser> map;

	public MultiChatAdapterMqtt(Context context,GroupInfo groupInfo, List<Msg> mDatas, int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		map = new HashMap<String,GroupUser>();
		this.ctx = context;
		if(groupInfo.getUsers() != null && groupInfo.getUsers().size() > 0){
			for(GroupUser item : groupInfo.getUsers()){
				map.put(item.getId(), item);
			}
		}
	}

	@Override
	public void convert(ViewHolder helper, final Msg item) {
		LayoutParams lp;
		ImageView ivAvatar = (ImageView) helper.getView(R.id.iv_avatar);
		TextView tvMsg = (TextView) helper.getView(R.id.tv_msg);
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
		final GroupUser user = map.get(item.getOwner());
		if (isOutMsg(item.getIsOut())) {
			lp = new LayoutParams(pxValue, pxValue);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.BELOW, tvTime.getId());
			lp.setMargins(0, 0, DensityUtil.dip2px(9), 0);
			ivAvatar.setLayoutParams(lp);
			if (!StrUtils.isEmpty(SettingUtility.getUserSelf().getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(
						SettingUtility.getUserSelf().getFace(), ivAvatar);
			} else{
				ivAvatar.setImageResource(R.drawable.default_head);
			}
			
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.LEFT_OF, ivAvatar.getId());
			lp.addRule(RelativeLayout.ALIGN_TOP, ivAvatar.getId());
			lp.setMargins(0, 0, DensityUtil.dip2px(5), 0);
		    tvMsg.setLayoutParams(lp);
		    tvMsg.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		    if(item.getContentType() == Msg.MSG_TYPE_PIC){
		    	tvMsg.setPadding(0, 0, DensityUtil.dip2px(8), 0);
		    }else{
		    	tvMsg.setPadding(DensityUtil.dip2px(10), 0, DensityUtil.dip2px(15), 0);
		    }
		    tvMsg.setBackgroundResource(R.drawable.bg_chat_out);
		    tvMsg.setText(ssb);
		} else {
			lp = new LayoutParams(pxValue, pxValue);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			lp.addRule(RelativeLayout.BELOW, tvTime.getId());
			lp.setMargins(DensityUtil.dip2px(9), 0, 0, 0);
			ivAvatar.setLayoutParams(lp);
			if (user != null && !StrUtils.isEmpty(user.getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(
						user.getFace(), ivAvatar);
			} else{
				ivAvatar.setImageResource(R.drawable.default_head);
			}
			
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.RIGHT_OF, ivAvatar.getId());
			lp.addRule(RelativeLayout.ALIGN_TOP, ivAvatar.getId());
			lp.setMargins(DensityUtil.dip2px(5), 0, 0, 0);
		    tvMsg.setLayoutParams(lp);
		    tvMsg.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		    if(item.getContentType() == Msg.MSG_TYPE_PIC){
		    	tvMsg.setPadding(DensityUtil.dip2px(8), 0, 0, 0);
		    }else{
		    	tvMsg.setPadding(DensityUtil.dip2px(15), 0, DensityUtil.dip2px(15), 0);
		    }
		    tvMsg.setBackgroundResource(R.drawable.bg_chat_in);
		    tvMsg.setText(ssb);
		}


		if (item.getContentType() == Msg.MSG_TYPE_VOICE) {
			AnimationDrawable imVoiceAnimation = null;
			try{
				if(isOutMsg(item.getIsOut())){
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
					L.i("MultiChatAdapter", "voice other ------- ");
				}
			});
		}
		ivAvatar.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if(isOutMsg(item.getIsOut()) || user == null){
					L.i("MultiChatAdapterMqtt", "is out Msg");
					return;
				}
				ToFriendInfoUtil.getInstance().toFriendInfo(ctx,user.getId());
				/*Intent it = new Intent();
				it.putExtra(AtyChatSet.FRI_UID_PARAM, user.getId());
				it.setClass(ctx, AtyChatSet.class);
				ctx.startActivity(it);*/
			}
		});
	}
	
	private boolean isOutMsg(byte isOut){
		return isOut == Msg.MSG_OUT;
	}
}
