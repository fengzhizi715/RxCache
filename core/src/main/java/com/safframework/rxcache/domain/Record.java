package com.safframework.rxcache.domain;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.utils.GsonUtils;
import com.safframework.rxcache.utils.SystemClock;

/**
 * 封装缓存的数据，使用 Immutable 对象，保证线程安全
 * Created by tony on 2018/9/28.
 */
public final class Record<T> {

    private final Source from;     // 缓存的来源
    private final String key;      // 缓存的key
    private final T data;          // 缓存的数据
    private final long createTime; // 缓存创建的时间
    private final long expireTime; // 缓存过期的时间

    public Record(Source from,String key,T value) {

        this(from, key, value, SystemClock.now());
    }

    public Record(Source from,String key,T value,long createTime) {

        this(from, key, value, createTime, Constant.NEVER_EXPIRE);
    }

    public Record(Source from,String key,T value,long createTime,long expireTime) {

        this.from = from;
        this.key = key;
        this.data = value;
        this.createTime = createTime;
        this.expireTime = expireTime;
    }

    public Source getFrom() {
        return from;
    }

    public String getKey() {
        return key;
    }

    public T getData() {
        return data;
    }

    public long getCreateTime() {
        return createTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    /**
     * 判断 Record 是否已经过期,true 表示已经过期，false 表示没有过期
     * @return
     */
    public boolean isExpired() {

        if (isNeverExpire()) {

            return false;
        }

        return createTime + expireTime < SystemClock.now();
    }

    /**
     * 判断 Record 是否永不过期
     * @return
     */
    public boolean isNeverExpire() {

        return expireTime == Constant.NEVER_EXPIRE;
    }

    /**
     * 当前 Record 还剩下多久的存活时间
     * @return
     */
    public long ttl() {

        if (isNeverExpire()) {

            return Constant.NEVER_EXPIRE;
        }

        if (createTime + expireTime < SystemClock.now()) {

            return Constant.HAS_EXPIRED;
        }

        return getExpireTime() - (SystemClock.now() - getCreateTime());
    }

    public String toString() {

        return GsonUtils.toJson(this);
    }
}
