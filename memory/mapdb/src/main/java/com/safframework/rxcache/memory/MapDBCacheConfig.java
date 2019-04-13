package com.safframework.rxcache.memory;

import java.util.concurrent.TimeUnit;

/**
 * Created by tony on 2019-01-09.
 */
public class MapDBCacheConfig extends CacheConfig {

    public long expireAfterGetDuration;
    public TimeUnit expireAfterGetTimeUnit;
}
