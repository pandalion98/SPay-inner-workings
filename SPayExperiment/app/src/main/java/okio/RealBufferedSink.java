/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.IOException
 *  java.io.OutputStream
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.nio.charset.Charset
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import okio.Buffer;
import okio.BufferedSink;
import okio.ByteString;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import okio.Util;

final class RealBufferedSink
implements BufferedSink {
    public final Buffer buffer;
    private boolean closed;
    public final Sink sink;

    public RealBufferedSink(Sink sink) {
        this(sink, new Buffer());
    }

    public RealBufferedSink(Sink sink, Buffer buffer) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        this.buffer = buffer;
        this.sink = sink;
    }

    @Override
    public Buffer buffer() {
        return this.buffer;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void close() {
        Throwable throwable;
        block12 : {
            block11 : {
                block10 : {
                    if (this.closed) break block11;
                    try {
                        long l2 = this.buffer.size LCMP 0L;
                        throwable = null;
                        if (l2 > 0) {
                            this.sink.write(this.buffer, this.buffer.size);
                        }
                    }
                    catch (Throwable throwable2) {}
                    try {
                        this.sink.close();
                    }
                    catch (Throwable throwable3) {
                        if (throwable != null) break block10;
                        throwable = throwable3;
                    }
                }
                this.closed = true;
                if (throwable != null) break block12;
            }
            return;
        }
        Util.sneakyRethrow(throwable);
    }

    @Override
    public BufferedSink emit() {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        long l2 = this.buffer.size();
        if (l2 > 0L) {
            this.sink.write(this.buffer, l2);
        }
        return this;
    }

    @Override
    public BufferedSink emitCompleteSegments() {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        long l2 = this.buffer.completeSegmentByteCount();
        if (l2 > 0L) {
            this.sink.write(this.buffer, l2);
        }
        return this;
    }

    @Override
    public void flush() {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        if (this.buffer.size > 0L) {
            this.sink.write(this.buffer, this.buffer.size);
        }
        this.sink.flush();
    }

    @Override
    public OutputStream outputStream() {
        return new OutputStream(){

            public void close() {
                RealBufferedSink.this.close();
            }

            public void flush() {
                if (!RealBufferedSink.this.closed) {
                    RealBufferedSink.this.flush();
                }
            }

            public String toString() {
                return RealBufferedSink.this + ".outputStream()";
            }

            public void write(int n2) {
                if (RealBufferedSink.this.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSink.this.buffer.writeByte((byte)n2);
                RealBufferedSink.this.emitCompleteSegments();
            }

            public void write(byte[] arrby, int n2, int n3) {
                if (RealBufferedSink.this.closed) {
                    throw new IOException("closed");
                }
                RealBufferedSink.this.buffer.write(arrby, n2, n3);
                RealBufferedSink.this.emitCompleteSegments();
            }
        };
    }

    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        return "buffer(" + this.sink + ")";
    }

    @Override
    public BufferedSink write(ByteString byteString) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(byteString);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink write(Source source, long l2) {
        while (l2 > 0L) {
            long l3 = source.read(this.buffer, l2);
            if (l3 == -1L) {
                throw new EOFException();
            }
            l2 -= l3;
            this.emitCompleteSegments();
        }
        return this;
    }

    @Override
    public BufferedSink write(byte[] arrby) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(arrby);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink write(byte[] arrby, int n2, int n3) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(arrby, n2, n3);
        return this.emitCompleteSegments();
    }

    @Override
    public void write(Buffer buffer, long l2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.write(buffer, l2);
        this.emitCompleteSegments();
    }

    @Override
    public long writeAll(Source source) {
        long l2;
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        long l3 = 0L;
        while ((l2 = source.read(this.buffer, 2048L)) != -1L) {
            l3 += l2;
            this.emitCompleteSegments();
        }
        return l3;
    }

    @Override
    public BufferedSink writeByte(int n2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeByte(n2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeDecimalLong(long l2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeDecimalLong(l2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeHexadecimalUnsignedLong(long l2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeHexadecimalUnsignedLong(l2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeInt(int n2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeInt(n2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeIntLe(int n2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeIntLe(n2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeLong(long l2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLong(l2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeLongLe(long l2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeLongLe(l2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeShort(int n2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShort(n2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeShortLe(int n2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeShortLe(n2);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeString(String string, int n2, int n3, Charset charset) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(string, n2, n3, charset);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeString(String string, Charset charset) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeString(string, charset);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeUtf8(String string) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(string);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeUtf8(String string, int n2, int n3) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8(string, n2, n3);
        return this.emitCompleteSegments();
    }

    @Override
    public BufferedSink writeUtf8CodePoint(int n2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        this.buffer.writeUtf8CodePoint(n2);
        return this.emitCompleteSegments();
    }

}

