package com.safframework.rxcache.domain.info;

import com.safframework.rxcache.memory.Memory;
import com.safframework.rxcache.persistence.Persistence;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.reflect.TypeUtils;
import com.safframework.rxcache.utils.GsonUtils;

import static org.joor.Reflect.on;

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
            memory.memoryImpl = TypeUtils.getClassSimpleName(builder.memory);
            memory.cacheStatistics = builder.memory.getCacheStatistics();
        }

        if (builder.persistence!=null) {

            persistence = new PersistenceInfo();
            persistence.keys = builder.persistence.allKeys();
            persistence.persistenceImpl = TypeUtils.getClassSimpleName(builder.persistence);
            Converter converter = on(builder.persistence).field("converter").get();
            persistence.converterName = converter.converterName();
        }
    }

    public static final class Builder {

        private Memory memory;
        private Persistence persistence;

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

        return GsonUtils.toJson(this);
    }
}
