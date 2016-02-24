package com.baoluo.community.support.task;

import java.util.HashMap;
import java.util.Map;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.http.HttpUtil;
import com.baoluo.community.support.http.MyAsyncTask;

/**
 * 语音上传
 * 
 * @author xiangyang.fu
 * 
 */
public class VoiceUpLoadTask extends MyAsyncTask<Void, Long, String> {

	private static final String TAG = "VoiceUpLoadTask";

	private String upLoadUrl;
	private String path;
	private UpdateViewHelper.UpdateViewListener listener;
	private Map<String, String> param;

	public VoiceUpLoadTask(String _upLoadUrl, String path,
			UpdateViewHelper.UpdateViewListener _listener) {
		listener = _listener;
		upLoadUrl = _upLoadUrl;
		this.path = path;

		param = new HashMap<String, String>();
		param.put("Token", GlobalContext.getInstance().getToken());
		this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
	}

	@Override
	protected String doInBackground(Void... params) {
		String rs = "";
		try {
			rs = HttpUtil.getInstance().executeUploadTask(this.upLoadUrl,
					param, this.path, "userfile", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rs;
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		if (listener != null) {
			listener.onComplete(result);
		}
	}
}