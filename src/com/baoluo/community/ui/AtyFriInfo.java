package com.baoluo.community.ui;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.util.FastBlur;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.UserInfo;
import com.baoluo.im.ui.AtyChatMqtt;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 好友信息页面
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyFriInfo extends AtyBase implements OnClickListener,
		HeadViewListener {

	private static final String TAG = "AtyFriInfo";
	private static final int REQUESTCODE_INFORM = 0X08;
	private RoundImageView rivHead;
	private HeadView headView;
	private Button btnAddFri, btnAttent, btnSendMsg;
	private TextView tvNick, tvAccount, tvJifen, tvArea;
	private RelativeLayout rlTop;
	private ImageView ivBlur, ivPhoto1, ivPhoto2, ivPhoto3, ivSex;
	private LinearLayout llPhoto;
	private DisplayImageOptions options;
	private UserInfo userInfo;
	private String id;
	private boolean isAttened;
	private ContactsInfo ci;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_fri_info);
		id = getIntent().getStringExtra("id");
		L.i(TAG, "id:  " + id);
		initUi();
	}

	@Override
	protected void onResume() {
		initData();
		super.onResume();
	}

	private void initData() {
		options = new DisplayImageOptions.Builder()//
				// .showImageOnLoading(R.drawable.bg_mine_blur_test) //
				// 加载中显示的默认图片
				.showImageOnFail(R.drawable.bg_no_pic) // 设置加载失败的默认图片
				.cacheInMemory(true) // 内存缓存
				.cacheOnDisk(true) // sdcard缓存
				.bitmapConfig(Config.RGB_565)// 设置最低配置
				.build();
		if (!StrUtils.isEmpty(id)) {
			getUserInfo();
		}
	}

	private void getUserInfo() {
		new GetTask(UrlHelper.USER_INFO,
				new UpdateViewHelper.UpdateViewListener(UserInfo.class) {
					@Override
					public void onComplete(Object obj) {
						userInfo = (UserInfo) obj;
						ci = new ContactsInfo();
						ci.setAccountID(userInfo.getId());
						ci.setAccountName(userInfo.getUserName());
						ci.setDisplayName(userInfo.getNickName());
						ci.setFace(userInfo.getFace());
						if (!StrUtils.isEmpty(userInfo.getFace())) {
							GlobalContext.getInstance().imageLoader
									.displayImage(userInfo.getFace(), ivBlur,
											options,
											new ImageLoadingListener() {

												@Override
												public void onLoadingStarted(
														String arg0, View arg1) {

												}

												@Override
												public void onLoadingFailed(
														String arg0, View arg1,
														FailReason arg2) {

												}

												@Override
												public void onLoadingComplete(
														String arg0, View arg1,
														Bitmap arg2) {
													applyBlur();
												}

												@Override
												public void onLoadingCancelled(
														String arg0, View arg1) {

												}
											});
							GlobalContext.getInstance().imageLoader
									.displayImage(userInfo.getFace(), rivHead,
											options, null);
						}
						tvNick.setText(userInfo.getNickName());
						String stringFormat = "账号 : %s";
						tvAccount.setText(String.format(stringFormat,
								userInfo.getUserName()));
						String strFormat = "累计总积分: %d";
						tvJifen.setText(String.format(strFormat,
								userInfo.getPoint()));
						if (userInfo.getSex() == 1) {
							ivSex.setImageResource(R.drawable.ic_nan);
						}
						if (userInfo.getAlbumList() != null
								&& userInfo.getAlbumList().size() > 0) {
							GlobalContext.getInstance().imageLoader
									.displayImage(userInfo.getAlbumList()
											.get(0).getUrl(), ivPhoto1,
											options, null);

						}
						if (userInfo.getAlbumList() != null
								&& userInfo.getAlbumList().size() > 1) {
							GlobalContext.getInstance().imageLoader
									.displayImage(userInfo.getAlbumList()
											.get(1).getUrl(), ivPhoto2,
											options, null);

						}
						if (userInfo.getAlbumList() != null
								&& userInfo.getAlbumList().size() > 2) {
							GlobalContext.getInstance().imageLoader
									.displayImage(userInfo.getAlbumList()
											.get(2).getUrl(), ivPhoto3,
											options, null);
						}
						tvArea.setText(userInfo.getLocationArea().getProvince()
								+ "  " + userInfo.getLocationArea().getCity());
						if (userInfo.isIsFollowing()) {
							btnAttent.setText("取消关注");
							btnAttent.setTextColor(Color.parseColor("#e60012"));
						}
						if (userInfo.isIsFriends()) {
							btnAddFri.setText("拉黑");
							btnSendMsg.setVisibility(View.VISIBLE);
						}
						isAttened = userInfo.isIsFollowing();
					}
				}, "Id", id);
	}

	private void initUi() {
		headView = (HeadView) findViewById(R.id.hv_fri_head);
		headView.setHeadViewListener(this);

		btnAddFri = (Button) findViewById(R.id.btn_fri_info_add_fri);
		btnAttent = (Button) findViewById(R.id.btn_fri_info_attention);
		btnSendMsg = (Button) findViewById(R.id.btn_fri_info_send_msg);
		btnSendMsg.setOnClickListener(this);
		btnAddFri.setOnClickListener(this);
		btnAttent.setOnClickListener(this);

		rivHead = (RoundImageView) findViewById(R.id.riv_fri_info_head);
		tvAccount = (TextView) findViewById(R.id.tv_fri_info_account);
		tvNick = (TextView) findViewById(R.id.tv_fri_info_nick);
		tvJifen = (TextView) findViewById(R.id.tv_fri_info_jifen);
		tvArea = (TextView) findViewById(R.id.tv_fri_info_area_right_flag);
		ivSex = (ImageView) findViewById(R.id.iv_fri_info_sex);
		ivBlur = (ImageView) findViewById(R.id.iv_fri_info_top_bac);
		rlTop = (RelativeLayout) findViewById(R.id.rl_fri_info_top);

		ivPhoto1 = (ImageView) findViewById(R.id.iv_fri_photo1);
		ivPhoto2 = (ImageView) findViewById(R.id.iv_fri_photo2);
		ivPhoto3 = (ImageView) findViewById(R.id.iv_fri_photo3);

		llPhoto = (LinearLayout) findViewById(R.id.ll_fri_photo);
		llPhoto.setOnClickListener(this);
	}

	private void applyBlur() {
		ivBlur.getViewTreeObserver().addOnPreDrawListener(
				new ViewTreeObserver.OnPreDrawListener() {
					@Override
					public boolean onPreDraw() {
						ivBlur.getViewTreeObserver().removeOnPreDrawListener(
								this);
						ivBlur.buildDrawingCache();
						Bitmap bmp = ivBlur.getDrawingCache();
						blur(bmp, rlTop);
						ivBlur.setVisibility(View.GONE);
						return true;
					}
				});
	}

	@SuppressLint("NewApi")
	private void blur(Bitmap bkg, View view) {
		float radius = 8;
		float scaleFactor = 8;
		Bitmap overlay = Bitmap.createBitmap(
				(int) (view.getMeasuredWidth() / scaleFactor),
				(int) (view.getMeasuredHeight() / scaleFactor),
				Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(overlay);
		canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
				/ scaleFactor);
		canvas.scale(1 / scaleFactor, 1 / scaleFactor);
		Paint paint = new Paint();
		paint.setFlags(Paint.FILTER_BITMAP_FLAG);
		canvas.drawBitmap(bkg, 0, 0, paint);
		overlay = FastBlur.doBlur(overlay, (int) radius, true);
		view.setBackground(new BitmapDrawable(getResources(), overlay));
	}

	/*
	 * @Override protected void onActivityResult(int requestCode, int
	 * resultCode, Intent data) { super.onActivityResult(requestCode,
	 * resultCode, data); if (resultCode == RESULT_OK) { if (requestCode ==
	 * AtyFriInfo.REQUESTCODE_INFORM) { int optionType =
	 * data.getIntExtra(AtyFriMenu.OPTION_TYPE, -1); switch (optionType) { case
	 * AtyFriInfoInform.FRI_INFORM: T.showShort(this, "执行举报"); break; case
	 * AtyFriInfoInform.FRI_CANCLE: T.showShort(this, "执行取消"); break; } } } }
	 */

	private void showAddFriDialog(final String account) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("添加好友");
		builder.setMessage("添加" + account + "为好友？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				new PostTask(UrlHelper.FRIEND_ADD,
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onCompleteExecute(String responseStr) {
								if (!responseStr.equals(Configs.RESPONSE_ERROR)) {
									T.showShort(AtyFriInfo.this, "添加完成，请等待好友验证");
								} else {
									T.showShort(AtyFriInfo.this, "添加失败");
								}
							}
						}, "Id", account);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.show();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_fri_info_send_msg:
			// TODO
			Intent i = new Intent();
			i.setClass(AtyFriInfo.this, AtyChatMqtt.class);
			Bundle bundle = new Bundle();
			bundle.putSerializable("contact", ci);
			i.putExtras(bundle);
			startActivity(i);
			break;
		case R.id.ll_fri_photo:
			// TODO
			break;
		case R.id.btn_fri_info_add_fri:
			if (btnAddFri.getText().toString().equals("添加好友")) {
				showAddFriDialog(id);
			} else if (btnAddFri.getText().toString().equals("删除好友")) {
				new PostTask(UrlHelper.DEL_FRI,
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onComplete(Object obj) {
								T.showShort(AtyFriInfo.this, "成功删除");
								ContactsHelper.getInstance().removeByUid(id);
							}
						}, "id", id);
			}
			break;
		case R.id.btn_fri_info_attention:
			new PostTask(UrlHelper.ATTENTION_USER,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							L.i(TAG, "check");
							if (!isAttened) {
								btnAttent.setText("取消关注");
								btnAttent.setTextColor(Color
										.parseColor("#e60012"));
								T.showShort(AtyFriInfo.this, "关注完成");
							} else {
								btnAttent.setText("关注");
								btnAttent.setTextColor(Color
										.parseColor("#1aba30"));
								T.showShort(AtyFriInfo.this, "取消关注完成");
							}
							isAttened = !isAttened;
						}
					}, "Id", id);
			break;
		}
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {

	}
}
