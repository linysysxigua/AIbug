package com.example.cache;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {
    
    private static Map<String, Object> globalCache = new HashMap<>();
    private static volatile Cache instance;
    private Map<String, Object> instanceCache;
    
    public Cache() {
        this.instanceCache = new HashMap<>();
    }
    
    public static void putGlobal(String key, Object value) {
        globalCache.put(key, value);
    }
    
    public static Object getGlobal(String key) {
        return globalCache.get(key);
    }
    
    public static void clearGlobal() {
        globalCache.clear();
    }
    
    public static Cache getInstance() {
        if (instance == null) {
            instance = new Cache();
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