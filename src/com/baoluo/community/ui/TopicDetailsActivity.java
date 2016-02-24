package com.baoluo.community.ui;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.PictureInfo;
import com.baoluo.community.entity.TopicComment;
import com.baoluo.community.entity.TopicCommentList;
import com.baoluo.community.entity.TopicInfo;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostObjTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.SendPagerAdapter;
import com.baoluo.community.ui.adapter.TopicDiscussAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.ListViewForScrollView;
import com.baoluo.community.ui.customview.SmInputView;
import com.baoluo.community.ui.customview.SmInputView.SmInputListener;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.ui.xscrollview.XScrollView;
import com.baoluo.community.ui.xscrollview.XScrollView.IXScrollViewListener;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.KeyBoardUtils;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.baoluo.im.util.DepthPageTransformer;
import com.baoluo.im.util.InputBoxUtil;

/**
 * 话题详情界面
 * 
 * @author xiangyang.fu
 * 
 */
public class TopicDetailsActivity extends AtyBase implements OnClickListener,
//OnXScrollListener,
		HeadViewListener, IXScrollViewListener, SmInputListener {

	private static final String TAG = "TopicDetailsActivity";
	public static final String ID = "RepId";
	public static final String TITLE = "RepTitle";
	// 数据相关
	private String id;
	private TopicInfo topic;
	private boolean isPraise, isLow;
	private List<TopicComment> topicComments = new ArrayList<TopicComment>();
	private TopicCommentList topicInfoList;
	// 控件相关
	private SmInputView smInputView;
	private HeadView headView;
	private TopicDiscussAdapter commentAdapter;
	private ImageView imgHead;
	private TextView tvNick, tvTitle, tvContent, tvPicPage, tvLowNum,
			tvPraiseNum;
	private Button btnHightReply;
	private ImageButton btnLow, btnPraise;
	private XScrollView mXScrollView;
	private ViewPager vpTopicPics;
	private ListViewForScrollView xlvComments;
	private int pageindex = 1;
	private int pagesize = 10;
	private int count;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_topic_details);
		id = getIntent().getStringExtra("id");
		L.i(TAG, "current id: " + id);
		initUI();
		initData();
	}

	private MyClickListener myListener = new MyClickListener() {

		@Override
		public void myOnClick(int position, View v) {
			TopicComment topic = topicComments.get(position);
			switch (v.getId()) {
			case R.id.iv_topic_nomal_comment_head:
			case R.id.iv_topic_hight_comment_head:
				String userId = topic.getBlogUser().getId();
				T.showShort(TopicDetailsActivity.this,
						topic.getBlogUser().getName());
				ToFriendInfoUtil.getInstance().toFriendInfo(TopicDetailsActivity.this, userId);
				break;
			default:
				break;
			}
		}
	};

	@SuppressLint("InflateParams")
	private void initUI() {
		headView = (HeadView) findViewById(R.id.hv_topic_head);
		mXScrollView = (XScrollView) findViewById(R.id.sv_topic_details);
		mXScrollView.setPullRefreshEnable(true);
		mXScrollView.setPullLoadEnable(true);
//		mXScrollView.setAutoLoadEnable(true);
		mXScrollView.setIXScrollViewListener(this);
		mXScrollView.setRefreshTime(getTime());
//		mXScrollView.setOnScrollListener(this);

		headView.setHeadViewListener(this);
		headView.getBackground().setAlpha(0);
		View content = LayoutInflater.from(this).inflate(
				R.layout.topic_details_scroll_view_new, null);

		if (null != content) {
			xlvComments = (ListViewForScrollView) content
					.findViewById(R.id.lv_topic_comments);
			commentAdapter = new TopicDiscussAdapter(topicComments, this,
					myListener);
			xlvComments.setAdapter(commentAdapter);
			xlvComments.setDivider(null);
			xlvComments.setFocusable(false);
			xlvComments.setFocusableInTouchMode(false);
			xlvComments.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent i = new Intent();
					i.setClass(TopicDetailsActivity.this, TopicDetailsActivity.class);
					L.i(TAG, "onclicked:  "+position);
					i.putExtra("id", topicComments.get(position).getId());
					startActivity(i);
				}
			});
			vpTopicPics = (ViewPager) content
					.findViewById(R.id.vp_topic_details);
			vpTopicPics.setPageTransformer(true, new DepthPageTransformer());
			btnHightReply = (Button) content
					.findViewById(R.id.tv_topic_details_hight_relpay);
			imgHead = (ImageView) content
					.findViewById(R.id.iv_topic_details_avatar);
			imgHead.setOnClickListener(this);
			tvNick = (TextView) content
					.findViewById(R.id.tv_topic_details_nick);
			tvTitle = (TextView) content
					.findViewById(R.id.tv_topic_details_title);
			tvPicPage = (TextView) content
					.findViewById(R.id.tv_topic_details_pic_page);
			tvContent = (TextView) content
					.findViewById(R.id.tv_topic_details_content);
			btnHightReply.setOnClickListener(this);
			tvLowNum = (TextView) content
					.findViewById(R.id.tv_topic_details_opposenum);
			tvPraiseNum = (TextView) content
					.findViewById(R.id.tv_topic_details_praisenum);
			btnLow = (ImageButton) content
					.findViewById(R.id.ib_topic_details_oppose);
			btnPraise = (ImageButton) content
					.findViewById(R.id.ib_topic_details_praise);
			btnPraise.setOnClickListener(this);
			btnLow.setOnClickListener(this);
		}
		mXScrollView.setView(content);
		smInputView = (SmInputView) findViewById(R.id.siv_topic_details);
		smInputView.setSmInputListener(this);
		
	}

	private void postNomalComment(String comment) {
		KeyBoardUtils.hideSoftKeyboard(TopicDetailsActivity.this);
		TopicInfo topicInfo = new TopicInfo();
		topicInfo.setTopicType(2);
		topicInfo.setRepId(topic.getId());
		topicInfo.setContent(comment);
		String paramsObj = GsonUtil.getInstance().obj2Str(topicInfo);
		new PostObjTask(UrlHelper.TOPIC_HIGHT_REPLY, paramsObj,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						T.showShort(TopicDetailsActivity.this, "评论完成");
						onRefresh();
					}
				});
	}

	private void initData() {
		new GetTask(UrlHelper.TOPIC_ONE,
				new UpdateViewHelper.UpdateViewListener(TopicInfo.class) {
					@Override
					public void onComplete(Object obj) {
						topic = (TopicInfo)obj;
							if(StrUtils.isEmpty(topic.getOwer().getFace())){
								imgHead.setImageResource(R.drawable.head_default);
							}else{
								GlobalContext.options.shouldShowImageOnFail();
								GlobalContext.getInstance().imageLoader
										.displayImage(topic.getOwer().getFace(),
												imgHead, GlobalContext.options,
												null);
							}
							getComments(UrlHelper.TOPIC_COMMENT_NEW);
							isLow = topic.isIsOpposes();
							isPraise = topic.isIsPraise();
							if (isLow) {
								btnLow.setSelected(true);
							}
							if (isPraise) {
								btnPraise.setSelected(true);
							}
							tvNick.setText(topic.getOwer().getName());
							String title = getResources().getString(
									R.string.topic_title);
							tvTitle.setText(String.format(title,
									topic.getTitle()));
							SpannableStringBuilder ssb = InputBoxUtil.getInstance()
									.changeTextColor(TopicDetailsActivity.this,topic.getContent());
							tvContent.setText(ssb);
							tvLowNum.setText(String.valueOf(topic
									.getOpposesNum()));
							tvPraiseNum.setText(String.valueOf(topic
									.getPraiseNum()));
							LayoutInflater mInflater = getLayoutInflater();
							List<View> listViews = new ArrayList<View>();
							if (topic.getPictures() != null
									&& topic.getPictures().size() > 0) {
								GlobalContext.options.shouldShowImageOnFail();
								List<PictureInfo> list = topic.getPictures();
								tvPicPage.setText("1/"
										+ String.valueOf(topic.getPictures()
												.size()));
								for (int i = 0; i < list.size(); i++) {
									View v = mInflater.inflate(
											R.layout.activity_more_imgs, null);
									ImageView iv = (ImageView) v
											.findViewById(R.id.iv_img);
									iv.setTag(i);
									if (!StrUtils.isEmpty(list.get(i).getUrl())) {
										L.i(TAG, "vp url::" + list.get(i).getUrl()+"\n");
										GlobalContext.getInstance().imageLoader
												.displayImage(
														list.get(i).getUrl(),
														iv,
														GlobalContext.options,
														null);
									} else {
										iv.setImageResource(R.drawable.img_event_details_test);
									}
									listViews.add(v);
								}
								/*for (PictureInfo pictureInfo : list) {
									ImageView iv = (ImageView) v
											.findViewById(R.id.iv_img);
									if (!StrUtils.isEmpty(pictureInfo.getUrl())) {
										L.i(TAG, "vp url::" + pictureInfo.getUrl()+"\n");
										GlobalContext.getInstance().imageLoader
												.displayImage(
														pictureInfo.getUrl(),
														iv,
														GlobalContext.options,
														null);
									} else {
										iv.setImageResource(R.drawable.img_event_details_test);
									}
									listViews.add(v);
								}*/
								vpTopicPics.setAdapter(new SendPagerAdapter(
										listViews));
								vpTopicPics.setCurrentItem(0);
								vpTopicPics
										.setOnPageChangeListener(new PageSelChangeListener());
							} else {
								tvPicPage.setText("0/0");
								View v = mInflater.inflate(
										R.layout.activity_more_imgs, null);
								ImageView iv = (ImageView) v
										.findViewById(R.id.iv_img);
								iv.setImageResource(R.drawable.bg_no_pic);
								listViews.add(v);
								vpTopicPics.setAdapter(new SendPagerAdapter(
										listViews));
								vpTopicPics.setCurrentItem(0);
							}
					}
				}, "id", id);
	}

	public class PageSelChangeListener implements OnPageChangeListener {
		@Override
		public void onPageSelected(int index) {
			tvPicPage.setText((index + 1) + "/"
					+ String.valueOf(topic.getPictures().size()));
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {
		}

		@Override
		public void onPageScrollStateChanged(int arg0) {
		}
	}

	private void getComments(String url) {
		topicComments.clear();
		pageindex = 1;
		new GetTask(url, new UpdateViewHelper.UpdateViewListener(TopicCommentList.class) {
			@Override
			public void onComplete(Object obj) {
				topicComments.clear();
				topicInfoList = (TopicCommentList) obj;
				count = topicInfoList.getCount();
				if (topicInfoList.getItems().size() > 0) {
					topicComments.addAll(topicInfoList.getItems());
				}
				commentAdapter.notifyDataSetChanged();
			}
		},"Id", topic.getId(),
				"PageIndex", String.valueOf(pageindex),
				"PageSize", String.valueOf(pagesize));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_topic_details_avatar:
			ToFriendInfoUtil.getInstance().toFriendInfo(TopicDetailsActivity.this, topic.getOwer().getId());
			break;
		case R.id.ib_topic_details_praise:
			praise();
			break;
		case R.id.ib_topic_details_oppose:
			trample();
			break;
		case R.id.tv_topic_details_hight_relpay:
			if (topic != null) {
				Intent i = new Intent();
				i.setClass(TopicDetailsActivity.this,
						PublishTopicActivity.class);
				i.putExtra(ID, topic.getId());
				i.putExtra(TITLE, topic.getTitle());
				startActivity(i);
			} else {
				T.showShort(this, "网络或API出现异常！");
			}
			break;
		}
	}

	private void trample() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", id);
		new PostTask(UrlHelper.TOPIC_LOW, params,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (!isLow) {
							btnLow.setSelected(true);
							btnPraise.setSelected(false);
							tvPraiseNum.setSelected(false);
							tvLowNum.setSelected(true);
							isPraise = false;
							isLow = true;
							if (topic.isIsPraise()) {
								tvPraiseNum.setText(String.valueOf(topic
										.getPraiseNum() - 1));
							} else {
								tvPraiseNum.setText(String.valueOf(topic
										.getPraiseNum()));
							}
							if (topic.isIsOpposes()) {
								tvLowNum.setText(String.valueOf(topic
										.getOpposesNum()));
							} else {
								tvLowNum.setText(String.valueOf(topic
										.getOpposesNum() + 1));
							}
						} else {
							btnLow.setSelected(false);
							tvLowNum.setSelected(false);
							isLow = false;
							if (topic.isIsOpposes()) {
								tvLowNum.setText(String.valueOf(topic
										.getOpposesNum() - 1));
							} else {
								tvLowNum.setText(String.valueOf(topic
										.getOpposesNum()));
							}
						}
					}
				});
	}

	private void praise() {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", topic.getId());
		new PostTask(UrlHelper.TOPIC_PRAISE, params,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onComplete(Object obj) {
						if (!isPraise) {
							btnPraise.setSelected(true);
							btnLow.setSelected(false);
							tvPraiseNum.setSelected(true);
							tvLowNum.setSelected(false);
							isPraise = true;
							isLow = false;
							if (topic.isIsOpposes()) {
								tvLowNum.setText(String.valueOf(topic
										.getOpposesNum() - 1));
							} else {
								tvLowNum.setText(String.valueOf(topic
										.getOpposesNum()));
							}
							if (topic.isIsPraise()) {
								tvPraiseNum.setText(String.valueOf(topic
										.getPraiseNum()));
							} else {
								tvPraiseNum.setText(String.valueOf(topic
										.getPraiseNum() + 1));
							}
						} else {
							btnPraise.setSelected(false);
							tvPraiseNum.setSelected(false);
							isPraise = false;
							if (topic.isIsPraise()) {
								tvPraiseNum.setText(String.valueOf(topic
										.getPraiseNum() - 1));
							} else {
								tvPraiseNum.setText(String.valueOf(topic
										.getPraiseNum()));
							}
						}
					}
				});
	}

	/*
	 * private void refreshCommentList() { topicComments.clear(); pageindex = 1;
	 * getComments(UrlHelper.TOPIC_COMMENT_WONDERFUL, 2);
	 * getComments(UrlHelper.TOPIC_COMMENT_NEW, 1); }
	 */

	private void onLoad() {
		mXScrollView.stopRefresh();
		mXScrollView.stopLoadMore();
		mXScrollView.setRefreshTime(getTime());
	}

	private String getTime() {
		return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
				.format(new Date());
	}

	private void refreshComment(String url) {
		topicComments.clear();
		pageindex = 1;
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", topic.getId());
		params.put("PageIndex", String.valueOf(pageindex));
		params.put("PageSize", String.valueOf(pagesize));
		new GetTask(url, params, new UpdateViewHelper.UpdateViewListener(TopicCommentList.class) {
			@Override
			public void onComplete(Object obj) {
				topicInfoList = (TopicCommentList) obj;
				count = topicInfoList.getCount();
				if (topicInfoList.getItems().size() > 0) {
					topicComments.addAll(topicInfoList.getItems());
				}
				commentAdapter.notifyDataSetChanged();
				onLoad();
			}
		});
	}

	@Override
	public void onRefresh() {
		refreshComment(UrlHelper.TOPIC_COMMENT_NEW);
	}

	private void loadMorComment(String url) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("Id", topic.getId());
		params.put("PageIndex", String.valueOf(++pageindex));
		params.put("PageSize", String.valueOf(pagesize));
		new GetTask(url, params, new UpdateViewHelper.UpdateViewListener(TopicCommentList.class) {
			@Override
			public void onComplete(Object obj) {
					topicInfoList = (TopicCommentList)obj;
					count = topicInfoList.getCount();
					if (topicInfoList.getItems().size() > 0) {
						topicComments.addAll(topicInfoList.getItems());
					}
					commentAdapter.notifyDataSetChanged();
					onLoad();
			}
		});
	}

	@Override
	public void onLoadMore() {
		if(topicComments.size()<count){
			loadMorComment(UrlHelper.TOPIC_COMMENT_NEW);
		}else{
			onLoad();
		}
	}


//	@Override
//	public void onScrollStateChanged(AbsListView view, int scrollState) {
//		
//	}
//
//	@Override
//	public void onScroll(AbsListView view, int firstVisibleItem,
//			int visibleItemCount, int totalItemCount) {
//	}
//
//	@Override
//	public void onXScrolling(View view) {
//		
//	}

	@Override
	public void leftListener() {
		finish();
	}

	@Override
	public void rightListener() {
		
	}

	@Override
	public void doSend(String msg) {
		postNomalComment(msg);
		
	}

}
