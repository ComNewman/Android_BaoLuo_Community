package com.baoluo.im.ui.adapter;

import java.util.List;

import android.content.Context;
import android.text.SpannableStringBuilder;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.voice.RecordHelp;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.util.DensityUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.DormMsg;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.util.ChatAdapterUtil;

public class DormChatAdapter extends CommonAdapter<DormMsg>{
	private static final String TAG = "DormChatAdapter";
	
	private Context ctx;

	public DormChatAdapter(Context context, List<DormMsg> mDatas,
			int itemLayoutId) {
		super(context, mDatas, itemLayoutId);
		this.ctx = context;
	}

	@Override
	public void convert(ViewHolder helper, final DormMsg item) {
		LayoutParams lp;
		ImageView ivAvatar = (ImageView) helper.getView(R.id.iv_avatar);
		TextView tvMsg = (TextView) helper.getView(R.id.tv_msg);
		SpannableStringBuilder ssb = ChatAdapterUtil.getInstance().getContent(
				tvMsg, item.getData().getContentType(), item.getData().getBody());
		int pxValue = DensityUtil.dip2px(42);
		
		if (!StrUtils.isEmpty(item.getData().getForm().getFace())) {
			GlobalContext.getInstance().imageLoader.displayImage(
					item.getData().getForm().getFace(), ivAvatar);
		} else{
			ivAvatar.setImageResource(R.drawable.default_head);
		}
		if (isOutMsg(item.getData().getForm().getId())) {
			lp = new LayoutParams(pxValue, pxValue);
			lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT,RelativeLayout.TRUE);
			lp.setMargins(0, 0, DensityUtil.dip2px(9), 0);
			ivAvatar.setLayoutParams(lp);
			
			
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.LEFT_OF, ivAvatar.getId());
			lp.addRule(RelativeLayout.ALIGN_TOP, ivAvatar.getId());
			lp.setMargins(0, 0, DensityUtil.dip2px(5), 0);
		    tvMsg.setLayoutParams(lp);
		    tvMsg.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		    if(item.getData().getContentType() == Msg.MSG_TYPE_PIC){
		    	tvMsg.setPadding(0, 0, DensityUtil.dip2px(8), 0);
		    }else{
		    	tvMsg.setPadding(DensityUtil.dip2px(10), 0, DensityUtil.dip2px(15), 0);
		    }
		    tvMsg.setBackgroundResource(R.drawable.bg_chat_out);
		    tvMsg.setText(ssb);
		} else {
			lp = new LayoutParams(pxValue, pxValue);
			lp.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
			lp.setMargins(DensityUtil.dip2px(9), 0, 0, 0);
			ivAvatar.setLayoutParams(lp);
			
			lp = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			lp.addRule(RelativeLayout.RIGHT_OF, ivAvatar.getId());
			lp.addRule(RelativeLayout.ALIGN_TOP, ivAvatar.getId());
			lp.setMargins(DensityUtil.dip2px(5), 0, 0, 0);
		    tvMsg.setLayoutParams(lp);
		    tvMsg.setGravity(Gravity.CENTER_VERTICAL|Gravity.LEFT);
		    if(item.getData().getContentType() == Msg.MSG_TYPE_PIC){
		    	tvMsg.setPadding(DensityUtil.dip2px(8), 0, 0, 0);
		    }else{
		    	tvMsg.setPadding(DensityUtil.dip2px(15), 0, DensityUtil.dip2px(15), 0);
		    }
		    tvMsg.setBackgroundResource(R.drawable.bg_chat_in);
		    tvMsg.setText(ssb);
		}
		if(item.getData().getContentType() == Msg.MSG_TYPE_VOICE){
			tvMsg.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					L.i("MultiChatAdapterMqtt", ""+item.getData().getBody());
					if(RecordHelp.getInstance().isPlaying()){
						RecordHelp.getInstance().stopPlayRecord();
					}else{
						RecordHelp.getInstance().playRecord(item.getData().getBody(),null);
					}
				}
			});
		}
	}
	
	private boolean isOutMsg(String userId) {
		return userId.equals(GlobalContext.getInstance().getUid());
	}
}
