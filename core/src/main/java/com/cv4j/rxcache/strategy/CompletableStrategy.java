package com.cv4j.rxcache.strategy;

import com.cv4j.rxcache.RxCache;
import io.reactivex.Completable;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface CompletableStrategy {

    Completable execute(RxCache rxCache, String key, Completable source, Type type);
}
