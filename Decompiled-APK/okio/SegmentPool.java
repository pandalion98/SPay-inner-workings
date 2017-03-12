package okio;

final class SegmentPool {
    static final long MAX_SIZE = 65536;
    static long byteCount;
    static Segment next;

    private SegmentPool() {
    }

    static Segment take() {
        synchronized (SegmentPool.class) {
            if (next != null) {
                Segment segment = next;
                next = segment.next;
                segment.next = null;
                byteCount -= 2048;
                return segment;
            }
            return new Segment();
        }
    }

    static void recycle(Segment segment) {
        if (segment.next != null || segment.prev != null) {
            throw new IllegalArgumentException();
        } else if (!segment.shared) {
            synchronized (SegmentPool.class) {
                if (byteCount + 2048 > MAX_SIZE) {
                    return;
                }
                byteCount += 2048;
                segment.next = next;
                segment.limit = 0;
                segment.pos = 0;
                next = segment;
            }
        }
    }
}
