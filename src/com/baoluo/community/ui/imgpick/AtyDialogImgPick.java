package com.baoluo.community.ui.imgpick;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;

import com.baoluo.community.ui.AtyBaseLBS;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.ThumbnailGridView;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;

/**
 * 
 * @author tao.lai
 *
 */
public class AtyDialogImgPick extends AtyBaseLBS{
	
	protected final int PHOTO_CODE = 0x0;
	protected final int CAMERA_CODE = 0x1;
	private ThumbnailGridView thumbnailGridView;
	
	protected void showImgPickDialog(){
		final AlertDialog alertDialog = new AlertDialog.Builder(AtyDialogImgPick.this).create();
		alertDialog.show();
		Window win = alertDialog.getWindow();
		WindowManager.LayoutParams lp = win.getAttributes();
		win.setGravity(Gravity.CENTER | Gravity.BOTTOM);
		lp.alpha = 0.7f;
		win.setAttributes(lp);
		win.setContentView(R.layout.dialog_pick_img_style);

		Button cancelBtn = (Button) win.findViewById(R.id.camera_cancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				alertDialog.cancel();
			}
		});
		Button camera_phone = (Button) win.findViewById(R.id.camera_phone);
		camera_phone.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				systemPhoto();
				alertDialog.dismiss();
			}
		});
		Button camera_camera = (Button) win.findViewById(R.id.camera_camera);
		camera_camera.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
			   cameraPhoto(initCamera());
			   alertDialog.dismiss();
			}
		});
	}
	
	private SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
	private String saveDir = Environment.getExternalStorageDirectory().getPath() + "/baoluo";
	protected File photoFile;
	private Bitmap photo;
	private void destoryImage() {
		if (photo != null) {
			photo.recycle();
			photo = null;
		}
	}
	
	public Parcelable initCamera(){
		File savePath = new File(saveDir);
		if (!savePath.exists()) {
			savePath.mkdirs();
		}
		destoryImage();
		String state = Environment.getExternalStorageState();
		if (state.equals(Environment.MEDIA_MOUNTED)) {
			Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
			String str = formatter.format(curDate);
			photoFile = new File(saveDir, str + ".jpg");
			if (!photoFile.exists()) {
				try {
					photoFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
					L.i("照片创建失败");
					return null;
				}
			}
			return Uri.fromFile(photoFile);
		} 
		return null;
	}
	
	private List<Bitmap> listBitmp = null;
	private Bitmap addBitmp = null;
	private AdapterQuickCoverGridView thumbnailAdapter;
	private int THUMBNAIL_HEIGHT = 120;
	
	/**
	 * 网格缩略图
	 */
	protected void setThumbnail(){
		if(SelectImg.getIntance().getCount() > 0){
			listBitmp = new ArrayList<Bitmap>();
//			thumbnailGridView = (ThumbnailGridView) findViewById(R.id.tgv_event_select_pic);
			initGridEvent();
			
			for(String imgUrl : SelectImg.getIntance().getSelectImgList()){
				listBitmp.add(BitmapUtil.AbbreviationsIcon(imgUrl));
			}
			
			if(SelectImg.getIntance().getCount() < SelectImg.getIntance().getChooseAble()){
				addBitmp = BitmapFactory.decodeResource(getResources(),
						R.drawable.quick_cover_add);
				if (addBitmp != null) {
					listBitmp.add(addBitmp);
				}
			}
			thumbnailAdapter = new AdapterQuickCoverGridView(this,listBitmp,THUMBNAIL_HEIGHT);
			thumbnailGridView.setAdapter(thumbnailAdapter);
		}
	}
	
	/**
	 * 为imageView 设置预览图
	 * @param imageView
	 */
	protected void setPreview(ImageView imageView){
		if(SelectImg.getIntance().getCount() > 0){
			imageView.setImageBitmap(BitmapUtil.AbbreviationsIcon(SelectImg.getIntance().getSelectImgList().get(0)));
		}
	}
	
	private void initGridEvent() {
		thumbnailGridView.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				if(position == SelectImg.getIntance().getCount()){
					showImgPickDialog();
				}
			}
		});
	}

	public void clear(){
		if(listBitmp != null){
			listBitmp.clear();
		}
		if(SelectImg.getIntance().getSelectImgList() != null){
			SelectImg.getIntance().getSelectImgList().clear();
		}
		if(addBitmp!=null){
			addBitmp.recycle();
			addBitmp = null;
		}
	}
	
	private static Context context;
	private void systemPhoto() {
		Intent mIntent = new Intent(context,AtyImageBrowse.class);
		startActivityForResult(mIntent, PHOTO_CODE);
	}

	
	public void cameraPhoto(Parcelable photo) {
		if (photo == null) {
			T.showShort(this, "照片创建失败");
			return;
		}
		Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
		intent.putExtra(MediaStore.EXTRA_OUTPUT, photo);
		startActivityForResult(intent, CAMERA_CODE);
	}
	
	public static void setConext(Context _context){
		context = _context;
	}
}
