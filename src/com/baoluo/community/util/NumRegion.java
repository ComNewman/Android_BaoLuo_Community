package com.baoluo.community.util;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * editText 数字输入范围
 * 
 * @author tao.lai
 * 
 */
public class NumRegion {
	private Context ctx;
	private int minValue;
	private int maxValue;

	public NumRegion(Context ctx, int minValue, int maxValue) {
		this.ctx = ctx;
		this.minValue = minValue;
		this.maxValue = maxValue;
	}

	public void setRegion(final EditText et) {
		et.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				if (start > 1) {
					if (minValue != -1 && maxValue != -1) {
						int num = Integer.parseInt(s.toString());
						if (num > maxValue) {
							s = String.valueOf(maxValue);
							et.setText(s);
						} else if (num < minValue)
							s = String.valueOf(minValue);
						return;
					}
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s != null && !s.equals("")) {
					if (minValue != -1 && maxValue != -1) {
						int markVal = 0;
						try {
							markVal = Integer.parseInt(s.toString());
						} catch (NumberFormatException e) {
							markVal = 0;
						}
						if (markVal > maxValue) {
							T.showShort(ctx, "不能超过" + maxValue);
							et.setText(String.valueOf(maxValue));
						}
						return;
					}
				}
			}
		});
	}
}
