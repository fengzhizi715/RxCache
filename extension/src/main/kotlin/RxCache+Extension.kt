import com.safframework.rxcache.RxCache
import com.safframework.rxcache.domain.Record
import com.safframework.rxcache.reflect.TypeToken
import io.reactivex.Flowable
import io.reactivex.Maybe
import io.reactivex.Observable
import io.reactivex.Single

/**
 * Created by tony on 2019-06-24.
 */

inline fun <reified T> RxCache.load2Observable(key: String): Observable<Record<T>> = load2Observable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Flowable(key: String): Flowable<Record<T>> = load2Flowable<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Single(key: String): Single<Record<T>> = load2Single<T>(key, object : TypeToken<T>() {}.type)

inline fun <reified T> RxCache.load2Maybe(key: String): Maybe<Record<T>> = load2Maybe<T>(key, object : TypeToken<T>() {}.type)