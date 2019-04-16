package com.safframework.rxcache.converter;

import com.safframework.rxcache.persistence.converter.AbstractConverter;
import org.nustaq.serialization.FSTConfiguration;

import java.lang.reflect.Type;

/**
 * Created by tony on 2019-04-16.
 */
public class FSTConverter extends AbstractConverter {

    static FSTConfiguration conf = FSTConfiguration.createJsonConfiguration();

    @Override
    public <T> T fromJson(String json, Type type) {
        return (T)conf.asObject(json.getBytes());
    }

    @Override
    public String toJson(Object data) {

        byte barray[] = conf.asByteArray(data);
        return new String(barray);
    }
}
