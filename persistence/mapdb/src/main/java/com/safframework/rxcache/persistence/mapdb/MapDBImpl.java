package com.safframework.rxcache.persistence.mapdb;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.persistence.Persistence;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.Serializer;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentMap;

/**
 * Created by tony on 2019-10-05.
 */
public class MapDBImpl implements Persistence {

    private Converter converter;
    private DB db;
    private ConcurrentMap<String,Object> map;

    public MapDBImpl(File dbFile) {

        this(dbFile,new GsonConverter());
    }

    public MapDBImpl(File dbFile, Converter converter) {

        this.converter = converter;

        db = DBMaker
                .fileDB(dbFile)
                .fileMmapEnable()
                .make();

        map = db.hashMap("rxcache", Serializer.STRING,Serializer.JAVA)
                .createOrOpen();
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {

        CacheHolder holder = (CacheHolder) map.get(key);;

        if (holder == null) return null;

        long timestamp = holder.getTimestamp();
        long expireTime = holder.getExpireTime();

        T result = null;

        if (expireTime<0) { // 缓存的数据从不过期

            String json = holder.getData();

            result = converter.fromJson(json,type);
        } else {

            if (timestamp + expireTime > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                String json = holder.getData();

                result = converter.fromJson(json,type);
            } else {        // 缓存的数据已经过期

                evict(key);
            }
        }

        return result != null ? new Record<>(Source.PERSISTENCE, key, result, timestamp, expireTime) : null;
    }

    @Override
    public <T> void save(String key, T value) {
        save(key, value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {

        map.put(key,new CacheHolder(converter.toJson(value),System.currentTimeMillis(),expireTime,converter.converterName()));
    }

    @Override
    public List<String> allKeys() {

        return new ArrayList(map.keySet());
    }

    @Override
    public boolean containsKey(String key) {

        return map.containsKey(key);
    }

    @Override
    public void evict(String key) {

        map.remove(key);
    }

    @Override
    public void evictAll() {

        map.clear();
    }

    public void close() {

        db.close();
    }
}
