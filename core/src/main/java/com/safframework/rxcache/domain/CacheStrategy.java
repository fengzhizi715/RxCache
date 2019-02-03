package com.safframework.rxcache.domain;

/**
 * 缓存存放的策略，缓存支持存放到内存、持久层获取两者都存放。
 * Created by tony on 2018-12-28.
 */
public enum CacheStrategy {

    MEMORY,
    PERSISTENCE,
    ALL
}
