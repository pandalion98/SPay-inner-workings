/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.util.zip.CRC32
 *  java.util.zip.Inflater
 */
package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;
import okio.Buffer;
import okio.BufferedSource;
import okio.InflaterSource;
import okio.Okio;
import okio.Segment;
import okio.Source;
import okio.Timeout;

public final class GzipSource
implements Source {
    private static final byte FCOMMENT = 4;
    private static final byte FEXTRA = 2;
    private static final byte FHCRC = 1;
    private static final byte FNAME = 3;
    private static final byte SECTION_BODY = 1;
    private static final byte SECTION_DONE = 3;
    private static final byte SECTION_HEADER = 0;
    private static final byte SECTION_TRAILER = 2;
    private final CRC32 crc = new CRC32();
    private final Inflater inflater;
    private final InflaterSource inflaterSource;
    private int section = 0;
    private final BufferedSource source;

    public GzipSource(Source source) {
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.inflater = new Inflater(true);
        this.source = Okio.buffer(source);
        this.inflaterSource = new InflaterSource(this.source, this.inflater);
    }

    private void checkEqual(String string, int n2, int n3) {
        if (n3 != n2) {
            Object[] arrobject = new Object[]{string, n3, n2};
            throw new IOException(String.format((String)"%s: actual 0x%08x != expected 0x%08x", (Object[])arrobject));
        }
    }

    /*
     * Enabled aggressive block sorting
     */
    private void consumeHeader() {
        this.source.require(10L);
        byte by = this.source.buffer().getByte(3L);
        boolean bl = (1 & by >> 1) == 1;
        if (bl) {
            this.updateCrc(this.source.buffer(), 0L, 10L);
        }
        this.checkEqual("ID1ID2", 8075, this.source.readShort());
        this.source.skip(8L);
        if ((1 & by >> 2) == 1) {
            this.source.require(2L);
            if (bl) {
                this.updateCrc(this.source.buffer(), 0L, 2L);
            }
            short s2 = this.source.buffer().readShortLe();
            this.source.require(s2);
            if (bl) {
                this.updateCrc(this.source.buffer(), 0L, s2);
            }
            this.source.skip(s2);
        }
        if ((1 & by >> 3) == 1) {
            long l2 = this.source.indexOf((byte)0);
            if (l2 == -1L) {
                throw new EOFException();
            }
            if (bl) {
                this.updateCrc(this.source.buffer(), 0L, 1L + l2);
            }
            this.source.skip(1L + l2);
        }
        if ((1 & by >> 4) == 1) {
            long l3 = this.source.indexOf((byte)0);
            if (l3 == -1L) {
                throw new EOFException();
            }
            if (bl) {
                this.updateCrc(this.source.buffer(), 0L, 1L + l3);
            }
            this.source.skip(1L + l3);
        }
        if (bl) {
            this.checkEqual("FHCRC", this.source.readShortLe(), (short)this.crc.getValue());
            this.crc.reset();
        }
    }

    private void consumeTrailer() {
        this.checkEqual("CRC", this.source.readIntLe(), (int)this.crc.getValue());
        this.checkEqual("ISIZE", this.source.readIntLe(), this.inflater.getTotalOut());
    }

    private void updateCrc(Buffer buffer, long l2, long l3) {
        Segment segment = buffer.head;
        while (l2 >= (long)(segment.limit - segment.pos)) {
            l2 -= (long)(segment.limit - segment.pos);
            segment = segment.next;
        }
        while (l3 > 0L) {
            int n2 = (int)(l2 + (long)segment.pos);
            int n3 = (int)Math.min((long)(segment.limit - n2), (long)l3);
            this.crc.update(segment.data, n2, n3);
            l3 -= (long)n3;
            segment = segment.next;
            l2 = 0L;
        }
    }

    @Override
    public void close() {
        this.inflaterSource.close();
    }

    @Override
    public long read(Buffer buffer, long l2) {
        if (l2 < 0L) {
            throw new IllegalArgumentException("byteCount < 0: " + l2);
        }
        if (l2 == 0L) {
            return 0L;
        }
        if (this.section == 0) {
            this.consumeHeader();
            this.section = 1;
        }
        if (this.section == 1) {
            long l3 = buffer.size;
            long l4 = this.inflaterSource.read(buffer, l2);
            if (l4 != -1L) {
                this.updateCrc(buffer, l3, l4);
                return l4;
            }
            this.section = 2;
        }
        if (this.section == 2) {
            this.consumeTrailer();
            this.section = 3;
            if (!this.source.exhausted()) {
                throw new IOException("gzip finished without exhausting source");
            }
        }
        return -1L;
    }

    @Override
    public Timeout timeout() {
        return this.source.timeout();
    }
}

