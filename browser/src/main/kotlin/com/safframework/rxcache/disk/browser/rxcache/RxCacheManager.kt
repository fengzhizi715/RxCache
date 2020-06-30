package com.safframework.rxcache.disk.browser.rxcache

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.disk.browser.Config
import com.safframework.rxcache.memory.impl.FIFOMemoryImpl
import com.safframework.rxcache.persistence.okio.OkioImpl
import java.io.File

/**
 *
 * @FileName:
 *          com.safframework.rxcache.disk.browser.rxcache.RxCacheManager
 * @author: Tony Shen
 * @date: 2020-06-30 16:07
 * @version: V1.0 <描述当前版本功能>
 */
val rxCache: RxCache by lazy {

    val cacheDirectory = File(Config.path) // rxCache 持久层存放地址
    if (!cacheDirectory.exists()) {
        cacheDirectory.mkdir()
    }
    val diskImpl = OkioImpl(cacheDirectory)
    RxCache.config(RxCache.Builder().memory(FIFOMemoryImpl()).persistence(diskImpl)) // 初始化 RxCache, 并配置二级缓存

    RxCache.getRxCache()
}