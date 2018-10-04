package com.safframework.rxcache.memory;

import com.safframework.rxcache.domain.CacheHolder;

import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public interface Memory {

    <T> CacheHolder<T> getIfPresent(String key);

    <T> void put(String key, T value);

    <T> void put(String key, T value, long expireTime);

    Set<String> keySet();

    boolean containsKey(String key);

    void evict(String key);

    void evictAll();
}
