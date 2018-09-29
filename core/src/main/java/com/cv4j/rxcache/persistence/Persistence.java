package com.cv4j.rxcache.persistence;

import com.cv4j.rxcache.domain.Record;

import java.lang.reflect.Type;
import java.util.List;

/**
 * Created by tony on 2018/9/28.
 */
public interface Persistence {

    <T> Record<T> retrieveRecord(String key,Type type);

    void saveRecord(String key, Record record);

    List<String> allKeys();

    boolean containsKey(String key);

    void evict(String key);

    void evictAll();
}
