package com.safframework.rxcache.persistence.disk.impl;

import com.safframework.bytekit.utils.IOUtils;
import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.log.LoggerProxy;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import com.safframework.rxcache.persistence.disk.Disk;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by tony on 2018/9/29.
 */
public class DiskImpl implements Disk {

    private File cacheDirectory;
    private Converter converter;

    public DiskImpl(File cacheDirectory) {

        this(cacheDirectory,new GsonConverter());
    }

    public DiskImpl(File cacheDirectory,Converter converter) {

        this.cacheDirectory = cacheDirectory;
        this.converter = converter;
    }

    @Override
    public int storedMB() {

        long bytes = 0;

        final File[] files = cacheDirectory.listFiles();
        if (files == null) return 0;

        for (File file : files) {
            bytes += file.length();
        }

        double megabytes = Math.ceil((double) bytes / 1024 / 1024);
        return (int) megabytes;
    }

    @Override
    public <T> Record<T> retrieve(String key, Type type) {

        FileInputStream inputStream = null;

        try {
            String safetyKey = safetyKey(key);
            File file = new File(cacheDirectory, safetyKey);
            
            if (file == null || !file.exists()) return null;

            inputStream = new FileInputStream(file);

            CacheHolder holder = converter.read(inputStream,CacheHolder.class);

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

                    evict(safetyKey);
                }
            }

            return result != null ? new Record<>(Source.PERSISTENCE, safetyKey, result, timestamp, expireTime) : null;
        } catch (Exception ignore) {
            LoggerProxy.INSTANCE.getLogger().e("retrieve() is failed...","rxcache", ignore);
            throw new RxCacheException(ignore);
        } finally {

            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public String getStringData(String key) {

        FileInputStream inputStream = null;

        try {
            String safetyKey = safetyKey(key);
            File file = new File(cacheDirectory, safetyKey);

            if (file == null || !file.exists()) return null;

            inputStream = new FileInputStream(file);

            CacheHolder holder = converter.read(inputStream,CacheHolder.class);

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

                    evict(safetyKey);
                }
            }

            return json;
        } catch (Exception ignore) {
            LoggerProxy.INSTANCE.getLogger().e("getStringData() is failed...","rxcache", ignore);
            throw new RxCacheException(ignore);
        } finally {

            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public <T> void save(String key, T value) {

        save(key, value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {

        FileOutputStream outputStream = null;

        try {
            String safetyKey = safetyKey(key);

            File file = new File(cacheDirectory, safetyKey);
            outputStream = new FileOutputStream(file, false);
            converter.writer(outputStream,new CacheHolder(converter.toJson(value),System.currentTimeMillis(),expireTime,converter.converterName()));
        } catch (Exception e) {
            LoggerProxy.INSTANCE.getLogger().e("save() is failed...","rxcache", e);
            throw new RxCacheException(e);
        } finally {
            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    public Set<String> keySet() {

        Set<String> result = new HashSet<String>();

        File[] files = cacheDirectory.listFiles();
        if (files == null) return result;

        for (File file : files) {
            if (file.isFile()) {
                result.add(file.getName());
            }
        }

        return result;
    }

    @Override
    public boolean containsKey(String key) {

        File[] files = cacheDirectory.listFiles();

        for (File file : files) {

            if (file.isFile() && file.getName().equals(key)) {

                return true;
            }
        }
        return false;
    }

    @Override
    public void evict(String key) {

        File file = new File(cacheDirectory, key);
        file.delete();
    }

    @Override
    public void evictAll() {

        File[] files = cacheDirectory.listFiles();

        if (Preconditions.isNotBlank(files)){
            for (File file : files) {
                if (file != null)
                    file.delete();
            }
        }
    }

    private String safetyKey(String key) {
        return key.replaceAll("/", "_");
    }
}
