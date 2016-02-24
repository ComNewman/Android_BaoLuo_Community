package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.L;
import com.baoluo.im.service.GroupChatService;
import com.baoluo.im.ui.adapter.GroupListAdapter;


/**
 * 群组列表
 * @author tao.lai
 *
 */
public class AtyGroupList extends AtyBase{
	private static final String TAG = "AtyGroupList";
	
	private ListView listView;
	private GroupListAdapter adapter;
	
	private List<String> groupList;
	private String myName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_group_list);
		listView = (ListView) findViewById(R.id.lv_group_list);
		
		groupList = new ArrayList<String>();
		adapter = new GroupListAdapter(this, groupList, R.layout.im_group_item);
		listView.setAdapter(adapter);
		myName = getIntent().getStringExtra("IM_MY_NAME");
		
		initData();
		initEvent();
	}
	
	public void initData(){
		List<String> groups = GroupChatService.getInstance().getJoinedGroup(myName);
		L.i(TAG, myName+"/groupList.size = "+groupList.size());
		if(groups != null){
			groupList.addAll(GroupChatService.getInstance().getJoinedGroup(myName));
		}
		adapter.notifyDataSetChanged();
	}
	
	public void initEvent(){
		listView.setOnItemClickListener(new OnItemClickListener(){
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				/*Intent i = new Intent(AtyGroupList.this,AtyMultiChat.class);
				i.putExtra("IM_MY_NAME", myName);
				i.putExtra("IM_GROUP_NAME", groupList.get(position));
				startActivity(i);
				L.i(TAG, groupList.get(position));*/
			}
		});
	}
}
