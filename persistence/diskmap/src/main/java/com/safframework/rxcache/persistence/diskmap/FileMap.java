package com.safframework.rxcache.persistence.diskmap;

import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @FileName: com.safframework.rxcache.persistence.diskmap.FileMap
 * @author: Tony Shen
 * @date: 2020-07-03 20:48
 * @version: V1.7
 */
public interface FileMap<K, V> extends Map<K, V> {

    File getFile();

    long diskSize() throws IOException;

    void close() throws IOException;
}
