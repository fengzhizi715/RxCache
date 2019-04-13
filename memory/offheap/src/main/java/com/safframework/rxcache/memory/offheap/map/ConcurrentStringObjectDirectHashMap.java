package com.safframework.rxcache.memory.offheap.map;

import com.safframework.bytekit.Bytes;

/**
 * Created by tony on 2018-12-22.
 */
public class ConcurrentStringObjectDirectHashMap extends ConcurrentDirectHashMap<String,Object> {

    @Override
    protected byte[] convertObjectToBytes(Object value) {

        return Bytes.serialize(value);
    }

    @Override
    protected Object convertBytesToObject(byte[] value) {

        return Bytes.deserialize(value);
    }
}
