package com.cv4j.rxcache;

import com.cv4j.rxcache.domain.Record;
import com.cv4j.rxcache.memory.Memory;
import com.cv4j.rxcache.persistence.Persistence;

/**
 * Created by tony on 2018/9/28.
 */
public class CacheRepository {

    private Memory memory;
    private Persistence persistence;

    public CacheRepository(Memory memory, Persistence persistence) {
        this.memory = memory;
        this.persistence = persistence;
    }

    public <T> Record<T> get(String key) {

        if (memory != null) {

            return memory.getIfPresent(key);
        }

        if (persistence != null) {

            return persistence.retrieveRecord(key);
        }

        return null;
    }

    public <T> void save(String key, Record<T> record) {

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

    public boolean containsKey(String key) {

        return memory != null && memory.containsKey(key) || persistence != null && persistence.containsKey(key);
    }

    public void remove(String key) {

        if (memory != null) {
            memory.evict(key);
        }

        if (persistence != null) {
            persistence.evict(key);
        }
    }

    public void clear() {

        if (memory != null) {
            memory.evictAll();
        }

        if (persistence != null) {
            persistence.evictAll();
        }
    }
}
