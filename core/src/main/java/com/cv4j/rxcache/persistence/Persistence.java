package com.cv4j.rxcache.persistence;

import com.cv4j.rxcache.domain.CacheHolder;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by tony on 2018/9/28.
 */
public interface Persistence {

    <T> CacheHolder<T> retrieve(String key, Type type);

    <T> void save(String key, T value);

    List<String> allKeys();

    boolean containsKey(String key);

    void evict(String key);

    void evictAll();
}
