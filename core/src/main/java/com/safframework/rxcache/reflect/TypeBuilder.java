package com.safframework.rxcache.reflect;

import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.log.LoggerProxy;
import com.safframework.rxcache.reflect.impl.ParameterizedTypeImpl;
import com.safframework.rxcache.reflect.impl.WildcardTypeImpl;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2019-02-03.
 */
public class TypeBuilder {

    private final TypeBuilder parent;
    private final Class raw;
    private final List<Type> args = new ArrayList<>();


    private TypeBuilder(Class raw, TypeBuilder parent) {
        this.raw = raw;
        this.parent = parent;
    }

    public static TypeBuilder newInstance(Class raw) {
        return new TypeBuilder(raw, null);
    }

    private static TypeBuilder newInstance(Class raw, TypeBuilder parent) {
        return new TypeBuilder(raw, parent);
    }

    public TypeBuilder beginSubType(Class raw) {
        return newInstance(raw, this);
    }

    public TypeBuilder endSubType() {

        if (parent == null) {
            LoggerProxy.INSTANCE.getLogger().i("expect beginSubType() before endSubType()", "rxcache");
            throw new RxCacheException("expect beginSubType() before endSubType()");
        }

        parent.addTypeParam(getType());

        return parent;
    }

    public TypeBuilder addTypeParam(Class clazz) {
        return addTypeParam((Type) clazz);
    }

    public TypeBuilder addTypeParamExtends(Class... classes) {

        if (classes == null) {
            LoggerProxy.INSTANCE.getLogger().i("addTypeParamExtends() expect not null Class", "rxcache");
            throw new RxCacheException("addTypeParamExtends() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(null, classes);

        return addTypeParam(wildcardType);
    }

    public TypeBuilder addTypeParamSuper(Class... classes) {

        if (classes == null) {
            LoggerProxy.INSTANCE.getLogger().i("addTypeParamSuper() expect not null Class", "rxcache");
            throw new RxCacheException("addTypeParamSuper() expect not null Class");
        }

        WildcardTypeImpl wildcardType = new WildcardTypeImpl(classes, null);

        return addTypeParam(wildcardType);
    }

    public TypeBuilder addTypeParam(Type type) {

        if (type == null) {
            LoggerProxy.INSTANCE.getLogger().i("addTypeParam expect not null Type", "rxcache");
            throw new RxCacheException("addTypeParam expect not null Type");
        }

        args.add(type);

        return this;
    }

    public Type build() {

        if (parent != null) {
            LoggerProxy.INSTANCE.getLogger().i("expect endSubType() before build()", "rxcache");
            throw new RxCacheException("expect endSubType() before build()");
        }

        return getType();
    }

    private Type getType() {

        if (args.isEmpty()) {
            return raw;
        }

        return new ParameterizedTypeImpl(raw, args.toArray(new Type[args.size()]), null);
    }
}
