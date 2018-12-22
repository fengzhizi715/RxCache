package com.safframework.rxcache.offheap.memory;

import com.safframework.tony.common.utils.IOUtils;

import java.io.*;

/**
 * Created by tony on 2018-12-22.
 */
public class ConcurrentStringObjectDirectHashMap extends ConcurrentDirectHashMap<String,Object> {

    @Override
    protected byte[] convertObjectToBytes(Object value) {
        return serialize(value);
    }

    @Override
    protected Object convertBytesToObject(byte[] value) {
        return deserialize(value);
    }

    private byte[] serialize(Object obj) {
        byte[] result = null;
        ByteArrayOutputStream fos = null;

        try {
            fos = new ByteArrayOutputStream();
            ObjectOutputStream o = new ObjectOutputStream(fos);
            o.writeObject(obj);
            result = fos.toByteArray();
        } catch (IOException e) {
            System.err.println(e);
        } finally {

            IOUtils.closeQuietly(fos);
        }

        return result;
    }

    private Object deserialize(byte[] bytes) {

        InputStream fis = null;

        try {
            fis = new ByteArrayInputStream(bytes);
            ObjectInputStream o = new ObjectInputStream(fis);
            return o.readObject();
        } catch (IOException e) {
            System.err.println(e);
        } catch (ClassNotFoundException e) {
            System.err.println(e);
        } finally {

            IOUtils.closeQuietly(fis);
        }

        return null;
    }

}
