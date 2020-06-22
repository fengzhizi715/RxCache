package com.safframework.rxcache;

import com.safframework.rxcache.domain.CacheStrategy;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.memory.Memory;
import com.safframework.rxcache.memory.impl.FIFOMemoryImpl;
import com.safframework.rxcache.persistence.Persistence;
import com.safframework.rxcache.transformstrategy.CompletableStrategy;
import com.safframework.rxcache.transformstrategy.FlowableStrategy;
import com.safframework.rxcache.transformstrategy.MaybeStrategy;
import com.safframework.rxcache.transformstrategy.ObservableStrategy;
import com.safframework.rxcache.transformstrategy.SingleStrategy;

import io.reactivex.rxjava3.core.*;
import org.reactivestreams.Publisher;

import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * RxCache 是单例，使用时需要先调用 config() 配置 RxCache
 * Created by tony on 2018/9/28.
 */
public final class RxCache {

    private final CacheRepository cacheRepository;

    private static RxCache mRxCache;

    public static RxCache getRxCache() {

        if (mRxCache == null) {

            mRxCache = new RxCache.Builder().build();
        }

        return mRxCache;
    }

    public static void config(Builder builder) {

        if (mRxCache == null) {

            RxCache.mRxCache = builder.build();
        }
    }

    private RxCache(Builder builder) {

        cacheRepository = new CacheRepository(builder.memory, builder.persistence);
    }

    public <T> ObservableTransformer<T, Record<T>> transformObservable(final String key, final Type type, final ObservableStrategy strategy) {
        return new ObservableTransformer<T, Record<T>>() {
            @Override
            public ObservableSource<Record<T>> apply(Observable<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> FlowableTransformer<T, Record<T>> transformFlowable(final String key, final Type type, final FlowableStrategy strategy) {
        return new FlowableTransformer<T, Record<T>>() {
            @Override
            public Publisher<Record<T>> apply(Flowable<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> FlowableTransformer<T, Record<T>> transformFlowable(final String key, final Type type, final FlowableStrategy strategy, BackpressureStrategy backpressureStrategy) {
        return new FlowableTransformer<T, Record<T>>() {
            @Override
            public Publisher<Record<T>> apply(Flowable<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type, backpressureStrategy);
            }
        };
    }

    public <T> SingleTransformer<T, Record<T>> transformSingle(final String key, final Type type, final SingleStrategy strategy) {
        return new SingleTransformer<T, Record<T>>() {

            @Override
            public SingleSource<Record<T>> apply(Single<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> CompletableTransformer transformCompletable(final String key, final Type type, final CompletableStrategy strategy) {
        return new CompletableTransformer() {

            @Override
            public CompletableSource apply(Completable upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> MaybeTransformer<T, Record<T>> transformMaybe(final String key, final Type type, final MaybeStrategy strategy) {
        return new MaybeTransformer<T, Record<T>>() {

            @Override
            public MaybeSource<Record<T>> apply(Maybe<T> upstream) {
                return strategy.execute(RxCache.this, key, upstream, type);
            }
        };
    }

    public <T> Observable<Record<T>> load2Observable(final String key, final Type type) {

        Record<T> record = get(key, type);

        return record != null ? Observable.create(new ObservableOnSubscribe<Record<T>>() {
            @Override
            public void subscribe(ObservableEmitter<Record<T>> emitter) throws Exception {
                emitter.onNext(record);
                emitter.onComplete();
            }
        }) : Observable.empty();
    }

    public <T> Flowable<Record<T>> load2Flowable(final String key, final Type type) {

        return load2Flowable(key,type,BackpressureStrategy.MISSING);
    }

    public <T> Flowable<Record<T>> load2Flowable(final String key, final Type type, BackpressureStrategy backpressureStrategy) {

        Record<T> record = get(key, type);

        return record != null ? Flowable.create(new FlowableOnSubscribe<Record<T>>() {
            @Override
            public void subscribe(FlowableEmitter<Record<T>> emitter) throws Exception {
                emitter.onNext(record);
                emitter.onComplete();
            }
        },backpressureStrategy) : Flowable.empty();
    }

    public <T> Single<Record<T>> load2Single(final String key, final Type type) {

        Record<T> record = get(key, type);

        return record != null ? Single.create(new SingleOnSubscribe<Record<T>>() {
            @Override
            public void subscribe(SingleEmitter<Record<T>> emitter) throws Exception {
                emitter.onSuccess(record);
            }
        }) : Single.never();
    }

    public <T> Maybe<Record<T>> load2Maybe(final String key, final Type type) {

        Record<T> record = get(key, type);

        return record != null ? Maybe.create(new MaybeOnSubscribe<Record<T>>() {
            @Override
            public void subscribe(MaybeEmitter<Record<T>> emitter) throws Exception {
                emitter.onSuccess(record);
                emitter.onComplete();
            }
        }) : Maybe.empty();
    }

    /**
     * 从 RxCache 中获取一条记录，该记录返回是一个 Record 类型的对象
     * @param key  缓存的key
     * @param type 缓存所存储的类型
     * @param <T>
     * @return
     */
    public <T> Record<T> get(String key, Type type) {

        return cacheRepository.get(key,type,CacheStrategy.ALL);
    }

    /**
     * 根据相应的缓存策略，从 RxCache 中获取 Record
     * @param key   缓存的key
     * @param type  缓存所存储的类型
     * @param cacheStrategy 缓存策略
     * @param <T>
     * @return
     */
    public <T> Record<T> get(String key, Type type, CacheStrategy cacheStrategy) {

        return cacheRepository.get(key,type,cacheStrategy);
    }

    /**
     * 保存缓存
     * @param key   缓存的key
     * @param value 缓存的对象，需要序列化
     * @param <T>
     */
    public <T> void save(String key, T value) {

        cacheRepository.save(key, value);
    }

    /**
     * 保存缓存
     * @param key        缓存的key
     * @param value      缓存的对象，需要序列化
     * @param expireTime 过期时间，默认单位是毫秒
     * @param <T>
     */
    public <T> void save(String key, T value, long expireTime) {

        cacheRepository.save(key, value, expireTime);
    }

    /**
     * 保存缓存
     * @param key        缓存的key
     * @param value      缓存的对象，需要序列化
     * @param expireTime 过期时间
     * @param timeUnit   时间的单位，默认单位是毫秒
     * @param <T>
     */
    public <T> void save(String key, T value, long expireTime, TimeUnit timeUnit) {

        cacheRepository.save(key, value, expireTime,timeUnit);
    }

    /**
     * 保存缓存，只存于内存中
     * @param key   缓存的key
     * @param value 缓存的对象，需要序列化
     * @param <T>
     */
    public <T> void saveMemory(String key, T value) {

        cacheRepository.saveMemory(key, value);
    }

    /**
     * 保存缓存，只存于内存中
     * @param key        缓存的key
     * @param value      缓存的对象，需要序列化
     * @param expireTime 过期时间，默认单位是毫秒
     * @param <T>
     */
    public <T> void saveMemory(String key, T value, long expireTime) {

        cacheRepository.saveMemory(key, value, expireTime);
    }

    /**
     * 保存缓存，只存于内存中
     * @param key        缓存的key
     * @param value      缓存的对象，需要序列化
     * @param expireTime 过期时间
     * @param timeUnit   时间的单位，默认单位是毫秒
     * @param <T>
     */
    public <T> void saveMemory(String key, T value, long expireTime, TimeUnit timeUnit) {

        cacheRepository.saveMemory(key, value, expireTime,timeUnit);
    }

    /**
     * 更新缓存
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void update(String key, T value) {

        cacheRepository.update(key, value);
    }

    /**
     * 更新缓存的值、过期的时间
     * @param key
     * @param value
     * @param expireTime 过期时间，默认单位是毫秒
     * @param <T>
     */
    public <T> void update(String key, T value, long expireTime) {

        cacheRepository.update(key, value, expireTime);
    }

    /**
     * 更新缓存的值、过期的时间
     * @param key
     * @param value
     * @param expireTime 过期时间，默认单位是毫秒
     * @param timeUnit   时间的单位，默认单位是毫秒
     * @param <T>
     */
    public <T> void update(String key, T value, long expireTime, TimeUnit timeUnit) {

        cacheRepository.update(key, value, expireTime, timeUnit);
    }

    /**
     * 更新某一条 Record 的过期时间
     * @param key
     * @param type
     * @param expireTime 过期时间，默认单位是毫秒
     * @param <T>
     */
    public <T> void expire(String key, Type type, long expireTime) {

        cacheRepository.expire(key, type, expireTime);
    }

    /**
     * 更新某一条 Record 的过期时间
     * @param key
     * @param type
     * @param expireTime 过期时间
     * @param timeUnit   时间的单位，默认单位是毫秒
     * @param <T>
     */
    public <T> void expire(String key, Type type, long expireTime, TimeUnit timeUnit) {

        cacheRepository.expire(key, type, expireTime, timeUnit);
    }

    /**
     * 判断 RxCache 是否包含这个key
     * @param key
     * @return
     */
    public boolean containsKey(String key) {

        return cacheRepository.containsKey(key);
    }

    /**
     * 获取缓存中所有的key
     * @return
     */
    public Set<String> getAllKeys() {

        return cacheRepository.getAllKeys();
    }

    /**
     * 删除缓存中某个key
     * 如果 memory、persistence 中包含这个可以，则根据key来删除Value
     * @param key
     */
    public void remove(String key) {

        cacheRepository.remove(key);
    }

    /**
     * 删除缓存中多个key
     * @param keys
     */
    public void remove(String... keys) {

        cacheRepository.remove(keys);
    }

    /**
     * 某一条记录还剩下的存活时间
     * 没有某条记录，则返回-2
     * 记录永不过期，则返回-1
     * 记录已经过期，则返回0
     * 记录还未过期，则返回还余下的时间
     * @param key
     * @param type
     * @return
     */
    public long ttl(String key, Type type) {

        return cacheRepository.ttl(key,type);
    }

    /**
     * 清空缓存
     */
    public void clear() {
        cacheRepository.clear();
    }

    /**
     * 仅仅判断 RxCache 是否可用
     * @return
     */
    public boolean test() {

        return cacheRepository!=null;
    }

    /**
     * 显示缓存中的信息
     */
    public void info() {

        info(System.out);
    }

    /**
     * 显示缓存中的信息
     * @param out
     */
    public void info(PrintStream out) {

        out.println(cacheRepository.info());
    }

    public static final class Builder {

        private Memory memory;
        private Persistence persistence;

        public Builder memory(Memory memory) {
            this.memory = memory;
            return this;
        }

        public Builder persistence(Persistence persistence) {
            this.persistence = persistence;
            return this;
        }

        public RxCache build() {

            if (memory == null && persistence == null) { // 如果 memory 和 persistence 都为空

                memory = new FIFOMemoryImpl();           // memory 使用 FIFOMemoryImpl 作为默认实现，从而至少保证 RxCache 可用
            }

            return new RxCache(this);
        }
    }
}
