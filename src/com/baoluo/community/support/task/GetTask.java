package com.baoluo.community.support.task;

import java.util.Map;

import com.baoluo.community.support.http.HttpUtil.HttpMethod;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;

public class GetTask extends BaseTask {

	public GetTask(String url, Map<String, String> params,
			UpdateViewListener listener) {
		super(HttpMethod.Get, url, params, listener, true);
	}

	public GetTask(String url, Map<String, String> params,
			UpdateViewListener listener, boolean beExecute) {
		super(HttpMethod.Get, url, params, listener, beExecute);
	}

	public GetTask(String url, UpdateViewListener listener, String... params) {
		super(true, HttpMethod.Get, url, listener, true, params);
	}
}