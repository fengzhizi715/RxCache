package com.safframework.rxcache;

import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.Memory;
import com.safframework.rxcache.memory.impl.DefaultMemoryImpl;
import com.safframework.rxcache.persistence.Persistence;
import com.safframework.rxcache.cachestrategy.*;
import io.reactivex.*;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;
import java.util.Set;

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

    public <T> ObservableTransformer<T, Record<T>> transformObservable(final String key, final Type type, final ObservableStrategy strategy) {
        return new ObservableTransformer<T, Record<T>>() {
            @Override
            public ObservableSource<Record<T>> apply(Observable<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> FlowableTransformer<T, Record<T>> transformFlowable(final String key, final Type type, final FlowableStrategy strategy) {
        return new FlowableTransformer<T, Record<T>>() {
            @Override
            public Publisher<Record<T>> apply(Flowable<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> SingleTransformer<T, Record<T>> transformSingle(final String key, final Type type, final SingleStrategy strategy) {
        return new SingleTransformer<T, Record<T>>() {

            @Override
            public SingleSource<Record<T>> apply(Single<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> CompletableTransformer transformCompletable(final String key, final Type type, final CompletableStrategy strategy) {
        return new CompletableTransformer() {

            @Override
            public CompletableSource apply(Completable upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> MaybeTransformer<T, Record<T>> transformMaybe(final String key, final Type type, final MaybeStrategy strategy) {
        return new MaybeTransformer<T, Record<T>>() {

            @Override
            public MaybeSource<Record<T>> apply(Maybe<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> Observable<Record<T>> load2Observable(final String key, final Type type) {

        Record<T> recodrd = get(key,type);

        return recodrd!=null?Observable.just(recodrd):Observable.empty();
    }

    public <T> Flowable<Record<T>> load2Flowable(final String key, final Type type) {

        Record<T> recodrd = get(key,type);

        return recodrd!=null?Flowable.just(recodrd):Flowable.empty();
    }

    public <T> Single<Record<T>> load2Single(final String key, final Type type) {

        Record<T> recodrd = get(key,type);

        return recodrd!=null?Single.just(recodrd):Single.never();
    }

    public <T> Maybe<Record<T>> load2Maybe(final String key, final Type type) {

        Record<T> recodrd = get(key,type);

        return recodrd!=null?Maybe.just(recodrd):Maybe.empty();
    }

    public <T> Record<T> get(String key, Type type) {

        return cacheRepository.get(key,type);
    }

    public <T> void save(String key, T value) {

        cacheRepository.save(key, value);
    }

    public boolean containsKey(String key) {

        return cacheRepository.containsKey(key);
    }

    public Set<String> getAllKeys() {

        return cacheRepository.getAllKeys();
    }

    public void remove(String key) {

        cacheRepository.remove(key);
    }

    public void clear() {
        cacheRepository.clear();
    }

    public static final class Builder {

        private Memory memory;
        private int maxCacheSize = 100;
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

        public Builder maxCacheSize(int maxCacheSize) {
            this.maxCacheSize = maxCacheSize;
            return this;
        }

        public RxCache build() {

            if (memory == null && persistence == null) { // 至少保证 RxCache 可用

                memory = new DefaultMemoryImpl(maxCacheSize);
            }

            return new RxCache(this);
        }
    }
}
