package com.safframework.rxcache.converter;

import com.safframework.rxcache.persistence.converter.AbstractConverter;
import com.safframework.rxcache.persistence.encrypt.Encryptor;

import java.lang.reflect.Type;

/**
 * Created by tony on 2019-04-17.
 */
public class KryoConverter extends AbstractConverter {

    public KryoConverter() {
    }

    public KryoConverter(Encryptor encryptor) {
        super(encryptor);
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        return KryoUtils.readFromString(json);
    }

    @Override
    public String toJson(Object data) {
        return KryoUtils.writeToString(data);
    }
}
