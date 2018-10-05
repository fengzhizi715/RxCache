package com.safframework.rxcache.transformstrategy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import io.reactivex.Maybe;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface MaybeStrategy {

    <T> Maybe<Record<T>> execute(RxCache rxCache, String key, Maybe<T> source, Type type);
}
