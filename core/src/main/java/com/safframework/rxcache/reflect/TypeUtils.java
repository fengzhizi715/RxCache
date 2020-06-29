package com.safframework.rxcache.reflect;

import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

/**
 * @FileName: com.safframework.rxcache.reflect.TypeUtils
 * @author: Tony Shen
 * @date: 2020-06-26 12:38
 * @version: V1.6.6
 */
public class TypeUtils {

    public static Class<?> getRawType(Type type) {

        return TypeToken.get(type).getRawType();
    }

    public static <T> String getClassSimpleName(T t) {

        return t.getClass().getSimpleName();
    }
}