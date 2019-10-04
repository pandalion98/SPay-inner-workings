/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Math
 *  java.lang.NumberFormatException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Throwable
 *  java.net.ProtocolException
 *  java.net.Socket
 *  java.net.SocketTimeoutException
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.RetryableSink;
import com.squareup.okhttp.internal.http.StatusLine;
import java.io.EOFException;
import java.io.IOException;
import java.net.ProtocolException;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.TimeUnit;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingTimeout;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class HttpConnection {
    private static final int ON_IDLE_CLOSE = 2;
    private static final int ON_IDLE_HOLD = 0;
    private static final int ON_IDLE_POOL = 1;
    private static final int STATE_CLOSED = 6;
    private static final int STATE_IDLE = 0;
    private static final int STATE_OPEN_REQUEST_BODY = 1;
    private static final int STATE_OPEN_RESPONSE_BODY = 4;
    private static final int STATE_READING_RESPONSE_BODY = 5;
    private static final int STATE_READ_RESPONSE_HEADERS = 3;
    private static final int STATE_WRITING_REQUEST_BODY = 2;
    private final Connection connection;
    private int onIdle = 0;
    private final ConnectionPool pool;
    private final BufferedSink sink;
    private final Socket socket;
    private final BufferedSource source;
    private int state = 0;

    public HttpConnection(ConnectionPool connectionPool, Connection connection, Socket socket) {
        this.pool = connectionPool;
        this.connection = connection;
        this.socket = socket;
        this.source = Okio.buffer(Okio.source(socket));
        this.sink = Okio.buffer(Okio.sink(socket));
    }

    private void detachTimeout(ForwardingTimeout forwardingTimeout) {
        Timeout timeout = forwardingTimeout.delegate();
        forwardingTimeout.setDelegate(Timeout.NONE);
        timeout.clearDeadline();
        timeout.clearTimeout();
    }

    public long bufferSize() {
        return this.source.buffer().size();
    }

    public void closeIfOwnedBy(Object object) {
        Internal.instance.closeIfOwnedBy(this.connection, object);
    }

    public void closeOnIdle() {
        this.onIdle = 2;
        if (this.state == 0) {
            this.state = 6;
            this.connection.getSocket().close();
        }
    }

    public void flush() {
        this.sink.flush();
    }

    public boolean isClosed() {
        return this.state == 6;
    }

    public boolean isReadable() {
        int n2;
        block8 : {
            n2 = this.socket.getSoTimeout();
            try {
                this.socket.setSoTimeout(1);
                boolean bl = this.source.exhausted();
                if (!bl) break block8;
            }
            catch (Throwable throwable) {
                try {
                    this.socket.setSoTimeout(n2);
                    throw throwable;
                }
                catch (SocketTimeoutException socketTimeoutException) {
                    return true;
                }
                catch (IOException iOException) {
                    return false;
                }
            }
            this.socket.setSoTimeout(n2);
            return false;
        }
        this.socket.setSoTimeout(n2);
        return true;
    }

    public Sink newChunkedSink() {
        if (this.state != 1) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 2;
        return new ChunkedSink();
    }

    public Source newChunkedSource(HttpEngine httpEngine) {
        if (this.state != 4) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 5;
        return new ChunkedSource(httpEngine);
    }

    public Sink newFixedLengthSink(long l2) {
        if (this.state != 1) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 2;
        return new FixedLengthSink(l2);
    }

    public Source newFixedLengthSource(long l2) {
        if (this.state != 4) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 5;
        return new FixedLengthSource(l2);
    }

    public Source newUnknownLengthSource() {
        if (this.state != 4) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 5;
        return new UnknownLengthSource();
    }

    public void poolOnIdle() {
        this.onIdle = 1;
        if (this.state == 0) {
            this.onIdle = 0;
            Internal.instance.recycle(this.pool, this.connection);
        }
    }

    public BufferedSink rawSink() {
        return this.sink;
    }

    public BufferedSource rawSource() {
        return this.source;
    }

    public void readHeaders(Headers.Builder builder) {
        String string;
        while ((string = this.source.readUtf8LineStrict()).length() != 0) {
            Internal.instance.addLenient(builder, string);
        }
    }

    public Response.Builder readResponse() {
        if (this.state != 1 && this.state != 3) {
            throw new IllegalStateException("state: " + this.state);
        }
        try {
            Response.Builder builder;
            StatusLine statusLine;
            do {
                statusLine = StatusLine.parse(this.source.readUtf8LineStrict());
                builder = new Response.Builder().protocol(statusLine.protocol).code(statusLine.code).message(statusLine.message);
                Headers.Builder builder2 = new Headers.Builder();
                this.readHeaders(builder2);
                builder2.add(OkHeaders.SELECTED_PROTOCOL, statusLine.protocol.toString());
                builder.headers(builder2.build());
            } while (statusLine.code == 100);
            this.state = 4;
            return builder;
        }
        catch (EOFException eOFException) {
            IOException iOException = new IOException("unexpected end of stream on " + this.connection + " (recycle count=" + Internal.instance.recycleCount(this.connection) + ")");
            iOException.initCause((Throwable)eOFException);
            throw iOException;
        }
    }

    public void setTimeouts(int n2, int n3) {
        if (n2 != 0) {
            this.source.timeout().timeout(n2, TimeUnit.MILLISECONDS);
        }
        if (n3 != 0) {
            this.sink.timeout().timeout(n3, TimeUnit.MILLISECONDS);
        }
    }

    public void writeRequest(Headers headers, String string) {
        if (this.state != 0) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.sink.writeUtf8(string).writeUtf8("\r\n");
        int n2 = headers.size();
        for (int i2 = 0; i2 < n2; ++i2) {
            this.sink.writeUtf8(headers.name(i2)).writeUtf8(": ").writeUtf8(headers.value(i2)).writeUtf8("\r\n");
        }
        this.sink.writeUtf8("\r\n");
        this.state = 1;
    }

    public void writeRequestBody(RetryableSink retryableSink) {
        if (this.state != 1) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = 3;
        retryableSink.writeToSocket(this.sink);
    }

    private abstract class AbstractSource
    implements Source {
        protected boolean closed;
        protected final ForwardingTimeout timeout;

        private AbstractSource() {
            this.timeout = new ForwardingTimeout(HttpConnection.this.source.timeout());
        }

        /*
         * Enabled aggressive block sorting
         */
        protected final void endOfInput(boolean bl) {
            if (HttpConnection.this.state != 5) {
                throw new IllegalStateException("state: " + HttpConnection.this.state);
            }
            HttpConnection.this.detachTimeout(this.timeout);
            HttpConnection.this.state = 0;
            if (bl && HttpConnection.this.onIdle == 1) {
                HttpConnection.this.onIdle = 0;
                Internal.instance.recycle(HttpConnection.this.pool, HttpConnection.this.connection);
                return;
            } else {
                if (HttpConnection.this.onIdle != 2) return;
                {
                    HttpConnection.this.state = 6;
                    HttpConnection.this.connection.getSocket().close();
                    return;
                }
            }
        }

        @Override
        public Timeout timeout() {
            return this.timeout;
        }

        protected final void unexpectedEndOfInput() {
            Util.closeQuietly(HttpConnection.this.connection.getSocket());
            HttpConnection.this.state = 6;
        }
    }

    private final class ChunkedSink
    implements Sink {
        private boolean closed;
        private final ForwardingTimeout timeout;

        private ChunkedSink() {
            this.timeout = new ForwardingTimeout(HttpConnection.this.sink.timeout());
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void close() {
            ChunkedSink chunkedSink = this;
            synchronized (chunkedSink) {
                block6 : {
                    boolean bl = this.closed;
                    if (!bl) break block6;
                    do {
                        return;
                        break;
                    } while (true);
                }
                this.closed = true;
                HttpConnection.this.sink.writeUtf8("0\r\n\r\n");
                HttpConnection.this.detachTimeout(this.timeout);
                HttpConnection.this.state = 3;
                return;
            }
        }

        /*
         * Enabled force condition propagation
         * Lifted jumps to return sites
         */
        @Override
        public void flush() {
            ChunkedSink chunkedSink = this;
            synchronized (chunkedSink) {
                block6 : {
                    boolean bl = this.closed;
                    if (!bl) break block6;
                    do {
                        return;
                        break;
                    } while (true);
                }
                HttpConnection.this.sink.flush();
                return;
            }
        }

        @Override
        public Timeout timeout() {
            return this.timeout;
        }

        @Override
        public void write(Buffer buffer, long l2) {
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (l2 == 0L) {
                return;
            }
            HttpConnection.this.sink.writeHexadecimalUnsignedLong(l2);
            HttpConnection.this.sink.writeUtf8("\r\n");
            HttpConnection.this.sink.write(buffer, l2);
            HttpConnection.this.sink.writeUtf8("\r\n");
        }
    }

    private class ChunkedSource
    extends AbstractSource {
        private static final long NO_CHUNK_YET = -1L;
        private long bytesRemainingInChunk = -1L;
        private boolean hasMoreChunks = true;
        private final HttpEngine httpEngine;

        ChunkedSource(HttpEngine httpEngine) {
            this.httpEngine = httpEngine;
        }

        private void readChunkSize() {
            if (this.bytesRemainingInChunk != -1L) {
                HttpConnection.this.source.readUtf8LineStrict();
            }
            try {
                this.bytesRemainingInChunk = HttpConnection.this.source.readHexadecimalUnsignedLong();
                String string = HttpConnection.this.source.readUtf8LineStrict().trim();
                if (this.bytesRemainingInChunk < 0L || !string.isEmpty() && !string.startsWith(";")) {
                    throw new ProtocolException("expected chunk size and optional extensions but was \"" + this.bytesRemainingInChunk + string + "\"");
                }
            }
            catch (NumberFormatException numberFormatException) {
                throw new ProtocolException(numberFormatException.getMessage());
            }
            if (this.bytesRemainingInChunk == 0L) {
                this.hasMoreChunks = false;
                Headers.Builder builder = new Headers.Builder();
                HttpConnection.this.readHeaders(builder);
                this.httpEngine.receiveHeaders(builder.build());
                this.endOfInput(true);
            }
        }

        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                this.unexpectedEndOfInput();
            }
            this.closed = true;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public long read(Buffer buffer, long l2) {
            long l3;
            block7 : {
                block6 : {
                    if (l2 < 0L) {
                        throw new IllegalArgumentException("byteCount < 0: " + l2);
                    }
                    if (this.closed) {
                        throw new IllegalStateException("closed");
                    }
                    if (!this.hasMoreChunks) break block6;
                    if (this.bytesRemainingInChunk != 0L && this.bytesRemainingInChunk != -1L) break block7;
                    this.readChunkSize();
                    if (this.hasMoreChunks) break block7;
                }
                return -1L;
            }
            if ((l3 = HttpConnection.this.source.read(buffer, Math.min((long)l2, (long)this.bytesRemainingInChunk))) == -1L) {
                this.unexpectedEndOfInput();
                throw new IOException("unexpected end of stream");
            }
            this.bytesRemainingInChunk -= l3;
            return l3;
        }
    }

    private final class FixedLengthSink
    implements Sink {
        private long bytesRemaining;
        private boolean closed;
        private final ForwardingTimeout timeout;

        private FixedLengthSink(long l2) {
            this.timeout = new ForwardingTimeout(HttpConnection.this.sink.timeout());
            this.bytesRemaining = l2;
        }

        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            this.closed = true;
            if (this.bytesRemaining > 0L) {
                throw new ProtocolException("unexpected end of stream");
            }
            HttpConnection.this.detachTimeout(this.timeout);
            HttpConnection.this.state = 3;
        }

        @Override
        public void flush() {
            if (this.closed) {
                return;
            }
            HttpConnection.this.sink.flush();
        }

        @Override
        public Timeout timeout() {
            return this.timeout;
        }

        @Override
        public void write(Buffer buffer, long l2) {
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            Util.checkOffsetAndCount(buffer.size(), 0L, l2);
            if (l2 > this.bytesRemaining) {
                throw new ProtocolException("expected " + this.bytesRemaining + " bytes but received " + l2);
            }
            HttpConnection.this.sink.write(buffer, l2);
            this.bytesRemaining -= l2;
        }
    }

    private class FixedLengthSource
    extends AbstractSource {
        private long bytesRemaining;

        public FixedLengthSource(long l2) {
            this.bytesRemaining = l2;
            if (this.bytesRemaining == 0L) {
                this.endOfInput(true);
            }
        }

        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            if (this.bytesRemaining != 0L && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                this.unexpectedEndOfInput();
            }
            this.closed = true;
        }

        @Override
        public long read(Buffer buffer, long l2) {
            if (l2 < 0L) {
                throw new IllegalArgumentException("byteCount < 0: " + l2);
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (this.bytesRemaining == 0L) {
                return -1L;
            }
            long l3 = HttpConnection.this.source.read(buffer, Math.min((long)this.bytesRemaining, (long)l2));
            if (l3 == -1L) {
                this.unexpectedEndOfInput();
                throw new ProtocolException("unexpected end of stream");
            }
            this.bytesRemaining -= l3;
            if (this.bytesRemaining == 0L) {
                this.endOfInput(true);
            }
            return l3;
        }
    }

    private class UnknownLengthSource
    extends AbstractSource {
        private boolean inputExhausted;

        private UnknownLengthSource() {
        }

        @Override
        public void close() {
            if (this.closed) {
                return;
            }
            if (!this.inputExhausted) {
                this.unexpectedEndOfInput();
            }
            this.closed = true;
        }

        @Override
        public long read(Buffer buffer, long l2) {
            if (l2 < 0L) {
                throw new IllegalArgumentException("byteCount < 0: " + l2);
            }
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            if (this.inputExhausted) {
                return -1L;
            }
            long l3 = HttpConnection.this.source.read(buffer, l2);
            if (l3 == -1L) {
                this.inputExhausted = true;
                this.endOfInput(false);
                return -1L;
            }
            return l3;
        }
    }

}

