package com.mozat.morange.service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import game.packet.Packet;
import game.session.GameSession;

public class MonetService2 { 
	private static Logger logger = LoggerFactory.getLogger(MonetService.class);  
    private ThreadPoolExecutor pool;

    private int workerThreadMin = 50;
    private int workerThreadMax = 50;
    private int workerKeepAlive = 60;
    
    private final Map<Long, Task> playerTaskMap = new HashMap<Long, Task>();

    private Task getTask(long userId, Packet pkt) {
        Task t = playerTaskMap.get(userId);
        if (null == t) {
            t = new Task(pkt);
            playerTaskMap.put(userId, t);
            return t;
        } else {
            /* 成功的话仍在原线程处理请求 */
            if (t.addPacket(pkt)) {
                return null;
            }
            t = new Task(pkt);
            playerTaskMap.put(userId, t);
            return t;
        }
    }
 
    public void start() throws Exception {
        init();
    }

    private void init() throws Exception {
        BlockingQueue<Runnable> in_task = new SynchronousQueue<Runnable>();
        pool = new ThreadPoolExecutor(workerThreadMin, Integer.MAX_VALUE, workerKeepAlive, TimeUnit.SECONDS, in_task, new ThreadPoolExecutor.DiscardOldestPolicy());
    }

    public void stop() {
        if (pool != null) {
            try {
                pool.shutdown();
                for (int i = 0; i < 10; i++) {
                    if (!pool.isTerminated()) {
                        try {
                            Thread.sleep(3000);
                        } catch (InterruptedException ignored) {
                        }
                    } else {
                        logger.info("shut down pool success.");
                        break;
                    }
                }
            } catch (Exception e) {
                logger.error("shut down pool failed:", e);
            } finally {
                if (!pool.isTerminated())
                    logger.error("shut down pool failed");
                pool = null;
            }
        }
    }

    public void dispatch(Packet packet){
    	GameSession session = packet.getSession();
    	if (session.getUserId() == 0) {
			execByThread(packet);
		}else{
			packet.userId = session.getUserId();
			execByPlayer(packet);
		}
    }
    
    private void execByThread(final Packet packet){
    	ExecuteManager.execute(new Runnable() {			
			@Override
			public void run() {
				try {
					packet.execPacket();
				} catch (Exception e) {
					logger.error("", e);
				}
			}
		});
    }
    
    private boolean execByPlayer(final Packet packet){
    	Task task = getTask(packet.userId, packet);
        if (task == null) {
            return false;
        }else{
        	pool.execute(task);
        	return true;
        }
    }
    
    int getActiveThreadCount() {
        return pool.getActiveCount();
    }

    int getInitThreadCount() {
        return workerThreadMin;
    }

    int getMaxThreadCount() {
        return workerThreadMax;
    }

    int getCurrentThreadCount() {
        return pool.getPoolSize();
    }

    public int getReqQueueSize() {
        return pool.getQueue().size();
    }

    public int getActiveReqCount() {
        return pool.getActiveCount();
    }
}
