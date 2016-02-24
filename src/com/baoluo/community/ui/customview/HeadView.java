package com.baoluo.community.ui.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.ui.R;

/**
 * 页面 头部
 * 
 * @author tao.lai
 * 
 */
public class HeadView extends RelativeLayout implements OnClickListener {
	private static final String TAG = "HeadView";

	private View view;
	private RelativeLayout rlHead;
	private ImageButton ibtnLeft, ibtnRight;
	private Button btnLeft, btnRight;
	private TextView tvTitle;
	private Context ctx;

	public HeadView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		LayoutInflater mInflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.ctx = context;
		view = mInflater.inflate(R.layout.view_head, null);
		addView(view);
		initUI();
		init(attrs);
	}

	public HeadView(Context context, AttributeSet attrs) {
		this(context,attrs,0);
	}

	public HeadView(Context context) {
		this(context,null);
	}

	private void initUI() {
		rlHead = (RelativeLayout) findViewById(R.id.rl_head);
		ibtnLeft = (ImageButton) findViewById(R.id.ibtn_left);
		ibtnRight = (ImageButton) findViewById(R.id.ibtn_right);
		btnLeft = (Button) view.findViewById(R.id.btn_left);
		btnRight = (Button) view.findViewById(R.id.btn_right);
		tvTitle = (TextView) view.findViewById(R.id.tv_title);
		tvTitle.setEllipsize(TextUtils.TruncateAt.valueOf("END"));
		tvTitle.setMaxEms(15);
		tvTitle.setSingleLine(true);
		ibtnLeft.setOnClickListener(this);
		ibtnRight.setOnClickListener(this);
		btnLeft.setOnClickListener(this);
		btnRight.setOnClickListener(this);
	}

	private void init(AttributeSet attrs) {
		TypedArray t = getContext().obtainStyledAttributes(attrs,
				R.styleable.HeadView);
		int bgColor = t.getColor(R.styleable.HeadView_head_color, 0X4DFFFFFF);
		String title = t.getString(R.styleable.HeadView_title_value);
		boolean leftShow = t.getBoolean(R.styleable.HeadView_left_show_icon,
				true);
		boolean rightShow = t.getBoolean(R.styleable.HeadView_right_show_icon,
				true);
		Drawable leftRes = t.getDrawable(R.styleable.HeadView_left_icon);
		Drawable rightRes = t.getDrawable(R.styleable.HeadView_right_icon);
		String leftTitle = t.getString(R.styleable.HeadView_left_name);
		String rightTitle = t.getString(R.styleable.HeadView_right_name);
		rlHead.setBackgroundColor(bgColor);
		
		tvTitle.setText(title);
		if(SettingUtility.getUserSelf() != null && SettingUtility.getUserSelf().getSex() == 0){
			tvTitle.setTextAppearance(ctx,R.style.girl_color);
		}else{
			tvTitle.setTextAppearance(ctx,R.style.boy_color);
		}
		//tvTitle.setTextColor(Color.parseColor("#ff7f7e"));
		if (!leftShow) {
			ibtnLeft.setVisibility(View.GONE);
			btnLeft.setText(leftTitle);
			if(SettingUtility.getUserSelf() != null && SettingUtility.getUserSelf().getSex() == 0){
				btnLeft.setTextAppearance(ctx,R.style.girl_color);
			}else{
				btnLeft.setTextAppearance(ctx,R.style.boy_color);
			}
			btnLeft.setVisibility(View.VISIBLE);
		} else {
			ibtnLeft.setImageDrawable(leftRes);
		}
		if (!rightShow) {
			ibtnRight.setVisibility(View.GONE);
			btnRight.setText(rightTitle);
			if(SettingUtility.getUserSelf() != null && SettingUtility.getUserSelf().getSex() == 0){
				btnRight.setTextAppearance(ctx,R.style.girl_color);
			}else{
				btnRight.setTextAppearance(ctx,R.style.boy_color);
			}
			btnRight.setVisibility(View.VISIBLE);
		} else {
			ibtnRight.setImageDrawable(rightRes);
		}
	}

	public void setRightText(String rightText) {
		btnRight.setText(rightText);
		if(SettingUtility.getUserSelf() != null && SettingUtility.getUserSelf().getSex() == 0){
			btnRight.setTextAppearance(ctx,R.style.girl_color);
		}else{
			btnRight.setTextAppearance(ctx,R.style.boy_color);
		}
	}

	public void setHeadTitle(String headTitle) {
		tvTitle.setText(headTitle);
		if(SettingUtility.getUserSelf() != null && SettingUtility.getUserSelf().getSex() == 0){
			tvTitle.setTextAppearance(ctx,R.style.girl_color);
		}else{
			tvTitle.setTextAppearance(ctx,R.style.boy_color);
		}
	}

	public void setRightIcon(int iconRes) {
		ibtnRight.setImageResource(iconRes);
	}

	public View getRightView() {
		return ibtnRight;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_left:
		case R.id.ibtn_left:
			headViewListener.leftListener();
			break;
		case R.id.btn_right:
		case R.id.ibtn_right:
			headViewListener.rightListener();
			break;
		default:
			break;
		}
	}

	private HeadViewListener headViewListener;

	public void setHeadViewListener(HeadViewListener headViewListener) {
		this.headViewListener = headViewListener;
	}

	public interface HeadViewListener {
		public void leftListener();

		public void rightListener();
	}
}
