package com.baoluo.community.support.task;

import java.util.HashMap;
import java.util.Map;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.support.http.HttpUtil;
import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.util.L;

/**
 * 文件下载
 * Created by tao.lai on 2015/10/19 0019.
 */
public class FileDownTask extends MyAsyncTask<Void, Long, String> {
    private static final String TAG = "FileDownTask";

    private Map<String, String> params;
    private String fileNetUrl;
    private String fileLocalUrl;
    private UpdateViewHelper.UpdateViewListener listener;


    public FileDownTask(String fileNetUrl, String fileLocalUrl, UpdateViewHelper.UpdateViewListener listener) {
        this.fileLocalUrl = fileLocalUrl;
        this.fileNetUrl = fileNetUrl;
        this.listener = listener;
        this.params = new HashMap<String, String>();
        this.params.put("Token", GlobalContext.getInstance().getToken());
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
    protected String doInBackground(Void... param) {
        boolean result = false;
        try {
            result = HttpUtil.getInstance().executeDownloadTask(this.fileNetUrl, this.fileLocalUrl, this.params, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (result) {
            return this.fileLocalUrl;
        }
        return "";
    }

    @Override
    protected void onProgressUpdate(Long... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        L.i(TAG, fileLocalUrl + "'s result=:" + s);
        if (listener != null) {
            listener.onComplete(s);
        }
    }
}
