package com.safframework.rxcache.memory;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

/**
 * @FileName: com.safframework.rxcache.memory.StringSerializer
 * @author: Tony Shen
 * @date: 2021/10/21 2:59 下午
 * @version: V1.0 <描述当前版本功能>
 */
public class StringSerializer implements CacheSerializer<String> {

    @Override
    public void serialize(String s, ByteBuffer byteBuffer) {
        byte[] bytes = s.getBytes(StandardCharsets.UTF_8);
        byteBuffer.put((byte) ((bytes.length >>> 8) & 0xFF));
        byteBuffer.put((byte) ((bytes.length >>> 0) & 0xFF));
        byteBuffer.put(bytes);
    }

    @Override
    public String deserialize(ByteBuffer byteBuffer) {
        int length = (((byteBuffer.get() & 0xff) << 8) + ((byteBuffer.get() & 0xff) << 0));
        byte[] bytes = new byte[length];
        byteBuffer.get(bytes);
        return new String(bytes, StandardCharsets.UTF_8);
    }

    @Override
    public int serializedSize(String s) {
        return writeUTFLen(s);
    }

    static int writeUTFLen(String str) {
        int strlen = str.length();
        int utflen = 0;
        int c;

        for (int i = 0; i < strlen; i++) {
            c = str.charAt(i);
            if ((c >= 0x0001) && (c <= 0x007F))
                utflen++;
            else if (c > 0x07FF)
                utflen += 3;
            else
                utflen += 2;
        }

        if (utflen > 65535)
            throw new RuntimeException("encoded string too long: " + utflen + " bytes");

        return utflen + 2;
    }
}
