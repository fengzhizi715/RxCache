package com.safframework.rxcache.persistence.encrypt;

/**
 * Created by tony on 2018/9/30.
 */
public interface Encryptor {

    String encrypt(String json);

    String decrypt(String json);
}
