package com.safframework.rxcache.domain;

import java.io.Serializable;

/**
 * Created by tony on 2018/9/29.
 */
public final class CacheHolder implements Serializable {

    private static final long serialVersionUID = -1795020035448398592L;

    private final String data; // 对象转换的 json 字符串
    private final long timestamp;
    private final long expireTime;
    private final String converterName;

    public CacheHolder(String data, long timestamp,long expireTime,String converterName) {

        this.data = data;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
        this.converterName = converterName;
    }

    public String getData() {
        return data;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public String getConverterName() {
        return converterName;
    }
}
