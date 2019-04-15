package com.safframework.rxcache.persistence.converter;

import com.safframework.rxcache.persistence.encrypt.Encryptor;
import com.safframework.rxcache.utils.GsonUtils;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/29.
 */
public class GsonConverter extends AbstractConverter {

    public GsonConverter() {
    }

    public GsonConverter(Encryptor encryptor) {
        super(encryptor);
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        return GsonUtils.fromJson(json, type);
    }

    @Override
    public String toJson(Object data) {
        return GsonUtils.toJson(data);
    }

    @Override
    public String converterName() {
        return "gson";
    }
}
