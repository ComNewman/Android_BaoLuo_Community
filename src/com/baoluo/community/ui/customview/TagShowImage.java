package com.baoluo.community.ui.customview;

import java.util.List;

import android.content.Context;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.ui.R;

/**
 * 标签显示在图片上
 * 
 * @author tao.lai
 * 
 */
public class TagShowImage extends RelativeLayout {

	private Context mContext;
	private List<TagInfo> tagList;

	public TagShowImage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.mContext = context;
	}

	public TagShowImage(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
	}

	public TagShowImage(Context context) {
		super(context);
	}

	public void setTagList(List<TagInfo> tagList) {
		this.tagList = tagList;
	}

	public void initTags() {
		LayoutInflater mInflater = (LayoutInflater) mContext
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View view;
		if (tagList != null && tagList.size() > 0) {
			for (final TagInfo tag : tagList) {
				view = mInflater.inflate(R.layout.image_tag, null);
				TextView text = (TextView) view.findViewById(R.id.tv_tag_text);
				text.setText(tag.getName());
				if (tag.getX() > 0.5) {
					text.setBackgroundResource(R.drawable.bg_tag_left);
				} else {
					text.setBackgroundResource(R.drawable.bg_tag_right);
				}
				final RelativeLayout layout = (RelativeLayout) view
						.findViewById(R.id.rl_tag);
				this.addView(layout);
				layout.setVisibility(View.INVISIBLE);
				new Handler().postDelayed(new Runnable() {
					@Override
					public void run() {
						setPosition(layout, tag.getX(), tag.getY());
					}
				}, 100);
			}
		}
	}

	private boolean isShow = true;

	public void switchTags() {
		isShow = !isShow;
		if (isShow) {
			this.setVisibility(View.VISIBLE);
		} else {
			this.setVisibility(View.INVISIBLE);
		}
	}

	private void setPosition(View view, double x, double y) {
		int w = this.getWidth();
		int h = this.getHeight();
		int left = (int) (w * x);
		int top = (int) (h * y);
		int right = left + view.getWidth();
		int bottom = top + view.getHeight();
		view.layout(left, top, right, bottom);
		RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) view
				.getLayoutParams();
		params.leftMargin = left;
		params.topMargin = top;
		view.setLayoutParams(params);
		view.setVisibility(View.VISIBLE);
	}
}
