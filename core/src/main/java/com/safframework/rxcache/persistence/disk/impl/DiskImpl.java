package com.safframework.rxcache.persistence.disk.impl;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.persistence.disk.Disk;
import com.safframework.rxcache.persistence.disk.converter.Converter;
import com.safframework.tony.common.utils.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tony on 2018/9/29.
 */
public class DiskImpl implements Disk {

    private File cacheDirectory;
    private Converter converter;
    private HashMap<String, Long> timestampMap;
    private HashMap<String, Long> expireTimeMap;
    private Lock lock = new ReentrantLock();

    public DiskImpl(File cacheDirectory,Converter converter) {

        this.cacheDirectory = cacheDirectory;
        this.converter = converter;
        this.timestampMap = new HashMap<>();
        this.expireTimeMap = new HashMap<>();
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
    public <T> CacheHolder<T> retrieve(String key, Type type) {

        FileInputStream inputStream = null;

        try {
            lock.lock();

            File file = null;
            T result = null;

            if (expireTimeMap.get(key)<0) { // 缓存的数据从不过期

                key = safetyKey(key);
                file = new File(cacheDirectory, key);
                inputStream = new FileInputStream(file);
                result = converter.read(inputStream,type);
            } else {

                if (timestampMap.get(key) + expireTimeMap.get(key) > System.currentTimeMillis()) {  // 缓存的数据还没有过期

                    key = safetyKey(key);
                    file = new File(cacheDirectory, key);
                    inputStream = new FileInputStream(file);
                    result = converter.read(inputStream,type);

                } else {                     // 缓存的数据已经过期

                    evict(key);

                    return null;
                }
            }

            return result != null ? new CacheHolder<>(result, timestampMap.get(key)) : null;
        } catch (Exception ignore) {

            return null;
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

        key = safetyKey(key);
        FileOutputStream outputStream = null;

        try {
            File file = new File(cacheDirectory, key);
            outputStream = new FileOutputStream(file, false);
            converter.writer(outputStream,value);
            timestampMap.put(key,System.currentTimeMillis());
            expireTimeMap.put(key,expireTime);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            IOUtils.closeQuietly(outputStream);
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
        final File file = new File(cacheDirectory, key);
        file.delete();

        timestampMap.remove(key);
        expireTimeMap.remove(key);
    }

    @Override
    public void evictAll() {

        File[] files = cacheDirectory.listFiles();

        if (null != files) {
            for (File file : files) {
                if (file != null)
                    file.delete();
            }
        }

        timestampMap.clear();
        expireTimeMap.clear();
    }

    private String safetyKey(String key) {
        return key.replaceAll("/", "_");
    }
}
