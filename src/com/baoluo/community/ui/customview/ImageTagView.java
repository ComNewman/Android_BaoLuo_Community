package com.baoluo.community.ui.customview;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.ui.R;

/**
 * 图片上可移动标签View
 * @author tao.lai
 *
 */
public class ImageTagView extends RelativeLayout{

	private Context mContext;
	private List<View> mTagViewList;
	private List<TagInfo> tagList;

	public ImageTagView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public ImageTagView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public ImageTagView(Context context) {
		super(context);
	}
	public void addTag(String tagName, final int dx, final int dy) {
		if (mTagViewList == null){
			mTagViewList = new ArrayList<View>();
			tagList = new ArrayList<TagInfo>();
		}
		TagInfo tag = new TagInfo();
		tag.setName(tagName);
		LayoutInflater mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view = mInflater.inflate(R.layout.image_tag, null);
		TextView text = (TextView) view.findViewById(R.id.tv_tag_text);
		final RelativeLayout layout = (RelativeLayout) view.findViewById(R.id.rl_tag);
		text.setText(tagName);
		layout.setOnTouchListener(new MyOnTouchListener(tag));
		this.addView(layout);
		layout.setVisibility(View.INVISIBLE);
		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				setPosition(layout, dx, dy);
			}
		}, 100);
		mTagViewList.add(layout);
		initTagXY(tag,dx,dy);
		tagList.add(tag);
	}
	
	public class MyOnTouchListener implements OnTouchListener{
		private TagInfo tag;
		public MyOnTouchListener(TagInfo tag){
			this.tag = tag;
		}
		int mDownInScreenX = 0;
		int mDownInScreenY = 0;
		@SuppressLint("ClickableViewAccessibility")
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			if (v.getId() == R.id.rl_tag) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					mDownInScreenX = (int) event.getRawX();
					mDownInScreenY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int x = (int) event.getRawX();
					int y = (int) event.getRawY();
					int mx = x - mDownInScreenX;
					int my = y - mDownInScreenY;
					move(v, tag, mx, my);
					mDownInScreenX = (int) event.getRawX();
					mDownInScreenY = (int) event.getRawY();
					break;
					default:break;
				}
			}
			return true;
		}
	}
	private void deleteTag(View v) {
		this.removeView(v);
		if (mTagViewList.size() != 0) {
			mTagViewList.remove(v);
		}
	}
	public void removeAll(){
		removeAllViews();
	}
	private void setPosition(View v, int dx, int dy) {
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();
		int l, t, r, b;
		if ((parentWidth - dx) >= v.getWidth()) {
			l = dx;
			r = l + v.getWidth();
		} else {
			r = parentWidth;
			l = parentWidth - v.getWidth();
		}
		if ((parentHeight - dy) >= v.getHeight()) {
			t = dy;
			b = l + v.getHeight();
		} else {
			b = parentHeight;
			t = parentHeight - v.getHeight();
		}
		v.layout(l, t, r, b);
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) v.getLayoutParams();
		params.leftMargin = l;
		params.topMargin = t;
		v.setLayoutParams(params);
		v.setVisibility(View.VISIBLE);
	}
	
	private void move(View v,TagInfo tag, int dx, int dy) {
		int parentWidth = this.getWidth();
		int parentHeight = this.getHeight();
		int l = v.getLeft() + dx;
		int t = v.getTop() + dy;
		if (l < 0) {
			l = 0;
		} else if ((l + v.getWidth()) >= parentWidth) {
			l = parentWidth - v.getWidth();
		}
		if (t < 0) {
			t = 0;
		} else if ((t + v.getHeight()) >= parentHeight) {
			t = parentHeight - v.getHeight();
		}
		int r = l + v.getWidth();
		int b = t + v.getHeight();
		v.layout(l, t, r, b);
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) v.getLayoutParams();
		params.leftMargin = l;
		params.topMargin = t;
		v.setLayoutParams(params);
		v.setVisibility(View.VISIBLE);
		
		TextView text = (TextView) v.findViewById(R.id.tv_tag_text);
        if(l*2 > parentWidth){
        	text.setBackgroundResource(R.drawable.bg_tag_left);
        }else{
        	text.setBackgroundResource(R.drawable.bg_tag_right);
        }
		initTagXY(tag,l,t);
	}
	
	private void initTagXY(TagInfo tag,int left,int top){
		int w = this.getWidth();
		int h = this.getHeight();
		
		tag.setX((double)left/w);
		tag.setY((double)top/h);
	}
	
	public List<TagInfo> getTagList(){
		return this.tagList;
	}
}
