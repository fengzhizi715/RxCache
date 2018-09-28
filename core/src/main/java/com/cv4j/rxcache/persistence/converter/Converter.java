package com.cv4j.rxcache.persistence.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Created by tony on 2018/9/28.
 */
public interface Converter {

    /**
     * 读取
     *
     * @param source
     * @return
     */
    <T> T read(InputStream source, Type type);

    /**
     * 写入
     *
     * @param sink
     * @param data
     * @return
     */
    boolean writer(OutputStream sink, Object data);
}
