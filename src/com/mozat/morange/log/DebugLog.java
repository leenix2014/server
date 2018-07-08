package com.mozat.morange.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DebugLog {
    private static final Logger debug = LoggerFactory.getLogger("debug");

    public static void info(String msg) {
        debug.info(msg);
    }
}
