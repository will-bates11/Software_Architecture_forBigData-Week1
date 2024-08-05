package io.collective

import java.time.Clock
import java.util.*

class SimpleAgedCache(private val clock: Clock = Clock.systemDefaultZone()) {

    private val cache: MutableList<ExpirableEntry> = ArrayList()

    fun put(key: Any, value: Any, retentionInMillis: Int) {
        val expiryTime = clock.millis() + retentionInMillis
        val entry = ExpirableEntry(key, value, expiryTime)
        cache.add(entry)
    }

    fun isEmpty(): Boolean {
        removeExpiredEntries()
        return cache.isEmpty()
    }

    fun size(): Int {
        removeExpiredEntries()
        return cache.size
    }

    fun get(key: Any): Any? {
        removeExpiredEntries()
        for (entry in cache) {
            if (entry.key == key) {
                return entry.value
            }
        }
        return null
    }

    private fun removeExpiredEntries() {
        val currentTime = clock.millis()
        val iterator = cache.iterator()
        while (iterator.hasNext()) {
            val entry = iterator.next()
            if (entry.expiryTime < currentTime) {
                iterator.remove()
            }
        }
    }

    private data class ExpirableEntry(
        val key: Any,
        val value: Any,
        val expiryTime: Long
    )
}