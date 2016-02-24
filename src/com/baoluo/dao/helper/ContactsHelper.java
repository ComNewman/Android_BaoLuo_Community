package com.baoluo.dao.helper;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.core.UrlHelper;
import com.baoluo.community.support.task.GetTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.dao.ContactDb;
import com.baoluo.dao.ContactDbDao;
import com.baoluo.dao.ContactDbDao.Properties;
import com.baoluo.dao.DaoSession;
import com.baoluo.event.NotifyContactUpdate;
import com.baoluo.im.entity.ContactsInfo;

import de.greenrobot.dao.query.DeleteQuery;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.event.EventBus;

/**
 * 联系人
 * @author tao.lai
 *
 */
public class ContactsHelper {
	private static final String TAG = "ContactsHelper";
	
	private static ContactsHelper instance;
	private static Context ctx;
	private DaoSession mSession;
	private ContactDbDao contactDbDao;
	
	private ContactsHelper(){
	}
	
	public static ContactsHelper getInstance(){
		if(instance == null){
			instance = new ContactsHelper();
			if(ctx == null){
				ctx = GlobalContext.getInstance();
			}
			instance.mSession = GlobalContext.getDaoSession(ctx);
			instance.contactDbDao = instance.mSession.getContactDbDao();
		}
		return instance;
	}
	
	public void insertContacts(ContactsInfo c) {
		ContactDb cd = getContactById(c.getAccountID());
		ContactDb entity = objSwitch(c);
		if (cd.getId() != null) {
			entity.setId(cd.getId());
		}
		L.i(TAG, "insertContacts:"+GsonUtil.getInstance().obj2Str(entity));
		contactDbDao.insertOrReplace(entity);
	}
	
	public void insertContacts(List<ContactsInfo> list){
		for(ContactsInfo ci : list){
			insertContacts(ci);
		}
	}


	
	public void deleteContact(String uid){
		QueryBuilder<ContactDb> qb = contactDbDao.queryBuilder();
	    DeleteQuery<ContactDb> bd = qb.where(
				qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.UserId.eq(uid))
				).limit(1).buildDelete();
	    bd.executeDeleteWithoutDetachingEntities();
	}
	
	public ContactsInfo getContactsById(String friId){
		return objSwitch(getContactById(friId));
	}
	
	public ContactDb getContactById(String friId){
		L.i(TAG,"start getContact from DB...");
		QueryBuilder<ContactDb> qb = contactDbDao.queryBuilder();
		qb.where(
				qb.and(
						Properties.MyId.eq(GlobalContext.getInstance().getUid()),
						Properties.UserId.eq(friId))
		);
		List<ContactDb> list = qb.limit(1).list();
		if(list != null && list.size() > 0){
			L.i(TAG,"getContact from DB:"+ GsonUtil.getInstance().obj2Str(list.get(0)));
			return (list.get(0));
		}
		L.i(TAG,"getContact from DB: is null");
		return new ContactDb();
	}
	
	public List<ContactsInfo> getAllContacts(){
		QueryBuilder<ContactDb> qb = contactDbDao.queryBuilder();
		qb.where(
				Properties.MyId.eq(GlobalContext.getInstance().getUid())
				);
		return list2General(qb.list());
	}
	
	public List<ContactsInfo> list2General(List<ContactDb> list){
		List<ContactsInfo> ci = new ArrayList<ContactsInfo>();
		for(ContactDb c : list){
			ci.add(objSwitch(c));
		}
		return ci;
	}
	public List<ContactDb> list2Db(List<ContactsInfo> list){
		List<ContactDb> c = new ArrayList<ContactDb>();
		for(ContactsInfo ci : list){
			c.add(objSwitch(ci));
		}
		return c;
	}
	
	public ContactsInfo objSwitch(ContactDb c){
		ContactsInfo ci = new ContactsInfo();
		ci.setAccountID(c.getUserId());
		ci.setAccountName(c.getUserName());
		ci.setDisplayName(c.getRemarkName());
		ci.setFace(c.getAvatar());
		return ci;
	}
	
	public ContactDb objSwitch(ContactsInfo ci){
		ContactDb c = new ContactDb();
		c.setMyId(GlobalContext.getInstance().getUid());
		c.setUserId(ci.getAccountID());
		c.setUserName(ci.getAccountName());
		c.setRemarkName(ci.getDisplayName());
		c.setAvatar(ci.getFace());
		return c;
	}
	
	public void addFriInfo2Db(String uid) {
		new GetTask(UrlHelper.GET_FRI,
				new UpdateViewHelper.UpdateViewListener(ContactsInfo.class) {
					@Override
					public void onComplete(Object obj) {
						ContactsInfo ci = (ContactsInfo)obj;
						ContactsHelper.getInstance().insertContacts(ci);
						EventBus.getDefault().post(new NotifyContactUpdate());
					}
				}, "id", uid);
	}
	
	public void removeByUid(String uid){
		deleteContact(uid);
		EventBus.getDefault().post(new NotifyContactUpdate());
		MessageHelper.getInstance().removeByFriId(uid);
	}
	
	public List<ContactDb> searchByName(String remarkName){
		QueryBuilder<ContactDb> qb = contactDbDao.queryBuilder();
		qb.where(qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.RemarkName.like("%" + remarkName + "%")));
		return qb.list();
	}
	
	public void switchDnded(String uid,boolean dnded){
		ContactDb cd = getContactById(uid);
		if(dnded){
			cd.setDnded(1);
		}else{
			cd.setDnded(0);
		}
		contactDbDao.insertOrReplace(cd);
	}
	
	public boolean getDnded(String uid) {
		boolean rs = false;
		ContactDb cd = getContactById(uid);
		if (cd.getDnded() == null) {
			return rs;
		}
		if (cd.getDnded() == 1) {
			rs = true;
		}
		return rs;
	}

	public void cleanData(){
		contactDbDao.deleteAll();
	}
}
