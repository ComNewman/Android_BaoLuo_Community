package com.baoluo.community.ui.imgpick;

import java.util.List;

import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ImageView;
import android.widget.ListView;

import com.baoluo.community.entity.ImageFloder;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.CommonAdapter;
import com.baoluo.community.ui.adapter.ViewHolder;

public class ListImageDirPopupWindow extends
		BasePopupWindowForListView<ImageFloder> {

	private ListView mListDir;

	public ListView getListDir() {
		return mListDir;
	}

	public ListImageDirPopupWindow(int width, int height,
			List<ImageFloder> datas, View convertView) {
		super(convertView, width, height, true, datas);
	}

	@Override
	public void initViews() {
		mListDir = (ListView) findViewById(R.id.id_list_dir);
		mListDir.setAdapter(new CommonAdapter<ImageFloder>(context, mDatas,
				R.layout.dialog_list_img_dir_item) {
			@Override
			public void convert(final ViewHolder helper, ImageFloder item) {
				helper.setText(R.id.id_dir_item_name, item.getName());
				helper.setLocalImageByUrl(R.id.id_dir_item_image,
						item.getFirstImagePath());
				helper.setText(R.id.id_dir_item_count, item.getCount() + "张");
			}
		});
	}

	private OnImageDirSelected mImageDirSelected;

	public void setOnImageDirSelected(OnImageDirSelected mImageDirSelected) {
		this.mImageDirSelected = mImageDirSelected;
	}

	@Override
	public void initEvents() {
		mListDir.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if (mImageDirSelected != null) {
					mImageDirSelected.selected(mDatas.get(position));
					// initSelStatus(parent,view,position);
				}
			}
		});
	}

	public void initSelStatus(ViewGroup parent, View view, int position) {
		ImageView imgView = null;
		for (int i = 0; i < parent.getChildCount(); i++) {
			imgView = (ImageView) parent.getChildAt(i).findViewById(
					R.id.id_dir_item_isChoose);
			if (i == position) {
				imgView.setImageResource(R.drawable.dir_choose);
			} else {
				imgView.setImageResource(0);
			}
		}
	}

	@Override
	public void init() {
	}

	@Override
	protected void beforeInitWeNeedSomeParams(Object... params) {
	}

	public interface OnImageDirSelected {
		void selected(ImageFloder floder);
	}

}
