package com.baoluo.community.ui;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.customview.SmInputView;
import com.baoluo.community.ui.customview.TagShowImage;
import com.baoluo.community.util.KeyBoardUtils;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * Created by tao.lai on 2015/11/11 0011.
 */
public class AtyHumorDetail extends AtyBase implements HeadView.HeadViewListener,SmInputView.SmInputListener,View.OnClickListener{
    private static final String TAG = "AtyHumorDetail";

    private HumorInfo humor;
    private String humorId;
    private VoiceInfo voice;
    private CommentInfoList commentList;
    private List<CommentInfo> comments = new ArrayList<CommentInfo>();
    private HumorCommentAdapter commentAdapter;

    private String praiseNum, commentNum;
    private boolean isPraise;
    private AnimationDrawable animationDrawable;
    private MediaPlayer player;
    private boolean hasMore = true;
    private int pageSize = 20;
    private int pageIndex = 1;

    private PullToRefreshListView plv;
    private View humorView,footView;
    private SmInputView smInputView;
    private HeadView headView;

    private TagShowImage viewTags;
    private ImageButton btnShare;
    private RoundImageView ivAvatar;
    private ImageView ivHumor,ivVoice,ivPraiseAnim;
    private TextView tvUserName,tvTime,tvLocation,tvContent,tvPraise,tvComment;

    private Animation animation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_sns_detail);

        Intent intent = getIntent();
        humor = (HumorInfo) intent.getSerializableExtra("humor");
        humorId = intent.getStringExtra("humorId");
        init();
    }

    private void init(){
    	animation = AnimationUtils.loadAnimation(AtyHumorDetail.this,R.anim.animation_praise);
        headView = (HeadView) findViewById(R.id.hv_head);
        plv = (PullToRefreshListView) findViewById(R.id.lv_detail);
        smInputView = (SmInputView) findViewById(R.id.siv_details);

        humorView = View.inflate(this, R.layout.view_humor_detail, null);
        ivAvatar = (RoundImageView) humorView.findViewById(R.id.iv_humor_details_avatar);
        tvUserName = (TextView) humorView.findViewById(R.id.tv_humor_details_user);
        tvTime = (TextView) humorView.findViewById(R.id.tv_humor_details_time);
        tvLocation = (TextView) humorView.findViewById(R.id.tv_humor_details_location);
        ivHumor = (ImageView) humorView.findViewById(R.id.iv_humor_details);
        ivVoice = (ImageView) humorView.findViewById(R.id.iv_humor_details_voice_playing);
        viewTags = (TagShowImage) humorView.findViewById(R.id.tsi_tags);
        tvContent = (TextView) humorView.findViewById(R.id.tv_humor_details_content);
        tvPraise = (TextView) humorView.findViewById(R.id.tv_humor_details_praise);
        tvComment = (TextView) humorView.findViewById(R.id.tv_humor_details_comment);
        btnShare = (ImageButton) humorView.findViewById(R.id.ib_humor_share);
        ivPraiseAnim = (ImageView) humorView.findViewById(R.id.iv_humor_details_praise);
        footView = View.inflate(this, R.layout.view_foot_loading, null);
        ListView listView = plv.getRefreshableView();
        listView.addHeaderView(humorView);

        initData();
        initListener();
    }

    private void initData(){
        headView.setHeadTitle("心情详情");
        praiseNum = getResources().getString(R.string.topic_praise_num);
        commentNum = getResources().getString(R.string.topic_comment_num);
        commentAdapter = new HumorCommentAdapter(AtyHumorDetail.this,
                comments, R.layout.humor_details_lv_item, null);
        plv.setAdapter(commentAdapter);

        if(humor != null){
            humorId = humor.getId();
        }
        new GetTask(UrlHelper.HUMOR_GET,
                new UpdateViewHelper.UpdateViewListener(HumorInfo.class) {
                    @Override
                    public void onComplete(Object obj) {
                        humor = (HumorInfo) obj;
                        setDatas();
                        loadComments(1);
                    }
                }, "id", humorId);
    }

    private void setDatas(){
        if (humor.getPictures() != null && humor.getPictures().size() > 0) {
            viewTags.setTagList(humor.getPictures().get(0).getTags());
            viewTags.initTags();
        }
        tvPraise.setText(String.format(praiseNum, humor.getPraiseNum()));
        tvComment.setText(String.format(commentNum, humor.getCommentNum()));
        tvUserName.setText(humor.getBlogUser().getName());
        tvLocation.setText(humor.getDistance());
        tvTime.setText(humor.getPostTime());
        isPraise = humor.isIsPraise();
        if (isPraise) {
            tvPraise.setSelected(true);
        }
        voice = humor.getVoice();
        if (voice != null && !StrUtils.isEmpty(voice.getVoiceUri())) {
            ivVoice.setVisibility(View.VISIBLE);
        }
        if (humor.getPictures().size() > 0
                && humor.getPictures().get(0).getUrl().length() > 0) {
            GlobalContext.getInstance().imageLoader.displayImage(humor
                            .getPictures().get(0).getUrl(), ivHumor,
                    GlobalContext.options, null);
        }
        if (!StrUtils.isEmpty(humor.getBlogUser().getFace())) {
            GlobalContext.getInstance().imageLoader.displayImage(humor
                            .getBlogUser().getFace(), ivAvatar, GlobalContext.options,
                    null);
        } else {
            ivVoice.setImageResource(R.drawable.head_default);
        }
        tvContent.setText(humor.getContent());
    }

    private void initListener(){
        headView.setHeadViewListener(this);
        ivAvatar.setOnClickListener(this);
        ivHumor.setOnClickListener(this);
        ivVoice.setOnClickListener(this);
        btnShare.setOnClickListener(this);
        tvPraise.setOnClickListener(this);
        smInputView.setSmInputListener(this);

        animationDrawable = (AnimationDrawable) ivVoice.getBackground();
        animationDrawable.setOneShot(false);

        plv.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener<ListView>() {
            @Override
            public void onRefresh(PullToRefreshBase<ListView> refreshView) {
                loadComments(1);
            }
        });

        plv.setOnLastItemVisibleListener(       // 滑动到底部最后一个item监听
                new PullToRefreshBase.OnLastItemVisibleListener() {
                    @Override
                    public void onLastItemVisible() {
                        loadComments(++pageIndex);
                    }
                });

        plv.setOnScrollListener(new AbsListView.OnScrollListener() {  //滚动状态监听
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 0-pullHead 1-detailHead 2-tab
                // 如果滑动到tab为第一个item时,则显示顶部隐藏的shadow_tab,作为悬浮菜单栏
               /* shadow_status_detail_tab.setVisibility(firstVisibleItem >= 2 ?
                        View.VISIBLE : View.GONE);*/
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_humor_details_avatar:
                ToFriendInfoUtil.getInstance().toFriendInfo(
                        AtyHumorDetail.this, humor.getBlogUser().getId());
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
                viewTags.switchTags();
                break;
            case R.id.tv_humor_details_praise:
                new PostTask(UrlHelper.HUMOR_PRAISE,
                        new UpdateViewHelper.UpdateViewListener() {
                            @Override
                            public void onComplete(Object obj) {
                                tvPraise.setSelected(!tvPraise.isSelected());
                                String s = tvPraise.getText().toString();
                                int praise = getCurPraiseNum(s);
                                if (!isPraise) {
                                	ivPraiseAnim.setVisibility(View.VISIBLE);
                        			ivPraiseAnim.startAnimation(animation);
                        			new Handler().postDelayed(new Runnable(){
                        	            public void run() {
                        	            	ivPraiseAnim.setVisibility(View.GONE);
                        	            } 
                        			}, 1000);
                                    tvPraise.setText(String.format(praiseNum,
                                            praise + 1));
                                    T.showShort(AtyHumorDetail.this, "点赞完成");
                                } else {
                                    tvPraise.setText(String.format(praiseNum,
                                            praise - 1));
                                    T.showShort(AtyHumorDetail.this, "取消点赞");
                                }
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
            default:break;
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

    private void postComment(String comment) {
        KeyBoardUtils.hideSoftKeyboard(AtyHumorDetail.this);
        final Dialog dialog = MyProgress.getInstance(AtyHumorDetail.this,
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
                        loadComments(1);
                    }
                }, "Id", humorId, "Content", comment);
    }

    private void loadComments(int pageNum){
        if(pageNum == 1){
            pageIndex = 1;
            comments.clear();
        }
        new GetTask(UrlHelper.HUMOR_COMMENT,
                new UpdateViewHelper.UpdateViewListener(CommentInfoList.class) {
                    @Override
                    public void onComplete(Object obj) {
                        commentList = (CommentInfoList) obj;
                        if (commentList.getItems() != null
                                && commentList.getItems().size()>0) {
                            comments.addAll(commentList.getItems());
                            if (comments.size() < commentList.getCount()) {
                                addFootView(plv, footView);
                            } else {
                                removeFootView(plv, footView);
                            }
                            tvComment.setText(String.format(commentNum,
                                    comments.size()));
                            commentAdapter.notifyDataSetChanged();
                            plv.onRefreshComplete();
                        }
                    }
                }, "Id", humorId, "Pageindex", String.valueOf(pageIndex),
                "Pagesize", String.valueOf(pageSize));

    }

    private void addFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if (lv.getFooterViewsCount() == 1) {
            lv.addFooterView(footView);
        }
    }

    private void removeFootView(PullToRefreshListView plv, View footView) {
        ListView lv = plv.getRefreshableView();
        if (lv.getFooterViewsCount() > 1) {
            lv.removeFooterView(footView);
        }
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

    private void palyNetVoice() {
        player = new MediaPlayer();
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
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

    public void stopPlayVoice() {
        if (null != player) {
            player.stop();
            player.release();
            player = null;
        }
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
                .format(new Date());
    }
}
