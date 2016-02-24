package com.baoluo.community.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.baoluo.community.entity.TokenInfo;
/**
 * sharedPreferences 工具类
 * @author Ryan_Fu
 *  2015-5-30
 */
public class SharedPreferencesUtil { 
	
    
    /**
     * 存储的sharedpreferences文件名 
     */
    private static final String FILE_NAME = "buluo.pre"; 
       
    /**
     * 保存数据到文件
     * @param context
     * @param key
     * @param data
     */ 
    public static void saveData(Context context, String key,Object data){ 
   
        String type = data.getClass().getSimpleName(); 
        SharedPreferences sharedPreferences = context 
                .getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE); 
        Editor editor = sharedPreferences.edit(); 
           
        if ("Integer".equals(type)){ 
            editor.putInt(key, (Integer)data); 
        }else if ("Boolean".equals(type)){ 
            editor.putBoolean(key, (Boolean)data); 
        }else if ("String".equals(type)){ 
            editor.putString(key, (String)data); 
        }else if ("Float".equals(type)){ 
            editor.putFloat(key, (Float)data); 
        }else if ("Long".equals(type)){ 
            editor.putLong(key, (Long)data); 
        } 
           
        editor.commit(); 
    } 
       
    /**
     * 从文件中读取数据
     * @param context
     * @param key
     * @param defValue
     * @return
     */ 
    public static Object getData(Context context, String key, Object defValue){ 
           
        String type = defValue.getClass().getSimpleName(); 
        SharedPreferences sharedPreferences = context.getSharedPreferences 
                (FILE_NAME, Context.MODE_PRIVATE); 
           
        //defValue为为默认值，如果当前获取不到数据就返回它 
        if ("Integer".equals(type)){ 
            return sharedPreferences.getInt(key, (Integer)defValue); 
        }else if ("Boolean".equals(type)){ 
            return sharedPreferences.getBoolean(key, (Boolean)defValue); 
        }else if ("String".equals(type)){ 
            return sharedPreferences.getString(key, (String)defValue); 
        }else if ("Float".equals(type)){ 
            return sharedPreferences.getFloat(key, (Float)defValue); 
        }else if ("Long".equals(type)){ 
            return sharedPreferences.getLong(key, (Long)defValue); 
        } 
           
        return null; 
    } 
    /**
	 * 保存token到sharedpreference中
	 * @param access_token
	 * @param token_type
	 * @param expires_in
	 * @param refresh_token
	 */
	public static void setTokenPreference(Context mContext,TokenInfo tokenInfo){
	    SharedPreferences sp= mContext.getSharedPreferences(FILE_NAME, mContext.MODE_PRIVATE);
	    SharedPreferences.Editor editor=sp.edit();
	    editor.putString("access_token", tokenInfo.getAccess_token());
	    editor.putString("token_type", tokenInfo.getToken_type());
	    editor.putString("expires_in", tokenInfo.getExpires_in());
	    editor.putString("refresh_token", tokenInfo.getRefresh_token());
	    editor.commit();
	}
	/**
	 * 从sharedpreference中获取token
	 * @return
	 */
	public static TokenInfo getTokenPreference(Context mContext){
		
	    SharedPreferences sp=mContext.getSharedPreferences(FILE_NAME, mContext.MODE_PRIVATE);
	    String access_token=sp.getString("access_token", "");
	    String refresh_token=sp.getString("refresh_token", "");
	    String token_type=sp.getString("token_type", "");
	    String expires_in=sp.getString("expires_in", "");
	    TokenInfo token = new TokenInfo(access_token,token_type,expires_in,refresh_token);
	    
	    return token;
	}
}
