package com.safframework.rxcache.transformstrategy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import io.reactivex.rxjava3.core.Single;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface SingleStrategy {

    <T> Single<Record<T>> execute(RxCache rxCache, String key, Single<T> source, Type type);
}
