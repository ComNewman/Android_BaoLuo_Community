package com.baoluo.im.task;

import org.jivesoftware.smack.SmackAndroid;

import android.content.Context;

import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.im.service.UserService;

public class LoginTask extends MyAsyncTask<Void, Long, Boolean> {
	private Context ctx;
	private String userName;
	private String password;
	private UpdateViewHelper.UpdateViewListener listener;

	public LoginTask(Context ctx, String userName, String password,
			UpdateViewHelper.UpdateViewListener listener) {
		this.ctx = ctx;
		this.userName = userName;
		this.password = password;
		this.listener = listener;
	}

	public LoginTask(String userName, String password,
			UpdateViewHelper.UpdateViewListener listener) {
		this.userName = userName;
		this.password = password;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		SmackAndroid.init(ctx);
		return UserService.getInstance().login(userName, password);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		if (listener != null) {
			listener.onCompleteExecute(result+"");
		}
	}
}
