package com.baoluo.im.ui;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TokenInfo;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper.UpdateViewListener;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.im.MqttHelper;

/**
 * 关于宝落
 *
 * @author xiangyang.fu
 */
public class AtyAboutBaoluo extends AtyBase implements OnClickListener, HeadViewListener {

    private HeadView headView;
    private Button btnExit;
    private RelativeLayout rlFuctionIndroduce;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_about);
        initUi();
        initData();
    }

    private void initUi() {
        headView = (HeadView) findViewById(R.id.about_headview);
        headView.setHeadViewListener(this);
        rlFuctionIndroduce = (RelativeLayout) findViewById(R.id.rl_about_baoluo_introduce);
        rlFuctionIndroduce.setOnClickListener(this);

        btnExit = (Button) findViewById(R.id.btn_exit);
        btnExit.setOnClickListener(this);
    }

    private void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rl_about_baoluo_introduce:
                startActivity(new Intent(this, AtyFunctionIntroduce.class));
                break;
            case R.id.btn_exit:
                exitApp();
                break;
            default:
                break;
        }
    }

    private void exitApp() {
        new PostTask(UrlHelper.SET_PING, new UpdateViewListener() {
            @Override
            public void onComplete(Object obj) {
                exit();
                MqttHelper.getInstance().disConnection();
                SettingUtility.setTokenInfo(new TokenInfo());
            }
        }, "Status", 0 + "");
    }

    @Override
    public void leftListener() {
        onBackPressed();
    }

    @Override
    public void rightListener() {

    }
}
