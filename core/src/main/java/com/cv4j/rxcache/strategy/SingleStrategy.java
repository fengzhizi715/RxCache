package com.cv4j.rxcache.strategy;

import com.cv4j.rxcache.RxCache;
import com.cv4j.rxcache.domain.Record;
import io.reactivex.Single;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface SingleStrategy {

    <T> Single<Record<T>> execute(RxCache rxCache, String key, Single<T> source, Type type);
}
