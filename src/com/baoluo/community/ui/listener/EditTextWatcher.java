package com.baoluo.community.ui.listener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import com.baoluo.community.util.T;

/**
 * 输入框 字数监听
 * 
 * @author tao.lai
 * 
 */
public class EditTextWatcher {

	private Context ctx;
	private TextWatcher textWatcher;
	private EditText mEdit;
	private int sizeLimit;
	private boolean overTop = false;

	public EditTextWatcher(Context ctx, EditText mEdit, int sizeLimit) {
		this.ctx = ctx;
		this.mEdit = mEdit;
		this.sizeLimit = sizeLimit;
	}

	public boolean getOverTop() {
		return this.overTop;
	}

	public TextWatcher getTextWatcher() {
		textWatcher = new TextWatcher() {
			private CharSequence temp;
			private int editStart;
			private int editEnd;

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				editStart = mEdit.getSelectionStart();
				editEnd = mEdit.getSelectionEnd();
				if (temp.length() > sizeLimit) {
					overTop = true;
					s.delete(editStart - 1, editEnd);
					int tempSelection = editStart;
					mEdit.setText(s);
					mEdit.setSelection(tempSelection);
					T.showShort(ctx, "字数最多不能超过" + sizeLimit);
				}
			}
		};
		return textWatcher;
	}

	public interface OverTopListener {
		public void editOverTop();

		public void editLess();
	}
}
