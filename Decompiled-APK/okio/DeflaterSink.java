package okio;

import java.util.zip.Deflater;
import org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement;

public final class DeflaterSink implements Sink {
    private boolean closed;
    private final Deflater deflater;
    private final BufferedSink sink;

    public DeflaterSink(Sink sink, Deflater deflater) {
        this(Okio.buffer(sink), deflater);
    }

    DeflaterSink(BufferedSink bufferedSink, Deflater deflater) {
        if (bufferedSink == null) {
            throw new IllegalArgumentException("source == null");
        } else if (deflater == null) {
            throw new IllegalArgumentException("inflater == null");
        } else {
            this.sink = bufferedSink;
            this.deflater = deflater;
        }
    }

    public void write(Buffer buffer, long j) {
        Util.checkOffsetAndCount(buffer.size, 0, j);
        while (j > 0) {
            Segment segment = buffer.head;
            int min = (int) Math.min(j, (long) (segment.limit - segment.pos));
            this.deflater.setInput(segment.data, segment.pos, min);
            deflate(false);
            buffer.size -= (long) min;
            segment.pos += min;
            if (segment.pos == segment.limit) {
                buffer.head = segment.pop();
                SegmentPool.recycle(segment);
            }
            j -= (long) min;
        }
    }

    @IgnoreJRERequirement
    private void deflate(boolean z) {
        Buffer buffer = this.sink.buffer();
        while (true) {
            Segment writableSegment = buffer.writableSegment(1);
            int deflate = z ? this.deflater.deflate(writableSegment.data, writableSegment.limit, 2048 - writableSegment.limit, 2) : this.deflater.deflate(writableSegment.data, writableSegment.limit, 2048 - writableSegment.limit);
            if (deflate > 0) {
                writableSegment.limit += deflate;
                buffer.size += (long) deflate;
                this.sink.emitCompleteSegments();
            } else if (this.deflater.needsInput()) {
                break;
            }
        }
        if (writableSegment.pos == writableSegment.limit) {
            buffer.head = writableSegment.pop();
            SegmentPool.recycle(writableSegment);
        }
    }

    public void flush() {
        deflate(true);
        this.sink.flush();
    }

    void finishDeflate() {
        this.deflater.finish();
        deflate(false);
    }

    public void close() {
        Throwable th;
        if (!this.closed) {
            Throwable th2 = null;
            try {
                finishDeflate();
            } catch (Throwable th3) {
                th2 = th3;
            }
            try {
                this.deflater.end();
                th = th2;
            } catch (Throwable th4) {
                th = th4;
                if (th2 != null) {
                    th = th2;
                }
            }
            try {
                this.sink.close();
            } catch (Throwable th22) {
                if (th == null) {
                    th = th22;
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
        return "DeflaterSink(" + this.sink + ")";
    }
}
