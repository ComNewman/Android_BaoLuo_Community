package com.baoluo.community.ui.customview;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * 已选择图片缩略图
 * 
 * @author tao.lai
 * 
 */
public class ThumbnailGridView extends GridView {

	public ThumbnailGridView(Context context) {
		super(context);

	}

	public ThumbnailGridView(Context context, AttributeSet attrs) {
		super(context, attrs);

	}

	public ThumbnailGridView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

	}

	/**
	 * 设置不滚动
	 */
	public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
				MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
