package okio;

import java.io.EOFException;
import java.io.IOException;
import java.util.zip.CRC32;
import java.util.zip.Inflater;

public final class GzipSource implements Source {
    private static final byte FCOMMENT = (byte) 4;
    private static final byte FEXTRA = (byte) 2;
    private static final byte FHCRC = (byte) 1;
    private static final byte FNAME = (byte) 3;
    private static final byte SECTION_BODY = (byte) 1;
    private static final byte SECTION_DONE = (byte) 3;
    private static final byte SECTION_HEADER = (byte) 0;
    private static final byte SECTION_TRAILER = (byte) 2;
    private final CRC32 crc;
    private final Inflater inflater;
    private final InflaterSource inflaterSource;
    private int section;
    private final BufferedSource source;

    public GzipSource(Source source) {
        this.section = 0;
        this.crc = new CRC32();
        if (source == null) {
            throw new IllegalArgumentException("source == null");
        }
        this.inflater = new Inflater(true);
        this.source = Okio.buffer(source);
        this.inflaterSource = new InflaterSource(this.source, this.inflater);
    }

    public long read(Buffer buffer, long j) {
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        } else if (j == 0) {
            return 0;
        } else {
            if (this.section == 0) {
                consumeHeader();
                this.section = 1;
            }
            if (this.section == 1) {
                long j2 = buffer.size;
                long read = this.inflaterSource.read(buffer, j);
                if (read != -1) {
                    updateCrc(buffer, j2, read);
                    return read;
                }
                this.section = 2;
            }
            if (this.section == 2) {
                consumeTrailer();
                this.section = 3;
                if (!this.source.exhausted()) {
                    throw new IOException("gzip finished without exhausting source");
                }
            }
            return -1;
        }
    }

    private void consumeHeader() {
        Object obj;
        long indexOf;
        this.source.require(10);
        byte b = this.source.buffer().getByte(3);
        if (((b >> 1) & 1) == 1) {
            obj = 1;
        } else {
            obj = null;
        }
        if (obj != null) {
            updateCrc(this.source.buffer(), 0, 10);
        }
        checkEqual("ID1ID2", 8075, this.source.readShort());
        this.source.skip(8);
        if (((b >> 2) & 1) == 1) {
            this.source.require(2);
            if (obj != null) {
                updateCrc(this.source.buffer(), 0, 2);
            }
            short readShortLe = this.source.buffer().readShortLe();
            this.source.require((long) readShortLe);
            if (obj != null) {
                updateCrc(this.source.buffer(), 0, (long) readShortLe);
            }
            this.source.skip((long) readShortLe);
        }
        if (((b >> 3) & 1) == 1) {
            indexOf = this.source.indexOf(SECTION_HEADER);
            if (indexOf == -1) {
                throw new EOFException();
            }
            if (obj != null) {
                updateCrc(this.source.buffer(), 0, 1 + indexOf);
            }
            this.source.skip(1 + indexOf);
        }
        if (((b >> 4) & 1) == 1) {
            indexOf = this.source.indexOf(SECTION_HEADER);
            if (indexOf == -1) {
                throw new EOFException();
            }
            if (obj != null) {
                updateCrc(this.source.buffer(), 0, 1 + indexOf);
            }
            this.source.skip(1 + indexOf);
        }
        if (obj != null) {
            checkEqual("FHCRC", this.source.readShortLe(), (short) ((int) this.crc.getValue()));
            this.crc.reset();
        }
    }

    private void consumeTrailer() {
        checkEqual("CRC", this.source.readIntLe(), (int) this.crc.getValue());
        checkEqual("ISIZE", this.source.readIntLe(), this.inflater.getTotalOut());
    }

    public Timeout timeout() {
        return this.source.timeout();
    }

    public void close() {
        this.inflaterSource.close();
    }

    private void updateCrc(Buffer buffer, long j, long j2) {
        Segment segment = buffer.head;
        while (j >= ((long) (segment.limit - segment.pos))) {
            j -= (long) (segment.limit - segment.pos);
            segment = segment.next;
        }
        while (j2 > 0) {
            int i = (int) (((long) segment.pos) + j);
            int min = (int) Math.min((long) (segment.limit - i), j2);
            this.crc.update(segment.data, i, min);
            j2 -= (long) min;
            segment = segment.next;
            j = 0;
        }
    }

    private void checkEqual(String str, int i, int i2) {
        if (i2 != i) {
            throw new IOException(String.format("%s: actual 0x%08x != expected 0x%08x", new Object[]{str, Integer.valueOf(i2), Integer.valueOf(i)}));
        }
    }
}
