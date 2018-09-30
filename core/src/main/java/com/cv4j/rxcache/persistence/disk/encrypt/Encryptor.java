package com.cv4j.rxcache.persistence.disk.encrypt;

import java.io.File;

/**
 * Created by tony on 2018/9/30.
 */
public interface Encryptor {

    void encrypt(String key, File encryptedFile);

    void decrypt(String key, File decryptedFile);
}
