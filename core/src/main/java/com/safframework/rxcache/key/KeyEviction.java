package com.safframework.rxcache.key;

/**
 * @FileName: com.safframework.rxcache.key.KeyEviction
 * @author: Tony Shen
 * @date: 2021-02-09 21:14
 * @version: V1.0 key 淘汰的枚举，包含同步淘汰 key 和 异步淘汰 key
 */
public enum KeyEviction {
    SYNC, // 默认为 同步淘汰 key的方式，在获取缓存 key 的同时，会判断是否过期
    ASYNC // 异步淘汰 key 的方式: RxCache 内部定时判断所有的 key ，如果有过期的 key，则删除
}
