package com.safframework.rxcache.disk.browser.rxcache

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.converter.*
import com.safframework.rxcache.disk.browser.Config
import com.safframework.rxcache.ext.persistence
import com.safframework.rxcache.persistence.converter.Converter
import com.safframework.rxcache.persistence.converter.GsonConverter
import com.safframework.rxcache.persistence.disk.impl.DiskImpl
import com.safframework.rxcache.persistence.mapdb.MapDBImpl
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

    val converter: Converter = when (Config.converter) {
        "gson"      -> GsonConverter()
        "fastjson"  -> FastJSONConverter()
        "moshi"     -> MoshiConverter()
        "kryo"      -> KryoConverter()
        "hessian"   -> HessianConverter()
        "fst"       -> FSTConverter()
        "protobuf"  -> ProtobufConverter()
        else        -> GsonConverter()
    }

    RxCache.config {
        RxCache.Builder().persistence {
            when (Config.type) {
                "disk"  -> DiskImpl(cacheDirectory, converter)
                "okio"  -> OkioImpl(cacheDirectory, converter)
                "mapdb" -> MapDBImpl(cacheDirectory, converter)
                else    -> DiskImpl(cacheDirectory, converter)
            }
        }
    }

    RxCache.getRxCache()
}