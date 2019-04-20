package com.safframework.rxcache.memory.algorithm.lfu;

import com.safframework.rxcache.domain.CacheStatistics;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by tony on 2018/10/22.
 */
public class LFUCache<K, V> {

    private HashMap<K, LFUCacheEntry<K, V>> kvStore;

    /* A Doubly linked list of frequency nodes */
    private NodeList freqList;

    /* HashMap for storing frequencyNode entries */
    private HashMap<Integer, FrequencyNode> frequencyMap;

    private CacheStatistics cacheStatistics;

    /* Capacity of cache */
    private int capacity;

    /* current size of Cache */
    private int size;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        size = 0;
        kvStore = new HashMap<K, LFUCacheEntry<K, V>>();
        freqList = new NodeList();
        frequencyMap = new HashMap<Integer, FrequencyNode>();
        cacheStatistics = new CacheStatistics(capacity);
    }

    public boolean containsKey(K key) {

        return kvStore.containsKey(key);
    }

    public FrequencyNode getFrequencyNode(int frequency) {
        if (!frequencyMap.containsKey(frequency - 1) &&
                !frequencyMap.containsKey(frequency) &&
                frequency != 1) {
            System.out.println("Request for Frequency Node " + frequency +
                    " But " + frequency + " or " + (frequency - 1) +
                    " Doesn't exist");
            return null;

        }

        if (!frequencyMap.containsKey(frequency)) {
            FrequencyNode newFrequencyNode = new FrequencyNode(frequency);
            if (frequency != 1)
                freqList.insertAfter(frequencyMap.get(frequency - 1),
                        newFrequencyNode);
            else
                freqList.prepend(newFrequencyNode);
            frequencyMap.put(frequency, newFrequencyNode);
        }

        return frequencyMap.get(frequency);
    }

    public void put(K key, V value) {
        if (capacity == 0)
            return;
        FrequencyNode newFrequencyNode = null;
        if (kvStore.containsKey(key)) {
            /* Remove old key if exists */
            newFrequencyNode = getFrequencyNode(kvStore.get(key).frequencyNode.frequency + 1);
            remove(kvStore.get(key));
        } else if (size == capacity) {
            /* If cache size if full remove first element from freq list */
            FrequencyNode fNode = (FrequencyNode) freqList.head;
            LFUCacheEntry<K, V> entry = (LFUCacheEntry<K, V>) fNode.lfuCacheEntryList.head;
            remove(entry);
            System.out.println("Cache full. Removed entry " + entry);
        }
        if (newFrequencyNode == null)
            newFrequencyNode = getFrequencyNode(1);
        LFUCacheEntry<K, V> entry = new LFUCacheEntry<K, V>(key, value,
                newFrequencyNode);
        kvStore.put(key, entry);
        newFrequencyNode.lfuCacheEntryList.append(entry);
        size++;

        System.out.println("Set new " + entry + " entry, cache size: " + size);
        cacheStatistics.incrementPutCount();
    }


    public V get(K key) {
        if (capacity == 0) return null;

        if (!kvStore.containsKey(key)) {
            cacheStatistics.incrementMissCount();
            return null;
        }

        LFUCacheEntry<K, V> entry = kvStore.get(key);
        FrequencyNode newFrequencyNode =
                getFrequencyNode(entry.frequencyNode.frequency + 1);
        entry.frequencyNode.lfuCacheEntryList.remove(entry);
        newFrequencyNode.lfuCacheEntryList.append(entry);
        if (entry.frequencyNode.lfuCacheEntryList.length <= 0) {
            frequencyMap.remove(entry.frequencyNode.frequency);
            freqList.remove(entry.frequencyNode);
        }
        entry.frequencyNode = newFrequencyNode;

        if (entry.value!=null) {

            cacheStatistics.incrementHitCount();
            return entry.value;
        } else {

            cacheStatistics.incrementMissCount();
            return null;
        }
    }

    public Set<K> keySet() {

        return kvStore.keySet();
    }

    public void remove(String key) {
        if (!kvStore.containsKey(key))
            return;

        LFUCacheEntry<K, V> entry = kvStore.get(key);

        remove(entry);
    }

    public void remove(LFUCacheEntry<K, V> entry) {
        if (!kvStore.containsKey(entry.key))
            return;

        kvStore.remove(entry.key);
        entry.frequencyNode.lfuCacheEntryList.remove(entry);
        if (entry.frequencyNode.lfuCacheEntryList.length <= 0) {
            frequencyMap.remove(entry.frequencyNode.frequency);
            freqList.remove(entry.frequencyNode);
        }
        size--;
        cacheStatistics.incrementEvictionCount();
    }

    public void clear() {

        System.out.println(String.format("Remove all %d entries",size));
        cacheStatistics.incrementEvictionCount(size);
        kvStore.clear();
        frequencyMap.clear();
        freqList.clear();
        size = 0;
    }

    public CacheStatistics getCacheStatistics() {
        return cacheStatistics;
    }
}
