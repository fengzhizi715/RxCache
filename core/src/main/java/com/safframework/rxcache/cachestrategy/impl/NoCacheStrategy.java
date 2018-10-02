package com.safframework.rxcache.cachestrategy.impl;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.cachestrategy.FlowableStrategy;
import com.safframework.rxcache.cachestrategy.MaybeStrategy;
import com.safframework.rxcache.cachestrategy.ObservableStrategy;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Function;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

/**
 * 不使用缓存，也不保存缓存
 * Created by tony on 2018/10/2.
 */
public class NoCacheStrategy implements ObservableStrategy,
        FlowableStrategy,
        MaybeStrategy {

    @Override
    public <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type) {

        return source.map(new Function<T, Record<T>>() {
            @Override
            public Record<T> apply(@NonNull T t) throws Exception {
                return new Record<>(Source.CLOUD, key, t);
            }
        });
    }

    @Override
    public <T> Maybe<Record<T>> execute(RxCache rxCache, String key, Maybe<T> source, Type type) {

        return source.map(new Function<T, Record<T>>() {
            @Override
            public Record<T> apply(@NonNull T t) throws Exception {
                return new Record<>(Source.CLOUD, key, t);
            }
        });
    }

    @Override
    public <T> Observable<Record<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {

        return source.map(new Function<T, Record<T>>() {
            @Override
            public Record<T> apply(@NonNull T t) throws Exception {
                return new Record<>(Source.CLOUD, key, t);
            }
        });
    }
}
