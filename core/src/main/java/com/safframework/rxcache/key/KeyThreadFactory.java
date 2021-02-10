package com.safframework.rxcache.key;

import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @FileName: com.safframework.rxcache.key.KeyThreadFactory
 * @author: Tony Shen
 * @date: 2021-02-10 11:34
 * @version: V1.0 <描述当前版本功能>
 */
public class KeyThreadFactory implements ThreadFactory {

    private static final AtomicInteger poolNumber = new AtomicInteger(1);//原子类，线程池编号
    private final ThreadGroup group;//线程组
    private final AtomicInteger threadNumber = new AtomicInteger(1);//线程数目
    private final String namePrefix;//为每个创建的线程添加的前缀

    public KeyThreadFactory() {
        SecurityManager s = System.getSecurityManager();
        group = (s != null) ? s.getThreadGroup() :
                Thread.currentThread().getThreadGroup();//取得线程组
        namePrefix = "pool-" + poolNumber.getAndIncrement() +
                "-thread-";
    }

    public Thread newThread(Runnable r) {
        Thread t = new Thread(group, r,
                namePrefix + threadNumber.getAndIncrement(),
                0);//真正创建线程的地方，设置了线程的线程组及线程名
        t.setDaemon(true);
        t.setPriority(Thread.MIN_PRIORITY);
        return t;
    }
}
