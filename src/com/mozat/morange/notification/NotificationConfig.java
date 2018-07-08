package com.mozat.morange.notification;

import java.util.Map;

/**
 * Created by zhaochonghui on 2015/6/11.
 */
public class NotificationConfig {
    private int interval;
    private long minAfterLogout;
    private long maxAfterLogout;
    private int minGovLevel;
    private int maxGovLevel;
    private int type;

    public long getMinAfterLogout() {
        return minAfterLogout;
    }

    public long getMaxAfterLogout() {
        return maxAfterLogout;
    }

    public int getInterval() {
        return interval;
    }

    private static final String cacheKey = "NotificationConfig";
    private static Map<Integer, NotificationConfig> _cacheMap;

    
    public static Map<Integer, NotificationConfig> loadCache() {
        return _cacheMap;
    }

    /*
    private static void setCache(Map<Integer, NotificationConfig> configMap) {
        CacheUtil.configCache.set(cacheKey, configMap);
    }
    */

    public static void init() throws Exception {
    	/*
        DBResultSet rs = Global.modb.execSQLQuery("SELECT * FROM NotificationConfig", null);
        if (rs == null) {
            throw new Exception("[NotificationConfig]init, init config failed, result set is null");
        }
        Map<Integer, NotificationConfig> configMap = new HashMap<Integer, NotificationConfig>();
        while (rs.next()) {
            NotificationConfig notificationConfig = new NotificationConfig();
            notificationConfig.type = rs.getInt("type");
            notificationConfig.interval = rs.getInt("interval") * 60 * 1000;
            notificationConfig.minAfterLogout = rs.getInt("minAfterLogout") * 60 * 1000;
            notificationConfig.maxAfterLogout = rs.getInt("maxAfterLogout") * 60 * 1000;
            notificationConfig.minGovLevel = rs.getInt("minGovLevel");
            notificationConfig.maxGovLevel = rs.getInt("maxGovLevel");
            configMap.put(notificationConfig.type, notificationConfig);
        }
        setCache(configMap);
        */
    	
//    	LNotificationConfig[] configs = LNotificationConfig.loadConfig(null);
//    	_cacheMap = new HashMap<Integer, NotificationConfig>();
//    	
//        for (LNotificationConfig cnf: configs){
//            NotificationConfig notificationConfig = new NotificationConfig();
//            notificationConfig.type = cnf.type;
//            notificationConfig.interval = cnf.interval * 60 * 1000;
//            notificationConfig.minAfterLogout = cnf.minAfterLogout * 60 * 1000;
//            notificationConfig.maxAfterLogout = cnf.maxAfterLogout * 60 * 1000;
//            notificationConfig.minGovLevel = cnf.minGovLevel;
//            notificationConfig.maxGovLevel = cnf.maxGovLevel;
//            
//            _cacheMap.put(notificationConfig.type, notificationConfig);
//        }
    }

    public int getMinGovLv() {
        return minGovLevel;
    }

    public int getMaxGovLv() {
        return maxGovLevel;
    }
}
