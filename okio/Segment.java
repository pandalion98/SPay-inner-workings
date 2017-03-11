package okio;

final class Segment {
    static final int SIZE = 2048;
    final byte[] data;
    int limit;
    Segment next;
    boolean owner;
    int pos;
    Segment prev;
    boolean shared;

    Segment() {
        this.data = new byte[SIZE];
        this.owner = true;
        this.shared = false;
    }

    Segment(Segment segment) {
        this(segment.data, segment.pos, segment.limit);
        segment.shared = true;
    }

    Segment(byte[] bArr, int i, int i2) {
        this.data = bArr;
        this.pos = i;
        this.limit = i2;
        this.owner = false;
        this.shared = true;
    }

    public Segment pop() {
        Segment segment = this.next != this ? this.next : null;
        this.prev.next = this.next;
        this.next.prev = this.prev;
        this.next = null;
        this.prev = null;
        return segment;
    }

    public Segment push(Segment segment) {
        segment.prev = this;
        segment.next = this.next;
        this.next.prev = segment;
        this.next = segment;
        return segment;
    }

    public Segment split(int i) {
        if (i <= 0 || i > this.limit - this.pos) {
            throw new IllegalArgumentException();
        }
        Segment segment = new Segment(this);
        segment.limit = segment.pos + i;
        this.pos += i;
        this.prev.push(segment);
        return segment;
    }

    public void compact() {
        if (this.prev == this) {
            throw new IllegalStateException();
        } else if (this.prev.owner) {
            int i = this.limit - this.pos;
            if (i <= (this.prev.shared ? 0 : this.prev.pos) + (2048 - this.prev.limit)) {
                writeTo(this.prev, i);
                pop();
                SegmentPool.recycle(this);
            }
        }
    }

    public void writeTo(Segment segment, int i) {
        if (segment.owner) {
            if (segment.limit + i > SIZE) {
                if (segment.shared) {
                    throw new IllegalArgumentException();
                } else if ((segment.limit + i) - segment.pos > SIZE) {
                    throw new IllegalArgumentException();
                } else {
                    System.arraycopy(segment.data, segment.pos, segment.data, 0, segment.limit - segment.pos);
                    segment.limit -= segment.pos;
                    segment.pos = 0;
                }
            }
            System.arraycopy(this.data, this.pos, segment.data, segment.limit, i);
            segment.limit += i;
            this.pos += i;
            return;
        }
        throw new IllegalArgumentException();
    }
}
