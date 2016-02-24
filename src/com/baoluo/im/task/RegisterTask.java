package com.baoluo.im.task;

import org.jivesoftware.smack.SmackAndroid;

import android.content.Context;

import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;
import com.baoluo.community.util.L;
import com.baoluo.im.service.UserService;

public class RegisterTask extends MyAsyncTask<Void, Long, Boolean> {

	private Context ctx;
	private String account;
	private String password;
	private UpdateViewListener listener;

	public RegisterTask(Context ctx, String account, String password,
			UpdateViewListener listener) {
		this.ctx = ctx;
		this.account = account;
		this.password = password;
		this.listener = listener;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		SmackAndroid.init(ctx);
		return UserService.getInstance().register(account, password);
	}

	@Override
	protected void onPostExecute(Boolean result) {
		super.onPostExecute(result);
		if (listener != null) {
			L.i("im reg", "成功");
			listener.onCompleteExecute(result+"");
		}
	}

}
