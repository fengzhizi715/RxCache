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

    /**
     * 读取
     */
    public <T> Record<T> get(String key) {
        if (memory != null) {
            Record<T> result = memory.getIfPresent(key);
            if (result != null) {
                return result;
            }
        }
        if (persistence != null) {
            Record<T> result = persistence.retrieveRecord(key);
            if (result != null) {
                return result;
            }
        }
        return null;
    }

    /**
     * 保存
     */
    public <T> void save(String key, Record<T> record) {

        if (record == null) { //如果要保存的值为空,则删除

            if (memory != null) {
                memory.evict(key);
            }

            if (persistence != null) {
                persistence.evict(key);
            }
        }

        if (memory != null) {
            memory.put(key, record);
        }

        if (persistence != null) {
            persistence.saveRecord(key, record);
        }
    }

    /**
     * 是否包含
     */
    public boolean containsKey(String key) {

        return memory != null && memory.containsKey(key) || persistence != null && persistence.containsKey(key);
    }

    /**
     * 删除缓存
     */
    public void remove(String key) {

        if (memory != null) {
            memory.evict(key);
        }

        if (persistence != null) {
            persistence.evict(key);
        }
    }

    /**
     * 清空缓存
     */
    public void clear() {

        if (memory != null) {
            memory.evictAll();
        }

        if (persistence != null) {
            persistence.evictAll();
        }
    }
}
