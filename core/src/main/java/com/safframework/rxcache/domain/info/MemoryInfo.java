package com.safframework.rxcache.domain.info;

import com.safframework.rxcache.domain.CacheStatistics;

import java.util.Set;

/**
 * Created by tony on 2019-02-07.
 */
public class MemoryInfo {

    public Set<String> keys;                // 缓存包含的key

    public String memoryImpl;               // 缓存实现的类

    public CacheStatistics cacheStatistics; // 缓存的统计
}
