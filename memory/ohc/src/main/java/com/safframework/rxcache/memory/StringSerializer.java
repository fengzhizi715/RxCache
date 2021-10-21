package com.safframework.rxcache.memory;

import org.caffinitas.ohc.CacheSerializer;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;

/**
 * @FileName: com.safframework.rxcache.memory.StringSerializer
 * @author: Tony Shen
 * @date: 2021/10/21 2:59 下午
 * @version: V1.0 <描述当前版本功能>
 */
public class StringSerializer implements CacheSerializer<String> {

    /**
     * 计算字符串序列化后占用的空间
     *
     * @param value 需要序列化存储的字符串
     * @return 序列化后的字节数
     */
    @Override
    public int serializedSize(String value) {
        byte[] bytes = value.getBytes(Charset.forName("UTF-8"));

        // 设置字符串长度限制，2^16 = 65536
        if (bytes.length > 65536)
            throw new RuntimeException("encoded string too long: " + bytes.length + " bytes");

        // 设置字符串长度限制，2^16 = 65536
        return bytes.length + 2;
    }


    /**
     * 将字符串对象序列化到 ByteBuffer 中，ByteBuffer是OHC管理的堆外内存区域的映射。
     *
     * @param value 需要序列化的对象
     * @param buf   序列化后的存储空间
     */
    @Override
    public void serialize(String value, ByteBuffer buf) {
        // 得到字符串对象UTF-8编码的字节数组
        byte[] bytes = value.getBytes(Charset.forName("UTF-8"));
        // 用前16位记录数组长度
        buf.put((byte) ((bytes.length >>> 8) & 0xFF));
        buf.put((byte) ((bytes.length) & 0xFF));
        buf.put(bytes);
    }


    /**
     * 对堆外缓存的字符串进行反序列化
     *
     * @param buf 字节数组所在的 ByteBuffer
     * @return 字符串对象.
     */
    @Override
    public String deserialize(ByteBuffer buf) {
        // 判断字节数组的长度
        int length = (((buf.get() & 0xff) << 8) + ((buf.get() & 0xff)));
        byte[] bytes = new byte[length];
        // 读取字节数组
        buf.get(bytes);
        // 返回字符串对象
        return new String(bytes, Charset.forName("UTF-8"));
    }
}
