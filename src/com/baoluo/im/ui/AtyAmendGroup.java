package com.baoluo.im.ui;

import java.util.HashMap;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.util.T;
import com.baoluo.dao.ContactDb;
import com.baoluo.dao.GroupDb;
import com.baoluo.dao.helper.ContactsHelper;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.im.entity.ContactsInfo;

import de.greenrobot.event.EventBus;

/**
 * 修改群名称和 自己在群中的昵称
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyAmendGroup extends AtyBase implements OnClickListener,HeadViewListener{
	private static final String TAG = "AtyAmendGroup";

	public static final String EXTRA_INPUT_TEXT = "inputText";
	public static final String EXTRA_ID = "id";
	public static final String EXTRA_FRI_ID = "friUid";
	public static final String EXTRA_FLAG = "flag";
	public static final String EXTRA_SEX = "sex";

	private RelativeLayout rlEdit, rlSex, rlSexM, rlSexW;
	private ImageView imgManFlag, imgGrilFlag;
	private HeadView headView;
	private EditText etInput;
	private ImageButton ibDel;
	private String inputText;
	private String id;
	private String friId;
	private int sex;
	private int flag;
	private int maxLen = 10;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_amend_group);
		id = getIntent().getStringExtra(EXTRA_ID);
		inputText = getIntent().getStringExtra(EXTRA_INPUT_TEXT);
		flag = getIntent().getIntExtra(EXTRA_FLAG, -1);
		sex = getIntent().getIntExtra(EXTRA_SEX, -1);
		friId = getIntent().getStringExtra(EXTRA_FRI_ID);
		initUI();
		initData();
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		rlEdit = (RelativeLayout) findViewById(R.id.rl_editext);
		rlSex = (RelativeLayout) findViewById(R.id.rl_sex);
		rlSexM = (RelativeLayout) findViewById(R.id.rl_sex_m);
		rlSexW = (RelativeLayout) findViewById(R.id.rl_sex_w);
		rlSexM.setOnClickListener(this);
		rlSexW.setOnClickListener(this);
		imgManFlag = (ImageView) findViewById(R.id.iv_sex_sel_flag_m);
		imgGrilFlag = (ImageView) findViewById(R.id.iv_sex_sel_flag_w);
		ibDel = (ImageButton) findViewById(R.id.ib_del_text);
		etInput = (EditText) findViewById(R.id.et_amend_name);
		ibDel.setOnClickListener(this);
	}

	private void initData() {
		if (flag == 0) {
			headView.setHeadTitle("我在本群的昵称");
		} else if (flag == 1) {
			headView.setHeadTitle("群聊名称");
		} else if (flag == 2) {
			maxLen = 8;
			headView.setHeadTitle("个人昵称");
		} else if (flag == 3) {
			headView.setHeadTitle("性别");
		}else if (flag == 4){
			headView.setHeadTitle("修改好友备注");
		}
		if (sex != -1) {
			rlEdit.setVisibility(View.GONE);
			rlSex.setVisibility(View.VISIBLE);
			switch (sex) {
			case 0:
				imgGrilFlag.setVisibility(View.VISIBLE);
				imgManFlag.setVisibility(View.GONE);
				break;
			case 1:
				imgGrilFlag.setVisibility(View.GONE);
				imgManFlag.setVisibility(View.VISIBLE);
				break;
			default:
				imgGrilFlag.setVisibility(View.GONE);
				imgManFlag.setVisibility(View.GONE);
				break;
			}
		}
		if (inputText != null) {
			etInput.setText(inputText);
			ibDel.setVisibility(View.VISIBLE);
			etInput.addTextChangedListener(new TextWatcher() {

				@Override
				public void onTextChanged(CharSequence s, int start,
						int before, int count) {
					String input = s.toString().trim();
					if (input.length() > 0) {
						ibDel.setVisibility(View.VISIBLE);
					} else {
						ibDel.setVisibility(View.GONE);
					}
				}

				@Override
				public void beforeTextChanged(CharSequence s, int start,
						int count, int after) {
					String name = s.toString().trim();
					if (name.length() > 0) {
						ibDel.setVisibility(View.VISIBLE);
					} else {
						ibDel.setVisibility(View.GONE);
					}
				}

				@Override
				public void afterTextChanged(Editable s) {
					Editable editable = etInput.getText();
					int len = editable.length();
					if (len > maxLen) {
						T.showShort(AtyAmendGroup.this, "最多可输入" + maxLen
								+ "个字！");
						int selEndIndex = Selection.getSelectionEnd(editable);
						String str = editable.toString();
						// 截取新字符串
						String newStr = str.substring(0, maxLen);
						etInput.setText(newStr);
						editable = etInput.getText();
						// 新字符串的长度
						int newLen = editable.length();
						// 旧光标位置超过字符串长度
						if (selEndIndex > newLen) {
							selEndIndex = editable.length();
						}
						// 设置新光标所在的位置
						Selection.setSelection(editable, selEndIndex);
					}
				}
			});
		}

	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.ib_del_text:
			etInput.setText("");
			ibDel.setVisibility(View.GONE);
			break;
		case R.id.rl_sex_m:
			updatePersonalSex(1);
			break;
		case R.id.rl_sex_w:
			updatePersonalSex(0);
			break;
		default:
			break;
		}
	}

	private void updateFriNick(final String name) {
		new PostTask(UrlHelper.UPDATE_FRI_DIS_NAME,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						ContactDb c = ContactsHelper.getInstance().getContactById(friId);
						c.setRemarkName(name);
						ContactsInfo contrat = ContactsHelper.getInstance().objSwitch(c);
						ContactsHelper.getInstance().insertContacts(contrat);
						Intent i = new Intent();
						i.putExtra("friNickName", name);
						setResult(RESULT_OK, i);
						finish();
					}
				}, "Id", friId, "DisplayName", name);
		
	}

	private void updatePersonalNick(final String name) {
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtyAmendGroup.this, "更换昵称完成");
							Intent i = new Intent();
							i.putExtra("nickName", name);
							setResult(RESULT_OK, i);
							finish();
					}
				},"NickName", name);
	}

	private void updatePersonalSex(final int sex) {
		new PostTask(UrlHelper.USERINFO_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							T.showShort(AtyAmendGroup.this, "更换性别完成");
							Intent i = new Intent();
							i.putExtra("sex", sex);
							setResult(RESULT_OK, i);
							finish();
					}
				},"Sex", String.valueOf(sex));
	}

	private void updateMyName(final String name) {
		new PostTask(UrlHelper.GROUP_AMEND,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							Intent i = new Intent();
							i.putExtra("name", name);
							setResult(RESULT_OK, i);
							finish();
					}
				},"Id", id,
				"MyGroupNick", name);
	}

	private void updateGroupName(final String name) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", id);
		params.put("Name", name);
		new PostTask(UrlHelper.GROUP_AMEND, params,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object data) {
							Intent i = new Intent();
							i.putExtra("name", name);
							setResult(RESULT_OK, i);
							notifyGroupDb(name);
							finish();
					}
				});
	}

	private void notifyGroupDb(String name) {
		GroupDb gd = GroupHelper.getInstance().getGroupDbByGid(id);
		gd.setGroupName(name);
		GroupHelper.getInstance().insertGroup(gd);
		EventBus.getDefault().post(new NotifyGroupUpdate());
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {
		final String name = etInput.getText().toString().trim();
		switch (flag) {
		case 0:
			updateMyName(name);
			break;
		case 1:
			updateGroupName(name);
			break;
		case 2:
			updatePersonalNick(name);
			break;
		case 4:
			updateFriNick(name);
			break;
		}
	}
}
