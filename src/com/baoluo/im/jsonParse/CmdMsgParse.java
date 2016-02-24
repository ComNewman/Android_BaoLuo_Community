package com.baoluo.im.jsonParse;

import org.json.JSONException;
import org.json.JSONObject;

import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.im.entity.AddFriEntity;
import com.baoluo.im.entity.CmdMsg;

public class CmdMsgParse {
	private static final String TAG = "CmdMsgParse";
	
	private static CmdMsgParse instance;
	public static CmdMsgParse getInstance(){
		if(instance == null){
			instance = new CmdMsgParse();
		}
		return instance;
	}

	public AddFriEntity getAddFriEntity(CmdMsg cmdMsg){
		JSONObject obj;
		AddFriEntity e = new AddFriEntity();
		try {
			obj = new JSONObject(GsonUtil.getInstance().obj2Str(cmdMsg));
			JSONObject data = (JSONObject) obj.get("Data");
			e.setName(data.getString("Name"));
			e.setUid(data.getString("Uid"));
			e.setFace(data.getString("Face"));
		} catch (JSONException e1) {
			e1.printStackTrace();
			L.e(TAG, "加好友申请数据请求失败");
		}
		return e;
	}
	
	public String getGroupGid(CmdMsg cmdMsg) {
		JSONObject obj;
		String gid = "";
		try {
			obj = new JSONObject(GsonUtil.getInstance().obj2Str(cmdMsg));
			JSONObject data = (JSONObject) obj.get("Data");
			gid = data.getString("Gid");
		} catch (JSONException e1) {
			e1.printStackTrace();
			L.e(TAG, "邀请加入群信息 解析错误");
		}
		return gid;
	}
	
	/**
	 * 请求加对方好友，对方id
	 * 如果拒绝返回  ""
	 * @return
	 */
	public String getAcceptedId(CmdMsg cmdMsg) {
		String rs = "";
		JSONObject obj;
		try {
			obj = new JSONObject(GsonUtil.getInstance().obj2Str(cmdMsg));
			JSONObject data = (JSONObject) obj.get("Data");
			if(data.getInt("Ret") == 1){
				rs = data.getString("Uid");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			L.i(TAG, "加好友结果解析失败");
		}
		return rs;
	}
}
