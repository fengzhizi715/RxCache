package com.safframework.rxcache.memory.impl;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tony on 2018/9/29.
 */
public class FIFOMemoryImpl extends AbstractMemoryImpl {

    private Map<String,Object> cache;

    public FIFOMemoryImpl(long maxSize) {

        super(maxSize);
        cache = new ConcurrentHashMap<String,Object>((int)maxSize);
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

        try {

            lock.lock();

            if (keySet().size()<maxSize) { // 缓存还有空间

                saveValue(key,value,expireTime);
            } else {                       // 缓存空间不足，需要删除一个

                if (containsKey(key)) {

                    keys.remove(key);

                    saveValue(key,value,expireTime);
                } else {

                    String oldKey = keys.get(0); // 最早缓存的key
                    evict(oldKey);               // 删除最早缓存的数据 FIFO算法

                    saveValue(key,value,expireTime);
                }
            }

        } finally {

            lock.unlock();
        }
    }

    private <T> void saveValue(String key, T value, long expireTime) {

        cache.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
        expireTimeMap.put(key,expireTime);
        keys.add(key);
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
