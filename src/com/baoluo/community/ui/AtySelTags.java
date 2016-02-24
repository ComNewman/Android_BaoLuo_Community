package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.baoluo.community.ui.customview.AddTagsView;
import com.baoluo.community.ui.listener.EditTextWatcher;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;

/**
 * 选择标签悬浮Activity
 * 
 * @author xiangyang.fu
 * 
 */
public class AtySelTags extends AtyBase implements OnClickListener {

	public static final String TAG = "AtySelTags";
	private static final int MAXLEN = 6;
	private EditText etSearch;
	private Button add;
	private Button sure;
	private AddTagsView addTagsView;
	private EditTextWatcher editWatcher;
	// 记录已经选择了的标签数
	private int selectedNum = 0;
	private List<String> selTags;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_sel_tags);
		selTags = getIntent().getStringArrayListExtra("tags");
		if(selTags!=null){
			selectedNum = selTags.size();
			L.i(TAG, "selectedNum:"+selectedNum);
		}
		
		initUi();
		addTagsView.addTag("校花");
		addTagsView.addTag("帅气小兄弟");
		addTagsView.addTag("hansboy");
		addTagsView.addTag("abc");
	}

	private void initUi() {
		etSearch = (EditText) findViewById(R.id.et_tags_search);
		setEditTextEnter(etSearch, this);
		add = (Button) findViewById(R.id.btn_add_tags);
		sure = (Button) findViewById(R.id.btn_tags_sure);
		addTagsView = (AddTagsView) findViewById(R.id.add_tags_view);
		add.setOnClickListener(this);
		sure.setOnClickListener(this);

		editWatcher = new EditTextWatcher(this, etSearch, MAXLEN);
		etSearch.addTextChangedListener(editWatcher.getTextWatcher());
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_add_tags:
			String tagName = etSearch.getText().toString();
			if (!StrUtils.isEmpty(tagName)) {
				addTagsView.addTag(tagName);
				etSearch.setText("");
			} else {
				T.showShort(this, "标签不能为空");
			}

			break;
		case R.id.btn_tags_sure:
			List<String> tags = addTagsView.getTags();
			List<String> resTags = new ArrayList<String>();
			if(tags!=null&&tags.size()>0){
				L.i(TAG, "tags.size:" + tags.size());
				for (int i = 0; i < tags.size(); i++) {
					L.i(TAG, tags.get(i));
					if(selTags!=null){
						if(selTags.contains(tags.get(i))){
							T.showShort(AtySelTags.this, tags.get(i) + "  标签已经存在");
						}else{
							resTags.add(tags.get(i));
						}
					}
				}	
			}
			if((selectedNum+resTags.size())>6){
				T.showShort(AtySelTags.this,  "  标签不能超过六个");
				return;
			}
			L.i("tags.size", "check tags: " + tags.size());
			Intent i = new Intent();
			i.putStringArrayListExtra("tags", (ArrayList<String>) resTags);
			setResult(RESULT_OK, i);
			finish();
			break;
		default:
			break;
		}
	}
}
