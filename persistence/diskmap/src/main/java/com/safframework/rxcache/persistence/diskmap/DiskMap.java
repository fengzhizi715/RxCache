package com.safframework.rxcache.persistence.diskmap;

import com.safframework.rxcache.persistence.converter.Converter;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @FileName: com.safframework.rxcache.persistence.diskmap.CachedFileMap
 * @author: Tony Shen
 * @date: 2020-07-03 23:50
 * @version: V1.7
 */
public class DiskMap<K,V> extends AbstractFileMap<K,V> {

    private Map<K,V> internal;

    public DiskMap(File file, Class<K> keyType, Class<V> valueType, Converter converter) throws IOException {
        super(file, keyType, valueType,converter);
    }

    @Override
    protected void init() throws IOException {
        internal = new HashMap<>();
    }

    @Override
    protected void loadEntry(long offset, String line) throws IOException {
        Entry<K, V> entry = parseLine(line);
        internal.put(entry.getKey(), entry.getValue());
    }

    @Override
    public synchronized int size() {
        return internal.size();
    }

    @Override
    public synchronized boolean isEmpty() {
        return internal.isEmpty();
    }

    @Override
    public synchronized boolean containsKey(Object key) {
        return internal.containsKey(key);
    }

    @Override
    public synchronized boolean containsValue(Object value) {
        return internal.containsValue(value);
    }

    @Override
    public synchronized V get(Object key) {
        return internal.get(key);
    }

    @Override
    public synchronized V put(K key, V value) {
        writeLine(key, value);
        return internal.put(key, value);
    }

    @Override
    public synchronized V remove(Object key) {
        writeLine((K) key, null);
        return internal.remove(key);
    }

    @Override
    public synchronized void clear() {
        super.clearLines();
        internal.clear();
    }

    @Override
    public synchronized Set<K> keySet() {
        return internal.keySet();
    }

    @Override
    public synchronized Collection<V> values() {
        return internal.values();
    }

    @Override
    public synchronized Set<Entry<K, V>> entrySet() {
        return internal.entrySet();
    }

}
