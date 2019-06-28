package extension

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.CacheStrategy
import com.safframework.rxcache.ext.get
import domain.User

/**
 * Created by tony on 2019-06-28.
 */
object TestCacheStrategy {

    @JvmStatic
    fun main(args: Array<String>) {

        RxCache.config(RxCache.Builder())

        val rxCache = RxCache.getRxCache()

        val u = User()
        u.name = "tony"
        u.password = "123456"
        rxCache.save("test", u)

        val record1 = rxCache.get<User>("test", CacheStrategy.PERSISTENCE)
        if (record1 == null) {
            println("record1 is null")
        }

        val record2 = rxCache.get<User>("test", CacheStrategy.ALL)
        if (record2 != null) {
            println(record2.data.name)
            println(record2.data.password)
        }
    }
}
