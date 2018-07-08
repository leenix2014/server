package com.mozat.morange.dbcache.util;

import com.mozat.morange.util.EhcacheUtil;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.store.MemoryStoreEvictionPolicy;

public class ObjectCacheUtilManager {
	private static CacheManager _ehCacheManager;
	private static final int MaxElementInMemory = 100000;
	private static final int AvailableSeconds = 5 * 60;
	
	public static <KT, VT> ObjectCacheUtil<KT, VT> create(){
		return ObjectCacheUtilManager.create(0, false);
	}
	
	public static <KT, VT> ObjectCacheUtil<KT, VT> createWithTimeToIdleSeconds(int seconds){
		return create(seconds, true);
	}
	
	public static <KT, VT> ObjectCacheUtil<KT, VT> createWithTimeToLiveSeconds(int seconds){
		return create(seconds, false);
	}
	
	private static <KT, VT> ObjectCacheUtil<KT, VT> create(int timeInSeconds, boolean isTimeToIdle){
		if (null == _ehCacheManager){
			_ehCacheManager = EhcacheUtil.getManger();
		}
		
		if (timeInSeconds <= 0){
			timeInSeconds = AvailableSeconds;
		}
		
		ObjectCacheUtil<KT, VT> cacheUtil = new ObjectCacheUtil<KT, VT>();
		
		CacheConfiguration ehCacheConfig = new CacheConfiguration(cacheUtil.getName(), MaxElementInMemory);
		ehCacheConfig.eternal(false);
		if (isTimeToIdle){
			ehCacheConfig.timeToIdleSeconds(timeInSeconds);
		}
		else{
			ehCacheConfig.timeToLiveSeconds(timeInSeconds);
		}
		ehCacheConfig.memoryStoreEvictionPolicy(MemoryStoreEvictionPolicy.LFU);
		
		Cache ehCache = new Cache(ehCacheConfig);
		_ehCacheManager.addCache(ehCache);
		
		cacheUtil.setEhcache(ehCache);
		
		return cacheUtil;
	}
}
