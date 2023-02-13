package com.safframework.rxcache;

import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.info.CacheInfo;
import com.safframework.rxcache.domain.CacheStrategy;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.key.KeyEviction;
import com.safframework.rxcache.log.Logger;
import com.safframework.rxcache.log.LoggerProxy;
import com.safframework.rxcache.memory.Memory;
import com.safframework.rxcache.persistence.Persistence;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.reflect.TypeUtils;
import com.safframework.rxcache.utils.GsonUtils;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Stream;

/**
 * Created by tony on 2018/9/28.
 */
class CacheRepository {

    private Memory memory;
    private Persistence persistence;
    private ConcurrentHashMap evictionPool;

    private final ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
    private final Lock readLock = lock.readLock();
    private final Lock writeLock = lock.writeLock();

    CacheRepository(Memory memory, Persistence persistence, KeyEviction keyEviction) {
        this.memory = memory;
        this.persistence = persistence;

        if (keyEviction == KeyEviction.ASYNC) {
            evictionPool = new ConcurrentHashMap<String,Type>();
        }
    }

    protected ConcurrentHashMap getEvictionPool() {
        return evictionPool;
    }

    protected <T> Record<T> get(String key, Type type, CacheStrategy cacheStrategy) {
        readLock.lock();

        try {
            Record<T> record = null;

            if (Preconditions.isNotBlanks(key, type)) {
                switch (cacheStrategy) {
                    case MEMORY: {
                        if (memory!=null) {
                            record = memory.getIfPresent(key);
                        }

                        break;
                    }

                    case PERSISTENCE: {
                        if (persistence!=null) {
                            record = persistence.retrieve(key, type);
                        }

                        break;
                    }

                    case ALL: {
                        if (memory != null) {
                            record = memory.getIfPresent(key);
                        }

                        if (record == null && persistence != null) {
                            record = persistence.retrieve(key, type);

                            if (memory!=null && record!=null && !record.isExpired()) { // 如果 memory 不为空，record 不为空，并且没有过期

                                readLock.unlock(); // 先释放读锁
                                writeLock.lock();  // 再获取写锁

                                try {
                                    if (record.isNeverExpire()) { // record永不过期的话，直接保存不需要计算ttl
                                        memory.put(record.getKey(),record.getData());
                                    } else {
                                        long ttl = record.getExpireTime()- (System.currentTimeMillis() - record.getCreateTime());
                                        memory.put(record.getKey(),record.getData(), ttl);
                                    }

                                    readLock.lock();    // 写锁在没有释放之前，获得读锁 (锁降级)
                                } finally {
                                    writeLock.unlock(); // 释放写锁
                                }
                            }
                        }
                        break;
                    }
                    default:
                        break;
                }
            }

            return record;
        } finally {
            readLock.unlock();
        }
    }

    protected String getStringData(String key) {
        readLock.lock();

        try {
            return Preconditions.isNotBlank(key) && persistence!=null ? persistence.getStringData(key) : null;
        } finally {
            readLock.unlock();
        }
    }

    protected String parseStringData(Converter converter, String data, Type type) {
        readLock.lock();

        try {
            return Preconditions.isNotBlanks(converter,data,type)? GsonUtils.toJson(converter.fromJson(data,type)) : null;
        } finally {
            readLock.unlock();
        }
    }

    protected <T> void save(String key, T value) {
        save(key,value, Constant.NEVER_EXPIRE);
    }

    /**
     * 保存缓存，并设置过期的时间
     * @param key
     * @param value
     * @param expireTime 过期的时间单位是毫秒
     * @param <T>
     */
    protected <T> void save(String key, T value, long expireTime) {
        save(key,value,expireTime,TimeUnit.MILLISECONDS);
    }

    protected <T> void save(String key, T value, long expireTime, TimeUnit timeUnit) {
        writeLock.lock();

        try {
            if (Preconditions.isNotBlanks(key, value)) {

                if (expireTime>0) {
                    long newExpireTime = timeUnit.toMillis(expireTime);

                    if (memory != null) {
                        memory.put(key, value, newExpireTime);
                    }

                    if (persistence != null) {
                        persistence.save(key, value, newExpireTime);
                    }

                    if (evictionPool !=null) {
                        evictionPool.put(key, TypeUtils.getRawType(value.getClass()));
                    }
                } else {
                    if (memory != null) {
                        memory.put(key, value);
                    }

                    if (persistence != null) {
                        persistence.save(key, value);
                    }
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    /**
     * 缓存的数据只保存在内存中（除非必要，否则建议内存、持久层同时保存数据）
     * @param key
     * @param value
     * @param <T>
     */
    protected <T> void saveMemory(String key, T value) {
        saveMemory(key,value, Constant.NEVER_EXPIRE);
    }

    protected <T> void saveMemory(String key, T value, long expireTime) {
        saveMemory(key,value,expireTime,TimeUnit.MILLISECONDS);
    }

    protected <T> void saveMemory(String key, T value, long expireTime, TimeUnit timeUnit) {
        writeLock.lock();

        try {
            if (Preconditions.isNotBlanks(key, value)) {

                if (expireTime>0) {
                    long newExpireTime = timeUnit.toMillis(expireTime);

                    if (memory != null) {
                        memory.put(key, value, newExpireTime);
                    }

                    if (evictionPool !=null) {
                        evictionPool.put(key, TypeUtils.getRawType(value.getClass()));
                    }
                } else {
                    if (memory != null) {
                        memory.put(key, value);
                    }
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected <T> void update(String key, T value) {
        update(key,value, Constant.NEVER_EXPIRE);
    }

    protected <T> void update(String key, T value, long expireTime) {
        update(key,value, expireTime, TimeUnit.MILLISECONDS);
    }

    protected <T> void update(String key, T value, long expireTime, TimeUnit timeUnit) {
        writeLock.lock();

        try {
            long newExpireTime = 0;
            if (expireTime>0) {
                newExpireTime = timeUnit.toMillis(expireTime);
            }

            remove(key);                       // 由于 record 是不可变对象，所以先删除。（此时并没有先释放写锁，因为写锁是可重入锁，所以不需要释放写锁）

            if (newExpireTime>0) {
                save(key,value,newExpireTime); // 再保存
            } else {
                save(key,value);               // 再保存
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected <T> void saveOrUpdate(String key, T value) {
        saveOrUpdate(key,value, Constant.NEVER_EXPIRE);
    }

    protected <T> void saveOrUpdate(String key, T value, long expireTime) {
        saveOrUpdate(key,value, expireTime, TimeUnit.MILLISECONDS);
    }

    protected <T> void saveOrUpdate(String key, T value, long expireTime, TimeUnit timeUnit) {
        writeLock.lock();

        try {
            if ((memory != null && memory.containsKey(key)) || (persistence != null && persistence.containsKey(key))) { // rxCache 里包含了 key，则更新
                update(key,value,expireTime,timeUnit); // 因为写锁是可重入锁，所以不需要释放写锁
            } else { // rxCache 里没有该 key，则保存
                save(key,value,expireTime,timeUnit);
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected <T> void expire(String key, Type type, long expireTime) {
        expire(key,type,expireTime,TimeUnit.MILLISECONDS);
    }

    protected <T> void expire(String key, Type type, long expireTime, TimeUnit timeUnit) {
        writeLock.lock();

        try {
            long newExpireTime = 0;
            if (expireTime>0) {
                newExpireTime = timeUnit.toMillis(expireTime);
            }

            Record<T> record = get(key,type,CacheStrategy.ALL);

            if (record!=null && record.getData()!=null) {
                T value = record.getData();
                remove(key);

                if (newExpireTime>0) {
                    save(key,value,expireTime);
                } else {
                    save(key,value);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected boolean containsKey(String key) {
        readLock.lock();

        try {
            return Preconditions.isBlank(key) ? false : (memory != null && memory.containsKey(key)) || (persistence != null && persistence.containsKey(key));
        } finally {
            readLock.unlock();
        }
    }

    protected boolean checkKey(String key) {
        try {
            return getStringData(key)!=null;
        } catch (Exception e){
            if (LoggerProxy.INSTANCE.getLogger()!=null) {
                LoggerProxy.INSTANCE.getLogger().e("checkKey is failed...", "rxcache", e.getCause());
            }
            return false;
        }
    }

    protected Set<String> getAllKeys() {
        readLock.lock();

        try {
            Set<String> result = new HashSet<>();

            if (memory != null) {
                result.addAll(memory.keySet());
            }

            if (persistence != null) {
                result.addAll(persistence.keySet());
            }

            return result;
        } finally {
            readLock.unlock();
        }
    }

    protected void remove(String key) {
        writeLock.lock();

        try {
            if (Preconditions.isNotBlank(key)) {

                if (memory != null) {
                    memory.evict(key);
                }

                if (persistence != null) {
                    persistence.evict(key);
                }
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected void remove(String... keys) {
        writeLock.lock();

        try {
            if (Preconditions.isNotBlank(keys)) {

                Stream.of(keys).forEach(key -> {

                    if (memory != null) {
                        memory.evict(key);
                    }

                    if (persistence != null) {
                        persistence.evict(key);
                    }
                });
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected long ttl(String key, Type type) {
        readLock.lock();

        try {
            Record record = null;

            if (Preconditions.isNotBlanks(key, type)) {

                if (memory != null) {

                    record =  memory.getIfPresent(key);
                }

                if (record == null && persistence != null) {

                    record = persistence.retrieve(key, type);
                }
            }

            return record == null ? Constant.NO_RECORD : record.ttl();
        } finally {
            readLock.unlock();
        }
    }

    protected void clear() {
        writeLock.lock();

        try {
            if (memory != null) {
                memory.evictAll();
            }

            if (persistence != null) {
                persistence.evictAll();
            }
        } finally {
            writeLock.unlock();
        }
    }

    protected String info() {
        readLock.lock();

        try {
            CacheInfo cacheInfo = new CacheInfo.Builder()
                    .memory(memory)
                    .persistence(persistence)
                    .build();

            return cacheInfo.toString();
        } finally {
            readLock.unlock();
        }
    }
}
