package com.baoluo.community.ui.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.entity.EventInfo;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.PostTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.ui.R;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.T;
import com.baoluo.dao.helper.GroupHelper;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.im.Configs;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.jsonParse.ResultParse;
import com.baoluo.im.task.GenerateGroupAvatarTask;
import com.baoluo.im.ui.AtyMultiChatMqtt;

import de.greenrobot.event.EventBus;

/**
 * Created by tao.lai on 2015/11/13 0013.
 */
public class ActivityDetailAdapter extends BaseAdapter {

    private Context ctx;
    private EventInfo info;
    private boolean followFlag, attenFlag;
    private String peopleNumFormat;
    private GroupInfo group;


    public ActivityDetailAdapter(Context ctx, EventInfo info) {
        this.ctx = ctx;
        this.info = info;
        peopleNumFormat = ctx.getResources().getString(
                R.string.event_detail_people_num);
        initEventGroup();
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    private TextView tvTitle, tvContent;
    private Button btnAttention, btnFollow, btnPeopleNum, btnTime, btnLocaltion;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View detailView = View.inflate(ctx, R.layout.view_activity_detail, null);
        tvTitle = (TextView) detailView.findViewById(R.id.tv_event_details_title);
        btnAttention = (Button) detailView.findViewById(R.id.btn_event_details_attention);
        btnFollow = (Button) detailView.findViewById(R.id.btn_event_details_enroll);
        btnPeopleNum = (Button) detailView.findViewById(R.id.btn_event_details_peoplenum);
        btnTime = (Button) detailView.findViewById(R.id.btn_event_details_time);
        btnLocaltion = (Button) detailView.findViewById(R.id.btn_event_details_location);
        tvContent = (TextView) detailView.findViewById(R.id.tv_event_details_content);

        attenFlag = info.isIsAttend();
        followFlag = info.isIsFollow();
        tvTitle.setText(info.getName());
        tvContent.setText(info.getContent());
        if (attenFlag) {
            btnAttention.setText("进群");
            btnAttention.setTextColor(Color.parseColor("#333333"));
        }
        if (followFlag) {
            btnFollow.setText("取消关注");
            btnAttention.setTextColor(Color.parseColor("#999999"));
        }
        String time = ctx.getResources().getString(R.string.event_detail_time);
        String location = ctx.getResources().getString(
                R.string.event_detail_location);
        btnTime.setText(String.format(time, info.getStartDate(),
                info.getEntDate()));
        btnPeopleNum.setText(String.format(peopleNumFormat, info.getUpNum(),
                info.getAttendNum()));
        if (info.getLocation() != null
                && !StrUtils.isEmpty(info.getLocation().getName())) {
            btnLocaltion.setText(String.format(location, info
                    .getLocation().getName()));
        } else {
            btnLocaltion.setText("");
        }

        btnAttention.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventAttend();
            }
        });

        btnFollow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eventFollow();
            }
        });
        return detailView;
    }

    private void eventAttend() {
        if (attenFlag) {
            Intent intent = new Intent();
            intent.setClass(ctx, AtyMultiChatMqtt.class);
            intent.putExtra(AtyMultiChatMqtt.EXTRA_GROUP, group);
            ctx.startActivity(intent);
        } else {
            new PostTask(UrlHelper.EVENT_ATTEND,
                    new UpdateViewHelper.UpdateViewListener() {
                        @Override
                        public void onCompleteExecute(String responseStr) {
                            int code = ResultParse.getInstance().getResCode(
                                    responseStr);
                            if (code != com.baoluo.community.core.Configs.RESPONSE_OK) {
                                T.showShort(ctx, ResultParse.getInstance().getResStr(
                                        responseStr));
                                return;
                            }
                            attenFlag = true;
                            btnAttention.setText("进群");
                            btnAttention.setTextColor(Color.parseColor("#333333"));
                            String peopelNum = btnPeopleNum.getText()
                                    .toString();
                            String[] str = peopelNum.split("/");
                            String num = str[str.length - 1];
                            btnPeopleNum.setText(String.format(peopleNumFormat,
                                    info.getUpNum(), num + 1));
                            updateGroupInfo();
                        }
                    }, "Id", info.getId());
        }

    }

    private void eventFollow() {
        new PostTask(UrlHelper.EVENT_FOLLOW,
                new UpdateViewHelper.UpdateViewListener() {
                    @Override
                    public void onComplete(Object obj) {
                        if (!followFlag) {
                            btnFollow.setText("取消关注");
                            btnFollow.setTextColor(Color.parseColor("#999999"));
                            T.showShort(ctx, "关注完成");
                        } else {
                            btnFollow.setText("+ 关注");
                            btnFollow.setTextColor(Color.parseColor("#ff7f7f"));
                            T.showShort(ctx, "取消关注");
                        }
                        followFlag = !followFlag;
                    }
                }, "Id", info.getId());
    }

    private void initEventGroup() {
        new GetTask(UrlHelper.GET_GROUP_INFO,
                new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
                    @Override
                    public void onComplete(Object obj) {
                        group = (GroupInfo) obj;
                    }
                }, "id", info.getId());
    }

    private void updateGroupInfo() {
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
                }, "id", info.getId());
    }
}
