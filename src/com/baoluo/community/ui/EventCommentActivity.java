package com.baoluo.community.ui;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

public class EventCommentActivity extends AtyBase implements OnClickListener {

	private static final String TAG = "HumroCommentActivity";

	private Button btnCancel, btnSend;
	private TextView tvNick, tvTitle;
	private EditText etComment;
	private String Id;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_humor_comment);
		Intent intent = getIntent();
		Id = intent.getStringExtra("Id");
		L.i(TAG, "pass id from eventdetailsactivity:" + Id);
		initUI();
		initData();
	}

	private void initUI() {
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);
		btnCancel.setOnClickListener(this);

		tvNick = (TextView) findViewById(R.id.tv_comment_nick);
		tvTitle = (TextView) findViewById(R.id.tv_comment_title);

		etComment = (EditText) findViewById(R.id.et_comment);
	}

	private void initData() {
		// tvNick.setText(self.getAccount());
		tvTitle.setText("发表评论");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
		case R.id.btn_send:
			String content = etComment.getText().toString();
			if (StrUtils.isEmpty(content)) {
				T.showShort(this, "评论信息不能为空");
				return;
			}
			final Dialog dialog = MyProgress.getInstance(
					EventCommentActivity.this, "评论...");
			new PostTask(UrlHelper.EVENT_COMMENT_POST,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onPreExecute() {
							dialog.show();
						}

						@Override
						public void onComplete(Object obj) {
							dialog.dismiss();
							EventCommentActivity.this.finish();
						}
					}, "Id", Id, "Content", content);
			break;
		default:
			break;
		}
	}
}
