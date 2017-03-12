package android.util;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

public class LruCache<K, V> {
    private int createCount;
    private int evictionCount;
    private int hitCount;
    private final LinkedHashMap<K, V> map;
    private int maxSize;
    private int missCount;
    private int putCount;
    private int size;

    public LruCache(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        this.maxSize = maxSize;
        this.map = new LinkedHashMap(0, 0.75f, true);
    }

    public void resize(int maxSize) {
        if (maxSize <= 0) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        synchronized (this) {
            this.maxSize = maxSize;
        }
        trimToSize(maxSize);
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public final V get(K r5) {
        /*
        r4 = this;
        if (r5 != 0) goto L_0x000a;
    L_0x0002:
        r2 = new java.lang.NullPointerException;
        r3 = "key == null";
        r2.<init>(r3);
        throw r2;
    L_0x000a:
        monitor-enter(r4);
        r2 = r4.map;	 Catch:{ all -> 0x002b }
        r1 = r2.get(r5);	 Catch:{ all -> 0x002b }
        if (r1 == 0) goto L_0x001c;
    L_0x0013:
        r2 = r4.hitCount;	 Catch:{ all -> 0x002b }
        r2 = r2 + 1;
        r4.hitCount = r2;	 Catch:{ all -> 0x002b }
        monitor-exit(r4);	 Catch:{ all -> 0x002b }
        r0 = r1;
    L_0x001b:
        return r0;
    L_0x001c:
        r2 = r4.missCount;	 Catch:{ all -> 0x002b }
        r2 = r2 + 1;
        r4.missCount = r2;	 Catch:{ all -> 0x002b }
        monitor-exit(r4);	 Catch:{ all -> 0x002b }
        r0 = r4.create(r5);
        if (r0 != 0) goto L_0x002e;
    L_0x0029:
        r0 = 0;
        goto L_0x001b;
    L_0x002b:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x002b }
        throw r2;
    L_0x002e:
        monitor-enter(r4);
        r2 = r4.createCount;	 Catch:{ all -> 0x0055 }
        r2 = r2 + 1;
        r4.createCount = r2;	 Catch:{ all -> 0x0055 }
        r2 = r4.map;	 Catch:{ all -> 0x0055 }
        r1 = r2.put(r5, r0);	 Catch:{ all -> 0x0055 }
        if (r1 == 0) goto L_0x004b;
    L_0x003d:
        r2 = r4.map;	 Catch:{ all -> 0x0055 }
        r2.put(r5, r1);	 Catch:{ all -> 0x0055 }
    L_0x0042:
        monitor-exit(r4);	 Catch:{ all -> 0x0055 }
        if (r1 == 0) goto L_0x0058;
    L_0x0045:
        r2 = 0;
        r4.entryRemoved(r2, r5, r0, r1);
        r0 = r1;
        goto L_0x001b;
    L_0x004b:
        r2 = r4.size;	 Catch:{ all -> 0x0055 }
        r3 = r4.safeSizeOf(r5, r0);	 Catch:{ all -> 0x0055 }
        r2 = r2 + r3;
        r4.size = r2;	 Catch:{ all -> 0x0055 }
        goto L_0x0042;
    L_0x0055:
        r2 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x0055 }
        throw r2;
    L_0x0058:
        r2 = r4.maxSize;
        r4.trimToSize(r2);
        goto L_0x001b;
        */
        throw new UnsupportedOperationException("Method not decompiled: android.util.LruCache.get(java.lang.Object):V");
    }

    public final V put(K key, V value) {
        if (key == null || value == null) {
            throw new NullPointerException("key == null || value == null");
        }
        V previous;
        synchronized (this) {
            this.putCount++;
            this.size += safeSizeOf(key, value);
            previous = this.map.put(key, value);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            entryRemoved(false, key, previous, value);
        }
        trimToSize(this.maxSize);
        return previous;
    }

    public void trimToSize(int maxSize) {
        while (true) {
            K key;
            V value;
            synchronized (this) {
                if (this.size >= 0 && (!this.map.isEmpty() || this.size == 0)) {
                    if (this.size <= maxSize) {
                        return;
                    }
                    Entry<K, V> toEvict = this.map.eldest();
                    if (toEvict == null) {
                        return;
                    }
                    key = toEvict.getKey();
                    value = toEvict.getValue();
                    this.map.remove(key);
                    this.size -= safeSizeOf(key, value);
                    this.evictionCount++;
                }
            }
            entryRemoved(true, key, value, null);
        }
        throw new IllegalStateException(getClass().getName() + ".sizeOf() is reporting inconsistent results!");
    }

    public final V remove(K key) {
        if (key == null) {
            throw new NullPointerException("key == null");
        }
        V previous;
        synchronized (this) {
            previous = this.map.remove(key);
            if (previous != null) {
                this.size -= safeSizeOf(key, previous);
            }
        }
        if (previous != null) {
            entryRemoved(false, key, previous, null);
        }
        return previous;
    }

    protected void entryRemoved(boolean evicted, K k, V v, V v2) {
    }

    protected V create(K k) {
        return null;
    }

    private int safeSizeOf(K key, V value) {
        int result = sizeOf(key, value);
        if (result >= 0) {
            return result;
        }
        throw new IllegalStateException("Negative size: " + key + "=" + value);
    }

    protected int sizeOf(K k, V v) {
        return 1;
    }

    public final void evictAll() {
        trimToSize(-1);
    }

    public final synchronized int size() {
        return this.size;
    }

    public final synchronized int maxSize() {
        return this.maxSize;
    }

    public final synchronized int hitCount() {
        return this.hitCount;
    }

    public final synchronized int missCount() {
        return this.missCount;
    }

    public final synchronized int createCount() {
        return this.createCount;
    }

    public final synchronized int putCount() {
        return this.putCount;
    }

    public final synchronized int evictionCount() {
        return this.evictionCount;
    }

    public final synchronized Map<K, V> snapshot() {
        return new LinkedHashMap(this.map);
    }

    public final synchronized String toString() {
        String format;
        int hitPercent = 0;
        synchronized (this) {
            int accesses = this.hitCount + this.missCount;
            if (accesses != 0) {
                hitPercent = (this.hitCount * 100) / accesses;
            }
            format = String.format("LruCache[maxSize=%d,hits=%d,misses=%d,hitRate=%d%%]", new Object[]{Integer.valueOf(this.maxSize), Integer.valueOf(this.hitCount), Integer.valueOf(this.missCount), Integer.valueOf(hitPercent)});
        }
        return format;
    }
}
