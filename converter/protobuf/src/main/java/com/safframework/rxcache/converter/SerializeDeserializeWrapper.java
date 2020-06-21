package com.safframework.rxcache.converter;

/**
 * @FileName: com.safframework.rxcache.converter.SerializeDeserializeWrapper
 * @author: Tony Shen
 * @date: 2020-06-21 22:43
 * @version: V1.0 <描述当前版本功能>
 */
public class SerializeDeserializeWrapper<T> {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public static <T> SerializeDeserializeWrapper<T> builder(T data) {
        SerializeDeserializeWrapper<T> wrapper = new SerializeDeserializeWrapper<>();
        wrapper.setData(data);
        return wrapper;
    }
}
