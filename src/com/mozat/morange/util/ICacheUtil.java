package com.mozat.morange.util;

public interface ICacheUtil {
	
	public void remove(String key);
	
	public boolean add(String key, Object value);
	
	public boolean set(String key, Object value);
	
//	public Object get(String key);
	
	public Object getObjectValue(String key);
}
