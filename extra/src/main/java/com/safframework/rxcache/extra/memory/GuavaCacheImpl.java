package com.safframework.rxcache.extra.memory;

import com.google.common.base.Optional;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;

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

    public GuavaCacheImpl(long maxSize, CacheConfig cacheConfig) {

        super(maxSize);
        CacheBuilder cacheBuilder = CacheBuilder
                .newBuilder()
                .maximumSize(maxSize);

        if (cacheConfig!=null) {

            if (cacheConfig.expireDuration>0 && cacheConfig.expireTimeUnit!=null) {

                cacheBuilder.expireAfterWrite(cacheConfig.expireDuration,cacheConfig.expireTimeUnit);
            }

            if (cacheConfig.refreshDuration>0 && cacheConfig.refreshTimeUnit!=null) {

                cacheBuilder.refreshAfterWrite(cacheConfig.refreshDuration,cacheConfig.refreshTimeUnit);
            }
        }

        cache = cacheBuilder.build(new CacheLoader<String, Object>(){
                    @Override
                    public String load(String key) throws Exception {
                        return key;
                    }
                });
    }

    @Override
    public <T> CacheHolder<T> getIfPresent(String key) {

        Optional<T> optional = (Optional<T>) cache.getIfPresent(key);
        return optional != null ? new CacheHolder<>(optional.orNull(), timestampMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {

        cache.put(key,Optional.fromNullable(value));
        timestampMap.put(key,System.currentTimeMillis());
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {


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
