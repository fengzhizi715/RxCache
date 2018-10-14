package com.safframework.rxcache.extra.converter;

import com.alibaba.fastjson.JSON;
import com.safframework.rxcache.persistence.converter.AbstractConverter;
import com.safframework.rxcache.persistence.encrypt.Encryptor;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/29.
 */
public class FastJSONConverter extends AbstractConverter {

    public FastJSONConverter() {
    }

    public FastJSONConverter(Encryptor encryptor) {
        super(encryptor);
    }

    @Override
    public <T> T fromJson(String json, Type type) {

        return JSON.parseObject(json, type);
    }

    @Override
    public String toJson(Object data) {

        return JSON.toJSONString(data);
    }
}
