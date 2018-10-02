package com.safframework.rxcache.extra.memory;

import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public class GuavaCacheImpl extends AbstractMemoryImpl {

    private LoadingCache<String,Object > cache;

    public GuavaCacheImpl(long maxSize) {

        super(maxSize);
        cache = CacheBuilder
                .newBuilder()
                .maximumSize(maxSize)
                .build(new CacheLoader<String, Object>(){
                    @Override
                    public String load(String key) throws Exception {
                        return key;
                    }
                });
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
