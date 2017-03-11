package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Headers.Builder;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.Util;
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
    private int onIdle;
    private final ConnectionPool pool;
    private final BufferedSink sink;
    private final Socket socket;
    private final BufferedSource source;
    private int state;

    private abstract class AbstractSource implements Source {
        protected boolean closed;
        protected final ForwardingTimeout timeout;

        private AbstractSource() {
            this.timeout = new ForwardingTimeout(HttpConnection.this.source.timeout());
        }

        public Timeout timeout() {
            return this.timeout;
        }

        protected final void endOfInput(boolean z) {
            if (HttpConnection.this.state != HttpConnection.STATE_READING_RESPONSE_BODY) {
                throw new IllegalStateException("state: " + HttpConnection.this.state);
            }
            HttpConnection.this.detachTimeout(this.timeout);
            HttpConnection.this.state = HttpConnection.STATE_IDLE;
            if (z && HttpConnection.this.onIdle == HttpConnection.STATE_OPEN_REQUEST_BODY) {
                HttpConnection.this.onIdle = HttpConnection.STATE_IDLE;
                Internal.instance.recycle(HttpConnection.this.pool, HttpConnection.this.connection);
            } else if (HttpConnection.this.onIdle == HttpConnection.STATE_WRITING_REQUEST_BODY) {
                HttpConnection.this.state = HttpConnection.STATE_CLOSED;
                HttpConnection.this.connection.getSocket().close();
            }
        }

        protected final void unexpectedEndOfInput() {
            Util.closeQuietly(HttpConnection.this.connection.getSocket());
            HttpConnection.this.state = HttpConnection.STATE_CLOSED;
        }
    }

    private final class ChunkedSink implements Sink {
        private boolean closed;
        private final ForwardingTimeout timeout;

        private ChunkedSink() {
            this.timeout = new ForwardingTimeout(HttpConnection.this.sink.timeout());
        }

        public Timeout timeout() {
            return this.timeout;
        }

        public void write(Buffer buffer, long j) {
            if (this.closed) {
                throw new IllegalStateException("closed");
            } else if (j != 0) {
                HttpConnection.this.sink.writeHexadecimalUnsignedLong(j);
                HttpConnection.this.sink.writeUtf8("\r\n");
                HttpConnection.this.sink.write(buffer, j);
                HttpConnection.this.sink.writeUtf8("\r\n");
            }
        }

        public synchronized void flush() {
            if (!this.closed) {
                HttpConnection.this.sink.flush();
            }
        }

        public synchronized void close() {
            if (!this.closed) {
                this.closed = true;
                HttpConnection.this.sink.writeUtf8("0\r\n\r\n");
                HttpConnection.this.detachTimeout(this.timeout);
                HttpConnection.this.state = HttpConnection.STATE_READ_RESPONSE_HEADERS;
            }
        }
    }

    private class ChunkedSource extends AbstractSource {
        private static final long NO_CHUNK_YET = -1;
        private long bytesRemainingInChunk;
        private boolean hasMoreChunks;
        private final HttpEngine httpEngine;

        ChunkedSource(HttpEngine httpEngine) {
            super(null);
            this.bytesRemainingInChunk = NO_CHUNK_YET;
            this.hasMoreChunks = true;
            this.httpEngine = httpEngine;
        }

        public long read(Buffer buffer, long j) {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            } else if (this.closed) {
                throw new IllegalStateException("closed");
            } else if (!this.hasMoreChunks) {
                return NO_CHUNK_YET;
            } else {
                if (this.bytesRemainingInChunk == 0 || this.bytesRemainingInChunk == NO_CHUNK_YET) {
                    readChunkSize();
                    if (!this.hasMoreChunks) {
                        return NO_CHUNK_YET;
                    }
                }
                long read = HttpConnection.this.source.read(buffer, Math.min(j, this.bytesRemainingInChunk));
                if (read == NO_CHUNK_YET) {
                    unexpectedEndOfInput();
                    throw new IOException("unexpected end of stream");
                }
                this.bytesRemainingInChunk -= read;
                return read;
            }
        }

        private void readChunkSize() {
            if (this.bytesRemainingInChunk != NO_CHUNK_YET) {
                HttpConnection.this.source.readUtf8LineStrict();
            }
            try {
                this.bytesRemainingInChunk = HttpConnection.this.source.readHexadecimalUnsignedLong();
                String trim = HttpConnection.this.source.readUtf8LineStrict().trim();
                if (this.bytesRemainingInChunk < 0 || !(trim.isEmpty() || trim.startsWith(";"))) {
                    throw new ProtocolException("expected chunk size and optional extensions but was \"" + this.bytesRemainingInChunk + trim + "\"");
                } else if (this.bytesRemainingInChunk == 0) {
                    this.hasMoreChunks = false;
                    Builder builder = new Builder();
                    HttpConnection.this.readHeaders(builder);
                    this.httpEngine.receiveHeaders(builder.build());
                    endOfInput(true);
                }
            } catch (NumberFormatException e) {
                throw new ProtocolException(e.getMessage());
            }
        }

        public void close() {
            if (!this.closed) {
                if (this.hasMoreChunks && !Util.discard(this, 100, TimeUnit.MILLISECONDS)) {
                    unexpectedEndOfInput();
                }
                this.closed = true;
            }
        }
    }

    private final class FixedLengthSink implements Sink {
        private long bytesRemaining;
        private boolean closed;
        private final ForwardingTimeout timeout;

        private FixedLengthSink(long j) {
            this.timeout = new ForwardingTimeout(HttpConnection.this.sink.timeout());
            this.bytesRemaining = j;
        }

        public Timeout timeout() {
            return this.timeout;
        }

        public void write(Buffer buffer, long j) {
            if (this.closed) {
                throw new IllegalStateException("closed");
            }
            Util.checkOffsetAndCount(buffer.size(), 0, j);
            if (j > this.bytesRemaining) {
                throw new ProtocolException("expected " + this.bytesRemaining + " bytes but received " + j);
            }
            HttpConnection.this.sink.write(buffer, j);
            this.bytesRemaining -= j;
        }

        public void flush() {
            if (!this.closed) {
                HttpConnection.this.sink.flush();
            }
        }

        public void close() {
            if (!this.closed) {
                this.closed = true;
                if (this.bytesRemaining > 0) {
                    throw new ProtocolException("unexpected end of stream");
                }
                HttpConnection.this.detachTimeout(this.timeout);
                HttpConnection.this.state = HttpConnection.STATE_READ_RESPONSE_HEADERS;
            }
        }
    }

    private class FixedLengthSource extends AbstractSource {
        private long bytesRemaining;

        public FixedLengthSource(long j) {
            super(null);
            this.bytesRemaining = j;
            if (this.bytesRemaining == 0) {
                endOfInput(true);
            }
        }

        public long read(Buffer buffer, long j) {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            } else if (this.closed) {
                throw new IllegalStateException("closed");
            } else if (this.bytesRemaining == 0) {
                return -1;
            } else {
                long read = HttpConnection.this.source.read(buffer, Math.min(this.bytesRemaining, j));
                if (read == -1) {
                    unexpectedEndOfInput();
                    throw new ProtocolException("unexpected end of stream");
                }
                this.bytesRemaining -= read;
                if (this.bytesRemaining == 0) {
                    endOfInput(true);
                }
                return read;
            }
        }

        public void close() {
            if (!this.closed) {
                if (!(this.bytesRemaining == 0 || Util.discard(this, 100, TimeUnit.MILLISECONDS))) {
                    unexpectedEndOfInput();
                }
                this.closed = true;
            }
        }
    }

    private class UnknownLengthSource extends AbstractSource {
        private boolean inputExhausted;

        private UnknownLengthSource() {
            super(null);
        }

        public long read(Buffer buffer, long j) {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            } else if (this.closed) {
                throw new IllegalStateException("closed");
            } else if (this.inputExhausted) {
                return -1;
            } else {
                long read = HttpConnection.this.source.read(buffer, j);
                if (read != -1) {
                    return read;
                }
                this.inputExhausted = true;
                endOfInput(false);
                return -1;
            }
        }

        public void close() {
            if (!this.closed) {
                if (!this.inputExhausted) {
                    unexpectedEndOfInput();
                }
                this.closed = true;
            }
        }
    }

    public HttpConnection(ConnectionPool connectionPool, Connection connection, Socket socket) {
        this.state = STATE_IDLE;
        this.onIdle = STATE_IDLE;
        this.pool = connectionPool;
        this.connection = connection;
        this.socket = socket;
        this.source = Okio.buffer(Okio.source(socket));
        this.sink = Okio.buffer(Okio.sink(socket));
    }

    public void setTimeouts(int i, int i2) {
        if (i != 0) {
            this.source.timeout().timeout((long) i, TimeUnit.MILLISECONDS);
        }
        if (i2 != 0) {
            this.sink.timeout().timeout((long) i2, TimeUnit.MILLISECONDS);
        }
    }

    public void poolOnIdle() {
        this.onIdle = STATE_OPEN_REQUEST_BODY;
        if (this.state == 0) {
            this.onIdle = STATE_IDLE;
            Internal.instance.recycle(this.pool, this.connection);
        }
    }

    public void closeOnIdle() {
        this.onIdle = STATE_WRITING_REQUEST_BODY;
        if (this.state == 0) {
            this.state = STATE_CLOSED;
            this.connection.getSocket().close();
        }
    }

    public boolean isClosed() {
        return this.state == STATE_CLOSED;
    }

    public void closeIfOwnedBy(Object obj) {
        Internal.instance.closeIfOwnedBy(this.connection, obj);
    }

    public void flush() {
        this.sink.flush();
    }

    public long bufferSize() {
        return this.source.buffer().size();
    }

    public boolean isReadable() {
        int soTimeout;
        try {
            soTimeout = this.socket.getSoTimeout();
            this.socket.setSoTimeout(STATE_OPEN_REQUEST_BODY);
            if (this.source.exhausted()) {
                this.socket.setSoTimeout(soTimeout);
                return false;
            }
            this.socket.setSoTimeout(soTimeout);
            return true;
        } catch (SocketTimeoutException e) {
            return true;
        } catch (IOException e2) {
            return false;
        } catch (Throwable th) {
            this.socket.setSoTimeout(soTimeout);
        }
    }

    public void writeRequest(Headers headers, String str) {
        if (this.state != 0) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.sink.writeUtf8(str).writeUtf8("\r\n");
        int size = headers.size();
        for (int i = STATE_IDLE; i < size; i += STATE_OPEN_REQUEST_BODY) {
            this.sink.writeUtf8(headers.name(i)).writeUtf8(": ").writeUtf8(headers.value(i)).writeUtf8("\r\n");
        }
        this.sink.writeUtf8("\r\n");
        this.state = STATE_OPEN_REQUEST_BODY;
    }

    public Response.Builder readResponse() {
        if (this.state == STATE_OPEN_REQUEST_BODY || this.state == STATE_READ_RESPONSE_HEADERS) {
            Response.Builder message;
            StatusLine parse;
            do {
                try {
                    parse = StatusLine.parse(this.source.readUtf8LineStrict());
                    message = new Response.Builder().protocol(parse.protocol).code(parse.code).message(parse.message);
                    Builder builder = new Builder();
                    readHeaders(builder);
                    builder.add(OkHeaders.SELECTED_PROTOCOL, parse.protocol.toString());
                    message.headers(builder.build());
                } catch (Throwable e) {
                    IOException iOException = new IOException("unexpected end of stream on " + this.connection + " (recycle count=" + Internal.instance.recycleCount(this.connection) + ")");
                    iOException.initCause(e);
                    throw iOException;
                }
            } while (parse.code == 100);
            this.state = STATE_OPEN_RESPONSE_BODY;
            return message;
        }
        throw new IllegalStateException("state: " + this.state);
    }

    public void readHeaders(Builder builder) {
        while (true) {
            String readUtf8LineStrict = this.source.readUtf8LineStrict();
            if (readUtf8LineStrict.length() != 0) {
                Internal.instance.addLenient(builder, readUtf8LineStrict);
            } else {
                return;
            }
        }
    }

    public Sink newChunkedSink() {
        if (this.state != STATE_OPEN_REQUEST_BODY) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = STATE_WRITING_REQUEST_BODY;
        return new ChunkedSink();
    }

    public Sink newFixedLengthSink(long j) {
        if (this.state != STATE_OPEN_REQUEST_BODY) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = STATE_WRITING_REQUEST_BODY;
        return new FixedLengthSink(j, null);
    }

    public void writeRequestBody(RetryableSink retryableSink) {
        if (this.state != STATE_OPEN_REQUEST_BODY) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = STATE_READ_RESPONSE_HEADERS;
        retryableSink.writeToSocket(this.sink);
    }

    public Source newFixedLengthSource(long j) {
        if (this.state != STATE_OPEN_RESPONSE_BODY) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = STATE_READING_RESPONSE_BODY;
        return new FixedLengthSource(j);
    }

    public Source newChunkedSource(HttpEngine httpEngine) {
        if (this.state != STATE_OPEN_RESPONSE_BODY) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = STATE_READING_RESPONSE_BODY;
        return new ChunkedSource(httpEngine);
    }

    public Source newUnknownLengthSource() {
        if (this.state != STATE_OPEN_RESPONSE_BODY) {
            throw new IllegalStateException("state: " + this.state);
        }
        this.state = STATE_READING_RESPONSE_BODY;
        return new UnknownLengthSource();
    }

    public BufferedSink rawSink() {
        return this.sink;
    }

    public BufferedSource rawSource() {
        return this.source;
    }

    private void detachTimeout(ForwardingTimeout forwardingTimeout) {
        Timeout delegate = forwardingTimeout.delegate();
        forwardingTimeout.setDelegate(Timeout.NONE);
        delegate.clearDeadline();
        delegate.clearTimeout();
    }
}
