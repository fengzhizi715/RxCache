package com.cv4j.rxcache.memory;

import com.cv4j.rxcache.domain.CacheHolder;

import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public interface Memory {

    <T> CacheHolder<T> getIfPresent(String key);

    <T> void put(String key, T value);

    Set<String> keySet();

    boolean containsKey(String key);

    void evict(String key);

    void evictAll();
}
