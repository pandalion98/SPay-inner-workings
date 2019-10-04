/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.OutputStream
 *  java.lang.AssertionError
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.System
 *  java.util.Arrays
 */
package okio;

import java.io.OutputStream;
import java.util.Arrays;
import okio.Buffer;
import okio.ByteString;
import okio.Segment;
import okio.Util;

final class SegmentedByteString
extends ByteString {
    final transient int[] directory;
    final transient byte[][] segments;

    SegmentedByteString(Buffer buffer, int n2) {
        int n3 = 0;
        super(null);
        Util.checkOffsetAndCount(buffer.size, 0L, n2);
        Segment segment = buffer.head;
        int n4 = 0;
        int n5 = 0;
        while (n5 < n2) {
            if (segment.limit == segment.pos) {
                throw new AssertionError((Object)"s.limit == s.pos");
            }
            n5 += segment.limit - segment.pos;
            ++n4;
            segment = segment.next;
        }
        this.segments = new byte[n4][];
        this.directory = new int[n4 * 2];
        Segment segment2 = buffer.head;
        int n6 = 0;
        while (n3 < n2) {
            this.segments[n6] = segment2.data;
            this.directory[n6] = n3 += segment2.limit - segment2.pos;
            this.directory[n6 + this.segments.length] = segment2.pos;
            segment2.shared = true;
            ++n6;
            segment2 = segment2.next;
        }
    }

    private int segment(int n2) {
        int n3 = Arrays.binarySearch((int[])this.directory, (int)0, (int)this.segments.length, (int)(n2 + 1));
        if (n3 >= 0) {
            return n3;
        }
        return ~n3;
    }

    private ByteString toByteString() {
        return new ByteString(this.toByteArray());
    }

    private Object writeReplace() {
        return this.toByteString();
    }

    @Override
    public String base64() {
        return this.toByteString().base64();
    }

    @Override
    public String base64Url() {
        return this.toByteString().base64Url();
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    @Override
    public boolean equals(Object object) {
        if (object == this) {
            return true;
        }
        if (!(object instanceof ByteString)) return false;
        if (((ByteString)object).size() != this.size()) return false;
        if (!this.rangeEquals(0, (ByteString)object, 0, this.size())) return false;
        return true;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public byte getByte(int n2) {
        Util.checkOffsetAndCount(this.directory[-1 + this.segments.length], n2, 1L);
        int n3 = this.segment(n2);
        int n4 = n3 == 0 ? 0 : this.directory[n3 - 1];
        int n5 = this.directory[n3 + this.segments.length];
        return this.segments[n3][n5 + (n2 - n4)];
    }

    @Override
    public int hashCode() {
        int n2 = this.hashCode;
        if (n2 != 0) {
            return n2;
        }
        int n3 = 1;
        int n4 = this.segments.length;
        int n5 = 0;
        for (int i2 = 0; i2 < n4; ++i2) {
            byte[] arrby = this.segments[i2];
            int n6 = this.directory[n4 + i2];
            int n7 = this.directory[i2];
            int n8 = n6 + (n7 - n5);
            int n9 = n3;
            for (int i3 = n6; i3 < n8; ++i3) {
                n9 = n9 * 31 + arrby[i3];
            }
            n5 = n7;
            n3 = n9;
        }
        this.hashCode = n3;
        return n3;
    }

    @Override
    public String hex() {
        return this.toByteString().hex();
    }

    @Override
    public ByteString md5() {
        return this.toByteString().md5();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean rangeEquals(int n2, ByteString byteString, int n3, int n4) {
        if (n2 <= this.size() - n4) {
            int n5 = this.segment(n2);
            do {
                int n6;
                if (n4 <= 0) {
                    return true;
                }
                int n7 = n5 == 0 ? 0 : this.directory[n5 - 1];
                int n8 = this.directory[n5 + this.segments.length] + (n2 - n7);
                if (!byteString.rangeEquals(n3, this.segments[n5], n8, n6 = Math.min((int)n4, (int)(n7 + (this.directory[n5] - n7) - n2)))) break;
                n2 += n6;
                n3 += n6;
                n4 -= n6;
                ++n5;
            } while (true);
        }
        return false;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean rangeEquals(int n2, byte[] arrby, int n3, int n4) {
        if (n2 <= this.size() - n4 && n3 <= arrby.length - n4) {
            int n5 = this.segment(n2);
            do {
                int n6;
                if (n4 <= 0) {
                    return true;
                }
                int n7 = n5 == 0 ? 0 : this.directory[n5 - 1];
                int n8 = this.directory[n5 + this.segments.length] + (n2 - n7);
                if (!Util.arrayRangeEquals(this.segments[n5], n8, arrby, n3, n6 = Math.min((int)n4, (int)(n7 + (this.directory[n5] - n7) - n2)))) break;
                n2 += n6;
                n3 += n6;
                n4 -= n6;
                ++n5;
            } while (true);
        }
        return false;
    }

    @Override
    public ByteString sha256() {
        return this.toByteString().sha256();
    }

    @Override
    public int size() {
        return this.directory[-1 + this.segments.length];
    }

    @Override
    public ByteString substring(int n2) {
        return this.toByteString().substring(n2);
    }

    @Override
    public ByteString substring(int n2, int n3) {
        return this.toByteString().substring(n2, n3);
    }

    @Override
    public ByteString toAsciiLowercase() {
        return this.toByteString().toAsciiLowercase();
    }

    @Override
    public ByteString toAsciiUppercase() {
        return this.toByteString().toAsciiUppercase();
    }

    @Override
    public byte[] toByteArray() {
        byte[] arrby = new byte[this.directory[-1 + this.segments.length]];
        int n2 = this.segments.length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = this.directory[n2 + i2];
            int n5 = this.directory[i2];
            System.arraycopy((Object)this.segments[i2], (int)n4, (Object)arrby, (int)n3, (int)(n5 - n3));
            n3 = n5;
        }
        return arrby;
    }

    @Override
    public String toString() {
        return this.toByteString().toString();
    }

    @Override
    public String utf8() {
        return this.toByteString().utf8();
    }

    @Override
    public void write(OutputStream outputStream) {
        if (outputStream == null) {
            throw new IllegalArgumentException("out == null");
        }
        int n2 = this.segments.length;
        int n3 = 0;
        for (int i2 = 0; i2 < n2; ++i2) {
            int n4 = this.directory[n2 + i2];
            int n5 = this.directory[i2];
            outputStream.write(this.segments[i2], n4, n5 - n3);
            n3 = n5;
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    void write(Buffer buffer) {
        int n2 = 0;
        int n3 = this.segments.length;
        int n4 = 0;
        do {
            if (n2 >= n3) {
                buffer.size += (long)n4;
                return;
            }
            int n5 = this.directory[n3 + n2];
            int n6 = this.directory[n2];
            Segment segment = new Segment(this.segments[n2], n5, n5 + n6 - n4);
            if (buffer.head == null) {
                segment.prev = segment;
                segment.next = segment;
                buffer.head = segment;
            } else {
                buffer.head.prev.push(segment);
            }
            ++n2;
            n4 = n6;
        } while (true);
    }
}

