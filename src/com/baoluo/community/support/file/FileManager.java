package com.baoluo.community.support.file;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.app.ProgressDialog;
import android.os.Environment;
import android.text.TextUtils;

import com.baoluo.community.util.L;

public class FileManager {

	public static boolean isExternalStorageMounted() {
		boolean canRead = Environment.getExternalStorageDirectory().canRead();
		boolean onlyRead = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED_READ_ONLY);
		boolean unMounted = Environment.getExternalStorageState().equals(
				Environment.MEDIA_UNMOUNTED);
		return !(!canRead || onlyRead || unMounted);
	}

	public static File createNewFileInSDCard(String absolutePath) {
		if (!isExternalStorageMounted()) {
			L.e("sdcard unavailiable");
			return null;
		}

		if (TextUtils.isEmpty(absolutePath)) {
			return null;
		}

		File file = new File(absolutePath);
		if (file.exists()) {
			return file;
		} else {
			File dir = file.getParentFile();
			if (!dir.exists()) {
				dir.mkdirs();
			}

			try {
				if (file.createNewFile()) {
					return file;
				}
			} catch (IOException e) {
				return null;
			}
		}
		return null;
	}
	
	public static File getFileFromServer(String path,String fileName, ProgressDialog pd) throws Exception{
		
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), fileName);
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}
}
