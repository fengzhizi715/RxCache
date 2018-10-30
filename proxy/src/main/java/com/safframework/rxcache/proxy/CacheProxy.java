package com.safframework.rxcache.proxy;

import com.safframework.rxcache.RxCache;
import com.safframework.rxcache.proxy.annotation.*;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * Created by tony on 2018/10/30.
 */
public class CacheProxy implements InvocationHandler {

    RxCache rxCache;

    public CacheProxy(RxCache rxCache) {

        this.rxCache = rxCache;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        CacheMethod cacheMethod = method.getAnnotation(CacheMethod.class);
        CacheKey cacheKey = method.getAnnotation(CacheKey.class);
        CacheLifecycle cacheLifecycle = method.getAnnotation(CacheLifecycle.class);

        Annotation[][] allParamsAnnotations = method.getParameterAnnotations();

        Class cacheClazz = null;
        Object cacheValue = null;

        if (allParamsAnnotations != null) {
            for (int i = 0; i < allParamsAnnotations.length; i++) {
                Annotation[] paramAnnotations = allParamsAnnotations[i];
                if (paramAnnotations != null) {
                    for (Annotation annotation : paramAnnotations) {
                        if (annotation instanceof CacheClass) {
                            cacheClazz = (Class) args[i];
                        }

                        if (annotation instanceof CacheValue) {
                            cacheValue = args[i];
                        }
                    }
                }
            }
        }

        if (cacheMethod!=null) {

            MethodType methodType = cacheMethod.methodType();

            long duration = 0;

            if (cacheLifecycle != null) {
                duration = cacheLifecycle.duration();
            }

            if (methodType == MethodType.GET) {

                return  rxCache.get(cacheKey.value(),cacheClazz);

            } else if (methodType == MethodType.SAVE) {

                rxCache.save(cacheKey.value(),cacheValue,duration);

            } else if (methodType == MethodType.REMOVE) {

                rxCache.remove(cacheKey.value());
            }
        }

        return null;
    }
}
