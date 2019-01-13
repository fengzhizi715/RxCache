package com.safframework.rxcache.domain;

import com.google.gson.Gson;
import com.safframework.rxcache.config.Constant;

/**
 * 封装缓存的数据，使用Immutable对象
 * Created by tony on 2018/9/28.
 */
public final class Record<T> {

    private final Source from;     // 缓存的来源
    private final String key;      // 缓存的key
    private final T data;          // 缓存的数据
    private final long createTime; // 缓存创建的时间
    private final long expireTime; // 缓存过期的时间

    public Record(Source from,String key,T value) {

        this(from,key,value,System.currentTimeMillis());
    }

    public Record(Source from,String key,T value,long createTime) {

        this(from,key,value,createTime, Constant.NEVER_EXPIRE);
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
     * 判断Record是否已经过期,true表示已经过期，false表示没有过期
     * @return
     */
    public boolean isExpired() {

        return createTime + expireTime < System.currentTimeMillis();
    }

    /**
     * 判断Record是否永不过期
     * @return
     */
    public boolean isNeverExpire() {

        return expireTime == Constant.NEVER_EXPIRE;
    }

    public long ttl() {

        if (isNeverExpire()) {

            return Constant.NEVER_EXPIRE;
        }

        if (isExpired()) {

            return Constant.HAS_EXPIRED;
        }

        return getExpireTime()- (System.currentTimeMillis() - getCreateTime());
    }

    public String toString() {

        return new Gson().toJson(this);
    }
}
