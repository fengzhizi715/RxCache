package extension

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.ext.get
import com.safframework.rxcache.ext.saveFunc
import domain.User
import java.util.concurrent.TimeUnit

/**
 * Created by tony on 2019-08-13.
 */
fun main() {

    RxCache.config(RxCache.Builder())

    val rxCache = RxCache.getRxCache()

    rxCache.saveFunc("test1") {
        val u = User()
        u.name = "tony"
        u.password = "123456"
        u
    }

    rxCache.get<User>("test1")?.let {

        val u = it.data

        println(u.name)
        println(u.password)
    }

    rxCache.saveFunc("test2") {
        "hello world"
    }

    rxCache.get<String>("test2")?.let {
        println(it.data)
    }

    rxCache.saveFunc("test3",5, TimeUnit.SECONDS) {

        val u = User()
        u.name = "tony"
        u.password = "123456"
        u
    }

    try {
        Thread.sleep(2300)
    } catch (e: InterruptedException) {
        e.printStackTrace()
    }

    println("ttl=" + rxCache.ttl("test3", User::class.java))
}