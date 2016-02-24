package com.baoluo.im.util;

import java.util.UUID;

public class UUIDGenerator {
	
	private static UUIDGenerator instance;
	
	/*public static UUIDGenerator getInstance(){
		if(instance == null){
			instance = new UUIDGenerator();
		}
		return instance;
	}*/
	
	public static String getUUID() {
		UUID uuid = UUID.randomUUID();
		String str = uuid.toString();
		String temp = str.substring(0, 8) + str.substring(9, 13)
				+ str.substring(14, 18) + str.substring(19, 23)
				+ str.substring(24);
		return temp;
	}
}
