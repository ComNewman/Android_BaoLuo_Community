package com.baoluo.community.ui.imgpick;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.entity.ImageFloder;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.imgpick.ListImageDirPopupWindow.OnImageDirSelected;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;

public class AtyImageBrowse extends Activity implements OnImageDirSelected{

	private static final String TAG = "ImageBrowseAty";
	private ProgressDialog mProgressDialog;

	private int dirSize; // 存储文件夹中的图片数量
	private File imgMaxDir; // 图片数量最多的文件夹
	private List<String> allImgs; // 所有的图片

	private GridView mGirdView;
	private AdapterPreviewImg mAdapter;

	private HashSet<String> mDirPaths = new HashSet<String>();// 临时的辅助类，用于防止同一个文件夹的多次扫描
	private List<ImageFloder> allImgFloders = new ArrayList<ImageFloder>();// 扫描拿到所有的图片文件夹

	private RelativeLayout mBottomLy;

	private HeadView headView;
	private TextView mChooseDir;
	private TextView mImageCount;
	//private TextView mImageSel;
	//int totalCount = 0;
	int MSG = 0x110;

	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;

	@SuppressLint("HandlerLeak")
	private Handler mHandler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if(msg.what == MSG){
				mProgressDialog.dismiss();
				initListDirPopupWindw();  //初始化展示文件夹的popupWindw
				data2View();  //为View绑定数据
			}
		}
	};

	/**
	 * 为View绑定数据
	 */
	private void data2View() {
		if (imgMaxDir == null) {
			T.showShort(getApplicationContext(), "没有找到图片");
			return;
		}
		
		ListView mListDir = mListImageDirPopupWindow.getListDir();
		View v = mListDir.getChildAt(0);
		boolean rs = mListDir.performItemClick(v, 0, mListDir.getItemIdAtPosition(0));
		L.e("setOnImageDirSelected--  performItemClick rs="+rs);
		/*allImgs = Arrays.asList(imgMaxDir.list());
		*//**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗
		 *//*
		mAdapter = new MyAdapter(getApplicationContext(), allImgs,
				R.layout.grid_item, imgMaxDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		mChooseDir.setText(allImgFloders.get(0).getName());
		mImageCount.setText(allImgFloders.get(0).getCount() + "张");*/
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	@SuppressLint("InflateParams")
	private void initListDirPopupWindw() {
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				allImgFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.dialog_list_img_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener() {
			@Override
			public void onDismiss() {
				WindowManager.LayoutParams lp = getWindow().getAttributes(); // 设置背景颜色变暗
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		mListImageDirPopupWindow.setOnImageDirSelected(this); //设置选择文件夹的回调
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_pick_img_browse);

		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;

		initView();
		getImages();
		initEvent();
	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages() {
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			T.showShort(this, "暂无外部存储");
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");
		new Thread(new Runnable() {
			@Override
			public void run() {
				String firstImage = null;
				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = AtyImageBrowse.this.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				L.i(TAG, "mCursor.getCount()="+mCursor.getCount());
				while (mCursor.moveToNext()) {
					String path = mCursor.getString(mCursor
							.getColumnIndex(MediaStore.Images.Media.DATA)); // 获取图片的路径
					if (firstImage == null){ //拿到第一张图片的路径
						firstImage = path;
					}
					File parentFile = new File(path).getParentFile(); //获取该图片的父路径名
					if (parentFile == null){
						continue;
					}
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					if (mDirPaths.contains(dirPath)) { // 利用一个HashSet防止多次扫描同一个文件夹
						continue;
					} else {
						mDirPaths.add(dirPath);
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter() {
						@Override
						public boolean accept(File dir, String filename) {
							if (filename.endsWith(".jpg")
									|| filename.endsWith(".png")
									|| filename.endsWith(".jpeg"))
								return true;
							return false;
						}
					}).length;
					//totalCount += picSize;

					imageFloder.setCount(picSize);
					allImgFloders.add(imageFloder);
					if (picSize > dirSize) {
						dirSize = picSize;
						imgMaxDir = parentFile;
					}
				}
				mCursor.close();
				mDirPaths = null;   //扫描完成，辅助的HashSet也就可以释放内存
				mHandler.sendEmptyMessage(MSG);  //通知Handler扫描图片完成
				Collections.sort(allImgFloders);
			}
		}).start();
	}

	/**
	 * 初始化View
	 */
	private void initView() {
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);
		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
		headView = (HeadView) findViewById(R.id.hv_head);

		/*mImageSel = (TextView)findViewById(R.id.tv_top_complete);
		mImageSel.setTextColor(Color.parseColor("#222222"));*/
		SelectImg.getIntance().setCount(SelectImg.getIntance().getSelectImgList().size());
		L.i("initView   count=" + SelectImg.getIntance().getCount());
		if(SelectImg.getIntance().getCount() <= 0){
			headView.getRightView().setEnabled(false);
			//mImageSel.setEnabled(false);
		}else{
			headView.setRightText("完成(" + SelectImg.getIntance().getCount() + "/" + SelectImg.getIntance().getChooseAble() + ")");
			/*mImageSel.setText("完成("+SelectImg.getIntance().getCount()+"/"+SelectImg.getIntance().getChooseAble()+")");
			mImageSel.setTextColor(Color.parseColor("#FFFFFF"));*/
		}
	}

	private void initEvent() {
		/**
		 * 为底部的布局设置点击事件，弹出popupWindow
		 */
		mBottomLy.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				mListImageDirPopupWindow
						.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});
		headView.setHeadViewListener(new HeadView.HeadViewListener() {
			@Override
			public void leftListener() {
				onBackPressed();
				AtyImageBrowse.this.finish();
			}

			@Override
			public void rightListener() {
				setResult(RESULT_OK, new Intent(AtyImageBrowse.this, MainActivity.class));
				finish();
			}
		});
		/*mImageSel.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				setResult(RESULT_OK,new Intent(AtyImageBrowse.this,MainActivity.class));
				finish();
			}
		});*/
	}

	@Override
	public void selected(ImageFloder floder) {
		imgMaxDir = new File(floder.getDir());
		allImgs = Arrays.asList(imgMaxDir.list(new FilenameFilter() {
			@Override
			public boolean accept(File dir, String filename) {
				if (filename.endsWith(".jpg") || filename.endsWith(".png")
						|| filename.endsWith(".jpeg"))
					return true;
				return false;
			}
		}));
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new AdapterPreviewImg(getApplicationContext(), allImgs,
				R.layout.aty_grid_img_item, imgMaxDir.getAbsolutePath(),headView);
		mGirdView.setAdapter(mAdapter);
		//mAdapter.notifyDataSetChanged();
		mImageCount.setText(floder.getCount() + "张");
		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();
	}
}
