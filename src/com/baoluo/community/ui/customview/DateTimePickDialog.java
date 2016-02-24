package com.baoluo.community.ui.customview;

import java.lang.reflect.Field;
import java.util.Calendar;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.DatePicker.OnDateChangedListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.TimePicker.OnTimeChangedListener;

import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.community.util.TimeUtil;

public class DateTimePickDialog extends Dialog implements OnDateChangedListener,
        OnTimeChangedListener,View.OnClickListener {
    private static final String TAG = "DateTimePickDialog";

    private TextView timeTitle;
    private DatePicker datePicker;
    private TimePicker timePicker;
    private Button btnEnsure,btnCancel;
    private TextView tvInput;

    private Context ctx;
    private String formatStr;
    private Calendar tempCalendar,calendar;

    public DateTimePickDialog(Context ctx,Calendar calendar,String formatStr,TextView tvInput) {
        super(ctx);
        this.ctx = ctx;
        this.formatStr = formatStr;
        this.tvInput = tvInput;
        if (calendar != null) {
            this.calendar = calendar;
            this.tempCalendar = calendar;
        }else{
            this.calendar = Calendar.getInstance();
            this.tempCalendar = Calendar.getInstance();
        }
        this.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_date_time_pick);
        datePicker = (DatePicker)findViewById(R.id.datepicker);
        timePicker = (TimePicker)findViewById(R.id.timepicker);
        timeTitle = (TextView) findViewById(R.id.tv_time_title);
        btnEnsure = (Button) findViewById(R.id.btn_ensure);
        btnCancel = (Button) findViewById(R.id.btn_cancel);
        initDateTimePicker();
        TextPaint tp = timeTitle.getPaint();
        tp.setFakeBoldText(true);

        btnEnsure.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        timeTitle.setText(TimeUtil.getFormat(formatStr).format(calendar.getTime()));
    }

    private void initDateTimePicker(){
        ((LinearLayout) ((ViewGroup) datePicker.getChildAt(0))
                .getChildAt(0))
                .getChildAt(0).setVisibility(View.GONE);
        calendar.set(Calendar.MINUTE, getSelMin(calendar.get(Calendar.MINUTE)));
        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
        timePicker.setCurrentHour(calendar.get(Calendar.HOUR_OF_DAY));
        timePicker.setCurrentMinute(calendar.get(Calendar.MINUTE));
        timePicker.setIs24HourView(true);
        timePicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_ensure:
                onDateChanged(null, 0, 0, 0);
                tvInput.setText(TimeUtil.getFormat(formatStr).format(tempCalendar.getTime()));
                DateTimePickDialog.this.dismiss();
                break;
            case R.id.btn_cancel:
                tvInput.setText(TimeUtil.getFormat(formatStr).format(calendar.getTime()));
                DateTimePickDialog.this.dismiss();
                break;
        }
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        try {
            Field mMinutePicker = view.getClass().getDeclaredField("mMinuteSpinner");
            mMinutePicker.setAccessible(true);
            Object value = mMinutePicker.get(view);

            Field mCurrent = value.getClass().getDeclaredField("mValue");
            mCurrent.setAccessible(true);

            Field mTextView = value.getClass().getDeclaredField("mInputText");
            mTextView.setAccessible(true);

            Field mCalendar = view.getClass().getDeclaredField("mTempCalendar");
            mCalendar.setAccessible(true);
            Calendar c = (Calendar) mCalendar.get(view);

            int selMin = getSelMin(minute);
            mCurrent.set(value, selMin);

            c.set(Calendar.MINUTE, selMin);
            mCalendar.set(view, c);

            mCurrent.set(value, selMin);
            c.set(Calendar.MINUTE, selMin);
            mCalendar.set(view, c);

            EditText et = (EditText) mTextView.get(value);
            et.setText(c.get(Calendar.MINUTE) + "");
            mTextView.set(value, et);

            if(timePicker.getCurrentMinute() == 60){
                timePicker.setCurrentMinute(0);
                timePicker.setCurrentHour(timePicker.getCurrentHour() + 1);
            }
            refreshTimeTitle();
        } catch (Exception e) {
            L.e(TAG, "timeChangedListener error");
            e.printStackTrace();
        }
    }

    public static int getSelMin(int minute) {
        String str = minute + "";
        if (str.endsWith("9")) {
            minute -= 9;
        } else if (str.endsWith("1")) {
            minute += 9;
        } else {
            float a = (float) minute;
            float b = 10f;
            return (int) Math.ceil((a / b)) * 10;
        }
        return minute;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        refreshTimeTitle();
    }

    private void refreshTimeTitle(){
        tempCalendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth(), timePicker.getCurrentHour(),
                timePicker.getCurrentMinute());
        timeTitle.setText(TimeUtil.getFormat(formatStr).format(tempCalendar.getTime()));
    }
}