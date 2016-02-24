package com.baoluo.im.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import android.annotation.SuppressLint;

import com.baoluo.community.ui.customview.SlideCutView;

/**
 * 查找好友实体
 * 
 * @author tao.lai
 * 
 */
public class ContactSearch implements Serializable,Comparator<ContactSearch>{
	private static final long serialVersionUID = 1L;
	
	private String AccountID;
	private String AccountName;
	private String DisplayName;
	private String Face;
	private String CoverImgUri;
	private int AccountType;
	private int Ver;
	private boolean IsTop;
	private boolean IsHide;
	private boolean IsSpecial;
	private String sortLetters;
	private SlideCutView slideCutView;
	private boolean isCatalog;
	
	public ContactSearch() {

	}

	/**
	 * @param sortLetters
	 */
	public ContactSearch(String sortLetters) {
		this.isCatalog = true;
		this.sortLetters = sortLetters;
	}
	
    public static ContactSearch objSwitch(ContactsInfo ci){
    	ContactSearch cs = new ContactSearch();
    	cs.setAccountID(ci.getAccountID());
    	cs.setAccountName(ci.getAccountName());
    	cs.setDisplayName(ci.getDisplayName());
    	cs.setFace(ci.getFace());
    	cs.setCoverImgUri(ci.getCoverImgUri());
    	cs.setAccountType(ci.getAccountType());
    	cs.setVer(ci.getVer());
    	cs.setIsTop(ci.getIsTop());
    	cs.setIsHide(ci.getIsHide());
    	cs.setIsSpecial(ci.isIsSpecial());
    	cs.setSortLetters(ci.getSortLetters());
    	return cs;
    }
    
    public static ContactsInfo objSwitch(ContactSearch ci){
    	ContactsInfo cs = new ContactsInfo();
    	cs.setAccountID(ci.getAccountID());
    	cs.setAccountName(ci.getAccountName());
    	cs.setDisplayName(ci.getDisplayName());
    	cs.setFace(ci.getFace());
    	cs.setCoverImgUri(ci.getCoverImgUri());
    	cs.setAccountType(ci.getAccountType());
    	cs.setVer(ci.getVer());
    	cs.setIsTop(ci.isIsTop());
    	cs.setIsHide(ci.isIsHide());
    	cs.setIsSpecial(ci.isIsSpecial());
    	cs.setSortLetters(ci.getSortLetters());
    	return cs;
    }
    
    public static List<ContactSearch> listSwitch(List<ContactsInfo> list){
    	List<ContactSearch> ls = new ArrayList<ContactSearch>();
    	for(ContactsInfo c : list){
    		ls.add(objSwitch(c));
    	}
    	return ls;
    }
    
   /* public static List<ContactsInfo> list2Info(List<ContactSearch> list){
    	List<ContactsInfo> ls = new ArrayList<ContactsInfo>();
    	for(ContactSearch c : list){
    		ls.add(objSwitch(c));
    	}
    	return ls;
    }*/
	
	public String getAccountID() {
		return AccountID;
	}

	public boolean isCatalog() {
		return isCatalog;
	}

	public void setCatalog(boolean isCatalog) {
		this.isCatalog = isCatalog;
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

	public boolean isIsTop() {
		return IsTop;
	}

	public void setIsTop(boolean isTop) {
		IsTop = isTop;
	}

	public boolean isIsHide() {
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

	public String getSortLetters() {
		return sortLetters;
	}

	public void setSortLetters(String sortLetters) {
		this.sortLetters = sortLetters;
	}

	public SlideCutView getSlideCutView() {
		return slideCutView;
	}

	public void setSlideCutView(SlideCutView slideCutView) {
		this.slideCutView = slideCutView;
	}
	
	public static List<ContactSearch> DataInit(List<ContactSearch> list){
		ContactSearch c = null;
		for(int i = 0; i < list.size(); i++){
			ContactSearch item = list.get(i);
			if (i == getPositionForSection(list, item.getSortLetters()
					.charAt(0))) {
				c = new ContactSearch(item.getSortLetters());
				list.add(i, c);
				i ++;
			}
		}
		return list;
	}
	
	@SuppressLint("DefaultLocale")
	public static int getPositionForSection(List<ContactSearch> list, int section) {
		for (int i = 0; i < list.size(); i++) {
			String sortStr = list.get(i).getSortLetters();
			char firstChar = sortStr.toUpperCase().charAt(0);
			if (firstChar == section) {
				return i;
			}
		}
		return -1;
	}
	
	@Override
	public int compare(ContactSearch o1, ContactSearch o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
