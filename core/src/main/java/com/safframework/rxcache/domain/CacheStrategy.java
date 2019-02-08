package com.safframework.rxcache.domain;

/**
 * 缓存存放的策略，缓存的对象支持存放到内存、持久层或者两者都存放该对象。
 * Created by tony on 2018-12-28.
 */
public enum CacheStrategy {

    MEMORY,
    PERSISTENCE,
    ALL
}
