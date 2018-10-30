package com.safframework.rxcache.proxy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.proxy.annotation.CacheKey;
import com.safframework.rxcache.proxy.annotation.CacheLifecycle;
import com.safframework.rxcache.proxy.annotation.CacheMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by tony on 2018/10/30.
 */
public class CacheProxy implements InvocationHandler {

    RxCache rxCache;

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        CacheMethod cacheMethod = method.getAnnotation(CacheMethod.class);
        CacheKey cacheKey = method.getAnnotation(CacheKey.class);
        CacheLifecycle cacheLifecycle = method.getAnnotation(CacheLifecycle.class);

        Annotation[][] allParamsAnnotations = method.getParameterAnnotations();

        long duration = 0;

        if (cacheLifecycle != null) {
            duration = cacheLifecycle.duration();
        }

        return null;
    }
}
