package com.cv4j.rxcache.memory.impl;

import com.cv4j.rxcache.memory.Memory;

import java.util.HashMap;

/**
 * Created by tony on 2018/9/29.
 */
public abstract class AbstractMemoryImpl implements Memory {

    protected HashMap<String, Long> timestampMap;
    protected long maxSize;

    public AbstractMemoryImpl(long maxSize) {

        timestampMap = new HashMap<>();
        this.maxSize = maxSize;
    }
}
