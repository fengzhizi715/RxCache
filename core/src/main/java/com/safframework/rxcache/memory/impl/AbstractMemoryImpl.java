package com.safframework.rxcache.memory.impl;

import com.safframework.rxcache.memory.Memory;

import java.util.HashMap;
import java.util.List;

/**
 * Created by tony on 2018/9/29.
 */
public abstract class AbstractMemoryImpl implements Memory {

    protected HashMap<String, Long> timestampMap;
    protected HashMap<String, Long> expireTimeMap;
    protected List<String> keys;
    protected long maxSize;

    public AbstractMemoryImpl(long maxSize) {

        this.timestampMap = new HashMap<>();
        this.expireTimeMap = new HashMap<>();
        this.maxSize = maxSize;
    }
}
