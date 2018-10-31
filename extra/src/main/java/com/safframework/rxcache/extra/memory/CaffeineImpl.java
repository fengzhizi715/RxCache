package com.safframework.rxcache.extra.memory;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;

import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public class CaffeineImpl extends AbstractMemoryImpl {

    private Cache<String, Object> cache;

    public CaffeineImpl(long maxSize) {

        super(maxSize);
        cache = Caffeine.newBuilder()
                .maximumSize(maxSize)
                .build();
    }

    public CaffeineImpl(long maxSize, CacheConfig cacheConfig) {

        super(maxSize);
        Caffeine caffeine = Caffeine.newBuilder()
                .maximumSize(maxSize);

        if (cacheConfig!=null) {

            if (cacheConfig.expireDuration>0 && cacheConfig.expireTimeUnit!=null) {

                caffeine.expireAfterWrite(cacheConfig.expireDuration,cacheConfig.expireTimeUnit);
            }

            if (cacheConfig.refreshDuration>0 && cacheConfig.refreshTimeUnit!=null) {

                caffeine.refreshAfterWrite(cacheConfig.refreshDuration,cacheConfig.refreshTimeUnit);
            }
        }

        cache = caffeine.build();
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {

        T result = null;

        if (expireTimeMap.get(key)!=null) {

            if (expireTimeMap.get(key)<0) { // 缓存的数据从不过期

                result = (T) cache.getIfPresent(key);
            } else {

                if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    result = (T) cache.getIfPresent(key);
                } else {                     // 缓存的数据已经过期

                    evict(key);
                }
            }
        }
        
        return result != null ? new Record<>(Source.MEMORY,key, result, timestampMap.get(key),expireTimeMap.get(key)) : null;
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
    }

    @Override
    public Set<String> keySet() {

        return cache.asMap().keySet();
    }

    @Override
    public boolean containsKey(String key) {

        return cache.asMap().containsKey(key);
    }

    @Override
    public void evict(String key) {

        cache.invalidate(key);
        timestampMap.remove(key);
        expireTimeMap.remove(key);
    }

    @Override
    public void evictAll() {

        cache.invalidateAll();
        timestampMap.clear();
        expireTimeMap.clear();
    }
}
