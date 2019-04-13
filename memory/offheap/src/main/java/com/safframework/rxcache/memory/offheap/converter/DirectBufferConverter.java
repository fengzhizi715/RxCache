package com.safframework.rxcache.memory.offheap.converter;

import java.nio.ByteBuffer;

/**
 * Created by tony on 2018-12-21.
 */
public abstract class DirectBufferConverter<V> {

    public void dispose(ByteBuffer direct) {

        Cleaner.clean(direct);
    }

    public ByteBuffer to(V from) {
        if(from == null) return null;

        byte[] bytes = toBytes(from);
        ByteBuffer.wrap(bytes);
        ByteBuffer bf = ByteBuffer.allocateDirect(bytes.length);
        bf.put(bytes);
        bf.flip();
        return bf;
    }

    abstract public byte[] toBytes(V value);

    abstract public V toObject(byte[] value);

    public V from(ByteBuffer to) {
        if(to == null) return null;

        byte[] bs = new byte[to.capacity()];
        to.get(bs);
        to.flip();
        return toObject(bs);
    }

}
