package com.baoluo.community.support.task;

import java.util.HashMap;
import java.util.Map;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.http.HttpUtil;
import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.ui.imgpick.SelectImg;
import com.baoluo.community.util.ImgUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.util.FileUtil;
import com.baoluo.im.util.UUIDGenerator;

/**
 * 图片经过压缩处理后上传
 */
public class ImgUpLoadTask extends MyAsyncTask<Void, Long, String> {
	private static final String TAG = "ImgUpLoadTask";

	private String upLoadUrl;
	private String picPath;
	private UpdateViewHelper.UpdateViewListener listener;
	private Map<String, String> param;
	private String temp;

	public ImgUpLoadTask(String _upLoadUrl, String _picPath,
			UpdateViewHelper.UpdateViewListener _listener) {
		listener = _listener;
		upLoadUrl = _upLoadUrl;
		picPath = _picPath;
		param = new HashMap<String, String>();
		param.put("Token", GlobalContext.getInstance().getToken());
		this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
	}

	@Override
	protected void onPreExecute() {
		super.onPreExecute();
		temp = FileUtil.getImageTemp() + UUIDGenerator.getUUID() + ".jpg";
		String tempPath = ImgUtil.compressImage(picPath, temp);
		L.i(TAG, "onPreExecute  tempPath=" + tempPath);
	}

	@Override
	protected String doInBackground(Void... params) {
		L.i(TAG, "doInBackground        --");
		try {
			return HttpUtil.getInstance().executeUploadTask(this.upLoadUrl,
					param, this.temp, "userfile", null);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@Override
	protected void onProgressUpdate(Long... values) {
		super.onProgressUpdate(values);
	}

	@Override
	protected void onPostExecute(String result) {
		super.onPostExecute(result);
		L.i(TAG, "onPostExecute        --");
		FileUtil.delFile(temp);
		L.i(TAG,
				"result=" + result + "   url="
						+ StrUtils.parseImgBackUrl(result));
		SelectImg.getIntance().getUpImgList()
				.add(StrUtils.parseImgBackUrl(result));
		if (listener != null) {
			listener.onComplete(result);
		}
	}
}
