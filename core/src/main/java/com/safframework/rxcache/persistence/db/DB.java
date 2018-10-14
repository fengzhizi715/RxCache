package com.safframework.rxcache.persistence.db;

import com.safframework.rxcache.persistence.Persistence;

import java.util.List;

/**
 * Created by tony on 2018/10/14.
 */
public interface DB extends Persistence {

    <T> T queryById(String key,String id, T value);

    <T> List<T> query(String key,String where, T value);
}
