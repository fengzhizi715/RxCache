package com.safframework.rxcache.rxjava.rxjava2.impl

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.domain.Source
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
import java.util.*

/**
 *
 * @FileName:
 *          com.safframework.rxcache.rxjava.rxjava3.transformstrategy.impl.CacheAndRemoteStrategy
 * @author: Tony Shen
 * @date: 2021-02-19 13:06
 * @version: V1.0 先获取缓存，再获取网络请求
 */
class CacheAndRemoteStrategy : ObservableStrategy, FlowableStrategy, MaybeStrategy {

    override fun <T> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<Record<T>> {
        val cache: Flowable<Record<T>> = rxCache.load2Flowable(key, type)
        val remote: Flowable<Record<T>> = source
            .map{ t ->
                rxCache.save(key, t)
                Record(Source.CLOUD, key, t)
            }
        return Flowable.concatDelayError(Arrays.asList(cache, remote))
            .filter{ record -> record.data != null }
    }

    override fun <T> execute(
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
        return Flowable.concatDelayError(Arrays.asList(cache, remote))
            .filter{ record -> record.data != null }
    }

    override fun <T> execute(rxCache: RxCache, key: String, source: Maybe<T>, type: Type): Maybe<Record<T>> {
        val cache: Maybe<Record<T>> = rxCache.load2Maybe(key, type)
        val remote: Maybe<Record<T>> = source
            .map{ t ->
                rxCache.save(key, t)
                Record(Source.CLOUD, key, t)
            }
        return Maybe.concatDelayError(Arrays.asList(cache, remote))
            .filter{ record -> record.data != null }
            .firstElement()
    }

    override fun <T> execute(rxCache: RxCache, key: String, source: Observable<T>, type: Type): Observable<Record<T>> {
        val cache: Observable<Record<T>> = rxCache.load2Observable(key, type)
        val remote: Observable<Record<T>> = source
            .map{ t ->
                rxCache.save(key, t)
                Record(Source.CLOUD, key, t)
            }
        return Observable.concatDelayError(Arrays.asList(cache, remote))
            .filter{ record -> record.data != null }
    }
}