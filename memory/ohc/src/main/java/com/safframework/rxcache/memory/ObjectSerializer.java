package com.safframework.rxcache.memory;

import com.safframework.bytekit.Bytes;
import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;

/**
 * @FileName: com.safframework.rxcache.memory.ObjectSerializer
 * @author: Tony Shen
 * @date: 2021/10/21 5:26 下午
 * @version: V1.0 <描述当前版本功能>
 */
public class ObjectSerializer<T> implements CacheSerializer<T> {

    @Override
    public void serialize(T value, ByteBuffer byteBuffer) {
        byte[] bytes = Bytes.serialize(value);
        byteBuffer.put(bytes);
    }

    @Override
    public T deserialize(ByteBuffer byteBuffer) {
        byte[] bytes = new byte[byteBuffer.capacity()];
        byteBuffer.get(bytes);
        return (T) Bytes.deserialize(bytes);
    }

    @Override
    public int serializedSize(T value) {
        byte[] bytes = Bytes.serialize(value);
        return bytes == null ? 0 : bytes.length;
    }
}
