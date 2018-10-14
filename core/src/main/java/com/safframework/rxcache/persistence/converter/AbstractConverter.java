package com.safframework.rxcache.persistence.converter;

import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.persistence.encrypt.Encryptor;
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
        ByteArrayOutputStream outputStream = null;
        try {

            outputStream = new ByteArrayOutputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while( (len = source.read(buffer)) !=-1 ){
                if (len!=0) {
                    outputStream.write(buffer, 0, len);
                }
            }

            json = new String(outputStream.toByteArray(), "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

            IOUtils.closeQuietly(outputStream);
        }

        if (encryptor!=null) {

            json = encryptor.decrypt(json);
        }

        return fromJson(json,type);
    }

    @Override
    public void writer(OutputStream sink, CacheHolder holder) {

        String wrapperJSONSerialized = toJson(holder);

        byte[] buffer = null;

        if (encryptor!=null) {

            String encryptResult = encryptor.encrypt(wrapperJSONSerialized);

            buffer = encryptResult.getBytes();
        } else {

            buffer = wrapperJSONSerialized.getBytes();
        }

        // sink 此时不必关闭，DiskImpl 会实现 sink 的关闭
        try {
            sink.write(buffer, 0, buffer.length);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
