package com.baoluo.community.entity;

import java.io.Serializable;
/**
 * 语音实体
 * @author xiangyang.fu
 *
 */
public class VoiceInfo implements Serializable{

	private static final long serialVersionUID = 1L;
	
	private String Id;
	//格式默认 为.arm
	private String Format;
	private int Lenght;
	private String VoiceUri;
	public VoiceInfo() {
		
	}
	public VoiceInfo(String id, String format, int lenght, String voiceUri) {
		super();
		Id = id;
		Format = format;
		Lenght = lenght;
		VoiceUri = voiceUri;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getFormat() {
		return Format;
	}
	public void setFormat(String format) {
		Format = format;
	}
	public int getLenght() {
		return Lenght;
	}
	public void setLenght(int lenght) {
		Lenght = lenght;
	}
	public String getVoiceUri() {
		return VoiceUri;
	}
	public void setVoiceUri(String voiceUri) {
		VoiceUri = voiceUri;
	}
	
}
