package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.baoluo.community.ui.customview.ListViewForScrollView;
import com.baoluo.community.util.InputFilterHelp;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 活动标签
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyEventTags extends AtyBase implements HeadViewListener,
		OnClickListener {

	public static final String EVENT_TAG = "humor_tag";
	public static final String SEL_TAG = "selected_tag";
	private HeadView headView;
	private EditText etSearch;
	private ImageButton ibAdd, ibSearch, ibFlag, ibDel;
	private ListViewForScrollView lvClassify;
	private ListViewForScrollView lvResult;
	private TagAdapter classifyAdapter;
	private TagAdapter searchAdapter;
	
	private String keyword;
	private List<String> classifyDatas;
	private List<String> searchDatas;
	private List<String> selelcted;
	private List<String> myTags;
	private Map<Integer, Boolean> isCheckMapClassify;
	private Map<Integer, Boolean> isCheckMapResult;
	private TagListInfo tagListInfo;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_event_tags);
		selelcted = getIntent().getStringArrayListExtra(SEL_TAG);
		initUI();
		initData();
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_event_tags_head);
		headView.setHeadViewListener(this);
		
		etSearch = (EditText) findViewById(R.id.et_event_tags_search);
		setEditTextEnter(etSearch,this);
		etSearch.setFilters(new InputFilter[]{new InputFilterHelp(AtyEventTags.this,Configs.TAG_LEN)});
		etSearch.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				keyword = s.toString().trim();
				if (keyword.length() > 0) {
					// lvClassify.setVisibility(View.GONE);
					ibDel.setVisibility(View.VISIBLE);
					new GetTask(UrlHelper.TAG_SEARCH,
							new UpdateViewHelper.UpdateViewListener(TagListInfo.class) {
								@Override
								public void onComplete(Object rsStr) {
									handleResult(rsStr);
								}
							}, "Key", keyword);
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
				} else {
					ibDel.setVisibility(View.VISIBLE);
				}
			}
		});
		lvClassify = (ListViewForScrollView) findViewById(R.id.lv_event_send_classify_tags);
		lvResult = (ListViewForScrollView) findViewById(R.id.lv_event_send_tags);
		ibSearch = (ImageButton) findViewById(R.id.ib_event_tags_search);
		ibFlag = (ImageButton) findViewById(R.id.ib_event_send_tags_classify_flag);
		ibAdd = (ImageButton) findViewById(R.id.ib_event_tags_add);
		ibDel = (ImageButton) findViewById(R.id.ib_event_tags_del);
		ibDel.setOnClickListener(this);
		ibAdd.setOnClickListener(this);
		ibFlag.setOnClickListener(this);
	}

	private void initData() {
		ibFlag.setSelected(true);
		myTags = new ArrayList<String>();
		isCheckMapClassify = new HashMap<Integer, Boolean>();
		String[] classifys = new String[] { "美食", "培训", "课程", "体育", "生活", "文化",
				"运动", "科技", "音乐", "娱乐", "其它" };
		classifyDatas = new ArrayList<String>();
		classifyDatas = Arrays.asList(classifys);
		classifyAdapter = new TagAdapter(this, classifyDatas);
		lvClassify.setDivider(null);
		lvClassify.setAdapter(classifyAdapter);
		if (selelcted != null && selelcted.size() > 0) {
			int position = classifyDatas.indexOf(selelcted.get(0));
			isCheckMapClassify.put(position, true);
			classifyAdapter.setIsCheckMap(isCheckMapClassify);
			classifyAdapter.notifyDataSetChanged();
		}

		lvClassify.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				ViewHolder holder = (ViewHolder) view.getTag();
				if (classifyAdapter.getSelectedDatas().size() == 0
						|| classifyAdapter.getIsCheckMap().get(position)) {
					holder.cbCheck.toggle();
				} else {
					T.showShort(AtyEventTags.this, "分类标签只能选一个");
				}
			}
		});

		isCheckMapResult = new HashMap<Integer, Boolean>();
		searchDatas = new ArrayList<String>();
		searchAdapter = new TagAdapter(this, searchDatas);
		lvResult.setDivider(null);
		lvResult.setAdapter(searchAdapter);
		lvResult.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					final int position, long id) {
				if (view.getTag() instanceof ViewHolder) {
					ViewHolder holder = (ViewHolder) view.getTag();
					if (searchAdapter.getSelectedDatas().size() < 4
							|| searchAdapter.getIsCheckMap().get(position)) {
						holder.cbCheck.toggle();
						if (holder.cbCheck.isChecked()) {
							if (searchAdapter.getSelectedDatas().size() <5) {
								if(!myTags.contains(holder.tvTitle.getText().toString())){
									myTags.add(holder.tvTitle.getText().toString());
								}
							} else {
								tagToast();
							}
						} else {
							if(myTags.contains(holder.tvTitle.getText().toString())){
								myTags.remove(holder.tvTitle.getText().toString());
							}
						}
					} else {
						tagToast();
					}

				}
			}
		});
		if (selelcted != null && selelcted.size() > 1) {
			for (int i = 1; i < selelcted.size(); i++) {
				searchDatas.add(selelcted.get(i));
				myTags.add(selelcted.get(i));
			}
			searchAdapter.configCheckMap(true);
			searchAdapter.notifyDataSetChanged();
		} else {
			new GetTask(UrlHelper.TAG_DEFAULT,
					new UpdateViewHelper.UpdateViewListener(TagListInfo.class) {
						@Override
						public void onComplete(Object obj) {
							tagListInfo = (TagListInfo) obj;
							if(tagListInfo.getItems()!=null&&tagListInfo.getItems().size()>0){
								for (int i = 0; i < tagListInfo.getItems().size(); i++) {
									searchDatas.add(tagListInfo.getItems().get(i).getName());
								}
								searchAdapter.notifyDataSetChanged();
							}
						}
					}, "Type", "1");
		}
	}

	private void handleResult(Object rsStr) {
		tagListInfo =  (TagListInfo) rsStr;
		if (tagListInfo.getItems() != null && tagListInfo.getItems().size() > 0) {
			searchDatas.clear();
			searchDatas.addAll(myTags);
			searchAdapter.configCheckMap(true);
			searchAdapter.notifyDataSetChanged();
			for (int i = 0; i < tagListInfo.getItems().size(); i++) {
				searchDatas.add(tagListInfo.getItems().get(i).getName());
			}
			searchAdapter.notifyDataSetChanged();
		} else {
			ibSearch.setVisibility(View.GONE);
			ibAdd.setVisibility(View.VISIBLE);
		}
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		List<String> tags = classifyAdapter.getSelectedDatas();
		if (tags == null || tags.size() == 0) {
			T.showShort(AtyEventTags.this, "分类标签不能为空");
			return;
		}
		L.i("tags", tags.size() + "");
		if (myTags == null || myTags.size() == 0) {
			T.showShort(AtyEventTags.this, "标签不能为空");
			return;
		}
		L.i("tagSearch", myTags.size() + "");
		tags.addAll(myTags);
		Intent i = new Intent();
		i.putStringArrayListExtra(EVENT_TAG, (ArrayList<String>) tags);
		setResult(RESULT_OK, i);
		finish();

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_event_tags_add:
			addTag();
			break;
		case R.id.ib_event_tags_del:
			etSearch.setText("");
			break;
		case R.id.ib_event_send_tags_classify_flag:
			ibFlag.setSelected(!ibFlag.isSelected());
			if (ibFlag.isSelected()) {
				lvClassify.setVisibility(View.VISIBLE);
			} else {
				lvClassify.setVisibility(View.GONE);
			}
			break;
		}

	}

	private void tagToast() {
		T.showShort(AtyEventTags.this, "最多可选五个标签");
	}

	private void addTag() {
		final String name = etSearch.getText().toString();
		if (!StrUtils.isEmpty(name)) {
			new PostTask(UrlHelper.TAG_ADD,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object rsStr) {
							if (myTags.size() <5) {
								if(!myTags.contains(name)){
									myTags.add(name);
								}
							} else {
								tagToast();
							}
							searchDatas.clear();
							searchDatas.addAll(myTags);
							searchAdapter.configCheckMap(true);
							searchAdapter.notifyDataSetChanged();
						}
					}, "Name", name, "TagType", "3");

		}

	}
}
