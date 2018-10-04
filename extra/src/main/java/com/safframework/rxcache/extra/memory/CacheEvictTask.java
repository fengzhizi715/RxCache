package com.safframework.rxcache.extra.memory;

import com.safframework.rxcache.memory.Memory;

import java.util.TimerTask;

/**
 * Created by tony on 2018/10/4.
 */
public class CacheEvictTask extends TimerTask {

    private Memory memory;

    private String key;

    public CacheEvictTask(Memory memory, String key) {
        this.memory = memory;
        this.key = key;
    }

    @Override
    public void run() {

        memory.evict(key);
    }
}
