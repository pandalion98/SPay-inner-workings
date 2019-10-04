/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.util.zip.CRC32
 *  java.util.zip.Deflater
 */
package okio;

import java.util.zip.CRC32;
import java.util.zip.Deflater;
import okio.Buffer;
import okio.BufferedSink;
import okio.DeflaterSink;
import okio.Okio;
import okio.Segment;
import okio.Sink;
import okio.Timeout;
import okio.Util;

public final class GzipSink
implements Sink {
    private boolean closed;
    private final CRC32 crc = new CRC32();
    private final Deflater deflater;
    private final DeflaterSink deflaterSink;
    private final BufferedSink sink;

    public GzipSink(Sink sink) {
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        this.deflater = new Deflater(-1, true);
        this.sink = Okio.buffer(sink);
        this.deflaterSink = new DeflaterSink(this.sink, this.deflater);
        this.writeHeader();
    }

    private void updateCrc(Buffer buffer, long l2) {
        Segment segment = buffer.head;
        while (l2 > 0L) {
            int n2 = (int)Math.min((long)l2, (long)(segment.limit - segment.pos));
            this.crc.update(segment.data, segment.pos, n2);
            l2 -= (long)n2;
            segment = segment.next;
        }
    }

    private void writeFooter() {
        this.sink.writeIntLe((int)this.crc.getValue());
        this.sink.writeIntLe(this.deflater.getTotalIn());
    }

    private void writeHeader() {
        Buffer buffer = this.sink.buffer();
        buffer.writeShort(8075);
        buffer.writeByte(8);
        buffer.writeByte(0);
        buffer.writeInt(0);
        buffer.writeByte(0);
        buffer.writeByte(0);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    @Override
    public void close() {
        Throwable throwable;
        block11 : {
            block10 : {
                block9 : {
                    block8 : {
                        if (this.closed) break block10;
                        Throwable throwable2 = null;
                        try {
                            this.deflaterSink.finishDeflate();
                            this.writeFooter();
                        }
                        catch (Throwable throwable3) {
                            throwable2 = throwable3;
                        }
                        try {
                            this.deflater.end();
                            throwable = throwable2;
                        }
                        catch (Throwable throwable4) {
                            if (throwable2 == null) break block8;
                            throwable = throwable2;
                        }
                    }
                    try {
                        this.sink.close();
                    }
                    catch (Throwable throwable5) {
                        if (throwable != null) break block9;
                        throwable = throwable5;
                    }
                }
                this.closed = true;
                if (throwable != null) break block11;
            }
            return;
        }
        Util.sneakyRethrow(throwable);
    }

    @Override
    public void flush() {
        this.deflaterSink.flush();
    }

    @Override
    public Timeout timeout() {
        return this.sink.timeout();
    }

    @Override
    public void write(Buffer buffer, long l2) {
        if (l2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + l2);
        }
        if (l2 == 0L) {
            return;
        }
        this.updateCrc(buffer, l2);
        this.deflaterSink.write(buffer, l2);
    }
}

