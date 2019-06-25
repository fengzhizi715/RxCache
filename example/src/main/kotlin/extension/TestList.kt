package extension

import com.safframework.bytekit.utils.Preconditions
import com.safframework.rxcache.RxCache
import com.safframework.rxcache.ext.load2Observable
import domain.User
import java.util.*

/**
 * Created by tony on 2019-06-25.
 */
object TestList {

    @JvmStatic
    fun main(args: Array<String>) {

        RxCache.config(RxCache.Builder())

        val rxCache = RxCache.getRxCache()

        val list = ArrayList<User>()

        val u1 = User()
        u1.name = "tony1"
        u1.password = "123456"
        list.add(u1)

        val u2 = User()
        u2.name = "tony2"
        u2.password = "123456"
        list.add(u2)
        rxCache.save<List<User>>("test", list)

        val observable = rxCache.load2Observable<List<User>>("test")

        observable.subscribe { record ->
            val recordDataList = record.data

            if (Preconditions.isNotBlank(recordDataList)) {

                val user = recordDataList[0]
                println(user.name)
                println(user.password)
            }
        }
    }
}