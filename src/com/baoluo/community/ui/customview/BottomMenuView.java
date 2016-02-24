package com.baoluo.community.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.ui.R;

/**
 * 底部菜单 view
 * 
 * @author tao.lai
 * 
 */
public class BottomMenuView extends RelativeLayout{
	private static final String TAG = "BottomMenuView";

	private View view;
	private ImageButton btn;
	private TextView tv;
	private Context ctx;

	public BottomMenuView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ctx = context;
		view = mInflater.inflate(R.layout.view_bootom_menu, null);
		addView(view);
		initUI();
		init(attrs);
	}

	public BottomMenuView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public BottomMenuView(Context context) {
		this(context, null, 0);
	}

	private void initUI() {
		btn = (ImageButton) view.findViewById(R.id.ibtn);
		tv = (TextView) view.findViewById(R.id.tv);
	}

	private void init(AttributeSet attrs) {
		TypedArray t = getContext().obtainStyledAttributes(attrs,R.styleable.BottomMenu);
		Drawable btnBg = t.getDrawable(R.styleable.BottomMenu_btn_bg);
		String btnName = t.getString(R.styleable.BottomMenu_btn_name);
		
		btn.setImageDrawable(btnBg);
		tv.setText(btnName);
	}
	
	public void setBtnName(String btnName){
		tv.setText(btnName);
	}
	
	public void setBtnBg(int btnBgRes){
		btn.setImageResource(btnBgRes);
	}
	
	/**
	 * 0XFF4F95DD
	 * @param color
	 */
	public void setBtnNameColor(int color){
		tv.setTextColor(color);
	}

	/**
	 * 根据性别设置字体
	 * @param isGirl
	 */
	public void setTextColor(boolean isGirl){
		if(isGirl){
			tv.setTextAppearance(ctx,R.style.girl_menu);
		}else{
			tv.setTextAppearance(ctx, R.style.boy_menu);
		}
	}
}
