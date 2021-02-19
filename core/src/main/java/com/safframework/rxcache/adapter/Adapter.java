package com.safframework.rxcache.adapter;

import com.safframework.rxcache.RxCache;

/**
 * @FileName: com.safframework.rxcache.adapter.Adapter
 * @author: Tony Shen
 * @date: 2021-02-18 23:46
 * @version: V1.0 <描述当前版本功能>
 */
public interface Adapter {

    void interval(RxCache rxCache);

    void dispose();
}