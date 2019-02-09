package com.safframework.rxcache.converter;

import com.safframework.rxcache.persistence.converter.AbstractConverter;
import com.safframework.rxcache.persistence.encrypt.Encryptor;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.Types;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

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

        if (data instanceof List) {
            if (((List) data).size() >0){
                Type type = Types.newParameterizedType(List.class, ((List) data).get(0).getClass());
                JsonAdapter jsonAdapter = moshi.adapter(type);
                return jsonAdapter.toJson(data);
            } else {
                return "";
            }

        } else {
            JsonAdapter jsonAdapter = moshi.adapter(data.getClass());
            return jsonAdapter.toJson(data);
        }

    }

    @Override
    public String converterName() {
        return "moshi";
    }
}
