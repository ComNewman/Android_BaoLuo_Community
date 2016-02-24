package com.baoluo.community.util;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
/**
 * EditText	字符控制帮助类
 * @author xiangyang.fu
 *
 */
public class InputFilterHelp implements InputFilter {
	
	private int maxLen;
	private Context mContext;
	public InputFilterHelp(Context mContext,int maxLen) {
		this.maxLen = maxLen;
		this.mContext =mContext;
	}

	@Override
	public CharSequence filter(CharSequence src, int start, int end,
			Spanned dest, int dstart, int dend) {
		int dindex = 0;
		int count = 0;

		while (count <= maxLen && dindex < dest.length()) {
			char c = dest.charAt(dindex++);
			if (c < 128) {
				count = count + 1;
			} else {
				count = count + 2;
			}
		}

		if (count > maxLen) {
			T.showShort(mContext, "超过字数限制！");
			return dest.subSequence(0, dindex - 1);
		}

		int sindex = 0;
		while (count <= maxLen && sindex < src.length()) {
			char c = src.charAt(sindex++);
			if (c < 128) {
				count = count + 1;
			} else {
				count = count + 2;
			}
		}

		if (count > maxLen) {
			T.showShort(mContext, "超过字数限制！");
			sindex--;
		}

		return src.subSequence(0, sindex);
	}

}
