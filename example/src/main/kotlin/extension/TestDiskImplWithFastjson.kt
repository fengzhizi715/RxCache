package extension

import com.safframework.bytekit.utils.Preconditions
import com.safframework.rxcache.RxCache
import com.safframework.rxcache.converter.FastJSONConverter
import com.safframework.rxcache.persistence.disk.impl.DiskImpl
import com.safframework.rxcache.rxjava.rxjava3.load2Observable
import domain.User
import java.io.File
import java.util.*

/**
 * Created by tony on 2019-06-25.
 */
fun main() {

    val cacheDirectory = File("aaa")

    if (!cacheDirectory.exists()) {

        cacheDirectory.mkdir()
    }

    val diskImpl = DiskImpl(cacheDirectory, FastJSONConverter())

    RxCache.config(RxCache.Builder().persistence(diskImpl))

    val rxCache = RxCache.getRxCache()

    testObject(rxCache)
    testMap(rxCache)
    testList(rxCache)
    testSet(rxCache)
}

private fun testObject(rxCache: RxCache) {

    val u = User()
    u.name = "tony"
    u.password = "123456"
    rxCache.save("test", u)

    val observable = rxCache.load2Observable<User>("test")

    observable.subscribe { record ->
        val user = record.data
        println(user.name)
        println(user.password)
    }
}

private fun testMap(rxCache: RxCache) {

    val map = HashMap<String, User>()

    val u1 = User()
    u1.name = "tonyMap1"
    u1.password = "map1123456"
    map["u1"] = u1

    val u2 = User()
    u2.name = "tonyMap12"
    u2.password = "map12345"
    map["u2"] = u2
    rxCache.save<Map<String, User>>("map", map)

    val observable = rxCache.load2Observable<Map<String, User>>("map")

    observable.subscribe { record ->
        val recordDataList = record.data

        if (Preconditions.isNotBlank(recordDataList)) {

            val user = recordDataList["u1"]
            println(user?.name)
            println(user?.password)


            val user2 = recordDataList["u2"]
            println(user2?.name)
            println(user2?.password)
        }
    }
}

private fun testList(rxCache: RxCache) {

    val list = ArrayList<User>()

    val u1 = User()
    u1.name = "tonyList1"
    u1.password = "list1123456"
    list.add(u1)

    val u2 = User()
    u2.name = "tonyList12"
    u2.password = "list12345"
    list.add(u2)
    rxCache.save<List<User>>("list", list)

    val observable = rxCache.load2Observable<List<User>>("list")

    observable.subscribe { record ->
        val recordDataList = record.data

        if (Preconditions.isNotBlank(recordDataList)) {
            for (user in recordDataList) {
                println(user.name)
                println(user.password)
            }
        }
    }
}

private fun testSet(rxCache: RxCache) {

    val set = HashSet<User>()

    val u1 = User()
    u1.name = "tonySet1"
    u1.password = "set1123456"
    set.add(u1)

    val u2 = User()
    u2.name = "tonySet12"
    u2.password = "set12345"
    set.add(u2)
    rxCache.save<Set<User>>("set", set)

    val observable = rxCache.load2Observable<Set<User>>("set")

    observable.subscribe { record ->
        val recordDataList = record.data

        if (Preconditions.isNotBlank(recordDataList)) {
            for (user in recordDataList) {
                println(user.name)
                println(user.password)
            }
        }
    }
}