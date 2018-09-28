package com.cv4j.rxcache.domain;

import com.cv4j.rxcache.Source;

/**
 * Created by tony on 2018/9/28.
 */
public class Record<T> {

    private Source from;
    private String key;
    private T data;
    private long timestamp;
}
