package com.baoluo.im.service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.harmony.javax.security.sasl.SaslException;
import org.jivesoftware.smack.AccountManager;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.RosterEntry;
import org.jivesoftware.smack.SmackException;
import org.jivesoftware.smack.SmackException.NoResponseException;
import org.jivesoftware.smack.SmackException.NotConnectedException;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.XMPPException.XMPPErrorException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smack.util.StringUtils;
import org.jivesoftware.smackx.search.ReportedData;
import org.jivesoftware.smackx.search.ReportedData.Row;
import org.jivesoftware.smackx.search.UserSearchManager;
import org.jivesoftware.smackx.vcardtemp.packet.VCard;
import org.jivesoftware.smackx.vcardtemp.provider.VCardProvider;
import org.jivesoftware.smackx.xdata.Form;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;

import com.baoluo.community.support.http.MyAsyncTask;
import com.baoluo.community.support.task.UpdateViewHelper;
import com.baoluo.community.util.FormatTools;
import com.baoluo.community.util.L;
import com.baoluo.im.Configs;
import com.baoluo.im.XmppConnection;
import com.baoluo.im.XmppUtil;
import com.baoluo.im.task.LoginTask;
import com.baoluo.im.task.RegisterTask;

public class UserService {
	
	private static final String TAG = "UserService";
	
	private static UserService userService;
	
	public static UserService getInstance(){
		if(userService == null){
			userService = new UserService();
		}
		return userService;
	}
	
	public void doLogin(final Context ctx,String account,String password){
		account = account + "@" +Configs.SERVER_NAME;
		L.i(TAG, "xmpp login  account="+account+"/pwd="+password);
		try{
			new LoginTask(ctx,account,password,new UpdateViewHelper.UpdateViewListener(){
				@Override
				public void onComplete(Object data) {
					Boolean rs =(Boolean)data;
					if(rs){
						//Configs.IM_MY_NAME = data.toString();
						Intent service = new Intent("com.baoluo.community.service.BackService");
						ctx.startService(service);
						L.i(TAG, "xmpp 登录成功");
					}else{
						L.i(TAG, "xmpp 登录异常");
					}
				}
			}).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);	
		}catch(Exception e){
			XmppConnection.closeConnection();
		}
	}
	
	public boolean login(String userName,String psd){
		try {
			XMPPConnection conn = XmppConnection.getConnection();
			conn.login(userName, psd);
			Presence presence = new Presence(Presence.Type.available);
			conn.sendPacket(presence);
			L.i(TAG, "登录成功");
			return true;
		} catch (SaslException e) {
			e.printStackTrace();
		} catch (XMPPException e) {
			e.printStackTrace();
		} catch (SmackException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public void doReg(Context ctx,String account,String password){
		L.i(TAG, "xmpp register  account="+account+",pwd="+password);
		new RegisterTask(ctx,account,password,new UpdateViewHelper.UpdateViewListener(){
			@Override
			public void onComplete(Object data) {
				if((Boolean)data){
					L.i(TAG, "xmpp 注册成功");
				}else{
					L.i(TAG, "xmpp 注册异常");
				}
			}
		}).executeOnExecutor(MyAsyncTask.THREAD_POOL_EXECUTOR);	
	}
	
	public boolean register(String userName,String psd){
        try {  
        	XMPPConnection conn = XmppConnection.getConnection();
            Map<String, String> attributes = new HashMap<String, String>();  
            attributes.put("username", userName);  
            attributes.put("password", psd);  
            attributes.put("name", userName+"@"+Configs.SERVER_HOST);  
            Registration r = new Registration();  
            r.setType(IQ.Type.SET);  
            r.setAttributes(attributes);  
            conn.sendPacket(r);
            PacketFilter packetFilter = new AndFilter(new PacketIDFilter(  
                    r.getPacketID()), new PacketTypeFilter(IQ.class));  
            PacketCollector collector = conn.createPacketCollector(packetFilter);  
            IQ result = (IQ) collector.nextResult();  
            if (result == null) {  
                L.i(TAG,"服务器没响应");  
                return false; 
            } else {  
                String resultString = result.getType().toString();  
                if (resultString.equalsIgnoreCase("result")){
                	L.i(TAG,userName + "注册成功");  
                	return true;
                }else if (resultString.equalsIgnoreCase("error")) {  
                    if (result.getError().toString()  
                            .equalsIgnoreCase("conflict(409)")) {  
                        L.i(TAG,userName + "用户名称已存在");  
                        return false;
                    } else {  
                        L.i(TAG,userName + "注册失败");  
                        return false; 
                    }  
                }  
            }  
        } catch (Exception e) {  
            e.printStackTrace();    
            return false;  
        }  
        return false;
	}
	
	public boolean changePassword(String pwd){
		if (XmppConnection.getConnection() == null){
			return false;
		}
		try {
			AccountManager.getInstance(XmppConnection.getConnection()).changePassword(pwd);
			return true;
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * 删除当前帐号
	 * @return
	 */
	public boolean deleteUser(){  
        try {
        	AccountManager.getInstance(XmppConnection.getConnection()).deleteAccount();
            return true;  
        } catch (Exception e) {  
            return false;  
        }  
    }  
	
 
	/**
	 * 添加一个好友
	 * @param friJid
	 * @param the nickname of the friend
	 * @return
	 */
	public boolean addFri(String friJid, String friName) {
		if (XmppConnection.getConnection() == null){
			return false;
		}
		try {
			XmppConnection.getConnection().getRoster().createEntry(friJid, friName, null);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * 删除好友
	 * @param userName
	 * @return
	 */
	public boolean removeUser(String userName) {
		XMPPConnection conn = XmppConnection.getConnection();
		if (conn == null){
			return false;
		}
		try {
			RosterEntry entry = conn.getRoster().getEntry(userName + "@" + conn.getServiceName());
			if (entry == null){
				entry = conn.getRoster().getEntry(userName);
			}
			conn.getRoster().removeEntry(entry);
			L.i(TAG, "removeUser = "+userName);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	private void searchUsers1(String UserName){
		XMPPConnection conn = XmppConnection.getConnection();
	    try{  
            UserSearchManager search = new UserSearchManager(conn);  
            Form searchForm = search.getSearchForm("users."+Configs.SERVER_HOST);  
            Form answerForm = searchForm.createAnswerForm();  
            answerForm.setAnswer("Username", true);  
            answerForm.setAnswer("search", UserName);  
            ReportedData data = search.getSearchResults(answerForm,Configs.SERVER_HOST);  
              
            List<Row> it = data.getRows();   
            String ansS="";
            for(Row row : it){
            	 ansS+=row.getValues("Username").toString()+"\n";  
            }
            L.i(TAG, ansS);
        }catch(Exception e){
        	L.i(TAG, e.getMessage()+" "+e.getClass().toString());
        }  
	}
	/**
	 * 查询用户
	 * @param searchName
	 * @return
	 */
	public List<HashMap<String, String>> searchUsers(String searchName){
		searchUsers1(searchName);
		return null;
		/*XMPPConnection conn = XmppConnection.getConnection();
		if (conn == null){
			return null;
		}
		HashMap<String, String> user = null;
		List<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
		List<Row> it = null;
		try {
			ServiceDiscoveryManager.getInstanceFor(conn);
			UserSearchManager usm = new UserSearchManager(conn);
			try {
				Form searchForm = usm.getSearchForm("search." + Configs.SERVER_NAME);
				Form answerForm = searchForm.createAnswerForm();
				answerForm.setAnswer("userAccount", true);
				answerForm.setAnswer("userPhote", searchName);
				ReportedData data = usm.getSearchResults(answerForm, "search." + Configs.SERVER_NAME);
				it =  data.getRows();
			} catch (NoResponseException e) {
				e.printStackTrace();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
			if(it == null){
				return null;
			}
			for(Row row : it){
				user = new HashMap<String,String>();
				user.put("userAccount", row.getValues("userAccount").toString());
				user.put("userPhote", row.getValues("userPhote").toString());
				for(String s : row.getValues("userAccount")){
					L.i(TAG, "row.getValue = "+s);
				}
				results.add(user);
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return results;*/
	}
	
	/**
	 * 获取好友列表
	 * @return
	 */
	/*public List<FriendInfo111> getFriList(){
		XMPPConnection conn = XmppConnection.getConnection();
		Roster roster = conn.getRoster();
		Collection<RosterGroup> groups = roster.getGroups();
		String friMood = "";
		FriendInfo111 friInfo;
		List<FriendInfo111> friList = new ArrayList<FriendInfo111>();
		
		for(RosterGroup group : groups){
			Collection<RosterEntry> entries = group.getEntries();
			for(RosterEntry entry : entries){
				if("both".equals(entry.getType().name())){   //双向好友
					friInfo = new FriendInfo111();
					friInfo.setUsername(XmppUtil.getJidToUsername(entry.getUser()));
					if (StrUtils.isEmpty(friMood)) {
						friMood = "Q我吧，静待你的来信！";
					}
					friInfo.setMood(friMood);
					friList.add(friInfo);
					friInfo = null;
				}
			}
		}
		return friList;
	}*/
	
	/**
	 * 修改心情
	 * @return
	 */
	public boolean setHumor(String humor){
		Presence presence = new Presence(Presence.Type.available);  
        presence.setStatus(humor);  
        try {
			XmppConnection.getConnection().sendPacket(presence);
			return true;
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}
		return false;
	}
	
	public VCard getUserVCard(String user) {
		if (XmppConnection.getConnection() == null){
			return null;
		}
		VCard vcard = new VCard();
		try {
			try {
				vcard.load(XmppConnection.getConnection(), user);
			} catch (NoResponseException e) {
				e.printStackTrace();
			} catch (NotConnectedException e) {
				e.printStackTrace();
			}
		} catch (XMPPException e) {
			e.printStackTrace();
		}
		return vcard;
	}
	
	public Drawable getAvatar(String user) {
		if (XmppConnection.getConnection() == null)
			return null;
		ByteArrayInputStream bais = null;
		try {
			VCard vcard = new VCard();
			ProviderManager.addIQProvider("vCard", "vcard-temp",
					new VCardProvider());
			if (user == "" || user == null || user.trim().length() <= 0) {
				return null;
			}
			vcard.load(XmppConnection.getConnection(), user + "@"
					+ XmppConnection.getConnection().getServiceName());
			if (vcard == null || vcard.getAvatar() == null)
				return null;
			bais = new ByteArrayInputStream(vcard.getAvatar());
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		return FormatTools.getInstance().InputStream2Drawable(bais);
	}
	
	/**
	 * 设置头像
	 * @return
	 */
	public boolean setAvatar(String filePath){
		VCard vcard = new VCard();  
        try {
			vcard.load(XmppConnection.getConnection());
		} catch (NoResponseException e1) {
			e1.printStackTrace();
		} catch (XMPPErrorException e1) {
			e1.printStackTrace();
		} catch (NotConnectedException e1) {
			e1.printStackTrace();
		}  
        byte[] bytes = null;
		try {
			bytes = XmppUtil.getFileBytes(filePath);
		} catch (IOException e1) {
			e1.printStackTrace();
		}  
        String encodedImage = StringUtils.encodeBase64(bytes);  
        vcard.setAvatar(bytes, XmppUtil.getImgType(filePath));  
        vcard.setField("PHOTO", "<TYPE>"+XmppUtil.getImgType(filePath)+"</TYPE><BINVAL>"  
        + encodedImage + "</BINVAL>", true);  
        try {
			vcard.save(XmppConnection.getConnection());
			return true;
		} catch (NoResponseException e) {
			e.printStackTrace();
		} catch (XMPPErrorException e) {
			e.printStackTrace();
		} catch (NotConnectedException e) {
			e.printStackTrace();
		}  
		return false;
	}
}
