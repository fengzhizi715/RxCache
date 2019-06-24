import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.CacheStrategy
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.reflect.TypeToken
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single
import java.lang.reflect.Type
import java.util.concurrent.TimeUnit

/**
 * Created by tony on 2019-06-24.
 */

inline fun <reified T> RxCache.load2Observable(key: String): Observable<Record<T>> = load2Observable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Flowable(key: String): Flowable<Record<T>> = load2Flowable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Single(key: String): Single<Record<T>> = load2Single<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Maybe(key: String): Maybe<Record<T>> = load2Maybe<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.get(key: String):Record<T> = get<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.get(key: String, cacheStrategy: CacheStrategy):Record<T> = get<T>(key, object : TypeToken<T>() {}.type, cacheStrategy)

inline fun <reified T> RxCache.expire(key: String, expireTime: Long) = expire<T>(key, object : TypeToken<T>() {}.type, expireTime)

inline fun <reified T> RxCache.expire(key: String, expireTime: Long, timeUnit: TimeUnit) = expire<T>(key, object : TypeToken<T>() {}.type, expireTime,timeUnit)

inline fun <reified T> RxCache.ttl(key: String) = ttl(key, object : TypeToken<T>() {}.type)