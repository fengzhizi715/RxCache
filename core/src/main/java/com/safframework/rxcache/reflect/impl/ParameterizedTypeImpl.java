package com.safframework.rxcache.reflect.impl;

import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.log.LoggerProxy;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

/**
 * Created by tony on 2019-02-03.
 */
public class ParameterizedTypeImpl implements ParameterizedType {

    private final Class raw;
    private final Type[] args;
    private final Type owner;

    public ParameterizedTypeImpl(Class raw, Type[] args, Type owner) {

        this.raw = raw;
        this.args = args != null ? args : new Type[0];
        this.owner = owner;
        checkArgs();
    }

    private void checkArgs() {

        if (raw == null) {
            LoggerProxy.INSTANCE.getLogger().i("raw class can't be null", "rxcache");
            throw new RxCacheException("raw class can't be null");
        }

        TypeVariable[] typeParameters = raw.getTypeParameters();
        if (args.length != 0 && typeParameters.length != args.length) {
            String errorMsg = raw.getName() + " expect " + typeParameters.length + " arg(s), got " + args.length;
            LoggerProxy.INSTANCE.getLogger().i(errorMsg, "rxcache");
            throw new RxCacheException(errorMsg);
        }
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return raw;
    }

    @Override
    public Type getOwnerType() {
        return owner;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(raw.getName());
        if (args.length != 0) {
            sb.append('<');
            for (int i = 0; i < args.length; i++) {
                if (i != 0) {
                    sb.append(", ");
                }
                Type type = args[i];
                if (type instanceof Class) {
                    Class clazz = (Class) type;

                    if (clazz.isArray()) {
                        int count = 0;
                        do {
                            count++;
                            clazz = clazz.getComponentType();
                        } while (clazz.isArray());

                        sb.append(clazz.getName());

                        for (int j = count; j > 0; j--) {
                            sb.append("[]");
                        }
                    } else {
                        sb.append(clazz.getName());
                    }
                } else {
                    sb.append(args[i].toString());
                }
            }
            sb.append('>');
        }
        return sb.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ParameterizedTypeImpl that = (ParameterizedTypeImpl) o;

        if (!raw.equals(that.raw)) return false;
        // Probably incorrect - comparing Object[] arrays with Arrays.equals
        if (!Arrays.equals(args, that.args)) return false;
        return owner != null ? owner.equals(that.owner) : that.owner == null;

    }

    @Override
    public int hashCode() {
        int result = raw.hashCode();
        result = 31 * result + Arrays.hashCode(args);
        result = 31 * result + (owner != null ? owner.hashCode() : 0);
        return result;
    }
}
