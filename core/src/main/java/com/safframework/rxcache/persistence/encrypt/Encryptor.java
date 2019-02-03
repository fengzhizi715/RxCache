package com.safframework.rxcache.persistence.encrypt;

/**
 *
 * Created by tony on 2018/9/30.
 */
public interface Encryptor {

    /**
     * 将字符串进行加密
     * @param json
     * @return
     */
    String encrypt(String json);

    /**
     * 将字符串进行加密
     * @param json
     * @return
     */
    String decrypt(String json);
}
