package com.baoluo.community.ui.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoluo.community.ui.R;

/**
 * 聊天语音
 * 
 * @author tao.lai
 * 
 */
@SuppressLint("NewApi")
public class SpeekView extends LinearLayout {
	private static final String TAG = "SpeekView";
	
	private View view;
	private ImageView iv;
	private TextView tv;
	private AnimationDrawable animationDrawable;

	public SpeekView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.view_speek, null);
		addView(view);

        iv = (ImageView) view.findViewById(R.id.mic_image);
        tv = (TextView) view.findViewById(R.id.recording_hint);
		init();
	}

	public SpeekView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public SpeekView(Context context) {
		this(context, null, 0);
	}
	
	private void init(){
		animationDrawable = (AnimationDrawable) iv.getBackground();
		animationDrawable.setOneShot(false);
	}
	
	public TextView getRecordHint(){
		return this.tv;
	}
	
	public void startAnimation(){
		animationDrawable.start();
	}
	
	public boolean animationIsRuning(){
		return animationDrawable.isRunning();
	}
	
	public void stopAnimation(){
		animationDrawable.stop();
	}

}
