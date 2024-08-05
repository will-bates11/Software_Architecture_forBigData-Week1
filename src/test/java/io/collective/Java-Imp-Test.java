package io.collective;

import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

import static org.junit.jupiter.api.Assertions.*;

public class SimpleAgedCacheTest {

    @Test
    public void testCacheIsEmptyInitially() {
        SimpleAgedCache cache = new SimpleAgedCache();
        assertTrue(cache.isEmpty());
        assertEquals(0, cache.size());
    }

    @Test
    public void testPutAndGet() {
        SimpleAgedCache cache = new SimpleAgedCache();
        cache.put("key1", "value1", 1000);
        assertFalse(cache.isEmpty());
        assertEquals(1, cache.size());
        assertEquals("value1", cache.get("key1"));
    }

    @Test
    public void testEntryExpiration() {
        Clock fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        SimpleAgedCache cache = new SimpleAgedCache(fixedClock);

        cache.put("key1", "value1", 1000);

        Clock laterClock = Clock.offset(fixedClock, java.time.Duration.ofMillis(1001));
        cache = new SimpleAgedCache(laterClock);
        assertNull(cache.get("key1"));
        assertTrue(cache.isEmpty());
        assertEquals(0, cache.size());
    }
}