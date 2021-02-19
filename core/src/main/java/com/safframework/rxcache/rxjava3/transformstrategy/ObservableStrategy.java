package com.safframework.rxcache.rxjava3.transformstrategy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.domain.Record;
import io.reactivex.rxjava3.core.Observable;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface ObservableStrategy {

    <T> Observable<Record<T>> execute(RxCache rxCache, String key, Observable<T> source, Type type);
}
