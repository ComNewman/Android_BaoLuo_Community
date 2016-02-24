package com.baoluo.community.ui.customview;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.baoluo.community.entity.TagInfo;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.DensityUtil;
import com.baoluo.community.util.L;

/**
 * 添加心情标签
 * 
 * @author xiangyang.fu
 * 
 */
public class AddTagsView extends ViewGroup {
	private static final String TAG = "AddTagsView";

	private static final int PADDING_HOR = DensityUtil.dip2px(10);// 水平方向padding
	private static final int PADDING_VERTICAL = DensityUtil.dip2px(8);// 垂直方向padding
	private static final int SIDE_MARGIN = 0;// DensityUtil.dip2px(10);// 左右间距
	private static final int TEXT_MARGIN = DensityUtil.dip2px(10);
	private Context context;
	private List<String> tags;
	private List<Button> btns = new ArrayList<Button>();
	private int maxRows = 2;

	/**
	 * @param context
	 */
	public AddTagsView(Context context) {
		this(context, null, 0);
	}

	/**
	 * @param context
	 * @param attrs
	 * @param defStyle
	 */
	public AddTagsView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.context = context;
		tags = new ArrayList<String>();
	}

	/**
	 * @param context
	 * @param attrs
	 */
	public AddTagsView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * 重新设置标签
	 * 
	 * @param tags
	 */
	@SuppressLint("NewApi")
	public void setTags(List<TagInfo> tagList) {
		removeAllViews();
		for (TagInfo tagInfo : tagList) {
			addTag(tagInfo.getName());
		}
		Button btn = new Button(context);
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
		Resources resource = (Resources) context.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.drawable.selector_btn_tags_color);
		if (csl != null) {
			btn.setTextColor(R.drawable.selector_btn_tags_color);// 设置按钮文字颜色
		}
		btn.setBackground(context.getResources().getDrawable(
				R.drawable.btn_bg_tag_dashed));
		btn.setText("添加标签");
		btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				addTagsListener.addTags();
			}
		});
		addView(btn);
	}

	/**
	 * 获取选中的标签
	 * 
	 * @return
	 */
	public List<String> getTags() {
		L.i(TAG, " btns.size:" + btns.size());
		List<String> selTags = new ArrayList<String>();
		for (int i = 0; i < btns.size(); i++) {
			Button btn = btns.get(i);
			String tag = btn.getText().toString();
			if (btn.isSelected() && !selTags.contains(tag)) {
				selTags.add(tag);
			}
		}
		L.i(TAG, " selTags.size:" + selTags.size());
		return selTags;
	}

	public void setTagSelected() {
		for (int i = 0; i < btns.size(); i++) {
			btns.get(i).setSelected(true);
		}
	}

	@SuppressLint("NewApi")
	public void addTag(final String tag) {
		final Button btn = new Button(context);
		btn.setTextSize(TypedValue.COMPLEX_UNIT_SP, 8);
		Resources resource = (Resources) context.getResources();
		ColorStateList csl = (ColorStateList) resource
				.getColorStateList(R.drawable.selector_btn_tags_color);
		if (csl != null) {
			btn.setTextColor(R.drawable.selector_btn_tags_color);// 设置按钮文字颜色
		}
		btn.setBackground(context.getResources().getDrawable(
				R.drawable.btn_bg_send_tag));
		btn.setText(tag);
		btns.add(btn);
		btn.setSelected(false);
		btn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				v.setSelected(!v.isSelected());
			}
		});
		addView(btn);
	}

	/*private boolean isSelectedTag(String tag) {
		if (getTags().contains(tag)) {
			return true;
		}
		return false;
	}*/

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {
		L.i(TAG, "onLayout     autualWidth=" + (r - l));
		int childCount = getChildCount();
		int y = 0;// 纵坐标开始
		int minIndex = 0;

		int[] rowX = new int[maxRows];
		for (int i = 0; i < maxRows; i++) {
			rowX[i] = SIDE_MARGIN;
		}
		L.i(TAG, "onLayout    childCount=" + childCount);
		for (int i = 0; i < childCount; i++) {
			View view = getChildAt(i);
			int width = view.getMeasuredWidth();
			int height = view.getMeasuredHeight();
			minIndex = getMinIndex(rowX);
			rowX[minIndex] += (width + TEXT_MARGIN);
			y = (minIndex + 1) * (height + TEXT_MARGIN);

			view.layout(rowX[minIndex] - width - TEXT_MARGIN, y - height,
					rowX[minIndex] - TEXT_MARGIN, y);

			/*
			 * x += width + TEXT_MARGIN; if (x > autualWidth) { x = width +
			 * SIDE_MARGIN; rows++; } y = rows * (height + TEXT_MARGIN); if (i
			 * == 0) { view.layout(x - width - TEXT_MARGIN, y - height, x -
			 * TEXT_MARGIN, y); } else { view.layout(x - width, y - height, x,
			 * y); }
			 */
		}
		/*
		 * for (int i = 0; i < childCount; i++) { View view = getChildAt(i); int
		 * width = view.getMeasuredWidth(); int height =
		 * view.getMeasuredHeight(); x += width + TEXT_MARGIN; if (x >
		 * autualWidth) { x = width + SIDE_MARGIN; rows++; } y = rows * (height
		 * + TEXT_MARGIN); if (i == 0) { view.layout(x - width - TEXT_MARGIN, y
		 * - height, x - TEXT_MARGIN, y); } else { view.layout(x - width, y -
		 * height, x, y); } }
		 */
	};

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int y = 0;// 纵坐标
		int[] rowX = new int[maxRows]; // 每行的x坐标
		for (int i = 0; i < maxRows; i++) {
			rowX[i] = 0;
		}
		int childCount = getChildCount();
		L.i(TAG, "childCount=" + childCount);
		for (int index = 0; index < childCount; index++) {
			View child = getChildAt(index);
			child.setPadding(PADDING_HOR, PADDING_VERTICAL, PADDING_HOR,
					PADDING_VERTICAL);
			child.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
			int width = child.getMeasuredWidth();
			int height = child.getMeasuredHeight();
			rowX[getMinIndex(rowX)] += width + TEXT_MARGIN;
			y = maxRows * (height + TEXT_MARGIN);
		}
		setMeasuredDimension(rowX[getMaxIndex(rowX)], y);
		L.i(TAG, "onMeasure      max rowX=" + rowX[getMaxIndex(rowX)]);
	}

	private int getMinIndex(int[] rowX) {
		int minIndex = 0;
		for (int i = 1; i < maxRows; i++) {
			if (rowX[i] < rowX[minIndex]) {
				minIndex = i;
			}
		}
		return minIndex;
	}

	private int getMaxIndex(int[] rowX) {
		int maxIndex = 0;
		for (int i = 1; i < maxRows; i++) {
			if (rowX[i] > rowX[maxIndex]) {
				maxIndex = i;
			}
		}
		return maxIndex;
	}

	private void test(int widthMeasureSpec, int heightMeasureSpec) {
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

	private AddTagsListener addTagsListener;

	public void setAddTagsListener(AddTagsListener addTagsListener) {
		this.addTagsListener = addTagsListener;
	}

	public interface AddTagsListener {
		public void addTags();
	}
}
