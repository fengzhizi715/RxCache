package com.cv4j.rxcache.extra.memory;

import com.cv4j.rxcache.domain.CacheHolder;
import com.cv4j.rxcache.memory.Memory;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public class GuavaCacheImpl implements Memory {

    private LoadingCache<String,Object > cache;
    private HashMap<String, Long> timestampMap;

    public GuavaCacheImpl(int maxSize) {

        cache = CacheBuilder
                .newBuilder()
                .build(new CacheLoader<String, Object>(){
                    @Override
                    public String load(String key) throws Exception {
                        return key;
                    }
                });
        timestampMap = new HashMap<>();
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
