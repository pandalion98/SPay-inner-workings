/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.zip.DataFormatException
 *  java.util.zip.Inflater
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Inflater;
import okio.Buffer;
import okio.BufferedSource;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Source;
import okio.Timeout;

public final class InflaterSource
implements Source {
    private int bufferBytesHeldByInflater;
    private boolean closed;
    private final Inflater inflater;
    private final BufferedSource source;

    InflaterSource(BufferedSource bufferedSource, Inflater inflater) {
        if (bufferedSource == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (inflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.source = bufferedSource;
        this.inflater = inflater;
    }

    public InflaterSource(Source source, Inflater inflater) {
        this(Okio.buffer(source), inflater);
    }

    private void releaseInflatedBytes() {
        if (this.bufferBytesHeldByInflater == 0) {
            return;
        }
        int n2 = this.bufferBytesHeldByInflater - this.inflater.getRemaining();
        this.bufferBytesHeldByInflater -= n2;
        this.source.skip(n2);
    }

    @Override
    public void close() {
        if (this.closed) {
            return;
        }
        this.inflater.end();
        this.closed = true;
        this.source.close();
    }

    @Override
    public long read(Buffer buffer, long l2) {
        block11 : {
            if (l2 < 0L) {
                throw new IllegalArgumentException("byteCount < 0: " + l2);
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (l2 == 0L) {
                return 0L;
            }
            do {
                boolean bl;
                Segment segment;
                block10 : {
                    bl = this.refill();
                    segment = buffer.writableSegment(1);
                    int n2 = this.inflater.inflate(segment.data, segment.limit, 2048 - segment.limit);
                    if (n2 <= 0) break block10;
                    segment.limit = n2 + segment.limit;
                    buffer.size += (long)n2;
                    return n2;
                }
                if (!this.inflater.finished() && !this.inflater.needsDictionary()) continue;
                this.releaseInflatedBytes();
                if (segment.pos == segment.limit) {
                    buffer.head = segment.pop();
                    SegmentPool.recycle(segment);
                }
                break block11;
                if (!bl) continue;
                break;
            } while (true);
            try {
                throw new EOFException("source exhausted prematurely");
            }
            catch (DataFormatException dataFormatException) {
                throw new IOException((Throwable)dataFormatException);
            }
        }
        return -1L;
    }

    public boolean refill() {
        if (!this.inflater.needsInput()) {
            return false;
        }
        this.releaseInflatedBytes();
        if (this.inflater.getRemaining() != 0) {
            throw new IllegalStateException("?");
        }
        if (this.source.exhausted()) {
            return true;
        }
        Segment segment = this.source.buffer().head;
        this.bufferBytesHeldByInflater = segment.limit - segment.pos;
        this.inflater.setInput(segment.data, segment.pos, this.bufferBytesHeldByInflater);
        return false;
    }

    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}

