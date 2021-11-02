package com.safframework.rxcache.memory;

import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;

import java.util.Set;

/**
 * @FileName: com.safframework.rxcache.memory.ChronicleMapImpl
 * @author: Tony Shen
 * @date: 2021/11/2 12:13 下午
 * @version: V1.0 <描述当前版本功能>
 */
public class ChronicleMapImpl extends AbstractMemoryImpl {

    public ChronicleMapImpl(long maxSize) {

        super(maxSize);
        this.cacheStatistics = new CacheStatistics((int)maxSize);
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {
        return null;
    }

    @Override
    public <T> void put(String key, T value) {

    }

    @Override
    public <T> void put(String key, T value, long expireTime) {

    }

    @Override
    public Set<String> keySet() {
        return null;
    }

    @Override
    public boolean containsKey(String key) {
        return false;
    }

    @Override
    public void evict(String key) {

    }

    @Override
    public void evictAll() {

    }

    @Override
    public CacheStatistics getCacheStatistics() {
        return null;
    }
}
