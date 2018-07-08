package com.mozat.morange.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtil {
	private static SimpleDateFormat dateFmt;
	private static SimpleDateFormat dateTimeFmt;
	static {
		dateFmt = new SimpleDateFormat("yyyy-MM-dd");
		dateTimeFmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	}
	
	public static String yyyyMMdd(Date dt){
		String res = dateFmt.format(dt);
		return res;
	}
	
	public static String yyyyMMddHHmmss(Date dt){
		String res = dateTimeFmt.format(dt);
		return res;
	}
	
	/**
	 * 获得date2比date1大多少天
	 */
	public static int getDifferDays(Date date1, Date date2){
		 Calendar cal1 = Calendar.getInstance();
	        cal1.setTime(date1);
	        
	        Calendar cal2 = Calendar.getInstance();
	        cal2.setTime(date2);
	       int day1= cal1.get(Calendar.DAY_OF_YEAR);
	        int day2 = cal2.get(Calendar.DAY_OF_YEAR);
	        
	        int year1 = cal1.get(Calendar.YEAR);
	        int year2 = cal2.get(Calendar.YEAR);
	        if(year1 != year2)   //不同年
	        {
	            int timeDistance = 0 ;
	            for(int i = year1 ; i < year2 ; i ++)
	            {
	                if(i%4==0 && i%100!=0 || i%400==0)    //闰年            
	                {
	                    timeDistance += 366;
	                }
	                else    //不是闰年
	                {
	                    timeDistance += 365;
	                }
	            }
	            
	            return timeDistance + (day2-day1) ;
	        }
	        else    //同一年
	        {
	            return day2-day1;
	        }
	}
}
