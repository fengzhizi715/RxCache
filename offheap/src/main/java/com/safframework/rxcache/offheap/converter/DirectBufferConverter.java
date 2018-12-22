package com.safframework.rxcache.offheap.converter;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;

/**
 * Created by tony on 2018-12-21.
 */
public abstract class DirectBufferConverter<V> {

    public void dispose(ByteBuffer direct) {
        if(direct == null) return;

        Method cleaner = null;
        Method cleanerClean = null;
        try {
            cleaner = Class.forName("java.nio.DirectByteBuffer").getMethod("cleaner");
            cleaner.setAccessible(true);

            cleanerClean = Class.forName("sun.misc.Cleaner").getMethod("clean");
            cleanerClean.setAccessible(true);

            cleaner.invoke(cleaner.invoke(direct));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
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
