package com.cv4j.rxcache;

import com.cv4j.rxcache.domain.CacheHolder;
import com.cv4j.rxcache.domain.Record;
import com.cv4j.rxcache.domain.Source;
import com.cv4j.rxcache.memory.Memory;
import com.cv4j.rxcache.persistence.Persistence;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

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

            CacheHolder<T> result = memory.getIfPresent(key);

            if (result!=null) {

                return new Record<>(Source.MEMORY,key,result.data,result.timestamp);
            }
        }

        if (persistence != null) {

            CacheHolder<T> result = persistence.retrieve(key,type);

            if (result!=null) {

                return new Record<>(Source.PERSISTENCE,key,result.data,result.timestamp);
            }
        }

        return null;
    }

    <T> void save(String key, T value) {

        if (value != null) {

            if (memory != null) {
                memory.put(key, value);
            }

            if (persistence != null) {
                persistence.save(key, value);
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

    Set<String> getAllKeys() {

        Set<String> result = new HashSet<>();

        if (memory!=null) {

            result.addAll(memory.keySet());
        }

        if (persistence!=null) {

            result.addAll(persistence.allKeys());
        }

        return result;
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
