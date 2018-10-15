package com.safframework.rxcache.persistence.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface Converter {

    <T> T read(InputStream source, Type type);

    void writer(OutputStream sink, Object data);

    <T> T fromJson(String json, Type type);

    String toJson(Object data);
}
