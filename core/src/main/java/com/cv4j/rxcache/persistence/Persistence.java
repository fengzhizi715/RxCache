package com.cv4j.rxcache.persistence;

import com.cv4j.rxcache.domain.Record;

import java.util.List;

/**
 * Created by tony on 2018/9/28.
 */
public interface Persistence {

    <T> Record<T> retrieveRecord(String key);

    void saveRecord(String key, Record record);

    /**
     * Retrieve the keys from all records persisted
     */
    List<String> allKeys();

    /**
     * Delete the data associated with its particular key
     *
     * @param key The key associated with the object to be deleted from persistence
     */
    void evict(String key);

    /**
     * Delete all the data
     */
    void evictAll();
}
