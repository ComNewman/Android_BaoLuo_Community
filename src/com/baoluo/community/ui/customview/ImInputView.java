package com.baoluo.community.ui.customview;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Environment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Editable;
import android.text.Selection;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
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

import com.baoluo.community.support.voice.RecordHelp;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.SmFaceGvAdapter;
import com.baoluo.community.ui.imgpick.AtyImageBrowse;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.KeyBoardUtils;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.adapter.FaceGVAdapter;
import com.baoluo.im.ui.adapter.FaceVPAdapter;

/**
 * 聊天输入框
 * 
 * @author tao.lai
 */
public class ImInputView extends LinearLayout implements OnClickListener {
	private static final String TAG = "ImInputView";
	
	public static final int REQUEST_CODE_PHOTO = 0x0;

	private InputListener inputListener;
	private Context ctx;
	private View view;
	private LayoutInflater mInflater;
	private Button btnDefault,btnGif;
	private ImageButton ibtnVoice,ibtnFace,ibtnFaceKeyB,ibtnVoiceKeyB,ibtnSend,ibtnAdd;
	private LinearLayout llFace, mDotsLayout, llMore;
	private EditText etMsg;
	private ViewPager mViewPager;
	
	private TextView tvPressToSpeak;

	private LinearLayout llCamera, llPhoto, llLocation, llFile, llVoice,
			llVideo;

	private List<String> staticFacesList;
	private List<View> emojiViews = new ArrayList<View>();
	private List<View> defaultEmojiViews = new ArrayList<View>();
	private int columns = 4;
	private int rows = 2;
	private int defaultColumns = 7;
	private int defaultRows = 3;

	@SuppressLint("NewApi")
	public ImInputView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		this.ctx = context;
		mInflater = (LayoutInflater) ctx
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = mInflater.inflate(R.layout.input_box_rl, null);
		addView(view);
		initInputView();
	}

	public ImInputView(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	public ImInputView(Context context) {
		this(context, null, 0);
	}
	/**
	 * 复原InputView
	 */
	public void restoreInputView(){
		tvPressToSpeak.setVisibility(View.GONE);
		etMsg.setVisibility(View.VISIBLE);
		llFace.setVisibility(View.GONE);
		llMore.setVisibility(View.GONE);
		ibtnVoiceKeyB.setVisibility(View.GONE);
		ibtnVoice.setVisibility(View.VISIBLE);
		ibtnFaceKeyB.setVisibility(View.GONE);
		ibtnFace.setVisibility(View.VISIBLE);
		ibtnSend.setVisibility(View.GONE);
		ibtnAdd.setVisibility(View.VISIBLE);
		KeyBoardUtils.closeKeybord(etMsg, ctx);
	}
	/**
	 * 初始化输入框
	 */
	private void initInputView() {
		mDotsLayout = (LinearLayout) view
				.findViewById(R.id.face_inputbox_dots_container);
		llFace = (LinearLayout) view.findViewById(R.id.ll_face);
		llMore = (LinearLayout) view.findViewById(R.id.ll_btn_container);

		llPhoto = (LinearLayout) view.findViewById(R.id.view_photo);
		llCamera = (LinearLayout) view.findViewById(R.id.view_camera);
		llLocation = (LinearLayout) view.findViewById(R.id.view_location);
		llFile = (LinearLayout) view.findViewById(R.id.view_file);
		llVoice = (LinearLayout) view.findViewById(R.id.view_audio);
		llVideo = (LinearLayout) view.findViewById(R.id.view_location_video);

		mViewPager = (ViewPager) view
				.findViewById(R.id.face_inputbox_viewpager);
		tvPressToSpeak = (TextView) view.findViewById(R.id.tv_press_to_speak_1);
		
		etMsg = (EditText) view.findViewById(R.id.et_inputbox_1);
		etMsg.setOnEditorActionListener(new OnEditorActionListener() {

			@Override
			public boolean onEditorAction(TextView arg0, int arg1, KeyEvent arg2) {
				InputMethodManager imm = (InputMethodManager) ctx
						.getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(etMsg.getWindowToken(), 0);
				return true;
			}
		});
		ibtnSend = (ImageButton) view.findViewById(R.id.btn_inputbox_send_1);
		ibtnFace = (ImageButton) view.findViewById(R.id.btn_inputbox_face_1);
		ibtnVoice = (ImageButton) view.findViewById(R.id.btn_inputbox_voice_1);
		ibtnFaceKeyB = (ImageButton) view
				.findViewById(R.id.btn_inputbox_face_keyboard_1);
		ibtnVoiceKeyB = (ImageButton) view
				.findViewById(R.id.btn_inputbox_voice_keyboard_1);
		ibtnAdd = (ImageButton) view.findViewById(R.id.btn_inputbox_add_1);
		btnDefault = (Button) view.findViewById(R.id.btn_default_face);
		btnGif = (Button) view.findViewById(R.id.btn_gif_face);
		btnDefault.setOnClickListener(this);
		btnGif.setOnClickListener(this);
		etMsg.setOnClickListener(this);
		ibtnSend.setOnClickListener(this);
		ibtnFace.setOnClickListener(this);
		ibtnVoice.setOnClickListener(this);
		ibtnFaceKeyB.setOnClickListener(this);
		ibtnVoiceKeyB.setOnClickListener(this);
		ibtnAdd.setOnClickListener(this);

		llCamera.setOnClickListener(this);
		llFile.setOnClickListener(this);
		llPhoto.setOnClickListener(this);
		llLocation.setOnClickListener(this);
		llVideo.setOnClickListener(this);
		llVoice.setOnClickListener(this);

		mViewPager.setOnPageChangeListener(new PageChange());
		etMsg.addTextChangedListener(new TextWatcher() {
			private CharSequence temp;
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				temp = s;
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				if (temp.length() > 0) {
					ibtnAdd.setVisibility(View.GONE);
					ibtnSend.setVisibility(View.VISIBLE);
				} else {
					ibtnAdd.setVisibility(View.VISIBLE);
					ibtnSend.setVisibility(View.GONE);
				}
			}
		});
		
		initDefaultEmoji();
	}

	private void sendMsg() {
		String msg = StrUtils.StrCode(etMsg.getText().toString());
		if (msg.length() > 0) {
			Msg myMsg = new Msg(toId, Msg.MSG_TYPE_TEXT, msg);
			inputListener.doSend(myMsg);
		} else {
			T.showShort(ctx, "发送消息不能为空");
		}
		etMsg.setText("");
	}
	
	public void sendImg(){
		Msg msg = new Msg(toId, Msg.MSG_TYPE_PIC, SelectImg
				.getIntance().getSelectImgList().get(0));
		inputListener.doSend(msg);
		SelectImg.getEmptyIntance();
	}
	
	private void sendRecord(){
		if(StrUtils.isEmpty(recordPath)){
			return;
		}
		Msg msg = new Msg(toId, Msg.MSG_TYPE_VOICE, recordPath);
		inputListener.doSend(msg);
		recordPath = "";
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_inputbox_send_1:
			llFace.setVisibility(View.GONE);
			restoreInputView();
			sendMsg();
			break;
		case R.id.btn_inputbox_face_1:
			KeyBoardUtils.closeKeybord(etMsg, ctx);
			llMore.setVisibility(View.GONE);
			llFace.setVisibility(View.VISIBLE);
			ibtnFace.setVisibility(View.GONE);
			ibtnFaceKeyB.setVisibility(View.VISIBLE);
			ibtnSend.setVisibility(View.GONE);
			ibtnAdd.setVisibility(View.VISIBLE);
			ibtnVoiceKeyB.setVisibility(View.GONE);
			ibtnVoice.setVisibility(View.VISIBLE);
			tvPressToSpeak.setVisibility(View.GONE);
			etMsg.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_inputbox_voice_1:
			KeyBoardUtils.closeKeybord(etMsg, ctx);
			ibtnVoice.setVisibility(View.GONE);
			ibtnVoiceKeyB.setVisibility(View.VISIBLE);
			etMsg.setVisibility(View.GONE);
			tvPressToSpeak.setVisibility(View.VISIBLE);
			ibtnSend.setVisibility(View.GONE);
			ibtnAdd.setVisibility(View.VISIBLE);
			llFace.setVisibility(View.GONE);
			llMore.setVisibility(View.GONE);
			ibtnFaceKeyB.setVisibility(View.GONE);
			ibtnFace.setVisibility(View.VISIBLE);
			break;
		case R.id.btn_inputbox_add_1:
			KeyBoardUtils.closeKeybord(etMsg, ctx);
			ibtnVoiceKeyB.setVisibility(View.GONE);
			ibtnVoice.setVisibility(View.VISIBLE);
			tvPressToSpeak.setVisibility(View.GONE);
			etMsg.setVisibility(View.VISIBLE);
			ibtnFaceKeyB.setVisibility(View.GONE);
			ibtnFace.setVisibility(View.VISIBLE);
			llFace.setVisibility(View.GONE);
			llMore.setVisibility(View.VISIBLE);
			break;
		case R.id.et_inputbox_1:
			ibtnFaceKeyB.setVisibility(View.GONE);
			ibtnFace.setVisibility(View.VISIBLE);
			llFace.setVisibility(View.GONE);
			llMore.setVisibility(View.GONE);
			KeyBoardUtils.openKeybord(etMsg, ctx);
			break;
		case R.id.btn_inputbox_face_keyboard_1:
			ibtnFaceKeyB.setVisibility(View.GONE);
			ibtnFace.setVisibility(View.VISIBLE);
			llFace.setVisibility(View.GONE);
			llMore.setVisibility(View.GONE);
			ibtnAdd.setVisibility(View.GONE);
			ibtnSend.setVisibility(View.VISIBLE);
			KeyBoardUtils.openKeybord(etMsg, ctx);
			break;
		case R.id.btn_inputbox_voice_keyboard_1:
			ibtnVoiceKeyB.setVisibility(View.GONE);
			ibtnVoice.setVisibility(View.VISIBLE);
			tvPressToSpeak.setVisibility(View.GONE);
			etMsg.setVisibility(View.VISIBLE);
			llMore.setVisibility(View.GONE);
			llFace.setVisibility(View.GONE);
			ibtnAdd.setVisibility(View.VISIBLE);
			ibtnSend.setVisibility(View.GONE);
			KeyBoardUtils.openKeybord(etMsg, ctx);
			break;
		case R.id.view_audio:
			
			break;
		case R.id.view_camera:
			
			break;
		case R.id.view_photo:
			selectPicFromLocal();
			break;
		case R.id.view_file:
			
			break;
		case R.id.view_location:
			
			break;
		case R.id.view_location_video:
			
			break;
		case R.id.btn_gif_face:
			// TODO
			initGifEmoji();
			break;
		case R.id.btn_default_face:
			initDefaultEmoji();
			break;
		}
	}
	
	/**
	 * 从图库获取图片
	 */
	public void selectPicFromLocal() {
		Intent mIntent = new Intent(ctx,AtyImageBrowse.class);
		((Activity) ctx).startActivityForResult(mIntent, REQUEST_CODE_PHOTO);
	}
	
	/**
	 * 照相获取图片
	 */
	public void selectPicFromCamera() {
		
	}
	/**
	 * 初始化表情页
	 */
	private void initGifEmoji() {
		initStaticFaces();
		emojiViews.clear();
		mDotsLayout.removeAllViews();
		for (int i = 0; i < getEmojiCount(); i++) {
			emojiViews.add(viewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			// LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT,
			// LayoutParams.WRAP_CONTENT);
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
				.subList(position * (columns * rows),
						(columns * rows) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size()
								: (columns * rows) * (position + 1)));
		FaceGVAdapter mGvAdapter = new FaceGVAdapter(subList, ctx);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(columns);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view)
							.getChildAt(1)).getText().toString();
					Msg myMsg = new Msg(toId, Msg.MSG_TYPE_TEXT, getFace(png,"face/png/")
							.toString());
					inputListener.doSend(myMsg);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		return gridview;
	}
	/**
	 * 获取表情的页数
	 * @return
	 */
	private int getEmojiCount() {
		int count = staticFacesList.size();
		return count % (columns * rows) == 0 ? count / (columns * rows) : count
				/ (columns * rows) + 1;
	}
	/**
	 * viewpage 改变时 小圆点也跟着改变
	 * @author xiangyang.fu
	 *
	 */
	class PageChange implements OnPageChangeListener {

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
	}
	/**
	 * 从根目录获取 静态表情
	 */
	private void initStaticFaces() {
		try {
			staticFacesList = new ArrayList<String>();
			String[] faces = ctx.getAssets().list("face/png");
			for (int i = 0; i < faces.length; i++) {
				staticFacesList.add(faces[i]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	/**
	 * 统一  图片名称 
	 * @param png
	 * @return
	 */
	private SpannableStringBuilder getFace(String png,String path) {
		SpannableStringBuilder sb = new SpannableStringBuilder();
		String tempPng = png.substring(path.length(), png.length()
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
	
	private String recordPath = "";
	
	@SuppressLint("ClickableViewAccessibility")
	public void setSpeakListener(final SpeekView speekView) {
		tvPressToSpeak.setOnTouchListener(new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					speekView.startAnimation();
					if (!Environment.getExternalStorageState().equals(
							Environment.MEDIA_MOUNTED)) {
						String st4 = getResources().getString(
								R.string.Send_voice_need_sdcard_support);
						L.i(TAG, st4);
						return false;
					}
					startRecord();
					try {
						v.setPressed(true);
						speekView.setVisibility(View.VISIBLE);
						speekView.getRecordHint().setText(R.string.move_up_to_cancel);
						speekView.getRecordHint().setBackgroundColor(Color.TRANSPARENT);
					} catch (Exception e) {
						e.printStackTrace();
						v.setPressed(false);
						speekView.setVisibility(View.INVISIBLE);
						L.i(TAG, ""+(R.string.recoding_fail));
						return false;
					}
					return true;
				case MotionEvent.ACTION_MOVE: {
					if (event.getY() < 0) {
						speekView.getRecordHint()
								.setText(R.string.release_to_cancel);
						speekView.getRecordHint()
								.setBackgroundResource(R.drawable.recording_text_hint_bg);
						canceRecord();
					} else {
						speekView.getRecordHint()
								.setText(R.string.move_up_to_cancel);
						speekView.getRecordHint().setBackgroundColor(Color.TRANSPARENT);
						speekView.startAnimation();
					}
					return true;
				}
				case MotionEvent.ACTION_UP:
					if (speekView.animationIsRuning()) {
						speekView.stopAnimation();
					}
					v.setPressed(false);
					speekView.setVisibility(View.INVISIBLE);
					sendRecord();
                    L.i(TAG, "----------- do voice send --------------");
					return true;
				default:
					speekView.getRecordHint().setVisibility(View.INVISIBLE);
					return false;
				}
			}
		});
	}
	
	private void startRecord(){
		recordPath = RecordHelp.getInstance().startAudio();
	}
	
	private void canceRecord(){
		RecordHelp.getInstance().stopAudio();
		File file = new File(recordPath);
		if(file.exists()){
			file.delete();
		}
		recordPath = "";
	}
	
	private String toId;
	public void setToId(String toId) {
		this.toId = toId;
	}

	public void setInputListener(InputListener inputListener) {
		this.inputListener = inputListener;
	}

	public interface InputListener {
		public void doSend(Msg msg);
	}
	
	/**默认表情**/
	// TODO
	
	private void initDefaultEmoji() {
		initDefaultFaces();
		emojiViews.clear();
		mDotsLayout.removeAllViews();
		for (int i = 0; i < getPagerCount(); i++) {
			emojiViews.add(defaultViewPagerItem(i));
			LayoutParams params = new LayoutParams(16, 16);
			mDotsLayout.addView(dotsItem(i), params);
		}
		FaceVPAdapter mVpAdapter = new FaceVPAdapter(emojiViews);
		mViewPager.setAdapter(mVpAdapter);
		mDotsLayout.getChildAt(0).setSelected(true);

	}
	private void initDefaultFaces() {
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
	
	private View defaultViewPagerItem(int position) {
		View layout = mInflater.inflate(R.layout.face_gridview, null);
		GridView gridview = (GridView) layout.findViewById(R.id.chart_face_gv);
		List<String> subList = new ArrayList<String>();
		subList.addAll(staticFacesList
				.subList(position * (defaultColumns * defaultRows - 1),
						(defaultColumns * defaultRows - 1) * (position + 1) > staticFacesList
								.size() ? staticFacesList.size() : (defaultColumns
								* defaultRows - 1)
								* (position + 1)));
		subList.add("emotion_del_normal.png");

		SmFaceGvAdapter mGvAdapter = new SmFaceGvAdapter(subList, ctx);
		gridview.setAdapter(mGvAdapter);
		gridview.setNumColumns(defaultColumns);
		gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				try {
					String png = ((TextView) ((LinearLayout) view)
							.getChildAt(1)).getText().toString();
					if (!png.contains("emotion_del_normal")) {
						insert(getFace(png,"face/bl/"));
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
			L.i(TAG, "iCursorEnd" + iCursorEnd);
			L.i(TAG, "iCursorStart" + iCursorStart);
			L.i(TAG, "etMsg.getText()" + etMsg.getText());
			if (iCursorEnd > 0) {
				if (iCursorEnd == iCursorStart) {
					if (isDeletePng(iCursorEnd)) {
						String st = "[em:b00]";
						((Editable) etMsg.getText()).delete(
								iCursorEnd - st.length(), iCursorEnd);
						L.i(TAG, "isDeletePng" + etMsg.getText().toString());
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
			L.i(TAG, "checkStr" + checkStr);
			String regex = "(\\[em:b)\\d{2}(\\])";
			Pattern p = Pattern.compile(regex);
			Matcher m = p.matcher(checkStr);
			L.i(TAG, "checkStr  m.matches(): " + m.matches());
			return m.matches();
		}
		return false;
	}

	private int getPagerCount() {
		int count = staticFacesList.size();
		return count % (defaultColumns * defaultRows - 1) == 0 ? count / (defaultColumns * defaultRows - 1)
				: count / (defaultColumns * defaultRows - 1) + 1;
	}

}
