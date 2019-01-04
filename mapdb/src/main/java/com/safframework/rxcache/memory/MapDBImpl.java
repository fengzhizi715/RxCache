package com.safframework.rxcache.memory;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.LinkedList;
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
        db = DBMaker.memoryDB()
                .executorEnable()
                .make();

        map = db.hashMap("test")
                .keySerializer(Serializer.STRING)
                .valueSerializer(Serializer.JAVA)
                .create();

        this.keys = new LinkedList<>();
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

        if (keySet().size()<maxSize) { // 缓存还有空间

            saveValue(key,value,expireTime);
        } else {                       // 缓存空间不足，需要删除一个

            if (containsKey(key)) {

                keys.remove(key);

                saveValue(key,value,expireTime);
            } else {

                String oldKey = keys.get(0); // 最早缓存的key
                evict(oldKey);               // 删除最早缓存的数据 FIFO算法

                saveValue(key,value,expireTime);
            }
        }
    }

    private <T> void saveValue(String key, T value, long expireTime) {

        map.put(key,value);
        timestampMap.put(key,System.currentTimeMillis());
        expireTimeMap.put(key,expireTime);
        keys.add(key);
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
