package com.baoluo.community.util;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.annotation.SuppressLint;

/**
 * 关于时间的帮助类
 * 
 * @author Ryan_Fu 2015-5-5
 */
@SuppressLint("SimpleDateFormat")
public class TimeUtil {

	public static final String FORMAT_TIME1 = "yyMMddHHmmss";
	public static final String FORMAT_TIME2 = "yyyy-MM-dd HH:mm:ss";
	public static final String FORMAT_TIME3 = "yy年MM月dd日   HH:mm:ss";
	public static final String FORMAT_TIME4 = "MM-dd hh:mm:ss";
	public static final String FORMAT_TIME5 = "yyyy-MM-dd hh:mm";
	public static final String FORMAT_TIME6 = "yyMMddHHmmss";
	public static final String FORMAT_TIME7 = "MM/dd HH:mm";
	public static final String FORMAT_TIME8 = "yyyy/MM/dd HH:mm";
	public static final String FORMAT_TIME9 =  "yyyy-MM-dd HH-mm-ss";
	public static final String FORMAT_TIME10 = "MM月dd日";
	public static final String FORMAT_TIME11 = "HH:mm";
	
	
	public static final String FORMAT_UTC = "yyyy'-'MM'-'dd'T'kk':'mm':'ss'Z'";
	
	public static SimpleDateFormat getFormat(String formatStr){
		SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
		return sdf;
	}
	
	public static boolean compareTimeStr(String str1,String str2,String formatStr){
		SimpleDateFormat sdf = getFormat(formatStr);
		try {
			if(sdf.parse(str1).before(sdf.parse(str2))){
				return true;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * 获取绝对时间(系统当前时间),时间格式为"yyMMddHHmmss"
	 * 
	 * @return
	 */
	public static String getAbsoluteTime() {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME6);
		return sdf.format(new Date());
	}

	/**
	 * 获取当前时间
	 * 
	 * @return
	 */
	public static String getCurrentTime() {
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_TIME5);// 设置日期格式
		return df.format(new Date());// new Date()为获取当前系统时间
	}

	/**
	 * 截取活动开始时间字符串 只显示月以后的字符
	 * 
	 * @param date
	 * @return
	 */
	public static String getEventStartTime(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return (c.get(Calendar.MONTH) + 1) + "/" + c.get(Calendar.DAY_OF_MONTH)
				+ " " + c.get(Calendar.HOUR_OF_DAY) + ":"
				+ c.get(Calendar.MINUTE);
	}

	/**
	 * 获取两个时间的时间差 如1天2小时
	 * 
	 * @param endDate
	 * @param nowDate
	 * @return
	 */
	public static String getDatePoor(Date endDate, Date nowDate) {
		long nd = 1000 * 24 * 60 * 60;
		long nh = 1000 * 60 * 60;
		// long ns = 1000;
		// 获得两个时间的毫秒时间差异
		long diff = endDate.getTime() - nowDate.getTime();
		// 计算差多少天
		long day = diff / nd;
		// 计算差多少小时
		long hour = diff % nd / nh;
		return day + "天" + hour + "小时";
	}

	/**
	 * 获取相对时间(将给点的时间变换成相对于系统当前时间的差值)，格式为“XX分钟前”
	 * 
	 * @return
	 */
	public static String getRelativeTime(String date) {
		String time = "";
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME6);
			Date dt1 = sdf.parse(date);

			Calendar cl = Calendar.getInstance();
			int year2 = cl.get(Calendar.YEAR);
			int month2 = cl.get(Calendar.MONTH);
			int day2 = cl.get(Calendar.DAY_OF_MONTH);
			int hour2 = cl.get(Calendar.HOUR_OF_DAY);
			int minute2 = cl.get(Calendar.MINUTE);
			int second2 = cl.get(Calendar.SECOND);

			cl.setTime(dt1);
			int year1 = cl.get(Calendar.YEAR);
			int month1 = cl.get(Calendar.MONTH);
			int day1 = cl.get(Calendar.DAY_OF_MONTH);
			int hour1 = cl.get(Calendar.HOUR_OF_DAY);
			int minute1 = cl.get(Calendar.MINUTE);
			int second1 = cl.get(Calendar.SECOND);

			if (year1 == year2) {
				if (month1 == month2) {
					if (day1 == day2) {
						if (hour1 == hour2) {
							if (minute1 == minute2) {
								time = "刚才";
							} else {
								time = (minute2 - minute1) + "分钟前";
							}
						} else if (hour2 - hour1 > 3) {
							time = formatTime(hour1, minute1);
						} else if (hour2 - hour1 == 1) {
							if (minute2 - minute1 > 0) {
								time = "1小时前";
							} else {
								time = (60 + minute2 - minute1) + "分钟前";
							}
						} else {
							time = (hour2 - hour1) + "小时前";
						}
					} else if (day2 - day1 == 1) { // 昨天
						if (hour1 > 12) {
							time = (month1 + 1) + "月" + day1 + "日  下午";
						} else {
							time = (month1 + 1) + "月" + day1 + "日  上午";
						}
					} else {
						time = (month1 + 1) + "月" + day1 + "日";
					}
				} else {
					time = (month1 + 1) + "月" + day1 + "日";
				}
			} else {
				time = year1 + "年" + month1 + "月" + day1;
			}
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return time;
	}

	public static String getFormatTime(String time) {
		SimpleDateFormat sdf = new SimpleDateFormat(FORMAT_TIME1);
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		SimpleDateFormat sdf2 = new SimpleDateFormat(FORMAT_TIME3);
		return sdf2.format(date);
	}

	private static String formatTime(int hour, int minute) {
		String time = "";
		if (hour < 10) {
			time += "0" + hour + ":";
		} else {
			time += hour + ":";
		}

		if (minute < 10) {
			time += "0" + minute;
		} else {
			time += minute;
		}
		return time;
	}

	/**
	 * date类型转换为String类型 formatType格式为yyyy-MM-dd HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒
	 * data Date类型的时间
	 * 
	 * @param data
	 * @param formatType
	 * @return
	 */
	public static String dateToString(Date data, String formatType) {
		return new SimpleDateFormat(formatType).format(data);
	}

	/**
	 * long类型转换为String类型 currentTime要转换的long类型的时间 formatType要转换的string类型的时间格式
	 * 
	 * @param currentTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 */

	public static String longToString(long currentTime, String formatType)
			throws ParseException {
		Date date = longToDate(currentTime, formatType); // long类型转成Date类型
		String strTime = dateToString(date, formatType); // date类型转成String
		return strTime;
	}

	/**
	 * string类型转换为date类型 strTime要转换的string类型的时间，formatType要转换的格式yyyy-MM-dd
	 * HH:mm:ss//yyyy年MM月dd日 HH时mm分ss秒， strTime的时间格式必须要与formatType的时间格式相同
	 * 
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 */
	public static Date stringToDate(String strTime, String formatType) {
		SimpleDateFormat formatter = new SimpleDateFormat(formatType);
		Date date = null;
		try {
			date = formatter.parse(strTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static Date strToDateLong(String strDate) {
		SimpleDateFormat formatter = new SimpleDateFormat(FORMAT_TIME2);
		ParsePosition pos = new ParsePosition(0);
		Date strtodate = formatter.parse(strDate, pos);
		return strtodate;
	}

	/**
	 * long转换为Date类型 currentTime要转换的long类型的时间 formatType要转换的时间格式yyyy-MM-dd
	 * HH:mm:ss /yyyy年MM月dd日 HH时mm分ss秒
	 * 
	 * @param currentTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 */
	public static Date longToDate(long currentTime, String formatType)
			throws ParseException {
		Date dateOld = new Date(currentTime); // 根据long类型的毫秒数生命一个date类型的时间
		String sDateTime = dateToString(dateOld, formatType); // 把date类型的时间转换为string
		Date date = stringToDate(sDateTime, formatType); // 把String类型转换为Date类型
		return date;
	}

	/**
	 * string类型转换为long类型 strTime要转换的String类型的时间 formatType时间格式
	 * strTime的时间格式和formatType的时间格式必须相同
	 * 
	 * @param strTime
	 * @param formatType
	 * @return
	 * @throws ParseException
	 */
	public static long stringToLong(String strTime, String formatType)
			throws ParseException {
		Date date = stringToDate(strTime, formatType); // String类型转成date类型
		if (date == null) {
			return 0;
		} else {
			long currentTime = dateToLong(date); // date类型转成long类型
			return currentTime;
		}
	}

	/**
	 * date类型转换为long类型 date要转换的date类型的时间
	 * 
	 * @param date
	 * @return
	 */
	public static long dateToLong(Date date) {
		return date.getTime();
	}

	public static String convertToTime(long time) {
		SimpleDateFormat df = new SimpleDateFormat(FORMAT_TIME2);
		Date date = new Date(time);
		return df.format(date);
	}

	private static SimpleDateFormat formatBuilder;

	public static String getDate(String format) {
		formatBuilder = new SimpleDateFormat(format);
		return formatBuilder.format(new Date());
	}

	public static String getDate() {
		return getDate(FORMAT_TIME4);
	}
	
	public static long getNow(){
		return (new Date()).getTime();
	}

	public static String getEventDeatilsTime(Date startDate, Date endDate) {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String startString = formatter.format(startDate);
		String endString = formatter.format(endDate);
		String startMouth;
		startMouth = startString.substring(5, 7);
		String startDay;
		startDay = startString.substring(8, 10);
		String endMouth;
		endMouth = endString.substring(5, 7);
		String endDay;
		endDay = endString.substring(8, 10);
		String endHour;
		endHour = endString.substring(11, 13);
		String endMin;
		endMin = endString.substring(14, 16);

		if (startMouth == endMouth && startDay == endDay) {
			return startMouth + "/" + startDay + " " + endHour + ":" + endMin;
		}
		return startMouth + "/" + startDay + " — " + endMouth + "/" + endDay
				+ " " + endHour + ":" + endMin;
	}
	
	public static String getTime(long time){
		SimpleDateFormat sft = getFormat(FORMAT_TIME5);
		return sft.format(new Date(time));
	}
	
	public static String getShowTime(Date date){
		String rs = "未知";
		if(date == null){
			return rs;
		}
		long nd = 1000 * 60 * 60 * 24;
		Date now = new Date();
		long diff = now.getTime() - date.getTime();
		int day = (int) (diff / nd);
		if(day > 1){
			day = 2;
		}
		
		SimpleDateFormat sdf = null;
		switch (day) {
		case 0:
			sdf = getFormat(FORMAT_TIME11);
			rs = sdf.format(date);
			break;
		case 1:
			rs = "昨天";
			break;
		case 2:
			sdf = getFormat(FORMAT_TIME10);
			rs = sdf.format(date);
			break;
		default:
			break;
		}
		return rs;
	}
	
	/**
	 * 聊天 时间显示
	 * @return
	 */
	public static String getShowTime(long time) {
		String rs = "";
		if (time == 0) {
			return rs;
		}
		long nd = 1000 * 60 * 60 * 24;
		Date now = new Date();
		long diff = now.getTime() - time;
		int day = (int) (diff / nd);

		SimpleDateFormat sdf = null;
		switch (day) {
		case 0:
			sdf = getFormat(FORMAT_TIME11);
			break;
		default:
			sdf = getFormat(FORMAT_TIME10 + " " + FORMAT_TIME11);
			break;
		}
		rs = sdf.format(new Date(time));
		return rs;
	}
	
	/**
	 * 消息是否显示时间
	 * @return
	 */
	private static Map<String,Long> mapTime;
	private static final long nm = 1000 * 60 * 5;     //5分钟
	public static boolean msgShowTimed(String uid,long msgTime){
		if(mapTime == null){
			mapTime = new HashMap<String,Long>();
		}
		if(msgTime == 0L){
			msgTime = (new Date()).getTime();
		}
		if(mapTime.get(uid) == null){
			mapTime.put(uid, msgTime);
			return true;
		}
		mapTime.put(uid, msgTime);
		long diff = Math.abs(msgTime - mapTime.get(uid));
		if(diff >= nm){
			return true;
		}else{
			return false;
		}
	}
}
