package com.squareup.okhttp.internal.spdy;

import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public final class SpdyStream {
    static final /* synthetic */ boolean $assertionsDisabled;
    long bytesLeftInWriteWindow;
    private final SpdyConnection connection;
    private ErrorCode errorCode;
    private final int id;
    private final SpdyTimeout readTimeout;
    private final List<Header> requestHeaders;
    private List<Header> responseHeaders;
    final SpdyDataSink sink;
    private final SpdyDataSource source;
    long unacknowledgedBytesRead;
    private final SpdyTimeout writeTimeout;

    final class SpdyDataSink implements Sink {
        static final /* synthetic */ boolean $assertionsDisabled;
        private static final long EMIT_BUFFER_SIZE = 16384;
        private boolean closed;
        private boolean finished;
        private final Buffer sendBuffer;

        static {
            $assertionsDisabled = !SpdyStream.class.desiredAssertionStatus() ? true : $assertionsDisabled;
        }

        SpdyDataSink() {
            this.sendBuffer = new Buffer();
        }

        public void write(Buffer buffer, long j) {
            if ($assertionsDisabled || !Thread.holdsLock(SpdyStream.this)) {
                this.sendBuffer.write(buffer, j);
                while (this.sendBuffer.size() >= EMIT_BUFFER_SIZE) {
                    emitDataFrame($assertionsDisabled);
                }
                return;
            }
            throw new AssertionError();
        }

        private void emitDataFrame(boolean z) {
            synchronized (SpdyStream.this) {
                SpdyStream.this.writeTimeout.enter();
                while (SpdyStream.this.bytesLeftInWriteWindow <= 0 && !this.finished && !this.closed && SpdyStream.this.errorCode == null) {
                    try {
                        SpdyStream.this.waitForIo();
                    } catch (Throwable th) {
                        SpdyStream.this.writeTimeout.exitAndThrowIfTimedOut();
                    }
                }
                SpdyStream.this.writeTimeout.exitAndThrowIfTimedOut();
                SpdyStream.this.checkOutNotClosed();
                long min = Math.min(SpdyStream.this.bytesLeftInWriteWindow, this.sendBuffer.size());
                SpdyStream spdyStream = SpdyStream.this;
                spdyStream.bytesLeftInWriteWindow -= min;
            }
            SpdyStream.this.writeTimeout.enter();
            try {
                SpdyConnection access$500 = SpdyStream.this.connection;
                int access$600 = SpdyStream.this.id;
                boolean z2 = (z && min == this.sendBuffer.size()) ? true : $assertionsDisabled;
                access$500.writeData(access$600, z2, this.sendBuffer, min);
            } finally {
                SpdyStream.this.writeTimeout.exitAndThrowIfTimedOut();
            }
        }

        public void flush() {
            if ($assertionsDisabled || !Thread.holdsLock(SpdyStream.this)) {
                synchronized (SpdyStream.this) {
                    SpdyStream.this.checkOutNotClosed();
                }
                while (this.sendBuffer.size() > 0) {
                    emitDataFrame($assertionsDisabled);
                    SpdyStream.this.connection.flush();
                }
                return;
            }
            throw new AssertionError();
        }

        public Timeout timeout() {
            return SpdyStream.this.writeTimeout;
        }

        public void close() {
            if ($assertionsDisabled || !Thread.holdsLock(SpdyStream.this)) {
                synchronized (SpdyStream.this) {
                    if (this.closed) {
                        return;
                    }
                    if (!SpdyStream.this.sink.finished) {
                        if (this.sendBuffer.size() > 0) {
                            while (this.sendBuffer.size() > 0) {
                                emitDataFrame(true);
                            }
                        } else {
                            SpdyStream.this.connection.writeData(SpdyStream.this.id, true, null, 0);
                        }
                    }
                    synchronized (SpdyStream.this) {
                        this.closed = true;
                    }
                    SpdyStream.this.connection.flush();
                    SpdyStream.this.cancelStreamIfNecessary();
                    return;
                }
            }
            throw new AssertionError();
        }
    }

    private final class SpdyDataSource implements Source {
        static final /* synthetic */ boolean $assertionsDisabled;
        private boolean closed;
        private boolean finished;
        private final long maxByteCount;
        private final Buffer readBuffer;
        private final Buffer receiveBuffer;

        static {
            $assertionsDisabled = !SpdyStream.class.desiredAssertionStatus();
        }

        private SpdyDataSource(long j) {
            this.receiveBuffer = new Buffer();
            this.readBuffer = new Buffer();
            this.maxByteCount = j;
        }

        public long read(Buffer buffer, long j) {
            if (j < 0) {
                throw new IllegalArgumentException("byteCount < 0: " + j);
            }
            long j2;
            synchronized (SpdyStream.this) {
                waitUntilReadable();
                checkNotClosed();
                if (this.readBuffer.size() == 0) {
                    j2 = -1;
                } else {
                    j2 = this.readBuffer.read(buffer, Math.min(j, this.readBuffer.size()));
                    SpdyStream spdyStream = SpdyStream.this;
                    spdyStream.unacknowledgedBytesRead += j2;
                    if (SpdyStream.this.unacknowledgedBytesRead >= ((long) (SpdyStream.this.connection.okHttpSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized) / 2))) {
                        SpdyStream.this.connection.writeWindowUpdateLater(SpdyStream.this.id, SpdyStream.this.unacknowledgedBytesRead);
                        SpdyStream.this.unacknowledgedBytesRead = 0;
                    }
                    synchronized (SpdyStream.this.connection) {
                        SpdyConnection access$500 = SpdyStream.this.connection;
                        access$500.unacknowledgedBytesRead += j2;
                        if (SpdyStream.this.connection.unacknowledgedBytesRead >= ((long) (SpdyStream.this.connection.okHttpSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized) / 2))) {
                            SpdyStream.this.connection.writeWindowUpdateLater(0, SpdyStream.this.connection.unacknowledgedBytesRead);
                            SpdyStream.this.connection.unacknowledgedBytesRead = 0;
                        }
                    }
                }
            }
            return j2;
        }

        private void waitUntilReadable() {
            SpdyStream.this.readTimeout.enter();
            while (this.readBuffer.size() == 0 && !this.finished && !this.closed && SpdyStream.this.errorCode == null) {
                try {
                    SpdyStream.this.waitForIo();
                } catch (Throwable th) {
                    SpdyStream.this.readTimeout.exitAndThrowIfTimedOut();
                }
            }
            SpdyStream.this.readTimeout.exitAndThrowIfTimedOut();
        }

        void receive(BufferedSource bufferedSource, long j) {
            if ($assertionsDisabled || !Thread.holdsLock(SpdyStream.this)) {
                while (j > 0) {
                    boolean z;
                    Object obj;
                    synchronized (SpdyStream.this) {
                        z = this.finished;
                        obj = this.readBuffer.size() + j > this.maxByteCount ? 1 : null;
                    }
                    if (obj != null) {
                        bufferedSource.skip(j);
                        SpdyStream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                        return;
                    } else if (z) {
                        bufferedSource.skip(j);
                        return;
                    } else {
                        long read = bufferedSource.read(this.receiveBuffer, j);
                        if (read == -1) {
                            throw new EOFException();
                        }
                        j -= read;
                        synchronized (SpdyStream.this) {
                            if (this.readBuffer.size() == 0) {
                                obj = 1;
                            } else {
                                obj = null;
                            }
                            this.readBuffer.writeAll(this.receiveBuffer);
                            if (obj != null) {
                                SpdyStream.this.notifyAll();
                            }
                        }
                    }
                }
                return;
            }
            throw new AssertionError();
        }

        public Timeout timeout() {
            return SpdyStream.this.readTimeout;
        }

        public void close() {
            synchronized (SpdyStream.this) {
                this.closed = true;
                this.readBuffer.clear();
                SpdyStream.this.notifyAll();
            }
            SpdyStream.this.cancelStreamIfNecessary();
        }

        private void checkNotClosed() {
            if (this.closed) {
                throw new IOException("stream closed");
            } else if (SpdyStream.this.errorCode != null) {
                throw new IOException("stream was reset: " + SpdyStream.this.errorCode);
            }
        }
    }

    class SpdyTimeout extends AsyncTimeout {
        SpdyTimeout() {
        }

        protected void timedOut() {
            SpdyStream.this.closeLater(ErrorCode.CANCEL);
        }

        public void exitAndThrowIfTimedOut() {
            if (exit()) {
                throw new InterruptedIOException("timeout");
            }
        }
    }

    static {
        $assertionsDisabled = !SpdyStream.class.desiredAssertionStatus();
    }

    SpdyStream(int i, SpdyConnection spdyConnection, boolean z, boolean z2, List<Header> list) {
        this.unacknowledgedBytesRead = 0;
        this.readTimeout = new SpdyTimeout();
        this.writeTimeout = new SpdyTimeout();
        this.errorCode = null;
        if (spdyConnection == null) {
            throw new NullPointerException("connection == null");
        } else if (list == null) {
            throw new NullPointerException("requestHeaders == null");
        } else {
            this.id = i;
            this.connection = spdyConnection;
            this.bytesLeftInWriteWindow = (long) spdyConnection.peerSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized);
            this.source = new SpdyDataSource((long) spdyConnection.okHttpSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized), null);
            this.sink = new SpdyDataSink();
            this.source.finished = z2;
            this.sink.finished = z;
            this.requestHeaders = list;
        }
    }

    public int getId() {
        return this.id;
    }

    public synchronized boolean isOpen() {
        boolean z = false;
        synchronized (this) {
            if (this.errorCode == null) {
                if (!(this.source.finished || this.source.closed) || (!(this.sink.finished || this.sink.closed) || this.responseHeaders == null)) {
                    z = true;
                }
            }
        }
        return z;
    }

    public boolean isLocallyInitiated() {
        boolean z;
        if ((this.id & 1) == 1) {
            z = true;
        } else {
            z = false;
        }
        return this.connection.client == z;
    }

    public SpdyConnection getConnection() {
        return this.connection;
    }

    public List<Header> getRequestHeaders() {
        return this.requestHeaders;
    }

    public synchronized List<Header> getResponseHeaders() {
        this.readTimeout.enter();
        while (this.responseHeaders == null && this.errorCode == null) {
            try {
                waitForIo();
            } catch (Throwable th) {
                this.readTimeout.exitAndThrowIfTimedOut();
            }
        }
        this.readTimeout.exitAndThrowIfTimedOut();
        if (this.responseHeaders != null) {
        } else {
            throw new IOException("stream was reset: " + this.errorCode);
        }
        return this.responseHeaders;
    }

    public synchronized ErrorCode getErrorCode() {
        return this.errorCode;
    }

    public void reply(List<Header> list, boolean z) {
        boolean z2 = true;
        if ($assertionsDisabled || !Thread.holdsLock(this)) {
            synchronized (this) {
                if (list == null) {
                    throw new NullPointerException("responseHeaders == null");
                } else if (this.responseHeaders != null) {
                    throw new IllegalStateException("reply already sent");
                } else {
                    this.responseHeaders = list;
                    if (z) {
                        z2 = false;
                    } else {
                        this.sink.finished = true;
                    }
                }
            }
            this.connection.writeSynReply(this.id, z2, list);
            if (z2) {
                this.connection.flush();
                return;
            }
            return;
        }
        throw new AssertionError();
    }

    public Timeout readTimeout() {
        return this.readTimeout;
    }

    public Timeout writeTimeout() {
        return this.writeTimeout;
    }

    public Source getSource() {
        return this.source;
    }

    public Sink getSink() {
        synchronized (this) {
            if (this.responseHeaders != null || isLocallyInitiated()) {
            } else {
                throw new IllegalStateException("reply before requesting the sink");
            }
        }
        return this.sink;
    }

    public void close(ErrorCode errorCode) {
        if (closeInternal(errorCode)) {
            this.connection.writeSynReset(this.id, errorCode);
        }
    }

    public void closeLater(ErrorCode errorCode) {
        if (closeInternal(errorCode)) {
            this.connection.writeSynResetLater(this.id, errorCode);
        }
    }

    private boolean closeInternal(ErrorCode errorCode) {
        if ($assertionsDisabled || !Thread.holdsLock(this)) {
            synchronized (this) {
                if (this.errorCode != null) {
                    return false;
                } else if (this.source.finished && this.sink.finished) {
                    return false;
                } else {
                    this.errorCode = errorCode;
                    notifyAll();
                    this.connection.removeStream(this.id);
                    return true;
                }
            }
        }
        throw new AssertionError();
    }

    void receiveHeaders(List<Header> list, HeadersMode headersMode) {
        if ($assertionsDisabled || !Thread.holdsLock(this)) {
            ErrorCode errorCode = null;
            boolean z = true;
            synchronized (this) {
                if (this.responseHeaders == null) {
                    if (headersMode.failIfHeadersAbsent()) {
                        errorCode = ErrorCode.PROTOCOL_ERROR;
                    } else {
                        this.responseHeaders = list;
                        z = isOpen();
                        notifyAll();
                    }
                } else if (headersMode.failIfHeadersPresent()) {
                    errorCode = ErrorCode.STREAM_IN_USE;
                } else {
                    List arrayList = new ArrayList();
                    arrayList.addAll(this.responseHeaders);
                    arrayList.addAll(list);
                    this.responseHeaders = arrayList;
                }
            }
            if (errorCode != null) {
                closeLater(errorCode);
                return;
            } else if (!z) {
                this.connection.removeStream(this.id);
                return;
            } else {
                return;
            }
        }
        throw new AssertionError();
    }

    void receiveData(BufferedSource bufferedSource, int i) {
        if ($assertionsDisabled || !Thread.holdsLock(this)) {
            this.source.receive(bufferedSource, (long) i);
            return;
        }
        throw new AssertionError();
    }

    void receiveFin() {
        if ($assertionsDisabled || !Thread.holdsLock(this)) {
            boolean isOpen;
            synchronized (this) {
                this.source.finished = true;
                isOpen = isOpen();
                notifyAll();
            }
            if (!isOpen) {
                this.connection.removeStream(this.id);
                return;
            }
            return;
        }
        throw new AssertionError();
    }

    synchronized void receiveRstStream(ErrorCode errorCode) {
        if (this.errorCode == null) {
            this.errorCode = errorCode;
            notifyAll();
        }
    }

    private void cancelStreamIfNecessary() {
        if ($assertionsDisabled || !Thread.holdsLock(this)) {
            Object obj;
            boolean isOpen;
            synchronized (this) {
                obj = (!this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed)) ? 1 : null;
                isOpen = isOpen();
            }
            if (obj != null) {
                close(ErrorCode.CANCEL);
                return;
            } else if (!isOpen) {
                this.connection.removeStream(this.id);
                return;
            } else {
                return;
            }
        }
        throw new AssertionError();
    }

    void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    private void checkOutNotClosed() {
        if (this.sink.closed) {
            throw new IOException("stream closed");
        } else if (this.sink.finished) {
            throw new IOException("stream finished");
        } else if (this.errorCode != null) {
            throw new IOException("stream was reset: " + this.errorCode);
        }
    }

    private void waitForIo() {
        try {
            wait();
        } catch (InterruptedException e) {
            throw new InterruptedIOException();
        }
    }
}
