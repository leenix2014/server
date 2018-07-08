package com.mozat.morange.game;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TimeZone;

import javax.net.SocketFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.dbcache.SQLServerCacheManager;
import com.mozat.morange.util.ICacheUtil;


public class Global {
    private static final Logger logger = LoggerFactory.getLogger("sys");
    private static TimeZone ClientTimeZone;
    private static TimeZone SystemTimeZone;
    private static int serverId;
    private static String billingCallBackUrl;
    private static boolean initCastle = false;
    private static boolean initMagic = false;
    private static String user = "";
    private static String pwd = "";
    private static int version = 0;
    private static int clientMajor = 0;
    private static int clientMinor = 0;
    private static int clientBuild = 0;
    private static int dbPoolSize = 200;
    private static SocketFactory socketFactory = null;
    private static InetAddress serverAddr = null;
    private static int serverPort = 0;
    private static ICacheUtil cache;
    private static String gcmAndroidApiKey;
    private static int wishSpringStatus;//是否开放许愿池,配置在system.properties�表示不开�表示仅在ios开�表示仅在android开�表示都开�

    public static void initMSSQLDBConn() throws Exception {
        SQLServerCacheManager userSqlDBCacheManager = SQLServerCacheManager.create("config_deploy/user_db_cache_config.xml");
        if (!userSqlDBCacheManager.init()) {
            throw new Exception("init sql server cache manager failed");
        }
    }

    public static void shutdownSQLServerCacheManager(){
    	SQLServerCacheManager.shutdown();
    }
    
    public static ICacheUtil getCache() {
        return cache;
    }

    public static String getUser() {
        return user;
    }

    public static String getPwd() {
        return pwd;
    }

    public static int getVersion() {
        return version;
    }

    public static int getClientMajor() {
        return clientMajor;
    }

    public static int getClientMinor() {
        return clientMinor;
    }

    public static int getClientBuild() {
        return clientBuild;
    }

    public static SocketFactory getSocketFactory() {
        return socketFactory;
    }

    public static InetAddress getServerAddr() {
        return serverAddr;
    }

    public static int getServerPort() {
        return serverPort;
    }

    public static boolean getInitCastle() {
        return initCastle;
    }

    public static boolean getInitMagic() {
        return initMagic;
    }

    public static int getServerId() {
        return serverId;
    }

    public static String getBillingCallBackUrl() {
        return billingCallBackUrl;
    }

    public static int getDbPoolSize() {
        return dbPoolSize;
    }

    public static String getGcmAndroidApiKey() {
        return gcmAndroidApiKey;
    }

    public static int getWishSpringStatus() {
        return wishSpringStatus;
    }

    /*
        Get offset shifted by day light saving time
     */
    public static int getUtcOffset() {
        return ClientTimeZone.getOffset(System.currentTimeMillis());
    }

    public static Calendar getClientMidnight() {
        Calendar clientCalendar = Calendar.getInstance(ClientTimeZone);
        clientCalendar.set(Calendar.HOUR_OF_DAY, 0);
        clientCalendar.set(Calendar.MINUTE, 0);
        clientCalendar.set(Calendar.SECOND, 0);
        clientCalendar.set(Calendar.MILLISECOND, 0);
        return clientCalendar;
    }

    public static boolean isSameDayParseToSystemTime(Date d1, Date d2) {
        d1 = parse2SystemTime(d1);
        d2 = parse2SystemTime(d2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
            return false;
        if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
            return false;
        if (c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH))
            return false;
        return true;
    }

    public static boolean isSameDayParseToClientTime(Date d1, Date d2) {
        d1 = parse2LocaleTime(d1);
        d2 = parse2LocaleTime(d2);
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();
        c1.setTime(d1);
        c2.setTime(d2);
        if (c1.get(Calendar.YEAR) != c2.get(Calendar.YEAR))
            return false;
        if (c1.get(Calendar.MONTH) != c2.get(Calendar.MONTH))
            return false;
        if (c1.get(Calendar.DAY_OF_MONTH) != c2.get(Calendar.DAY_OF_MONTH))
            return false;
        return true;
    }

    //返回d1的天数比是否比d2大指定的天数，参数differDays为指定天数，为正则是大几天，为负则是小几�
    public static boolean isDifferDaysParseToClientTime(Date d1, Date d2, int differDays) {
        // reviewed: 业务相关的逻辑
        long oneDayTimeCurSecond = 1000L * 60 * 60 * 24;
        long time = d1.getTime() - oneDayTimeCurSecond * differDays;
        Date d = new Date(time);
        return isSameDayParseToClientTime(d, d2);
    }

    public static Date getNow() {
        return parse2LocaleTime(new Date());
    }

    public static int getTimeOffset() {
        return (SystemTimeZone.getRawOffset() - ClientTimeZone.getRawOffset()) / (60 * 60 * 1000);
    }

    public static Date parse2LocaleTime(Date date) {
        return new Date(date.getTime() - (SystemTimeZone.getRawOffset() - ClientTimeZone.getRawOffset()));
    }

    public static Date parse2SystemTime(Date date) {
        return new Date(date.getTime() + (SystemTimeZone.getRawOffset() - ClientTimeZone.getRawOffset()));
    }

    public static Calendar getLocaleCalendar() {
        return Calendar.getInstance(ClientTimeZone);
    }

    public static void resetCalendarToMidnight(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    private static Map<String, Map<String, ResourceBundle>> languageSupported;


    public static String getText(String key, String lang) {
        if (lang == null || "".equals(lang))
            lang = "ar";
        Map<String, ResourceBundle> map = languageSupported.get(lang);
        Map<String, ResourceBundle> enMap = languageSupported.get("en");
        if (key == null || key.isEmpty()) {
            return "";
        }

        try {
            key = key.trim();
            if (key.startsWith("msg.")) {
                if (map.get("msg").containsKey(key))
                    return map.get("msg").getString(key);
                else
                    return enMap.get("msg").getString(key);
            } else {
                logger.error("can't find key");
                return "";
            }
        } catch (Exception e) {
            logger.error("can't find key=" + key + "\n" + e);
            return key;
        }
    }

    public static String getTextWithParameter(String key, Object[] parameter, String lang) {
        String msg = getText(key, lang);
        int size = parameter.length;
        if (size == 0) {
            return msg;
        }
        for (int i = 0; i < size; i++) {
            msg = msg.replace("{" + i + "}", String.valueOf(parameter[i]));
        }
        return msg;
    }

    public static List<Integer[]> proccessStr(String str) {
        List<Integer[]> list = new ArrayList<Integer[]>();
        String[] arr = str.split("\\|");
        for (String string : arr) {
            String[] costTypeArr = string.split(":");
            int size = costTypeArr.length;
            if (size > 1) {
                Integer[] integers = new Integer[size];
                integers[0] = Integer.valueOf(costTypeArr[0]);
                integers[1] = Integer.valueOf(costTypeArr[1]);
                list.add(integers);
            }
        }
        return list;
    }
}
