package com.safframework.rxcache.memory;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;
import org.caffinitas.ohc.*;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @FileName: com.safframework.rxcache.memory.OHCImpl
 * @author: Tony Shen
 * @date: 2021/10/21 1:22 下午
 * @version: V1.0 <描述当前版本功能>
 */
public class OHCImpl extends AbstractMemoryImpl {

    private OHCache ohCache;
    private AtomicInteger putCount = new AtomicInteger();

    public OHCImpl(long maxSize) {

        super(maxSize);
        this.ohCache = OHCacheBuilder.<String, String>newBuilder()
                .keySerializer(new StringSerializer())
                .valueSerializer(new ObjectSerializer())
                .timeouts(true)
                .eviction(Eviction.LRU)
                .build();
        this.cacheStatistics = new CacheStatistics((int)maxSize);
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {
        T result = null;
        result = (T)ohCache.get(key);
        if (result == null) {
            timestampMap.remove(key);
            expireTimeMap.remove(key);
        }
        return result != null ? new Record<>(Source.MEMORY,key, result, timestampMap.get(key),expireTimeMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {
        put(key,value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {
        if (expireTime>0) {
            ohCache.put(key,value,System.currentTimeMillis() + expireTime);
        } else {
            ohCache.put(key,value);
        }

        timestampMap.put(key,System.currentTimeMillis());
        expireTimeMap.put(key,expireTime);
        putCount.incrementAndGet();
    }

    @Override
    public Set<String> keySet() {

        Set<String> keys = new HashSet<>();
        for (CloseableIterator<String> it = ohCache.keyIterator(); it.hasNext(); ) {
            keys.add(it.next());
        }
        return keys;
    }

    @Override
    public boolean containsKey(String key) {
        return ohCache.containsKey(key);
    }

    @Override
    public void evict(String key) {
        ohCache.remove(key);
        timestampMap.remove(key);
        expireTimeMap.remove(key);
    }

    @Override
    public void evictAll() {
        ohCache.clear();
        timestampMap.clear();
        expireTimeMap.clear();
    }

    @Override
    public CacheStatistics getCacheStatistics() {
        OHCacheStats cacheStats = ohCache.stats();

        cacheStatistics.setPutCount(putCount.get());
        cacheStatistics.setEvictionCount((int)cacheStats.getEvictionCount());
        cacheStatistics.setHitCount((int)cacheStats.getHitCount());
        cacheStatistics.setMissCount((int)cacheStats.getMissCount());

        return cacheStatistics;
    }
}
