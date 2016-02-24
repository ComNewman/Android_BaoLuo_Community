package com.baoluo.community.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.entity.HumorInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.EventCommentAdapter;
import com.baoluo.community.ui.adapter.SendPagerAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.ListViewForScrollView;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.customview.SmInputView;
import com.baoluo.community.ui.customview.SmInputView.SmInputListener;
import com.baoluo.community.ui.xscrollview.XScrollView;
import com.baoluo.community.ui.xscrollview.XScrollView.IXScrollViewListener;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.im.Configs;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.task.GenerateGroupAvatarTask;
import com.baoluo.im.ui.AtyMultiChatMqtt;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import de.greenrobot.event.EventBus;

/**
 * 活动详情界面
 * 
 * @author Ryan_Fu 2015-5-28
 */
public class EventDetailsActivity extends AtyBase implements OnClickListener,
		HeadViewListener, IXScrollViewListener,SmInputListener{

	private final String TAG = this.getClass().getName();

	private HeadView headView;
	private TextView tvTitle, tvNick, tvContent, tvPage;
	private Button btnStartTime, btnLocation, btnDetails,
			btnComment, btnPeopelNum;
	private Button btnAttention, btnFollow;
	private XScrollView mXScrollView;
	private RelativeLayout rlDetails, rlComment, rlList;
	private ListViewForScrollView lvComents;
	private ViewPager vpEventPics;
	private RoundImageView head;
	private EventCommentAdapter commentAdpter;
	private SmInputView smInputView;
	
	private EventInfo eventInfo;
	private List<HumorInfo> eventComments = new ArrayList<HumorInfo>();
	private int pageindex = 1;
	private int pagesize = 10;
	private HumorInfoList humorInfoList;
	private boolean followFlag, attenFlag;
	private boolean hasMore = true;
	private GroupInfo group;
	private int pageNo = 1;
	private DisplayImageOptions options;
	private String commentFormat;
	private String peopleNum;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event_details);
		Intent intent = this.getIntent();
		eventInfo = (EventInfo) intent.getSerializableExtra("eventInfo");
		L.i(TAG, "check eventInfo: "
				+ GsonUtil.getInstance().obj2Str(eventInfo));
		pageNo = intent.getIntExtra("eventPageNo", 1);
		initUI();
		initData();
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("InflateParams")
	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_head);
		headView.setHeadViewListener(this);
		mXScrollView = (XScrollView) findViewById(R.id.event_scrollview);
		mXScrollView.setPullRefreshEnable(true);
		mXScrollView.setPullLoadEnable(false);
		// mXScrollView.setAutoLoadEnable(true);
		mXScrollView.setIXScrollViewListener(this);
		mXScrollView.setRefreshTime(getTime());

		View content = LayoutInflater.from(this).inflate(
				R.layout.event_details_scroll_view_new, null);

		if (null != content) {
			L.i(TAG, "null != content");
			rlDetails = (RelativeLayout) content
					.findViewById(R.id.rl_event_details_details);
			rlList = (RelativeLayout) content
					.findViewById(R.id.rl_event_details_comment);
			vpEventPics = (ViewPager) content.findViewById(R.id.vp_imgs);
			head = (RoundImageView) content
					.findViewById(R.id.iv_event_details_avatar);
			head.setOnClickListener(this);
			tvNick = (TextView) content
					.findViewById(R.id.tv_event_details_nick);
			tvPage = (TextView) content
					.findViewById(R.id.tv_event_details_page);

			btnDetails = (Button) content.findViewById(R.id.btn_event_detail);
			btnComment = (Button) content.findViewById(R.id.btn_event_comment);
			btnDetails.setSelected(true);
			btnDetails.setOnClickListener(this);
			btnComment.setOnClickListener(this);

			tvTitle = (TextView) content
					.findViewById(R.id.tv_event_details_title);
			btnStartTime = (Button) content
					.findViewById(R.id.btn_event_details_time);
			btnLocation = (Button) content
					.findViewById(R.id.btn_event_details_location);
			btnPeopelNum = (Button) content
					.findViewById(R.id.btn_event_details_peoplenum);

			btnAttention = (Button) content
					.findViewById(R.id.btn_event_details_attention);
			btnFollow = (Button) content
					.findViewById(R.id.btn_event_details_enroll);
			btnAttention.setOnClickListener(this);
			btnFollow.setOnClickListener(this);

			tvContent = (TextView) content
					.findViewById(R.id.tv_event_details_content);

			// btnEventMore = (ImageButton) findViewById(R.id.btn_more_content);
			// btnEventMore.setOnClickListener(this);
			lvComents = (ListViewForScrollView) content
					.findViewById(R.id.lv_event_details_comment);
			lvComents.setDivider(null);
			L.i(TAG, "lvComents==null: " + (lvComents == null));
			commentAdpter = new EventCommentAdapter(EventDetailsActivity.this,
					eventComments, R.layout.event_details_comments_item);
			lvComents.setAdapter(commentAdpter);

		}
		mXScrollView.setView(content);
		smInputView = (SmInputView) findViewById(R.id.siv_event_details);
		smInputView.setSmInputListener(this);
	}

	private void initData() {
		headView.getBackground().setAlpha(76);
		initEventGroup();
		new GetTask(UrlHelper.EVENT_ONT,
				new UpdateViewHelper.UpdateViewListener(EventInfo.class) {
					@Override
					public void onComplete(Object obj) {
						eventInfo = (EventInfo) obj;
						initEvent();
					}
				}, "id", eventInfo.getId());
	}

	private void initEventGroup() {
		new GetTask(UrlHelper.GET_GROUP_INFO,
				new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
					@Override
					public void onComplete(Object obj) {
						group = (GroupInfo) obj;
					}
				}, "id", eventInfo.getId());
	}

	private void updateGroupInfo() {
		L.i(TAG, "event id: " + eventInfo.getId());
		new GetTask(UrlHelper.GET_GROUP_INFO,
				new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
					@Override
					public void onComplete(Object obj) {
						group = (GroupInfo) obj;
						GroupHelper.getInstance().insertGroup(group);
						EventBus.getDefault().post(new NotifyGroupUpdate());
						new GenerateGroupAvatarTask(group);
						MqttHelper.getInstance().subscribe(
								Configs.MQ_EVENT_PRE + group.getId());
					}
				}, "id", eventInfo.getId());
	}

	private void initEvent() {
		getCommentList(false, false);
		if (eventInfo != null) {
			// TODO  event  is  end!
			attenFlag = eventInfo.isIsAttend();
			followFlag = eventInfo.isIsFollow();
			if (attenFlag) {
				btnAttention.setText("进群");
				btnAttention.setTextColor(Color.parseColor("#333333"));
			}
			if (followFlag) {
				btnFollow.setText("取消关注");
				btnAttention.setTextColor(Color.parseColor("#999999"));
			}
			tvNick.setText(eventInfo.getOwner().getName());
			tvTitle.setText(eventInfo.getName());
			tvContent.setText(eventInfo.getContent());
			String time = getResources().getString(R.string.event_detail_time);
			String location = getResources().getString(
					R.string.event_detail_location);
			peopleNum = getResources().getString(
					R.string.event_detail_people_num);
			commentFormat = getResources().getString(
					R.string.event_detail_comment);
			btnComment.setText(String.format(commentFormat,
					eventInfo.getCommmitNum()));
			btnStartTime.setText(String.format(time, eventInfo.getStartDate(),
					eventInfo.getEntDate()));
			btnPeopelNum.setText(String.format(peopleNum, eventInfo.getUpNum(),
					eventInfo.getAttendNum()));
			if (eventInfo.getLocation() != null
					&& !StrUtils.isEmpty(eventInfo.getLocation().getName())) {
				btnLocation.setText(String.format(location, eventInfo
						.getLocation().getName()));

			} else {
				btnLocation.setText("");
			}
			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_launcher)
					.showImageOnFail(R.drawable.head_test).cacheInMemory(true)
					.cacheOnDisk(true).build();
			options.shouldShowImageOnFail();
			if (!StrUtils.isEmpty(eventInfo.getOwner().getFace())) {
				GlobalContext.getInstance().imageLoader.displayImage(eventInfo
						.getOwner().getFace(), head, options, null);
			} else {
				head.setImageDrawable(getResources().getDrawable(
						R.drawable.head_default));
			}

			if (eventInfo.getPictures() != null
					&& eventInfo.getPictures().size() > 0) {
				tvPage.setText("1/" + eventInfo.getPictures().size());
				GlobalContext.options.shouldShowImageOnFail();
				List<View> listViews = new ArrayList<View>();
				LayoutInflater mInflater = getLayoutInflater();
				for (int i = 0; i < eventInfo.getPictures().size(); i++) {
					View v = mInflater.inflate(R.layout.activity_more_imgs,
							null);
					ImageView iv = (ImageView) v.findViewById(R.id.iv_img);
					if (!StrUtils.isEmpty(eventInfo.getPictures().get(i)
							.getUrl())) {
						GlobalContext.getInstance().imageLoader.displayImage(
								eventInfo.getPictures().get(i).getUrl(), iv,
								GlobalContext.options, null);
					} else {
						iv.setImageResource(R.drawable.img_event_details_test);
					}
					listViews.add(v);
				}
				vpEventPics.setAdapter(new SendPagerAdapter(listViews));
				vpEventPics.setCurrentItem(0);
				vpEventPics
						.setOnPageChangeListener(new PageSelChangeListener());
			}
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_event_details_avatar:
			ToFriendInfoUtil.getInstance().toFriendInfo(
					EventDetailsActivity.this, eventInfo.getOwner().getId());
			break;
		case R.id.btn_event_details_attention:
			eventAttend();
			break;
		case R.id.btn_event_details_enroll:
			eventFollow();
			break;
		case R.id.btn_event_detail:
			btnDetails.setSelected(true);
			btnComment.setSelected(false);
			rlList.setVisibility(View.GONE);
			rlDetails.setVisibility(View.VISIBLE);
			smInputView.setVisibility(View.GONE);
			mXScrollView.setPullLoadEnable(false);
			break;
		case R.id.btn_event_comment:
			btnDetails.setSelected(false);
			btnComment.setSelected(true);
			rlList.setVisibility(View.VISIBLE);
			rlDetails.setVisibility(View.GONE);
			smInputView.setVisibility(View.VISIBLE);
			mXScrollView.setPullLoadEnable(true);
			break;
			
		}
	}

	private void refreshCommentList() {
		eventComments.clear();
		pageindex = 1;
		getCommentList(false, false);
	}

	private void onLoad() {
		mXScrollView.stopRefresh();
		mXScrollView.stopLoadMore();
		mXScrollView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	private void getCommentList(final boolean isLoad, final boolean loadMore) {
		new GetTask(UrlHelper.EVENT_COMMENT_LIST,
				new UpdateViewHelper.UpdateViewListener(HumorInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						humorInfoList = (HumorInfoList) obj;
						if (humorInfoList.getItems() != null
								&& humorInfoList.getItems().size() > 0) {
							if (!loadMore) {
								eventComments.clear();
								hasMore = true;
							}
							L.i(TAG, "humorInfoList.getItems().size(): "
									+ humorInfoList.getItems().size());
							eventComments.addAll(humorInfoList.getItems());
							commentAdpter.notifyDataSetChanged();
						}else{
							hasMore = false;
						}
						if (isLoad) {
							onLoad();
						}
					}
				}, "Id", eventInfo.getId(), "Pageindex", pageindex + "",
				"Pagesize", pagesize + "");
	}

	/**
	 * 活动报名
	 */
	private void eventAttend() {
		if (attenFlag) {
			Intent intent = new Intent();
			intent.setClass(this, AtyMultiChatMqtt.class);
			intent.putExtra(AtyMultiChatMqtt.EXTRA_GROUP, group);
			startActivity(intent);
		} else {
			new PostTask(UrlHelper.EVENT_ATTEND,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onCompleteExecute(String responseStr) {
							int code = ResultParse.getInstance().getResCode(
									responseStr);
							if (code != com.baoluo.community.core.Configs.RESPONSE_OK) {
								T.showShort(
										EventDetailsActivity.this,
										ResultParse.getInstance().getResStr(
												responseStr));
								return;
							}
							attenFlag = true;
							btnAttention.setText("进群");
							btnAttention.setTextColor(Color.parseColor("#333333"));
							String peopelNum = btnPeopelNum.getText()
									.toString();
							String[] str = peopelNum.split("/");
							String num = str[str.length - 1];
							btnPeopelNum.setText(String.format(peopleNum,
									eventInfo.getUpNum(), num + 1));
							updateGroupInfo();
						}
					}, "Id", eventInfo.getId());
		}

	}

	private void eventFollow() {
		new PostTask(UrlHelper.EVENT_FOLLOW,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (!followFlag) {
							// bvFollow.setSelected(true);
							btnFollow.setText("取消关注");
							btnFollow.setTextColor(Color.parseColor("#999999"));
							T.showShort(EventDetailsActivity.this, "关注完成");
						} else {
							// bvFollow.setSelected(false);
							btnFollow.setText("+ 关注");
							btnFollow.setTextColor(Color.parseColor("#ff7f7f"));
							T.showShort(EventDetailsActivity.this, "取消关注");
						}
						followFlag = !followFlag;
					}
				}, "Id", eventInfo.getId());
	}

	public class PageSelChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			tvPage.setText((index + 1) + "/" + eventInfo.getPictures().size());
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	@Override
	public void onRefresh() {
		eventComments.clear();
		pageindex = 1;
		getCommentList(true, false);
	}

	@Override
	public void onLoadMore() {
		if (hasMore) {
			++pageindex;
			getCommentList(true, true);
		} else {
			onLoad();
		}
	}

	@Override
	public void leftListener() {
		finish();
	}

	@Override
	public void rightListener() {
	}

	@Override
	public void doSend(String msg) {
		final Dialog dialog = MyProgress.getInstance(
				EventDetailsActivity.this, "评论...");
		new PostTask(UrlHelper.EVENT_COMMENT_POST,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onPreExecute() {
						super.onPreExecute();
						dialog.show();
					}

					@Override
					public void onComplete(Object obj) {
						dialog.dismiss();
						String comment = btnComment.getText().toString();
						String num = comment.replace("评论 ", "");
						L.i(TAG, "comment: " + comment + "\n num:" + num);
						btnComment.setText(String.format(commentFormat,
								Integer.parseInt(num) + 1));
						refreshCommentList();
					}
				}, "Id", eventInfo.getId(), "Content", msg);
		
	}
}
