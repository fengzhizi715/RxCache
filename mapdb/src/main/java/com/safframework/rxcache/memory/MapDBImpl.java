package com.safframework.rxcache.memory;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.List;
import java.util.Set;

/**
 * Created by tony on 2019-01-04.
 */
public class MapDBImpl extends AbstractMemoryImpl {

    private DB db;
    private HTreeMap<String, Object> map;
    private List<String> keys;

    public MapDBImpl(long maxSize) {
        super(maxSize);
        db = DBMaker.heapDB()
                .make();

        map = db.hashMap("rxcache",Serializer.STRING,Serializer.JAVA)
                .expireMaxSize(maxSize)
                .expireAfterCreate()
                .expireAfterUpdate()
                .expireAfterGet()
                .counterEnable()
                .create();
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {

        T result = null;

        if(expireTimeMap.get(key)!=null) {

            if (expireTimeMap.get(key)<0) { // 缓存的数据从不过期

                result = (T) map.get(key);
            } else {

                if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    result = (T) map.get(key);
                } else {                     // 缓存的数据已经过期

                    evict(key);
                }
            }
        }

        return result != null ? new Record<>(Source.MEMORY,key, result, timestampMap.get(key),expireTimeMap.get(key)) : null;
    }

    @Override
    public <T> void put(String key, T value) {

        put(key,value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void put(String key, T value, long expireTime) {

        map.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
        expireTimeMap.put(key,expireTime);
    }

    @Override
    public Set<String> keySet() {

        return map.keySet();
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
}
