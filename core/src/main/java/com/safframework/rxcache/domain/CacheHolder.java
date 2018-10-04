package com.safframework.rxcache.domain;

import com.safframework.rxcache.config.Constant;

/**
 * Created by tony on 2018/9/29.
 */
public class CacheHolder<T> {

    public T data;
    public long timestamp;
    public long expireTime;

    public CacheHolder(T data, long timestamp) {

        this(data,timestamp,Constant.NEVER_EXPIRE);
    }

    public CacheHolder(T data, long timestamp,long expireTime) {
        this.data = data;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
    }
}
