package okio;

import java.util.zip.CRC32;
import java.util.zip.Deflater;

public final class GzipSink implements Sink {
    private boolean closed;
    private final CRC32 crc;
    private final Deflater deflater;
    private final DeflaterSink deflaterSink;
    private final BufferedSink sink;

    public GzipSink(Sink sink) {
        this.crc = new CRC32();
        if (sink == null) {
            throw new IllegalArgumentException("sink == null");
        }
        this.deflater = new Deflater(-1, true);
        this.sink = Okio.buffer(sink);
        this.deflaterSink = new DeflaterSink(this.sink, this.deflater);
        writeHeader();
    }

    public void write(Buffer buffer, long j) {
        if (j < 0) {
            throw new IllegalArgumentException("byteCount < 0: " + j);
        } else if (j != 0) {
            updateCrc(buffer, j);
            this.deflaterSink.write(buffer, j);
        }
    }

    public void flush() {
        this.deflaterSink.flush();
    }

    public Timeout timeout() {
        return this.sink.timeout();
    }

    public void close() {
        Throwable th;
        if (!this.closed) {
            Throwable th2 = null;
            try {
                this.deflaterSink.finishDeflate();
                writeFooter();
            } catch (Throwable th3) {
                th2 = th3;
            }
            try {
                this.deflater.end();
                th3 = th2;
            } catch (Throwable th4) {
                th3 = th4;
                if (th2 != null) {
                    th3 = th2;
                }
            }
            try {
                this.sink.close();
            } catch (Throwable th22) {
                if (th3 == null) {
                    th3 = th22;
                }
            }
            this.closed = true;
            if (th3 != null) {
                Util.sneakyRethrow(th3);
            }
        }
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

    private void writeFooter() {
        this.sink.writeIntLe((int) this.crc.getValue());
        this.sink.writeIntLe(this.deflater.getTotalIn());
    }

    private void updateCrc(Buffer buffer, long j) {
        Segment segment = buffer.head;
        while (j > 0) {
            int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
            this.crc.update(segment.data, segment.pos, min);
            j -= (long) min;
            segment = segment.next;
        }
    }
}
