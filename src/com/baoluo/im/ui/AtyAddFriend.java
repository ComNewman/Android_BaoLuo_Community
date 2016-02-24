package com.baoluo.im.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.TextView;

import com.baoluo.community.core.Configs;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.AtyBase;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.MyDialog;
import com.baoluo.community.ui.customview.MyDialog.MyDialogListener;
import com.baoluo.community.ui.customview.SlideCutListView;
import com.baoluo.community.ui.customview.SlideCutListView.SlideCutViewListener;
import com.baoluo.community.ui.customview.SlideCutView;
import com.baoluo.community.ui.customview.SlideCutView.OnSlideListener;
import com.baoluo.community.ui.xlistview.XListView;
import com.baoluo.community.ui.xlistview.XListView.IXListViewListener;
import com.baoluo.community.util.L;
import com.baoluo.community.util.T;
import com.baoluo.im.entity.ContactSearch;
import com.baoluo.im.entity.ContactsInfoList;
import com.baoluo.im.entity.NearBy;
import com.baoluo.im.entity.NearByList;
import com.baoluo.im.ui.adapter.FriSearchListAdapter;
import com.baoluo.im.ui.adapter.NearByAdapter;

/**
 * 加好友
 * 
 * @author xiangyang.fu
 * 
 */
public class AtyAddFriend extends AtyBase implements OnSlideListener,
		IXListViewListener, SlideCutViewListener, HeadViewListener{
	private static final String TAG = "AtyAddFriend";

	private HeadView headView;
	private SlideCutListView sclvList;
	private EditText etSearch;
	private SlideCutView scv;
	private XListView xlvNearby;
	private List<ContactSearch> list;
	private TextView tvTitle;
	private FriSearchListAdapter adpter;

	private List<NearBy> nearByList;
	private NearByAdapter nearbyAdapter;
	private NearByList nears;
	private int PageIndex = 1;
	private int PageSize = 10;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.aty_add_friend);
		initUI();
		initData();
	}

	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_head);
		sclvList = (SlideCutListView) findViewById(R.id.sclv_list);
		etSearch = (EditText) findViewById(R.id.et_addfriend_search);
		tvTitle = (TextView) findViewById(R.id.tv_fujin);
		headView.setHeadViewListener(this);

		xlvNearby = (XListView) findViewById(R.id.xlv_nearby);
		xlvNearby.setPullLoadEnable(true);
		xlvNearby.setPullRefreshEnable(true);
		xlvNearby.setXListViewListener(this);
		xlvNearby.setOnItemClickListener( new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				L.i(TAG, "xlvNearby  onItemClick");
				showDialog(nearByList.get(position-1).getUid(),
						nearByList.get(position-1).getUserNick());
				
			}
		});
	}

	private void initData() {
		initNear();
		sclvList.setSlideCutViewListener(this);
		list = new ArrayList<ContactSearch>();
		adpter = new FriSearchListAdapter(AtyAddFriend.this, AtyAddFriend.this,
				list);
		sclvList.setAdapter(adpter);
		etSearch.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				String keyword = s.toString().trim();
				if (!TextUtils.isEmpty(keyword) && keyword.length() > 1) {
					xlvNearby.setVisibility(View.GONE);
					sclvList.setVisibility(View.VISIBLE);
					doSearch(keyword);
				} else {
					tvTitle.setText("附近的人");
					xlvNearby.setVisibility(View.VISIBLE);
					sclvList.setVisibility(View.GONE);
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

		sclvList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				SlideCutView slideView = list.get(position).getSlideCutView();
				if (slideView.ismIsMoveClick()) {
					return;
				}
				if (slideView.close() && slideView.getScrollX() == 0) {
					if (scv == null || scv.getScrollX() == 0) {
						showDialog(list.get(position).getAccountID(),
								list.get(position).getAccountName());
					}
				}
			}
		});
	}

	private void initNear() {
		nearByList = new ArrayList<NearBy>();
		nearbyAdapter = new NearByAdapter(this, nearByList,
				R.layout.item_add_fri_nearby);
		xlvNearby.setAdapter(nearbyAdapter);

		new GetTask(UrlHelper.LBS_NEAR_USER,
				new UpdateViewHelper.UpdateViewListener(NearByList.class) {
					@Override
					public void onComplete(Object obj) {
						nears = (NearByList) obj;
						if (nears.getItems() != null
								&& nears.getItems().size() > 0) {
							nearByList.clear();
							nearByList.addAll(nears.getItems());
							nearbyAdapter.notifyDataSetChanged();
						}
					}
				}, "PageIndex", String.valueOf(PageIndex), "PageSize",
				String.valueOf(PageSize));

	}

	private void doSearch(String keyword) {
		new GetTask(UrlHelper.FRIEND_SEARCH,
				new UpdateViewHelper.UpdateViewListener(ContactsInfoList.class) {
					@Override
					public void onComplete(Object data) {
							ContactsInfoList clist = (ContactsInfoList)data;
							tvTitle.setText("搜索好友结果");
							list.clear();
							list.addAll(ContactSearch.listSwitch(clist
									.getItems()));
							if (list.size() > 0) {
								adpter.notifyDataSetChanged();
							}
					}
				}, "Keyword", keyword);
	}

	@Override
	public SlideCutView getSlideCutView(int position) {
		return list.get(position).getSlideCutView();
	}

	@Override
	public void onSlide(View view, int status) {
		if (scv != null && scv != view) {
			scv.shrink();
		}
		if (status == SLIDE_STATUS_ON) {
			scv = (SlideCutView) view;
		}
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_OK) {
			if (requestCode == AtyFriMenu.OPTION_CODE) {
				int optionType = data.getIntExtra(AtyFriMenu.OPTION_TYPE, -1);
				switch (optionType) {
				case AtyFriMenu.FRI_JUBAO:
					T.showShort(this, "执行举报");
					break;
				case AtyFriMenu.FRI_LAHEI:
					T.showShort(this, "执行拉黑");
					break;
				}
			}
		}
	}

	private void showDialog(final String Uid, String account) {
		MyDialog dialog = new MyDialog(this, "添加" + account + "为好友？",
				new MyDialogListener() {
					@Override
					public void sure() {
						new PostTask(UrlHelper.FRIEND_ADD,
								new UpdateViewHelper.UpdateViewListener() {
									@Override
									public void onCompleteExecute(
											String responseStr) {
										if (!responseStr
												.equals(Configs.RESPONSE_ERROR)) {
											T.showShort(AtyAddFriend.this,
													"请求发送成功，请等待好友验证");
										} else {
											T.showShort(AtyAddFriend.this,
													"添加失败");
										}
									}
								}, "Id", Uid);
					}
				});
		dialog.show();
		/*AlertDialog.Builder builder = new AlertDialog.B
		uilder(this);
		builder.setIcon(R.drawable.ic_launcher);
		builder.setTitle("添加好友");
		builder.setMessage("添加" + account + "为好友？");
		builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				new PostTask(UrlHelper.FRIEND_ADD,
						new UpdateViewHelper.UpdateViewListener() {
							@Override
							public void onCompleteExecute(String responseStr) {
								if (!responseStr.equals(Configs.RESPONSE_ERROR)) {
									T.showShort(AtyAddFriend.this,
											"添加完成，请等待好友验证");
								} else {
									T.showShort(AtyAddFriend.this, "添加失败");
								}
							}
						}, "Id", Uid);
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {

			}
		});
		builder.show();*/
	}

	@Override
	public void leftListener() {
		onBackPressed();
	}

	@Override
	public void rightListener() {

	}

	@Override
	public void onRefresh() {
		// TODO Auto-generated method stub
		xlvNearby.stopRefresh();

	}

	@Override
	public void onLoadMore() {
		// TODO Auto-generated method stub
		xlvNearby.stopLoadMore();
	}
}
