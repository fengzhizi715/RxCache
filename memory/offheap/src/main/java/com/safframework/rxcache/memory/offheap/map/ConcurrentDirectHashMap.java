package com.safframework.rxcache.memory.offheap.map;

import com.safframework.rxcache.memory.offheap.converter.DirectBufferConverter;

import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Created by tony on 2018-12-21.
 */
public abstract class ConcurrentDirectHashMap<K, V> implements Map<K, V> {

    final private Map<K, ByteBuffer> map;

    private final DirectBufferConverter<V> converter = new DirectBufferConverter<V>() {

        @Override
        public byte[] toBytes(V value) {
            return convertObjectToBytes(value);
        }

        @Override
        public V toObject(byte[] value) {
            return convertBytesToObject(value);
        }
    };

    ConcurrentDirectHashMap() {

        map = new ConcurrentHashMap<>();
    }

    ConcurrentDirectHashMap(Map<K, V> m) {

        map = new ConcurrentHashMap<>();

        for (Entry<K, V> entry : m.entrySet()) {
            K key = entry.getKey();
            ByteBuffer val = converter.to(entry.getValue());
            map.put(key, val);
        }
    }

    protected abstract byte[] convertObjectToBytes(V value);

    protected abstract V convertBytesToObject(byte[] value);

    @Override
    public int size() {
        return map.size();
    }

    @Override
    public boolean isEmpty() {
        return map.isEmpty();
    }

    @Override
    public boolean containsKey(Object key) {
        return map.containsKey(key);
    }

    @Override
    public V get(Object key) {
        final ByteBuffer byteBuffer = map.get(key);
        return converter.from(byteBuffer);
    }

    @Override
    public V put(K key, V value) {
        final ByteBuffer byteBuffer = map.put(key, converter.to(value));
        converter.dispose(byteBuffer);
        return converter.from(byteBuffer);
    }

    @Override
    public V remove(Object key) {
        final ByteBuffer byteBuffer = map.remove(key);
        final V value = converter.from(byteBuffer);
        converter.dispose(byteBuffer);
        return value;
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        for (Entry<? extends K, ? extends V> entry : m.entrySet()) {
            ByteBuffer byteBuffer = converter.to(entry.getValue());
            map.put(entry.getKey(), byteBuffer);
        }
    }

    @Override
    public void clear() {
        final Set<K> keys = map.keySet();

        for (K key : keys) {
            map.remove(key);
        }
    }

    @Override
    public Set<K> keySet() {
        return map.keySet();
    }

    @Override
    public Collection<V> values() {
        Collection<V> values = new ArrayList<>();

        for (ByteBuffer byteBuffer : map.values())
        {
            V value = converter.from(byteBuffer);
            values.add(value);
        }
        return values;
    }

    @Override
    public Set<Entry<K, V>> entrySet() {
        Set<Entry<K, V>> entries = new HashSet<>();

        for (Entry<K, ByteBuffer> entry : map.entrySet()) {
            K key = entry.getKey();
            V value = converter.from(entry.getValue());

            entries.add(new Entry<K, V>() {
                @Override
                public K getKey() {
                    return key;
                }

                @Override
                public V getValue() {
                    return value;
                }

                @Override
                public V setValue(V v) {
                    return null;
                }
            });
        }

        return entries;
    }

    @Override
    public boolean containsValue(Object value) {

        for (ByteBuffer v : map.values()) {
            if (v.equals(value)) {
                return true;
            }
        }
        return false;
    }
}
