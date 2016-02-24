package com.baoluo.community.support.setting;

import android.content.Context;

import com.baoluo.community.application.GlobalContext;
import com.baoluo.community.entity.LocationArea;
import com.baoluo.community.entity.TokenInfo;
import com.baoluo.community.entity.UserSelf;

public class SettingUtility {

	private static final String FIRST_START = "firstStart";
	private static final String PUSH_BOOLEAN = "push";
	private static final String SHIELD_STRANGER = "stranger";
	private static final String NIGHT_MODE = "nightMode";
	private static final String VOICE_BOOLEAN = "voice";
	private static final String SHAKE_BOOLEAN = "shake";

	private static final String OPEN_LOCATION = "openLocation";
	private static final String ADD_ME_CHECK = "addMeCheck";
	private static final String FIND_ME_BY_BAOLUO = "findMeByBaoluo";
	private static final String FIND_ME_BY_PHONE = "findMeByPhone";

	private SettingUtility() {

	}

	private static Context getContext() {
		return GlobalContext.getInstance();
	}

	public static boolean firstStart() {
		boolean value = SettingHelper.getSharedPreferences(getContext(),
				FIRST_START, true);
		if (value) {
			SettingHelper.setEditor(getContext(), FIRST_START, false);
		}
		return value;
	}

	/** 个人设置 保存开始 **/

	public static void setPushBoolean(boolean push) {
		SettingHelper.setEditor(getContext(), PUSH_BOOLEAN, push);
	}

	public static boolean getPushBoolean() {
		return SettingHelper.getSharedPreferences(getContext(), PUSH_BOOLEAN,
				true);
	}

	public static void setStrangerBoolean(boolean stranger) {
		SettingHelper.setEditor(getContext(), SHIELD_STRANGER, stranger);
	}

	public static boolean getStrangerBoolean() {
		return SettingHelper.getSharedPreferences(getContext(),
				SHIELD_STRANGER, false);
	}

	public static void setNightBoolean(boolean night) {
		SettingHelper.setEditor(getContext(), NIGHT_MODE, night);
	}

	public static boolean getNightBoolean() {
		return SettingHelper.getSharedPreferences(getContext(), NIGHT_MODE,
				false);
	}

	public static void setVoiceBoolean(boolean voice) {
		SettingHelper.setEditor(getContext(), VOICE_BOOLEAN, voice);
	}

	public static boolean getVoiceBoolean() {
		return SettingHelper.getSharedPreferences(getContext(), VOICE_BOOLEAN,
				true);
	}

	public static void setShakeBoolean(boolean shake) {
		SettingHelper.setEditor(getContext(), SHAKE_BOOLEAN, shake);
	}

	public static boolean getShakeBoolean() {
		return SettingHelper.getSharedPreferences(getContext(), SHAKE_BOOLEAN,
				true);
	}

	public static void setLocationBoolean(boolean location) {
		SettingHelper.setEditor(getContext(), OPEN_LOCATION, location);
	}

	public static boolean getLocationBoolean() {
		return SettingHelper.getSharedPreferences(getContext(), OPEN_LOCATION,
				true);
	}

	public static void setAddMeBoolean(boolean addMeCheck) {
		SettingHelper.setEditor(getContext(), ADD_ME_CHECK, addMeCheck);
	}

	public static boolean getAddMeCheckBoolean() {
		return SettingHelper.getSharedPreferences(getContext(), ADD_ME_CHECK,
				true);
	}

	public static void setFindMeBaoluoBoolean(boolean findMeBaoluo) {
		SettingHelper.setEditor(getContext(), FIND_ME_BY_BAOLUO, findMeBaoluo);
	}

	public static boolean getFindMeBaoluoBoolean() {
		return SettingHelper.getSharedPreferences(getContext(),
				FIND_ME_BY_BAOLUO, true);
	}

	public static void setFindMePhoneBoolean(boolean findMePhone) {
		SettingHelper.setEditor(getContext(), FIND_ME_BY_PHONE, findMePhone);
	}

	public static boolean getFindMePhoneBoolean() {
		return SettingHelper.getSharedPreferences(getContext(),
				FIND_ME_BY_PHONE, true);
	}

	/** 个人设置 保存结束 **/

	public static String getDromId() {
		return SettingHelper.getSharedPreferences(getContext(), "dormId", "");
	}

	public static void setDormId(String dormId) {
		SettingHelper.setEditor(getContext(), "dormId", dormId);
	}

	public static void setAccount(String account) {
		SettingHelper.setEditor(getContext(), "account", account);
	}

	public static String getAccount() {
		return SettingHelper.getSharedPreferences(getContext(), "account", "");
	}

	public static void setPassword(String psd) {
		SettingHelper.setEditor(getContext(), "password", psd);
	}

	public static String getPassword() {
		return SettingHelper.getSharedPreferences(getContext(), "password", "");
	}

	public static void setUid(String uid) {
		SettingHelper.setEditor(getContext(), "user_uid", uid);
	}

	public static String getUid() {
		return SettingHelper.getSharedPreferences(getContext(), "user_uid", "");
	}

	public static void setTodayTime(String todayTime) {
		SettingHelper.setEditor(getContext(), "todayTime", todayTime);
	}

	public static String getTodayTime() {
		return SettingHelper
				.getSharedPreferences(getContext(), "todayTime", "");
	}

	public static void setTokenInfo(TokenInfo tokenInfo) {
		SettingHelper.setEditor(getContext(), "access_token",
				tokenInfo.getAccess_token());
		SettingHelper.setEditor(getContext(), "token_type",
				tokenInfo.getToken_type());
		SettingHelper.setEditor(getContext(), "expires_in",
				tokenInfo.getExpires_in());
		SettingHelper.setEditor(getContext(), "refresh_token",
				tokenInfo.getRefresh_token());
	}

	public static String getToken() {
		return SettingHelper.getSharedPreferences(getContext(), "access_token",
				"");
	}

	public static TokenInfo getTokenInfo() {
		String access_token = SettingHelper.getSharedPreferences(getContext(),
				"access_token", "");
		String refresh_token = SettingHelper.getSharedPreferences(getContext(),
				"refresh_token", "");
		String token_type = SettingHelper.getSharedPreferences(getContext(),
				"token_type", "");
		String expires_in = SettingHelper.getSharedPreferences(getContext(),
				"expires_in", "");
		TokenInfo token = new TokenInfo(access_token, token_type, expires_in,
				refresh_token);
		return token;
	}

	public static void setDormUserInfo(UserSelf userSelf) {
		SettingHelper.setEditor(getContext(), "face", userSelf.getFace());
		SettingHelper.setEditor(getContext(), "id", getUserSelf().getId());
		SettingHelper.setEditor(getContext(), "nickName",
				userSelf.getNickName());
	}

	public static UserSelf getDormUserSelf() {
		String face = SettingHelper.getSharedPreferences(getContext(), "face",
				"");
		String id = SettingHelper.getSharedPreferences(getContext(), "id", "");
		String nickName = SettingHelper.getSharedPreferences(getContext(),
				"nickName", "");
		UserSelf self = new UserSelf();
		self.setFace(face);
		self.setId(id);
		self.setNickName(nickName);
		return self;
	}

	public static void setUserSelf(UserSelf userSelf) {
		SettingHelper.setEditor(getContext(), "face", userSelf.getFace());
		SettingHelper.setEditor(getContext(), "id", userSelf.getId());
		SettingHelper.setEditor(getContext(), "nickName",
				userSelf.getNickName());
		SettingHelper.setEditor(getContext(), "sex", userSelf.getSex());
		SettingHelper.setEditor(getContext(), "userName",
				userSelf.getUserName());
		SettingHelper.setEditor(getContext(), "qr_code", userSelf.getQRCode());
		SettingHelper.setEditor(getContext(), "province", userSelf.getLocationArea().getProvince());
		SettingHelper.setEditor(getContext(), "city", userSelf.getLocationArea().getCity());
	}

	public static UserSelf getUserSelf() {
		String face = SettingHelper.getSharedPreferences(getContext(), "face",
				"");
		String id = SettingHelper.getSharedPreferences(getContext(), "id", "");
		String nickName = SettingHelper.getSharedPreferences(getContext(),
				"nickName", "");
		int sex = SettingHelper.getSharedPreferences(getContext(), "sex", 2);
		String userName = SettingHelper.getSharedPreferences(getContext(),
				"userName", "");
		String code = SettingHelper.getSharedPreferences(getContext(),
				"qr_code", "");
		String province = SettingHelper.getSharedPreferences(getContext(), "province", "");
		String city = SettingHelper.getSharedPreferences(getContext(), "city", "");
		LocationArea locationArea = new LocationArea(province, city);
		
		UserSelf self = new UserSelf();
		self.setFace(face);
		self.setId(id);
		self.setNickName(nickName);
		self.setSex(sex);
		self.setUserName(userName);
		self.setQRCode(code);
		self.setLocationArea(locationArea);
		return self;
	}
}
