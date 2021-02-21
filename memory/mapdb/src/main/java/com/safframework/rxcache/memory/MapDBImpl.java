package com.safframework.rxcache.memory;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.memory.impl.AbstractMemoryImpl;
import org.mapdb.DB;
import org.mapdb.DBMaker;
import org.mapdb.HTreeMap;
import org.mapdb.Serializer;

import java.util.Set;

/**
 * Created by tony on 2019-01-04.
 */
public class MapDBImpl extends AbstractMemoryImpl {

    private DB db;
    private HTreeMap<String, Object> map;
    private CacheStatistics cacheStatistics;

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

        this.cacheStatistics = new CacheStatistics((int)maxSize);
    }


    public MapDBImpl(long maxSize, MapDBCacheConfig cacheConfig) {
        super(maxSize);
        db = DBMaker.heapDB()
                .make();

        map = db.hashMap("rxcache",Serializer.STRING,Serializer.JAVA)
                .expireMaxSize(maxSize)
                .expireAfterCreate(cacheConfig.expireDuration,cacheConfig.expireTimeUnit)
                .expireAfterUpdate(cacheConfig.expireDuration,cacheConfig.expireTimeUnit)
                .expireAfterGet(cacheConfig.expireAfterGetDuration,cacheConfig.expireAfterGetTimeUnit)
                .counterEnable()
                .create();

        this.cacheStatistics = new CacheStatistics((int)maxSize);
    }

    @Override
    public <T> Record<T> getIfPresent(String key) {

        T result = null;

        if(expireTimeMap.get(key)!=null) {

            if (expireTimeMap.get(key) == Constant.NEVER_EXPIRE) { // 缓存的数据从不过期

                result = (T) map.get(key);
            } else {

                if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    result = (T) map.get(key);
                } else {                     // 缓存的数据已经过期

                    evict(key);
                }
            }
        }

        if (result!=null) {

            cacheStatistics.incrementHitCount();
            return new Record<>(Source.MEMORY,key, result, timestampMap.get(key),expireTimeMap.get(key));
        } else {

            cacheStatistics.incrementMissCount();
            return null;
        }
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

        cacheStatistics.incrementPutCount();
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
        cacheStatistics.incrementEvictionCount();
    }

    @Override
    public void evictAll() {

        map.clear();
        cacheStatistics.incrementEvictionCount(keySet().size());
    }

    @Override
    public CacheStatistics getCacheStatistics() {
        return cacheStatistics;
    }
}
