package com.safframework.rxcache.log

/**
 *
 * @FileName:
 *          com.safframework.rxcache.log.LoggerProxy
 * @author: Tony Shen
 * @date: 2023/2/13 8:00 PM
 * @version: V1.0 <描述当前版本功能>
 */
interface Logger {
    fun i(msg: String, tag: String? = "rxcache")
    fun v(msg: String, tag: String? = "rxcache")
    fun d(msg: String, tag: String? = "rxcache")
    fun w(msg: String, tag: String? = "rxcache", tr: Throwable?)
    fun e(msg: String, tag: String? = "rxcache", tr: Throwable?)
}

internal object LoggerProxy {

    private lateinit var mLogger: Logger

    fun initLogger(logger: Logger) {
        mLogger = logger
    }

    fun getLogger() = mLogger
}

fun String.logI() = LoggerProxy.getLogger().i(this,"rxcache")

fun Throwable.logE(msg: String) = LoggerProxy.getLogger().e(msg, "rxcache", this)