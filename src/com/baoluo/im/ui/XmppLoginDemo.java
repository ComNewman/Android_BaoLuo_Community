package com.baoluo.im.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.MainActivity;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
import com.baoluo.im.Configs;
import com.baoluo.im.XmppConnection;
import com.baoluo.im.task.LoginTask;
import com.baoluo.im.task.RegisterTask;

public class XmppLoginDemo extends Activity{
    private static String TAG = "XmppLoginDemo";
	
	EditText evUser;
	EditText evPsd;
	
	Button btnLogin;
	Button btnReg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_xmpp_login_demo);
		evUser = (EditText) findViewById(R.id.account);
		evPsd = (EditText) findViewById(R.id.password);
		btnLogin = (Button) findViewById(R.id.btn_login);
		btnReg = (Button) findViewById(R.id.btn_reg);
		
		btnLogin.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				String account = evUser.getText().toString();
				String password = evPsd.getText().toString();
				doLogin(account, password);
			}
		});
		
		btnReg.setOnClickListener(new OnClickListener(){
			@Override
			public void onClick(View v) {
				 final String account = evUser.getText().toString();
				 final String password = evPsd.getText().toString();
				new RegisterTask(XmppLoginDemo.this,account,password,new UpdateViewHelper.UpdateViewListener(){
					@Override
					public void onComplete(Object data) {
						if((Boolean)data){
							doLogin(account,password);
						}
					}
				}).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);	
			}
		});
	}
	
	private void doLogin(String account,String password){
		/*account = "hansman";
		password = "123456";*/
		account = account + "@" +Configs.SERVER_NAME;
		L.i(TAG, "login  "+account+"/"+password);
		try{
			new LoginTask(this,account,password,new UpdateViewHelper.UpdateViewListener(){
				@Override
				public void onComplete(Object data) {
					Boolean rs =(Boolean)data;
					if(rs){
						T.showLong(XmppLoginDemo.this, "登录成功");
						Intent intent = new Intent();
					//	Configs.IM_MY_NAME = data.toString();
						intent.setClass(XmppLoginDemo.this, MainActivity.class);
						startActivity(intent);
					}else{
						T.showLong(XmppLoginDemo.this, "登录异常");
					}
				}
			}).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);	
		}catch(Exception e){
			XmppConnection.closeConnection();
			T.showLong(this, "登录失败");
		}
	}

}
