package com.baoluo.community.ui.fragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.MinePhoto;
import com.baoluo.community.entity.MineTrack;
import com.baoluo.community.entity.MineTrackList;
import com.baoluo.community.entity.UserSelf;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.R;
import com.baoluo.community.ui.adapter.MinePhotoAdapter;
import com.baoluo.community.ui.adapter.MineTrackAdapter;
import com.baoluo.community.ui.customview.HeadView;
import com.baoluo.community.ui.customview.HeadView.HeadViewListener;
import com.baoluo.community.ui.customview.RoundImageView;
import com.baoluo.community.ui.xscrollview.XScrollView;
import com.baoluo.community.ui.xscrollview.XScrollView.IXScrollViewListener;
import com.baoluo.community.util.FastBlur;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.im.ui.AtyAboutBaoluo;
import com.baoluo.im.ui.AtyMsgSetting;
import com.baoluo.im.ui.AtyPerson;
import com.baoluo.im.ui.AtySecurity;
import com.baoluo.im.ui.AtySetting;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

/**
 * 个人
 *
 * @author Ryan_Fu 2015-5-12
 */
@SuppressLint("NewApi")
public class MineFragment extends Fragment implements OnClickListener,
        OnPageChangeListener, HeadViewListener, IXScrollViewListener {

    private static final String TAG = "MineFragment";
    private View view;
    private HeadView headView;
    private RoundImageView rivHead;
    private XScrollView mXScrollView;
    private TextView tvNick, tvAccount;
    private RelativeLayout rlTop;
    private ImageView ivBlur, ivSex;
    private Button btnPhoto, btnTrack;
    private DisplayImageOptions options;
    private UserSelf self;
    private ListView lvPhoto;
    private List<MinePhoto> minePhotoData;
    private MinePhotoAdapter photoAdapter;
    private MineTrackList trackList;
    private List<MineTrack> mineTrackData;
    private MineTrackAdapter trackAdapter;

    private int PageIndex = 1;
    private int PageSize = 10;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);
        initView();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
        L.i(TAG, "onResume  initData");
    }

    private void initView() {
        headView = (HeadView) view.findViewById(R.id.hv_mine_head);
        headView.setHeadViewListener(this);
        headView.setOnClickListener(this);
        mXScrollView = (XScrollView) view.findViewById(R.id.xsv_mine);
        mXScrollView.setPullRefreshEnable(false);
        mXScrollView.setPullLoadEnable(false);
        mXScrollView.setIXScrollViewListener(this);
        mXScrollView.setRefreshTime(getTime());
        View content = LayoutInflater.from(getActivity()).inflate(
                R.layout.mine_scroll_view, null);

        if (null != content) {
            lvPhoto = (ListView) content.findViewById(R.id.lv_mine);
            lvPhoto.setFocusable(false);
            lvPhoto.setFocusableInTouchMode(false);
            lvPhoto.setDivider(null);
            rivHead = (RoundImageView) content.findViewById(R.id.riv_mine_head);
            rivHead.setOnClickListener(this);
            tvAccount = (TextView) content.findViewById(R.id.tv_mine_account);
            tvNick = (TextView) content.findViewById(R.id.tv_mine_nick);
            ivSex = (ImageView) content.findViewById(R.id.iv_mine_sex);
            ivBlur = (ImageView) content.findViewById(R.id.iv_mine_top_bac);
            rlTop = (RelativeLayout) content.findViewById(R.id.rl_mine_top);
            // rlTop.setOnClickListener(this);
            btnPhoto = (Button) content.findViewById(R.id.btn_mine_task);
            btnTrack = (Button) content.findViewById(R.id.btn_mine_setting);

        }
        mXScrollView.setView(content);
        btnPhoto.setOnClickListener(this);
        btnTrack.setOnClickListener(this);

    }

    private void getUserInfo() {
        new GetTask(UrlHelper.CURRENT_USER_INFO,
                new UpdateViewHelper.UpdateViewListener(UserSelf.class) {
                    @Override
                    public void onComplete(Object obj) {
                        self = (UserSelf)obj;
                        if (!StrUtils.isEmpty(self.getFace())) {
                            GlobalContext.getInstance().imageLoader
                                    .displayImage(self.getFace(), ivBlur,
                                            options,
                                            new ImageLoadingListener() {
                                                @Override
                                                public void onLoadingStarted(
                                                        String arg0, View arg1) {

                                                }

                                                @Override
                                                public void onLoadingFailed(
                                                        String arg0, View arg1,
                                                        FailReason arg2) {

                                                }

                                                @Override
                                                public void onLoadingComplete(
                                                        String arg0, View arg1,
                                                        Bitmap arg2) {
                                                    applyBlur();
                                                }

                                                @Override
                                                public void onLoadingCancelled(
                                                        String arg0, View arg1) {

                                                }
                                            });
                            GlobalContext.getInstance().imageLoader
                                    .displayImage(self.getFace(), rivHead,
                                            options, null);
                        } else {
                            ivBlur.setImageResource(R.drawable.head_default);
                            rivHead.setImageResource(R.drawable.head_default);
                            applyBlur();
                        }
                        if (self != null && self.getSex() == 1) {
                            ivSex.setImageResource(R.drawable.ic_nan);
                        }
                        tvNick.setText(self.getNickName());
                        String stringFormat = "账号 : %s";
                        tvAccount.setText(String.format(stringFormat,
                                self.getUserName()));
                    }
                });

        new GetTask(UrlHelper.MINE_PHOTO_DATA,
                new UpdateViewHelper.UpdateViewListener(new TypeToken<List<MinePhoto>>() {
                }.getType()) {
                    @SuppressWarnings("unchecked")
                    @Override
                    public void onComplete(Object obj) {
                        List<MinePhoto> photoData = (List<MinePhoto>)obj;
                        if (photoData != null && photoData.size() > 0) {
                            minePhotoData.addAll(photoData);
                            photoAdapter.notifyDataSetChanged();
                        }
                    }
                });
        getTrackData(true);
    }

    private void onLoad() {
        mXScrollView.stopRefresh();
        mXScrollView.stopLoadMore();
        mXScrollView.setRefreshTime(getTime());
    }

    private void getTrackData(final boolean isRefresh) {
        if (isRefresh) {
            PageIndex = 1;
        } else {
            PageIndex++;
        }
        new GetTask(UrlHelper.MINE_TRACK_DATA, new UpdateViewHelper.UpdateViewListener(MineTrackList.class) {
            @Override
            public void onComplete(Object obj) {
                trackList = (MineTrackList)obj;
                if (trackList != null && trackList.getItems() != null && trackList.getItems().size() > 0) {
                    if (isRefresh) {
                        mineTrackData.clear();
                    }
                    mineTrackData.addAll(trackList.getItems());
                    trackAdapter.notifyDataSetChanged();
                }
                onLoad();
            }
        }, "PageIndex", String.valueOf(PageIndex), "PageSize", String.valueOf(PageSize));
    }

    private void initData() {
        minePhotoData = new ArrayList<MinePhoto>();
        photoAdapter = new MinePhotoAdapter(getActivity(), minePhotoData, R.layout.item_mine_photo);
        lvPhoto.setAdapter(photoAdapter);
        mineTrackData = new ArrayList<MineTrack>();
        trackAdapter = new MineTrackAdapter(mineTrackData, getActivity());
        btnPhoto.setSelected(true);
        headView.getBackground().setAlpha(76);
        options = new DisplayImageOptions.Builder()//
                // .showImageOnLoading(R.drawable.bg_mine_blur_test) //
                // 加载中显示的默认图片
                .showImageOnFail(R.drawable.bg_mine_blur_test) // 设置加载失败的默认图片
                .cacheInMemory(true) // 内存缓存
                .cacheOnDisk(true) // sdcard缓存
                .bitmapConfig(Config.RGB_565)// 设置最低配置
                .build();
        getUserInfo();
    }

    private String getTime() {
        return new SimpleDateFormat("MM-dd HH:mm", Locale.CHINA)
                .format(new Date());
    }

    private void applyBlur() {
        ivBlur.getViewTreeObserver().addOnPreDrawListener(
                new ViewTreeObserver.OnPreDrawListener() {
                    @Override
                    public boolean onPreDraw() {
                        ivBlur.getViewTreeObserver().removeOnPreDrawListener(
                                this);
                        ivBlur.buildDrawingCache();
                        Bitmap bmp = ivBlur.getDrawingCache();
                        blur(bmp, rlTop);
                        ivBlur.setVisibility(View.GONE);
                        return true;
                    }
                });
    }

    @SuppressLint("NewApi")
    private void blur(Bitmap bkg, View view) {
        float radius = 8;
        float scaleFactor = 8;
        Bitmap overlay = Bitmap.createBitmap(
                (int) (view.getMeasuredWidth() / scaleFactor),
                (int) (view.getMeasuredHeight() / scaleFactor),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(overlay);
        canvas.translate(-view.getLeft() / scaleFactor, -view.getTop()
                / scaleFactor);
        canvas.scale(1 / scaleFactor, 1 / scaleFactor);
        Paint paint = new Paint();
        paint.setFlags(Paint.FILTER_BITMAP_FLAG);
        canvas.drawBitmap(bkg, 0, 0, paint);
        overlay = FastBlur.doBlur(overlay, (int) radius, true);
        view.setBackground(new BitmapDrawable(getResources(), overlay));
    }

    @Override
    public void onClick(View v) {
        Intent i = new Intent();
        switch (v.getId()) {
            case R.id.btn_mine_task:
                mXScrollView.setPullRefreshEnable(false);
                mXScrollView.setPullLoadEnable(false);
                btnPhoto.setSelected(true);
                btnTrack.setSelected(false);
                lvPhoto.setAdapter(photoAdapter);
                break;
            case R.id.btn_mine_setting:
                mXScrollView.setPullRefreshEnable(true);
                mXScrollView.setPullLoadEnable(true);
                btnTrack.setSelected(true);
                btnPhoto.setSelected(false);
                lvPhoto.setAdapter(trackAdapter);
                break;
            case R.id.rl_mine_setting_setting:
                i.setClass(getActivity(), AtyMsgSetting.class);
                startActivity(i);
                break;
            case R.id.rl_mine_setting_safety:
                startActivity(new Intent(getActivity(), AtySecurity.class));
                break;
            case R.id.rl_mine_setting_aboout:
                i.setClass(getActivity(), AtyAboutBaoluo.class);
                startActivity(i);
                break;
            case R.id.hv_mine_head:
                mXScrollView.scrollTo(0, 0);
                break;
            case R.id.riv_mine_head:
                Bundle bundle = new Bundle();
                bundle.putSerializable("self", self);
                i.setClass(getActivity(), AtyPerson.class);
                i.putExtras(bundle);
                startActivity(i);
                break;
            default:
                break;
        }
    }

    @Override
    public void onPageScrollStateChanged(int position) {

    }

    @Override
    public void onPageScrolled(int position, float arg1, int arg2) {

    }

    @Override
    public void onPageSelected(int position) {
        switch (position) {
            case 0:
                btnPhoto.setSelected(true);
                btnTrack.setSelected(false);
                break;
            case 1:
                btnTrack.setSelected(true);
                btnPhoto.setSelected(false);
                break;
            default:
                break;
        }
    }

    @Override
    public void leftListener() {

    }

    @Override
    public void rightListener() {
        startActivity(new Intent(getActivity(), AtySetting.class));
    }

    @Override
    public void onRefresh() {
        if (btnTrack.isSelected()) {
            getTrackData(true);
        }
    }

    @Override
    public void onLoadMore() {
        if (btnTrack.isSelected()) {
            getTrackData(false);
        }

    }

}
