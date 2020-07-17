package extension

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.coroutines.getDeferred
import com.safframework.rxcache.ext.saveFunc
import domain.User
import kotlinx.coroutines.runBlocking

/**
 *
 * @FileName:
 *          extension.TestCoroutines
 * @author: Tony Shen
 * @date: 2020-07-17 14:25
 * @version: V1.0 <描述当前版本功能>
 */
fun main() = runBlocking {

    RxCache.config(RxCache.Builder())

    val rxCache = RxCache.getRxCache()

    rxCache.saveFunc("test1") {
        val u = User()
        u.name = "tony"
        u.password = "123456"
        u
    }

    rxCache.saveFunc("test2") {
        "hello world"
    }

    val deferred1 =  rxCache.getDeferred<User>("test1",User::class.java)

    val deferred2 = rxCache.getDeferred<String>("test2",String::class.java)

    val result = deferred1.await().name + "," + deferred2.await()
    println(result)
}