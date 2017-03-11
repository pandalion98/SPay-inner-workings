package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

final class RealBufferedSink implements BufferedSink {
    public final Buffer buffer;
    private boolean closed;
    public final Sink sink;

    /* renamed from: okio.RealBufferedSink.1 */
    class C07021 extends OutputStream {
        C07021() {
        }

        public void write(int i) {
            if (RealBufferedSink.this.closed) {
                throw new IOException("closed");
            }
            RealBufferedSink.this.buffer.writeByte((byte) i);
            RealBufferedSink.this.emitCompleteSegments();
        }

        public void write(byte[] bArr, int i, int i2) {
            if (RealBufferedSink.this.closed) {
                throw new IOException("closed");
            }
            RealBufferedSink.this.buffer.write(bArr, i, i2);
            RealBufferedSink.this.emitCompleteSegments();
        }

        public void flush() {
            if (!RealBufferedSink.this.closed) {
                RealBufferedSink.this.flush();
            }
        }

        public void close() {
            RealBufferedSink.this.close();
        }

        public String toString() {
            return RealBufferedSink.this + ".outputStream()";
        }
    }

    public RealBufferedSink(Sink sink, Buffer buffer) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        this.buffer = buffer;
        this.sink = sink;
    }

    public RealBufferedSink(Sink sink) {
        this(sink, new Buffer());
    }

    public Buffer buffer() {
        return this.buffer;
    }

    public void write(Buffer buffer, long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(buffer, j);
        emitCompleteSegments();
    }

    public BufferedSink write(ByteString byteString) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(byteString);
        return emitCompleteSegments();
    }

    public BufferedSink writeUtf8(String str) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(str);
        return emitCompleteSegments();
    }

    public BufferedSink writeUtf8(String str, int i, int i2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(str, i, i2);
        return emitCompleteSegments();
    }

    public BufferedSink writeUtf8CodePoint(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8CodePoint(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeString(String str, Charset charset) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(str, charset);
        return emitCompleteSegments();
    }

    public BufferedSink writeString(String str, int i, int i2, Charset charset) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(str, i, i2, charset);
        return emitCompleteSegments();
    }

    public BufferedSink write(byte[] bArr) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(bArr);
        return emitCompleteSegments();
    }

    public BufferedSink write(byte[] bArr, int i, int i2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(bArr, i, i2);
        return emitCompleteSegments();
    }

    public long writeAll(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long j = 0;
        while (true) {
            long read = source.read(this.buffer, 2048);
            if (read == -1) {
                return j;
            }
            j += read;
            emitCompleteSegments();
        }
    }

    public BufferedSink write(Source source, long j) {
        while (j > 0) {
            long read = source.read(this.buffer, j);
            if (read == -1) {
                throw new EOFException();
            }
            j -= read;
            emitCompleteSegments();
        }
        return this;
    }

    public BufferedSink writeByte(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeByte(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeShort(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShort(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeShortLe(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShortLe(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeInt(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeInt(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeIntLe(int i) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeIntLe(i);
        return emitCompleteSegments();
    }

    public BufferedSink writeLong(long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLong(j);
        return emitCompleteSegments();
    }

    public BufferedSink writeLongLe(long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLongLe(j);
        return emitCompleteSegments();
    }

    public BufferedSink writeDecimalLong(long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeDecimalLong(j);
        return emitCompleteSegments();
    }

    public BufferedSink writeHexadecimalUnsignedLong(long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeHexadecimalUnsignedLong(j);
        return emitCompleteSegments();
    }

    public BufferedSink emitCompleteSegments() {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        long completeSegmentByteCount = this.buffer.completeSegmentByteCount();
        if (completeSegmentByteCount > 0) {
            this.sink.write(this.buffer, completeSegmentByteCount);
        }
        return this;
    }

    public BufferedSink emit() {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        long size = this.buffer.size();
        if (size > 0) {
            this.sink.write(this.buffer, size);
        }
        return this;
    }

    public OutputStream outputStream() {
        return new C07021();
    }

    public void flush() {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size > 0) {
            this.sink.write(this.buffer, this.buffer.size);
        }
        this.sink.flush();
    }

    public void close() {
        if (!this.closed) {
            Throwable th = null;
            try {
                if (this.buffer.size > 0) {
                    this.sink.write(this.buffer, this.buffer.size);
                }
            } catch (Throwable th2) {
                th = th2;
            }
            try {
                this.sink.close();
            } catch (Throwable th3) {
                if (th == null) {
                    th = th3;
                }
            }
            this.closed = true;
            if (th != null) {
                Util.sneakyRethrow(th);
            }
        }
    }

    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        return "buffer(" + this.sink + ")";
    }
}
