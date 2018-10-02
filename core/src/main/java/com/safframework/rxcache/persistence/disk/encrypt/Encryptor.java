package com.safframework.rxcache.persistence.disk.encrypt;

/**
 * Created by tony on 2018/9/30.
 */
public interface Encryptor {

    byte[] encrypt(String json);

    String decrypt(String json);
}
