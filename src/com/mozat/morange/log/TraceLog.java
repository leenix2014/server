package com.mozat.morange.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TraceLog {
    private static final Logger traceLog = LoggerFactory.getLogger("trace");

    public static void info(String msg) {
        traceLog.info(msg);
    }

    public static void info(int monetId,String type,String msg) {
        traceLog.info("monetId="+monetId+",Type="+type+","+msg);
    }
}
