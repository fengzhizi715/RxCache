package com.safframework.rxcache.ext

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.CacheStrategy
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.memory.Memory
import com.safframework.rxcache.persistence.Persistence
import com.safframework.rxcache.persistence.converter.Converter
import com.safframework.rxcache.reflect.TypeToken
import com.safframework.rxcache.rxjava3.transformstrategy.*
import io.reactivex.rxjava3.core.*
import java.util.concurrent.TimeUnit

/**
 * Created by tony on 2019-06-24.
 */

typealias valueFuc<T> = () -> T

inline fun <reified T> RxCache.get(key: String): Record<T>? = get<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.get(key: String, cacheStrategy: CacheStrategy): Record<T>? = get<T>(key, object : TypeToken<T>() {}.type, cacheStrategy)

inline fun <reified T> RxCache.parseStringData(converter: Converter, data: String) = parseStringData(converter,data,object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.expire(key: String, expireTime: Long) = expire<T>(key, object : TypeToken<T>() {}.type, expireTime)

inline fun <reified T> RxCache.expire(key: String, expireTime: Long, timeUnit: TimeUnit) = expire<T>(key, object : TypeToken<T>() {}.type, expireTime, timeUnit)

inline fun <reified T> RxCache.saveFunc(key: String, noinline value: valueFuc<T>) {
    save(key, value.invoke())
}

inline fun <reified T> RxCache.saveFunc(key: String, expireTime: Long, noinline value: valueFuc<T>) {
    save(key, value.invoke(), expireTime)
}

inline fun <reified T> RxCache.saveFunc(key: String, expireTime: Long, timeUnit: TimeUnit, noinline value: valueFuc<T>) {
    save(key, value.invoke(), expireTime, timeUnit)
}

inline fun <reified T> RxCache.saveMemoryFunc(key: String, noinline value: valueFuc<T>) {
    saveMemory(key, value.invoke())
}

inline fun <reified T> RxCache.saveMemoryFunc(key: String, expireTime: Long, noinline value: valueFuc<T>) {
    saveMemory(key, value.invoke(), expireTime)
}

inline fun <reified T> RxCache.saveMemoryFunc(key: String, expireTime: Long, timeUnit: TimeUnit, noinline value: valueFuc<T>) {
    saveMemory(key, value.invoke(), expireTime, timeUnit)
}

inline fun <reified T> RxCache.updateFunc(key: String, noinline value: valueFuc<T>) {
    update(key, value.invoke())
}

inline fun <reified T> RxCache.updateFunc(key: String, expireTime: Long, noinline value: valueFuc<T>) {
    update(key, value.invoke(), expireTime)
}

inline fun <reified T> RxCache.updateFunc(key: String, expireTime: Long, timeUnit: TimeUnit, noinline value: valueFuc<T>) {
    update(key, value.invoke(), expireTime, timeUnit)
}

inline fun <reified T> RxCache.saveOrUpdateFunc(key: String, noinline value: valueFuc<T>) {
    saveOrUpdate(key, value.invoke())
}

inline fun <reified T> RxCache.saveOrUpdateFunc(key: String, expireTime: Long, noinline value: valueFuc<T>) {
    saveOrUpdate(key, value.invoke(), expireTime)
}

inline fun <reified T> RxCache.saveOrUpdateFunc(key: String, expireTime: Long, timeUnit: TimeUnit, noinline value: valueFuc<T>) {
    saveOrUpdate(key, value.invoke(), expireTime, timeUnit)
}

fun RxCache.Builder.memory(init:RxCache.Builder.()->Memory) = this.apply {
    this.memory(init())
}

fun RxCache.Builder.persistence(init:RxCache.Builder.()-> Persistence) = this.apply {
    this.persistence(init())
}