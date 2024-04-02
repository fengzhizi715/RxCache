package com.safframework.rxcache.coroutines

import com.safframework.kotlin.coroutines.asyncInBackground
import com.safframework.kotlin.coroutines.extension.emitFlow
import com.safframework.rxcache.RxCache
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.rxcache.coroutines.`RxCache+Extension`
 * @author: Tony Shen
 * @date: 2020-07-17 14:10
 * @since: V1.8
 */
fun <T> RxCache.getDeferred(key: String,type: Type)= asyncInBackground {
    get<T>(key,type).data
}

fun <T> RxCache.getFlow(key: String,type: Type) = emitFlow {
    get<T>(key,type).data
}