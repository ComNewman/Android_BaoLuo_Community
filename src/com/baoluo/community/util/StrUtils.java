package com.baoluo.community.util;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.TextAppearanceSpan;
import android.widget.EditText;

import com.baoluo.community.core.Configs;

public class StrUtils {

	private static final String TAG = "StrUtils";

	/**
	 * 判断字符串是否为url
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isUrl(String str) {
		String regex = "((http://)?([a-z]+[.])|(www.))\\w+[.]([a-z]{2,4})?[[.]([a-z]{2,4})]+((/[\\S&&[^,;\u4E00-\u9FA5]]+)+)?([.][a-z]{2,4}+|/?)";
		Pattern pattern = Pattern.compile(regex);
		Matcher isUrl = pattern.matcher(str);
		return isUrl.matches();
	}
	
	public static boolean isPhoneNum(String str){
		String regex = "^[1]([3][0-9]{1}|59|58|88|89)[0-9]{8}$";
		Pattern p = Pattern.compile(regex);
		Matcher isPhoneNum = p.matcher(str);
		return isPhoneNum.matches();
	}
	
	/**
	 * 去掉空格 和 换行
	 * @return
	 */
	public static String replaceBlank(String str) {
		String rs = str;
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			rs = m.replaceAll("");
		}else{
			rs = "";
		}
		return rs;
	}

	public static boolean isEmpty(String str) {
		if (str == null || str.equals("")) {
			return true;
		}
		return false;
	}

	public static String checkEditText(EditText editText) {
		if (editText != null && editText.getText() != null
				&& !(editText.getText().toString().trim().equals(""))) {
			return editText.getText().toString().trim();
		} else {
			return "";
		}
	}

	public static boolean isNumeric(String str) {
		for (int i = 0; i < str.length(); i++) {
			System.out.println(str.charAt(i));
			if (!Character.isDigit(str.charAt(i))) {
				return false;
			}
		}
		return true;
	}

	/**
	 * @param str
	 *            解析上传图片后返回的json
	 * @return 图片地址
	 */
	public static String parseImgBackUrl(String str) {
		// String str =
		// "[{'ret':true,'info':{'url':'http://img.baoluo.dev/a2037a80c951574a5ff6406016e4b010'}}]";
		if (isEmpty(str)) {
			return "";
		}
		try {
			JSONArray array = new JSONArray(str);
			if (array.length() > 0) {
				JSONObject obj = (JSONObject) array.get(0);
				boolean ret = obj.getBoolean("ret");
				if (ret) {
					return obj.getJSONObject("info").getString("url");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static String parseUpLoadFileUrl(String str){
		if(isEmpty(str)){
			return "";
		}
		try {
			JSONArray array = new JSONArray(str);
			if (array.length() > 0) {
				JSONObject obj = (JSONObject) array.get(0);
				boolean ret = obj.getBoolean("ret");
				if (ret) {
					return obj.getJSONObject("info").getString("url");
				}
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 获取标签 在content中 开始的位置
	 * 
	 * @param s
	 * @return
	 */
	public static List<String> getTagsPosition(String s) {
		List<String> starts = new ArrayList<String>();
		Pattern p = Pattern.compile("#([\\w]*)#");
		Matcher m = p.matcher(s);
		while (!m.hitEnd() && m.find()) {
			starts.add(String.valueOf(m.start()));
		}
		return starts;
	}

	/**
	 * 获取话题中的标签
	 * 
	 * @param s
	 * @return
	 */
	public static List<String> getTags(String s) {
		List<String> results = new ArrayList<String>();
		Pattern p = Pattern.compile("#([\\w]*)#");
		Matcher m = p.matcher(s);
		while (!m.hitEnd() && m.find()) {
			results.add(m.group(1));
		}
		return results;
	}
//	public static SpannableStringBuilder handler(Context mContext,String content) {
//	SpannableStringBuilder sb = new SpannableStringBuilder(content);
//	L.i(TAG, "content: " + content);
//	String regex = "(\\[em:b)\\d{2}(\\])";
//	Pattern p = Pattern.compile(regex);
//	Matcher m = p.matcher(content);
//	while (m.find()) {
//		String tempText = m.group();
//			String png = tempText.substring("[em:".length(),
//					tempText.length() - "]".length());
//			L.i(TAG, "png: " + png);
//			StringBuilder sBuilder = new StringBuilder();
//			sBuilder.append("face/bl/");
//			sBuilder.append(png);
//			sBuilder.append(".png");
//			L.i(TAG, "sBuilder.toString(): " + sBuilder.toString());
//			try {
//				sb.setSpan(
//						new ImageSpan(mContext, BitmapFactory
//								.decodeStream(mContext.getAssets()
//										.open(sBuilder.toString()))), m.start(), m.end(),
//						Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//			} catch (IOException e1) {
//				e1.printStackTrace();
//			}
//	}
//	return sb;
//}
	/**
	 * 改变# # 为橙色
	 * 
	 * @param item
	 * @return
	 */
//	public static SpannableStringBuilder changeTextColor(Context context,String content) {
//		List<String> result = StrUtils.getTags(content);
//		List<String> starts = StrUtils.getTagsPosition(content);
//		SpannableStringBuilder ss = new SpannableStringBuilder(content);
//		if (result.size() > 0 && starts.size() > 0
//				&& result.size() == starts.size()) {
//			for (int i = 0; i < result.size(); i++) {
//				String tag = ("#" + result.get(i) + "#");
//				ss.setSpan(new ForegroundColorSpan(Color.argb(255,
//						243, 150, 51)), Integer.parseInt(starts.get(i)),
//						(Integer.parseInt(starts.get(i))+tag.length()),
//						Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//			}
//		}
//		String regex = "(\\[em:b)\\d{2}(\\])";
//		Pattern p = Pattern.compile(regex);
//		Matcher m = p.matcher(content);
//		while (m.find()) {
//			String tempText = m.group();
//				String png = tempText.substring("[em:".length(),
//						tempText.length() - "]".length());
//				L.i(TAG, "png: " + png);
//				StringBuilder sBuilder = new StringBuilder();
//				sBuilder.append("face/bl/");
//				sBuilder.append(png);
//				sBuilder.append(".png");
//				L.i(TAG, "sBuilder.toString(): " + sBuilder.toString());
//				try {
//					ss.setSpan(
//							new ImageSpan(context, BitmapFactory
//									.decodeStream(context.getAssets()
//											.open(sBuilder.toString()))), m.start(), m.end(),
//							Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//				} catch (IOException e1) {
//					e1.printStackTrace();
//				}
//		}
//		return ss;
//	}
	
	public static SpannableStringBuilder setPointColor(String points,String color) {
		ColorStateList redColors = ColorStateList.valueOf(Color
				.parseColor(color));
		SpannableStringBuilder spanBuilder = new SpannableStringBuilder(points);
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(points);
		String num =  m.replaceAll("").trim();
		int start = points.indexOf(num);
		if(start != 0 && !num.equals("")){
			spanBuilder.setSpan(
					new TextAppearanceSpan(null, 0, 0, redColors, null), start,
					start + num.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
		}
		return spanBuilder;
	}
	


	/**
	 * List<String> 转 StringArray
	 * 
	 * @return
	 */
	public static String[] getStringArray() {
		List<String> list = new ArrayList<String>();
		String[] strArr = new String[list.size()];
		list.toArray(strArr);
		return strArr;
	}

	public static String byte2Str(byte[] data) {
		try {
			return new String(data, Configs.CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return null;
		}
	}

	public static byte[] Str2Byte(String str) {
		try {
			return str.getBytes(Configs.CHAR_SET);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static String StrCode(String str) {
		try {
			return new String(str.getBytes(Configs.CHAR_SET));
		} catch (UnsupportedEncodingException e) {
			L.e(TAG, "解析错误");
			e.printStackTrace();
			return str;
		}
	}
	
	public static String getUnReadNum(int num){
		if(num > 99){
			return "99+";
		}
		return num + "";
	}
}
