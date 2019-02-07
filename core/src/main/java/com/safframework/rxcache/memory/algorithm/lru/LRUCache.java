package com.safframework.rxcache.memory.algorithm.lru;

import com.safframework.rxcache.config.Constant;
import com.safframework.rxcache.domain.CacheStatistics;
import com.safframework.rxcache.exception.RxCacheException;

import java.util.AbstractQueue;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by tony on 2018/10/21.
 */
public class LRUCache<K,V> {

    private Map<K,V> cache = null;
    private AbstractQueue<K> queue = null;
    private CacheStatistics cacheStatistics;
    private int size = 0;

    public LRUCache() {

        this(Constant.DEFAULT_CACHE_SIZE);
    }

    public LRUCache(int size) {

        this.size = size;
        this.cache = new ConcurrentHashMap<K,V>(size);
        this.queue = new ConcurrentLinkedQueue<K>();
        this.cacheStatistics = new CacheStatistics(size);
    }

    public boolean containsKey(K key) {

        return cache.containsKey(key);
    }

    public V get(K key) {

        //Recently accessed, hence move it to the tail
        queue.remove(key);
        queue.add(key);
        V cacheValue = cache.get(key);

        if(cacheValue != null) {
            cacheStatistics.incrementHitCount(); // 缓存命中，则增加命中的数量
        } else {
            cacheStatistics.incrementMissCount();// 缓存没命中，则增加没命中的数量
        }
        return cacheValue;
    }

    public V getSilent(K key) {

        return cache.get(key);
    }

    public void put(K key, V value) {

        if(key == null || value == null) throw new RxCacheException("key is null or value is null");

        if(cache.containsKey(key)) {
            queue.remove(key);
        } else {
            cacheStatistics.incrementMissCount(); // 缓存没命中，则增加没命中的数量
            cacheStatistics.incrementPutCount();  // 增加缓存put的数量
        }

        if(queue.size() >= size) {
            K lruKey = queue.poll();
            if(lruKey != null) {
                cache.remove(lruKey);
                cacheStatistics.incrementEvictionCount(); // 增加缓存删除的数量
            }
        }

        queue.add(key);
        cache.put(key,value);
    }

    /**
     * 获取最近最少使用的值
     * @return
     */
    public V getLeastRecentlyUsed() {

        K remove = queue.remove();
        queue.add(remove);
        cacheStatistics.incrementHitCount(); // 增加缓存命中的数量
        return cache.get(remove);
    }

    public Set<K> keySet() {

        return cache.keySet();
    }

    public void remove(K key) {

        cache.remove(key);
        queue.remove(key);
        cacheStatistics.incrementEvictionCount(); // 增加缓存删除的数量
    }

    public void clear() {

        cacheStatistics.incrementEvictionCount(queue.size());
        cache.clear();
        queue.clear();
    }

    public CacheStatistics getCacheStatistics() {
        return cacheStatistics;
    }

    @Override
    public String toString() {

        Iterator<K> iterator = queue.iterator();
        StringBuilder sb = new StringBuilder();

        while (iterator.hasNext()) {
            K key = iterator.next();
            sb.append("{ ");
            sb.append(key);
            sb.append(":");
            sb.append(this.getSilent(key));
            sb.append(" }");
            if(iterator.hasNext()) {
                sb.append(", ");
            }
        }
        return sb.toString();
    }
}
