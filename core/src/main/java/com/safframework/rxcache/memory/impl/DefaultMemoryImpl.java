package com.safframework.rxcache.memory.impl;

import com.safframework.rxcache.domain.CacheHolder;

import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tony on 2018/9/29.
 */
public class DefaultMemoryImpl extends AbstractMemoryImpl {

    private ConcurrentHashMap<String,Object> cache;

    public DefaultMemoryImpl(long maxSize) {

        super(maxSize);
        cache = new ConcurrentHashMap<String,Object>((int)maxSize);
    }

    @Override
    public <T> CacheHolder<T> getIfPresent(String key) {

        T result = (T) cache.get(key);
        return result != null ? new CacheHolder<>(result, timestampMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {

        cache.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
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
