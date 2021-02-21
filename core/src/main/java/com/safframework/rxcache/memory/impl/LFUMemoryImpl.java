package com.safframework.rxcache.memory.impl;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.algorithm.lfu.LFUCache;

import java.util.Set;

/**
 * Created by tony on 2018/10/22.
 */
public class LFUMemoryImpl extends AbstractMemoryImpl {

    private LFUCache<String, Object> cache;

    public LFUMemoryImpl() {

        this(Constant.DEFAULT_CACHE_SIZE);
    }

    public LFUMemoryImpl(long maxSize) {

        super(maxSize);
        cache = new LFUCache<String, Object>((int) maxSize,new CacheStatistics((int)maxSize));
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {

        T result = null;

        if (expireTimeMap.get(key)!=null) {

            if (expireTimeMap.get(key) == Constant.NEVER_EXPIRE) { // 缓存的数据从不过期

                result = (T) cache.get(key);
            } else {

                if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    result = (T) cache.get(key);
                } else {                     // 缓存的数据已经过期

                    evict(key);
                }
            }
        }

        if (result!=null) {

            return new Record<>(Source.MEMORY,key, result, timestampMap.get(key),expireTimeMap.get(key));
        } else {

            getCacheStatistics().incrementMissCount();
            return null;
        }
    }

    @Override
    public <T> void put(String key, T value) {

        put(key, value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {

        cache.put(key, value);
        timestampMap.put(key, System.currentTimeMillis());
        expireTimeMap.put(key, expireTime);
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
        timestampMap.remove(key);
        expireTimeMap.remove(key);
    }

    @Override
    public void evictAll() {

        cache.clear();
        timestampMap.clear();
        expireTimeMap.clear();
    }

    public CacheStatistics getCacheStatistics() {

        return cache.getCacheStatistics();
    }
}
