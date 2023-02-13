package com.safframework.rxcache.reflect;

import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.log.LoggerProxy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by tony on 2019-02-03.
 */
public abstract class TypeToken<T> {

    private final Type type;

    public TypeToken() {

        Type superclass = getClass().getGenericSuperclass();
        if (superclass instanceof Class) {
            LoggerProxy.INSTANCE.getLogger().i("No generics found!", "rxcache");
            throw new RxCacheException("No generics found!");
        }
        ParameterizedType type = (ParameterizedType) superclass;
        this.type = type.getActualTypeArguments()[0];
    }

    public Type getType() {
        return type;
    }
}