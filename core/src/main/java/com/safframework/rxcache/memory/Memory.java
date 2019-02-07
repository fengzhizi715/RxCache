package com.safframework.rxcache.memory;

import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.domain.Record;

import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public interface Memory {

    <T> Record<T> getIfPresent(String key);

    <T> void put(String key, T value);

    <T> void put(String key, T value, long expireTime);

    Set<String> keySet();

    boolean containsKey(String key);

    void evict(String key);

    void evictAll();

    /**
     * 部分Memory的实现类还未实现这个方法，所以暂时用default方法，并默认为null
     * 等到它的实现类都实现了该方法，考虑去掉default
     * @return
     */
    default CacheStatistics getCacheStatistics() {

        return null;
    }
}
