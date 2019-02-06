package com.safframework.rxcache.domain.info;

import com.google.gson.Gson;
import com.safframework.rxcache.memory.Memory;
import com.safframework.rxcache.persistence.Persistence;

/**
 * 显示缓存的信息
 * Created by tony on 2019-02-06.
 */
public class CacheInfo {

    private MemoryInfo memory;
    private PersistenceInfo persistence;

    private CacheInfo(Builder builder) {

        if (builder.memory!=null) {

            memory = new MemoryInfo();
            memory.keys = builder.memory.keySet();
            memory.memoryImpl = builder.memory.getClass().getSimpleName();
        }

        if (builder.persistence!=null) {

            persistence = new PersistenceInfo();
            persistence.keys = builder.persistence.allKeys();
            persistence.persistenceImpl = builder.persistence.getClass().getSimpleName();
        }
    }

    public static final class Builder {

        private Memory memory;
        private Persistence persistence;

        public Builder() {
        }

        public Builder memory(Memory memory) {
            this.memory = memory;
            return this;
        }

        public Builder persistence(Persistence persistence) {
            this.persistence = persistence;
            return this;
        }

        public CacheInfo build() {

            return new CacheInfo(this);
        }
    }

    public String toString() {

        return new Gson().toJson(this);
    }
}
