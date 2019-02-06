package com.safframework.rxcache.domain;

import com.google.gson.Gson;

import java.util.Set;

/**
 * Created by tony on 2019-02-06.
 */
public class CacheInfo {

    private boolean hasMemory;
    private boolean hasPersistence;
    private Set<String> keys;

    private CacheInfo(Builder builder) {

        hasMemory = builder.hasMemory;
        hasPersistence = builder.hasPersistence;
    }

    public static final class Builder {

        private boolean hasMemory;
        private boolean hasPersistence;
        private Set<String> keys;

        public Builder() {
        }

        public CacheInfo.Builder hasMemory(boolean hasMemory) {
            this.hasMemory = hasMemory;
            return this;
        }

        public CacheInfo.Builder hasPersistence(boolean hasPersistence) {
            this.hasPersistence = hasPersistence;
            return this;
        }

        public CacheInfo.Builder keys(Set<String> keys) {
            this.keys = keys;
            return this;
        }

        public CacheInfo build() {

            return new CacheInfo(this);
        }
    }

    public String toString() {

        return new Gson().toJson(this);
    }
}
