package com.baoluo.community.ui.imgpick;

import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;

import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.util.T;

public class AdapterPreviewImg extends CommonAdapter<String> {
	private String mDirPath; // 文件夹路径
	private HeadView headView;
	private Context context;

	public AdapterPreviewImg(Context context, List<String> mDatas,
			int itemLayoutId, String dirPath, HeadView imgSel) {
		super(context, mDatas, itemLayoutId);
		this.context = context;
		this.mDirPath = dirPath;
		this.headView = imgSel;
	}

	@Override
	public void convert(final ViewHolder helper, final String item) {
		// 设置no_pic
		helper.setImageResource(R.id.id_item_image, R.drawable.pictures_no);
		// 设置no_selected
		helper.setImageResource(R.id.id_item_select,
				R.drawable.picture_unselected);
		// 设置图片
		helper.setLocalImageByUrl(R.id.id_item_image, mDirPath + "/" + item);

		final ImageView mImageView = helper.getView(R.id.id_item_image);
		final ImageView mSelect = helper.getView(R.id.id_item_select);

		mImageView.setColorFilter(null);
		// 设置ImageView的点击事件
		mImageView.setOnClickListener(new OnClickListener() {
			// 选择，则将图片变暗，反之则反之
			@Override
			public void onClick(View v) {
				if (SelectImg.getIntance().getSelectImgList()
						.contains(mDirPath + "/" + item)) { // 已经选择过该图片
					SelectImg.getIntance().getSelectImgList()
							.remove(mDirPath + "/" + item);
					mSelect.setImageResource(R.drawable.picture_unselected);
					mImageView.setColorFilter(null);
					SelectImg.getIntance().setCount(
							SelectImg.getIntance().getCount() - 1);
					headView.setRightText("完成(" + SelectImg.getIntance().getCount()
							+ "/" + SelectImg.getIntance().getChooseAble()
							+ ")");
					if (SelectImg.getIntance().getCount() <= 0) {
						headView.getRightView().setEnabled(false);
					}
				} else { // 未选择该图片
					if (SelectImg.getIntance().getCount() < SelectImg
							.getIntance().getChooseAble()) {
						SelectImg.getIntance().getSelectImgList()
								.add(mDirPath + "/" + item);
						mSelect.setImageResource(R.drawable.pictures_selected);
						mImageView.setColorFilter(Color.parseColor("#77000000"));
						SelectImg.getIntance().setCount(
								SelectImg.getIntance().getCount() + 1);
						headView.setRightText("完成("
								+ SelectImg.getIntance().getCount() + "/"
								+ SelectImg.getIntance().getChooseAble() + ")");

						if (SelectImg.getIntance().getCount() > 0) {
							headView.getRightView().setEnabled(true);
						}
					} else {
						T.showShort(context, "最多显示"
								+ SelectImg.getIntance().getCount() + "张图片");
					}
				}
			}
		});

		/**
		 * 已经选择过的图片，显示出选择过的效果
		 */
		if (SelectImg.getIntance().getSelectImgList()
				.contains(mDirPath + "/" + item)) {
			mSelect.setImageResource(R.drawable.pictures_selected);
			mImageView.setColorFilter(Color.parseColor("#77000000"));
		}
	}

}
