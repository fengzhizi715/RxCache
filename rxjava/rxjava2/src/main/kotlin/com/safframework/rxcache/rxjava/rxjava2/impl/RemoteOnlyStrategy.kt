package com.safframework.rxcache.rxjava.rxjava2.impl

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.domain.Source
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
 *          com.safframework.rxcache.rxjava.rxjava3.transformstrategy.impl.RemoteOnlyStrategy
 * @author: Tony Shen
 * @date: 2021-02-19 14:05
 * @version: V1.0 只获取接口的数据，并且将获取到数据保持到缓存中。
 */
class RemoteOnlyStrategy : ObservableStrategy, FlowableStrategy, MaybeStrategy {
    override fun <T> execute(
        rxCache: RxCache,
        key: String,
        source: Flowable<T>,
        type: Type
    ): Publisher<Record<T>> {
        return source
            .map{ t ->
                rxCache.save<T>(key, t)
                Record(Source.CLOUD, key, t)
            }
    }

    override fun <T> execute(
        rxCache: RxCache,
        key: String,
        source: Flowable<T>,
        type: Type,
        backpressureStrategy: BackpressureStrategy
    ): Publisher<Record<T>> {
        return source
            .map{ t ->
                rxCache.save<T>(key, t)
                Record(Source.CLOUD, key, t)
            }
    }

    override fun <T> execute(
        rxCache: RxCache,
        key: String,
        source: Maybe<T>,
        type: Type
    ): Maybe<Record<T>> {
        return source
            .map{ t ->
                rxCache.save<T>(key, t)
                Record(Source.CLOUD, key, t)
            }
    }

    override fun <T> execute(
        rxCache: RxCache,
        key: String,
        source: Observable<T>,
        type: Type
    ): Observable<Record<T>> {
        return source
            .map{ t ->
                rxCache.save<T>(key, t)
                Record(Source.CLOUD, key, t)
            }
    }
}