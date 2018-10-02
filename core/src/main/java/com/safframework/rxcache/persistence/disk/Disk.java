package com.safframework.rxcache.persistence.disk;

import com.safframework.rxcache.persistence.Persistence;

/**
 * Created by tony on 2018/9/29.
 */
public interface Disk extends Persistence {

    int storedMB();
}
