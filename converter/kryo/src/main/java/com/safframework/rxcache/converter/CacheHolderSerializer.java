package com.safframework.rxcache.converter;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.safframework.rxcache.domain.CacheHolder;

/**
 * Created by tony on 2019-04-17.
 */
public class CacheHolderSerializer extends Serializer<CacheHolder> {

    @Override
    public void write(Kryo kryo, Output output, CacheHolder object) {
        output.writeString(object.getData());
        output.writeLong(object.getTimestamp());
        output.writeLong(object.getExpireTime());
        output.writeString(object.getConverterName());
    }

    @Override
    public CacheHolder read(Kryo kryo, Input input, Class<CacheHolder> type) {

        return new CacheHolder(input.readString(),input.readLong(),input.readLong(),input.readString());
    }
}
