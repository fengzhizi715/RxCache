package com.safframework.rxcache.extra.memory;

import java.util.concurrent.TimeUnit;

/**
 * Created by tony on 2018/10/3.
 */
public class CacheConfig {

    public long expireDuration;
    public TimeUnit expireTimeUnit;
    public long refreshDuration;
    public TimeUnit refreshTimeUnit;
}
