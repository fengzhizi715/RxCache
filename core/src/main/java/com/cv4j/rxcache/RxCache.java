package com.cv4j.rxcache;

import com.cv4j.rxcache.memory.Memory;
import com.cv4j.rxcache.persistence.Persistence;

/**
 * Created by tony on 2018/9/28.
 */
public class RxCache {

    private final CacheRepository cacheRepository;

    private static RxCache sDefaultRxCache;

    public static RxCache getDefault() {

        if (sDefaultRxCache == null) {
            sDefaultRxCache = new RxCache.Builder()
                    .build();

        }
        return sDefaultRxCache;
    }

    public static void initializeConfig(RxCache rxCache) {

        if (sDefaultRxCache == null) {
            RxCache.sDefaultRxCache = rxCache;
        }
    }

    private RxCache(Builder builder) {

        Memory memory= builder.memory;
        Persistence persistence = builder.persistence;

        cacheRepository = new CacheRepository(memory, persistence);
    }

    public static final class Builder {

        private static final int DEFAULT_MEMORY_CACHE_SIZE = (int) (Runtime.getRuntime().maxMemory() / 8);//运行内存的8分之1
        private Memory memory;
        private Persistence persistence;
        private Integer memoryMaxSize;

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

        /**
         * 不设置,默认为运行内存的8分之1.设置0,或小于0，则不开启内存缓存;
         */
        public Builder memoryMax(int maxSize) {
            this.memoryMaxSize = maxSize;
            return this;
        }

        public RxCache build() {

            return new RxCache(this);
        }

    }
}
