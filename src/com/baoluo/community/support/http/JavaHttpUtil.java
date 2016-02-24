package com.baoluo.community.support.http;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.InterruptedIOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.URL;
import java.util.Iterator;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import javax.activation.MimetypesFileTypeMap;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;

import android.text.TextUtils;

import com.baoluo.community.core.Configs;
import com.baoluo.community.support.file.FileDownloaderHttpHelper;
import com.baoluo.community.support.file.FileManager;
import com.baoluo.community.support.file.FileUploaderHttpHelper;
import com.baoluo.community.support.http.HttpUtil.HttpMethod;
import com.baoluo.community.ui.BuildConfig;
import com.baoluo.community.util.L;
import com.baoluo.community.util.StrUtils;
import com.baoluo.community.util.Utility;

public class JavaHttpUtil {

	private static final String TAG = "JavaHttpUtil";

	private static final int CONNECT_TIMEOUT = 10 * 1000;
	private static final int READ_TIMEOUT = 10 * 1000;
	private static final int DOWNLOAD_CONNECT_TIMEOUT = 15 * 1000;
	private static final int DOWNLOAD_READ_TIMEOUT = 60 * 1000;
	private static final int UPLOAD_CONNECT_TIMEOUT = 15 * 1000;
	private static final int UPLOAD_READ_TIMEOUT = 5 * 60 * 1000;

	public class NullHostNameVerifier implements HostnameVerifier {
		public boolean verify(String hostname, SSLSession session) {
			return true;
		}
	}

	private TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
		public java.security.cert.X509Certificate[] getAcceptedIssuers() {
			return null;
		}

		public void checkClientTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
		}

		public void checkServerTrusted(
				java.security.cert.X509Certificate[] certs, String authType) {
		}
	} };

	public JavaHttpUtil() {
		// allow Android to use an untrusted certificate for SSL/HTTPS
		// connection
		// so that when you debug app, you can use Fiddler http://fiddler2.com
		// to logs all HTTPS traffic
		try {
			if (BuildConfig.DEBUG) {
				HttpsURLConnection
						.setDefaultHostnameVerifier(new NullHostNameVerifier());
				SSLContext sc = SSLContext.getInstance("SSL");
				sc.init(null, trustAllCerts, new java.security.SecureRandom());
				HttpsURLConnection.setDefaultSSLSocketFactory(sc
						.getSocketFactory());
			}
		} catch (Exception e) {
		}

	}

	public String executeNormalTask(HttpMethod httpMethod, String url,
			Map<String, String> param) throws Exception {
		switch (httpMethod) {
		case Post:
		case Put:
			return doPost(httpMethod, url, param);
		case Get:
		case Delete:
			return doGet(httpMethod, url, param);
		}
		return "";
	}

	private static Proxy getProxy() {
		String proxyHost = System.getProperty("http.proxyHost");
		String proxyPort = System.getProperty("http.proxyPort");
		if (!TextUtils.isEmpty(proxyHost) && !TextUtils.isEmpty(proxyPort)) {
			return new Proxy(java.net.Proxy.Type.HTTP, new InetSocketAddress(
					proxyHost, Integer.valueOf(proxyPort)));
		} else {
			return null;
		}
	}
		
	/**
	 * 参数类型需要传递 对象 方法
	 * @param token
	 * @param url
	 * @param paramsObj
	 * @return
	 * @throws JSONException
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 * @throws ClientProtocolException
	 */
	public static String doPost(String token, String url, String paramsObj){
		String result="";
		HttpPost request = new HttpPost(url);
		request.setHeader("Content-Type", "application/json");
	    request.setHeader("Authorization", "Bearer " + token);
		
		try {
			StringEntity se = new StringEntity(paramsObj, "utf-8");
			se.setContentType("application/json;charset=utf-8");
			request.setEntity(se);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		HttpResponse httpResponse = null;
		try {
			httpResponse = new DefaultHttpClient().execute(request);
			if(httpResponse.getEntity() != null){
				result = EntityUtils.toString(httpResponse.getEntity());
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}

	public static String doPost(HttpMethod httpMethod, String urlAddress,
			Map<String, String> param) throws Exception {
		try {
			StringBuilder urlBuilder = new StringBuilder(urlAddress);
			if (urlAddress.endsWith("/")) {
				urlBuilder.append(param.get("id"));
			} else {
				urlBuilder.append("?").append(Utility.encodeUrl(param));
			}
			URL url = new URL(urlBuilder.toString());
			L.i(TAG, urlBuilder.toString());
			Proxy proxy = getProxy();
			HttpURLConnection uRLConnection;
			if (proxy != null) {
				uRLConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				uRLConnection = (HttpURLConnection) url.openConnection();
			}
			uRLConnection.setDoInput(true);
            uRLConnection.setDoOutput(true);
            uRLConnection.setRequestMethod(httpMethod == HttpMethod.Put ? "PUT":"POST");
            uRLConnection.setUseCaches(false);
            uRLConnection.setConnectTimeout(CONNECT_TIMEOUT);
            uRLConnection.setReadTimeout(READ_TIMEOUT);
            uRLConnection.setInstanceFollowRedirects(false);
            uRLConnection.setRequestProperty("Connection", "Keep-Alive");
            uRLConnection.setRequestProperty("Charset", "UTF-8");
            uRLConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
            if(StrUtils.isEmpty(param.get("Token"))){
            	uRLConnection.setRequestProperty("Authorization","Basic YmFwbHVvX2FwaWtleTpiMjEwOTUzMDFmYzM1MzFm");
            }else{
            	uRLConnection.setRequestProperty("Authorization","Bearer "+param.get("Token"));
            }
            uRLConnection.connect();

            DataOutputStream out = new DataOutputStream(uRLConnection.getOutputStream());
            out.write(Utility.encodeUrl(param).getBytes());
            
            out.flush();
            out.close();
            return handleResponse(uRLConnection);
        } catch (IOException e) {
            e.printStackTrace();
            throw new Exception("post 请求失败", e);
        }
    }
			

	private static String handleResponse(HttpURLConnection httpURLConnection)
			throws Exception {
		int status = 0;
		try {
			status = httpURLConnection.getResponseCode();
		} catch (IOException e) {
			e.printStackTrace();
			httpURLConnection.disconnect();
			throw new Exception("", e);
		}

		L.i(TAG, "status:" + status);
		if (status != HttpURLConnection.HTTP_OK) {
			L.i(TAG, "url:"+httpURLConnection.getURL());
			L.i(TAG, "error info ="+handleError(httpURLConnection));
			return Configs.RESPONSE_ERROR;
		}
		return readResult(httpURLConnection);
	}

	private static String handleError(HttpURLConnection urlConnection)
			throws Exception {
		String result = readError(urlConnection);
		return result;
	}

	private static String readResult(HttpURLConnection urlConnection)
			throws Exception {
		InputStream is = null;
		BufferedReader buffer = null;
		try {
			is = urlConnection.getInputStream();
			String content_encode = urlConnection.getContentEncoding();
			if (!TextUtils.isEmpty(content_encode)
					&& content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			return strBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("", e);
		} finally {
			Utility.closeSilently(is);
			Utility.closeSilently(buffer);
			urlConnection.disconnect();
		}
	}

	private static String readError(HttpURLConnection urlConnection)
			throws Exception {
		InputStream is = null;
		BufferedReader buffer = null;
		try {
			is = urlConnection.getErrorStream();
			if (is == null) {
				throw new Exception("网络错误");
			}

			String content_encode = urlConnection.getContentEncoding();
			if (!TextUtils.isEmpty(content_encode)
					&& content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}

			buffer = new BufferedReader(new InputStreamReader(is));
			StringBuilder strBuilder = new StringBuilder();
			String line;
			while ((line = buffer.readLine()) != null) {
				strBuilder.append(line);
			}
			return strBuilder.toString();
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("", e);
		} finally {
			Utility.closeSilently(is);
			Utility.closeSilently(buffer);
			urlConnection.disconnect();
		}

	}

	/**
	 * 
	 * @param httpMethod
	 * @param urlAddress
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public static String doGet(HttpMethod httpMethod, String urlAddress,
			Map<String, String> param) throws Exception {
		try {
			StringBuilder urlBuilder = new StringBuilder(urlAddress);
			if (urlAddress.endsWith("/")) {
				urlBuilder.append(param.get("id"));
			} else {
				if(param.size()>1){
					urlBuilder.append("?").append(Utility.encodeUrl(param));
				}
			}
			URL url = new URL(urlBuilder.toString());
			L.i(TAG, urlBuilder.toString());
			Proxy proxy = getProxy();
			HttpURLConnection urlConnection;
			if (proxy != null) {
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}

			urlConnection
					.setRequestMethod(httpMethod == HttpMethod.Delete ? "DELETE"
							: "GET");
			L.i(TAG, "httpMethod:"
					+ (httpMethod == HttpMethod.Delete ? "DELETE" : "GET"));
			urlConnection.setDoOutput(false);
			urlConnection.setConnectTimeout(CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(READ_TIMEOUT);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection
					.setRequestProperty("Accept-Encoding", "gzip, deflate");
			if (StrUtils.isEmpty(param.get("Token"))) {
				urlConnection.setRequestProperty("Authorization",
						"Basic YmFwbHVvX2FwaWtleTpiMjEwOTUzMDFmYzM1MzFm");
			} else {
				urlConnection.setRequestProperty("Authorization", "Bearer "
						+ param.get("Token"));
			}
			urlConnection.connect();
			return handleResponse(urlConnection);
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("get 请求失败", e);
		}
	}

	public boolean doGetSaveFile(String urlStr, String path,Map<String,String> params,
			FileDownloaderHttpHelper.DownloadListener downloadListener) {
		File file = FileManager.createNewFileInSDCard(path);
		L.i(TAG,"local File="+path+"   /file==null ? :"+(file==null));
		if (file == null) {
			return false;
		}
		boolean result = false;
		BufferedOutputStream out = null;
		InputStream in = null;
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urlStr);
			L.d("download request=" + urlStr);
			Proxy proxy = getProxy();
			if (proxy != null) {
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoOutput(false);
			urlConnection.setConnectTimeout(DOWNLOAD_CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(DOWNLOAD_READ_TIMEOUT);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Accept-Encoding", "gzip, deflate");
			if (StrUtils.isEmpty(params.get("Token"))) {
				urlConnection.setRequestProperty("Authorization",
						"Basic YmFwbHVvX2FwaWtleTpiMjEwOTUzMDFmYzM1MzFm");
			} else {
				urlConnection.setRequestProperty("Authorization", "Bearer "
						+ params.get("Token"));
			}
			urlConnection.connect();

			int status = urlConnection.getResponseCode();

			if (status != HttpURLConnection.HTTP_OK) {
				return false;
			}

			int bytetotal = (int) urlConnection.getContentLength();
			int bytesum = 0;
			int byteread = 0;
			out = new BufferedOutputStream(new FileOutputStream(file));

			InputStream is = urlConnection.getInputStream();
			String content_encode = urlConnection.getContentEncoding();
			if (!TextUtils.isEmpty(content_encode)
					&& content_encode.equals("gzip")) {
				is = new GZIPInputStream(is);
			}
			in = new BufferedInputStream(is);

			final Thread thread = Thread.currentThread();
			byte[] buffer = new byte[1444];
			while ((byteread = in.read(buffer)) != -1) {
				if (thread.isInterrupted()) {
					if (((float) bytesum / (float) bytetotal) < 0.8f) {
						file.delete();
						throw new InterruptedIOException();
					}
				}
				bytesum += byteread;
				out.write(buffer, 0, byteread);
				if (downloadListener != null && bytetotal > 0) {
					downloadListener.pushProgress(bytesum, bytetotal);
				}
			}
			if (downloadListener != null) {
				downloadListener.completed();
			}
			L.i(TAG,"download request= " + urlStr + " download finished");
			result = true;
		} catch (IOException e) {
			e.printStackTrace();
			L.i(TAG,"download request= " + urlStr + " download failed");
		} finally {
			Utility.closeSilently(in);
			Utility.closeSilently(out);
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		//return result && ImgUtil.isThisBitmapCanRead(path);
		return result;
	}

	private static String getBoundry() {
		StringBuffer _sb = new StringBuffer();
		for (int t = 1; t < 12; t++) {
			long time = System.currentTimeMillis() + t;
			if (time % 3 == 0) {
				_sb.append((char) time % 9);
			} else if (time % 3 == 1) {
				_sb.append((char) (65 + time % 26));
			} else {
				_sb.append((char) (97 + time % 26));
			}
		}
		return _sb.toString();
	}

	private String getBoundaryMessage(String boundary, Map params,
			String fileField, String fileName, String fileType) {
		StringBuffer res = new StringBuffer("--").append(boundary).append(
				"\r\n");

		if (params != null) {
			Iterator keys = params.keySet().iterator();
			while (keys.hasNext()) {
				String key = (String) keys.next();
				String value = (String) params.get(key);
				res.append("Content-Disposition: form-data; name=\"")
						.append(key).append("\"\r\n").append("\r\n")
						.append(value).append("\r\n").append("--")
						.append(boundary).append("\r\n");
			}
		}
		res.append("Content-Disposition: form-data; name=\"").append(fileField)
				.append("\"; filename=\"").append(fileName).append("\"\r\n")
				.append("Content-Type: ").append(fileType).append("\r\n\r\n");

		return res.toString();
	}

	public String doUploadFile(String urlStr, Map<String, String> param,
			String path, String imageParamName,
			final FileUploaderHttpHelper.ProgressListener listener)
			throws Exception {
		L.i(TAG, "up loadFile Path="+path);
		String BOUNDARYSTR = getBoundry();
		File targetFile = new File(path);
		String result = null;

		byte[] barry = null;
		int contentLength = 0;
		String sendStr = "";
		try {
			barry = ("--" + BOUNDARYSTR + "--\r\n").getBytes("UTF-8");
			sendStr = getBoundaryMessage(BOUNDARYSTR, param, imageParamName,
					new File(path).getName(), getContentType(targetFile));
			contentLength = sendStr.getBytes("UTF-8").length
					+ (int) targetFile.length() + 2 * barry.length;
		} catch (UnsupportedEncodingException e) {

		}
		int totalSend = 0;
		String lenstr = Integer.toString(contentLength);
		HttpURLConnection urlConnection = null;
		BufferedOutputStream out = null;
		FileInputStream fis = null;
		try {
			URL url = null;
			url = new URL(urlStr);
			Proxy proxy = getProxy();
			if (proxy != null) {
				urlConnection = (HttpURLConnection) url.openConnection(proxy);
			} else {
				urlConnection = (HttpURLConnection) url.openConnection();
			}
			urlConnection.setConnectTimeout(UPLOAD_CONNECT_TIMEOUT);
			urlConnection.setReadTimeout(UPLOAD_READ_TIMEOUT);
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.setRequestMethod("POST");
			urlConnection.setUseCaches(false);
			urlConnection.setRequestProperty("Connection", "Keep-Alive");
			urlConnection.setRequestProperty("Charset", "UTF-8");
			urlConnection.setRequestProperty("Content-type",
					"multipart/form-data;boundary=" + BOUNDARYSTR);
			urlConnection.setRequestProperty("Content-Length", lenstr);
			((HttpURLConnection) urlConnection)
					.setFixedLengthStreamingMode(contentLength);
			if (StrUtils.isEmpty(param.get("Token"))) {
				urlConnection.setRequestProperty("Authorization",
						"Basic YmFwbHVvX2FwaWtleTpiMjEwOTUzMDFmYzM1MzFm");
			} else {
				urlConnection.setRequestProperty("Authorization", "Bearer "
						+ param.get("Token"));
			}
			urlConnection.connect();

			out = new BufferedOutputStream(urlConnection.getOutputStream());
			out.write(sendStr.getBytes("UTF-8"));
			totalSend += sendStr.getBytes("UTF-8").length;

			fis = new FileInputStream(targetFile);

			int bytesRead;
			int bytesAvailable;
			int bufferSize;
			byte[] buffer;
			int maxBufferSize = 1 * 1024;

			bytesAvailable = fis.available();
			bufferSize = Math.min(bytesAvailable, maxBufferSize);
			buffer = new byte[bufferSize];
			bytesRead = fis.read(buffer, 0, bufferSize);
			int transferred = 0;
			final Thread thread = Thread.currentThread();
			while (bytesRead > 0) {
				if (thread.isInterrupted()) {
					L.e(TAG, "thread is interrupted!");
					throw new InterruptedIOException();
				}
				out.write(buffer, 0, bufferSize);
				totalSend += bytesRead;
				transferred += bytesRead;
				bytesAvailable = fis.available();
				bufferSize = Math.min(bytesAvailable, maxBufferSize);
				bytesRead = fis.read(buffer, 0, bufferSize);
				/*if (transferred % 50 == 0) {
					out.flush();
				}*/
				out.flush();
				if (listener != null) {
					listener.transferred(transferred);
				}
			}
			out.write(barry);
			totalSend += barry.length;
			out.write(barry);
			totalSend += barry.length;
			out.flush();

			out.close();
			if (listener != null) {
				listener.waitServerResponse();
			}
			int status = urlConnection.getResponseCode();
			L.i("JavaHttpUtil", "http status="+status);
			if (status != HttpURLConnection.HTTP_OK) {
				String error = handleError(urlConnection);
				throw new Exception(error);
			} else {
				result = readResult(urlConnection);
			}
		} catch (IOException e) {
			e.printStackTrace();
			throw new Exception("文件上传错误", e);
		} finally {
			Utility.closeSilently(fis);
			//Utility.closeSilently(is);
			Utility.closeSilently(out);
			if (urlConnection != null) {
				urlConnection.disconnect();
			}
		}
		return result;
	}

	/**
	 * 获取文件类型
	 * 
	 * @param file
	 * @return
	 */
	private static String getContentType(File file) {
		String contentType = new MimetypesFileTypeMap().getContentType(file);
		String fileName = file.getName();
		if (fileName.endsWith(".png")) {
			contentType = "image/png";
		} else if (fileName.endsWith(".PNG")) {
			contentType = "image/PNG";
		}
		if (contentType == null || contentType.equals("")) {
			contentType = "application/octet-stream";
		}
		return contentType;
	}
}
