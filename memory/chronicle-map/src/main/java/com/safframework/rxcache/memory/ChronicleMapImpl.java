package com.safframework.rxcache.memory;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;
import net.openhft.chronicle.map.ChronicleMap;
import net.openhft.chronicle.map.ChronicleMapBuilder;

import java.util.Set;

/**
 * @FileName: com.safframework.rxcache.memory.ChronicleMapImpl
 * @author: Tony Shen
 * @date: 2021/11/2 12:13 下午
 * @version: V1.0 <描述当前版本功能>
 */
public class ChronicleMapImpl extends AbstractMemoryImpl {

    private ChronicleMap<String, Object> cache;

    public ChronicleMapImpl(long maxSize) {
        super(maxSize);

        cache = ChronicleMapBuilder
                .of(String.class, Object.class)
                .name("rxcache-cm")
                .entries(maxSize)
                .averageKey("test")
                .averageValue("test")
                .create();

        this.cacheStatistics = new CacheStatistics((int)maxSize);
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

            cacheStatistics.incrementHitCount();
            return new Record<>(Source.MEMORY,key, result, timestampMap.get(key),expireTimeMap.get(key));
        } else {

            cacheStatistics.incrementMissCount();
            return null;
        }
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

        cacheStatistics.incrementPutCount();
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
        cacheStatistics.incrementEvictionCount();
    }

    @Override
    public void evictAll() {
        cache.clear();
        timestampMap.clear();
        expireTimeMap.clear();
        cacheStatistics.incrementEvictionCount(keySet().size());
    }

    @Override
    public CacheStatistics getCacheStatistics() {
        return cacheStatistics;
    }

    public void close() {
        cache.close();
    }
}
