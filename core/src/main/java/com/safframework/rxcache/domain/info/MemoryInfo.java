package com.safframework.rxcache.domain.info;

import com.safframework.rxcache.domain.CacheStatistics;

import java.util.Set;

/**
 * Created by tony on 2019-02-07.
 */
public class MemoryInfo {

    public Set<String> keys;

    public String memoryImpl;

    public CacheStatistics cacheStatistics;
}
