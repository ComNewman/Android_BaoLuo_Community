package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.dao.helper.MessageHelper;
import com.baoluo.im.entity.ContactsInfo;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.entity.Message;
import com.baoluo.im.entity.Msg;
import com.baoluo.im.ui.adapter.SearchAdapter;

/**
 * IM搜索相关
 * @author tao.lai
 *
 */
public class AtyIMSearch extends AtyBase implements OnClickListener{
	private static final String TAG = "AtyIMSearch";
	
	public static final String CHAT_SCROLL_TO = "chat_scroll_to";     //聊天窗滚动到 指定内容
	public static final String SEARCH_TYPE    = "search_type";
	public static final String UID            = "to_uid";
	
	public static final byte SEARCH_TYPE_ALL = 0x0;
	public static final byte SEARCH_TYPE_PERSON = 0x1;
	public static final byte SEARCH_TYPE_GROUP = 0x2;
	public static final byte SEARCH_TYPE_CONTENT = 0x3;
	//public static final byte SEARCH_TYPE_ATTENTION = 0x4;
	
	private Button btnCance;
	private EditText etSearch;
	private ListView lvResult;
	
	private List<Message> list;
	private SearchAdapter adapter;
	
	private byte searchType;
	private String searchStr;
	private String uid;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_im_search);
		searchType = getIntent().getByteExtra(SEARCH_TYPE, SEARCH_TYPE_ALL);
		uid = getIntent().getStringExtra(UID);
		initUI();
		initData();
	}
	
	private void initUI(){
		btnCance = (Button) findViewById(R.id.btn_cance);
		etSearch = (EditText) findViewById(R.id.et_text);
		lvResult = (ListView) findViewById(R.id.lv_result);
		
		btnCance.setOnClickListener(this);
	}
	
	private void initData() {
		list = new ArrayList<Message>();
		adapter = new SearchAdapter(this,list);
		lvResult.setAdapter(adapter);
		
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				searchStr = s.toString().trim();
				if (!TextUtils.isEmpty(searchStr) && searchStr.length() > 0) {
					doSearch();
				}else{
					list.clear();
					adapter.notifyDataSetChanged();
				}
			}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}

			@Override
			public void afterTextChanged(Editable s) {
				
			}
		});
		
		lvResult.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Message ma = list.get(position);
				if (ma.getSearchResultType() > 0) {
					return;
				}
				startActivity(getIntent(ma));
			}
		});
	}
	
	private Intent getIntent(Message ma){
		Intent it = new Intent();
		if(!StrUtils.isEmpty(ma.getMessage())){
			it.putExtra(CHAT_SCROLL_TO, ma.getMessage());
		}
		if(ma.getMsgType() == Msg.MSG_GROUP){
			GroupInfo gd = GroupHelper.getInstance().getGroupByGid(ma.getUid());
			it.putExtra(AtyMultiChatMqtt.EXTRA_GROUP, gd);
			it.setClass(this, AtyMultiChatMqtt.class);
		}else{
			ContactsInfo ci = ContactsHelper.getInstance().getContactsById(ma.getUid());
			it.putExtra(AtyChatMqtt.EXTRA_CONTACTS, ci);
			it.setClass(this, AtyChatMqtt.class);
		}
		return it;
	}
	
	private void doSearch(){
		list.clear();
		if(searchType == SEARCH_TYPE_ALL || searchType == SEARCH_TYPE_PERSON){
			personSearch();
		}
		if(searchType == SEARCH_TYPE_ALL || searchType == SEARCH_TYPE_GROUP){
			groupSearch();
		}
		if(searchType == SEARCH_TYPE_ALL || searchType == SEARCH_TYPE_CONTENT){
			contentSearch();
		}
		adapter.setMatchStr(searchStr);
		adapter.notifyDataSetChanged();
	}
	
	private void personSearch(){
		List<Message> persons = MessageHelper.getInstance().searchPerson(searchStr);
		if(persons != null && persons.size() > 0){
			Message m = new Message();
			m.setSearchResultType(SEARCH_TYPE_PERSON);
			list.add(m);
			list.addAll(persons);
		}
	}
	
	private void groupSearch(){
		List<Message> groups = MessageHelper.getInstance().searchGroup(searchStr);
		if(groups != null && groups.size() > 0){
			Message m = new Message();
			m.setSearchResultType(SEARCH_TYPE_GROUP);
			list.add(m);
			list.addAll(groups);
		}
	}
	
	private void contentSearch(){
		List<Message> contents = MessageHelper.getInstance().searchContent(searchStr,uid);
		if(contents != null && contents.size() > 0){
			Message m = new Message();
			m.setSearchResultType(SEARCH_TYPE_CONTENT);
			list.add(m);
			list.addAll(contents);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_cance:
			onBackPressed();
			break;
		default:
			break;
		}
	}
}
