package com.cv4j.rxcache.strategy;

import com.cv4j.rxcache.RxCache;
import com.cv4j.rxcache.domain.Record;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface FlowableStrategy {

    <T> Publisher<Record<T>> execute(RxCache rxCache, String key, Flowable<T> source, Type type);
}
