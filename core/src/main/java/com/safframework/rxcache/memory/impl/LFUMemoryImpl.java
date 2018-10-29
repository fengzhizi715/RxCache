package com.safframework.rxcache.memory.impl;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.algorithm.lfu.LFUCache;
import com.safframework.rxcache.memory.algorithm.lfu.LFUCacheEntry;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by tony on 2018/10/22.
 */
public class LFUMemoryImpl extends AbstractMemoryImpl {

    private LFUCache<String, Object> cache;

    public LFUMemoryImpl(long maxSize) {

        super(maxSize);
        cache = new LFUCache<String, Object>((int) maxSize);
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {

        try {
            readLock.lock();

            T result = null;

            if (expireTimeMap.get(key) < 0) { // 缓存的数据从不过期

                result = (T) cache.get(key);
            } else {

                if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    result = (T) cache.get(key);
                } else {                     // 缓存的数据已经过期

                    evict(key);
                }
            }

            return result != null ? new Record<>(Source.MEMORY, key, result, timestampMap.get(key), expireTimeMap.get(key)) : null;

        } finally {

            readLock.unlock();
        }
    }

    @Override
    public <T> void put(String key, T value) {

        put(key, value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {

        try {
            writeLock.lock();

            cache.put(key, value);
            timestampMap.put(key, System.currentTimeMillis());
            expireTimeMap.put(key, expireTime);
            keys.add(key);

        } finally {

            writeLock.unlock();
        }
    }

    @Override
    public Set<String> keySet() {

        try {
            readLock.lock();

            return new HashSet<>(keys);
        } finally {

            readLock.unlock();
        }
    }

    @Override
    public boolean containsKey(String key) {

        try {
            readLock.lock();

            return cache.containsKey(key);
        } finally {

            readLock.unlock();
        }
    }

    @Override
    public void evict(String key) {

        try {
            writeLock.lock();

            cache.remove((LFUCacheEntry<String, Object>) cache.get(key));
            timestampMap.remove(key);
            expireTimeMap.remove(key);
            keys.remove(key);
        } finally {

            writeLock.unlock();
        }
    }

    @Override
    public void evictAll() {

        try {
            writeLock.lock();

            cache.clear();
            timestampMap.clear();
            expireTimeMap.clear();
            keys.clear();
        } finally {

            writeLock.unlock();
        }
    }
}
