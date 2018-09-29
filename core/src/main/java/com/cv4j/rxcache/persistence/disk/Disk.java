package com.cv4j.rxcache.persistence.disk;

import com.cv4j.rxcache.persistence.Persistence;

/**
 * Created by tony on 2018/9/29.
 */
public interface Disk extends Persistence {

    int storedMB();
}
