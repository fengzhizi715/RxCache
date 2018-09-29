package com.cv4j.rxcache.domain;

/**
 * Created by tony on 2018/9/29.
 */
public class CacheHolder<T> {

    public T data;
    public long timestamp;

    public CacheHolder(T data, long timestamp) {
        this.data = data;
        this.timestamp = timestamp;
    }
}
