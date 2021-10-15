package com.safframework.rxcache.adapter;

import com.safframework.rxcache.RxCache;

/**
 * @FileName: com.safframework.rxcache.adapter.Adapter
 * @author: Tony Shen
 * @date: 2021-02-18 23:46
 * @version: V1.0 RxJava 的适配器，便于不同版本的 RxJava 使用
 */
public interface Adapter {

    void interval(RxCache rxCache);

    void dispose();
}