package com.safframework.rxcache.persistence.converter;

import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * 对象转换的接口
 * Created by tony on 2018/9/28.
 */
public interface Converter {

    /**
     * 读取inputStrean流，并转换成对象
     * @param source
     * @param type
     * @param <T>
     * @return
     */
    <T> T read(InputStream source, Type type);

    /**
     * 将对象写入到outputStream流中
     * @param sink
     * @param data
     */
    void writer(OutputStream sink, Object data);

    /**
     * 将字符串转换成type类型的对象
     * @param json
     * @param type
     * @param <T>
     * @return
     */
    <T> T fromJson(String json, Type type);

    /**
     * 将对象序列化成字符串对象
     * @param data
     * @return
     */
    String toJson(Object data);

    /**
     * 用于标识哪个序列化框架实现序列化
     * @return
     */
    String converterName();
}
