package com.baoluo.im.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.DensityUtil;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.gif.AnimatedGifDrawable;
import com.baoluo.im.gif.AnimatedImageSpan;

/**
 * 聊天界面adpter util
 * @author tao.lai
 *
 */
public class ChatAdapterUtil {
	
private static final String TAG = "ChatAdapterUtil";
	
	private static ChatAdapterUtil instance;
	public static ChatAdapterUtil getInstance(){
		if(instance == null){
			instance = new ChatAdapterUtil();
		}
		return instance;
	}
	
	public SpannableStringBuilder getContent(TextView tvContent,
			byte contentType, String content) {
		SpannableStringBuilder ssb = null ;
		if(contentType == Msg.MSG_TYPE_PIC){
			ssb = handlePic(content);
		}else if(contentType == Msg.MSG_TYPE_VOICE){
			ssb = handleVoice(content);
		}else if(contentType == Msg.MSG_TYPE_TEXT){
			if(isEmoji(content)){
				ssb = handleEmoji(tvContent,content);
			}else{
				ssb = InputBoxUtil.getInstance().handler(GlobalContext.getInstance(), content);
			}
		}else{
			ssb = new SpannableStringBuilder(content);
		}
		return ssb;
	}
	
	private SpannableStringBuilder handlePic(String filePath){
		SpannableStringBuilder ssb = new SpannableStringBuilder(filePath);
		ssb.setSpan(new ImageSpan(GlobalContext.getInstance(), getShrinkPic(filePath)),
				0, filePath.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}
	
	private SpannableStringBuilder handleVoice(String voicePath) {
		SpannableStringBuilder ssb = new SpannableStringBuilder(voicePath);
		ssb.setSpan(
				new ImageSpan(GlobalContext.getInstance(), BitmapFactory
						.decodeResource(GlobalContext.getInstance()
								.getResources(), 0)),
				0, voicePath.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		return ssb;
	}
	private SpannableStringBuilder handleEmoji(final TextView tvContent,
			String content) {
		SpannableStringBuilder ssb = new SpannableStringBuilder(content);
		Context ctx = GlobalContext.getInstance();
		try {
			String num = content.substring("[em:".length(), content.length()
					- "]".length());
			String gif = "face/gif/" + num + ".gif";
			InputStream is = ctx.getAssets().open(gif);
			ssb.setSpan(new AnimatedImageSpan(new AnimatedGifDrawable(is,
					new AnimatedGifDrawable.UpdateListener() {
						@Override
						public void update() {
							tvContent.postInvalidate();
						}
					})), 0, content.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			is.close();
		} catch (Exception e) {
			String name = content.substring("[em:".length(), content.length()
					- "]".length());
			String png = "face/png/" + name + ".png";
			try {
				ssb.setSpan(
						new ImageSpan(ctx, BitmapFactory.decodeStream(ctx
								.getAssets().open(png))), 0, content.length(),
						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		return ssb;
	}
	
	private boolean isEmoji(String msg){
		String regex ="(\\[em:x)\\d{2}(\\])";
		//"\\[em:[0-9a-zA-Z]+\\]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(msg);
		while (m.find()) {
			return true;
		}
		return false;
	}
	
	
	/**
	 * 清除空格 和换行
	 * @param str
	 * @return
	 */
	private String cleanStr(String str) {
		String dest = str;
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}


	/**
	 * 本地图片
	 * @param picPath
	 * @return
	 */
	private Bitmap getShrinkPic(String picPath) {
		try {
			return BitmapUtil.getThumbNail(picPath, DensityUtil.dip2px(150));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 网络图片
	 * @param picNetPath
	 * @return
	 */
	private Bitmap getShrinkNetPic(String picNetPath){
		try{
			Bitmap bm = GlobalContext.getInstance().imageLoader
					.loadImageSync(picNetPath);
			return BitmapUtil.getThumbNail(bm,DensityUtil.dip2px(150));
		}catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

}
