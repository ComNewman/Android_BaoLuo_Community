package com.baoluo.community.ui.customview;

import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.DensityUtil;

/**
 * 标签集
 * 
 * @author xiangyang.fu
 * 
 */
public class TagsView extends ViewGroup {

	private static final int PADDING_HOR = DensityUtil.dip2px(8);// 水平方向padding
	private static final int PADDING_VERTICAL = DensityUtil.dip2px(3);;// 垂直方向padding
	private static final int SIDE_MARGIN = 10;// 左右间距
	private static final int TEXT_MARGIN = 10;

	private Context context;

	/**
	 * @param context
	 */
	public TagsView(Context context) {
		super(context);
		this.context = context;
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public TagsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public TagsView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	@SuppressLint("NewApi")
	public void setTextViews(List<TagInfo> tags) {
		if (tags != null) {
			for (int i = 0; i < tags.size(); i++) {
				TextView textview = new TextView(context);
				textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 7);
				textview.setTextColor(Color.parseColor("#ffffff"));
				textview.setBackground(context.getResources().getDrawable(
						R.drawable.bg_hd_biaoqian_nor));
				textview.setText(tags.get(i).getName());
				addView(textview);
			}
		} else {
			removeAllViews();
		}

	}

	@SuppressLint("NewApi")
	public void setEventTags(List<TagInfo> tags) {
		if (tags != null) {
			for (int i = 0; i < tags.size(); i++) {
				TextView textview = new TextView(context);
				textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 15);
				textview.setTextColor(Color.parseColor("#333333"));
				if (i == 0) {
					textview.setBackground(context.getResources().getDrawable(
							R.drawable.bg_hd_biaoqian_nor));
				} else {
					textview.setBackground(context.getResources().getDrawable(
							R.drawable.bg_btn_tags_selected));
				}
				textview.setText(tags.get(i).getName());
				addView(textview);
			}
		} else {
			removeAllViews();
		}

	}
	@SuppressLint("NewApi")
	public void setEventListTags(List<TagInfo> tags) {
		if (tags != null) {
			for (int i = 0; i < tags.size(); i++) {
				TextView textview = new TextView(context);
				textview.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
				textview.setTextColor(Color.parseColor("#333333"));
				if (i == 0) {
					textview.setBackground(context.getResources().getDrawable(
							R.drawable.bg_hd_biaoqian_nor));
				} else {
					textview.setBackground(context.getResources().getDrawable(
							R.drawable.bg_btn_tags_selected));
				}
				textview.setText(tags.get(i).getName());
				addView(textview);
			}
		} else {
			removeAllViews();
		}

	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		int childCount = getChildCount();
		int autualWidth = r - l;
		int x = SIDE_MARGIN;// 横坐标开始
		int y = 0;// 纵坐标开始
		int rows = 1;
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			// 设置背景颜色
			// view.setBackgroundColor(Color.TRANSPARENT);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			x += width + TEXT_MARGIN;
			if (x > autualWidth) {
				x = width + SIDE_MARGIN;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);
			if (i == 0) {
				view.layout(x - width - TEXT_MARGIN, y - height, x
						- TEXT_MARGIN, y);
			} else {
				view.layout(x - width, y - height, x, y);
			}
		}
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int x = 0;// 横坐标
		int y = 0;// 纵坐标
		int rows = 1;// 总行数
		int specWidth = MeasureSpec.getSize(widthMeasureSpec);
		int actualWidth = specWidth - SIDE_MARGIN * 2;// 实际宽度
		int childCount = getChildCount();
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);
			child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR,
					PADDING_VERTICAL);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			x += width + TEXT_MARGIN;
			if (x > actualWidth) {// 换行
				x = width;
				rows++;
			}
			y = rows * (height + TEXT_MARGIN);
		}
		setMeasuredDimension(actualWidth, y);
	}

}
