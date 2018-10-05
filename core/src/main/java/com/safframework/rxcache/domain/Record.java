package com.safframework.rxcache.domain;

import com.safframework.rxcache.config.Constant;

/**
 * 封装缓存的数据
 * Created by tony on 2018/9/28.
 */
public class Record<T> {

    private Source from;     // 缓存的来源
    private String key;      // 缓存的key
    private T data;          // 缓存的数据
    private long createTime; // 缓存创建的时间
    private long expireTime; // 缓存过期的时间

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

    public void setFrom(Source from) {
        this.from = from;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getExpireTime() {
        return expireTime;
    }

    public void setExpireTime(long expireTime) {
        this.expireTime = expireTime;
    }

    /**
     * 缓存是否过期
     * @return
     */
    public boolean isExpired() {

        return createTime + expireTime < System.currentTimeMillis();
    }
}
