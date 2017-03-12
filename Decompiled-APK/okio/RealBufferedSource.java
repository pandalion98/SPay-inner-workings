package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import org.bouncycastle.crypto.tls.CipherSuite;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;

final class RealBufferedSource implements BufferedSource {
    public final Buffer buffer;
    private boolean closed;
    public final Source source;

    /* renamed from: okio.RealBufferedSource.1 */
    class C07031 extends InputStream {
        C07031() {
        }

        public int read() {
            if (RealBufferedSource.this.closed) {
                throw new IOException("closed");
            } else if (RealBufferedSource.this.buffer.size == 0 && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048) == -1) {
                return -1;
            } else {
                return RealBufferedSource.this.buffer.readByte() & GF2Field.MASK;
            }
        }

        public int read(byte[] bArr, int i, int i2) {
            if (RealBufferedSource.this.closed) {
                throw new IOException("closed");
            }
            Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
            if (RealBufferedSource.this.buffer.size == 0 && RealBufferedSource.this.source.read(RealBufferedSource.this.buffer, 2048) == -1) {
                return -1;
            }
            return RealBufferedSource.this.buffer.read(bArr, i, i2);
        }

        public int available() {
            if (!RealBufferedSource.this.closed) {
                return (int) Math.min(RealBufferedSource.this.buffer.size, 2147483647L);
            }
            throw new IOException("closed");
        }

        public void close() {
            RealBufferedSource.this.close();
        }

        public String toString() {
            return RealBufferedSource.this + ".inputStream()";
        }
    }

    public RealBufferedSource(Source source, Buffer buffer) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.buffer = buffer;
        this.source = source;
    }

    public RealBufferedSource(Source source) {
        this(source, new Buffer());
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public long read(Buffer buffer, long j) {
        if (buffer == null) {
            throw new IllegalArgumentException("sink == null");
        } else if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        } else if (this.closed) {
            throw new IllegalStateException("closed");
        } else if (this.buffer.size == 0 && this.source.read(this.buffer, 2048) == -1) {
            return -1;
        } else {
            return this.buffer.read(buffer, Math.min(j, this.buffer.size));
        }
    }

    public boolean exhausted() {
        if (!this.closed) {
            return this.buffer.exhausted() && this.source.read(this.buffer, 2048) == -1;
        } else {
            throw new IllegalStateException("closed");
        }
    }

    public void require(long j) {
        if (!request(j)) {
            throw new EOFException();
        }
    }

    public boolean request(long j) {
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        } else if (this.closed) {
            throw new IllegalStateException("closed");
        } else {
            while (this.buffer.size < j) {
                if (this.source.read(this.buffer, 2048) == -1) {
                    return false;
                }
            }
            return true;
        }
    }

    public byte readByte() {
        require(1);
        return this.buffer.readByte();
    }

    public ByteString readByteString() {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteString();
    }

    public ByteString readByteString(long j) {
        require(j);
        return this.buffer.readByteString(j);
    }

    public byte[] readByteArray() {
        this.buffer.writeAll(this.source);
        return this.buffer.readByteArray();
    }

    public byte[] readByteArray(long j) {
        require(j);
        return this.buffer.readByteArray(j);
    }

    public int read(byte[] bArr) {
        return read(bArr, 0, bArr.length);
    }

    public void readFully(byte[] bArr) {
        try {
            require((long) bArr.length);
            this.buffer.readFully(bArr);
        } catch (EOFException e) {
            EOFException eOFException = e;
            int i = 0;
            while (this.buffer.size > 0) {
                int read = this.buffer.read(bArr, i, (int) this.buffer.size);
                if (read == -1) {
                    throw new AssertionError();
                }
                i += read;
            }
            throw eOFException;
        }
    }

    public int read(byte[] bArr, int i, int i2) {
        Util.checkOffsetAndCount((long) bArr.length, (long) i, (long) i2);
        if (this.buffer.size == 0 && this.source.read(this.buffer, 2048) == -1) {
            return -1;
        }
        return this.buffer.read(bArr, i, (int) Math.min((long) i2, this.buffer.size));
    }

    public void readFully(Buffer buffer, long j) {
        try {
            require(j);
            this.buffer.readFully(buffer, j);
        } catch (EOFException e) {
            buffer.writeAll(this.buffer);
            throw e;
        }
    }

    public long readAll(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        long j = 0;
        while (this.source.read(this.buffer, 2048) != -1) {
            long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
            if (completeSegmentByteCount > 0) {
                j += completeSegmentByteCount;
                sink.write(this.buffer, completeSegmentByteCount);
            }
        }
        if (this.buffer.size() <= 0) {
            return j;
        }
        j += this.buffer.size();
        sink.write(this.buffer, this.buffer.size());
        return j;
    }

    public String readUtf8() {
        this.buffer.writeAll(this.source);
        return this.buffer.readUtf8();
    }

    public String readUtf8(long j) {
        require(j);
        return this.buffer.readUtf8(j);
    }

    public String readString(Charset charset) {
        if (charset == null) {
            throw new IllegalArgumentException("charset == null");
        }
        this.buffer.writeAll(this.source);
        return this.buffer.readString(charset);
    }

    public String readString(long j, Charset charset) {
        require(j);
        if (charset != null) {
            return this.buffer.readString(j, charset);
        }
        throw new IllegalArgumentException("charset == null");
    }

    public String readUtf8Line() {
        long indexOf = indexOf((byte) 10);
        if (indexOf == -1) {
            return this.buffer.size != 0 ? readUtf8(this.buffer.size) : null;
        } else {
            return this.buffer.readUtf8Line(indexOf);
        }
    }

    public String readUtf8LineStrict() {
        long indexOf = indexOf((byte) 10);
        if (indexOf != -1) {
            return this.buffer.readUtf8Line(indexOf);
        }
        Buffer buffer = new Buffer();
        this.buffer.copyTo(buffer, 0, Math.min(32, this.buffer.size()));
        throw new EOFException("\\n not found: size=" + this.buffer.size() + " content=" + buffer.readByteString().hex() + "...");
    }

    public int readUtf8CodePoint() {
        require(1);
        byte b = this.buffer.getByte(0);
        if ((b & 224) == CipherSuite.TLS_RSA_WITH_CAMELLIA_256_CBC_SHA256) {
            require(2);
        } else if ((b & 240) == 224) {
            require(3);
        } else if ((b & 248) == 240) {
            require(4);
        }
        return this.buffer.readUtf8CodePoint();
    }

    public short readShort() {
        require(2);
        return this.buffer.readShort();
    }

    public short readShortLe() {
        require(2);
        return this.buffer.readShortLe();
    }

    public int readInt() {
        require(4);
        return this.buffer.readInt();
    }

    public int readIntLe() {
        require(4);
        return this.buffer.readIntLe();
    }

    public long readLong() {
        require(8);
        return this.buffer.readLong();
    }

    public long readLongLe() {
        require(8);
        return this.buffer.readLongLe();
    }

    public long readDecimalLong() {
        require(1);
        int i = 0;
        while (request((long) (i + 1))) {
            byte b = this.buffer.getByte((long) i);
            if ((b < 48 || b > 57) && !(i == 0 && b == 45)) {
                if (i == 0) {
                    throw new NumberFormatException(String.format("Expected leading [0-9] or '-' character but was %#x", new Object[]{Byte.valueOf(b)}));
                }
                return this.buffer.readDecimalLong();
            }
            i++;
        }
        return this.buffer.readDecimalLong();
    }

    public long readHexadecimalUnsignedLong() {
        require(1);
        for (int i = 0; request((long) (i + 1)); i++) {
            byte b = this.buffer.getByte((long) i);
            if ((b < 48 || b > 57) && ((b < 97 || b > 102) && (b < 65 || b > 70))) {
                if (i == 0) {
                    throw new NumberFormatException(String.format("Expected leading [0-9a-fA-F] character but was %#x", new Object[]{Byte.valueOf(b)}));
                }
                return this.buffer.readHexadecimalUnsignedLong();
            }
        }
        return this.buffer.readHexadecimalUnsignedLong();
    }

    public void skip(long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (j > 0) {
            if (this.buffer.size == 0 && this.source.read(this.buffer, 2048) == -1) {
                throw new EOFException();
            }
            long min = Math.min(j, this.buffer.size());
            this.buffer.skip(min);
            j -= min;
        }
    }

    public long indexOf(byte b) {
        return indexOf(b, 0);
    }

    public long indexOf(byte b, long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (j >= this.buffer.size) {
            if (this.source.read(this.buffer, 2048) == -1) {
                return -1;
            }
        }
        do {
            long indexOf = this.buffer.indexOf(b, j);
            if (indexOf != -1) {
                return indexOf;
            }
            j = this.buffer.size;
        } while (this.source.read(this.buffer, 2048) != -1);
        return -1;
    }

    public long indexOfElement(ByteString byteString) {
        return indexOfElement(byteString, 0);
    }

    public long indexOfElement(ByteString byteString, long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        while (j >= this.buffer.size) {
            if (this.source.read(this.buffer, 2048) == -1) {
                return -1;
            }
        }
        do {
            long indexOfElement = this.buffer.indexOfElement(byteString, j);
            if (indexOfElement != -1) {
                return indexOfElement;
            }
            j = this.buffer.size;
        } while (this.source.read(this.buffer, 2048) != -1);
        return -1;
    }

    public InputStream inputStream() {
        return new C07031();
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            this.source.close();
            this.buffer.clear();
        }
    }

    public Timeout timeout() {
        return this.source.timeout();
    }

    public String toString() {
        return "buffer(" + this.source + ")";
    }
}
