package com.baoluo.community.support.task;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.http.HttpUtil;
import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.util.L;

public class PostObjTask extends MyAsyncTask<Void, Long, String>{
    private static final String TAG = "PostObjTask";
	
	private UpdateViewHelper.UpdateViewListener listener;
	private String url;
	private String paramStr;
	private String token;
	
	public PostObjTask(String url,String paramStr, UpdateViewHelper.UpdateViewListener listener){
		this(url, paramStr, listener, true);
	}
	
	public PostObjTask(String url, String paramStr,
			UpdateViewHelper.UpdateViewListener listener, boolean isExecute) {
		this.url = url;
		this.paramStr = paramStr;
		this.listener = listener;
		this.token = GlobalContext.getInstance().getToken();
		if (isExecute) {
			this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
		}
	}
	
	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}
	
	@Override
	protected String doInBackground(Void... params) {
		L.i(TAG, paramStr);
		String result = "";
		try {
			result = HttpUtil.getInstance().executePostTask(token, url,
					paramStr);
		} catch (Exception e) {
			e.printStackTrace();
		}
		L.i(TAG, "result=" + result);
		return result;
	}
	
	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (listener != null) {
			listener.onCompleteExecute(result);
		}
	}
}
