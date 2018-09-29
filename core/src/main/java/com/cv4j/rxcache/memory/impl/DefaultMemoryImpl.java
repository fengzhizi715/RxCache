package com.cv4j.rxcache.memory.impl;

import com.cv4j.rxcache.domain.Record;
import com.cv4j.rxcache.memory.Memory;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tony on 2018/9/29.
 */
public class DefaultMemoryImpl implements Memory {

    private ConcurrentHashMap<String,Record> cache = new ConcurrentHashMap<String,Record>();

    @Override
    public <T> Record<T> getIfPresent(String key) {
        return cache.get(key);
    }

    @Override
    public <T> void put(String key, Record<T> record) {

        cache.put(key,record);
    }

    @Override
    public Set<String> keySet() {

        return cache.keySet();
    }

    @Override
    public boolean containsKey(String key) {

        return cache.containsKey(key);
    }

    @Override
    public void evict(String key) {

        cache.remove(key);
    }

    @Override
    public void evictAll() {

        cache.clear();
    }
}
