package com.baoluo.community.ui;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.entity.HumorInfo;
import com.baoluo.community.entity.HumorInfoList;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.adapter.ActivityDetailAdapter;
import com.baoluo.community.ui.adapter.EventCommentAdapter;
import com.baoluo.community.ui.adapter.SendPagerAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.MyProgress;
import com.baoluo.community.ui.customview.Pull2RefreshListView;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.customview.SmInputView;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.ToFriendInfoUtil;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by tao.lai on 2015/11/12 0012.
 */
public class AtyActivityDetail extends AtyBase implements HeadView.HeadViewListener,SmInputView.SmInputListener,View.OnClickListener{
    
	private  final String TAG = getClass().getName();

    private HeadView headView;
    private SmInputView smInputView;
    private Pull2RefreshListView plv;
    private ViewPager vpImgs;
    private RoundImageView ivAvatar;
    private View atyImgView,tabView,footView;
    private TextView tvUseName,tvImgIndex;
    private Button btnDetail,btnComment;

    private EventInfo eventInfo;
    private boolean showDetail = true;
    private List<HumorInfo> comments = new ArrayList<HumorInfo>();
    private EventCommentAdapter commentAdpter;
    private ActivityDetailAdapter detailAdapter;
    private HumorInfoList humorInfoList;
    private DisplayImageOptions options;
    private String commentFormat;
    private int pageSize = 10;
    private int pageIndex = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.aty_activity_detail);
        eventInfo = (EventInfo) this.getIntent().getSerializableExtra("eventInfo");
        init();
    }

    private void init(){
        headView = (HeadView) findViewById(R.id.hv_head);
        plv = (Pull2RefreshListView) findViewById(R.id.lv_detail);
        smInputView = (SmInputView) findViewById(R.id.siv_details);

        atyImgView = View.inflate(this,R.layout.view_acitity_img,null);
        vpImgs = (ViewPager) atyImgView.findViewById(R.id.vp_imgs);
        ivAvatar = (RoundImageView) atyImgView.findViewById(R.id.iv_event_details_avatar);
        tvUseName = (TextView) atyImgView.findViewById(R.id.tv_event_details_nick);
        tvImgIndex = (TextView) atyImgView.findViewById(R.id.tv_event_details_page);

        tabView = View.inflate(this,R.layout.view_activity_tab,null);
        tabView.findViewById(R.id.view_bord_bottom).setVisibility(View.GONE);
        btnDetail = (Button) tabView.findViewById(R.id.btn_event_detail);
        btnComment = (Button) tabView.findViewById(R.id.btn_event_comment);

        footView = View.inflate(this, R.layout.view_foot_loading, null);

        ListView listView = plv.getRefreshableView();
        listView.addHeaderView(atyImgView);
        listView.addHeaderView(tabView);

        initData();
        initListener();
    }

    private void initData(){
        commentAdpter = new EventCommentAdapter(AtyActivityDetail.this,
                comments, R.layout.event_details_comments_item);
        new GetTask(UrlHelper.EVENT_ONT,
                new UpdateViewHelper.UpdateViewListener(EventInfo.class) {
                    @Override
                    public void onComplete(Object obj) {
                        eventInfo = (EventInfo) obj;
                        detailAdapter  = new ActivityDetailAdapter(AtyActivityDetail.this,eventInfo);
                        plv.setAdapter(detailAdapter);
                        initActivity();
                        loadComments(1);
                    }
                }, "id", eventInfo.getId());
    }

    private void initListener(){
        ivAvatar.setOnClickListener(this);
        btnDetail.setOnClickListener(this);
        btnComment.setOnClickListener(this);

        headView.setHeadViewListener(this);
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
                        loadComments(++pageIndex);
                    }
                });

        plv.setOnScrollListener(new AbsListView.OnScrollListener() {  //滚动状态监听
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                // 如果滑动到tab为第一个item时,则显示顶部隐藏的shadow_tab,作为悬浮菜单栏
                /*shadowTabView.setVisibility(firstVisibleItem >= 3 ?
                        View.VISIBLE : View.GONE);*/
            }
        });
    }

    private void initActivity() {
        if (eventInfo != null) {
            tvUseName.setText(eventInfo.getOwner().getName());
            commentFormat = getResources().getString(
                    R.string.event_detail_comment);
            btnComment.setText(String.format(commentFormat,
                    eventInfo.getCommmitNum()));
            options = new DisplayImageOptions.Builder()
                    .showImageOnLoading(R.drawable.ic_launcher)
                    .showImageOnFail(R.drawable.head_test).cacheInMemory(true)
                    .cacheOnDisk(true).build();
            options.shouldShowImageOnFail();
            if (!StrUtils.isEmpty(eventInfo.getOwner().getFace())) {
                GlobalContext.getInstance().imageLoader.displayImage(eventInfo
                        .getOwner().getFace(), ivAvatar, options, null);
            } else {
                ivAvatar.setImageDrawable(getResources().getDrawable(
                        R.drawable.head_default));
            }

            if (eventInfo.getPictures() != null
                    && eventInfo.getPictures().size() > 0) {
                tvImgIndex.setText("1/" + eventInfo.getPictures().size());
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
                vpImgs.setAdapter(new SendPagerAdapter(listViews));
                vpImgs.setCurrentItem(0);
                vpImgs.setOnPageChangeListener(new PageSelChangeListener());
            }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_event_detail:
                switchDetailAndComment(true);
                break;
            case R.id.btn_event_comment:
                switchDetailAndComment(false);
                break;
            case R.id.iv_event_details_avatar:
                ToFriendInfoUtil.getInstance().toFriendInfo(
                        AtyActivityDetail.this, eventInfo.getOwner().getId());
                break;
            default:break;
        }
    }

    private void switchDetailAndComment(boolean showDetailed){
        if(showDetailed) {
            btnDetail.setSelected(true);
            btnComment.setSelected(false);
            smInputView.setVisibility(View.GONE);
            removeFootView(plv, footView);
            showDetail = true;
            plv.setAdapter(detailAdapter);
        }else {
            btnDetail.setSelected(false);
            btnComment.setSelected(true);
            smInputView.setVisibility(View.VISIBLE);
            showDetail = false;
            plv.setAdapter(commentAdpter);
        }
    }



    private void loadComments(int pageNum) {
        if(pageNum == 1){
            pageIndex = 1;
            comments.clear();
        }
        new GetTask(UrlHelper.EVENT_COMMENT_LIST,
                new UpdateViewHelper.UpdateViewListener(HumorInfoList.class) {
                    @Override
                    public void onFail() {
                        plv.onRefreshComplete();
                    }
                    @Override
                    public void onComplete(Object obj) {
                        humorInfoList = (HumorInfoList) obj;
                        if (humorInfoList.getItems() != null
                                && humorInfoList.getItems().size() > 0) {
                                comments.addAll(humorInfoList.getItems());
                            if (!showDetail && comments.size() < humorInfoList.getCount()) {
                                addFootView(plv, footView);
                            } else {
                                removeFootView(plv, footView);
                            }
                            commentAdpter.notifyDataSetChanged();
                            plv.onRefreshComplete();
                        }
                    }
                }, "Id", eventInfo.getId(), "Pageindex", pageIndex + "",
                "Pagesize", pageSize + "");
    }

    @Override
    public void doSend(String msg) {
        final Dialog dialog = MyProgress.getInstance(
                AtyActivityDetail.this, "评论...");
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
                        btnComment.setText(String.format(commentFormat,
                                Integer.parseInt(num) + 1));
                        loadComments(1);
                    }
                }, "Id", eventInfo.getId(), "Content", msg);
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

    public class PageSelChangeListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageSelected(int index) {
            tvImgIndex.setText((index + 1) + "/" + eventInfo.getPictures().size());
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    }
}
