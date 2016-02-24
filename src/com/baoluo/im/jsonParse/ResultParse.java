package com.baoluo.im.jsonParse;

import org.json.JSONException;
import org.json.JSONObject;

import com.baoluo.community.core.Configs;
import com.baoluo.community.entity.DormUser;
import com.baoluo.community.util.GsonUtil;
import com.baoluo.community.util.L;
import com.baoluo.im.entity.CmdMsg;


/**
 * 返回结果 json解析
 * 
 * @author tao.lai
 * 
 */
public class ResultParse {

	private static ResultParse instance;
	
	public static final int RESPONSE_OK = 200;

	public static ResultParse getInstance() {
		if (instance == null) {
			instance = new ResultParse();
		}
		return instance;
	}

	public int getResCode(String rsStr) {
		int code = 0;
		try {
			JSONObject obj = new JSONObject(rsStr);
			code = obj.getInt("code");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return code;
	}
	public String getResStr(String rsStr){
		String res = "";
		try {
			JSONObject obj = new JSONObject(rsStr);
			res = obj.getString("msg");
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return res;
	}
	/**
	 * 建群  从返回结果解析出Gid
	 * @param rsStr
	 * @return
	 */
	public String getGidCreated(String rsStr){
		String gid = "";
		try {
			JSONObject obj = new JSONObject(rsStr);
			if(obj.getInt("code") == Configs.RESPONSE_OK){
				obj = obj.getJSONObject("data");
				return obj.getString("Id");
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return gid;
	}
	
	/**
	 * 解析出宿舍成员
	 * @return
	 */
	public DormUser getAddDormUser(CmdMsg cmdMsg) {
		DormUser user = null;
		try {
			JSONObject obj = new JSONObject(GsonUtil.getInstance().obj2Str(
					cmdMsg));
			JSONObject data = (JSONObject) obj.get("Data");
			user = new DormUser();
			user.setId(data.getString("Id"));
			user.setName(data.getString("Name"));
			user.setFace(data.getString("Face"));
			user.setSex(data.getInt("Sex"));
		} catch (JSONException e) {
			L.e("ResultParse", "");
		}
		return user;
	}

	public DormUser getMinusDormUser(CmdMsg cmdMsg) {
		DormUser user = null;
		try {
			JSONObject obj = new JSONObject(GsonUtil.getInstance().obj2Str(
					cmdMsg));
			JSONObject data = (JSONObject) obj.get("Data");
			user = new DormUser();
			user.setId(data.getString("Id"));
			user.setName("");
			user.setFace("");
			user.setSex(0);
		} catch (JSONException e) {
			L.e("ResultParse", "");
		}
		return user;
	}
}
