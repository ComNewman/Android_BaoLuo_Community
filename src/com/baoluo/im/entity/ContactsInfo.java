package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.Comparator;

/**
 * 联系人实体
 * 
 * @author xiangyang.fu
 * 
 */
public class ContactsInfo implements Serializable, Comparator<ContactsInfo> {

	private static final long serialVersionUID = 1L;

	private String AccountID;
	private String AccountName;
	// 备注名
	private String DisplayName;
	private String Face;
	// 聊天封面图片
	private String CoverImgUri;

	private int AccountType;
	private int Ver;
	// 置顶
	private boolean IsTop;
	// 屏蔽
	private boolean IsHide;
	// 特别关注
	private boolean IsSpecial;

	private String sortLetters;

	public ContactsInfo() {

	}

	public ContactsInfo(String accountID, String accountName,
			String displayName, String face, String coverImgUri,
			int accountType, int ver, boolean isTop, boolean isHide,
			boolean isSpecial, String sortLetters) {
		super();
		AccountID = accountID;
		AccountName = accountName;
		DisplayName = displayName;
		Face = face;
		CoverImgUri = coverImgUri;
		AccountType = accountType;
		Ver = ver;
		IsTop = isTop;
		IsHide = isHide;
		IsSpecial = isSpecial;
		this.sortLetters = sortLetters;
	}

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public String getAccountID() {
		return AccountID;
	}

	public void setAccountID(String accountID) {
		AccountID = accountID;
	}

	public String getAccountName() {
		return AccountName;
	}

	public void setAccountName(String accountName) {
		AccountName = accountName;
	}

	public String getDisplayName() {
		return DisplayName;
	}

	public void setDisplayName(String displayName) {
		DisplayName = displayName;
	}

	public String getFace() {
		return Face;
	}

	public void setFace(String face) {
		Face = face;
	}

	public String getCoverImgUri() {
		return CoverImgUri;
	}

	public void setCoverImgUri(String coverImgUri) {
		CoverImgUri = coverImgUri;
	}

	public int getAccountType() {
		return AccountType;
	}

	public void setAccountType(int accountType) {
		AccountType = accountType;
	}

	public int getVer() {
		return Ver;
	}

	public void setVer(int ver) {
		Ver = ver;
	}

	public boolean getIsTop() {
		return IsTop;
	}

	public void setIsTop(boolean isTop) {
		IsTop = isTop;
	}

	public boolean getIsHide() {
		return IsHide;
	}

	public void setIsHide(boolean isHide) {
		IsHide = isHide;
	}

	public boolean isIsSpecial() {
		return IsSpecial;
	}

	public void setIsSpecial(boolean isSpecial) {
		IsSpecial = isSpecial;
	}

	@Override
	public int compare(ContactsInfo o1, ContactsInfo o2) {
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
