package com.safframework.rxcache.domain;

/**
 * 缓存统计的类
 * Created by tony on 2019-02-07.
 */
public class CacheStatistics {

    private int size = 0;         // 缓存的大小
    private int putCount = 0;     // 缓存put的数量
    private int evictionCount = 0;// 缓存删除的数量
    private int hitCount = 0;     // 缓存的命中数
    private int missCount = 0;    // 缓存的未命中数

    public CacheStatistics(int size) {
        this.size = size;
    }

    public CacheStatistics(int size,int putCount,int evictionCount,int hitCount,int missCount) {

        this.size = size;
        this.putCount = putCount;
        this.evictionCount = evictionCount;
        this.hitCount = hitCount;
        this.missCount = missCount;
    }

    public int getSize() { return this.size; }

    public int getPutCount() {
        return putCount;
    }

    public void setPutCount(int putCount) {
        this.putCount = putCount;
    }

    public int getEvictionCount() {
        return evictionCount;
    }

    public void setEvictionCount(int evictionCount) {
        this.evictionCount = evictionCount;
    }

    public int getHitCount() {
        return hitCount;
    }

    public void setHitCount(int hitCount) {
        this.hitCount = hitCount;
    }

    public int getMissCount() {
        return missCount;
    }

    public void setMissCount(int missCount) {
        this.missCount = missCount;
    }

    public void incrementPutCount() { this.putCount++; }

    public void incrementEvictionCount() { this.evictionCount++; }

    public void incrementEvictionCount(int count) {

        this.evictionCount += count;
    }

    public void incrementHitCount() { this.hitCount++; }

    public void incrementMissCount() { this.missCount++; }
}
