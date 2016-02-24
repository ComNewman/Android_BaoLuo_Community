package com.baoluo.im.ui;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.StrUtils;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.im.entity.AddFriEntity;


/**
 * 处理好友请求
 * @author tao.lai
 *
 */
public class AtyNewFri extends AtyBase implements OnClickListener, HeadViewListener{
	private static final String TAG = "AtyNewFri";
	
	private HeadView headView;
	private ImageView ivAvatar;
	private TextView tvUser;
	private Button btnSure,btnCance;
	
	private AddFriEntity newFri;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_new_fri);
		newFri = (AddFriEntity)getIntent().getSerializableExtra("newFri");
		initUI();
		initData();
	}
	
	private void initUI(){
		headView = (HeadView) findViewById(R.id.hv_head);
		ivAvatar = (ImageView) findViewById(R.id.iv_avatar);
		tvUser = (TextView) findViewById(R.id.tv_user);
		btnSure = (Button) findViewById(R.id.btn_sure);
		btnCance = (Button) findViewById(R.id.btn_cance);
		
		headView.setHeadViewListener(this);
		btnSure.setOnClickListener(this);
		btnCance.setOnClickListener(this);
	}
	
	private void initData(){
		tvUser.setText(newFri.getName());
		if(!StrUtils.isEmpty(newFri.getFace())){
			GlobalContext.getInstance().imageLoader.displayImage(newFri.getFace(), ivAvatar);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_sure:
			handAddFriRequest(newFri.getUid(),true);
			break;
		case R.id.btn_cance:
			handAddFriRequest(newFri.getUid(),false);
			break;
		default:
			break;
		}
	}
	
	private void handAddFriRequest(final String uid, final boolean agress) {
		JSONObject obj = new JSONObject();
		try {
			obj.put("Uid", uid);
			obj.put("Ret", agress ? 1 : 0);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		new PostObjTask(UrlHelper.FRIEND_ADD_ASK, obj.toString(),
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (agress) {
							ContactsHelper.getInstance().addFriInfo2Db(uid);
						}
						finish();
					}
				});
	}

	@Override
	public void leftListener() {
        onBackPressed();
	}

	@Override
	public void rightListener() {
	}
}
