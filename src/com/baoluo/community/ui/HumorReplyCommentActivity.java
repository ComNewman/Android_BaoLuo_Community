package com.baoluo.community.ui;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baoluo.community.entity.AccountInfo;
import com.baoluo.community.entity.CommentInfo;

/**
 * 回复评论的activity
 * 
 * @author Ryan_Fu 2015-5-21
 */
public class HumorReplyCommentActivity extends Activity implements
		OnClickListener {

	private static final String TAG = "HumorReplyCommentActivity";
	private Button btnCancel, btnSend;
	private TextView tvTitle, tvNick;
	private EditText etContent;
	private CommentInfo comment;
	private AccountInfo user;
	private String humorID;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_humor_comment);
//		Intent intent = this.getIntent();
//
//		comment = (Comment) intent.getSerializableExtra("comment");
//		user = (AccountInfo) intent.getSerializableExtra("user");
//		humorID = intent.getStringExtra("humorID");
		
		initUI();
		initData();
	}

	/**
	 * 初始化控件
	 */
	private void initUI() {
		btnCancel = (Button) findViewById(R.id.btn_cancel);
		btnSend = (Button) findViewById(R.id.btn_send);

		btnCancel.setOnClickListener(this);
		btnSend.setOnClickListener(this);

		tvTitle = (TextView) findViewById(R.id.tv_comment_title);
		tvNick = (TextView) findViewById(R.id.tv_comment_nick);

		etContent = (EditText) findViewById(R.id.et_comment);

	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		tvNick.setText(user.getAccount());
		tvTitle.setText("回复评论");
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cancel:
			finish();
			break;
			
		case R.id.btn_send:
//			StringBuffer sb = new StringBuffer();
//			String lastUser = comment.getBlogUser().getName();
//			String content = etContent.getText().toString();
//			sb.append("@");
//			sb.append(lastUser);
//			sb.append(":");
//			sb.append(content);
//			String reply = sb.toString();
//			BlogUser blogUser = new BlogUser(user.getId(), user.getAccount(),
//					"");
//			Comment comm = new Comment("", comment.get_id(), blogUser, reply, 0,
//					new Date(), new ArrayList<Praise>());
//			List<Comment> comments = new ArrayList<Comment>();
//			comments.add(comm);
//			HumorInfo commentBlog = new HumorInfo();
//			commentBlog.set_id(humorID);
//			commentBlog.setComments(comments);
//			MoodInfoRequest.getInstance().sendComment(commentBlog);
			finish();
			
			break;
		default:
			break;
		}
	}

}
