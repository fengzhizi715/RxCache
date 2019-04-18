package com.safframework.rxcache.proxy;

import com.safframework.rxcache.RxCache;

import java.lang.reflect.Proxy;

/**
 * Created by tony on 2018/10/30.
 */
public final class CacheProvider {

    private RxCache rxCache;

    private CacheProvider(CacheProvider.Builder builder) {

        this.rxCache = builder.rxCache;
    }

    public <T> T create(Class<T> clazz) {

        CacheProxy cacheProxy = new CacheProxy(rxCache);

        try {
            return (T) Proxy.newProxyInstance(CacheProvider.class.getClassLoader(), new Class[]{clazz}, cacheProxy);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    public static final class Builder {

        private RxCache rxCache;

        public CacheProvider.Builder rxCache(RxCache rxCache) {

            this.rxCache = rxCache;
            return this;
        }

        public CacheProvider build() {

            return new CacheProvider(this);
        }
    }
}
