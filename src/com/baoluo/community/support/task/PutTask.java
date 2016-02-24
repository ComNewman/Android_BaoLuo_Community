package com.baoluo.community.support.task;

import java.util.Map;

import com.baoluo.community.support.http.HttpUtil.HttpMethod;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;

public class PutTask extends BaseTask {

	public PutTask(String url, Map<String, String> params,
			UpdateViewListener listener) {
		super(HttpMethod.Put, url, params, listener, true);
	}

	public PutTask(String url, Map<String, String> params,
			UpdateViewListener listener, boolean beExecute) {
		super(HttpMethod.Put, url, params, listener, beExecute);
	}

	public PutTask(String url, UpdateViewListener listener, String... params) {
		super(true, HttpMethod.Put, url, listener, true, params);
	}

}
