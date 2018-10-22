package com.safframework.rxcache.memory.algorithm.lfu;

/**
 * Created by tony on 2018/10/22.
 */
public class LFUCacheEntry<K, V> extends Node {

    K key;
    V value;
    FrequencyNode frequencyNode;

    public LFUCacheEntry(K key, V value,
                         FrequencyNode frequencyNode) {

        this.key = key;
        this.value = value;
        this.frequencyNode = frequencyNode;
    }

    public boolean equals(Object o) {
        LFUCacheEntry<K, V> entry = (LFUCacheEntry<K, V>) o;
        return key.equals(entry.key) &&
                value.equals(entry.value);
    }

    public int hashCode() {

        return key.hashCode() * 31 + value.hashCode() * 17;
    }

    public String toString() {
        return "[" + key.toString() + "," + value.toString() + "]";
    }

}
