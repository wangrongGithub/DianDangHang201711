package com.alisure.tool.core;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * ����ʱ������ĺ�����
 * @author ALISURE
 * @version 1507
 */
public class CoreTime {
	
	private static SimpleDateFormat simpleDateFormat = new SimpleDateFormat();
	
	private static String init(String value){
		simpleDateFormat.applyPattern(value);
		return simpleDateFormat.format(new Date());
	}
	
	/**
	 * ���ʱ��,��ʽΪ��pattern
	 * @param pattern
	 * @return
	 */
	public static String getTimeByPattern(String pattern){
		return init(pattern);
	}
	
	/**
	 * ���������ʱ����,��ʽΪ: yyyy-MM-dd HH:mm:ss
	 * @return
	 */
	public static String getDataTime() {
		return init("yyyy-MM-dd HH:mm:ss");
	}
	/**
	 * ���ʱ����,��ʽΪ: HH:mm:ss
	 * @return
	 */
	public static String getTime() {
		return init("HH:mm:ss");
	}
	/**
	 * ���������,��ʽΪ: yyyy-MM-dd
	 * @return
	 */
	public static String getData() {
		return init("yyyy-MM-dd");
	}
	/**
	 * ������,��ʽΪ: yyyy
	 * @return
	 */
	public static String getYear() {
		return init("yyyy");
	}
	/**
	 * �������,��ʽΪ: MM-dd
	 * @return
	 */
	public static String getMonthAndDay() {
		return init("MM-dd");
	}
	/**
	 * ����·�,��ʽΪ: MM
	 * @return
	 */
	public static String getMonth() {
		return init("MM");
	}
	/**
	 * �����,��ʽΪ: dd
	 * @return
	 */
	public static String getDay() {
		return init("dd");
	}
	/**
	 * ���ʱ,��ʽΪ: hh
	 * @return
	 */
	public static String getHour() {
		return init("hh");
	}
	/**
	 * �������,��ʽΪ: mm:ss
	 * @return
	 */
	public static String getMinuteAndSecond() {
		return init("mm:ss");
	}
	/**
	 * �����,��ʽΪ: mm
	 * @return
	 */
	public static String getMinute() {
		return init("mm");
	}
	/**
	 * �����,��ʽΪ: ss
	 * @return
	 */
	public static String getSecond() {
		return init("ss");
	}
	/**
	 * ���ʱ���,��:2014-08-16 23:23:30.787
	 * @return
	 */
	public static Timestamp getTimestamp() {
		return new Timestamp(new Date().getTime());
	}
	/**
	 * ��ȡϵͳ������
	 */
	public static long currentTimeMillis(){
		return System.currentTimeMillis();
	}
	/**
	 * ��ǰϵͳʱ������day����ʱ��
	 * @param day
	 * @return
	 */
	public static String addDay(int day){
		 Calendar calendar = Calendar.getInstance();
		 calendar.setTimeInMillis(System.currentTimeMillis() + ((long) day) * 24 * 3600 * 1000);
		 return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(calendar.getTime());
	}
}
