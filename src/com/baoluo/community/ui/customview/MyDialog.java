package com.baoluo.community.ui.customview;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.baoluo.community.ui.R;

public class MyDialog extends AlertDialog implements OnClickListener {

	private TextView tvTitle, tvSure, tvCance;

	private String title;
	private MyDialogListener myDialogListener;

	public MyDialog(Context context, String title,
			MyDialogListener myDialogListener) {
		super(context);
		this.title = title;
		this.myDialogListener = myDialogListener;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_dialog);
		tvTitle = (TextView) findViewById(R.id.tv_title);
		tvSure = (TextView) findViewById(R.id.tv_sure);
		tvCance = (TextView) findViewById(R.id.tv_cance);

		tvTitle.setText(title);
		tvSure.setOnClickListener(this);
		tvCance.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.tv_sure:
			myDialogListener.sure();
			this.dismiss();
			break;
		case R.id.tv_cance:
			this.dismiss();
			break;
		default:
			break;
		}
	}

	public interface MyDialogListener {
		public void sure();
	}
}
