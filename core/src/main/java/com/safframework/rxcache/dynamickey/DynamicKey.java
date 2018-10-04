package com.safframework.rxcache.dynamickey;

import java.lang.reflect.Method;

/**
 * Created by tony on 2018/10/4.
 */
public interface DynamicKey {

    String generateCacheKey(Object target, Method method, Object... params);
}
