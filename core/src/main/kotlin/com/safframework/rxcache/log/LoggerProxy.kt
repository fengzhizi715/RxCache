package com.safframework.rxcache.log

import com.safframework.rxcache.utils.GsonUtils

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

object DefaultLogger: Logger {
    override fun i(msg: String, tag: String?) {
        println("$tag $msg")
    }

    override fun v(msg: String, tag: String?) {
        println("$tag $msg")
    }

    override fun d(msg: String, tag: String?) {
        println("$tag $msg")
    }

    override fun w(msg: String, tag: String?, tr: Throwable?) {
        tr?.printStackTrace()
        println("$tag $msg")
    }

    override fun e(msg: String, tag: String?, tr: Throwable?) {
        tr?.printStackTrace()
        System.err.println("$tag $msg")
    }
}

object LoggerProxy {

    private lateinit var mLogger: Logger

    fun initLogger(logger: Logger) {
        mLogger = logger
    }

    fun getLogger() = mLogger
}

fun String.logI(tag:String = "rxcache") = LoggerProxy.getLogger().i(this, tag)

fun Any.logI(tag:String = "rxcache") = LoggerProxy.getLogger().i(GsonUtils.toJson(this), tag)

fun Throwable.logE(msg: String,tag:String = "rxcache") = LoggerProxy.getLogger().e(msg, tag, this)