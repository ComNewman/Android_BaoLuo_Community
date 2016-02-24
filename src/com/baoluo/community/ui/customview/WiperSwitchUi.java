package com.baoluo.community.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

import com.baoluo.community.ui.R;

/**
 * 自定义选择开关
 * 
 * @author Ryan_Fu 2015-6-1
 */
public class WiperSwitchUi extends View implements OnTouchListener {
	private Bitmap bg_on, bg_off, slipper_btn;
	private float downX, nowX;

	private boolean onSlip = false;

	private boolean nowStatus = false;

	private OnChangedListener listener;

	public WiperSwitchUi(Context context) {
		super(context);
		init();
	}

	public WiperSwitchUi(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public void init() {
		bg_on = BitmapFactory.decodeResource(getResources(), R.drawable.on_btn);
		bg_off = BitmapFactory.decodeResource(getResources(),
				R.drawable.off_btn);
		slipper_btn = BitmapFactory.decodeResource(getResources(),
				R.drawable.white_btn);

		setOnTouchListener(this);
	}

	@SuppressLint("DrawAllocation")
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Matrix matrix = new Matrix();
		Paint paint = new Paint();
		float x = 0;

		if (nowX < (bg_on.getWidth() / 2)) {
			canvas.drawBitmap(bg_off, matrix, paint);
		} else {
			canvas.drawBitmap(bg_on, matrix, paint);
		}

		if (onSlip) {
			if (nowX >= bg_on.getWidth())
				x = bg_on.getWidth() - slipper_btn.getWidth() / 2;
			else
				x = nowX - slipper_btn.getWidth() / 2;
		} else {
			if (nowStatus) {
				x = bg_on.getWidth() - slipper_btn.getWidth();
			} else {
				x = 0;
			}
		}

		if (x < 0) {
			x = 0;
		} else if (x > bg_on.getWidth() - slipper_btn.getWidth()) {
			x = bg_on.getWidth() - slipper_btn.getWidth();
		}

		canvas.drawBitmap(slipper_btn, x, 0, paint);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN: {
			if (event.getX() > bg_off.getWidth()
					|| event.getY() > bg_off.getHeight()) {
				return false;
			} else {
				onSlip = true;
				downX = event.getX();
				nowX = downX;
			}
			break;
		}
		case MotionEvent.ACTION_MOVE: {
			nowX = event.getX();
			break;
		}
		case MotionEvent.ACTION_UP: {
			onSlip = false;
			if (event.getX() >= (bg_on.getWidth() / 2)) {
				nowStatus = true;
				nowX = bg_on.getWidth() - slipper_btn.getWidth();
			} else {
				nowStatus = false;
				nowX = 0;
			}

			if (listener != null) {
				listener.OnChanged(WiperSwitchUi.this, nowStatus);
			}
			break;
		}
		}
		invalidate();
		return true;
	}

	public void setOnChangedListener(OnChangedListener listener) {
		this.listener = listener;
	}

	public void setChecked(boolean checked) {
		if (checked) {
			nowX = bg_off.getWidth();
		} else {
			nowX = 0;
		}
		nowStatus = checked;
	}

	public interface OnChangedListener {
		public void OnChanged(WiperSwitchUi wiperSwitch, boolean checkState);
	}

}
