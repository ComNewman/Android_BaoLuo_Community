package com.baoluo.dao.helper;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.Context;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.dao.ContactDb;
import com.baoluo.dao.DaoSession;
import com.baoluo.dao.GroupDb;
import com.baoluo.dao.MessageDb;
import com.baoluo.dao.MessageDbDao;
import com.baoluo.dao.MessageDbDao.Properties;
import com.baoluo.dao.MsgDb;
import com.baoluo.event.NotifyMessageUpdate;
import com.baoluo.im.entity.Message;
import com.baoluo.im.entity.Msg;

import de.greenrobot.dao.query.QueryBuilder;
import de.greenrobot.event.EventBus;

/**
 * 消息页
 * @author tao.lai
 *
 */
public class MessageHelper {
	
	private static final String TAG = "MessageHelper";
	
	private static MessageHelper instance;
	private static Context ctx;
	private DaoSession mSession;
	private MessageDbDao messageDbDao;
	private MessageHelper() {
	}

	public static MessageHelper getInstance() {
		if (instance == null) {
			instance = new MessageHelper();
			if (ctx == null) {
				ctx = GlobalContext.getInstance().getApplicationContext();
			}
			instance.mSession = GlobalContext.getDaoSession(ctx);
			instance.messageDbDao = instance.mSession.getMessageDbDao();
		}
		return instance;
	}
	
	public void update(MsgDb msg){
		String friId = "";
		if(msg.getToId().equals(GlobalContext.getInstance().getUid())){
			friId = msg.getMyId();
		}else{
			friId = msg.getToId();
		}
		MessageDb md = getById(friId);
		md.setITime(new Date());
		if(isHave(friId)){
			
		}else{
			md.setMyId(GlobalContext.getInstance().getUid());
			md.setToId(friId);
			md.setMsgType(msg.getMsgType());
		}
		messageDbDao.insertOrReplace(md);
		EventBus.getDefault().post(new NotifyMessageUpdate());
	}
	
	public MessageDb getById(String friId){
		QueryBuilder<MessageDb> qb = messageDbDao.queryBuilder();
		qb.where(qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.ToId.eq(friId)));
		List<MessageDb> list = qb.list();
		if(list == null || list.size() == 0){
			return new MessageDb();
		}
		return list.get(0);
	}
	
	public void removeByFriId(String uid){
		QueryBuilder<MessageDb> qb = messageDbDao.queryBuilder();
		qb.where(qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.ToId.eq(uid)));
		qb.buildDelete().executeDeleteWithoutDetachingEntities();
		MsgInfoHelper.getInstance().clearUnReadMsg(uid);
	}
	
	private boolean isHave(String friId) {
		QueryBuilder<MessageDb> qb = messageDbDao.queryBuilder();
		qb.where(qb.and(
				Properties.MyId.eq(GlobalContext.getInstance().getUid()),
				Properties.ToId.eq(friId)));
		return qb.buildCount().count() > 0 ? true : false;
	}
	
	public List<Message> getAllMessageItem() {
		QueryBuilder<MessageDb> qb = messageDbDao.queryBuilder();
		qb.where(Properties.MyId.eq(GlobalContext.getInstance().getUid()))
				.orderDesc(Properties.SetTopTime).orderDesc(Properties.ITime);
		List<MessageDb> mesDb = qb.list();
		List<Message> list = Db2General(mesDb);
		if (list.size() == 0) {
			return list;
		}
		for (Message m : list) {
			if (m.getMsgType() == Msg.MSG_GROUP) {
				fillGroupData(m);
			} else {
				fillPersonData(m);
			}
			m.setUnReadNum(MsgInfoHelper.getInstance().getUnReadMsg(m.getUid()));
			m.setMessage(MsgInfoHelper.getInstance().getLastMsg(m.getUid()));
		}
		return list;
	}
	
	private void fillPersonData(Message m){
		ContactDb cd = ContactsHelper.getInstance().getContactById(m.getUid());
		m.setName(cd.getRemarkName());
		m.setAvatar(cd.getAvatar());
	}
	
	private void fillGroupData(Message m){
		GroupDb gd = GroupHelper.getInstance().getGroupDbByGid(m.getUid());
		m.setName(gd.getGroupName());
		m.setAvatar(gd.getGroupAvatar());
	}

	public List<Message> Db2General(List<MessageDb> lmd){
		List<Message> lm = new ArrayList<Message>();
		for(MessageDb md : lmd){
			lm.add(objSwitch(md));
		}
		return lm;
	}
	
	public MessageDb objSwitch(Message msg) {
		MessageDb md = new MessageDb();
		md.setToId(msg.getUid());
		md.setMsgType((int) msg.getMsgType());
		md.setITime(msg.getItime());
		md.setMyId(GlobalContext.getInstance().getUid());
		md.setSetTopTime(msg.getSetTopTime());
		return md;
	}
	
	public Message objSwitch(MessageDb md){
		Message msg = new Message();
		msg.setUid(md.getToId());
		msg.setMsgType((md.getMsgType()).byteValue());
		msg.setItime(md.getITime());
		msg.setSetTopTime(md.getSetTopTime());
		return msg;
	}
	
	/** 消息置顶开关  **/
	public void switchMsgTop(String toId,boolean setToped){
		MessageDb md = getById(toId);
		if(setToped){
			md.setSetTopTime(new Date());
		}else{
			md.setSetTopTime(null);
		}
		messageDbDao.insertOrReplace(md);
	}
	
	public boolean getMsgToped(String toId) {
		MessageDb md = getById(toId);
		L.i(TAG, GsonUtil.getInstance().obj2Str(md));
		return md.getSetTopTime() == null ? false : true;
	}
	
	private List<String> getAllUid(){
		QueryBuilder<MessageDb> qb = messageDbDao.queryBuilder();
		qb.where(Properties.MyId.eq(GlobalContext.getInstance().getUid()));
		List<MessageDb> md = qb.list();
		List<String> list = new ArrayList<String>();
		if(md == null || md.size() == 0){
			return list;
		}
		for(MessageDb m : md){
			list.add(m.getToId());
		}
		return list;
	}
	
	/**---------------   联系人搜索 start   ----------------**/
	public List<Message> searchPerson(String keyWord){
		List<ContactDb> rs = ContactsHelper.getInstance().searchByName(keyWord);
		/*List<String> uids = getAllUid();
		if(rs == null || rs.size() == 0){
			return new ArrayList<Message>();
		}
		for(ContactDb cd : rs){
			if(!uids.contains(cd.getUserId())){
				rs.remove(cd);
			}
		}*/
		return ContactDb2Message(rs);
	}
	
	private Message ContactDb2Message(ContactDb cd){
		Message ma = new Message();
		ma.setMsgType(Msg.MSG_PERSON);
		ma.setAvatar(cd.getAvatar());
		ma.setUid(cd.getUserId());
		ma.setName(cd.getRemarkName());
		return ma;
	}
	private List<Message> ContactDb2Message(List<ContactDb> listCd){
		List<Message> list = new ArrayList<Message>();
		if(listCd == null || listCd.size() == 0){
			return list;
		}
		for(ContactDb cd : listCd){
			list.add(ContactDb2Message(cd));
		}
		return list;
	}
	/**---------------   联系人搜索 end   ----------------**/
	
	
	/**---------------   群搜索 start   ----------------**/
	public List<Message> searchGroup(String keyWord){
		List<GroupDb> rs = GroupHelper.getInstance().searchByName(keyWord);
		return GroupDb2Message(rs);
	}
	
	private Message GroupDb2Message(GroupDb gd){
		Message m = new Message();
		m.setMsgType(Msg.MSG_GROUP);
		m.setAvatar(gd.getGroupAvatar());
		m.setUid(gd.getGroupId());
		m.setName(gd.getGroupName());
		return m;
	}
	
	private List<Message> GroupDb2Message(List<GroupDb> listCd){
		List<Message> list = new ArrayList<Message>();
		if(listCd == null || listCd.size() == 0){
			return list;
		}
		for(GroupDb cd : listCd){
			list.add(GroupDb2Message(cd));
		}
		return list;
	}
	/**---------------   群搜索 end   ----------------**/

	/** --------------内容搜索start---------------**/
	public List<Message> searchContent(String keyWord,String friUid) {
		List<MsgDb> contens = MsgInfoHelper.getInstance().searchByName(keyWord,friUid);
		return MsgDb2Message(contens);
	}
	private List<Message> MsgDb2Message(List<MsgDb> listC){
		List<Message> list = new ArrayList<Message>();
		if(listC == null || listC.size() == 0){
			return list;
		}
		for(MsgDb md : listC){
			list.add(MsgDb2Message(md));
		}
		return list;
	}
	private Message MsgDb2Message(MsgDb md){
		Message ma = new Message();
		ma.setMsgType((byte)md.getMsgType().intValue());
		ma.setMessage(md.getBody());
		ma.setUid(md.getToId());
		if(ma.getMsgType() == Msg.MSG_GROUP){
			GroupDb gd = GroupHelper.getInstance().getGroupDbByGid(md.getToId());
			ma.setAvatar(gd.getGroupAvatar());
			ma.setName(gd.getGroupName());
		}else{
			ContactDb cd = ContactsHelper.getInstance().getContactById(md.getToId());
			ma.setAvatar(cd.getAvatar());
			ma.setName(cd.getRemarkName());
		}
		return ma;
	}
	/** --------------内容搜索end---------------**/

	public void cleanData(){
		messageDbDao.deleteAll();
	}
	
}
