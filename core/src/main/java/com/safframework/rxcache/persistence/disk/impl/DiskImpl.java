package com.safframework.rxcache.persistence.disk.impl;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.persistence.disk.Disk;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import com.safframework.tony.common.utils.IOUtils;
import com.safframework.tony.common.utils.Preconditions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tony on 2018/9/29.
 */
public class DiskImpl implements Disk {

    private File cacheDirectory;
    private Converter converter;

    private Lock lock = new ReentrantLock();

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
            lock.lock();

            key = safetyKey(key);
            File file = new File(cacheDirectory, key);
            inputStream = new FileInputStream(file);

            CacheHolder holder = converter.read(inputStream,CacheHolder.class);

            long timestamp = holder.timestamp;
            long expireTime = holder.expireTime;

            T result = null;

            if (expireTime<0) { // 缓存的数据从不过期

                String json = holder.data;

                result = converter.fromJson(json,type);
            } else {

                if (timestamp + expireTime > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    String json = holder.data;

                    result = converter.fromJson(json,type);
                } else {        // 缓存的数据已经过期

                    evict(key);
                }
            }

            return result != null ? new Record<>(Source.PERSISTENCE, key, result, timestamp, expireTime) : null;
        } catch (Exception ignore) {

            throw new RxCacheException(ignore);
        } finally {

            IOUtils.closeQuietly(inputStream);
            lock.unlock();
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
            lock.lock();

            key = safetyKey(key);

            File file = new File(cacheDirectory, key);
            outputStream = new FileOutputStream(file, false);
            converter.writer(outputStream,new CacheHolder(converter.toJson(value),System.currentTimeMillis(),expireTime));
        } catch (Exception e) {

            throw new RxCacheException(e);
        } finally {

            IOUtils.closeQuietly(outputStream);
            lock.unlock();
        }
    }

    @Override
    public List<String> allKeys() {

        List<String> result = new ArrayList<>();

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

        for (File file:files) {

            if (file.isFile() && file.getName().equals(key)) {

                return true;
            }
        }
        return false;
    }

    @Override
    public void evict(String key) {
        key = safetyKey(key);
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
