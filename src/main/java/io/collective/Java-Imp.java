package io.collective;

import java.time.Clock;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleAgedCache {

    private final Clock clock;
    private final List<ExpirableEntry> cache;

    public SimpleAgedCache(Clock clock) {
        this.clock = clock;
        this.cache = new ArrayList<>();
    }

    public SimpleAgedCache() {
        this(Clock.systemDefaultZone());
    }

    public void put(Object key, Object value, int retentionInMillis) {
        long expiryTime = clock.millis() + retentionInMillis;
        ExpirableEntry entry = new ExpirableEntry(key, value, expiryTime);
        cache.add(entry);
    }

    public boolean isEmpty() {
        removeExpiredEntries();
        return cache.isEmpty();
    }

    public int size() {
        removeExpiredEntries();
        return cache.size();
    }

    public Object get(Object key) {
        removeExpiredEntries();
        for (ExpirableEntry entry : cache) {
            if (entry.key.equals(key)) {
                return entry.value;
            }
        }
        return null;
    }

    private void removeExpiredEntries() {
        long currentTime = clock.millis();
        Iterator<ExpirableEntry> iterator = cache.iterator();
        while (iterator.hasNext()) {
            ExpirableEntry entry = iterator.next();
            if (entry.expiryTime < currentTime) {
                iterator.remove();
            }
        }
    }

    private class ExpirableEntry {
        private final Object key;
        private final Object value;
        private final long expiryTime;

        public ExpirableEntry(Object key, Object value, long expiryTime) {
            this.key = key;
            this.value = value;
            this.expiryTime = expiryTime;
        }
    }
}