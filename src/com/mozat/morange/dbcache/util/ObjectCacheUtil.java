package com.mozat.morange.dbcache.util;

import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;

public class ObjectCacheUtil<KT, VT>{
	private Ehcache _cache;
	
	ObjectCacheUtil(){}
	
	void setEhcache(Ehcache cache){
		this._cache = cache;
	}
	
	public void close(){
		if (this._cache != null){
		}
	}
	
	@SuppressWarnings("unchecked")
	public VT get(KT key){
		if (this._cache != null){
			Element found = this._cache.get(key);
			if (found != null){
				return (VT)found.getObjectValue();
			}
		}
		
		return null;
	}
	
	public void set(KT key, VT val){
		if (this._cache != null){
			this._cache.put(new Element(key, val));
		}
	}

	public void remove(KT key) {
		if (this._cache != null){
			this._cache.remove(key);
		}
	}
	
	public void removeAll() {
		if (this._cache != null){
			this._cache.removeAll();
		}
	}

	public boolean containsKey(KT key) {
		return this._cache != null && this._cache.isKeyInCache(key);
	}
	
	public String getName(){
		return "" + this.hashCode();
	}
}
