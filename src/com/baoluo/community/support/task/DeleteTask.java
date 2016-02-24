package com.baoluo.community.support.task;

import java.util.Map;

import com.baoluo.community.support.http.HttpUtil.HttpMethod;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;

public class DeleteTask extends BaseTask {

	public DeleteTask(String url, Map<String, String> params,
			UpdateViewListener listener) {
		super(HttpMethod.Delete, url, params, listener, true);
	}

	public DeleteTask(String url, Map<String, String> params,
			UpdateViewListener listener, boolean beExecute) {
		super(HttpMethod.Delete, url, params, listener, beExecute);
	}

	public DeleteTask(String url, UpdateViewListener listener, String... params) {
		super(true, HttpMethod.Delete, url, listener, true, params);
	}
}
