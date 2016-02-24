package com.baoluo.dao.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.util.L;
import com.baoluo.dao.DaoSession;
import com.baoluo.dao.GroupDb;
import com.baoluo.dao.GroupDbDao;
import com.baoluo.dao.GroupDbDao.Properties;
import com.baoluo.event.NotifyGroupUpdate;
import com.baoluo.im.Configs;
import com.baoluo.im.MqttHelper;
import com.baoluo.im.entity.GroupInfo;
import com.baoluo.im.task.GenerateGroupAvatarTask;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.event.EventBus;

/**
 * 群组
 * 
 * @author tao.lai
 * 
 */
public class GroupHelper {
	private static final String TAG = "GroupHelper";

	private static GroupHelper instance;
	private static Context ctx;
	private DaoSession mSession;
	private GroupDbDao groupDbDao;

	private GroupHelper() {
	}

	public static GroupHelper getInstance() {
		if (instance == null) {
			instance = new GroupHelper();
			if (ctx == null) {
				ctx = GlobalContext.getInstance();
			}
			instance.mSession = GlobalContext.getDaoSession(ctx);
			instance.groupDbDao = instance.mSession.getGroupDbDao();
		}
		return instance;
	}

	public void insertGroup(GroupInfo gi) {
		GroupDb gd = getGroupDbByGid(gi.getId());
		GroupDb entity = objSwitch(gi);
		if (gd.getId() != null) {
			entity.setId(gd.getId());
		}
		groupDbDao.insertOrReplace(objSwitch(gi));
	}
	
	public void insertGroup(GroupDb gd){
		groupDbDao.insertOrReplace(gd);
	}
	
	public void insertGroups(List<GroupInfo> list) {
		for (GroupInfo gi : list) {
			insertGroup(gi);
		}
	}
	
	public List<GroupDb> getAllGroupDb(){
		QueryBuilder<GroupDb> qb = groupDbDao.queryBuilder();
		qb.where(Properties.MyId.eq(GlobalContext.getInstance().getUid()));
		return qb.list();
	}

	public List<GroupInfo> getAllGroups() {
		return list2General(getAllGroupDb());
	}

	public void delGroup(String gid) {
		L.i(TAG, "delGroup: gid="+gid);
		QueryBuilder<GroupDb> qb = groupDbDao.queryBuilder();
	    DeleteQuery<GroupDb> bd = qb.where(
				qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.GroupId.eq(gid))
				).limit(1).buildDelete();
	    bd.executeDeleteWithoutDetachingEntities();
	}

	public GroupDb getGroupDbByGid(String gid) {
		QueryBuilder<GroupDb> qb = groupDbDao.queryBuilder();
		qb.where(qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.GroupId.eq(gid)));
		List<GroupDb> list = qb.limit(1).list();
		if (list != null && list.size() > 0) {
			return list.get(0);
		}
		return new GroupDb();
	}

	public GroupInfo getGroupByGid(String gid) {
		return objSwitch(getGroupDbByGid(gid));
	}

	public List<GroupInfo> list2General(List<GroupDb> list) {
		List<GroupInfo> rs = new ArrayList<GroupInfo>();
		for (GroupDb gd : list) {
			rs.add(objSwitch(gd));
		}
		return rs;
	}

	public GroupDb objSwitch(GroupInfo gi) {
		GroupDb gd = new GroupDb();
		gd.setMyId(GlobalContext.getInstance().getUid());
		gd.setGroupType(gi.getGroupType());
		gd.setGroupId(gi.getId());
		gd.setGroupName(gi.getName());
		gd.setDescribe(gi.getDec());
		gd.setOwner(gi.getOwner());
		gd.setGroupAvatar(gi.getGroupAvatar());
		return gd;
	}

	public GroupInfo objSwitch(GroupDb gd) {
		GroupInfo gi = new GroupInfo();
		gi.setGroupType(gd.getGroupType());
		gi.setId(gd.getGroupId());
		gi.setName(gd.getGroupName());
		gi.setDec(gd.getDescribe());
		gi.setOwner(gd.getOwner());
		gi.setGroupAvatar(gd.getGroupAvatar());
		return gi;
	}
	
	public void addOrUpdateGroupInfo(final String gid) {
		new GetTask(UrlHelper.GET_GROUP_INFO,
				new UpdateViewHelper.UpdateViewListener(GroupInfo.class) {
					@Override
					public void onComplete(Object obj) {
						GroupInfo gi = (GroupInfo) obj;
						MqttHelper
								.getInstance()
								.subscribe(
										gi.getGroupType() == 2 ? (Configs.MQ_EVENT_PRE + gid)
												: (Configs.MQ_GROUP_PRE + gid));
						GroupHelper.getInstance().insertGroup(gi);
						EventBus.getDefault().post(new NotifyGroupUpdate());
						new GenerateGroupAvatarTask(gi);
					}
				}, "id", gid);
	}
	
	public void removeEventGroupByGid(String gid) {
		removeGroupByGid(gid, true);
	}

	public void removeGroupByGid(String gid, boolean isEventGroup) {
		delGroup(gid);
		MqttHelper.getInstance().unSubscribe(
				(isEventGroup ? Configs.MQ_EVENT_PRE : Configs.MQ_GROUP_PRE)
						+ gid);
		EventBus.getDefault().post(new NotifyGroupUpdate());
		MessageHelper.getInstance().removeByFriId(gid);
	}

	public void removeGroupByGid(String gid) {
		removeGroupByGid(gid, false);
	}

	public List<GroupDb> searchByName(String keyWord) {
		QueryBuilder<GroupDb> qb = groupDbDao.queryBuilder();
		qb.where(qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.GroupName.like("%" + keyWord + "%")));
		return qb.list();
	}
	
	public void switchDnded(String gid,boolean dnded){
		GroupDb gd = getGroupDbByGid(gid); 
		if(dnded){
			gd.setDnded(1);
		}else{
			gd.setDnded(0);
		}
		groupDbDao.insertOrReplace(gd);
	}
	
	public boolean getDnded(String gid) {
		boolean rs = false;
		GroupDb gd = getGroupDbByGid(gid);
		if (gd == null || gd.getDnded() == null) {
			return rs;
		}
		if (gd.getDnded() == 1) {
			rs = true;
		}
		return rs;
	}

	public void cleanData(){
		groupDbDao.deleteAll();
	}
}
