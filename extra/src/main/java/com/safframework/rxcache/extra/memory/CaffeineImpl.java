package com.safframework.rxcache.extra.memory;

import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;
import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;

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

    @Override
    public <T> CacheHolder<T> getIfPresent(String key) {

        T result = (T) cache.getIfPresent(key);
        return result != null ? new CacheHolder<>(result, timestampMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {

        cache.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
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
    }

    @Override
    public void evictAll() {

        cache.invalidateAll();
    }
}
