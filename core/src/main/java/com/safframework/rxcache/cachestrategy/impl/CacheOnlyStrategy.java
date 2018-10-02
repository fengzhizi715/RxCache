package com.safframework.rxcache.cachestrategy.impl;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.cachestrategy.FlowableStrategy;
import com.safframework.rxcache.cachestrategy.MaybeStrategy;
import com.safframework.rxcache.cachestrategy.ObservableStrategy;
import com.safframework.rxcache.domain.Record;
import io.reactivex.Flowable;
import io.reactivex.Maybe;
import io.reactivex.Observable;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/10/2.
 */
public class CacheOnlyStrategy implements ObservableStrategy,
        FlowableStrategy,
        MaybeStrategy {

    @Override
    public <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type) {

        return rxCache.<T>load2Flowable(key, type);
    }

    @Override
    public <T> Maybe<Record<T>> execute(RxCache rxCache, String key, Maybe<T> source, Type type) {

        return rxCache.<T>load2Maybe(key, type);
    }

    @Override
    public <T> Observable<Record<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type) {

        return rxCache.<T>load2Observable(key, type);
    }
}
