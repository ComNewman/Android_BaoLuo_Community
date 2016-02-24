package com.baoluo.community.support.voice;

import java.io.File;
import java.io.IOException;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.text.format.Time;

import com.baoluo.community.util.L;

/**
 * 录音帮助类
 * 
 * @author xiangyang.fu
 * 
 */
@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class RecordHelp {

	private MediaRecorder mediaRecorder; // 录音控制
	private MediaPlayer mediaPlayer;

	private static RecordHelp recordHelp = null;

	public static RecordHelp getInstance() {
		if (recordHelp == null) {
			recordHelp = new RecordHelp();
		}
		return recordHelp;
	}

	/**
	 * 开始录音
	 * 
	 * @return recordFilePath 录音的文件保存的路径
	 */
	public String startAudio() {
		String recordFilePath = getRecordFilePath();
		mediaRecorder = new MediaRecorder();
		mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
		mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.RAW_AMR);
		mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
		mediaRecorder.setOutputFile(recordFilePath);
		try {
			mediaRecorder.prepare();
			mediaRecorder.start();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return recordFilePath;
	}

	/**
	 * 停止录音
	 */
	public void stopAudio() {
		if (null != mediaRecorder) {
			mediaRecorder.stop();
			mediaRecorder.release();
			mediaRecorder = null;
		}
	}

	/**
	 * 通过url播放音频
	 * 
	 * @param url
	 * @param context
	 */
	public void playNetRecord(String url, Context context) {
		mediaPlayer = new MediaPlayer();
		Uri uri = Uri.parse(url);
		try {
			mediaPlayer.setDataSource(context, uri);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 播放本地音频文件
	 * 
	 * @param path
	 */
	public void playRecord(String path,OnCompletionListener completionListener) {
		if(isPlaying()){
			return;
		}
		mediaPlayer = new MediaPlayer();
		try {
			mediaPlayer.setOnCompletionListener(completionListener);
			mediaPlayer.setOnErrorListener(new MediaPlayer.OnErrorListener() {
				@Override
				public boolean onError(MediaPlayer mp, int what, int extra) {
					L.e("RecordHelper:","playEnd error!.............");
					return false;
				}
			});
			mediaPlayer.setDataSource(path);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalStateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public boolean isPlaying(){
		if(mediaPlayer != null){
			return true;
		}
		return false;
	}

	/**
	 * 使用系统自带的播放器 播放本地录音
	 * 
	 * @param f
	 * @param context
	 */
	public void palyRecord(File f, Context context) {
		Intent intent = new Intent();
		intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		intent.setAction(android.content.Intent.ACTION_VIEW);
		String type = getMIMEType(f);
		intent.setDataAndType(Uri.fromFile(f), type);
		context.startActivity(intent);
	}

	/**
	 * 暂停播放
	 */
	public void pausePlayRecord() {
		if (null != mediaPlayer) {
			mediaPlayer.pause();
		}
	}

	/**
	 * 停止播放
	 */
	public void stopPlayRecord() {
		if (null != mediaPlayer) {
			mediaPlayer.stop();
			mediaPlayer.release();
			mediaPlayer = null;
		}
	}

	/**
	 * 从文件夹获取 文件的类型
	 * 
	 * @param file
	 * @return
	 */
	private String getMIMEType(File file) {

		String end = file
				.getName()
				.substring(file.getName().lastIndexOf(".") + 1,
						file.getName().length()).toLowerCase();
		String type = "";
		if (end.equals("mp3") || end.equals("aac") || end.equals("aac")
				|| end.equals("amr") || end.equals("mpeg") || end.equals("mp4")) {
			type = "audio";
		} else if (end.equals("jpg") || end.equals("gif") || end.equals("png")
				|| end.equals("jpeg")) {
			type = "image";
		} else {
			type = "*";
		}
		type += "/*";
		return type;
	}

	/**
	 * 获取系统当前时间
	 * 
	 * @return
	 */
	public String GetSystemDateTime() {
		Time localTime = new Time();
		localTime.setToNow();
		return localTime.format("%Y%m%d%H%M%S");
	}

	/**
	 * 获取录音保存的文件夹路径
	 * 
	 * @return
	 */
	public String getVoicePath() {
		String path = null;
		if (Environment.getExternalStorageState().equals(
				android.os.Environment.MEDIA_MOUNTED)) {
			path = Environment.getExternalStorageDirectory().toString();// 获得根目录

		}
		return path + "/" + "baoluo" + "/" + "voice";
	}

	/**
	 * 删除录音
	 * 
	 * @param file
	 */
	public void deleteFile(File file) {
		if (file.exists()) { 
			if (file.isFile()) { 
				file.delete(); 
			} else if (file.isDirectory()) { 
				File files[] = file.listFiles(); 
				for (int i = 0; i < files.length; i++) { 
					this.deleteFile(files[i]); 
				}
			}
			file.delete();
		} else {
			L.e("文件不存在！" + "\n");
		}
	}

	/**
	 * 获取录音后文件保存的 路径
	 * 
	 * @return
	 */
	public String getRecordFilePath() {
		String filePath = getVoicePath();
		File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		String fileAudioName = GetSystemDateTime() + ".amr";
		return filePath + "/" + fileAudioName;
	}
}
