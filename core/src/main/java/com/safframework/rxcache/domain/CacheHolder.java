package com.safframework.rxcache.domain;

import com.safframework.rxcache.config.Constant;

import java.io.Serializable;

/**
 * Created by tony on 2018/9/29.
 */
public class CacheHolder implements Serializable {

    private static final long serialVersionUID = -1795020035448398592L;

    public String data; // 对象转换的 json 字符串
    public long timestamp;
    public long expireTime;

    public CacheHolder(String data, long timestamp) {

        this(data,timestamp,Constant.NEVER_EXPIRE);
    }

    public CacheHolder(String data, long timestamp,long expireTime) {

        this.data = data;
        this.timestamp = timestamp;
        this.expireTime = expireTime;
    }
}
