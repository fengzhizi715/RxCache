package com.safframework.rxcache.rxjava.rxjava3

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.reflect.TypeToken
import com.safframework.rxcache.rxjava3.transformstrategy.*
import io.reactivex.rxjava3.core.*

/**
 *
 * @FileName:
 *          com.safframework.rxcache.rxjava.rxjava3.`RxCache+Extension`
 * @author: Tony Shen
 * @date: 2021-02-19 12:18
 * @version: V1.0 <描述当前版本功能>
 */

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