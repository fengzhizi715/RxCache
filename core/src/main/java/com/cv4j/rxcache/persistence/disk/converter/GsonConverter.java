package com.cv4j.rxcache.persistence.disk.converter;

import com.google.gson.Gson;

import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/29.
 */
public class GsonConverter extends AbstractConverter {

    private Gson gson;

    public GsonConverter() {

        gson = new Gson();
    }

    @Override
    <T> T fromJson(String json, Type type) {
        return gson.fromJson(json, type);
    }

    @Override
    String toJson(Object data) {
        return gson.toJson(data);
    }
}
