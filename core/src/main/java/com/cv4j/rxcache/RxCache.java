package com.cv4j.rxcache;

import com.cv4j.rxcache.domain.Record;
import com.cv4j.rxcache.memory.Memory;
import com.cv4j.rxcache.persistence.Persistence;
import io.reactivex.*;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public class RxCache {

    private final CacheRepository cacheRepository;

    private static RxCache mRxCache;

    public static RxCache getRxCache() {

        if (mRxCache == null) {

            mRxCache = new RxCache.Builder().build();
        }

        return mRxCache;
    }

    public static void config(Builder builder) {

        if (mRxCache == null) {

            RxCache.mRxCache = builder.build();
        }
    }

    private RxCache(Builder builder) {

        Memory memory= builder.memory;
        Persistence persistence = builder.persistence;

        cacheRepository = new CacheRepository(memory, persistence);
    }

    public <T> Observable<Record<T>> get(final String key, final Type type) {

        return Observable.create(new ObservableOnSubscribe<Record<T>>() {
            @Override
            public void subscribe(ObservableEmitter<Record<T>> emitter) throws Exception {

                Record<T> record = cacheRepository.get(key,type);
                if (!emitter.isDisposed()) {
                    if (record != null) {
                        emitter.onNext(record);
                        emitter.onComplete();
                    } else {

                        Observable.empty();
                    }
                }
            }
        });
    }

    public <T> void save(final String key, final T value) {

        cacheRepository.save(key, value);
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

            if (memoryMaxSize == null) {
                memoryMaxSize = DEFAULT_MEMORY_CACHE_SIZE;
            }

            return new RxCache(this);
        }

    }
}
