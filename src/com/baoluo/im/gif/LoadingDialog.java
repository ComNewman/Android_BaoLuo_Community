package com.baoluo.im.gif;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import com.baoluo.community.ui.R;
/**
 * 数据加载中的GIF 动画
 * @author xiangyang.fu
 *
 */
public class LoadingDialog extends ProgressDialog {  
  
    private Context mContext;  
    private GifView gif1; 
    public LoadingDialog(Context context) {  
        super(context);  
        this.mContext = context;  
        setCanceledOnTouchOutside(true);  
    }  
  
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState);  
        initView();  
        initData();  
    }  
  
    private void initData() {  
        gif1.setMovieResource(R.raw.loading);  
    }  
  
    private void initView() {  
        setContentView(R.layout.test_for_gif);  
        gif1 = (GifView) findViewById(R.id.gif1);
    }  
  
}  
