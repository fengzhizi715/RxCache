package com.safframework.rxcache.memory.impl;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;

import java.util.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tony on 2018/10/22.
 */
public class LRUMemoryImpl extends AbstractMemoryImpl {

    private LRUCache<String,Object> cache;
    private Lock lock = new ReentrantLock();
    private List<String> keys;

    public LRUMemoryImpl(long maxSize) {

        super(maxSize);
        cache = new LRUCache<String,Object>((int)maxSize);
        keys = new ArrayList<>();
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {

        try {
            lock.lock();

            T result = null;

            if (expireTimeMap.get(key)<0) { // 缓存的数据从不过期

                result = (T) cache.get(key);
            } else {

                if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    result = (T) cache.get(key);
                } else {                     // 缓存的数据已经过期

                    evict(key);
                }
            }

            return result != null ? new Record<>(Source.MEMORY,key, result, timestampMap.get(key),expireTimeMap.get(key)) : null;

        } finally {

            lock.unlock();
        }
    }

    @Override
    public <T> void put(String key, T value) {

        put(key,value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {

        cache.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
        expireTimeMap.put(key,expireTime);
        keys.add(key);
    }

    @Override
    public Set<String> keySet() {

        return new HashSet<>(keys);
    }

    @Override
    public boolean containsKey(String key) {

        return cache.containsKey(key);
    }

    @Override
    public void evict(String key) {

        cache.remove(key);
        timestampMap.remove(key);
        expireTimeMap.remove(key);
        keys.remove(key);
    }

    @Override
    public void evictAll() {

        cache.clear();
        timestampMap.clear();
        expireTimeMap.clear();
        keys.clear();
    }
}
