package com.safframework.rxcache.coroutines

import com.safframework.kotlin.coroutines.asyncInBackground
import com.safframework.rxcache.RxCache
import kotlinx.coroutines.flow.flowOf
import java.lang.reflect.Type

/**
 *
 * @FileName:
 *          com.safframework.rxcache.coroutines.`RxCache+Extension`
 * @author: Tony Shen
 * @date: 2020-07-17 14:10
 * @version: V1.0 <描述当前版本功能>
 */
fun <T> RxCache.getDeferred(key: String,type: Type)= asyncInBackground {
    get<T>(key,type).data
}

fun <T> RxCache.getFlow(key: String,type: Type) = flowOf(get<T>(key,type).data)