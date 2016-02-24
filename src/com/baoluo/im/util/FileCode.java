package com.baoluo.im.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import org.apaches.commons.codec.binary.Base64;

import com.baoluo.community.util.L;

public class FileCode {
	private static final String TAG = "FileSave";
	
	private static FileCode instance;
	
	public static FileCode getInstance(){
		if(instance == null){
			instance = new FileCode();
		}
		return instance;
	}
	
	public String file2Base64Str(String filePath){
		StringBuffer sb = new StringBuffer();
		try{
			File file = new File(filePath);  
	        FileInputStream in = new FileInputStream(file);  
	        byte[] data = new byte[in.available()];  
	        in.read(data);  
	        in.close();
	        data = Base64.encodeBase64(data);
	        for (byte bt : data) {  
	            sb.append((char) bt);  
	        }  
		}catch(IOException e){
			L.e(TAG, "file to base64Str error!");
		}
        return new String(sb);
	}
	
	public boolean base64Str2File(String str,String filePath){
		FileOutputStream fos = null;
		try{
			byte[] da = Base64.decodeBase64(str);  
            File file = new File(filePath);
            fos = new FileOutputStream(file);  
            fos.write(da);  
            fos.flush();
            return true;
		}catch(IOException e){
			e.printStackTrace();
			L.e(TAG, "base64Str to File error!");
			return false;
		}finally{
			try {
				fos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
