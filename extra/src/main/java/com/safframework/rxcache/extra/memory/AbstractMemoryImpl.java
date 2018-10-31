package com.safframework.rxcache.extra.memory;

import com.safframework.rxcache.memory.Memory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tony on 2018/10/31.
 */
public abstract class AbstractMemoryImpl implements Memory {

    protected Map<String, Long> timestampMap;
    protected Map<String, Long> expireTimeMap;

    protected long maxSize;

    public AbstractMemoryImpl(long maxSize) {

        this.timestampMap = new ConcurrentHashMap<>();
        this.expireTimeMap = new ConcurrentHashMap<>();
        this.maxSize = maxSize;
    }
}
