package com.mozat.morange.game;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

public class Lock {

    private static final Map<Integer, Object> userLocks = new ConcurrentHashMap<Integer, Object>();
    //    private static final Map<Integer, Object> cityLocks = new ConcurrentHashMap<Integer, Object>();
    private static final Map<String, Object> worldCellLocks = new ConcurrentHashMap<String, Object>();
    //    private static final Map<Integer, Object> allianceLocks = new ConcurrentHashMap<Integer, Object>();
    private static final ConcurrentHashMap<Integer, ReentrantLock> allianceLocks = new ConcurrentHashMap<Integer, ReentrantLock>();

    public static synchronized Object getLock(int monetId) {
        if (userLocks.containsKey(monetId))
            return userLocks.get(monetId);
        Object obj = new Object();
        userLocks.put(monetId, obj);
        return obj;
    }

//    public static synchronized Object getCityLock(int cityId) {
//        if (cityLocks.containsKey(cityId))
//            return cityLocks.get(cityId);
//        Object obj = new Object();
//        cityLocks.put(cityId, obj);
//        return obj;
//    }

    public static synchronized Object getWorldCellLock(String xy) {
        if (worldCellLocks.containsKey(xy))
            return worldCellLocks.get(xy);
        Object obj = new Object();
        worldCellLocks.put(xy, obj);
        return obj;
    }

//    public static synchronized Object getAllianceLock(int allianceId) {
//        if (allianceLocks.containsKey(allianceId))
//            return allianceLocks.get(allianceId);
//        Object obj = new Object();
//        allianceLocks.put(allianceId, obj);
//        return obj;
//    }

    public static ReentrantLock getAllianceLock(int allianceId) {
        ReentrantLock lock = allianceLocks.get(allianceId);
        if (lock != null) {
            return lock;
        }
        lock = new ReentrantLock();
        ReentrantLock oldLock = allianceLocks.putIfAbsent(allianceId, lock);
        return oldLock == null ? lock : oldLock;
    }
}
