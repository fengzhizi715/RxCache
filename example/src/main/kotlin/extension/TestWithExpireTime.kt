package extension

import com.safframework.rxcache.RxCache
import com.safframework.rxcache.ext.get
import domain.User

/**
 * Created by tony on 2019-06-26.
 */
object TestWithExpireTime {

    @JvmStatic
    fun main(args: Array<String>) {

        RxCache.config(RxCache.Builder())

        val rxCache = RxCache.getRxCache()

        val u = User()
        u.name = "tony"
        u.password = "123456"
        rxCache.save("test", u, 2000)

        try {
            Thread.sleep(2500)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }

        val record = rxCache.get<User>("test")

        if (record == null) {
            println("record is null")
        }
    }
}