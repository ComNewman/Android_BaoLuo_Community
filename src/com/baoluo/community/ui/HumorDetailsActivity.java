package com.baoluo.community.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.CommentInfo;
import com.baoluo.community.entity.CommentInfoList;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.entity.VoiceInfo;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.HumorCommentAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.ListViewForScrollView;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.customview.SmInputView;
import com.baoluo.community.ui.customview.SmInputView.SmInputListener;
import com.baoluo.community.ui.customview.TagShowImage;
import com.baoluo.community.ui.fragment.HumorFragment;
import com.baoluo.community.ui.xscrollview.XScrollView;
import com.baoluo.community.ui.xscrollview.XScrollView.IXScrollViewListener;
import com.baoluo.community.util.KeyBoardUtils;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.ToFriendInfoUtil;

/**
 * 心情详情页面
 * 
 * @author Ryan_Fu 2015-5-19
 */
@SuppressLint("ClickableViewAccessibility")
public class HumorDetailsActivity extends AtyBase implements 
//OnGestureListener,
		HeadViewListener, OnClickListener, IXScrollViewListener,
		SmInputListener {
	private static final String TAG = "HumorDetailsActivity";

	private HeadView headView;
	private ImageButton ibShare;
	private ImageView ivHumor, ivVoicePlaying;
	private RoundImageView imgHead;
	private TextView tvContent, tvPraise, tvComment, tvNick, tvTime,
			tvLocation;
	private ListViewForScrollView xlvComment;
	private HumorCommentAdapter commentAdapter;
	private XScrollView mXScrollView;
	private TagShowImage tsiTags;
	private SmInputView smInputView;
	private ImageView ivPraiseAnim;
	private HumorInfo humor;
	private VoiceInfo voice;
	private CommentInfoList commentList;
	private List<CommentInfo> comments = new ArrayList<CommentInfo>();
	private String humorId;
	private String homePassedId;
	private String praiseNum, commentNum;
	private boolean isPraise;
	private GestureDetector mGd;
	private AnimationDrawable animationDrawable;
	private int pageindex = 1;
	private int pagesize = 20;
	private int pageNo = 1;
	private MediaPlayer player;
	private boolean hasMore = true;
	
	private Animation animation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_humor_details);
		Intent intent = getIntent();
		humor = (HumorInfo) intent.getSerializableExtra("humor");
		homePassedId = intent.getStringExtra("humorId");
		L.i(TAG, "check homePassedId::" + homePassedId);
		pageNo = intent.getIntExtra("humorPageNo", 1);
		L.i(TAG, "pageNo = " + pageNo + " / humors.size = "
				+ HumorFragment.humors.size());
		initUI();
//		mGd = new GestureDetector(HumorDetailsActivity.this, this);
		initData();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (null != player) {
			player.stop();
			player.release();
			player = null;
		}
	}

	/**
	 * 初始化控件
	 */
	@SuppressLint("InflateParams")
	private void initUI() {
		animation = AnimationUtils.loadAnimation(HumorDetailsActivity.this,R.anim.animation_praise);
		headView = (HeadView) findViewById(R.id.hv_head);
		mXScrollView = (XScrollView) findViewById(R.id.rrrrrrr);
		mXScrollView.setPullRefreshEnable(true);
		mXScrollView.setPullLoadEnable(true);
		mXScrollView.setAutoLoadEnable(true);
		mXScrollView.setIXScrollViewListener(this);
		mXScrollView.setRefreshTime(getTime());

		headView.setHeadViewListener(this);

		View content = LayoutInflater.from(this).inflate(
				R.layout.act_scroll_view, null);
		
		if (null != content) {
			praiseNum = getResources().getString(R.string.topic_praise_num);
			commentNum = getResources().getString(R.string.topic_comment_num);
			xlvComment = (ListViewForScrollView) content
					.findViewById(R.id.xlv_humor_comments);
			xlvComment.setFocusable(false);
			xlvComment.setFocusableInTouchMode(false);
			xlvComment.setDivider(null);
			xlvComment.setAdapter(commentAdapter);
			ivHumor = (ImageView) content.findViewById(R.id.iv_humor_details);
			ivVoicePlaying = (ImageView) content
					.findViewById(R.id.iv_humor_details_voice_playing);
			animationDrawable = (AnimationDrawable) ivVoicePlaying
					.getBackground();
			animationDrawable.setOneShot(false);
			tsiTags = (TagShowImage) content.findViewById(R.id.tsi_tags);
			imgHead = (RoundImageView) content
					.findViewById(R.id.iv_humor_details_avatar);
			imgHead.setOnClickListener(this);
			tvContent = (TextView) content
					.findViewById(R.id.tv_humor_details_content);
			ivVoicePlaying.setOnClickListener(this);
			ivHumor.setOnClickListener(this);
			
			ivPraiseAnim = (ImageView) findViewById(R.id.iv_humor_details_praise);
			
			tvPraise = (TextView) content
					.findViewById(R.id.tv_humor_details_praise);
			tvComment = (TextView) content
					.findViewById(R.id.tv_humor_details_comment);
			ibShare = (ImageButton) content.findViewById(R.id.ib_humor_share);
			tvPraise.setOnClickListener(this);
			ibShare.setOnClickListener(this);

			tvNick = (TextView) content
					.findViewById(R.id.tv_humor_details_user);
			tvTime = (TextView) content
					.findViewById(R.id.tv_humor_details_time);
			tvLocation = (TextView) content
					.findViewById(R.id.tv_humor_details_location);

		}
		mXScrollView.setView(content);
		smInputView = (SmInputView) findViewById(R.id.siv_humor_details);
		smInputView.setSmInputListener(this);
		
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		commentAdapter = new HumorCommentAdapter(HumorDetailsActivity.this,
				comments, R.layout.humor_details_lv_item, null);
		xlvComment.setAdapter(commentAdapter);
		getData();
	}

	private void getData() {
		if (humor != null) {
			if (humor.getPictures() != null && humor.getPictures().size() > 0) {
				tsiTags.setTagList(humor.getPictures().get(0).getTags());
				tsiTags.initTags();
			}
			new GetTask(UrlHelper.HUMOR_GET,
					new UpdateViewHelper.UpdateViewListener(HumorInfo.class) {
						@Override
						public void onComplete(Object obj) {
							HumorInfo humorInfo = (HumorInfo) obj;
							setData(humorInfo);
						}
					}, "id", humor.getId());
			getComment(humor.getId(), true, false);
		} else {
			humorId = homePassedId;
			if (!StrUtils.isEmpty(homePassedId)) {
				new GetTask(
						UrlHelper.HUMOR_GET,
						new UpdateViewHelper.UpdateViewListener(HumorInfo.class) {
							@Override
							public void onComplete(Object obj) {
								humor = (HumorInfo) obj;
								setData(humor);
							}
						}, "id", homePassedId);
				getComment(homePassedId, true, false);
			}
		}
	}

	private void getComment(String humorId, final boolean isRefresh,
			final boolean isLoadMore) {
		new GetTask(UrlHelper.HUMOR_COMMENT,
				new UpdateViewHelper.UpdateViewListener(CommentInfoList.class) {
					@Override
					public void onComplete(Object obj) {
						commentList = (CommentInfoList) obj;
						if (commentList.getItems() != null
								&& commentList.getItems().size()>0) {
							if (isRefresh) {
								comments.clear();
								comments.addAll(commentList.getItems());
							}
							if (isLoadMore) {
								comments.addAll(commentList.getItems());
							}
							if (isRefresh || isLoadMore) {
								onLoad();
							}
							tvComment.setText(String.format(commentNum,
									comments.size()));
							commentAdapter.notifyDataSetChanged();
						} else {
							hasMore = false;
							onLoad();
						}
					}
				}, "Id", humorId, "Pageindex", String.valueOf(pageindex),
				"Pagesize", String.valueOf(pagesize));
	}

	/**
	 * 设置数据
	 */
	private void setData(HumorInfo humor) {
		humorId = humor.getId();
		tvPraise.setText(String.format(praiseNum, humor.getPraiseNum()));
		tvComment.setText(String.format(commentNum, humor.getCommentNum()));
		tvNick.setText(humor.getBlogUser().getName());
		tvLocation.setText(humor.getDistance());
		tvTime.setText(humor.getPostTime());
		isPraise = humor.isIsPraise();
		if (isPraise) {
			tvPraise.setSelected(true);
		}
		voice = humor.getVoice();
		if (voice != null && !StrUtils.isEmpty(voice.getVoiceUri())) {
			ivVoicePlaying.setVisibility(View.VISIBLE);
		}
		if (humor.getPictures().size() > 0
				&& humor.getPictures().get(0).getUrl().length() > 0) {
			GlobalContext.getInstance().imageLoader.displayImage(humor
					.getPictures().get(0).getUrl(), ivHumor,
					GlobalContext.options, null);
		}
		if (!StrUtils.isEmpty(humor.getBlogUser().getFace())) {
			GlobalContext.getInstance().imageLoader.displayImage(humor
					.getBlogUser().getFace(), imgHead, GlobalContext.options,
					null);
		} else {
			imgHead.setImageResource(R.drawable.head_default);
		}
		tvContent.setText(humor.getContent());
	}


//	private int verticalMinDistance = 50;
//
//	@Override
//	public boolean onDown(MotionEvent e) {
//
//		return false;
//	}
//
//	@Override
//	public boolean dispatchTouchEvent(MotionEvent ev) {
//		mGd.onTouchEvent(ev);
//		return super.dispatchTouchEvent(ev);
//	}
//
//	@Override
//	public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
//			float velocityY) {
//		float x = e1.getX() - e2.getX();
//		float y = e1.getY() - e2.getY();
//		if ((e1.getX() - e2.getX()) > verticalMinDistance
//				&& Math.abs(x) > Math.abs(y)) {
//			if (HumorFragment.humors.size() > 0
//					&& (pageNo < HumorFragment.humors.size() - 1)) {
//				Intent intent = new Intent();
//				intent.setClass(this, HumorDetailsActivity.class);
//				HumorInfo humor = HumorFragment.humors.get(++pageNo);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("humor", humor);
//				bundle.putInt("humorPageNo", pageNo);
//				intent.putExtras(bundle);
//				startActivity(intent);
//				overridePendingTransition(R.anim.in_from_right,
//						R.anim.out_to_left);
//				finish();
//			} else {
//				T.showShort(this, "下一页没有了哦");
//			}
//		} else if ((e2.getX() - e1.getX()) > verticalMinDistance
//				&& Math.abs(x) > Math.abs(y)) {
//			if (pageNo > 0 && HumorFragment.humors.size() > 0) {
//				Intent intent = new Intent();
//				intent.setClass(this, HumorDetailsActivity.class);
//				HumorInfo humor = HumorFragment.humors.get(--pageNo);
//				Bundle bundle = new Bundle();
//				bundle.putSerializable("humor", humor);
//				bundle.putInt("humorPageNo", pageNo);
//				intent.putExtras(bundle);
//				startActivity(intent);
//				overridePendingTransition(R.anim.in_from_left,
//						R.anim.out_to_right);
//				finish();
//			} else {
//				T.showShort(this, "上一页没有了哦");
//			}
//		}
//		return false;
//	}
//
//	@Override
//	public void onShowPress(MotionEvent e) {
//	}
//
//	@Override
//	public boolean onSingleTapUp(MotionEvent e) {
//		return false;
//	}
//
//	@Override
//	public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
//			float distanceY) {
//		// if (etComment.hasFocus()) {
//		// KeyBoardUtils.hideSoftKeyboard(HumorDetailsActivity.this);
//		// etComment.setText("");
//		// }
//		return false;
//	}
//
//	@Override
//	public void onLongPress(MotionEvent e) {
//
//	}

	private void postComment(String comment) {
		KeyBoardUtils.hideSoftKeyboard(HumorDetailsActivity.this);
		final Dialog dialog = MyProgress.getInstance(HumorDetailsActivity.this,
				"评论...");
		new PostTask(UrlHelper.HUMOR_COMMENT,
				new UpdateViewHelper.UpdateViewListener() {
					@Override
					public void onPreExecute() {
						super.onPreExecute();
						dialog.show();
					}

					@Override
					public void onComplete(Object object) {
						dialog.dismiss();
						onRefresh();
					}
				}, "Id", humorId, "Content", comment);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.iv_humor_details_avatar:
			ToFriendInfoUtil.getInstance().toFriendInfo(
					HumorDetailsActivity.this, humor.getBlogUser().getId());
			break;
		case R.id.iv_humor_details_voice_playing:
			if (animationDrawable.isRunning()) {
				animationDrawable.stop();
				stopPlayVoice();
				animationDrawable.selectDrawable(0);
			} else {
				animationDrawable.start();
				palyNetVoice();
			}
			break;
		case R.id.iv_humor_details:
			tsiTags.switchTags();
			break;
		case R.id.tv_humor_details_praise:
			ivPraiseAnim.setVisibility(View.VISIBLE);
			ivPraiseAnim.startAnimation(animation);
			new Handler().postDelayed(new Runnable(){
	            public void run() {
	            	ivPraiseAnim.setVisibility(View.GONE);
	            } 
			}, 1000);
			new PostTask(UrlHelper.HUMOR_PRAISE,
					new UpdateViewHelper.UpdateViewListener() {
						@Override
						public void onComplete(Object obj) {
							tvPraise.setSelected(!tvPraise.isSelected());
							String s = tvPraise.getText().toString();
							int praise = getCurPraiseNum(s);
							if (!isPraise) {
								tvPraise.setText(String.format(praiseNum,
										praise + 1));
								T.showShort(HumorDetailsActivity.this, "点赞完成");
							} else {
								tvPraise.setText(String.format(praiseNum,
										praise - 1));
								T.showShort(HumorDetailsActivity.this, "取消点赞");
							}
							// getData();
							isPraise = !isPraise;
						}

					}, "Id", humorId);
			break;
		case R.id.ib_humor_share:
			Intent i = new Intent();
			i.setClass(this,AtyInform.class);
			Bundle b = new Bundle();
			b.putSerializable("user", humor.getBlogUser());
			b.putString("Mid", humor.getId());
			b.putInt("Modules", 0);
			i.putExtras(b);
			startActivity(i);
			break;
		}
	}

	private int getCurPraiseNum(String s) {
		String regEx = "[^0-9]";
		Pattern p = Pattern.compile(regEx);
		Matcher m = p.matcher(s);
		String num = m.replaceAll("").trim();
		int start = s.indexOf(num);
		int end = start + num.length();
		String praise = s.substring(start, end);
		return Integer.parseInt(praise);
	}

	/**
	 * 停止播放
	 */
	public void stopPlayVoice() {
		if (null != player) {
			player.stop();
			player.release();
			player = null;
		}
	}

	/**
	 * 通过url播放语音
	 */
	private void palyNetVoice() {
		player = new MediaPlayer();
		player.setOnCompletionListener(new OnCompletionListener() {
			@Override
			public void onCompletion(MediaPlayer mp) {
				if (animationDrawable.isRunning()) {
					animationDrawable.stop();
					animationDrawable.selectDrawable(0);
					stopPlayVoice();
				}
			}
		});
		if (player != null) {
			try {
				player.setDataSource(voice.getVoiceUri());
				player.prepare();
				player.start();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (IllegalStateException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

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

	@Override
	public void onRefresh() {
		L.i(TAG, "onRefresh");
		comments.clear();
		hasMore = true;
		pageindex = 1;
		getComment(humorId, true, false);
	}

	@Override
	public void onLoadMore() {
		if (hasMore) {
			pageindex++;
			getComment(humorId, false, true);
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
		postComment(msg);
	}

}
