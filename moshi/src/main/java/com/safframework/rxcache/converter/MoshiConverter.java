package com.safframework.rxcache.converter;

import com.safframework.rxcache.persistence.converter.AbstractConverter;
import com.safframework.rxcache.persistence.encrypt.Encryptor;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;

import java.io.IOException;
import java.lang.reflect.Type;

/**
 * Created by tony on 2018/11/6.
 */
public class MoshiConverter extends AbstractConverter {

    private Moshi moshi = null;

    public MoshiConverter() {

        moshi = new Moshi.Builder().build();
    }

    public MoshiConverter(Encryptor encryptor) {

        super(encryptor);
        moshi = new Moshi.Builder().build();
    }

    @Override
    public <T> T fromJson(String json, Type type) {

        JsonAdapter<T> jsonAdapter = moshi.adapter(type);

        try {
            return jsonAdapter.fromJson(json);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toJson(Object data) {

        JsonAdapter jsonAdapter = moshi.adapter(data.getClass());

        return jsonAdapter.toJson(data);
    }

    @Override
    public String converterName() {
        return "moshi";
    }
}
