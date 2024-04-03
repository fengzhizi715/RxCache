package extension

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.ext.getResult
import com.safframework.rxcache.ext.saveFunc
import domain.User

/**
 *
 * @FileName:
 *          extension.TestResult
 * @author: Tony Shen
 * @date: 2020-07-17 13:06
 * @version: V1.0 <描述当前版本功能>
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

    rxCache.getResult<User>("test1").getOrNull()?.let {

        print(it.name)
        println(it.password)
    }

    rxCache.saveFunc("test2") {
        "hello world"
    }

    rxCache.getResult<String>("test2").getOrNull()?.let {
        println(it)
    }

    val result = rxCache.getResult<String>("test2").map {
         "$it, hello kotlin"
    }.getOrNull()

    println(result)

}