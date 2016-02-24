package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.im.entity.NotificationInfo;
import com.baoluo.im.entity.NotificationModel;
import com.baoluo.im.ui.adapter.NotificationAdapter;
import com.baoluo.im.util.PopupWindowHelp;

/**
 * 校方通知
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyPushNotification extends AtyBase implements OnClickListener {

	private Button btnSwith, btnFace, btnVoice, btnAdd;
	private ImageButton imgBtnBack, imgBtnMenu;
	private EditText editText;
	private ListView listView;

	private NotificationAdapter adapter;
	private List<NotificationModel> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_push_notification);

		initUI();
		initData();
	}

	private void initUI() {

		imgBtnBack = (ImageButton) findViewById(R.id.btn_notification_back);
		imgBtnMenu = (ImageButton) findViewById(R.id.btn_notification_menu);
		btnSwith = (Button) findViewById(R.id.btn_notification_swith);
		btnFace = (Button) findViewById(R.id.btn_notification_face);
		btnVoice = (Button) findViewById(R.id.btn_notification_voice);
		btnAdd = (Button) findViewById(R.id.btn_notification_add);

		imgBtnBack.setOnClickListener(this);
		imgBtnMenu.setOnClickListener(this);
		btnSwith.setOnClickListener(this);
		btnFace.setOnClickListener(this);
		btnVoice.setOnClickListener(this);
		btnAdd.setOnClickListener(this);

		editText = (EditText) findViewById(R.id.et_notification);

		list = new ArrayList<NotificationModel>();
		listView = (ListView) findViewById(R.id.lv_notification);
		adapter = new NotificationAdapter(list, this);
		listView.setAdapter(adapter);
		listView.setDivider(null);

	}

	private void initData() {
		List<NotificationInfo> infos1 = new ArrayList<NotificationInfo>();
		NotificationInfo info1 = new NotificationInfo(
				"url",
				"只有一条信息的推送通知",
				"我是只有一条信息的推送通知，只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知只有一条信息的推送通知");
		infos1.add(info1);
		NotificationModel model1 = new NotificationModel();
		model1.setItems(infos1);

		List<NotificationInfo> infos2 = new ArrayList<NotificationInfo>();
		NotificationInfo info2 = new NotificationInfo("url", "第一条信息", null);
		NotificationInfo info3 = new NotificationInfo("url", "第二条信息", null);
		NotificationInfo info4 = new NotificationInfo("url", "第三条信息", null);
		NotificationInfo info5 = new NotificationInfo("url", "第四条信息", null);
		infos2.add(info2);
		infos2.add(info3);
		infos2.add(info4);
		infos2.add(info5);
		NotificationModel model2 = new NotificationModel();
		model2.setItems(infos2);

		list.add(model2);
		list.add(model1);
		adapter.notifyDataSetChanged();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_notification_back:
			finish();
			break;
		case R.id.btn_notification_menu:
			new PopupWindowHelp(this).showNotificationMenu(imgBtnMenu, 30);
			break;
		case R.id.btn_notification_swith:

			break;
		case R.id.btn_notification_face:

			break;
		case R.id.btn_notification_voice:

			break;
		case R.id.btn_notification_add:

			break;
		default:
			break;
		}
	}
}
