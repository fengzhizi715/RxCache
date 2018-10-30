package com.safframework.rxcache.proxy;

import java.lang.reflect.Proxy;

/**
 * Created by tony on 2018/10/30.
 */
public final class CacheProvider {

    public static <T> T create(Class<T> clazz) {

        CacheProxy proxy = new CacheProxy();
        try {
            return (T) Proxy.newProxyInstance(CacheProvider.class.getClassLoader(), new Class[]{clazz}, proxy);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
