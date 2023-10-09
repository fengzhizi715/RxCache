package com.safframework.rxcache.rxjava.rxjava3.transformstrategy.impl

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.domain.Source
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
 *          com.safframework.rxcache.rxjava.rxjava3.transformstrategy.impl.NoCacheStrategy
 * @author: Tony Shen
 * @date: 2021-02-19 13:57
 * @version: V1.0 不使用缓存，也不保存缓存
 */
class NoCacheStrategy : ObservableStrategy, FlowableStrategy, MaybeStrategy {

    override fun <T:Any> execute(rxCache: RxCache, key: String, source: Flowable<T>, type: Type): Publisher<Record<T>> {
        return source.map{ t -> Record(Source.CLOUD, key, t) }
    }

    override fun <T:Any> execute(
        rxCache: RxCache,
        key: String,
        source: Flowable<T>,
        type: Type,
        backpressureStrategy: BackpressureStrategy
    ): Publisher<Record<T>> {
        return source.map{ t -> Record(Source.CLOUD, key, t) }
    }

    override fun <T> execute(rxCache: RxCache, key: String, source: Maybe<T>, type: Type): Maybe<Record<T>> {
        return source.map{ t -> Record(Source.CLOUD, key, t) }
    }

    override fun <T:Any> execute(
        rxCache: RxCache,
        key: String,
        source: Observable<T>,
        type: Type
    ): Observable<Record<T>> {
        return source.map{ t -> Record(Source.CLOUD, key, t) }
    }
}