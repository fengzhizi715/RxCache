package com.safframework.rxcache.ext

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.CacheStrategy
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.reflect.TypeToken
import com.safframework.rxcache.transformstrategy.*
import io.reactivex.*
import java.util.concurrent.TimeUnit

/**
 * Created by tony on 2019-06-24.
 */

inline fun <reified T> RxCache.transformObservable(key: String, strategy: ObservableStrategy): ObservableTransformer<T, Record<T>> = transformObservable<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformFlowable(key: String, strategy: FlowableStrategy): FlowableTransformer<T, Record<T>> = transformFlowable<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformSingle(key: String, strategy: SingleStrategy): SingleTransformer<T, Record<T>> = transformSingle<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformCompletable(key: String, strategy: CompletableStrategy): CompletableTransformer = transformCompletable<T>(key, object : TypeToken<T>() {}.type, strategy)

inline fun <reified T> RxCache.transformMaybe(key: String, strategy: MaybeStrategy): MaybeTransformer<T, Record<T>> = transformMaybe<T>(key, object : TypeToken<T>() {}.type, strategy)


inline fun <reified T> RxCache.load2Observable(key: String): Observable<Record<T>> = load2Observable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Flowable(key: String): Flowable<Record<T>> = load2Flowable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Single(key: String): Single<Record<T>> = load2Single<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Maybe(key: String): Maybe<Record<T>> = load2Maybe<T>(key, object : TypeToken<T>() {}.type)


inline fun <reified T> RxCache.get(key: String):Record<T>? = get<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.get(key: String, cacheStrategy: CacheStrategy):Record<T>? = get<T>(key, object : TypeToken<T>() {}.type, cacheStrategy)

inline fun <reified T> RxCache.expire(key: String, expireTime: Long) = expire<T>(key, object : TypeToken<T>() {}.type, expireTime)

inline fun <reified T> RxCache.expire(key: String, expireTime: Long, timeUnit: TimeUnit) = expire<T>(key, object : TypeToken<T>() {}.type, expireTime,timeUnit)