package com.baoluo.community.entity;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 表情
 * @author Ryan_Fu
 *  2015-5-6
 */
public class SmsHandler implements Serializable{
	
	private static final long serialVersionUID = 6847084416941320473L;
	private static ArrayList<String> list;
	static{
		list=new ArrayList<String>();;
		list.add("/微笑");
		list.add("/撇嘴");
		list.add("/色");
		list.add("/发呆");
		list.add("/得意");
		list.add("/流泪");
		list.add("/害羞");
		list.add("/闭嘴");
		list.add("/睡");
		list.add("/大哭");
		list.add("/尴尬");
		list.add("/发怒");
		list.add("/调皮");
		list.add("/龇牙");
		list.add("/惊讶");
		list.add("/难过");
		list.add("/酷");
		list.add("/冷汗");
		list.add("/抓狂");
		list.add("/吐");
		list.add("/偷笑");
		list.add("/可爱");
		list.add("/白眼");
		list.add("/傲慢");
		list.add("/饥饿");
		list.add("/困");
		list.add("/惊恐");
		list.add("/流汗");
		list.add("/憨笑");
		list.add("/大兵");
		list.add("/奋斗");
		list.add("/咒骂");
		list.add("/疑问");
		list.add("/嘘");
		list.add("/晕");
		list.add("/折磨");
		list.add("/衰");
		list.add("/骷髅");
		list.add("/敲打");
		list.add("/再见");
		list.add("/擦汗");
		list.add("/抠鼻");
		list.add("/鼓掌");
		list.add("/糗大了");
		list.add("/坏笑");
		list.add("/左哼哼");
		list.add("/右哼哼");
		list.add("/哈欠");
		list.add("/鄙视");
		list.add("/委屈");
		list.add("/快哭了");
		list.add("/阴险");
		list.add("/亲亲");
		list.add("/吓");
		list.add("/可伶");
		list.add("/菜刀");
		list.add("/西瓜");
		list.add("/啤酒");
		list.add("/篮球");
		list.add("/乒乓");
		list.add("/咖啡");
		list.add("/饭");
		list.add("/猪头");
		list.add("/玫瑰");
		list.add("/凋谢");
		list.add("/示爱");
		list.add("/爱心");
		list.add("/心碎");
		list.add("/蛋糕");
		list.add("/闪电");
		list.add("/炸弹");
		list.add("/刀");
		list.add("/足球");
		list.add("/瓢虫");
		list.add("/便便");
		list.add("/月亮");
		list.add("/太阳");
		list.add("/礼物");
		list.add("/拥抱");
		list.add("/强");
		list.add("/弱");
		list.add("/握手");
		list.add("/胜利");
		list.add("/抱拳");
		list.add("/勾引");
		list.add("/拳头");
		list.add("/差劲");
		list.add("/爱你");
		list.add("/NO");
		list.add("/OK");
		list.add("/爱情");
		list.add("/飞吻");
		list.add("/跳跳");
		list.add("/发抖");
		list.add("/怄火");
		list.add("/转圈");
		list.add("/磕头");
		list.add("/回头");
		list.add("/跳绳");
		list.add("/挥手");
		list.add("/激动");
		list.add("/街舞");
		list.add("/献吻");
		list.add("/左太极");
		list.add("/右太极");
	}
	
	public static boolean exist(String obj){
		return  list.contains(obj);
	}
	
	public static String getValue(int index){
		return list.get(index);
	}
	
	public static int getIndex(String str){
		return list.indexOf(str);
	}
}