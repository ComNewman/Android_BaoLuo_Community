package com.baoluo.community.ui.customview;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

@SuppressLint("NewApi")
public class DatePick extends DatePickerDialog {

	public DatePick(Context context, OnDateSetListener callBack, int year,
			int monthOfYear, int dayOfMonth) {
		super(context, callBack, year, monthOfYear, dayOfMonth);
		((ViewGroup) ((ViewGroup) this.getDatePicker().getChildAt(0))
				.getChildAt(0)).getChildAt(0).setVisibility(View.GONE);
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int month, int day) {
		super.onDateChanged(view, year, month, day);
	}

}
