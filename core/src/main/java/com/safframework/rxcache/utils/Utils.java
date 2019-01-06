package com.safframework.rxcache.utils;

import com.safframework.bytekit.Bytes;
import com.safframework.bytekit.bytes.ByteBufferBytes;

/**
 * Created by tony on 2018/10/6.
 */
public class Utils {

    /**
     * 将二进制转换成16进制
     * @param buf
     * @return
     */
    public static String parseByte2HexStr(byte buf[]) {

        return ByteBufferBytes.create(buf).toHexString();
    }

    /**
     * 将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {

        return Bytes.getByteArray(hexStr);
    }
}
