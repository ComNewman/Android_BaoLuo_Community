package com.baoluo.community.util;

import java.lang.reflect.Type;
import java.util.List;

import com.google.gson.Gson;

public class GsonUtil {
	private static final String TAG = "GsonUtil";
	private static Gson gson = null;
	private static GsonUtil gsonUtil = null;
	
	private static Gson getGson(){
		if(gson == null){
			gson = new Gson();
		}
		return gson;
	}
	public static GsonUtil getInstance(){
		if(gsonUtil == null){
			gsonUtil = new GsonUtil();
		}
		return gsonUtil;
	}
	
	public Object str2Obj(String jsonStr,Class<?> cls){
		try{
			return getGson().fromJson(jsonStr, cls);
		}catch(Exception e){
			L.e(TAG, "jsonStr="+jsonStr);
			L.e(TAG, "数据转化错误");
			return null;
		}
	}
	
	public String List2Str(List<?> list){
		return getGson().toJson(list);
	}
	
	public List<?> String2List(String str, Type tp){
		try{
			return getGson().fromJson(str, tp);
		}catch (Exception e){
			L.e(TAG,"jsonStr="+str);
			L.e(TAG,"数据转化错误");
			return null;
		}
	}
	
	public String obj2Str(Object obj){
		return getGson().toJson(obj);
	}
}
