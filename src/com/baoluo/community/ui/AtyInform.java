package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.BlogUser;
import com.baoluo.community.entity.Inform;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.TagAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.T;
/**
 * 举报
 * @author xiangyang.fu
 *
 */
public class AtyInform extends AtyBase implements HeadViewListener{
	
	private final String TAG = this.getClass().getName();
	
	private HeadView headView;
	private ListView lv;
	private LinearLayout llOther;
	private EditText etOther;
	
	private TagAdapter adapter;
	private List<String> datas;
	private BlogUser user;
	private String mid;
	private int modules;
	private Dialog dialog;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_inform);
		Bundle bundle = getIntent().getExtras();
		user = (BlogUser) bundle.getSerializable("user");
		mid = bundle.getString("Mid");
		modules = bundle.getInt("Modules");
		initUI();
		initData();
	}
	private void initUI() {
		
		headView = (HeadView) findViewById(R.id.hv_inform_head);
		headView.setHeadViewListener(this);
		lv = (ListView) findViewById(R.id.lv_inform);
		llOther = (LinearLayout) findViewById(R.id.ll_inform_reason_edit);
		etOther = (EditText) findViewById(R.id.et_inform_other);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				adapter.getIsCheckMap().clear();
				adapter.getIsCheckMap().put(position, true);
				adapter.notifyDataSetChanged();
				if( position == datas.size() - 1 && adapter.getIsCheckMap().get(position)){
					llOther.setVisibility(View.VISIBLE);
				}else{
					llOther.setVisibility(View.GONE);
				}
			}
		});
	}
	private void initData() {
		dialog = MyProgress.getInstance(AtyInform.this, "举报中中...");
		String[] informs = new String []{"色情","盗版","垃圾","暴力","政治","诈骗","其他"};
		datas = new ArrayList<String>();
		datas = Arrays.asList(informs);
		adapter = new TagAdapter(this, datas);
		lv.setDivider(null);
		lv.setAdapter(adapter);
	}
	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		String other = etOther.getText().toString();
		if(adapter.getSelectedDatas().size() == 0){
			T.showShort(AtyInform.this, "举报内容不能为空！");
			return;
		}
		Inform inform = new Inform();
		inform.setContent(other);
		inform.setUser(user);
		inform.setMid(mid);
		inform.setModules(modules);
		inform.setType(datas.indexOf(adapter.getSelectedDatas().get(0)));
		String paramsObj = GsonUtil.getInstance().obj2Str(inform);
		new PostObjTask(UrlHelper.INFORM, paramsObj, new UpdateViewHelper.UpdateViewListener() {
			@Override
			public void onComplete(Object obj) {
				T.showShort(AtyInform.this, "举报完成！请等待验证");
				dialog.dismiss();
				AtyInform.this.finish();
			}
		});
	}

}
