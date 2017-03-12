package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Util;
import java.net.ProtocolException;
import okio.Buffer;
import okio.Sink;
import okio.Timeout;

public final class RetryableSink implements Sink {
    private boolean closed;
    private final Buffer content;
    private final int limit;

    public RetryableSink(int i) {
        this.content = new Buffer();
        this.limit = i;
    }

    public RetryableSink() {
        this(-1);
    }

    public void close() {
        if (!this.closed) {
            this.closed = true;
            if (this.content.size() < ((long) this.limit)) {
                throw new ProtocolException("content-length promised " + this.limit + " bytes, but received " + this.content.size());
            }
        }
    }

    public void write(Buffer buffer, long j) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        Util.checkOffsetAndCount(buffer.size(), 0, j);
        if (this.limit == -1 || this.content.size() <= ((long) this.limit) - j) {
            this.content.write(buffer, j);
            return;
        }
        throw new ProtocolException("exceeded content-length limit of " + this.limit + " bytes");
    }

    public void flush() {
    }

    public Timeout timeout() {
        return Timeout.NONE;
    }

    public long contentLength() {
        return this.content.size();
    }

    public void writeToSocket(Sink sink) {
        Buffer buffer = new Buffer();
        this.content.copyTo(buffer, 0, this.content.size());
        sink.write(buffer, buffer.size());
    }
}
