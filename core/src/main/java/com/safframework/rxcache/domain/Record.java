package com.safframework.rxcache.domain;

import lombok.Data;

/**
 * 封装缓存的数据
 * Created by tony on 2018/9/28.
 */
@Data
public class Record<T> {

    private Source from;
    private String key;
    private T data;
    private long timestamp;

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
}
