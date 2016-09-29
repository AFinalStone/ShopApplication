package com.shuimunianhua.xianglixiangqin.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * TimeUtils
 * 
 * @author <a href="http://www.trinea.cn" target="_blank">Trinea</a> 2013-8-24
 */
public class TimeUtils {

    public static final SimpleDateFormat DEFAULT_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_FORMAT_DATE    = new SimpleDateFormat("yyyy-MM-dd");
    public static final Calendar CAL = Calendar.getInstance();
    public static final int YEAR =  Calendar.YEAR;
    public static final int DATE =  Calendar.DATE;
    public static final int MONTH =  Calendar.MONTH;
    
    private TimeUtils() {
        throw new AssertionError();
       
    }
    
    /**
     * 把timeInMillis转化成"yyyy-MM-dd HH:mm:ss"格式的时间字符串返回
     * @param timeInMillis  毫秒级时间
     * @return
     */
    public static String getTimeString(long timeInMillis) {
        return getTimeString(timeInMillis, DEFAULT_DATE_FORMAT);
    }
    
    /**
     * 把date转化成"yyyy-MM-dd HH:mm:ss"格式的时间字符串返回
     * @param date  Date对象
     * @return
     */
    public static String getTimeString(Date date) {
    	return DEFAULT_DATE_FORMAT.format(date);
    }
    
    /**
     * 把timeInMillis转化成dateFormat格式的时间字符串返回
     * @param timeInMillis  毫秒级时间
     * @param dateFormat    
     * @return
     */
    public static String getTimeString(long timeInMillis, SimpleDateFormat dateFormat) {
        return dateFormat.format(new Date(timeInMillis));
    }
    
    /**
     * 把date转化成dateFormat格式的时间字符串返回
     * @param date  Date对象
     * @param dateFormat  格式对象
     * @return
     */
    public static String getTimeString(Date date, SimpleDateFormat dateFormat) {
    	return dateFormat.format(date);
    }
    
    /**
     * 把字符串按照"yyyy-MM-dd HH:mm:ss"格式 转化成时间Date对象返回
     * @param time  时间字符串
     * @return
     */
    public static Date getTimeDate(String time){
		Date date = null;
		try {
			date = DEFAULT_DATE_FORMAT.parse(time);
		} catch (Exception e) {
			//e.printStackTrace()
			try {
				date = DEFAULT_DATE_FORMAT.parse("0000-00-00 00:00:00");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return date;
    }
    
    /****
     * 把字符串按照指定格式 转化成时间Date对象返回
     * @param time  时间字符串
     * @param dateFormat  时间格式对象
     * @return
     */
    public static Date getTimeDate(String time, SimpleDateFormat dateFormat){
		Date date = null;
		try {
			date = dateFormat.parse(time);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				date = DEFAULT_DATE_FORMAT.parse("0000-00-00 00:00:00");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return date;
    }
    
    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static long getCurrentTimeInLong() {
        return System.currentTimeMillis();
    }

    /**
     * get current time in milliseconds, format is {@link #DEFAULT_DATE_FORMAT}
     * 
     * @return
     */
    public static String getCurrentTimeInString() {
        return getTimeString(getCurrentTimeInLong());
    }

    /**
     * get current time in milliseconds
     * 
     * @return
     */
    public static String getCurrentTimeInString(SimpleDateFormat dateFormat) {
        return getTimeString(getCurrentTimeInLong(), dateFormat);
    }
    
    /**
     * 增加或减少当前时间
     * @param date  需要修改的时间对象
     * @param timeNumer  修改的数量
     * @param CalendarFlag  使用Calendar的一些属相来设置
     * @return
     */
    public static Date add(Date date, int timeNumer, int CalendarFlag){
    	
    	CAL.setTime(date);
    	CAL.add(CalendarFlag, timeNumer);
    	
		return CAL.getTime();
    }
    
}





