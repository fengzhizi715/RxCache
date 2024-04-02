package extension

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.log.Logger
import com.safframework.rxcache.log.logI
import com.safframework.rxcache.rxjava.rxjava3.load2Observable
import domain.User

/**
 *
 * @FileName:
 *          extension.TestLog
 * @author: Tony Shen
 * @date: 2024/4/2 23:14
 * @version: V1.0 <描述当前版本功能>
 */
fun main() {

    RxCache.config(RxCache.Builder().log(object :Logger{
        override fun i(msg: String, tag: String?) {
            println("$tag = $msg")
        }

        override fun v(msg: String, tag: String?) {
        }

        override fun d(msg: String, tag: String?) {
        }

        override fun w(msg: String, tag: String?, tr: Throwable?) {
        }

        override fun e(msg: String, tag: String?, tr: Throwable?) {
        }

    }))

    val rxCache = RxCache.getRxCache()

    val u = User()
    u.name = "tony"
    u.password = "123456"
    rxCache.save("test", u)

    val observable = rxCache.load2Observable<User>("test", User::class.java)

    observable.subscribe { record ->
        val user = record.data
        user.name.logI("user.name")
        user.password.logI("user.password")
        user.logI("user")
    }
}