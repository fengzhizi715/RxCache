package com.safframework.rxcache.memory.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by tony on 2018/10/22.
 */
public class LFUCache<K, V> {


    private Map<K, CacheNode<K, V>> map;
    private CacheDeque<K, V>[] freqTable;

    private final int capacity;
    private int curSize = 0;
    private int evictionCount;

    private final ReentrantLock lock = new ReentrantLock();

    public LFUCache(final int maxCapacity, final float evictionFactor) {

        if (maxCapacity <= 0) {
            throw new IllegalArgumentException("Illegal initial capacity: " +
                    maxCapacity);
        }

        boolean factorInRange = evictionFactor <= 1 || evictionFactor < 0;
        if (!factorInRange || Float.isNaN(evictionFactor)) {
            throw new IllegalArgumentException("Illegal eviction factor value:"
                    + evictionFactor);
        }
        this.capacity = maxCapacity;
        this.evictionCount = (int) (capacity * evictionFactor);
        this.map = new HashMap<>();
        this.freqTable = new CacheDeque[capacity + 1];
        for (int i = 0; i <= capacity; i++) {
            freqTable[i] = new CacheDeque<K, V>();
        }
        for (int i = 0; i < capacity; i++) {
            freqTable[i].nextDeque = freqTable[i + 1];
        }
        freqTable[capacity].nextDeque = freqTable[capacity];
    }

    public V put(K key, V value) {

        CacheNode<K, V> node;
        lock.lock();
        try {
            if (map.containsKey(key)) {
                node = map.get(key);
                if (node != null) {
                    CacheNode.withdrawNode(node);
                }
                node.value = value;
                freqTable[0].addLastNode(node);
                map.put(key, node);
            } else {
                node = freqTable[0].addLast(key, value);
                map.put(key, node);
                curSize++;
                if (curSize > capacity) {
                    proceedEviction();
                }
            }
        } finally {
            lock.unlock();
        }
        return node.value;
    }

    public V remove(K key) {

        CacheNode<K, V> node = null;
        lock.lock();
        try {
            if (map.containsKey(key)) {
                node = map.remove(key);
                if (node != null) {
                    CacheNode.withdrawNode(node);
                }
                curSize--;
            }
        } finally {
            lock.unlock();
        }
        return (node != null) ? node.value : null;
    }

    public V get(K key) {

        CacheNode<K, V> node = null;
        lock.lock();
        try {
            if (map.containsKey(key)) {
                node = map.get(key);
                CacheNode.withdrawNode(node);
                node.owner.nextDeque.addLastNode(node);
            }
        } finally {
            lock.unlock();
        }
        return (node != null) ? node.value : null;
    }

    private int proceedEviction() {
        int targetSize = capacity - evictionCount;
        int evictedElements = 0;

        FREQ_TABLE_ITER_LOOP:
        for (int i = 0; i <= capacity; i++) {
            CacheNode<K, V> node;
            while (!freqTable[i].isEmpty()) {
                node = freqTable[i].pollFirst();
                remove(node.key);
                if (targetSize >= curSize) {
                    break FREQ_TABLE_ITER_LOOP;
                }
                evictedElements++;
            }
        }
        return evictedElements;
    }

    public int getSize() {
        return curSize;
    }

    /**
     * Custom node implementation. Those nodes are part of
     * {@link CacheDeque} data structure.
     *
     * @param <K> key
     * @param <V> value
     */
    static class CacheNode<K, V> {

        CacheNode<K, V> prev;
        CacheNode<K, V> next;
        K key;
        V value;
        CacheDeque owner;

        CacheNode() {
        }

        CacheNode(final K key, final V value) {
            this.key = key;
            this.value = value;
        }

        /**
         * This method takes specified node and reattaches it neighbors nodes
         * links to each other, so specified node will no longer tied with them.
         * Returns united node, returns null if argument is null.
         *
         * @param node note to retrieve
         * @param <K>  key
         * @param <V>  value
         * @return retrieved node
         */
        static <K, V> CacheNode<K, V> withdrawNode(
                final CacheNode<K, V> node) {
            if (node != null && node.prev != null) {
                node.prev.next = node.next;
                if (node.next != null) {
                    node.next.prev = node.prev;
                }
            }
            return node;
        }

    }

    /**
     * Custom deque implementation of LIFO type. Allows to place element at top
     * of deque and poll very last added elements. An arbitrary node from the
     * deque can be removed with {@link CacheNode#withdrawNode(CacheNode)}
     * method.
     *
     * @param <K> key
     * @param <V> value
     */
    static class CacheDeque<K, V> {

        CacheNode<K, V> last;
        CacheNode<K, V> first;
        CacheDeque<K, V> nextDeque;

        /**
         * Constructs list and initializes last and first pointers.
         */
        CacheDeque() {
            last = new CacheNode<>();
            first = new CacheNode<>();
            last.next = first;
            first.prev = last;
        }

        /**
         * Puts the node with specified key and value at the end of the deque
         * and returns node.
         *
         * @param key   key
         * @param value value
         * @return added node
         */
        CacheNode<K, V> addLast(final K key, final V value) {
            CacheNode<K, V> node = new CacheNode<>(key, value);
            node.owner = this;
            node.next = last.next;
            node.prev = last;
            node.next.prev = node;
            last.next = node;
            return node;
        }

        CacheNode<K, V> addLastNode(final CacheNode<K, V> node) {
            node.owner = this;
            node.next = last.next;
            node.prev = last;
            node.next.prev = node;
            last.next = node;
            return node;
        }

        /**
         * Retrieves and removes the first node of this deque.
         *
         * @return removed node
         */
        CacheNode<K, V> pollFirst() {
            CacheNode<K, V> node = null;
            if (first.prev != last) {
                node = first.prev;
                first.prev = node.prev;
                first.prev.next = first;
                node.prev = null;
                node.next = null;
            }
            return node;
        }

        /**
         * Checks if link to the last node points to link to the first node.
         *
         * @return is deque empty
         */
        boolean isEmpty() {
            return last.next == first;
        }
    }
}
