/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.zip.Deflater
 *  org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
 */
package okio;

import java.util.zip.Deflater;
import okio.Buffer;
import okio.BufferedSink;
import okio.Okio;
import okio.Segment;
import okio.SegmentPool;
import okio.Sink;
import okio.Timeout;
import okio.Util;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class DeflaterSink
implements Sink {
    private boolean closed;
    private final Deflater deflater;
    private final BufferedSink sink;

    DeflaterSink(BufferedSink bufferedSink, Deflater deflater) {
        if (bufferedSink == null) {
            throw new IllegalArgumentException("source == null");
        }
        if (deflater == null) {
            throw new IllegalArgumentException("inflater == null");
        }
        this.sink = bufferedSink;
        this.deflater = deflater;
    }

    public DeflaterSink(Sink sink, Deflater deflater) {
        this(Okio.buffer(sink), deflater);
    }

    /*
     * Enabled aggressive block sorting
     */
    @IgnoreJRERequirement
    private void deflate(boolean bl) {
        Segment segment;
        Buffer buffer = this.sink.buffer();
        do {
            segment = buffer.writableSegment(1);
            int n2 = bl ? this.deflater.deflate(segment.data, segment.limit, 2048 - segment.limit, 2) : this.deflater.deflate(segment.data, segment.limit, 2048 - segment.limit);
            if (n2 > 0) {
                segment.limit = n2 + segment.limit;
                buffer.size += (long)n2;
                this.sink.emitCompleteSegments();
                continue;
            }
            if (this.deflater.needsInput()) break;
        } while (true);
        if (segment.pos == segment.limit) {
            buffer.head = segment.pop();
            SegmentPool.recycle(segment);
        }
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
                    block9 : {
                        Throwable throwable2;
                        if (this.closed) break block11;
                        throwable2 = null;
                        try {
                            this.finishDeflate();
                        }
                        catch (Throwable throwable3) {}
                        try {
                            this.deflater.end();
                            throwable = throwable2;
                        }
                        catch (Throwable throwable4) {
                            if (throwable2 == null) break block9;
                            throwable = throwable2;
                        }
                    }
                    try {
                        this.sink.close();
                    }
                    catch (Throwable throwable5) {
                        if (throwable != null) break block10;
                        throwable = throwable5;
                    }
                }
                this.closed = true;
                if (throwable != null) break block12;
            }
            return;
        }
        Util.sneakyRethrow(throwable);
    }

    void finishDeflate() {
        this.deflater.finish();
        this.deflate(false);
    }

    @Override
    public void flush() {
        this.deflate(true);
        this.sink.flush();
    }

    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }

    public String toString() {
        return "DeflaterSink(" + this.sink + ")";
    }

    @Override
    public void write(Buffer buffer, long l2) {
        Util.checkOffsetAndCount(buffer.size, 0L, l2);
        while (l2 > 0L) {
            Segment segment = buffer.head;
            int n2 = (int)Math.min((long)l2, (long)(segment.limit - segment.pos));
            this.deflater.setInput(segment.data, segment.pos, n2);
            this.deflate(false);
            buffer.size -= (long)n2;
            segment.pos = n2 + segment.pos;
            if (segment.pos == segment.limit) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            l2 -= (long)n2;
        }
    }
}

