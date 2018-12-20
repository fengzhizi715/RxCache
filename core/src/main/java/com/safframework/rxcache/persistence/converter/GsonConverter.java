package com.safframework.rxcache.persistence.converter;

import com.google.gson.Gson;
import com.safframework.rxcache.persistence.encrypt.Encryptor;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/29.
 */
public class GsonConverter extends AbstractConverter {

    private Gson gson;

    public GsonConverter() {

        gson = new Gson();
    }

    public GsonConverter(Encryptor encryptor) {

        super(encryptor);
        gson = new Gson();
    }

    @Override
    public <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    @Override
    public String toJson(Object data) {
        return gson.toJson(data);
    }

    @Override
    public String converterName() {
        return "gson";
    }
}
