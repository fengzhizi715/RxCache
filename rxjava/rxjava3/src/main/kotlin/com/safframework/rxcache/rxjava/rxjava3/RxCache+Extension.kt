package com.safframework.rxcache.rxjava.rxjava3

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.reflect.TypeToken
import com.safframework.rxcache.rxjava3.transformstrategy.*
import io.reactivex.rxjava3.core.*
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.rxcache.rxjava.rxjava3.`RxCache+Extension`
 * @author: Tony Shen
 * @date: 2021-02-19 12:18
 * @version: V1.0 <描述当前版本功能>
 */

fun <T> RxCache.transformObservable(
    key: String,
    type: Type,
    strategy: ObservableStrategy
): ObservableTransformer<T, Record<T>> {
    return ObservableTransformer<T, Record<T>> { upstream -> strategy.execute(this, key, upstream, type) }
}

fun <T> RxCache.transformFlowable(key: String, type: Type, strategy: FlowableStrategy): FlowableTransformer<T, Record<T>> {
    return FlowableTransformer { upstream -> strategy.execute(this, key, upstream, type) }
}

fun <T> RxCache.transformFlowable(
    key: String,
    type: Type,
    strategy: FlowableStrategy,
    backpressureStrategy: BackpressureStrategy
): FlowableTransformer<T, Record<T>> {
    return FlowableTransformer { upstream -> strategy.execute(this, key, upstream, type, backpressureStrategy) }
}

fun <T> RxCache.transformSingle(key: String, type: Type, strategy: SingleStrategy): SingleTransformer<T, Record<T>> {
    return SingleTransformer { upstream -> strategy.execute(this, key, upstream, type) }
}

fun <T> RxCache.transformCompletable(key: String, type: Type, strategy: CompletableStrategy): CompletableTransformer {
    return CompletableTransformer { upstream -> strategy.execute(this, key, upstream, type) }
}

fun <T> RxCache.transformMaybe(key: String, type: Type, strategy: MaybeStrategy): MaybeTransformer<T, Record<T>> {
    return MaybeTransformer { upstream -> strategy.execute(this, key, upstream, type) }
}

fun <T> RxCache.load2Observable(key: String?, type: Type): Observable<Record<T>> {
    val record: Record<T> = get(key, type)
    return if (record != null) Observable.create { emitter ->
        emitter.onNext(record)
        emitter.onComplete()
    } else Observable.empty()
}

fun <T> RxCache.load2Flowable(key: String, type: Type): Flowable<Record<T>> {
    return load2Flowable(key, type, BackpressureStrategy.MISSING)
}

fun <T> RxCache.load2Flowable(key: String?, type: Type, backpressureStrategy: BackpressureStrategy): Flowable<Record<T>> {
    val record: Record<T> = get(key, type)
    return if (record != null) Flowable.create({ emitter ->
        emitter.onNext(record)
        emitter.onComplete()
    }, backpressureStrategy) else Flowable.empty()
}

fun <T> RxCache.load2Single(key: String, type: Type): Single<Record<T>> {
    val record: Record<T> = get(key, type)
    return if (record != null) Single.create { emitter -> emitter.onSuccess(record) } else Single.never()
}

fun <T> RxCache.load2Maybe(key: String, type: Type): Maybe<Record<T>> {
    val record: Record<T> = get(key, type)
    return if (record != null) Maybe.create { emitter ->
        emitter.onSuccess(record)
        emitter.onComplete()
    } else Maybe.empty()
}

inline fun <reified T> RxCache.transformObservable(key: String, strategy: ObservableStrategy): ObservableTransformer<T, Record<T>> = transformObservable<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformFlowable(key: String, strategy: FlowableStrategy): FlowableTransformer<T, Record<T>> = transformFlowable<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformFlowable(key: String, strategy: FlowableStrategy, backpressureStrategy: BackpressureStrategy): FlowableTransformer<T, Record<T>> = transformFlowable<T>(key, object : TypeToken<T>() {}.type, strategy, backpressureStrategy)

inline fun <reified T> RxCache.transformSingle(key: String, strategy: SingleStrategy): SingleTransformer<T, Record<T>> = transformSingle<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformCompletable(key: String, strategy: CompletableStrategy): CompletableTransformer = transformCompletable<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformMaybe(key: String, strategy: MaybeStrategy): MaybeTransformer<T, Record<T>> = transformMaybe<T>(key, object : TypeToken<T>() {}.type, strategy)


inline fun <reified T> RxCache.load2Observable(key: String): Observable<Record<T>> = load2Observable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Flowable(key: String): Flowable<Record<T>> = load2Flowable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Flowable(key: String, backpressureStrategy: BackpressureStrategy): Flowable<Record<T>> = load2Flowable<T>(key, object : TypeToken<T>() {}.type, backpressureStrategy)

inline fun <reified T> RxCache.load2Single(key: String): Single<Record<T>>? = load2Single<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Maybe(key: String): Maybe<Record<T>> = load2Maybe<T>(key, object : TypeToken<T>() {}.type)