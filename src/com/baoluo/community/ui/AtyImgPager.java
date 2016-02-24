package com.baoluo.community.ui;

import java.util.ArrayList;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.ui.customview.HackyViewPager;
import com.baoluo.community.ui.fragment.FgmImgDetail;
import com.baoluo.community.util.ImgUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
import com.baoluo.im.util.FileUtil;
import com.baoluo.im.util.UUIDGenerator;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

/**
 * 图片查看器
 * 
 * @author tao.lai
 * 
 */
public class AtyImgPager extends FragmentActivity implements OnClickListener {
	private static final String TAG = "AtyImgPager";

	private static final String STATE_POSITION = "STATE_POSITION";
	public static final String EXTRA_IMAGE_INDEX = "image_index";
	public static final String EXTRA_IMAGE_URLS = "image_urls";

	private ArrayList<String> urls;

	private HackyViewPager mPager;
	private int pagerPosition;
	private TextView indicator;

	private Button btnNature, btnSave;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_img_pager);
		pagerPosition = getIntent().getIntExtra(EXTRA_IMAGE_INDEX, 0);
		urls = getIntent().getStringArrayListExtra(EXTRA_IMAGE_URLS);

		mPager = (HackyViewPager) findViewById(R.id.pager);
		ImagePagerAdapter mAdapter = new ImagePagerAdapter(
				getSupportFragmentManager(), urls);
		mPager.setAdapter(mAdapter);
		indicator = (TextView) findViewById(R.id.indicator);
		btnNature = (Button) findViewById(R.id.btn_nature);
		btnSave = (Button) findViewById(R.id.btn_save);
		btnNature.setOnClickListener(this);
		btnSave.setOnClickListener(this);

		CharSequence text = getString(R.string.viewpager_indicator, 1, mPager
				.getAdapter().getCount());
		indicator.setText(text);
		mPager.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageScrollStateChanged(int arg0) {
			}

			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
			}

			@Override
			public void onPageSelected(int arg0) {
				CharSequence text = getString(R.string.viewpager_indicator,
						arg0 + 1, mPager.getAdapter().getCount());
				indicator.setText(text);
			}
		});
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
		mPager.setCurrentItem(pagerPosition);
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, mPager.getCurrentItem());
	}

	private class ImagePagerAdapter extends FragmentStatePagerAdapter {
		public ArrayList<String> fileList;

		public ImagePagerAdapter(android.support.v4.app.FragmentManager fm, ArrayList<String> fileList) {
			super(fm);
			this.fileList = fileList;
		}

		@Override
		public int getCount() {
			return fileList == null ? 0 : fileList.size();
		}

		@Override
		public android.support.v4.app.Fragment getItem(int position) {
			String url = fileList.get(position);
			return FgmImgDetail.newInstance(url);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_nature:
			T.showShort(this, "执行原图");
			break;
		case R.id.btn_save:
			doSaveImg();
			break;
		default:
			break;
		}
	}

	private void doSaveImg() {
		GlobalContext.getInstance().imageLoader.loadImage(
				urls.get(pagerPosition), new MyImageListener());
	}

	class MyImageListener extends SimpleImageLoadingListener {

		@Override
		public void onLoadingStarted(String imageUri, View view) {
			super.onLoadingStarted(imageUri, view);
		}

		@Override
		public void onLoadingFailed(String imageUri, View view,
				FailReason failReason) {
			super.onLoadingFailed(imageUri, view, failReason);
		}

		@Override
		public void onLoadingComplete(String imageUri, View view,
				Bitmap loadedImage) {
			String path = FileUtil.getImageTemp() + UUIDGenerator.getUUID()
					+ ".jpg";
			L.i(TAG, "position=" + pagerPosition + "/path=" + path);
			if (ImgUtil.bitmap2file(loadedImage, path)) {
				T.showShort(AtyImgPager.this, "已保存为" + path);
			}
			super.onLoadingComplete(imageUri, view, loadedImage);
		}
	}
}
