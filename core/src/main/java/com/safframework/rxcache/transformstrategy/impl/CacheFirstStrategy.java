package com.safframework.rxcache.transformstrategy.impl;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.transformstrategy.FlowableStrategy;
import com.safframework.rxcache.transformstrategy.MaybeStrategy;
import com.safframework.rxcache.transformstrategy.ObservableStrategy;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

/**
 * 缓存优先的策略，缓存取不到时取接口的数据。
 * Created by tony on 2018/9/30.
 */
public class CacheFirstStrategy implements ObservableStrategy,
        FlowableStrategy,
        MaybeStrategy {

    @Override
    public <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type) {

        Flowable<Record<T>> cache = rxCache.<T>load2Flowable(key, type);

        Flowable<Record<T>> remote = source
                .map(new Function<T, Record<T>>() {
                    @Override
                    public Record<T> apply(@NonNull T t) throws Exception {

                        rxCache.save(key, t);

                        return new Record<>(Source.CLOUD, key, t);
                    }
                });

        return cache.switchIfEmpty(remote);
    }

    @Override
    public <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type, BackpressureStrategy backpressureStrategy) {

        Flowable<Record<T>> cache = rxCache.<T>load2Flowable(key, type, backpressureStrategy);

        Flowable<Record<T>> remote = source
                .map(new Function<T, Record<T>>() {
                    @Override
                    public Record<T> apply(@NonNull T t) throws Exception {

                        rxCache.save(key, t);

                        return new Record<>(Source.CLOUD, key, t);
                    }
                });

        return cache.switchIfEmpty(remote);
    }

    @Override
    public <T> Maybe<Record<T>> execute(RxCache rxCache, String key, Maybe<T> source, Type type) {

        Maybe<Record<T>> cache = rxCache.<T>load2Maybe(key, type);

        Maybe<Record<T>> remote = source
                .map(new Function<T, Record<T>>() {
                    @Override
                    public Record<T> apply(@NonNull T t) throws Exception {

                        rxCache.save(key, t);

                        return new Record<>(Source.CLOUD, key, t);
                    }
                });

        return cache.switchIfEmpty(remote);
    }

    @Override
    public <T> Observable<Record<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {

        Observable<Record<T>> cache = rxCache.<T>load2Observable(key, type);

        Observable<Record<T>> remote = source
                .map(new Function<T, Record<T>>() {
                    @Override
                    public Record<T> apply(@NonNull T t) throws Exception {

                        rxCache.save(key, t);

                        return new Record<>(Source.CLOUD, key, t);
                    }
                });

        return cache.switchIfEmpty(remote);
    }
}
