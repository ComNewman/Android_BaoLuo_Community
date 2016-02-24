package com.baoluo.community.support.task;

import java.util.HashMap;
import java.util.Map;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.http.HttpUtil;
import com.baoluo.community.support.http.HttpUtil.HttpMethod;
import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;
import com.baoluo.community.util.L;
import com.baoluo.community.util.NetUtil;
import com.baoluo.community.util.T;

public class BaseTask extends MyAsyncTask<Void, Long, String> {
	private static final String TAG = "BaseTask";

	private String url;
	private Map<String, String> params;
	private UpdateViewHelper.UpdateViewListener listener;
	private HttpMethod httpMethod;

	public BaseTask(boolean hasToken, HttpMethod httpMethod, String url,
			Map<String, String> params, UpdateViewListener listener) {
		this.url = url;
		this.params = params;
		this.listener = listener;
		this.httpMethod = httpMethod;
		if (this.params == null) {
			this.params = new HashMap<String, String>();
		}
		if (hasToken) {
			this.params.put("Token", GlobalContext.getInstance().getToken());
		}
	}

	public BaseTask(boolean hasToken, HttpMethod httpMethod, String url,
			UpdateViewListener listener, boolean beExecute, String[] params) {
		this(hasToken, httpMethod, url, null, listener);
		if (params != null && params.length > 0) {
			if (this.params == null) {
				this.params = new HashMap<String, String>();
			}
			for (int i = 0; i < params.length; i = i + 2) {
				this.params.put(params[i], params[i + 1]);
			}
		}
		if (!NetUtil.isConnnected(GlobalContext.getInstance())) {
			T.showShort(GlobalContext.getInstance(), "网络未连接，请检查网络");
			L.e(TAG, "网络未连接，请检查网络");
			return;
		}
		if (beExecute) {
			this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	public BaseTask(HttpMethod httpMethod, String url,
			Map<String, String> params, UpdateViewListener listener,
			boolean beExecute) {
		this(true, httpMethod, url, params, listener, beExecute);
	}

	public BaseTask(boolean hasToken, HttpMethod httpMethod, String url,
			Map<String, String> params, UpdateViewListener listener,
			boolean beExecute) {
		this(hasToken, httpMethod, url, params, listener);
		if (!NetUtil.isConnnected(GlobalContext.getInstance())) {
			L.i(TAG, "网络未连接，请检查网络");
			return;
		}
		if (beExecute) {
			this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		if (listener != null) {
			listener.onPreExecute();
		}
	}

	@Override
	protected String doInBackground(Void... params) {
		String rs = "";
		try {
			rs = HttpUtil.getInstance().executeNormalTask(this.httpMethod,
					this.url, this.params);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		L.i(TAG, this.url + "\n resultStr =" + result);
		if (listener != null) {
			listener.onCompleteExecute(result);
		}
	}
}
