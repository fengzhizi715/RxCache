package com.safframework.rxcache;

import com.safframework.rxcache.adapter.Adapter;
import com.safframework.rxcache.domain.CacheStrategy;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.key.KeyEviction;
import com.safframework.rxcache.log.Logger;
import com.safframework.rxcache.log.LoggerProxy;
import com.safframework.rxcache.memory.Memory;
import com.safframework.rxcache.memory.impl.FIFOMemoryImpl;
import com.safframework.rxcache.persistence.Persistence;
import com.safframework.rxcache.persistence.converter.Converter;

import java.io.PrintStream;
import java.lang.reflect.Type;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

/**
 * RxCache 是单例，使用时需要先调用 config() 配置 RxCache
 * Created by tony on 2018/9/28.
 */
public final class RxCache {

    private final CacheRepository cacheRepository;
    private static RxCache mRxCache;
    private Adapter adapter;

    public static RxCache getRxCache() {
        if (mRxCache == null) {
            mRxCache = new RxCache.Builder().build();
        }

        return mRxCache;
    }

    /**
     * 配置 RxCache，配置一次即可，以后无需再配置。只有配置完了 RxCache，才可以使用。
     * @param builder
     */
    public synchronized static void config(Builder builder) {
        if (mRxCache == null) {
            RxCache.mRxCache = builder.build();
        }
    }

    /**
     * 配置 RxCache，配置一次即可，以后无需再配置。只有配置完了 RxCache，才可以使用。
     * 方便使用 Kotlin 时，可以使用 dsl 来配置 RxCache
     * @param builder
     */
    public synchronized static void config(Supplier<Builder> builder) {
        if (mRxCache == null) {
            RxCache.mRxCache = builder.get().build();
        }
    }

    private RxCache(Builder builder) {
        cacheRepository = new CacheRepository(builder.memory, builder.persistence, builder.keyEviction);
        adapter = builder.adapter;

        LoggerProxy.INSTANCE.initLogger(builder.log);

        if (builder.keyEviction == KeyEviction.ASYNC && adapter!=null) {
            adapter.interval(this);
        }
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
     * 基于缓存存放的策略，获取 RxCache 中一条记录，该记录返回是一个 Record 类型的对象
     * @param key  缓存的key
     * @param type 缓存所存储的类型
     * @param cacheStrategy 缓存存放的策略
     * @param <T>
     * @return
     */
    public <T> Record<T> get(String key, Type type, CacheStrategy cacheStrategy) {
        return cacheRepository.get(key,type,cacheStrategy);
    }

    /**
     * 从 RxCache 中获取一条记录(并不是 Record 这个真实缓存内容的包装类，而是 Record 对应的 data)，只支持 persistence 对象
     * 该记录返回是一个 json 对象或者是一个 Base64 加密过的字符串
     * @param key
     * @return
     */
    public String getStringData(String key) {
        return cacheRepository.getStringData(key);
    }

    /**
     * 可以解析 getStringData() 返回的内容，转换成 json 字符串
     * @param converter
     * @param data
     * @param type
     * @return
     */
    public String parseStringData(Converter converter, String data, Type type) {
        return cacheRepository.parseStringData(converter, data, type);
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
     * 保存缓存只存于内存中，所以需要确保有 memory 的实现，否则缓存数据保存不了。
     * @param key   缓存的key
     * @param value 缓存的对象，需要序列化
     * @param <T>
     */
    public <T> void saveMemory(String key, T value) {
        cacheRepository.saveMemory(key, value);
    }

    /**
     * 保存缓存只存于内存中，所以需要确保有 memory 的实现，否则缓存数据保存不了。
     * @param key        缓存的key
     * @param value      缓存的对象，需要序列化
     * @param expireTime 过期时间，默认单位是毫秒
     * @param <T>
     */
    public <T> void saveMemory(String key, T value, long expireTime) {
        cacheRepository.saveMemory(key, value, expireTime);
    }

    /**
     * 保存缓存只存于内存中，所以需要确保有 memory 的实现，否则缓存数据保存不了。
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
     * 保存或更新缓存
     * @param key
     * @param value
     * @param <T>
     */
    public <T> void saveOrUpdate(String key, T value) {
        cacheRepository.saveOrUpdate(key, value);
    }

    /**
     * 保存或更新缓存的值、过期的时间
     * @param key
     * @param value
     * @param expireTime 过期时间，默认单位是毫秒
     * @param <T>
     */
    public <T> void saveOrUpdate(String key, T value, long expireTime) {
        cacheRepository.saveOrUpdate(key, value, expireTime);
    }

    /**
     * 保存或更新缓存的值、过期的时间
     * @param key
     * @param value
     * @param expireTime 过期时间，默认单位是毫秒
     * @param timeUnit   时间的单位，默认单位是毫秒
     * @param <T>
     */
    public <T> void saveOrUpdate(String key, T value, long expireTime, TimeUnit timeUnit) {
        cacheRepository.saveOrUpdate(key, value, expireTime, timeUnit);
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
     * 校验 RxCache 是否可以从 persistence 中获取这个 key
     * 获取失败会抛出 RxCacheException
     * @param key
     * @throws RxCacheException
     * @return
     */
    public boolean checkKey(String key) throws RxCacheException {
        return cacheRepository.checkKey(key);
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

    /**
     * 获取缓存中的信息
     * @return
     */
    public String getInfo() {
        return cacheRepository.info();
    }

    /**
     *
     * @return
     */
    public ConcurrentHashMap getEvictionPool() {
        return cacheRepository.getEvictionPool();
    }

    /**
     * RxCache 不再使用时，需要释放资源，并清空缓存
     */
    public void dispose() {
        if (adapter!=null) {
            adapter.dispose();
        }

        clear();
    }

    public static final class Builder {
        private Memory memory;
        private Persistence persistence;
        private Logger log;
        private KeyEviction keyEviction;
        private Adapter adapter;

        public Builder memory(Memory memory) {
            this.memory = memory;
            return this;
        }

        public Builder persistence(Persistence persistence) {
            this.persistence = persistence;
            return this;
        }

        public Builder log(Logger logger) {
            this.log = logger;
            return this;
        }

        public Builder keyEviction(KeyEviction keyEviction) {
            this.keyEviction = keyEviction;
            return this;
        }

        public Builder adapter(Adapter adapter) {
            this.adapter = adapter;
            return this;
        }

        public RxCache build() {
            if (memory == null && persistence == null) { // 如果 memory 和 persistence 都为空
                memory = new FIFOMemoryImpl();           // memory 使用 FIFOMemoryImpl 作为默认实现，从而至少保证 RxCache 可用
            }

            if (keyEviction == null) {
                keyEviction = KeyEviction.SYNC; // 默认情况，使用同步删除淘汰 key 的方式
            }

            if (log == null) { // 默认情况，设置 Logger
                log = new Logger() {
                    @Override
                    public void i(String msg, String tag) {
                        System.out.println(tag + " " + msg);
                    }

                    @Override
                    public void v(String msg, String tag) {
                        System.out.println(tag + " " + msg);
                    }

                    @Override
                    public void d(String msg, String tag) {
                        System.out.println(tag + " " + msg);
                    }

                    @Override
                    public void w(String msg, String tag, Throwable tr) {
                        tr.printStackTrace();
                        System.out.println(tag + " " + msg);
                    }

                    @Override
                    public void e(String msg, String tag, Throwable tr) {
                        tr.printStackTrace();
                        System.err.println(tag + " " + msg);
                    }
                };
            }

            return new RxCache(this);
        }
    }
}