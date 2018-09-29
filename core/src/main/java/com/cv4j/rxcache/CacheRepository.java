package com.cv4j.rxcache;

import com.cv4j.rxcache.domain.Record;
import com.cv4j.rxcache.memory.Memory;
import com.cv4j.rxcache.persistence.Persistence;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
class CacheRepository {

    private Memory memory;
    private Persistence persistence;

    CacheRepository(Memory memory, Persistence persistence) {
        this.memory = memory;
        this.persistence = persistence;
    }

    <T> Record<T> get(String key,Type type) {

        if (memory != null) {

            return memory.getIfPresent(key);
        }

        if (persistence != null) {

            return persistence.retrieveRecord(key,type);
        }

        return null;
    }

    <T> void save(String key, Record<T> record) {

        if (record != null) {

            if (memory != null) {
                memory.put(key, record);
            }

            if (persistence != null) {
                persistence.saveRecord(key, record);
            }

        } else {

            if (memory != null) {
                memory.evict(key);
            }

            if (persistence != null) {
                persistence.evict(key);
            }
        }
    }

    boolean containsKey(String key) {

        return (memory != null && memory.containsKey(key)) || (persistence != null && persistence.containsKey(key));
    }

    void remove(String key) {

        if (memory != null) {
            memory.evict(key);
        }

        if (persistence != null) {
            persistence.evict(key);
        }
    }

    void clear() {

        if (memory != null) {
            memory.evictAll();
        }

        if (persistence != null) {
            persistence.evictAll();
        }
    }
}
