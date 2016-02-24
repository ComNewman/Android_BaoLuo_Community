package com.baoluo.community.support.topicCloud;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Canvas;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.baoluo.community.entity.Topic;
import com.baoluo.community.ui.TopicDetailsActivity;
import com.baoluo.community.util.L;

public class TopicCloudView extends RelativeLayout {
	private static final String TAG = "TopicCloudView";
	
	private static final int textColor = 0xff666666;

	RelativeLayout navigation_bar;
	TextView mTextView1;

	public TopicCloudView(Context mContext) {
		super(mContext);
	}
	
	public TopicCloudView(Context mContext, int width, int height,
			List<Topic> topicList) {
		this(mContext, width, height, topicList, 0,0);
	}

	public TopicCloudView(Context mContext, int width, int height,
			List<Topic> tagList, int offsetX, int offsetY) {
		this(mContext, width, height, tagList, 6, 34, 1, offsetX, offsetY); // default
																			// for
																			// min/max
	}

	public TopicCloudView(Context mContext, int width, int height,
			List<Topic> tagList, int textSizeMin, int textSizeMax,
			int scrollSpeed) {
		this(mContext, width, height, tagList, textSizeMin, textSizeMax,
				scrollSpeed, 0, 0);
	}

	public TopicCloudView(Context mContext, int width, int height,
			List<Topic> tagList, int textSizeMin, int textSizeMax,
			int scrollSpeed, int offsetX, int offsetY) {
		super(mContext);
		this.mContext = mContext;
		this.textSizeMin = textSizeMin;
		this.textSizeMax = textSizeMax;
		this.offsetX = offsetX;
		this.offsetY = offsetY;

		tspeed = scrollSpeed;

		centerX = width / 2;
		centerY = height / 2;
		radius = Math.min(centerX * 0.95f, centerY * 0.95f); // use 95% of
																// screen
		shiftLeft = (int) (Math.min(centerX * 0.15f, centerY * 0.15f));
		mTagCloud = new TopicCloud(Filter(tagList), (int) radius, textSizeMin,
				textSizeMax);
		float[] tempColor1 = { 0.9412f, 0.7686f, 0.2f, 1 }; // rgb Alpha
		float[] tempColor2 = { 1f, 0f, 0f, 1 }; // rgb Alpha
												// {0f,0f,1f,1} blue
												// {0.1294f,0.1294f,0.1294f,1}
												// grey
												// {0.9412f,0.7686f,0.2f,1}
												// light orange
		mTagCloud.setTagColor1(tempColor1);// higher color
		mTagCloud.setTagColor2(tempColor2);// lower color
		mTagCloud.setRadius((int) radius);
		mTagCloud.create(true); // to put each Topic at its correct initial
								// location
		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		mTextView = new ArrayList<TextView>();
		mParams = new ArrayList<RelativeLayout.LayoutParams>();
		Iterator<Topic> it = mTagCloud.iterator();
		Topic tempTag;
		int i = 0;

		while (it.hasNext()) {
			tempTag = (Topic) it.next();
			tempTag.setParamNo(i); // store the parameter No. related to this
									// tag
			mTextView.add(new TextView(this.mContext));
			mTextView.get(i).setText(tempTag.getText());

			mParams.add(new RelativeLayout.LayoutParams(
					LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
			mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
			mParams.get(i)
					.setMargins(
							(int) (centerX - shiftLeft + offsetX + tempTag
									.getLoc2DX()),
							(int) (centerY + offsetY + tempTag.getLoc2DY()), 0,
							0);
			mTextView.get(i).setLayoutParams(mParams.get(i));
			mTextView.get(i).setSingleLine(true);
			/*int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
					(int) (tempTag.getColorR() * 255),
					(int) (tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));*/
			mTextView.get(i).setTextColor(textColor);
			mTextView.get(i).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			addView(mTextView.get(i));
			mTextView.get(i).setOnClickListener(
					OnTagClickListener(tempTag.getId()));
			i++;
		}
	}
	
	private ScrollView mScrollView;
	public void setScrollView(ScrollView mScrollView){
		this.mScrollView = mScrollView;
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
	}

	public void addTag(Topic newTag) {
		mTagCloud.add(newTag);

		int i = mTextView.size();
		newTag.setParamNo(i);

		mTextView.add(new TextView(this.mContext));
		mTextView.get(i).setText(newTag.getText());

		mParams.add(new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
				LayoutParams.WRAP_CONTENT));
		mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_LEFT);
		mParams.get(i).addRule(RelativeLayout.ALIGN_PARENT_TOP);
		mParams.get(i).setMargins(
				(int) (centerX - shiftLeft + offsetX + newTag.getLoc2DX()),
				(int) (centerY + offsetY + newTag.getLoc2DY()), 0, 0);
		mTextView.get(i).setLayoutParams(mParams.get(i));

		mTextView.get(i).setSingleLine(true);
		/*int mergedColor = Color.argb((int) (newTag.getAlpha() * 255),
				(int) (newTag.getColorR() * 255),
				(int) (newTag.getColorG() * 255),
				(int) (newTag.getColorB() * 255));*/
		mTextView.get(i).setTextColor(textColor);
		mTextView.get(i).setTextSize(
				(int) (newTag.getTextSize() * newTag.getScale()));
		addView(mTextView.get(i));
		mTextView.get(i).setOnClickListener(OnTagClickListener(newTag.getId()));
	}

	public boolean Replace(Topic newTag, String oldTagText) {
		boolean result = false;
		int j = mTagCloud.Replace(newTag, oldTagText);
		if (j >= 0) { 
			Iterator<Topic> it = mTagCloud.iterator();
			Topic tempTag;
			while (it.hasNext()) {
				tempTag = (Topic) it.next();
				mParams.get(tempTag.getParamNo()).setMargins(
						(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
						(int) (centerY + tempTag.getLoc2DY()), 0, 0);
				mTextView.get(tempTag.getParamNo()).setText(tempTag.getText());
				mTextView.get(tempTag.getParamNo()).setTextSize(
						(int) (tempTag.getTextSize() * tempTag.getScale()));
				/*int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
						(int) (tempTag.getColorR() * 255),
						(int) (tempTag.getColorG() * 255),
						(int) (tempTag.getColorB() * 255));*/
				mTextView.get(tempTag.getParamNo()).setTextColor(textColor);
				mTextView.get(tempTag.getParamNo()).bringToFront();
			}
			result = true;
		}
		return result;
	}

	public void reset() {
		mTagCloud.reset();
		Iterator<Topic> it = mTagCloud.iterator();
		Topic tempTag;
		while (it.hasNext()) {
			tempTag = (Topic) it.next();
			mParams.get(tempTag.getParamNo()).setMargins(
					(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()), 0, 0);
			mTextView.get(tempTag.getParamNo()).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			/*int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
					(int) (tempTag.getColorR() * 255),
					(int) (tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));*/
			mTextView.get(tempTag.getParamNo()).setTextColor(textColor);
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
	}

	@Override
	public boolean onTrackballEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		Log.d(TAG, "using trackball");
		mAngleX = (y) * tspeed * TRACKBALL_SCALE_FACTOR;
		mAngleY = (-x) * tspeed * TRACKBALL_SCALE_FACTOR;

		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();

		Iterator<Topic> it = mTagCloud.iterator();
		Topic tempTag;
		while (it.hasNext()) {
			tempTag = (Topic) it.next();
			mParams.get(tempTag.getParamNo()).setMargins(
					(int) (centerX - shiftLeft + tempTag.getLoc2DX()),
					(int) (centerY + tempTag.getLoc2DY()), 0, 0);
			mTextView.get(tempTag.getParamNo()).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			/*int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
					(int) (tempTag.getColorR() * 255),
					(int) (tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));*/
			mTextView.get(tempTag.getParamNo()).setTextColor(textColor);
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
		return true;
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		boolean result = true;
		if (e.getAction() == MotionEvent.ACTION_MOVE) {
			onTouchEvent(e);
		} else {
			oldX = x;
			oldY = y;
		}
		result = super.dispatchTouchEvent(e);
		return result;
	}
	
	@SuppressLint("ClickableViewAccessibility")
	@Override
	public boolean onTouchEvent(MotionEvent e) {
		float x = e.getX();
		float y = e.getY();
		switch (e.getAction()) {
		case MotionEvent.ACTION_DOWN: // pressed
			oldX = x;
			oldY = y;
			mScrollView.requestDisallowInterceptTouchEvent(true);
			L.e(TAG, "requestDisallowInterceptTouchEvent        to true");
			break;
		case MotionEvent.ACTION_MOVE:
			float dx = x - oldX;
			float dy = y - oldY;
			oldX = x;
			oldY = y;
			mAngleX = (dy / radius) * tspeed * TOUCH_SCALE_FACTOR;
			mAngleY = (-dx / radius) * tspeed * TOUCH_SCALE_FACTOR;

			initCloud();
			break;
		case MotionEvent.ACTION_UP:
			//mScrollView.requestDisallowInterceptTouchEvent(false);
			L.e(TAG, "requestDisallowInterceptTouchEvent  to false");
			break;
		case MotionEvent.ACTION_CANCEL:
			mScrollView.requestDisallowInterceptTouchEvent(false);
		}
		return true;
	}
	
	private void initCloud(){
		mTagCloud.setAngleX(mAngleX);
		mTagCloud.setAngleY(mAngleY);
		mTagCloud.update();
		Iterator<Topic> it = mTagCloud.iterator();
		Topic tempTag;
		while (it.hasNext()) {
			tempTag = (Topic) it.next();
			mParams.get(tempTag.getParamNo())
					.setMargins(
							(int) (centerX - shiftLeft + offsetX + tempTag
									.getLoc2DX()),
							(int) (centerY + offsetY + tempTag.getLoc2DY()),
							0, 0);
			mTextView.get(tempTag.getParamNo()).setTextSize(
					(int) (tempTag.getTextSize() * tempTag.getScale()));
			/*int mergedColor = Color.argb((int) (tempTag.getAlpha() * 255),
					(int) (tempTag.getColorR() * 255),
					(int) (tempTag.getColorG() * 255),
					(int) (tempTag.getColorB() * 255));*/
			mTextView.get(tempTag.getParamNo()).setTextColor(textColor);
			mTextView.get(tempTag.getParamNo()).bringToFront();
		}
	}
	
	private float mDx = 0f;
	private float mDy = 0f;
	private boolean isAuto = true;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		public void run() {
			initStatus();
			handler.postDelayed(this, 100);// 间隔1秒
		}
	};

	private void initStatus() {
		if (!isAuto) {
			return;
		}
		mDx = mDx + 0.005f;
		mDy = mDy + 0.005f;
		if (mDx > 10f) {
			mDx = 0f;
		}
		if (mDy > 10f) {
			mDy = 0f;
		}
		mAngleX = (mDy / radius) * tspeed * TOUCH_SCALE_FACTOR;
		mAngleY = (-mDx / radius) * tspeed * TOUCH_SCALE_FACTOR;
		initCloud();
	}

	public void startAutoTopic() {
		handler.postDelayed(runnable, 100);
		L.e(TAG, "start autoTOpic");
	}

	public void topicAutoDestroy() {
		handler.removeCallbacks(runnable);
	}

	private List<Topic> Filter(List<Topic> tagList) {
		List<Topic> tempTagList = new ArrayList<Topic>();
		Iterator<Topic> itr = tagList.iterator();
		Iterator<Topic> itrInternal;
		Topic tempTag1, tempTag2;
		while (itr.hasNext()) {
			tempTag1 = (Topic) (itr.next());
			boolean found = false;
			itrInternal = tempTagList.iterator();
			while (itrInternal.hasNext()) {
				tempTag2 = (Topic) (itrInternal.next());
				if (tempTag2.getText().equalsIgnoreCase(tempTag1.getText())) {
					found = true;
					break;
				}
			}
			if (found == false)
				tempTagList.add(tempTag1);
		}
		return tempTagList;
	}

	private View.OnClickListener OnTagClickListener(final String id) {
		return new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent it = new Intent();
				it.setClass(mContext, TopicDetailsActivity.class);
				it.putExtra("id", id);
				mContext.startActivity(it);
			}
		};
	}
	
	private final float TOUCH_SCALE_FACTOR = 30.8f;
	private final float TRACKBALL_SCALE_FACTOR = 50;
	private float tspeed;
	private TopicCloud mTagCloud;
	private float mAngleX = 0;
	private float mAngleY = 0;
	private float centerX, centerY;
	private float offsetX, offsetY;
	private float oldX, oldY;
	private float radius;
	private Context mContext;
	private int textSizeMin, textSizeMax;
	private List<TextView> mTextView;
	private List<RelativeLayout.LayoutParams> mParams;
	private int shiftLeft;
}
