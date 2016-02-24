package com.baoluo.community.support.zxing.view;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Vector;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.support.zxing.camera.CameraManager;
import com.baoluo.community.support.zxing.decoding.CaptureActivityHandler;
import com.baoluo.community.support.zxing.decoding.InactivityTimer;
import com.baoluo.community.support.zxing.decoding.RGBLuminanceSource;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.task.GenerateGroupAvatarTask;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.ChecksumException;
import com.google.zxing.DecodeHintType;
import com.google.zxing.FormatException;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeReader;

import de.greenrobot.event.EventBus;

/**
 * 扫一扫页面
 * 
 * @author xiangyang.fu
 * 
 */
public class CaptureActivity extends Activity implements Callback,HeadViewListener{
	public static final String QR_RESULT = "RESULT";
	public static final int REQUEST_CODE = 0x2;
	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private SurfaceView surfaceView;
	private boolean hasSurface;
	private HeadView headView;
	private Vector<BarcodeFormat> decodeFormats;
	private String characterSet;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean playBeep;
	private String photo_path;
	private Bitmap scanBitmap;
	// private static final float BEEP_VOLUME = 0.10f;
	private boolean vibrate;
	CameraManager cameraManager;
	private GroupInfo group;
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_NO_TITLE);
		/*
		 * this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
		 * WindowManager.LayoutParams.FLAG_FULLSCREEN);// 去掉信息栏
		 * 
		 * RelativeLayout layout = new RelativeLayout(this);
		 * layout.setLayoutParams(new
		 * ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT));
		 * 
		 * this.surfaceView = new SurfaceView(this); this.surfaceView
		 * .setLayoutParams(new ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT));
		 * 
		 * layout.addView(this.surfaceView);
		 * 
		 * this.viewfinderView = new ViewfinderView(this);
		 * this.viewfinderView.setBackgroundColor(0x00000000);
		 * this.viewfinderView.setLayoutParams(new
		 * ViewGroup.LayoutParams(LayoutParams.FILL_PARENT,
		 * LayoutParams.FILL_PARENT)); layout.addView(this.viewfinderView);
		 * 
		 * TextView status = new TextView(this); RelativeLayout.LayoutParams
		 * params = new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
		 * LayoutParams.WRAP_CONTENT);
		 * params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		 * params.addRule(RelativeLayout.CENTER_HORIZONTAL);
		 * status.setLayoutParams(params);
		 * status.setBackgroundColor(0x00000000);
		 * status.setTextColor(0xFFFFFFFF); status.setText("请将条码置于取景框内扫描。");
		 * status.setTextSize(14.0f);
		 * 
		 * layout.addView(status); setContentView(layout);
		 */

		setContentView(R.layout.activity_capture);
		surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		headView = (HeadView)findViewById(R.id.capture_headview);
		headView.setHeadViewListener(this);
		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_PORTRAIT) {
			setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
		}
		// CameraManager.init(getApplication());
		cameraManager = new CameraManager(getApplication());

		viewfinderView.setCameraManager(cameraManager);

		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = true;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		cameraManager.closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			// CameraManager.get().openDriver(surfaceHolder);
			cameraManager.openDriver(surfaceHolder);
		} catch (IOException ioe) {
			return;
		} catch (RuntimeException e) {
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats,
					characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public CameraManager getCameraManager() {
		return cameraManager;
	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		playBeepSoundAndVibrate();
		String rs = obj.getText();
		handleResult(rs);
	}

	private void handleResult(String rs) {
		int flag = swithGorU(rs);
		switch (flag) {
		case 1:
			String gid = rs.replace(com.baoluo.im.Configs.MQ_GROUP_PRE, "");	
			Map<String, String> params = new HashMap<String, String>();
			params.put("Id", gid);
			new PostTask(UrlHelper.GROUP_QRCODE, params,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							T.showShort(CaptureActivity.this, "加群完成");
							CaptureActivity.this.finish();
						}
					});
			break;
		case 2:
			String uid = rs.replace(com.baoluo.im.Configs.MQ_USER_PRE, "");
			new PostTask(UrlHelper.QRCODE_ADD_FRI,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							CaptureActivity.this.finish();
						}
					},"Id", uid);
			break;
		case 3:
			final String eventId = rs.replace(com.baoluo.im.Configs.MQ_EVENT_PRE, "");	
			new PostTask(UrlHelper.EVENT_ATTEND,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							T.showShort(CaptureActivity.this, "报名并加群完成！");
							getGroupInfo(eventId);
						}
					}, "Id", eventId);
			break;
		case 0:
			T.showLong(this, rs);
			break;
		default:
			break;
		}
	}
	private void getGroupInfo(final String id) {
		new GetTask(UrlHelper.GET_GROUP_INFO,
				new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
					@Override
					public void onComplete(Object obj) {
						group = (GroupInfo) obj;
						GroupHelper.getInstance().insertGroup(group);
						EventBus.getDefault().post(new NotifyGroupUpdate());
						new GenerateGroupAvatarTask(group);
						MqttHelper.getInstance().subscribe(com.baoluo.im.Configs.MQ_EVENT_PRE + id);
						CaptureActivity.this.finish();
					}
				}, "id", id);
	}
	/**
	 * 判断扫一扫 返回的结果是加好友还是加群
	 * 
	 * @param qrCode
	 * @return 1 group 2 friend 3 eventGroup 0 other
	 */
	private int swithGorU(String qrCode) {
		if (qrCode.indexOf(com.baoluo.im.Configs.MQ_GROUP_PRE) != -1) {
			return 1;
		} else if (qrCode.indexOf(com.baoluo.im.Configs.MQ_USER_PRE) != -1) {
			return 2;
		}else if(qrCode.indexOf(com.baoluo.im.Configs.MQ_EVENT_PRE) != -1){
			return 3;
		}
		return 0;
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(MessageIDs.restart_preview, delayMS);
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found it
			// too loud,
			// so we now play on the music stream.
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);

			try {
				AssetFileDescriptor fileDescriptor = getAssets().openFd(
						"qrbeep.ogg");
				this.mediaPlayer.setDataSource(
						fileDescriptor.getFileDescriptor(),
						fileDescriptor.getStartOffset(),
						fileDescriptor.getLength());
				this.mediaPlayer.setVolume(0.1F, 0.1F);
				this.mediaPlayer.prepare();
			} catch (IOException e) {
				this.mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			setResult(RESULT_CANCELED);
			finish();
			return true;
		} else if (keyCode == KeyEvent.KEYCODE_FOCUS
				|| keyCode == KeyEvent.KEYCODE_CAMERA) {
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {

		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {

			switch (requestCode) {

			case REQUEST_CODE:

				String[] proj = { MediaStore.Images.Media.DATA };
				// 获取选中图片的路径
				Cursor cursor = getContentResolver().query(data.getData(),
						proj, null, null, null);
				if (cursor.moveToFirst()) {
					int column_index = cursor
							.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
					photo_path = cursor.getString(column_index);
					if (photo_path == null) {
						photo_path = Utils.getPath(getApplicationContext(),
								data.getData());
						Log.i("123path  Utils", photo_path);
					}
					Log.i("123path", photo_path);

				}

				cursor.close();

				new Thread(new Runnable() {

					@Override
					public void run() {
						Result result = scanningImage(photo_path);
						// String result = decode(photo_path);
						if (result == null) {
							Looper.prepare();
							T.showShort(getApplicationContext(), "图片格式有误");
							Looper.loop();
						} else {
							Log.i("123result", result.toString());
							Looper.prepare();
							handleResult(result.toString());
							Looper.loop();
						}
					}
				}).start();
				break;
			}

		}

	}

	protected Result scanningImage(String path) {
		if (TextUtils.isEmpty(path)) {

			return null;

		}
		// DecodeHintType 和EncodeHintType
		Hashtable<DecodeHintType, String> hints = new Hashtable<DecodeHintType, String>();
		hints.put(DecodeHintType.CHARACTER_SET, "utf-8"); // 设置二维码内容的编码
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true; // 先获取原大小
		scanBitmap = BitmapFactory.decodeFile(path, options);
		options.inJustDecodeBounds = false; // 获取新的大小

		int sampleSize = (int) (options.outHeight / (float) 200);

		if (sampleSize <= 0)
			sampleSize = 1;
		options.inSampleSize = sampleSize;
		scanBitmap = BitmapFactory.decodeFile(path, options);

		RGBLuminanceSource source = new RGBLuminanceSource(scanBitmap);
		BinaryBitmap bitmap1 = new BinaryBitmap(new HybridBinarizer(source));
		QRCodeReader reader = new QRCodeReader();
		try {

			return reader.decode(bitmap1, hints);

		} catch (NotFoundException e) {

			e.printStackTrace();

		} catch (ChecksumException e) {

			e.printStackTrace();

		} catch (FormatException e) {

			e.printStackTrace();

		}

		return null;

	}

	private String recode(String str) {
		String formart = "";

		try {
			boolean ISO = Charset.forName("ISO-8859-1").newEncoder()
					.canEncode(str);
			if (ISO) {
				formart = new String(str.getBytes("ISO-8859-1"), "GB2312");
				Log.i("1234      ISO8859-1", formart);
			} else {
				formart = str;
				Log.i("1234      stringExtra", str);
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return formart;
	}

	@Override
	public void leftListener() {
		CaptureActivity.this.finish();
		
	}

	@Override
	public void rightListener() {
		Intent innerIntent = new Intent(); // "android.intent.action.GET_CONTENT"
		if (Build.VERSION.SDK_INT < 19) {
			innerIntent.setAction(Intent.ACTION_GET_CONTENT);
		} else {
			innerIntent.setAction(Intent.ACTION_OPEN_DOCUMENT);
		}
		innerIntent.setType("image/*");
		Intent wrapperIntent = Intent.createChooser(innerIntent, "选择二维码图片");
		CaptureActivity.this.startActivityForResult(wrapperIntent,
				REQUEST_CODE);
		
	}
}