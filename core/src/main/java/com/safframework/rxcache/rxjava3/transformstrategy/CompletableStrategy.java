package com.safframework.rxcache.rxjava3.transformstrategy;

import com.safframework.rxcache.RxCache;
import io.reactivex.rxjava3.core.Completable;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface CompletableStrategy {

    Completable execute(RxCache rxCache, String key, Completable source, Type type);
}
