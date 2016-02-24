package com.baoluo.im.ui;

import java.util.List;

import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.zxing.decoding.EncodeQR;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.BitmapUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.GroupUser;
import com.baoluo.im.entity.MyBitmapEntity;
import com.google.zxing.WriterException;

/**
 * 群二维码显示和保存页面
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyGroupCode extends AtyBase implements OnClickListener {

	private static final String TAG = "AtyGroupCode";

	private ImageButton ibBack;
	private TextView tvNum, tvGroupName;
	private ImageView ivHead, ivCode;
	private LinearLayout llCodeView;
	private Button btnSave;
	private String groupCode;
	private GroupInfo groupInfo;
	private Bitmap bmCode;
	
	private int groupUsers = 9;
	private MyBitmapEntity mBmpEntity;
	private List<MyBitmapEntity> mEntityList;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_code_group);
		groupCode = getIntent().getStringExtra("groupCode");
		groupInfo = (GroupInfo) getIntent().getSerializableExtra("groupInfo");
		initUi();
		initData();
	}

	private void initUi() {
		llCodeView = (LinearLayout) findViewById(R.id.ll_group_code_content);
		ibBack = (ImageButton) findViewById(R.id.ib_group_code_back);
		tvNum = (TextView) findViewById(R.id.tv_group_code_num);
		tvGroupName = (TextView) findViewById(R.id.tv_group_code_name);
		ivHead = (ImageView) findViewById(R.id.img_group_code__head);
		ivCode = (ImageView) findViewById(R.id.iv_group_code_pic);
		btnSave = (Button) findViewById(R.id.btn_group_code_save);

		ibBack.setOnClickListener(this);
		tvNum.setOnClickListener(this);
		btnSave.setOnClickListener(this);

	}

	private void initData() {
		getGroudHead();

		if (!StrUtils.isEmpty(groupCode)) {
			try {
				bmCode = EncodeQR.getInstance().encode(groupCode);
			} catch (WriterException e) {
				e.printStackTrace();
			}
		}
		if (bmCode != null) {
			ivCode.setImageBitmap(bmCode);
		}
	}

	private void getGroudHead() {
		mBmpEntity = new MyBitmapEntity(this);
		if (groupInfo != null) {
			if (groupInfo.getUsers() != null) {
				tvNum.setText("聊天信息（" + groupInfo.getUsers().size() + ")");
			}
			tvGroupName.setText(groupInfo.getName());
			List<GroupUser> groups = groupInfo.getUsers();
			if (groups.size() > 0) {
				if (groupUsers > groups.size()) {
					groupUsers = groups.size();
				}
				L.i(TAG, "group size:" + groups.size() + "groupUsers:"
						+ groupUsers);
				mEntityList = mBmpEntity.getBitmapEntitys(groupUsers);
				Bitmap[] mBitmaps = new Bitmap[groupUsers];
				for (int i = 0; i < groupUsers; i++) {
					L.i(TAG, ""+groups.get(i).getFace());
					if(!StrUtils.isEmpty(groups.get(i).getFace())){
						mBitmaps[i] = ThumbnailUtils.extractThumbnail(GlobalContext
								.getInstance().imageLoader.loadImageSync(groups
										.get(i).getFace()), (int) mEntityList.get(i).getWidth(),
										(int) mEntityList.get(i).getWidth());
					}else{
						mBitmaps[i] = ThumbnailUtils.extractThumbnail(BitmapUtil.getScaleBitmap(
								getResources(), R.drawable.head_test), (int) mEntityList.get(i).getWidth(),
										(int) mEntityList.get(i).getWidth());
					}
				}
				Bitmap combineBitmap = BitmapUtil.getCombineBitmaps(
						mEntityList, mBitmaps);
				ivHead.setImageBitmap(combineBitmap);
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_group_code_back:
			onBackPressed();
			break;
		case R.id.btn_group_code_save:
			// 保存群二维码到相册
			btnSave.setVisibility(View.GONE);
			Bitmap bmp = EncodeQR.getInstance().convertViewToBitmap(llCodeView);
			EncodeQR.getInstance().saveImageToGallery(this, bmp);
			btnSave.setVisibility(View.VISIBLE);
			break;
		case R.id.tv_group_code_num:
			onBackPressed();
			break;
		default:
			break;
		}

	}
}
