package com.mozat.morange.dbcache.core;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import com.mozat.morange.log.DebugLog;

public class MemoryMonitor {
    public static int getObjectSize(Serializable ser) {
    	if (null == ser){
    		return 0;
    	}
    	
    	try{
	    	ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    	
	    	ObjectOutputStream oos = new ObjectOutputStream(baos);
	    	oos.writeObject(ser);
	    	oos.close();
	    	
	    	return baos.size();
    	}
    	catch (IOException ex){
    		DebugLog.info("[MemoryMonitor.getObjectSize] Ex: " + ex);
    		
    		return 0;
    	}
    }
}
