package com.safframework.rxcache.memory.impl;

import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.memory.Memory;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by tony on 2018/9/29.
 */
public abstract class AbstractMemoryImpl implements Memory {

    protected Map<String, Long> timestampMap;
    protected Map<String, Long> expireTimeMap;
    protected CacheStatistics cacheStatistics;

    protected long maxSize;

    public AbstractMemoryImpl(long maxSize) {

        this.timestampMap = new HashMap<>();
        this.expireTimeMap = new HashMap<>();
        this.maxSize = maxSize;
    }
}
