/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.EOFException
 *  java.io.IOException
 *  java.io.InterruptedIOException
 *  java.lang.AssertionError
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.InterruptedException
 *  java.lang.Math
 *  java.lang.NullPointerException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.Thread
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.List
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.HeadersMode;
import com.squareup.okhttp.internal.spdy.Settings;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import java.io.EOFException;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import okio.AsyncTimeout;
import okio.Buffer;
import okio.BufferedSource;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class SpdyStream {
    static final /* synthetic */ boolean $assertionsDisabled;
    long bytesLeftInWriteWindow;
    private final SpdyConnection connection;
    private ErrorCode errorCode = null;
    private final int id;
    private final SpdyTimeout readTimeout = new SpdyTimeout();
    private final List<Header> requestHeaders;
    private List<Header> responseHeaders;
    final SpdyDataSink sink;
    private final SpdyDataSource source;
    long unacknowledgedBytesRead = 0L;
    private final SpdyTimeout writeTimeout = new SpdyTimeout();

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = !SpdyStream.class.desiredAssertionStatus();
        $assertionsDisabled = bl;
    }

    SpdyStream(int n2, SpdyConnection spdyConnection, boolean bl, boolean bl2, List<Header> list) {
        if (spdyConnection == null) {
            throw new NullPointerException("connection == null");
        }
        if (list == null) {
            throw new NullPointerException("requestHeaders == null");
        }
        this.id = n2;
        this.connection = spdyConnection;
        this.bytesLeftInWriteWindow = spdyConnection.peerSettings.getInitialWindowSize(65536);
        this.source = new SpdyDataSource(spdyConnection.okHttpSettings.getInitialWindowSize(65536));
        this.sink = new SpdyDataSink();
        this.source.finished = bl2;
        this.sink.finished = bl;
        this.requestHeaders = list;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void cancelStreamIfNecessary() {
        if (!$assertionsDisabled && Thread.holdsLock((Object)this)) {
            throw new AssertionError();
        }
        SpdyStream spdyStream = this;
        // MONITORENTER : spdyStream
        boolean bl = !this.source.finished && this.source.closed && (this.sink.finished || this.sink.closed);
        boolean bl2 = this.isOpen();
        // MONITOREXIT : spdyStream
        if (bl) {
            this.close(ErrorCode.CANCEL);
            return;
        }
        if (bl2) return;
        this.connection.removeStream(this.id);
    }

    private void checkOutNotClosed() {
        if (this.sink.closed) {
            throw new IOException("stream closed");
        }
        if (this.sink.finished) {
            throw new IOException("stream finished");
        }
        if (this.errorCode != null) {
            throw new IOException("stream was reset: " + (Object)((Object)this.errorCode));
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private boolean closeInternal(ErrorCode errorCode) {
        if (!$assertionsDisabled && Thread.holdsLock((Object)this)) {
            throw new AssertionError();
        }
        SpdyStream spdyStream = this;
        synchronized (spdyStream) {
            if (this.errorCode != null) {
                return false;
            }
            if (this.source.finished && this.sink.finished) {
                return false;
            }
            this.errorCode = errorCode;
            this.notifyAll();
        }
        this.connection.removeStream(this.id);
        return true;
    }

    private void waitForIo() {
        try {
            this.wait();
            return;
        }
        catch (InterruptedException interruptedException) {
            throw new InterruptedIOException();
        }
    }

    void addBytesToWriteWindow(long l2) {
        this.bytesLeftInWriteWindow = l2 + this.bytesLeftInWriteWindow;
        if (l2 > 0L) {
            this.notifyAll();
        }
    }

    public void close(ErrorCode errorCode) {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynReset(this.id, errorCode);
    }

    public void closeLater(ErrorCode errorCode) {
        if (!this.closeInternal(errorCode)) {
            return;
        }
        this.connection.writeSynResetLater(this.id, errorCode);
    }

    public SpdyConnection getConnection() {
        return this.connection;
    }

    public ErrorCode getErrorCode() {
        SpdyStream spdyStream = this;
        synchronized (spdyStream) {
            ErrorCode errorCode = this.errorCode;
            return errorCode;
        }
    }

    public int getId() {
        return this.id;
    }

    public List<Header> getRequestHeaders() {
        return this.requestHeaders;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public List<Header> getResponseHeaders() {
        SpdyStream spdyStream = this;
        synchronized (spdyStream) {
            this.readTimeout.enter();
            try {
                while (this.responseHeaders == null && this.errorCode == null) {
                    this.waitForIo();
                }
            }
            finally {
                this.readTimeout.exitAndThrowIfTimedOut();
            }
            if (this.responseHeaders == null) throw new IOException("stream was reset: " + (Object)((Object)this.errorCode));
            return this.responseHeaders;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Sink getSink() {
        SpdyStream spdyStream = this;
        synchronized (spdyStream) {
            if (this.responseHeaders == null && !this.isLocallyInitiated()) {
                throw new IllegalStateException("reply before requesting the sink");
            }
            return this.sink;
        }
    }

    public Source getSource() {
        return this.source;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isLocallyInitiated() {
        boolean bl;
        if ((1 & this.id) == 1) {
            bl = true;
            do {
                return this.connection.client == bl;
                break;
            } while (true);
        }
        bl = false;
        return this.connection.client == bl;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isOpen() {
        SpdyStream spdyStream = this;
        synchronized (spdyStream) {
            boolean bl;
            block6 : {
                ErrorCode errorCode = this.errorCode;
                bl = false;
                if (errorCode == null) break block6;
                return bl;
            }
            if (!this.source.finished) {
                if (!this.source.closed) return true;
            }
            if (!this.sink.finished) {
                if (!this.sink.closed) return true;
            }
            List<Header> list = this.responseHeaders;
            bl = false;
            if (list != null) return bl;
            return true;
        }
    }

    public Timeout readTimeout() {
        return this.readTimeout;
    }

    void receiveData(BufferedSource bufferedSource, int n2) {
        if (!$assertionsDisabled && Thread.holdsLock((Object)this)) {
            throw new AssertionError();
        }
        this.source.receive(bufferedSource, n2);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void receiveFin() {
        if (!$assertionsDisabled && Thread.holdsLock((Object)this)) {
            throw new AssertionError();
        }
        SpdyStream spdyStream = this;
        // MONITORENTER : spdyStream
        this.source.finished = true;
        boolean bl = this.isOpen();
        this.notifyAll();
        // MONITOREXIT : spdyStream
        if (bl) return;
        this.connection.removeStream(this.id);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    void receiveHeaders(List<Header> list, HeadersMode headersMode) {
        ErrorCode errorCode;
        if (!$assertionsDisabled && Thread.holdsLock((Object)this)) {
            throw new AssertionError();
        }
        boolean bl = true;
        SpdyStream spdyStream = this;
        // MONITORENTER : spdyStream
        if (this.responseHeaders == null) {
            if (headersMode.failIfHeadersAbsent()) {
                errorCode = ErrorCode.PROTOCOL_ERROR;
            } else {
                this.responseHeaders = list;
                bl = this.isOpen();
                this.notifyAll();
                errorCode = null;
            }
        } else if (headersMode.failIfHeadersPresent()) {
            errorCode = ErrorCode.STREAM_IN_USE;
        } else {
            ArrayList arrayList = new ArrayList();
            arrayList.addAll(this.responseHeaders);
            arrayList.addAll(list);
            this.responseHeaders = arrayList;
            errorCode = null;
        }
        // MONITOREXIT : spdyStream
        if (errorCode != null) {
            this.closeLater(errorCode);
            return;
        }
        if (bl) return;
        this.connection.removeStream(this.id);
    }

    void receiveRstStream(ErrorCode errorCode) {
        SpdyStream spdyStream = this;
        synchronized (spdyStream) {
            if (this.errorCode == null) {
                this.errorCode = errorCode;
                this.notifyAll();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void reply(List<Header> list, boolean bl) {
        boolean bl2 = true;
        if (!$assertionsDisabled && Thread.holdsLock((Object)this)) {
            throw new AssertionError();
        }
        SpdyStream spdyStream = this;
        synchronized (spdyStream) {
            if (list == null) {
                throw new NullPointerException("responseHeaders == null");
            }
            if (this.responseHeaders != null) {
                throw new IllegalStateException("reply already sent");
            }
            this.responseHeaders = list;
            if (!bl) {
                this.sink.finished = true;
            } else {
                bl2 = false;
            }
        }
        this.connection.writeSynReply(this.id, bl2, list);
        if (bl2) {
            this.connection.flush();
        }
    }

    public Timeout writeTimeout() {
        return this.writeTimeout;
    }

    final class SpdyDataSink
    implements Sink {
        static final /* synthetic */ boolean $assertionsDisabled = false;
        private static final long EMIT_BUFFER_SIZE = 16384L;
        private boolean closed;
        private boolean finished;
        private final Buffer sendBuffer = new Buffer();

        /*
         * Enabled aggressive block sorting
         */
        static {
            boolean bl = !SpdyStream.class.desiredAssertionStatus();
            $assertionsDisabled = bl;
        }

        SpdyDataSink() {
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        private void emitDataFrame(boolean bl) {
            long l2;
            SpdyStream spdyStream;
            SpdyStream spdyStream2 = spdyStream = SpdyStream.this;
            synchronized (spdyStream2) {
                SpdyStream.this.writeTimeout.enter();
                try {
                    while (SpdyStream.this.bytesLeftInWriteWindow <= 0L && !this.finished && !this.closed && SpdyStream.this.errorCode == null) {
                        SpdyStream.this.waitForIo();
                    }
                }
                finally {
                    SpdyStream.this.writeTimeout.exitAndThrowIfTimedOut();
                }
                SpdyStream.this.checkOutNotClosed();
                l2 = Math.min((long)SpdyStream.this.bytesLeftInWriteWindow, (long)this.sendBuffer.size());
                SpdyStream spdyStream3 = SpdyStream.this;
                spdyStream3.bytesLeftInWriteWindow -= l2;
            }
            SpdyStream.this.writeTimeout.enter();
            try {
                SpdyConnection spdyConnection = SpdyStream.this.connection;
                int n2 = SpdyStream.this.id;
                boolean bl2 = bl && l2 == this.sendBuffer.size();
                spdyConnection.writeData(n2, bl2, this.sendBuffer, l2);
                return;
            }
            finally {
                SpdyStream.this.writeTimeout.exitAndThrowIfTimedOut();
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public void close() {
            SpdyStream spdyStream;
            SpdyStream spdyStream2;
            if (!$assertionsDisabled && Thread.holdsLock((Object)SpdyStream.this)) {
                throw new AssertionError();
            }
            SpdyStream spdyStream3 = spdyStream2 = SpdyStream.this;
            // MONITORENTER : spdyStream3
            if (this.closed) {
                // MONITOREXIT : spdyStream3
                return;
            }
            // MONITOREXIT : spdyStream3
            if (!SpdyStream.this.sink.finished) {
                if (this.sendBuffer.size() > 0L) {
                    while (this.sendBuffer.size() > 0L) {
                        this.emitDataFrame(true);
                    }
                } else {
                    SpdyStream.this.connection.writeData(SpdyStream.this.id, true, null, 0L);
                }
            }
            SpdyStream spdyStream4 = spdyStream = SpdyStream.this;
            // MONITORENTER : spdyStream4
            this.closed = true;
            // MONITOREXIT : spdyStream4
            SpdyStream.this.connection.flush();
            SpdyStream.this.cancelStreamIfNecessary();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void flush() {
            SpdyStream spdyStream;
            if (!$assertionsDisabled && Thread.holdsLock((Object)SpdyStream.this)) {
                throw new AssertionError();
            }
            SpdyStream spdyStream2 = spdyStream = SpdyStream.this;
            synchronized (spdyStream2) {
                SpdyStream.this.checkOutNotClosed();
            }
            while (this.sendBuffer.size() > 0L) {
                this.emitDataFrame(false);
                SpdyStream.this.connection.flush();
            }
            return;
        }

        @Override
        public Timeout timeout() {
            return SpdyStream.this.writeTimeout;
        }

        @Override
        public void write(Buffer buffer, long l2) {
            if (!$assertionsDisabled && Thread.holdsLock((Object)SpdyStream.this)) {
                throw new AssertionError();
            }
            this.sendBuffer.write(buffer, l2);
            while (this.sendBuffer.size() >= 16384L) {
                this.emitDataFrame(false);
            }
        }
    }

    private final class SpdyDataSource
    implements Source {
        static final /* synthetic */ boolean $assertionsDisabled;
        private boolean closed;
        private boolean finished;
        private final long maxByteCount;
        private final Buffer readBuffer = new Buffer();
        private final Buffer receiveBuffer = new Buffer();

        /*
         * Enabled aggressive block sorting
         */
        static {
            boolean bl = !SpdyStream.class.desiredAssertionStatus();
            $assertionsDisabled = bl;
        }

        private SpdyDataSource(long l2) {
            this.maxByteCount = l2;
        }

        private void checkNotClosed() {
            if (this.closed) {
                throw new IOException("stream closed");
            }
            if (SpdyStream.this.errorCode != null) {
                throw new IOException("stream was reset: " + (Object)((Object)SpdyStream.this.errorCode));
            }
        }

        private void waitUntilReadable() {
            SpdyStream.this.readTimeout.enter();
            try {
                while (this.readBuffer.size() == 0L && !this.finished && !this.closed && SpdyStream.this.errorCode == null) {
                    SpdyStream.this.waitForIo();
                }
            }
            finally {
                SpdyStream.this.readTimeout.exitAndThrowIfTimedOut();
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void close() {
            SpdyStream spdyStream;
            SpdyStream spdyStream2 = spdyStream = SpdyStream.this;
            synchronized (spdyStream2) {
                this.closed = true;
                this.readBuffer.clear();
                SpdyStream.this.notifyAll();
            }
            SpdyStream.this.cancelStreamIfNecessary();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public long read(Buffer buffer, long l2) {
            long l3;
            SpdyStream spdyStream;
            SpdyConnection spdyConnection;
            if (l2 < 0L) {
                throw new IllegalArgumentException("byteCount < 0: " + l2);
            }
            SpdyStream spdyStream2 = spdyStream = SpdyStream.this;
            synchronized (spdyStream2) {
                this.waitUntilReadable();
                this.checkNotClosed();
                if (this.readBuffer.size() == 0L) {
                    return -1L;
                }
                l3 = this.readBuffer.read(buffer, Math.min((long)l2, (long)this.readBuffer.size()));
                SpdyStream spdyStream3 = SpdyStream.this;
                spdyStream3.unacknowledgedBytesRead = l3 + spdyStream3.unacknowledgedBytesRead;
                if (SpdyStream.this.unacknowledgedBytesRead >= (long)(SpdyStream.access$500((SpdyStream)SpdyStream.this).okHttpSettings.getInitialWindowSize(65536) / 2)) {
                    SpdyStream.this.connection.writeWindowUpdateLater(SpdyStream.this.id, SpdyStream.this.unacknowledgedBytesRead);
                    SpdyStream.this.unacknowledgedBytesRead = 0L;
                }
            }
            SpdyConnection spdyConnection2 = spdyConnection = SpdyStream.this.connection;
            synchronized (spdyConnection2) {
                SpdyConnection spdyConnection3 = SpdyStream.this.connection;
                spdyConnection3.unacknowledgedBytesRead = l3 + spdyConnection3.unacknowledgedBytesRead;
                if (SpdyStream.access$500((SpdyStream)SpdyStream.this).unacknowledgedBytesRead >= (long)(SpdyStream.access$500((SpdyStream)SpdyStream.this).okHttpSettings.getInitialWindowSize(65536) / 2)) {
                    SpdyStream.this.connection.writeWindowUpdateLater(0, SpdyStream.access$500((SpdyStream)SpdyStream.this).unacknowledgedBytesRead);
                    SpdyStream.access$500((SpdyStream)SpdyStream.this).unacknowledgedBytesRead = 0L;
                }
                return l3;
            }
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        void receive(BufferedSource var1_1, long var2_2) {
            if (SpdyDataSource.$assertionsDisabled || !Thread.holdsLock((Object)SpdyStream.this)) ** GOTO lbl11
            throw new AssertionError();
lbl-1000: // 1 sources:
            {
                var2_2 -= var8_7;
                var15_10 = var10_8 = SpdyStream.this;
                // MONITORENTER : var15_10
                var12_9 = this.readBuffer.size() == 0L;
                this.readBuffer.writeAll(this.receiveBuffer);
                if (var12_9) {
                    SpdyStream.this.notifyAll();
                }
                // MONITOREXIT : var15_10
lbl11: // 2 sources:
                if (var2_2 <= 0L) return;
                var16_3 = var4_4 = SpdyStream.this;
                // MONITORENTER : var16_3
                var6_5 = this.finished;
                var7_6 = var2_2 + this.readBuffer.size() > this.maxByteCount;
                // MONITOREXIT : var16_3
                if (var7_6) {
                    var1_1.skip(var2_2);
                    SpdyStream.this.closeLater(ErrorCode.FLOW_CONTROL_ERROR);
                    return;
                }
                if (!var6_5) continue;
                var1_1.skip(var2_2);
                return;
                ** while ((var8_7 = var1_1.read((Buffer)this.receiveBuffer, (long)var2_2)) != -1L)
            }
lbl25: // 1 sources:
            throw new EOFException();
        }

        @Override
        public Timeout timeout() {
            return SpdyStream.this.readTimeout;
        }
    }

    class SpdyTimeout
    extends AsyncTimeout {
        SpdyTimeout() {
        }

        public void exitAndThrowIfTimedOut() {
            if (this.exit()) {
                throw new InterruptedIOException("timeout");
            }
        }

        @Override
        protected void timedOut() {
            SpdyStream.this.closeLater(ErrorCode.CANCEL);
        }
    }

}

