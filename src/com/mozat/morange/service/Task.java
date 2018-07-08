package com.mozat.morange.service;

import java.util.LinkedList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mozat.morange.log.DebugLog;

import game.packet.Packet;

public class Task implements Runnable {
    private static Logger logger = LoggerFactory.getLogger(Task.class);

    private final LinkedList<Packet> pktList = new LinkedList<Packet>();
    private final long userId;
    private boolean isStopped;
    private long startTime;

    private static final long deadThreshold = 10000; // 10s

    public long getUserId() {
        return userId;
    }

    public boolean addPacket(Packet ptk) {
        synchronized (this.pktList) {
            if (isStopped) {
                return false;
            }

            /* 当前packet处理超时,不再重用这个线程 */
            if (startTime > 0 && (System.currentTimeMillis() - startTime) > deadThreshold) {
                DebugLog.info("thread is being hang, discard all packets, userId=" + userId + ",pktSize=" + pktList.size());
                pktList.clear();
                return false;
            }

            return pktList.offer(ptk);
        }
    }

    public Task(Packet p) {
        isStopped = false;
        startTime = 0;
        userId = p.userId;
        pktList.offer(p);
    }

    @Override
	public void run() {
        do {
            Packet currentPkt;
            // atomically get a packet
            synchronized (this.pktList) {
                if (pktList.isEmpty()) {
                    isStopped = true;
                    return;
                }

                currentPkt = pktList.poll();
                startTime = System.currentTimeMillis();
            }

            try {
                if (currentPkt != null) {
                	currentPkt.execPacket();
                }
            } catch (Throwable e) {
                logger.error("task error:", e);
                //MonetService.STATS_REQ_ERR.reportServiceTime(1L);
            }
        } while (true);
    }
}
