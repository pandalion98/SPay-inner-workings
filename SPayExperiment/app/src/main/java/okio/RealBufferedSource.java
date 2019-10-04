/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.IOException
 *  java.io.InputStream
 *  java.lang.AssertionError
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.nio.charset.Charset
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

final class RealBufferedSource
implements BufferedSource {
    public final Buffer buffer;
    private boolean closed;
    public final Source source;

    public RealBufferedSource(Source source) {
        this(source, new Buffer());
    }

    public RealBufferedSource(Source source, Buffer buffer) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.buffer = buffer;
        this.source = source;
    }

    @Override
    public Buffer buffer() {
        return this.buffer;
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.closed = true;
        this.source.close();
        this.buffer.clear();
    }

    @Override
    public boolean exhausted() {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        return this.buffer.exhausted() && this.source.read(this.buffer, 2048L) == -1L;
    }

    @Override
    public long indexOf(byte by) {
        return this.indexOf(by, 0L);
    }

    @Override
    public long indexOf(byte by, long l2) {
        long l3;
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (l2 >= this.buffer.size) {
            if (this.source.read(this.buffer, 2048L) != -1L) continue;
            return -1L;
        }
        while ((l3 = this.buffer.indexOf(by, l2)) == -1L) {
            l2 = this.buffer.size;
            if (this.source.read(this.buffer, 2048L) != -1L) continue;
            return -1L;
        }
        return l3;
    }

    @Override
    public long indexOfElement(ByteString byteString) {
        return this.indexOfElement(byteString, 0L);
    }

    @Override
    public long indexOfElement(ByteString byteString, long l2) {
        long l3;
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (l2 >= this.buffer.size) {
            if (this.source.read(this.buffer, 2048L) != -1L) continue;
            return -1L;
        }
        while ((l3 = this.buffer.indexOfElement(byteString, l2)) == -1L) {
            l2 = this.buffer.size;
            if (this.source.read(this.buffer, 2048L) != -1L) continue;
            return -1L;
        }
        return l3;
    }

    @Override
    public InputStream inputStream() {
        return new InputStream(){

            public int available() {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                return (int)Math.min((long)RealBufferedSource.this.buffer.size, (long)Integer.MAX_VALUE);
            }

            public void close() {
                RealBufferedSource.this.close();
            }

            public int read() {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                if (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048L) == -1L) {
                    return -1;
                }
                return 255 & RealBufferedSource.this.buffer.readByte();
            }

            public int read(byte[] arrby, int n2, int n3) {
                if (RealBufferedSource.this.closed) {
                    throw new IOException("closed");
                }
                Util.checkOffsetAndCount(arrby.length, n2, n3);
                if (RealBufferedSource.this.buffer.size == 0L && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048L) == -1L) {
                    return -1;
                }
                return RealBufferedSource.this.buffer.read(arrby, n2, n3);
            }

            public String toString() {
                return RealBufferedSource.this + ".inputStream()";
            }
        };
    }

    @Override
    public int read(byte[] arrby) {
        return this.read(arrby, 0, arrby.length);
    }

    @Override
    public int read(byte[] arrby, int n2, int n3) {
        Util.checkOffsetAndCount(arrby.length, n2, n3);
        if (this.buffer.size == 0L && this.source.read(this.buffer, 2048L) == -1L) {
            return -1;
        }
        int n4 = (int)Math.min((long)n3, (long)this.buffer.size);
        return this.buffer.read(arrby, n2, n4);
    }

    @Override
    public long read(Buffer buffer, long l2) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        }
        if (l2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + l2);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size == 0L && this.source.read(this.buffer, 2048L) == -1L) {
            return -1L;
        }
        long l3 = Math.min((long)l2, (long)this.buffer.size);
        return this.buffer.read(buffer, l3);
    }

    @Override
    public long readAll(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        long l2 = 0L;
        while (this.source.read(this.buffer, 2048L) != -1L) {
            long l3 = this.buffer.completeSegmentByteCount();
            if (l3 <= 0L) continue;
            l2 += l3;
            sink.write(this.buffer, l3);
        }
        if (this.buffer.size() > 0L) {
            l2 += this.buffer.size();
            sink.write(this.buffer, this.buffer.size());
        }
        return l2;
    }

    @Override
    public byte readByte() {
        this.require(1L);
        return this.buffer.readByte();
    }

    @Override
    public byte[] readByteArray() {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    @Override
    public byte[] readByteArray(long l2) {
        this.require(l2);
        return this.buffer.readByteArray(l2);
    }

    @Override
    public ByteString readByteString() {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }

    @Override
    public ByteString readByteString(long l2) {
        this.require(l2);
        return this.buffer.readByteString(l2);
    }

    @Override
    public long readDecimalLong() {
        this.require(1L);
        int n2 = 0;
        while (this.request(n2 + 1)) {
            byte by = this.buffer.getByte(n2);
            if (!(by >= 48 && by <= 57 || n2 == 0 && by == 45)) {
                if (n2 != 0) break;
                Object[] arrobject = new Object[]{by};
                throw new NumberFormatException(String.format((String)"Expected leading [0-9] or '-' character but was %#x", (Object[])arrobject));
            }
            ++n2;
        }
        return this.buffer.readDecimalLong();
    }

    @Override
    public void readFully(Buffer buffer, long l2) {
        try {
            this.require(l2);
        }
        catch (EOFException eOFException) {
            buffer.writeAll(this.buffer);
            throw eOFException;
        }
        this.buffer.readFully(buffer, l2);
    }

    @Override
    public void readFully(byte[] arrby) {
        try {
            this.require(arrby.length);
        }
        catch (EOFException eOFException) {
            int n2 = 0;
            while (this.buffer.size > 0L) {
                int n3 = this.buffer.read(arrby, n2, (int)this.buffer.size);
                if (n3 == -1) {
                    throw new AssertionError();
                }
                n2 += n3;
            }
            throw eOFException;
        }
        this.buffer.readFully(arrby);
    }

    @Override
    public long readHexadecimalUnsignedLong() {
        this.require(1L);
        int n2 = 0;
        while (this.request(n2 + 1)) {
            byte by = this.buffer.getByte(n2);
            if (!(by >= 48 && by <= 57 || by >= 97 && by <= 102 || by >= 65 && by <= 70)) {
                if (n2 != 0) break;
                Object[] arrobject = new Object[]{by};
                throw new NumberFormatException(String.format((String)"Expected leading [0-9a-fA-F] character but was %#x", (Object[])arrobject));
            }
            ++n2;
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }

    @Override
    public int readInt() {
        this.require(4L);
        return this.buffer.readInt();
    }

    @Override
    public int readIntLe() {
        this.require(4L);
        return this.buffer.readIntLe();
    }

    @Override
    public long readLong() {
        this.require(8L);
        return this.buffer.readLong();
    }

    @Override
    public long readLongLe() {
        this.require(8L);
        return this.buffer.readLongLe();
    }

    @Override
    public short readShort() {
        this.require(2L);
        return this.buffer.readShort();
    }

    @Override
    public short readShortLe() {
        this.require(2L);
        return this.buffer.readShortLe();
    }

    @Override
    public String readString(long l2, Charset charset) {
        this.require(l2);
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        return this.buffer.readString(l2, charset);
    }

    @Override
    public String readString(Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        this.buffer.writeAll(this.source);
        return this.buffer.readString(charset);
    }

    @Override
    public String readUtf8() {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }

    @Override
    public String readUtf8(long l2) {
        this.require(l2);
        return this.buffer.readUtf8(l2);
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public int readUtf8CodePoint() {
        this.require(1L);
        byte by = this.buffer.getByte(0L);
        if ((by & 224) == 192) {
            this.require(2L);
            return this.buffer.readUtf8CodePoint();
        }
        if ((by & 240) == 224) {
            this.require(3L);
            return this.buffer.readUtf8CodePoint();
        }
        if ((by & 248) != 240) return this.buffer.readUtf8CodePoint();
        this.require(4L);
        return this.buffer.readUtf8CodePoint();
    }

    @Override
    public String readUtf8Line() {
        long l2 = this.indexOf((byte)10);
        if (l2 == -1L) {
            if (this.buffer.size != 0L) {
                return this.readUtf8(this.buffer.size);
            }
            return null;
        }
        return this.buffer.readUtf8Line(l2);
    }

    @Override
    public String readUtf8LineStrict() {
        long l2 = this.indexOf((byte)10);
        if (l2 == -1L) {
            Buffer buffer = new Buffer();
            this.buffer.copyTo(buffer, 0L, Math.min((long)32L, (long)this.buffer.size()));
            throw new EOFException("\\n not found: size=" + this.buffer.size() + " content=" + buffer.readByteString().hex() + "...");
        }
        return this.buffer.readUtf8Line(l2);
    }

    @Override
    public boolean request(long l2) {
        if (l2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + l2);
        }
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (this.buffer.size < l2) {
            if (this.source.read(this.buffer, 2048L) != -1L) continue;
            return false;
        }
        return true;
    }

    @Override
    public void require(long l2) {
        if (!this.request(l2)) {
            throw new EOFException();
        }
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    @Override
    public void skip(long var1_1) {
        if (!this.closed) ** GOTO lbl6
        throw new IllegalStateException("closed");
lbl-1000: // 1 sources:
        {
            var3_2 = Math.min((long)var1_1, (long)this.buffer.size());
            this.buffer.skip(var3_2);
            var1_1 -= var3_2;
lbl6: // 2 sources:
            if (var1_1 <= 0L) return;
            ** while (this.buffer.size != 0L || this.source.read((Buffer)this.buffer, (long)2048L) != -1L)
        }
lbl8: // 1 sources:
        throw new EOFException();
    }

    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        return "buffer(" + this.source + ")";
    }

}

