package com.mozat.morange.util;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

public class EhcacheUtil implements ICacheUtil{
	private static CacheManager manager;
	static {
		manager = CacheManager.create("config_runtime/ehcache.xml");
	}
	
	public static CacheManager getManger() {
		return manager;
	}
	public EhcacheUtil(Cache cache) {
		ehCache = cache;
	}
	public void clear(){
		ehCache.removeAll();
	}
	private Cache ehCache;
	//static final Logger logger = Logger.getLogger(EhcacheUtil.class);
	@Override
	public void remove(String key) {
		//long t = System.currentTimeMillis();
		ehCache.remove(key);
		//logger.info("[EhcacheUtil] remove cache , key = " + key+",time = " + (System.currentTimeMillis() - t));
	}

	@Override
	public boolean add(String key, Object value) {
		//long t = System.currentTimeMillis();
		Element el = new Element(key, value);
		ehCache.put(el);
		//logger.info("[EhcacheUtil] add cache , key = " + key + " , value = " + el.getObjectValue()
			//	+",time = " + (System.currentTimeMillis() - t));
		return true;
	}

	@Override
	public boolean set(String key, Object value) {
		//long t = System.currentTimeMillis();
		Element el = new Element(key, value);
		ehCache.put(el);
//		logger.info("[EhcacheUtil] set cache , key = " + key + " , value = " + el.getObjectValue()
//				+",time = " + (System.currentTimeMillis() - t));
		return true;
	}

//	@Override
//	public Object get(String key) {
//		//long t = System.currentTimeMillis();
//		//logger.info("[EhcacheUtil] get cache , key = " + key+",time = " + (System.currentTimeMillis() - t));
//		return ehCache.get(key);
//	}

	@Override
	public Object getObjectValue(String key) {
		//long t = System.currentTimeMillis();
//		if(ehCache.isKeyInCache(key)){
			//logger.info("[EhcacheUtil] getObjectValue cache , key = " + key+",time = " + (System.currentTimeMillis() - t));
			if(ehCache.get(key) != null){
				return ehCache.get(key).getObjectValue();
			}
			return null;
//		}else{
//			logger.debug("[EhcacheUtil] getObjectValue cache , invalid key in cache,time = " + (System.currentTimeMillis() - t));
//		}
//		return null;
	}
	
	
}
