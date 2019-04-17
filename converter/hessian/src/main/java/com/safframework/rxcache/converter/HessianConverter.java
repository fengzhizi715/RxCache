package com.safframework.rxcache.converter;

import com.safframework.bytekit.Bytes;
import com.safframework.bytekit.bytes.ByteBufferBytes;
import com.safframework.rxcache.persistence.converter.AbstractConverter;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;

/**
 * Created by tony on 2019-04-17.
 */
public class HessianConverter extends AbstractConverter {

    @Override
    public <T> T fromJson(String json, Type type) {

        try {
            return HessianUtils.deserialize(Bytes.parseBase64(json));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public String toJson(Object data) {

        try {
            return new String(ByteBufferBytes.create(HessianUtils.serialize(data)).encodeBase64(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
