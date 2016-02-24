package com.baoluo.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.TimeUtil;
import com.baoluo.dao.DaoSession;
import com.baoluo.dao.MsgDb;
import com.baoluo.dao.MsgDbDao;
import com.baoluo.dao.MsgDbDao.Properties;
import com.baoluo.event.MainUnReadMsgEvent;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.event.PersonMsgClearEvent;
import com.baoluo.im.entity.Msg;

import de.greenrobot.dao.query.Query;
import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.dao.query.WhereCondition;
import de.greenrobot.event.EventBus;

/**
 * 消息
 * 
 * @author tao.lai
 * 
 */
public class MsgInfoHelper {
	private static final String TAG = "MsgInfoHelper";

	private static MsgInfoHelper instance;
	private static Context ctx;
	private DaoSession mSession;
	private MsgDbDao msgDbDao;

	private MsgInfoHelper() {

	}

	public static MsgInfoHelper getInstance() {
		if (instance == null) {
			instance = new MsgInfoHelper();
			if (ctx == null) {
				ctx = GlobalContext.getInstance().getApplicationContext();
			}
			instance.mSession = GlobalContext.getDaoSession(ctx);
			instance.msgDbDao = instance.mSession.getMsgDbDao();
		}
		return instance;
	}

	public void insertMsg(MsgDb msg) {
		L.i(TAG, "do insert msg :" + GsonUtil.getInstance().obj2Str(msg));
		msgDbDao.insert(msg);
		MessageHelper.getInstance().update(msg);
	}

	public List<Msg> getAllMsg(String friId) {
		String myId = GlobalContext.getInstance().getUid();
		QueryBuilder<MsgDb> qb = msgDbDao.queryBuilder();
		qb.where(qb.and(Properties.MyId.eq(myId), Properties.ToId.eq(friId)))
				.orderAsc(Properties.Id);
		return listSwitch(qb.list());
	}

	public List<Msg> getMsgs(String friId, int startNum, int pageSize) {
		String myId = GlobalContext.getInstance().getUid();
		Query<MsgDb> qb = msgDbDao
				.queryBuilder()
				.where(new WhereCondition.StringCondition("MY_ID='" + myId
						+ "' AND TO_ID='" + friId
						+ "' ORDER BY _id DESC LIMIT " + startNum + ","
						+ pageSize)).build();
		return listSwitch(qb.list());
	}

	public List<Msg> getMsgs(String friId, String scrollStr) {
		String myId = GlobalContext.getInstance().getUid();
		L.e(TAG, "myId=" + myId + ",friId=" + friId + ",scrollStr=" + scrollStr);
		Query<MsgDb> qb = msgDbDao
				.queryBuilder()
				.where(new WhereCondition.StringCondition(" MY_ID='" + myId
						+ "' " + " AND TO_ID='" + friId
						+ "' AND _id>=(SELECT _id FROM MSG_DB WHERE MY_ID='"
						+ myId + "' " + " AND TO_ID='" + friId
						+ "' AND BODY like '%" + scrollStr + "%')")).build();
		L.i(TAG, "list.size = " + qb.list().size());
		return listSwitch(qb.list());
	}

	public int getTotalNum(String friId) {
		List<Msg> list = getAllMsg(friId);
		if (list == null) {
			return 0;
		} else {
			return list.size();
		}
	}

	public void clearMsg(String friId) {
		String myId = GlobalContext.getInstance().getUid();
		QueryBuilder<MsgDb> qb = msgDbDao.queryBuilder();
		qb.where(qb.and(Properties.MyId.eq(myId), Properties.ToId.eq(friId)));
		qb.buildDelete().executeDeleteWithoutDetachingEntities();
		EventBus.getDefault().post(new NotifyMessageUpdate());
		EventBus.getDefault().post(new PersonMsgClearEvent());
	}

	public int getUnReadMsg(String friId) {
		String myId = GlobalContext.getInstance().getUid();
		QueryBuilder<MsgDb> qb = msgDbDao.queryBuilder();
		qb.where(qb.and(Properties.IsRead.eq(1), Properties.MyId.eq(myId),
				Properties.ToId.eq(friId)));
		return (int) qb.buildCount().count();
	}

	public int getUnReadMsg() {
		String myId = GlobalContext.getInstance().getUid();
		QueryBuilder<MsgDb> qb = msgDbDao.queryBuilder();
		qb.where(qb.and(Properties.IsRead.eq(1), Properties.MyId.eq(myId)));
		return (int) qb.buildCount().count();
	}

	public void clearUnReadMsg(String friId) {
		String myId = GlobalContext.getInstance().getUid();
		QueryBuilder<MsgDb> qb = msgDbDao.queryBuilder();
		qb.where(qb.and(Properties.MyId.eq(myId), Properties.ToId.eq(friId)));
		List<MsgDb> list = qb.list();
		if (list == null || list.size() == 0) {
			return;
		}
		for (MsgDb md : list) {
			md.setIsRead(0);
			msgDbDao.insertOrReplace(md);
		}
		EventBus.getDefault().post(new NotifyMessageUpdate());
		EventBus.getDefault().post(new MainUnReadMsgEvent());
	}

	public String getLastMsg(String friId) {
		String myId = GlobalContext.getInstance().getUid();
		QueryBuilder<MsgDb> qb = msgDbDao.queryBuilder();
		qb.where(qb.and(Properties.MyId.eq(myId), Properties.ToId.eq(friId)))
				.orderDesc(Properties.Id).limit(1);
		List<MsgDb> list = qb.list();
		if (list == null || list.size() == 0) {
			return "";
		}
		MsgDb md = list.get(0);
		String lastBody = "";
		if (md.getContentType() == Msg.MSG_TYPE_PIC) {
			lastBody = "[img]";
		} else if (md.getContentType() == Msg.MSG_TYPE_VOICE) {
			lastBody = "[voice]";
		} else {
			lastBody = md.getBody();
		}
		return lastBody;
	}

	public void delAllMsg() {
		L.i(TAG, "MsgInfo all delete");
		msgDbDao.deleteAll();
	}

	public Msg objSwitch(MsgDb msgDb) {
		Msg msg = new Msg();
		msg.setContentType((byte) msgDb.getContentType().intValue());
		msg.setBody(msgDb.getBody());
		msg.setMsgType((byte) msgDb.getMsgType().intValue());
		msg.setExpired(msgDb.getItime().getTime());
		msg.setIsOut((byte) msgDb.getIsOut().intValue());
		msg.setShowTimed((msgDb.getShowTimed() == null ? false : (msgDb
				.getShowTimed().intValue() == 1 ? true : false)));
		if (msg.getMsgType() == Msg.MSG_GROUP) {
			msg.setOwner(msgDb.getFromId());
			msg.setToId(msgDb.getToId());
		} else {
			if (msg.getIsOut() == Msg.MSG_IN) {
				msg.setOwner(msgDb.getToId());
				msg.setToId(msgDb.getMyId());
			} else {
				msg.setOwner(msgDb.getMyId());
				msg.setToId(msgDb.getToId());
			}
		}
		return msg;
	}

	public MsgDb objSwitch(Msg msg) {
		MsgDb msgDb = new MsgDb();
		msgDb.setContentType((int) msg.getContentType());
		msgDb.setBody(msg.getBody());
		msgDb.setMsgType((int) msg.getMsgType());
		msgDb.setItime(long2Date(msg.getExpired()));
		msgDb.setIsOut((int) msg.getIsOut());
		msgDb.setMyId(GlobalContext.getInstance().getUid());
		if (msg.getMsgType() == Msg.MSG_GROUP) {
			msgDb.setFromId(msg.getOwner());
			msgDb.setToId(msg.getToId());
		} else {
			if (msg.getIsOut() == Msg.MSG_IN) {
				msgDb.setToId(msg.getOwner());
			} else {
				msgDb.setToId(msg.getToId());
			}
		}
		msg.setShowTimed(TimeUtil.msgShowTimed(msgDb.getToId(),
				msg.getExpired()));
		msgDb.setShowTimed(msg.isShowTimed() ? 1 : 0);
		return msgDb;
	}

	public List<Msg> listSwitch(List<MsgDb> listDb) {
		List<Msg> list = new ArrayList<Msg>();
		for (MsgDb m : listDb) {
			list.add(objSwitch(m));
		}
		return list;
	}

	private Date long2Date(long itime) {
		if (itime == 0) {
			return new Date();
		} else {
			return new Date(itime);
		}
	}

	public List<MsgDb> searchByName(String keyWord, String uid) {
		Query<MsgDb> query = msgDbDao
				.queryBuilder()
				.where(new WhereCondition.StringCondition("_id IN "
						+ "("
						+ "SELECT _id FROM MSG_DB WHERE MY_ID='"
						+ GlobalContext.getInstance().getUid()
						+ "' "
						+ (StrUtils.isEmpty(uid) ? "" : "AND TO_ID='"
								+ uid + "'") + " AND BODY LIKE '%" + keyWord
						+ "%' ORDER BY _id DESC " + ") GROUP BY TO_ID"))
				.orderDesc(Properties.Id).build();
		return query.list();
	}


	public void cleanData(){
		msgDbDao.deleteAll();
	}
}
