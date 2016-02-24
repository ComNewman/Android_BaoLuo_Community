package com.baoluo.im.util;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.baoluo.im.entity.ContactSearch;
/**
 * 联系人搜索帮助类
 * @author xiangyang.fu
 *
 */
public class ContactSearchHelp {
	/**
	 * 按号码-拼音搜索联系人
	 * @param str
	 */
	public static List<ContactSearch> search(String str,
			List<ContactSearch> allContacts, List<ContactSearch> contactList) {
		contactList.clear();
		// 如果搜索条件以0 1 +开头则按号码搜索
		if (str.startsWith("0") || str.startsWith("1") || str.startsWith("+")) {
			for (ContactSearch contact : allContacts) {
				if (contact.getAccountName() != null
						&& contact.getDisplayName() != null) {
					if (contact.getAccountName().contains(str)
							|| contact.getDisplayName().contains(str)) {
						contactList.add(contact);
					}
				}
			}
			return contactList;
		}
		CharacterParser finder = CharacterParser.getInstance();

		String result = "";
		for (ContactSearch contact : allContacts) {
			// 先将输入的字符串转换为拼音
			finder.setResource(str);
			result = finder.getSpelling();
			if (contains(contact, result)) {
				contactList.add(contact);
			}
		}
		return contactList;
	}

	/**
	 * 根据拼音搜索
	 * 
	 * @param str
	 *            正则表达式
	 * @param pyName
	 *            拼音
	 * @param isIncludsive
	 *            搜索条件是否大于6个字符
	 * @return
	 */
	public static boolean contains(ContactSearch contact, String search) {
		if (contact.getDisplayName() == null
				|| contact.getDisplayName().equals("")) {
			return false;
		}

		boolean flag = false;

		// 简拼匹配,如果输入在字符串长度大于6就不按首字母匹配了
		if (search.length() < 6) {
			String firstLetters = FirstLetterUtil.getFirstLetter(contact
					.getDisplayName());
			// 不区分大小写
			Pattern firstLetterMatcher = Pattern.compile(search,
					Pattern.CASE_INSENSITIVE);
			flag = firstLetterMatcher.matcher(firstLetters).find();
		}

		if (!flag) { // 如果简拼已经找到了，就不使用全拼了
			// 全拼匹配
			CharacterParser finder = CharacterParser.getInstance();
			finder.setResource(contact.getDisplayName());
			// 不区分大小写
			Pattern pattern2 = Pattern
					.compile(search, Pattern.CASE_INSENSITIVE);
			Matcher matcher2 = pattern2.matcher(finder.getSpelling());
			flag = matcher2.find();
		}

		return flag;
	}
}
