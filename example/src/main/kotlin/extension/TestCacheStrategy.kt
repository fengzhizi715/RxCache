package extension

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.CacheStrategy
import com.safframework.rxcache.ext.get
import domain.User

/**
 * Created by tony on 2019-06-28.
 */
fun main() {

    RxCache.config(RxCache.Builder())

    val rxCache = RxCache.getRxCache()

    val u = User()
    u.name = "tony"
    u.password = "123456"
    rxCache.save("test", u)

    rxCache.get<User>("test", CacheStrategy.PERSISTENCE) ?: println("record1 is null")

    rxCache.get<User>("test", CacheStrategy.ALL)?.let {
        println(it.data.name)
        println(it.data.password)
    }
}
