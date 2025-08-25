package com.example.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    
    private static Map<String, Object> globalCache = new ConcurrentHashMap<>();
    private static volatile Cache instance;
    private Map<String, Object> instanceCache;
    
    public Cache() {
        this.instanceCache = new HashMap<>();
    }
    
    public static void putGlobal(String key, Object value) {
        // FIX: Now using thread-safe ConcurrentHashMap
        globalCache.put(key, value);
    }
    
    public static Object getGlobal(String key) {
        return globalCache.get(key);
    }
    
    public static void clearGlobal() {
        globalCache.clear();
    }
    
    // FIX: Thread-safe increment using ConcurrentHashMap atomic operations
    public static void incrementCounter(String key) {
        globalCache.compute(key, (k, v) -> {
            int count = (v instanceof Integer) ? (Integer) v : 0;
            return count + 1;
        });
    }
    
    // FIX: Proper double-checked locking pattern for thread-safe singleton
    public static Cache getInstance() {
        if (instance == null) {
            synchronized (Cache.class) {
                // Second null check to prevent multiple instance creation
                if (instance == null) {
                    instance = new Cache();
                    // Simulate some initialization work
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
        }
        return instance;
    }
    
    public void put(String key, Object value) {
        instanceCache.put(key, value);
    }
    
    public Object get(String key) {
        return instanceCache.get(key);
    }
    
    public void clear() {
        instanceCache.clear();
    }
    
    public boolean containsKey(String key) {
        return instanceCache.containsKey(key);
    }
    
    public int size() {
        return instanceCache.size();
    }
    
    public static class CacheEntry {
        private String key;
        private Object value;
        private long timestamp;
        private long ttl;
        
        public CacheEntry(String key, Object value, long ttl) {
            this.key = key;
            this.value = value;
            this.timestamp = System.currentTimeMillis();
            this.ttl = ttl;
        }
        
        public boolean isExpired() {
            return ttl > 0 && (System.currentTimeMillis() - timestamp) > ttl;
        }
        
        public String getKey() { return key; }
        public Object getValue() { return value; }
        public long getTimestamp() { return timestamp; }
        public long getTtl() { return ttl; }
    }
}