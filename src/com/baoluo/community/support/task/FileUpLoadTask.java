package com.baoluo.community.support.task;

import java.util.HashMap;
import java.util.Map;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.Configs;
import com.baoluo.community.support.http.HttpUtil;
import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.util.L;

/**
 * 文件上传
 * 图片、文件不做处理
 * Created by tao.lai on 2015/9/24 0024.
 */
public class FileUpLoadTask extends MyAsyncTask<Void, Long, String> {
    private static final String TAG = "FileUpLoadTask";

    private String upLoadUrl;
    private String filePath;
    private UpdateViewHelper.UpdateViewListener listener;
    private Map<String, String> param;

    public FileUpLoadTask(String upLoadUrl, String filePath, UpdateViewHelper.UpdateViewListener listener) {
        this.upLoadUrl = upLoadUrl;
        this.filePath = filePath;
        this.listener = listener;
        this.param = new HashMap<String, String>();
        this.param.put("Token", GlobalContext.getInstance().getToken());
        this.executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);
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
        try {
            return HttpUtil.getInstance().executeUploadTask(this.upLoadUrl,
                    param, filePath, "userfile", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return Configs.RESPONSE_ERROR;
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        L.i(TAG, TAG + "'s result=:" + s);
        if (listener != null) {
            listener.onComplete(s);
        }
    }
}
