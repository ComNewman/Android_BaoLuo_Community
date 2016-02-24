package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.TagListInfo;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.TagAdapter;
import com.baoluo.community.ui.adapter.TagAdapter.ViewHolder;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.InputFilterHelp;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 标签的搜索和添加 author xiangyang.fu
 * 
 */
public class AtyTags extends AtyBase implements HeadViewListener,OnClickListener {

	public static final String HUMOR_TAG = "humor_tag";
	public static final String SEL_TAG = "selected_tag";
	private HeadView headView;
	private EditText etSearch;
	private ImageButton ibSearch, ibAdd,ibDel;
	private ListView lvRst;
	
	private String keyword;
	private List<String> datas;
	private TagAdapter adapter;
	private List<String> slelcted = null;
	private List<String> myTags;
	private TagListInfo tagListInfo;
	private Map<Integer, Boolean> isCheckMap;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_tags);
		slelcted = getIntent().getStringArrayListExtra(SEL_TAG);
		L.i("slelcted", "" + slelcted.size());
		initUI();
		initData();
	}

	private void initData() {
		myTags = new ArrayList<String>();
		isCheckMap = new HashMap<Integer, Boolean>();
		datas = new ArrayList<String>();
		adapter = new TagAdapter(this, datas);
		lvRst.setDivider(null);
		lvRst.setAdapter(adapter);
		if (slelcted != null && slelcted.size() > 0) {
			datas.clear();
			datas.addAll(slelcted);
			myTags.addAll(slelcted);
			adapter.configCheckMap(true);
			adapter.notifyDataSetChanged();
		}else{
			new GetTask(UrlHelper.TAG_DEFAULT,
					new UpdateViewHelper.UpdateViewListener() {
				@Override
				public void onComplete(Object rsStr) {
					tagListInfo = (TagListInfo) GsonUtil.getInstance().str2Obj(
							(String) rsStr, TagListInfo.class);
					if (tagListInfo.getItems() != null && tagListInfo.getItems().size() > 0) {
						datas.clear();
						
						for (int i = 0; i < tagListInfo.getItems().size(); i++) {
							datas.add(tagListInfo.getItems().get(i).getName());
						}
						adapter.notifyDataSetChanged();
					}
				}
			}, "Type", "0");
			
		}
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_tags_head);
		headView.setHeadViewListener(this);

		etSearch = (EditText) findViewById(R.id.et_tags_search);
		ibSearch = (ImageButton) findViewById(R.id.ib_tags_search);
		ibAdd = (ImageButton) findViewById(R.id.ib_tags_add);
		ibAdd.setOnClickListener(this);
		ibDel = (ImageButton) findViewById(R.id.ib_tags_del);
		ibDel.setOnClickListener(this);
		lvRst = (ListView) findViewById(R.id.lv_tags);
		lvRst.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (view.getTag() instanceof ViewHolder) {
					ViewHolder holder = (ViewHolder) view.getTag();
					if (adapter.getSelectedDatas().size() < 5
							|| adapter.getIsCheckMap().get(position)) {
						// 会自动出发CheckBox的checked事件
						holder.cbCheck.toggle();
						if (holder.cbCheck.isChecked()) {
							if (adapter.getSelectedDatas().size() < 6) {
								myTags.add(holder.tvTitle.getText().toString());
							} else {
								tagToast();
							}
						} else {
							myTags.remove(holder.tvTitle.getText().toString());
						}
					} else {
						tagToast();
					}

				}
			}
		});
		setEditTextEnter(etSearch,this);
		etSearch.setFilters(new InputFilter[]{new InputFilterHelp(AtyTags.this,Configs.TAG_LEN)});
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				keyword = s.toString().trim();
				if (keyword.length() > 0) {
					ibDel.setVisibility(View.VISIBLE);
					
					new GetTask(UrlHelper.TAG_SEARCH,
							new UpdateViewHelper.UpdateViewListener() {
								@Override
								public void onComplete(Object rsStr) {
									handleResult(rsStr);
								}

							}, "Key", keyword);
				}else{
					ibDel.setVisibility(View.GONE);
				}
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0) {
					ibDel.setVisibility(View.GONE);
					ibSearch.setVisibility(View.VISIBLE);
					ibAdd.setVisibility(View.GONE);
				}else{
					ibDel.setVisibility(View.VISIBLE);
				}
			}
		});
	}

	private void handleResult(Object rsStr) {
		tagListInfo = (TagListInfo) GsonUtil.getInstance().str2Obj(
				(String) rsStr, TagListInfo.class);
		if (tagListInfo.getItems() != null && tagListInfo.getItems().size() > 0) {
			datas.clear();
			datas.addAll(myTags);
			for (int i = 0; i < tagListInfo.getItems().size(); i++) {
				datas.add(tagListInfo.getItems().get(i).getName());
			}
			adapter.notifyDataSetChanged();
		} else {
			ibSearch.setVisibility(View.GONE);
			ibAdd.setVisibility(View.VISIBLE);
		}

	}

	private void addTag() {
		final String name = etSearch.getText().toString();
		if (!StrUtils.isEmpty(name)) {
			new PostTask(UrlHelper.TAG_ADD,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object rsStr) {
							if (myTags.size() < 6) {
								myTags.add(name);
							} else {
								tagToast();
							}
							datas.clear();
							datas.addAll(myTags);
							adapter.configCheckMap(true);
							adapter.notifyDataSetChanged();
						}
					}, "Name", name, "TagType", "2");

		}

	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	private void tagToast() {
		T.showShort(AtyTags.this, "最多可添加六个标签！");
	}

	@Override
	public void rightListener() {
		Intent i = new Intent();
		i.putStringArrayListExtra(HUMOR_TAG,(ArrayList<String>) myTags);
		setResult(RESULT_OK, i);
		finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_tags_add:
			addTag();
			break;
		case R.id.ib_tags_del:
			etSearch.setText("");
			break;
		}
		
	}
}
