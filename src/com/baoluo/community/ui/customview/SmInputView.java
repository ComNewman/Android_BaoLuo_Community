package com.baoluo.community.ui.customview;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TextView.OnEditorActionListener;

import com.baoluo.community.core.Configs;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.SmFaceGvAdapter;
import com.baoluo.community.util.InputFilterHelp;
import com.baoluo.community.util.KeyBoardUtils;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.ui.adapter.FaceVPAdapter;

/**
 * 社交评论输入框
 * @author xiangyang.fu
 * 
 */
public class SmInputView extends LinearLayout implements OnClickListener,
		OnPageChangeListener {

	private final String TAG = this.getClass().getName();

	private Context ctx;
	private View view;
	private LayoutInflater mInflater;

	private EditText etMsg;
	private ViewPager mViewPager;
	private LinearLayout llFace, mDotsLayout;
	private Button btnSend;
	private ImageButton ibFace;
	private List<View> emojiViews = new ArrayList<View>();
	private List<String> staticFacesList;
	private final int columns = 7;
	private final int rows = 3;
	private SmInputListener smInputListener;

	public SmInputView(Context context) {
		super(context);
	}

	public SmInputView(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.ctx = context;
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.sm_inputview, null);
		addView(view);
		initInputView();
		initStaticFaces();
		initEmojiPager();
	}

	private void initEmojiPager() {
		
		for (int i = 0; i < getPagerCount(); i++) {
			emojiViews.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(emojiViews);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);

	}

	private ImageView dotsItem(int position) {
		View layout = mInflater.inflate(R.layout.dot_image, null);
		ImageView iv = (ImageView) layout.findViewById(R.id.face_dot);
		iv.setId(position);
		return iv;
	}

	private View viewPagerItem(int position) {
		View layout = mInflater.inflate(R.layout.face_gridview, null);
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList
				.subList(position * (columns * rows - 1),
						(columns * rows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (columns
								* rows - 1)
								* (position + 1)));
		subList.add("emotion_del_normal.png");

		SmFaceGvAdapter mGvAdapter = new SmFaceGvAdapter(subList, ctx);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view)
							.getChildAt(1)).getText().toString();
					if (!png.contains("emotion_del_normal")) {
						insert(getFace(png));
					} else {
						delete();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return gridview;
	}

	private SpannableStringBuilder getFace(String png) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		String tempPng = png.substring("face/bl/".length(), png.length()
				- ".png".length());
		try {
			String tempText = "[em:" + tempPng + "]";
			sb.append(tempText);
			sb.setSpan(
					new ImageSpan(ctx, BitmapFactory.decodeStream(ctx
							.getAssets().open(png))),
					sb.length() - tempText.length(), sb.length(),
					Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

		} catch (Exception e) {
			e.printStackTrace();
		}

		return sb;
	}

	/**
	 * 向输入框里添加表情
	 * */
	private void insert(CharSequence text) {
		int iCursorStart = Selection.getSelectionStart((etMsg.getText()));
		int iCursorEnd = Selection.getSelectionEnd((etMsg.getText()));
		if (iCursorStart != iCursorEnd) {
			((Editable) etMsg.getText()).replace(iCursorStart, iCursorEnd, "");
		}
		int iCursor = Selection.getSelectionEnd((etMsg.getText()));
		((Editable) etMsg.getText()).insert(iCursor, text);
	}

	private void delete() {
		if (etMsg.getText().length() != 0) {
			int iCursorEnd = Selection.getSelectionEnd(etMsg.getText());
			int iCursorStart = Selection.getSelectionStart(etMsg.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(iCursorEnd)) {
						String st = "[em:b00]";
						((Editable) etMsg.getText()).delete(
								iCursorEnd - st.length(), iCursorEnd);
					} else {
						((Editable) etMsg.getText()).delete(iCursorEnd - 1,
								iCursorEnd);
					}
				} else {
					((Editable) etMsg.getText()).delete(iCursorStart,
							iCursorEnd);
				}
			}
		}
	}

	private boolean isDeletePng(int cursor) {
		String st = "[em:b00]";
		String content = etMsg.getText().toString().substring(0, cursor);
		if (content.length() >= st.length()) {
			String checkStr = content.substring(content.length() - st.length(),
					content.length());
			String regex = "(\\[em:b)\\d{2}(\\])";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);

			return m.matches();
		}
		return false;
	}

	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (columns * rows - 1) == 0 ? count / (columns * rows - 1)
				: count / (columns * rows - 1) + 1;
	}

	private void initStaticFaces() {
		try {
			staticFacesList = new ArrayList<String>();
			String[] faces = ctx.getAssets().list("face/bl");
			for (int i = 0; i < faces.length; i++) {
				staticFacesList.add(faces[i]);
			}
			staticFacesList.remove("emotion_del_normal.png");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@SuppressLint("NewApi")
	public SmInputView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.ctx = context;
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.sm_inputview, null);
		addView(view);
		initInputView();
	}

	private void initInputView() {
		etMsg = (EditText) view.findViewById(R.id.et_sminputview);
		mViewPager = (ViewPager) view
				.findViewById(R.id.face_sminputview_viewpager);
		llFace = (LinearLayout) view.findViewById(R.id.ll_sminputview_face);
		mDotsLayout = (LinearLayout) view
				.findViewById(R.id.face_sminputview_dots_container);
		btnSend = (Button) view.findViewById(R.id.btn_sminputview_send);
		ibFace = (ImageButton) view.findViewById(R.id.ib_sminputview_emoji);
		setEditTextEnter();
		etMsg.setFilters(new InputFilter[]{new InputFilterHelp(ctx,Configs.COMMENT_LEN)});
		etMsg.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				InputMethodManager imm = (InputMethodManager) ctx
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etMsg.getWindowToken(), 0);
				return true;
			}
		});
		etMsg.setOnClickListener(this);
		btnSend.setOnClickListener(this);
		ibFace.setOnClickListener(this);
		mViewPager.setOnPageChangeListener(this);
	}

	private void sendMsg() {
		String msg = StrUtils.StrCode(etMsg.getText().toString());
		if (msg.length() > 0) {
			smInputListener.doSend(msg);
		} else {
			T.showShort(ctx, "发送消息不能为空");
		}
		llFace.setVisibility(View.GONE);
		etMsg.setText("");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.et_sminputview:
			KeyBoardUtils.openKeybord(etMsg, ctx);
			break;
		case R.id.ib_sminputview_emoji:
			if (llFace.getVisibility() == View.VISIBLE) {
				llFace.setVisibility(View.GONE);
			} else {
				llFace.setVisibility(View.VISIBLE);
			}
			break;
		case R.id.btn_sminputview_send:
			sendMsg();
			break;
		}

	}

	protected void setEditTextEnter() {
		etMsg.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				InputMethodManager imm = (InputMethodManager) ctx
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etMsg.getWindowToken(), 0);
				return true;
			}
		});
	}

	@Override
	public void onPageScrollStateChanged(int arg0) {

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {

	}

	@Override
	public void onPageSelected(int arg0) {
		for (int i = 0; i < mDotsLayout.getChildCount(); i++) {
			mDotsLayout.getChildAt(i).setSelected(false);
		}
		mDotsLayout.getChildAt(arg0).setSelected(true);

	}

	public void setSmInputListener(SmInputListener smInputListener) {
		this.smInputListener = smInputListener;
	}

	public interface SmInputListener {
		public void doSend(String msg);
	}
}
