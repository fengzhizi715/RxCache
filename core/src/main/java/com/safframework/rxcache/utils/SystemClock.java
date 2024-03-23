package com.safframework.rxcache.utils;

import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @FileName: com.safframework.rxcache.utils.SystemClock
 * @author: Tony Shen
 * @date: 2024/3/23 21:10
 * @version: V1.0 <描述当前版本功能>
 */
public class SystemClock {
    private final int period;

    private final AtomicLong now;

    private static final String THREAD_NAME ="system.clock";

    private static class InstanceHolder {
        private static final SystemClock INSTANCE = new SystemClock(1);
    }

    private SystemClock(int period) {
        this.period = period;
        this.now = new AtomicLong(System.currentTimeMillis());
        scheduleClockUpdating();
    }

    private static SystemClock instance() {
        return InstanceHolder.INSTANCE;
    }

    private void scheduleClockUpdating() {
        ScheduledThreadPoolExecutor scheduler = new ScheduledThreadPoolExecutor(1, r -> {
            Thread thread = new Thread(r, THREAD_NAME);
            thread.setDaemon(true);
            return thread;
        });
        scheduler.scheduleAtFixedRate(() -> now.set(System.currentTimeMillis()), period, period, TimeUnit.MILLISECONDS);
    }

    private long currentTimeMillis() {
        return now.get();
    }

    /**
     * 用来替换原来的System.currentTimeMillis()
     */
    public static long now() {
        return instance().currentTimeMillis();
    }
}
