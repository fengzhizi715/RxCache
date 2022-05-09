package com.safframework.rxcache.persistence.diskmap;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import com.safframework.rxcache.persistence.disk.Disk;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @FileName: com.safframework.rxcache.persistence.diskmap.DiskMapImpl
 * @author: Tony Shen
 * @date: 2020-07-04 10:28
 * @since: V1.7
 */
public class DiskMapImpl implements Disk {

    private File cacheDirectory;
    private Converter converter;
    private Map<String, CacheHolder> map = null;

    public DiskMapImpl(File cacheDirectory) {

        this(cacheDirectory,new GsonConverter());
    }

    public DiskMapImpl(String cachePath) {

        this(cachePath,new GsonConverter());
    }

    public DiskMapImpl(File cacheDirectory, Converter converter) {

        this.cacheDirectory = cacheDirectory;
        this.converter = converter;
        try {
            map = new DiskMap(cacheDirectory, String.class, CacheHolder.class, converter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public DiskMapImpl(String cachePath, Converter converter) {

        File dir = new File(cachePath);
        if (!dir.exists()) {
            dir.mkdirs();
        }

        this.cacheDirectory = dir;
        this.converter = converter;
        try {
            map = new DiskMap(cacheDirectory, String.class, CacheHolder.class, converter);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int storedMB() {

        long bytes = cacheDirectory.length();

        double megabytes = Math.ceil((double) bytes / 1024 / 1024);
        return (int) megabytes;
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {

        CacheHolder holder = map.get(key);

        if (holder == null) return null;

        long timestamp = holder.getTimestamp();
        long expireTime = holder.getExpireTime();

        T result = null;

        if (expireTime == Constant.NEVER_EXPIRE) { // 缓存的数据从不过期

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
    public String getStringData(String key) {

        CacheHolder holder = (CacheHolder) map.get(key);

        if (holder == null) return null;

        long timestamp = holder.getTimestamp();
        long expireTime = holder.getExpireTime();

        String json = null;

        if (expireTime<0) { // 缓存的数据从不过期

            json = holder.getData();
        } else {

            if (timestamp + expireTime > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                json = holder.getData();
            } else {        // 缓存的数据已经过期

                evict(key);
            }
        }

        return json;
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
