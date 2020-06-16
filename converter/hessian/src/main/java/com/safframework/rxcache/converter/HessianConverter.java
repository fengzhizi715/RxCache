package com.safframework.rxcache.converter;

import com.safframework.bytekit.Bytes;
import com.safframework.bytekit.bytes.ByteBufferBytes;
import com.safframework.rxcache.persistence.converter.AbstractConverter;
import com.safframework.rxcache.persistence.encrypt.Encryptor;

import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Created by tony on 2019-04-17.
 */
public class HessianConverter extends AbstractConverter {

    public HessianConverter() {
    }

    public HessianConverter(Encryptor encryptor) {
        super(encryptor);
    }

    @Override
    public <T> T fromJson(String json, Type type) {

        return HessianUtils.deserialize(Bytes.parseBase64(json));
    }

    @Override
    public String toJson(Object data) {

        return new String(ByteBufferBytes.create(HessianUtils.serialize(data)).encodeBase64(), StandardCharsets.UTF_8);
    }
}
