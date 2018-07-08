package com.mozat.morange.util;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;

public class Statistics {

    private static final ScheduledExecutorService sexe;

    // history for last 60 seconds
    private final long[] serviceTime = new long[60];
    private final int[] serviceCount = new int[60];

    // last value set by the executor.
    private volatile int index = 0;

    private final AtomicInteger counter = new AtomicInteger(0);
    private final AtomicLong totalTime = new AtomicLong(0L);
    private final AtomicReference<ScheduledFuture<?>> rollupHandle = new AtomicReference<ScheduledFuture<?>>();

    static {
        sexe = Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory("stats-"));
    }

    public int getServiceRatePerMinute() {
        int tindex = index;

        if (tindex == 59) {
            return serviceCount[59] - serviceCount[0];
        } else {
            return serviceCount[tindex] - serviceCount[tindex + 1];
        }
    }

    public double getAvgServiceTimePerMinute() {
        int count;
        long time;
        int tindex = index;
        if (tindex == 59) {
            count = serviceCount[59] - serviceCount[0];
            time = serviceTime[59] - serviceTime[0];
        } else {
            count = serviceCount[tindex] - serviceCount[tindex + 1];
            time = serviceTime[tindex] - serviceTime[tindex + 1];
        }

        double r = (time / 1000000d) / count;
        if (count == 0)
            return 0;
        return Double.isNaN(r) ? -1 : r;
    }

    public void reportServiceTime(long nanoseconds) {
        // may have inconsistency, but we can tolerant.
        counter.incrementAndGet();
        totalTime.addAndGet(nanoseconds);
    }

    /**
     * This method should be called within a single thread, otherwise external
     * lock is needed.
     */
    public void rollup() {
        index = index == 59 ? 0 : index + 1;

        serviceTime[index] = totalTime.get();
        serviceCount[index] = counter.get();
    }

    public ScheduledFuture<?> register(ScheduledExecutorService texe) {
        return texe.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                rollup();
            }
        }, 1, 1, TimeUnit.SECONDS);
    }

    /**
     * Guaranteed to be registered only once.
     *
     * @return
     */
    public ScheduledFuture<?> register() {
        ScheduledFuture<?> ret = register(sexe);
        if (rollupHandle.compareAndSet(null, ret)) {
            return ret;
        } else {
            ret.cancel(false);
            return rollupHandle.get();
        }
    }

}
