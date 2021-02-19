package com.safframework.rxcache.rxjava3.transformstrategy.impl;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.rxjava3.transformstrategy.FlowableStrategy;
import com.safframework.rxcache.rxjava3.transformstrategy.MaybeStrategy;
import com.safframework.rxcache.rxjava3.transformstrategy.ObservableStrategy;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;

import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.functions.Function;
import io.reactivex.rxjava3.functions.Predicate;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

/**
 * 缓存优先，并且缓存有有效时间
 * Created by tony on 2018/10/2.
 */
public class CacheFirstTimeoutStrategy implements ObservableStrategy,
        FlowableStrategy,
        MaybeStrategy {

    private long timestamp;

    public CacheFirstTimeoutStrategy(long timestamp) {

        this.timestamp = timestamp;
    }

    @Override
    public <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type) {

        Flowable<Record<T>> cache = rxCache.<T>load2Flowable(key, type)
                .filter(new Predicate<Record<T>>() {
                    @Override
                    public boolean test(Record<T> record) throws Exception {
                        return System.currentTimeMillis() - record.getCreateTime() <= timestamp;
                    }
                });

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

        Flowable<Record<T>> cache = rxCache.<T>load2Flowable(key, type, backpressureStrategy)
                .filter(new Predicate<Record<T>>() {
                    @Override
                    public boolean test(Record<T> record) throws Exception {
                        return System.currentTimeMillis() - record.getCreateTime() <= timestamp;
                    }
                });

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

        Maybe<Record<T>> cache = rxCache.<T>load2Maybe(key, type)
                .filter(new Predicate<Record<T>>() {
                    @Override
                    public boolean test(Record<T> record) throws Exception {
                        return System.currentTimeMillis() - record.getCreateTime() <= timestamp;
                    }
                });

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

        Observable<Record<T>> cache = rxCache.<T>load2Observable(key, type)
                .filter(new Predicate<Record<T>>() {
                    @Override
                    public boolean test(Record<T> record) throws Exception {
                        return System.currentTimeMillis() - record.getCreateTime() <= timestamp;
                    }
                });

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
