package com.safframework.rxcache.rxjava.rxjava2.transformstrategy.impl

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.rxjava.rxjava2.load2Flowable
import com.safframework.rxcache.rxjava.rxjava2.load2Maybe
import com.safframework.rxcache.rxjava.rxjava2.load2Observable
import com.safframework.rxcache.rxjava.rxjava2.transformstrategy.FlowableStrategy
import com.safframework.rxcache.rxjava.rxjava2.transformstrategy.MaybeStrategy
import com.safframework.rxcache.rxjava.rxjava2.transformstrategy.ObservableStrategy
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import org.reactivestreams.Publisher
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.rxcache.rxjava.rxjava2.transformstrategy.impl.CacheOnlyStrategy
 * @author: Tony Shen
 * @date: 2021-02-19 13:48
 * @version: V1.0 只获取缓存的策略
 */
class CacheOnlyStrategy : ObservableStrategy, FlowableStrategy, MaybeStrategy {

    override fun <T> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<Record<T>> {
        return rxCache.load2Flowable(key, type)
    }

    override fun <T> execute(
        rxCache: RxCache,
        key: String,
        source: Flowable<T>,
        type: Type,
        backpressureStrategy: BackpressureStrategy
    ): Publisher<Record<T>> {
        return rxCache.load2Flowable(key, type, backpressureStrategy)
    }

    override fun <T> execute(rxCache: RxCache, key: String, source: Maybe<T>, type: Type): Maybe<Record<T>> {
        return rxCache.load2Maybe(key, type)
    }

    override fun <T> execute(
        rxCache: RxCache,
        key: String,
        source: Observable<T>,
        type: Type
    ): Observable<Record<T>> {
        return rxCache.load2Observable(key, type)
    }
}