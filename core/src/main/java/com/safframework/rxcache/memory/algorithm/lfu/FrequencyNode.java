package com.safframework.rxcache.memory.algorithm.lfu;

/**
 * Created by tony on 2018/10/22.
 */
class FrequencyNode extends Node {

    int frequency;
    NodeList lfuCacheEntryList;

    public FrequencyNode(int frequency) {
        this.frequency = frequency;
        lfuCacheEntryList = new NodeList();
    }

    public boolean equals(Object o) {
        return frequency == ((FrequencyNode) o).frequency;
    }

    public int hashCode() {
        return frequency * 31;
    }

    public String toString() {
        return Integer.toString(frequency);
    }
}
