/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Class
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 */
package okio;

import okio.Segment;

final class SegmentPool {
    static final long MAX_SIZE = 65536L;
    static long byteCount;
    static Segment next;

    private SegmentPool() {
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static void recycle(Segment segment) {
        if (segment.next != null || segment.prev != null) {
            throw new IllegalArgumentException();
        }
        if (segment.shared) {
            return;
        }
        Class<SegmentPool> class_ = SegmentPool.class;
        synchronized (SegmentPool.class) {
            if (2048L + byteCount > 65536L) {
                // ** MonitorExit[var2_1] (shouldn't be in output)
                return;
            }
            byteCount = 2048L + byteCount;
            segment.next = next;
            segment.limit = 0;
            segment.pos = 0;
            next = segment;
            // ** MonitorExit[var2_1] (shouldn't be in output)
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    static Segment take() {
        Class<SegmentPool> class_ = SegmentPool.class;
        synchronized (SegmentPool.class) {
            if (next != null) {
                Segment segment = next;
                next = segment.next;
                segment.next = null;
                byteCount -= 2048L;
                // ** MonitorExit[var2] (shouldn't be in output)
                return segment;
            }
            // ** MonitorExit[var2] (shouldn't be in output)
            return new Segment();
        }
    }
}

