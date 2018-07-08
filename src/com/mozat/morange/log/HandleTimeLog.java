package com.mozat.morange.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HandleTimeLog {
	private static final Logger handleTime = LoggerFactory.getLogger("handleTime");

    public static void info(int monetId,int action, long time) {
    	if(time > 400){
	        handleTime.info("[service]time="+time+",action=" + action +",monetId=" + monetId);
    	}
    }
}
