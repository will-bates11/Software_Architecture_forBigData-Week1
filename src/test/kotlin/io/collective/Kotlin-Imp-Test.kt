package io.collective

import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class SimpleAgedCacheTest {

    @Test
    fun testCacheIsEmptyInitially() {
        val cache = SimpleAgedCache()
        assertTrue(cache.isEmpty())
        assertEquals(0, cache.size())
    }

    @Test
    fun testPutAndGet() {
        val cache = SimpleAgedCache()
        cache.put("key1", "value1", 1000)
        assertTrue(!cache.isEmpty())
        assertEquals(1, cache.size())
        assertEquals("value1", cache.get("key1"))
    }

    @Test
    fun testEntryExpiration() {
        val fixedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault())
        var cache = SimpleAgedCache(fixedClock)

        cache.put("key1", "value1", 1000)

        val laterClock = Clock.offset(fixedClock, java.time.Duration.ofMillis(1001))
        cache = SimpleAgedCache(laterClock)
        assertNull(cache.get("key1"))
        assertTrue(cache.isEmpty())
        assertEquals(0, cache.size())
    }
}