package com.baoluo.community.support.task;

import java.util.Map;

import com.baoluo.community.support.http.HttpUtil.HttpMethod;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;

public class PostTask extends BaseTask {

	public PostTask(String url, Map<String, String> params,
			UpdateViewListener listener) {
		this(true, url, params, listener, true);
	}

	public PostTask(boolean hasToken, String url, Map<String, String> params,
			UpdateViewListener listener) {
		this(hasToken, url, params, listener, true);
	}

	public PostTask(boolean hasToken, String url, Map<String, String> params,
			UpdateViewListener listener, boolean beExecute) {
		super(hasToken, HttpMethod.Post, url, params, listener, beExecute);
	}

	public PostTask(String url, UpdateViewListener listener, String... params) {
		this(true, url, listener, params);
	}

	public PostTask(boolean hasToken, String url, UpdateViewListener listener,
			String... params) {
		super(hasToken, HttpMethod.Post, url, listener, true, params);
	}
}
