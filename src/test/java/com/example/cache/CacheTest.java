package com.example.cache;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class CacheTest {
    
    private Cache cache;
    
    @BeforeEach
    void setUp() {
        cache = new Cache();
        Cache.clearGlobal();
    }
    
    @Test
    void put_and_get_shouldStoreAndRetrieveValue() {
        cache.put("key1", "value1");
        assertEquals("value1", cache.get("key1"));
    }
    
    @Test
    void get_withNonExistentKey_returnsNull() {
        assertNull(cache.get("nonexistent"));
    }
    
    @Test
    void containsKey_withExistingKey_returnsTrue() {
        cache.put("key1", "value1");
        assertTrue(cache.containsKey("key1"));
    }
    
    @Test
    void containsKey_withNonExistentKey_returnsFalse() {
        assertFalse(cache.containsKey("nonexistent"));
    }
    
    @Test
    void size_withEmptyCache_returnsZero() {
        assertEquals(0, cache.size());
    }
    
    @Test
    void size_withItems_returnsCorrectSize() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        assertEquals(2, cache.size());
    }
    
    @Test
    void clear_shouldRemoveAllItems() {
        cache.put("key1", "value1");
        cache.put("key2", "value2");
        cache.clear();
        assertEquals(0, cache.size());
    }
    
    @Test
    void globalCache_putAndGet_shouldWork() {
        Cache.putGlobal("globalKey", "globalValue");
        assertEquals("globalValue", Cache.getGlobal("globalKey"));
    }
    
    @Test
    void getInstance_shouldReturnSameInstance() {
        Cache instance1 = Cache.getInstance();
        Cache instance2 = Cache.getInstance();
        assertSame(instance1, instance2);
    }
    
    @Test
    void cacheEntry_isExpired_withTtl() {
        Cache.CacheEntry entry = new Cache.CacheEntry("key", "value", 100);
        assertFalse(entry.isExpired());
        
        Cache.CacheEntry expiredEntry = new Cache.CacheEntry("key", "value", -1);
        try {
            Thread.sleep(1);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        assertTrue(expiredEntry.isExpired());
    }
}