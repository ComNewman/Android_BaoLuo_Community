package com.baoluo.community.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.InitDatas;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.aty.user.AtyLogin;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.util.DormQueueCache;

/**
 * Created by tao.lai on 2015/9/28 0028.
 */
public class AtyStart extends AtyBase{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.aty_start);
        enterNextPage();
    }

    private void enterNextPage(){
        String token = GlobalContext.getInstance().getToken();
        if(StrUtils.isEmpty(token)){
           toLogin();
        }else{
            new PostTask(false, UrlHelper.CHECK_LOGIN,new UpdateViewHelper.UpdateViewListener(){
                @Override
                public void onComplete(Object obj) {
                   //{"code":"200","msg":"Success"}
                    if(ResultParse.getInstance().getResCode(obj.toString()) == 200){
                        try {
                            Thread.sleep(3 * 1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        toMain();
                    }else {
                        toLogin();
                    }
                }
            },"Key",token);
        }
    }

    private void toLogin(){
        startActivity(new Intent(this, AtyLogin.class));
        this.finish();
    }

    private void toMain(){
        startActivity(new Intent(this, MainActivity.class));
        MqttHelper.getInstance();
        DormQueueCache.getInstance().initQueue();
        InitDatas.getInstance().initDatas();
        AtyStart.this.startService(new Intent(
                "com.baoluo.community.service.BackService"));
        this.finish();
    }
}
