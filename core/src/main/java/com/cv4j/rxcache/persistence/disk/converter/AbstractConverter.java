package com.cv4j.rxcache.persistence.disk.converter;

import com.cv4j.rxcache.persistence.disk.encrypt.Encryptor;
import com.safframework.tony.common.utils.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * Created by tony on 2018/10/1.
 */
public abstract class AbstractConverter implements Converter {

    private Encryptor encryptor;

    public AbstractConverter() {
    }

    public AbstractConverter(Encryptor encryptor) {

        this.encryptor = encryptor;
    }

    @Override
    public <T> T read(InputStream source, Type type) {

        String json = null;
        ByteArrayOutputStream outSteam = null;
        try {

            outSteam = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len = source.read(buffer)) !=-1 ){
                if (len!=0) {
                    outSteam.write(buffer, 0, len);
                }
            }

            json = new String(outSteam.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            IOUtils.closeQuietly(outSteam);
        }

        if (encryptor!=null) {

            json = encryptor.decrypt(json);
        }

        return fromJson(json,type);
    }

    @Override
    public void writer(OutputStream sink, Object data) {

        String wrapperJSONSerialized = toJson(data);

        if (encryptor!=null) {

            wrapperJSONSerialized = encryptor.encrypt(wrapperJSONSerialized);
        }

        byte[] buffer = wrapperJSONSerialized.getBytes();

        // sink 此时不必关闭，DiskImpl 会实现 sink 的关闭
        try {
            sink.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected abstract <T> T fromJson(String json, Type type);

    protected abstract String toJson(Object data);
}
