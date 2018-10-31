package com.safframework.rxcache.proxy.annotation;

import com.safframework.rxcache.proxy.MethodType;
import com.safframework.rxcache.proxy.ObservableType;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Created by tony on 2018/10/31.
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface CacheMethod {

    MethodType methodType();

    ObservableType observableType() default ObservableType.NOUSE;
}
