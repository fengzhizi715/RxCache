package com.safframework.rxcache.persistence;

import com.safframework.rxcache.domain.Record;

import java.lang.reflect.Type;
import java.util.List;
import java.util.Set;

/**
 * Created by tony on 2018/9/28.
 */
public interface Persistence {

    <T> Record<T> retrieve(String key, Type type);

    String getStringData(String key);

    <T> void save(String key, T value);

    <T> void save(String key, T value, long expireTime);

//    List<String> allKeys();
    Set<String> keySet();

    boolean containsKey(String key);

    void evict(String key);

    void evictAll();
}
