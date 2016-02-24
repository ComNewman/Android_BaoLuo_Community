package com.baoluo.community.ui;

import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.text.SpannableStringBuilder;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.baoluo.community.ui.customview.SmInputView;
import com.baoluo.community.ui.listener.MyClickListener;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.KeyBoardUtils;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.baoluo.im.util.DepthPageTransformer;
import com.baoluo.im.util.InputBoxUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;

/**
 * 话题详情
 * Created by tao.lai on 2015/11/10 0010.
 */
public class AtyTopicDetail extends AtyBase implements View.OnClickListener,SmInputView.SmInputListener,HeadView.HeadViewListener {
	
    private static final String TAG = "AtyTopicDetail";

    public static final String ID = "RepId";
    public static final String TITLE = "RepTitle";

    private int pageindex = 1;
    private int pagesize = 10;
    private int count;

    private String id;
    private TopicInfo topic;
    private boolean isPraise, isSob;
    private TopicDiscussAdapter commentAdapter;
    private List<TopicComment> topicComments = new ArrayList<TopicComment>();
    private TopicCommentList topicInfoList;

    private PullToRefreshListView plv;
    private View topicView,footView;
    private SmInputView smInputView;
    private HeadView headView;
    private ImageView ivAvatar;
    private ViewPager vpTopicPics;
    private Button btnCiteReply;
    private ImageButton btnPraise,btnSob;
    private TextView tvUserName,tvTopicTitle,tvTopicContent,tvPicPage,tvPraiseNum,tvSob;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_sns_detail);
        id = getIntent().getStringExtra("id");
        init();
    }

    private void init(){
        headView = (HeadView) findViewById(R.id.hv_head);
        plv = (PullToRefreshListView) findViewById(R.id.lv_detail);
        smInputView = (SmInputView) findViewById(R.id.siv_details);

        topicView = View.inflate(this, R.layout.view_topic_detail, null);
        vpTopicPics = (ViewPager) topicView.findViewById(R.id.vp_topic_details);
        ivAvatar = (ImageView) topicView.findViewById(R.id.iv_topic_details_avatar);
        tvUserName = (TextView) topicView.findViewById(R.id.tv_topic_details_nick);
        tvPicPage = (TextView) topicView.findViewById(R.id.tv_topic_details_pic_page);
        tvTopicTitle = (TextView) topicView.findViewById(R.id.tv_topic_details_title);
        tvTopicContent = (TextView) topicView.findViewById(R.id.tv_topic_details_content);
        tvPraiseNum = (TextView) topicView.findViewById(R.id.tv_topic_details_praisenum);
        tvSob = (TextView) topicView.findViewById(R.id.tv_topic_details_opposenum);
        btnCiteReply = (Button) topicView.findViewById(R.id.tv_topic_details_hight_relpay);
        btnPraise = (ImageButton) topicView.findViewById(R.id.ib_topic_details_praise);
        btnSob = (ImageButton) topicView.findViewById(R.id.ib_topic_details_oppose);

        footView = View.inflate(this, R.layout.view_foot_loading, null);
        ListView listView = plv.getRefreshableView();
        listView.addHeaderView(topicView);

        initData();
        initListener();
    }

    private void initData(){
        headView.setHeadTitle("话题详情");
        commentAdapter = new TopicDiscussAdapter(topicComments, this,
                myListener);
        plv.setAdapter(commentAdapter);

        new GetTask(UrlHelper.TOPIC_ONE,
                new UpdateViewHelper.UpdateViewListener(TopicInfo.class) {
                    @Override
                    public void onComplete(Object obj) {
                        topic = (TopicInfo)obj;
                        if(StrUtils.isEmpty(topic.getOwer().getFace())){
                            ivAvatar.setImageResource(R.drawable.head_default);
                        }else{
                            GlobalContext.options.shouldShowImageOnFail();
                            GlobalContext.getInstance().imageLoader
                                    .displayImage(topic.getOwer().getFace(),
                                            ivAvatar, GlobalContext.options,
                                            null);
                        }
                        isSob = topic.isIsOpposes();
                        isPraise = topic.isIsPraise();
                        if (isSob) {
                            btnSob.setSelected(true);
                        }
                        if (isPraise) {
                            btnPraise.setSelected(true);
                        }
                        tvUserName.setText(topic.getOwer().getName());
                        String title = getResources().getString(
                                R.string.topic_title);
                        tvTopicTitle.setText(String.format(title,
                                topic.getTitle()));
                        SpannableStringBuilder ssb = InputBoxUtil.getInstance()
                                .changeTextColor(AtyTopicDetail.this,topic.getContent());
                        tvTopicContent.setText(ssb);
                        tvSob.setText(String.valueOf(topic
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
                        loadComments(1);
                    }
                }, "id", id);
    }

    private void initListener(){
        headView.setHeadViewListener(this);
        headView.getBackground().setAlpha(10);
        ivAvatar.setOnClickListener(this);
        vpTopicPics.setPageTransformer(true, new DepthPageTransformer());
        btnCiteReply.setOnClickListener(this);
        btnPraise.setOnClickListener(this);
        btnSob.setOnClickListener(this);
        smInputView.setSmInputListener(this);

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
                       loadComments(++pageindex);
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

    private MyClickListener myListener = new MyClickListener() {
        @Override
        public void myOnClick(int position, View v) {
            TopicComment topic = topicComments.get(position);
            switch (v.getId()) {
                case R.id.iv_topic_nomal_comment_head:
                case R.id.iv_topic_hight_comment_head:
                    String userId = topic.getBlogUser().getId();
                    T.showShort(AtyTopicDetail.this, topic.getBlogUser().getName());
                    ToFriendInfoUtil.getInstance().toFriendInfo(AtyTopicDetail.this, userId);
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_topic_details_avatar:
                ToFriendInfoUtil.getInstance().toFriendInfo(AtyTopicDetail.this, topic.getOwer().getId());
                break;
            case R.id.tv_topic_details_hight_relpay: //高比格回复
                if (topic != null) {
                    Intent i = new Intent();
                    i.setClass(AtyTopicDetail.this,
                            PublishTopicActivity.class);
                    i.putExtra(ID, id);
                    i.putExtra(TITLE, topic.getTitle());
                    startActivity(i);
                } else {
                    T.showShort(this, "网络或API出现异常！");
                }
                break;
            case R.id.ib_topic_details_praise: //赞
                doPraise();
                break;
            case R.id.ib_topic_details_oppose: //
                doSob();
                break;
            default:break;
        }
    }

    private void doPraise(){
        new PostTask(UrlHelper.TOPIC_PRAISE,
                new UpdateViewHelper.UpdateViewListener() {
                    @Override
                    public void onComplete(Object obj) {
                        if (!isPraise) {
                            btnPraise.setSelected(true);
                            btnSob.setSelected(false);
                            tvPraiseNum.setSelected(true);
                            tvSob.setSelected(false);
                            isPraise = true;
                            isSob = false;
                            if (topic.isIsOpposes()) {
                                tvSob.setText(String.valueOf(topic
                                        .getOpposesNum() - 1));
                            } else {
                                tvSob.setText(String.valueOf(topic
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
                },"Id", id);

    }

    private void doSob(){
        new PostTask(UrlHelper.TOPIC_LOW,
                new UpdateViewHelper.UpdateViewListener() {
                    @Override
                    public void onComplete(Object obj) {
                        if (!isSob) {
                            btnSob.setSelected(true);
                            btnPraise.setSelected(false);
                            tvPraiseNum.setSelected(false);
                            tvSob.setSelected(true);
                            isPraise = false;
                            isSob = true;
                            if (topic.isIsPraise()) {
                                tvPraiseNum.setText(String.valueOf(topic
                                        .getPraiseNum() - 1));
                            } else {
                                tvPraiseNum.setText(String.valueOf(topic
                                        .getPraiseNum()));
                            }
                            if (topic.isIsOpposes()) {
                                tvSob.setText(String.valueOf(topic
                                        .getOpposesNum()));
                            } else {
                                tvSob.setText(String.valueOf(topic
                                        .getOpposesNum() + 1));
                            }
                        } else {
                            btnSob.setSelected(false);
                            tvSob.setSelected(false);
                            isSob = false;
                            if (topic.isIsOpposes()) {
                                tvSob.setText(String.valueOf(topic
                                        .getOpposesNum() - 1));
                            } else {
                                tvSob.setText(String.valueOf(topic
                                        .getOpposesNum()));
                            }
                        }
                    }
                },"Id", id);
    }

    private void postNomalComment(String comment) {
        KeyBoardUtils.hideSoftKeyboard(AtyTopicDetail.this);
        TopicInfo topicInfo = new TopicInfo();
        topicInfo.setTopicType(2);
        topicInfo.setRepId(topic.getId());
        topicInfo.setContent(comment);
        String paramsObj = GsonUtil.getInstance().obj2Str(topicInfo);
        new PostObjTask(UrlHelper.TOPIC_HIGHT_REPLY, paramsObj,
                new UpdateViewHelper.UpdateViewListener() {
                    @Override
                    public void onComplete(Object obj) {
                        T.showShort(AtyTopicDetail.this, "评论完成");
                        loadComments(1);
                    }
                });
    }

    private void loadComments(final int pageNum) {
        if (pageNum == 1) {
            pageindex = 1;
            topicComments.clear();
        }
        new GetTask(UrlHelper.TOPIC_COMMENT_NEW, new UpdateViewHelper.UpdateViewListener(TopicCommentList.class) {
            @Override
            public void onComplete(Object obj) {
                topicInfoList = (TopicCommentList) obj;
                count = topicInfoList.getCount();
                if (topicInfoList.getItems().size() > 0) {
                    topicComments.addAll(topicInfoList.getItems());
                }
                commentAdapter.notifyDataSetChanged();

                plv.onRefreshComplete();
                if (topicComments.size() < count) {
                    addFootView(plv, footView);
                } else {
                    removeFootView(plv, footView);
                }
            }
        }, "Id", topic.getId(),
                "PageIndex", String.valueOf(pageNum),
                "PageSize", String.valueOf(pagesize));
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
    public void doSend(String msg) {
        postNomalComment(msg);
    }

    @Override
    public void leftListener() {
        finish();
    }

    @Override
    public void rightListener() {

    }

    public class PageSelChangeListener implements ViewPager.OnPageChangeListener {
    	
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
}
