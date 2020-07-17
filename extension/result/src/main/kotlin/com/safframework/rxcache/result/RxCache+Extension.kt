package com.safframework.rxcache.result

import cn.netdiscovery.result.Result
import cn.netdiscovery.result.resultFrom
import com.safframework.rxcache.RxCache
import com.safframework.rxcache.reflect.TypeToken

/**
 *
 * @FileName:
 *          com.safframework.rxcache.result.`RxCache+Extension`
 * @author: Tony Shen
 * @date: 2020-07-17 11:10
 * @version: V1.0 <描述当前版本功能>
 */
inline fun <reified T> RxCache.getResult(key: String): Result<T,Exception> = resultFrom {
    get<T>(key, object : TypeToken<T>() {}.type).data
}