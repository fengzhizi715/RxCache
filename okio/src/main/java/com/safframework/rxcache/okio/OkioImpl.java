package com.safframework.rxcache.okio;

import com.safframework.bytekit.utils.IOUtils;
import com.safframework.bytekit.utils.Preconditions;
import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheHolder;
import com.safframework.rxcache.domain.Record;
import com.safframework.rxcache.domain.Source;
import com.safframework.rxcache.exception.RxCacheException;
import com.safframework.rxcache.persistence.converter.Converter;
import com.safframework.rxcache.persistence.converter.GsonConverter;
import com.safframework.rxcache.persistence.disk.Disk;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.Okio;

import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tony on 2019-02-27.
 */
public class OkioImpl implements Disk {

    private File cacheDirectory;
    private Converter converter;

    public OkioImpl(File cacheDirectory) {

        this(cacheDirectory,new GsonConverter());
    }

    public OkioImpl(File cacheDirectory,Converter converter) {

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
        BufferedSource bufferedSource = null;

        try {

            key = safetyKey(key);
            File file = new File(cacheDirectory, key);

            if (file == null || !file.exists()) return null;

            inputStream = new FileInputStream(file);

            bufferedSource = Okio.buffer(Okio.source(inputStream));
            String line = bufferedSource.readUtf8();

            CacheHolder holder = converter.fromJson(line,CacheHolder.class);

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
        } catch (Exception ignore) {

            throw new RxCacheException(ignore);
        } finally {

            IOUtils.closeQuietly(inputStream,bufferedSource);
        }
    }

    @Override
    public <T> void save(String key, T value) {
        save(key, value, Constant.NEVER_EXPIRE);
    }

    @Override
    public <T> void save(String key, T value, long expireTime) {

        FileOutputStream outputStream = null;
        BufferedSink bufferedSink = null;

        try {
            key = safetyKey(key);

            File file = new File(cacheDirectory, key);
            outputStream = new FileOutputStream(file, false);

            bufferedSink = Okio.buffer(Okio.sink(outputStream));
            bufferedSink.writeUtf8(converter.toJson(new CacheHolder(converter.toJson(value),System.currentTimeMillis(),expireTime,converter.converterName())));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            IOUtils.closeQuietly(bufferedSink,outputStream);
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

        for (File file : files) {

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
