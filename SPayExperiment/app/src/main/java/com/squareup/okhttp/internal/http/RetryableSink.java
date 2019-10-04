/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.ProtocolException
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.internal.Util;
import java.net.ProtocolException;
import okio.Buffer;
import okio.Sink;
import okio.Timeout;

public final class RetryableSink
implements Sink {
    private boolean closed;
    private final Buffer content = new Buffer();
    private final int limit;

    public RetryableSink() {
        this(-1);
    }

    public RetryableSink(int n2) {
        this.limit = n2;
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public void close() {
        block3 : {
            block2 : {
                if (this.closed) break block2;
                this.closed = true;
                if (this.content.size() < (long)this.limit) break block3;
            }
            return;
        }
        throw new ProtocolException("content-length promised " + this.limit + " bytes, but received " + this.content.size());
    }

    public long contentLength() {
        return this.content.size();
    }

    @Override
    public void flush() {
    }

    @Override
    public Timeout timeout() {
        return Timeout.NONE;
    }

    @Override
    public void write(Buffer buffer, long l2) {
        if (this.closed) {
            throw new IllegalStateException("closed");
        }
        Util.checkOffsetAndCount(buffer.size(), 0L, l2);
        if (this.limit != -1 && this.content.size() > (long)this.limit - l2) {
            throw new ProtocolException("exceeded content-length limit of " + this.limit + " bytes");
        }
        this.content.write(buffer, l2);
    }

    public void writeToSocket(Sink sink) {
        Buffer buffer = new Buffer();
        this.content.copyTo(buffer, 0L, this.content.size());
        sink.write(buffer, buffer.size());
    }
}

