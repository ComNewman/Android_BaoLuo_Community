package com.baoluo.im.ui;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.setting.SettingUtility;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.util.StrUtils;
/**
 * 随机昵称
 * @author xiangyang.fu
 *
 */
public class AtyDormGetNick extends AtyBase{
	
	private RoundImageView riv;
	private TextView tvNick;
	private ImageButton ibRefresh;
	private Button btnSure;
	private UserSelf dormUser;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_dorm_getrandomnick);
		initUi();
	}

	private void initUi() {
		riv = (RoundImageView) findViewById(R.id.riv_get_random_nick);
		tvNick = (TextView) findViewById(R.id.tv_get_random_nick_name);
		ibRefresh = (ImageButton) findViewById(R.id.ib_get_random_nick_refresh);
		btnSure = (Button) findViewById(R.id.btn_dorm_random_nick_sure);
		btnSure.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(dormUser!=null){
					SettingUtility.setDormUserInfo(dormUser);
				}
				AtyDormGetNick.this.finish();
			}
		});
		ibRefresh.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				new GetTask(UrlHelper.DORM_GET_NICK, new UpdateViewHelper.UpdateViewListener(UserSelf.class){
					@Override
					public void onComplete(Object obj) {
						dormUser = (UserSelf)obj;
						tvNick.setText(dormUser.getNickName());
						if(!StrUtils.isEmpty(dormUser.getFace())){
							GlobalContext.getInstance().imageLoader.displayImage(dormUser.getFace(), riv, GlobalContext.options, null);
						}
					}
				});
				
			}
		});
	}
}
