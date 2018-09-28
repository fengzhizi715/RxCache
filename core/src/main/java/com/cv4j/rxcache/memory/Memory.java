package com.cv4j.rxcache.memory;

import com.cv4j.rxcache.domain.Record;

import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public interface Memory {

    <T> Record<T> getIfPresent(String key);

    <T> void put(String key, Record<T> record);

    Set<String> keySet();

    void evict(String key);

    void evictAll();
}
