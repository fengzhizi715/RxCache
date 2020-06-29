package com.safframework.rxcache.persistence.converter;

import com.safframework.bytekit.utils.IOUtils;
import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.persistence.encrypt.Encryptor;
import com.safframework.rxcache.reflect.TypeUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Type;

/**
 * 抽象的对象转换类，实现了 Converter 接口 的 read、write 方法，支持加密、解密
 * 而 fromJson、toJson 方法需要 AbstractConverter 的继承者来实现
 * （当然，开发者也可以自己实现 Converter 接口，不必继承 AbstractConverter 类）
 *
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

        if (Preconditions.isBlank(source) || Preconditions.isBlank(type)) {

            return null;
        }

        String json = null;
        ByteArrayOutputStream outputStream = null;
        try {
            outputStream = new ByteArrayOutputStream();
            IOUtils.copyStream(source,outputStream);

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
    public void writer(OutputStream sink, Object data) {

        if (Preconditions.isNotBlanks(sink,data)) {

            String wrapperJSONSerialized = toJson(data);

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

    public String converterName(){

        String simpleName = TypeUtils.getClassSimpleName(this);

        int index = simpleName.lastIndexOf("Converter");
        if (index>0) {
            return simpleName.substring(0,index).toLowerCase();
        } else {
            return simpleName.toLowerCase();
        }
    }
}
