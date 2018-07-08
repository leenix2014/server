package com.mozat.morange.service;

import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


/**
 * 
 * @author gaopeidian
 * 线程池管理
 */

public class ExecuteManager {
	public static int corePoolSize = 48;
	public static int maximumPoolSize = 48;
	
	public static long keepAliveTime = 3;
	public static int taskQueueSize = 10240;
	
	public static int scheduledCorePoolSize = 10;
	
	public static ThreadPoolExecutor executor;
	
	public static void init(){
		executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.SECONDS,
				new LinkedBlockingDeque(taskQueueSize));
	}
	
	public static void execute(Runnable task){
		executor.execute(task);
	}
	
	public static void stop(){
		if (executor != null) {
			executor.shutdown();
		}
	}
}
