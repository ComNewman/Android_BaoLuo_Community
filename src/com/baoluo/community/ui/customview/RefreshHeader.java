package com.baoluo.community.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.ui.R;

public class RefreshHeader extends RelativeLayout{

	public RefreshHeader(Context context){
		super(context);
		initView(context);
	}
	
	public RefreshHeader(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView(context);
	}
	
	private ImageView mHeaderImageView;
	private TextView mHeaderTextView,mHeaderUpdateTextView;
	private ProgressBar mHeaderProgressBar;
	
	private void initView(Context context){
		View.inflate(context, R.layout.refresh_header, this);
		mHeaderImageView = (ImageView) findViewById(R.id.pull_to_refresh_image);
		mHeaderTextView = (TextView)findViewById(R.id.pull_to_refresh_text);
		mHeaderUpdateTextView = (TextView)findViewById(R.id.pull_to_refresh_updated_at);
		mHeaderProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_progress);
	}
	
	

}
