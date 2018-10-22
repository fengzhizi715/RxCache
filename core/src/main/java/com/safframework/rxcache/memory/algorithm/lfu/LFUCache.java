package com.safframework.rxcache.memory.algorithm.lfu;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tony on 2018/10/22.
 */
public class LFUCache<K, V> {

    HashMap<K, LFUCacheEntry<K, V>> kvStore;

    /* A Doubly linked list of frequency nodes */
    NodeList freqList;

    /* HashMap for storing frequencyNode entries */
    HashMap<Integer, FrequencyNode> frequencyMap;

    /* Capacity of cache */
    int capacity;

    /* current size of Cache */
    int size;

    public LFUCache(int capacity) {
        this.capacity = capacity;
        size = 0;
        kvStore = new HashMap<K, LFUCacheEntry<K, V>>();
        freqList = new NodeList();
        frequencyMap = new HashMap<Integer, FrequencyNode>();
    }

    public void delete(LFUCacheEntry<K, V> entry) {
        if (!kvStore.containsKey(entry.key))
            return;

        kvStore.remove(entry.key);
        entry.frequencyNode.lfuCacheEntryList.remove(entry);
        if (entry.frequencyNode.lfuCacheEntryList.length <= 0) {
            frequencyMap.remove(entry.frequencyNode.frequency);
            freqList.remove(entry.frequencyNode);
        }
        size--;
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

    public void set(K key, V value) {
        if (capacity == 0)
            return;
        FrequencyNode newFrequencyNode = null;
        if (kvStore.containsKey(key)) {
            /* Remove old key if exists */
            newFrequencyNode = getFrequencyNode(kvStore.get(key).frequencyNode.frequency + 1);
            delete(kvStore.get(key));
        } else if (size == capacity) {
            /* If cache size if full remove first element from freq list */
            FrequencyNode fNode = (FrequencyNode) freqList.head;
            LFUCacheEntry<K, V> entry = (LFUCacheEntry<K, V>) fNode.lfuCacheEntryList.head;
            delete(entry);
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
    }


    public V get(K key) {
        if (!kvStore.containsKey(key) || capacity == 0)
            return null;

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

        return entry.value;
    }
}
