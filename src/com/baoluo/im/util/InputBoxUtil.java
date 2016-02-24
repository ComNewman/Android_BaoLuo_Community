package com.baoluo.im.util;

import java.io.IOException;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.FileUpLoadTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.event.IMSendMsgEvent;
import com.baoluo.im.entity.Msg;

import de.greenrobot.event.EventBus;


/**
 * 聊天输入框  util
 * @author tao.lai
 *
 */
public class InputBoxUtil {
	private static final String TAG = "getHandledMsg";
	
	private static InputBoxUtil instance;
	public static InputBoxUtil getInstance(){
		if(instance == null){
			instance = new InputBoxUtil();
		}
		return instance;
	}


	/**
	 * 消息 文件通过base64传输
	 * @param msg
	 * @return
	 */
	public Msg getSendMsg(Msg msg) {
		if(msg.getContentType() == Msg.MSG_TYPE_TEXT){
			return msg;
		}
		Msg m = null;
		try {
			m = (Msg) msg.clone();
			m.setBody(FileCode.getInstance().file2Base64Str(msg.getBody()));
		} catch (CloneNotSupportedException e) {
			m = msg;
			e.printStackTrace();
		}
		return m;
	}

	/**
	 * 消息 文件传到服务器
	 * @param msg
	 * @return
	 */
	public void prepareSendMsg(final Msg msg){
		final String fileLocalUrl = msg.getBody();
		switch (msg.getContentType()){
			case Msg.MSG_TYPE_TEXT:
				EventBus.getDefault().post(new IMSendMsgEvent(msg));
				break;
			case Msg.MSG_TYPE_PIC:
				L.i("getHandledMsg","do pic upLoad...");
				new FileUpLoadTask(UrlHelper.FILE_URL,fileLocalUrl,new UpdateViewHelper.UpdateViewListener(){
					@Override
					public void onComplete(Object obj) {
						String filePath = StrUtils.parseUpLoadFileUrl(obj.toString());
						if(StrUtils.isEmpty(filePath)){
							L.e(TAG,"图片上传服务器失败...rsStr:"+obj.toString());
						}else{
							msg.setBody(filePath);
							EventBus.getDefault().post(new IMSendMsgEvent(msg,fileLocalUrl));
						}
					}
				});
				break;
			case Msg.MSG_TYPE_VOICE:
				new FileUpLoadTask(UrlHelper.UPLOAD_VOICE,msg.getBody(),new UpdateViewHelper.UpdateViewListener(){
					@Override
					public void onComplete(Object obj) {
						String filePath = StrUtils.parseUpLoadFileUrl(obj.toString());
						if(StrUtils.isEmpty(filePath)){
							L.e(TAG,"语音上传服务器失败...rsStr:"+obj.toString());
						}else{
							msg.setBody(filePath);
							EventBus.getDefault().post(new IMSendMsgEvent(msg,fileLocalUrl));
						}
					}
				});
				break;
			default:
				L.e("getHandledMsg", GsonUtil.getInstance().obj2Str(msg) + ":消息发送暂时只支持语音、图片！");
				break;
		}
	}
	/**
	 * 把输入的文本变为emoji表情
	 * @param mContext
	 * @param content
	 * @return
	 */
	public SpannableStringBuilder handler(Context mContext,String content) {
		SpannableStringBuilder sb = new SpannableStringBuilder(content);
		String regex = "(\\[em:b)\\d{2}(\\])";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
				String png = tempText.substring("[em:".length(),
						tempText.length() - "]".length());
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append("face/bl/");
				sBuilder.append(png);
				sBuilder.append(".png");
				try {
					sb.setSpan(
							new ImageSpan(mContext, BitmapFactory
									.decodeStream(mContext.getAssets()
											.open(sBuilder.toString()))), m.start(), m.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return sb;
	}
	/**
	 * 输入的话题文本变为emoji表情  并把话题标签变为橙色
	 * @param points
	 * @return
	 */
	public SpannableStringBuilder changeTextColor(Context context,String content) {
		List<String> result = StrUtils.getTags(content);
		List<String> starts = StrUtils.getTagsPosition(content);
		SpannableStringBuilder ss = new SpannableStringBuilder(content);
		if (result.size() > 0 && starts.size() > 0
				&& result.size() == starts.size()) {
			for (int i = 0; i < result.size(); i++) {
				String tag = ("#" + result.get(i) + "#");
				ss.setSpan(new ForegroundColorSpan(Color.argb(255,
						243, 150, 51)), Integer.parseInt(starts.get(i)),
						(Integer.parseInt(starts.get(i))+tag.length()),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		String regex = "(\\[em:b)\\d{2}(\\])";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(content);
		while (m.find()) {
			String tempText = m.group();
				String png = tempText.substring("[em:".length(),
						tempText.length() - "]".length());
				StringBuilder sBuilder = new StringBuilder();
				sBuilder.append("face/bl/");
				sBuilder.append(png);
				sBuilder.append(".png");
				try {
					ss.setSpan(
							new ImageSpan(context, BitmapFactory
									.decodeStream(context.getAssets()
											.open(sBuilder.toString()))), m.start(), m.end(),
							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
		}
		return ss;
	}
	public SpannableStringBuilder changeTextColor(String content) {
		List<String> result = StrUtils.getTags(content);
		List<String> starts = StrUtils.getTagsPosition(content);
		SpannableStringBuilder ss = new SpannableStringBuilder(content);
		if (result.size() > 0 && starts.size() > 0
				&& result.size() == starts.size()) {
			for (int i = 0; i < result.size(); i++) {
				String tag = ("#" + result.get(i) + "#");
				ss.setSpan(new ForegroundColorSpan(Color.parseColor("#ff0909")), Integer.parseInt(starts.get(i)),
						(Integer.parseInt(starts.get(i))+tag.length()),
						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
			}
		}
		return ss;
	}
}
