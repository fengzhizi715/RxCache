package com.safframework.rxcache.persistence.disk.impl;

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

/**
 * Created by tony on 2018/9/29.
 */
public class DiskImpl implements Disk {

    private File cacheDirectory;
    private Converter converter;
    private HashMap<String, Long> timestampMap;

    public DiskImpl(File cacheDirectory,Converter converter) {

        this.cacheDirectory = cacheDirectory;
        this.converter = converter;
        this.timestampMap = new HashMap<>();
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

        key = safetyKey(key);

        File file = new File(cacheDirectory, key);

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            T result = converter.read(inputStream,type);

            return result != null ? new CacheHolder<>(result, timestampMap.get(key)) : null;
        } catch (Exception ignore) {
            return null;
        } finally {

            IOUtils.closeQuietly(inputStream);
        }
    }

    @Override
    public <T> void save(String key, T value) {

        key = safetyKey(key);
        FileOutputStream outputStream = null;

        try {
            File file = new File(cacheDirectory, key);
            outputStream = new FileOutputStream(file, false);
            converter.writer(outputStream,value);
            timestampMap.put(key,System.currentTimeMillis());
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {

            IOUtils.closeQuietly(outputStream);
        }
    }

    @Override
    public List<String> allKeys() {

        List<String> nameFiles = new ArrayList<>();

        File[] files = cacheDirectory.listFiles();
        if (files == null) return nameFiles;

        for (File file : files) {
            if (file.isFile()) {
                nameFiles.add(file.getName());
            }
        }

        return nameFiles;
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
    }

    private String safetyKey(String key) {
        return key.replaceAll("/", "_");
    }
}
