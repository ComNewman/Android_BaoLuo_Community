package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.entity.BlogUser;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.util.L;

/**
 * 转发心情activity
 * 
 * @author Ryan_Fu 2015-5-21
 */
public class TranspondHumorActivity extends AtyBase implements OnClickListener {

	private static final String TAG = "TranspondHumorActivity";
	private Button btnCancel, btnSend;
	private TextView tvNick, tvContent, tvFrom;
	private ImageView imgFirst;
	private EditText etTranspond;
	private HumorInfo humorDetails;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_humor_transpond);
		Intent intent = this.getIntent();
		humorDetails = (HumorInfo) intent.getSerializableExtra("humorDetails");
		initUI();
		initData();
	}

	private void initUI() {

		btnCancel = (Button) findViewById(R.id.btn_transpond_cancel);
		btnSend = (Button) findViewById(R.id.btn_transpond_send);
		btnCancel.setOnClickListener(this);
		btnSend.setOnClickListener(this);

		imgFirst = (ImageView) findViewById(R.id.img_transpond);

		tvNick = (TextView) findViewById(R.id.tv_transpond_nick);
		tvFrom = (TextView) findViewById(R.id.tv_transpond_from);
		tvContent = (TextView) findViewById(R.id.tv_transpond_content);

		etTranspond = (EditText) findViewById(R.id.et_transpond);
	}

	private void initData() {

		tvNick.setText(getAccountInfo().getAccount());

		StringBuffer sb = new StringBuffer();
		sb.append("@");
		sb.append(humorDetails.getBlogUser().getName());

		tvFrom.setText(sb.toString());
		tvContent.setText(humorDetails.getContent());

		if (humorDetails.getPictures().size() > 0) {
			String url = humorDetails.getPictures().get(0).getUrl();
			if (url != null && url.length() > 0) {
				// GlobalContext.options.shouldShowStubImage();
				GlobalContext.getInstance().imageLoader.displayImage(url,
						imgFirst, GlobalContext.options, null);
			}
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		// 取消
		case R.id.btn_transpond_cancel:

			finish();
			break;
		// 转发
		case R.id.btn_transpond_send:
			String transpond = etTranspond.getText().toString();
			BlogUser blogUser = new BlogUser(getAccountInfo().getId(),
					getAccountInfo().getAccount(), "",1,1);
			if (humorDetails.getRawRelay() != null) {

				// 二转
				StringBuffer sb = new StringBuffer();
				sb.append(transpond);
				sb.append("@");
				sb.append(humorDetails.getBlogUser().getName());
				sb.append(" ");
				sb.append(humorDetails.getContent());

				L.i(TAG, android.os.Build.MODEL);
				L.i(TAG, "二转ID" + humorDetails.getRawRelay().getId());
				L.i(TAG, "转发的内容" + sb.toString());
			} else {
				// 一转
			}

			finish();
			break;
		default:
			break;
		}

	}
}
