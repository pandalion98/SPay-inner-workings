package okio;

import java.io.OutputStream;
import java.util.Arrays;

final class SegmentedByteString extends ByteString {
    final transient int[] directory;
    final transient byte[][] segments;

    SegmentedByteString(Buffer buffer, int i) {
        int i2 = 0;
        super(null);
        Util.checkOffsetAndCount(buffer.size, 0, (long) i);
        Segment segment = buffer.head;
        int i3 = 0;
        int i4 = 0;
        while (i4 < i) {
            if (segment.limit == segment.pos) {
                throw new AssertionError("s.limit == s.pos");
            }
            i4 += segment.limit - segment.pos;
            i3++;
            segment = segment.next;
        }
        this.segments = new byte[i3][];
        this.directory = new int[(i3 * 2)];
        segment = buffer.head;
        i3 = 0;
        while (i2 < i) {
            this.segments[i3] = segment.data;
            i2 += segment.limit - segment.pos;
            this.directory[i3] = i2;
            this.directory[this.segments.length + i3] = segment.pos;
            segment.shared = true;
            i3++;
            segment = segment.next;
        }
    }

    public String utf8() {
        return toByteString().utf8();
    }

    public String base64() {
        return toByteString().base64();
    }

    public String hex() {
        return toByteString().hex();
    }

    public ByteString toAsciiLowercase() {
        return toByteString().toAsciiLowercase();
    }

    public ByteString toAsciiUppercase() {
        return toByteString().toAsciiUppercase();
    }

    public ByteString md5() {
        return toByteString().md5();
    }

    public ByteString sha256() {
        return toByteString().sha256();
    }

    public String base64Url() {
        return toByteString().base64Url();
    }

    public ByteString substring(int i) {
        return toByteString().substring(i);
    }

    public ByteString substring(int i, int i2) {
        return toByteString().substring(i, i2);
    }

    public byte getByte(int i) {
        Util.checkOffsetAndCount((long) this.directory[this.segments.length - 1], (long) i, 1);
        int segment = segment(i);
        return this.segments[segment][(i - (segment == 0 ? 0 : this.directory[segment - 1])) + this.directory[this.segments.length + segment]];
    }

    private int segment(int i) {
        int binarySearch = Arrays.binarySearch(this.directory, 0, this.segments.length, i + 1);
        return binarySearch >= 0 ? binarySearch : binarySearch ^ -1;
    }

    public int size() {
        return this.directory[this.segments.length - 1];
    }

    public byte[] toByteArray() {
        int i = 0;
        Object obj = new byte[this.directory[this.segments.length - 1]];
        int length = this.segments.length;
        int i2 = 0;
        while (i < length) {
            int i3 = this.directory[length + i];
            int i4 = this.directory[i];
            System.arraycopy(this.segments[i], i3, obj, i2, i4 - i2);
            i++;
            i2 = i4;
        }
        return obj;
    }

    public void write(OutputStream outputStream) {
        int i = 0;
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        int length = this.segments.length;
        int i2 = 0;
        while (i < length) {
            int i3 = this.directory[length + i];
            int i4 = this.directory[i];
            outputStream.write(this.segments[i], i3, i4 - i2);
            i++;
            i2 = i4;
        }
    }

    void write(Buffer buffer) {
        int i = 0;
        int length = this.segments.length;
        int i2 = 0;
        while (i < length) {
            int i3 = this.directory[length + i];
            int i4 = this.directory[i];
            Segment segment = new Segment(this.segments[i], i3, (i3 + i4) - i2);
            if (buffer.head == null) {
                segment.prev = segment;
                segment.next = segment;
                buffer.head = segment;
            } else {
                buffer.head.prev.push(segment);
            }
            i++;
            i2 = i4;
        }
        buffer.size = ((long) i2) + buffer.size;
    }

    public boolean rangeEquals(int i, ByteString byteString, int i2, int i3) {
        if (i > size() - i3) {
            return false;
        }
        int segment = segment(i);
        while (i3 > 0) {
            int i4 = segment == 0 ? 0 : this.directory[segment - 1];
            int min = Math.min(i3, ((this.directory[segment] - i4) + i4) - i);
            if (!byteString.rangeEquals(i2, this.segments[segment], (i - i4) + this.directory[this.segments.length + segment], min)) {
                return false;
            }
            i += min;
            i2 += min;
            i3 -= min;
            segment++;
        }
        return true;
    }

    public boolean rangeEquals(int i, byte[] bArr, int i2, int i3) {
        if (i > size() - i3 || i2 > bArr.length - i3) {
            return false;
        }
        int segment = segment(i);
        while (i3 > 0) {
            int i4 = segment == 0 ? 0 : this.directory[segment - 1];
            int min = Math.min(i3, ((this.directory[segment] - i4) + i4) - i);
            if (!Util.arrayRangeEquals(this.segments[segment], (i - i4) + this.directory[this.segments.length + segment], bArr, i2, min)) {
                return false;
            }
            i += min;
            i2 += min;
            i3 -= min;
            segment++;
        }
        return true;
    }

    private ByteString toByteString() {
        return new ByteString(toByteArray());
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        boolean z = (obj instanceof ByteString) && ((ByteString) obj).size() == size() && rangeEquals(0, (ByteString) obj, 0, size());
        return z;
    }

    public int hashCode() {
        int i = this.hashCode;
        if (i == 0) {
            i = 1;
            int length = this.segments.length;
            int i2 = 0;
            int i3 = 0;
            while (i2 < length) {
                byte[] bArr = this.segments[i2];
                int i4 = this.directory[length + i2];
                int i5 = this.directory[i2];
                i3 = (i5 - i3) + i4;
                int i6 = i4;
                i4 = i;
                for (i = i6; i < i3; i++) {
                    i4 = (i4 * 31) + bArr[i];
                }
                i2++;
                i3 = i5;
                i = i4;
            }
            this.hashCode = i;
        }
        return i;
    }

    public String toString() {
        return toByteString().toString();
    }

    private Object writeReplace() {
        return toByteString();
    }
}
