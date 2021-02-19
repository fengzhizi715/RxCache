package com.safframework.rxcache.rxjava.rxjava3.transformstrategy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import io.reactivex.rxjava3.core.BackpressureStrategy;
import io.reactivex.rxjava3.core.Flowable;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface FlowableStrategy {

    <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type);

    <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type, BackpressureStrategy backpressureStrategy);
}
