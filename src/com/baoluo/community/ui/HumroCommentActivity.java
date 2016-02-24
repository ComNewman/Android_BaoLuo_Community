package com.baoluo.community.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baoluo.community.entity.HumorInfo;
/**
 * 评论界面
 * @author Ryan_Fu
 *  2015-5-19
 */
public class HumroCommentActivity extends AtyBase implements OnClickListener{
	
	private static final String TAG = "HumroCommentActivity";
	
	private Button btnCancel,btnSend;
	private TextView tvNick,tvTitle;
	private EditText etComment;
	private HumorInfo humorDetail;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_humor_comment);
		initUI();
		initData();
	}
	
	/**
	 * 初始化控件
	 */
	private void initUI() {
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnSend = (Button) findViewById(R.id.btn_send);
		btnSend.setOnClickListener(this);
		btnCancel.setOnClickListener(this);
		
		tvNick = (TextView) findViewById(R.id.tv_comment_nick);
		tvTitle = (TextView) findViewById(R.id.tv_comment_title);
		
		etComment = (EditText) findViewById(R.id.et_comment);
		
	}
	/**
	 * 初始化数据
	 */
	private void initData() {
		tvNick.setText(getAccountInfo().getAccount());
		tvTitle.setText("发表评论");
	}
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
			
		case R.id.btn_send:
			finish();
			break;
			

		default:
			break;
		}
	}
}
