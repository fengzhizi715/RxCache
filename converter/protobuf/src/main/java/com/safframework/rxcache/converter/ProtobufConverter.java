package com.safframework.rxcache.converter;

import com.google.gson.reflect.TypeToken;
import com.safframework.bytekit.Bytes;
import com.safframework.bytekit.bytes.ByteBufferBytes;
import com.safframework.rxcache.persistence.converter.AbstractConverter;
import com.safframework.rxcache.persistence.encrypt.Encryptor;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * @FileName: com.safframework.rxcache.converter.ProtobufConveerter
 * @author: Tony Shen
 * @date: 2020-06-21 12:23
 * @version: V1.0 <描述当前版本功能>
 */
public class ProtobufConverter extends AbstractConverter {

    public ProtobufConverter() {
    }

    public ProtobufConverter(Encryptor encryptor) {
        super(encryptor);
    }

    @Override
    public <T> T fromJson(String json, Type type) {

        return (T) ProtostuffUtils.deserialize(Bytes.parseBase64(json), TypeToken.get(type).getRawType());
    }

    @Override
    public String toJson(Object data) {

        return new String(ByteBufferBytes.create(ProtostuffUtils.serialize(data)).encodeBase64(), StandardCharsets.UTF_8);
    }
}
