package com.safframework.rxcache.domain;

import lombok.Data;

/**
 * 封装缓存的数据
 * Created by tony on 2018/9/28.
 */
@Data
public class Record<T> {

    private Source from;    // 缓存的来源
    private String key;     // 缓存的key
    private T data;         // 缓存的数据
    private long timestamp; // 缓存创建的时间
    private long expireTime;// 缓存过期的时间

    public Record(Source from,String key,T value) {

        this.from = from;
        this.key = key;
        this.data = value;
    }

    public Record(Source from,String key,T value,long timestamp) {

        this.from = from;
        this.key = key;
        this.data = value;
        this.timestamp = timestamp;
    }

    public Record(Source from,String key,T value,long timestamp,long expireTime) {

        this.from = from;
        this.key = key;
        this.data = value;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
    }
}
