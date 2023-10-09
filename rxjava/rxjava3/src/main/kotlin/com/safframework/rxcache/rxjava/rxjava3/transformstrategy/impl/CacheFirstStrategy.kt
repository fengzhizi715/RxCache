package com.safframework.rxcache.rxjava.rxjava3.transformstrategy.impl

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.domain.Source
import com.safframework.rxcache.rxjava.rxjava3.load2Flowable
import com.safframework.rxcache.rxjava.rxjava3.load2Maybe
import com.safframework.rxcache.rxjava.rxjava3.load2Observable
import com.safframework.rxcache.rxjava.rxjava3.transformstrategy.FlowableStrategy
import com.safframework.rxcache.rxjava.rxjava3.transformstrategy.MaybeStrategy
import com.safframework.rxcache.rxjava.rxjava3.transformstrategy.ObservableStrategy
import io.reactivex.rxjava3.core.BackpressureStrategy
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.core.Maybe
import io.reactivex.rxjava3.core.Observable
import org.reactivestreams.Publisher
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.rxcache.rxjava.rxjava3.transformstrategy.impl.CacheFirstStrategy
 * @author: Tony Shen
 * @date: 2021-02-19 13:15
 * @version: V1.0 缓存优先的策略，缓存取不到时取接口的数据。
 */
class CacheFirstStrategy : ObservableStrategy, FlowableStrategy, MaybeStrategy {

    override fun <T:Any> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<Record<T>> {
        val cache: Flowable<Record<T>> = rxCache.load2Flowable(key, type)
        val remote: Flowable<Record<T>> = source
            .map{ t ->
                rxCache.save(key, t)
                Record(Source.CLOUD, key, t)
            }
        return cache.switchIfEmpty(remote)
    }

    override fun <T:Any> execute(
        rxCache: RxCache,
        key: String,
        source: Flowable<T>,
        type: Type,
        backpressureStrategy: BackpressureStrategy
    ): Publisher<Record<T>> {
        val cache: Flowable<Record<T>> = rxCache.load2Flowable(key, type, backpressureStrategy)
        val remote: Flowable<Record<T>> = source
            .map{ t ->
                rxCache.save(key, t)
                Record(Source.CLOUD, key, t)
            }
        return cache.switchIfEmpty(remote)
    }

    override fun <T> execute(rxCache: RxCache, key: String, source: Maybe<T>, type: Type): Maybe<Record<T>> {
        val cache: Maybe<Record<T>> = rxCache.load2Maybe(key, type)
        val remote: Maybe<Record<T>> = source
            .map{ t ->
                rxCache.save(key, t)
                Record(Source.CLOUD, key, t)
            }
        return cache.switchIfEmpty(remote)
    }

    override fun <T:Any> execute(
        rxCache: RxCache,
        key: String,
        source: Observable<T>,
        type: Type
    ): Observable<Record<T>> {
        val cache: Observable<Record<T>> = rxCache.load2Observable(key, type)
        val remote: Observable<Record<T>> = source
            .map{ t ->
                rxCache.save(key, t)
                Record(Source.CLOUD, key, t)
            }
        return cache.switchIfEmpty(remote)
    }
}