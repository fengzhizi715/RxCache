package com.safframework.rxcache;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.Memory;
import com.safframework.rxcache.persistence.Persistence;
import com.safframework.tony.common.utils.Preconditions;

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

        if (Preconditions.isNotBlanks(key,type)) {

            if (memory != null) {

                return memory.getIfPresent(key);
            }

            if (persistence != null) {

                return persistence.retrieve(key,type);
            }
        }

        return null;
    }

    <T> void save(String key, T value) {

        save(key,value, Constant.NEVER_EXPIRE);
    }

    <T> void save(String key, T value, long expireTime) {

        if (Preconditions.isNotBlanks(key,value)) {

            if (memory != null) {
                memory.put(key, value, expireTime);
            }

            if (persistence != null) {
                persistence.save(key, value, expireTime);
            }
        }
    }

    boolean containsKey(String key) {

        if (Preconditions.isBlank(key)) return false;

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
