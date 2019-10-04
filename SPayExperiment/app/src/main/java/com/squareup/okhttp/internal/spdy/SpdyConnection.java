/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.Closeable
 *  java.io.IOException
 *  java.io.InterruptedIOException
 *  java.lang.AssertionError
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.InterruptedException
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.Runnable
 *  java.lang.String
 *  java.lang.System
 *  java.lang.Thread
 *  java.lang.Throwable
 *  java.net.InetSocketAddress
 *  java.net.Socket
 *  java.net.SocketAddress
 *  java.util.Collection
 *  java.util.HashMap
 *  java.util.LinkedHashSet
 *  java.util.List
 *  java.util.Map
 *  java.util.Set
 *  java.util.concurrent.BlockingQueue
 *  java.util.concurrent.ExecutorService
 *  java.util.concurrent.Future
 *  java.util.concurrent.LinkedBlockingQueue
 *  java.util.concurrent.SynchronousQueue
 *  java.util.concurrent.ThreadFactory
 *  java.util.concurrent.ThreadPoolExecutor
 *  java.util.concurrent.TimeUnit
 *  java.util.logging.Level
 *  java.util.logging.Logger
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.NamedRunnable;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.FrameReader;
import com.squareup.okhttp.internal.spdy.FrameWriter;
import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.HeadersMode;
import com.squareup.okhttp.internal.spdy.Http2;
import com.squareup.okhttp.internal.spdy.IncomingStreamHandler;
import com.squareup.okhttp.internal.spdy.Ping;
import com.squareup.okhttp.internal.spdy.PushObserver;
import com.squareup.okhttp.internal.spdy.Settings;
import com.squareup.okhttp.internal.spdy.Spdy3;
import com.squareup.okhttp.internal.spdy.SpdyStream;
import com.squareup.okhttp.internal.spdy.Variant;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;

public final class SpdyConnection
implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled = false;
    private static final int OKHTTP_CLIENT_WINDOW_SIZE = 16777216;
    private static final ExecutorService executor;
    long bytesLeftInWriteWindow;
    final boolean client;
    private final Set<Integer> currentPushRequests;
    final FrameWriter frameWriter;
    private final IncomingStreamHandler handler;
    private final String hostName;
    private long idleStartTimeNs;
    private int lastGoodStreamId;
    private int nextPingId;
    private int nextStreamId;
    final Settings okHttpSettings;
    final Settings peerSettings;
    private Map<Integer, Ping> pings;
    final Protocol protocol;
    private final ExecutorService pushExecutor;
    private final PushObserver pushObserver;
    final Reader readerRunnable;
    private boolean receivedInitialPeerSettings;
    private boolean shutdown;
    final Socket socket;
    private final Map<Integer, SpdyStream> streams;
    long unacknowledgedBytesRead;
    final Variant variant;

    /*
     * Enabled aggressive block sorting
     */
    static {
        boolean bl = !SpdyConnection.class.desiredAssertionStatus();
        $assertionsDisabled = bl;
        executor = new ThreadPoolExecutor(0, Integer.MAX_VALUE, 60L, TimeUnit.SECONDS, (BlockingQueue)new SynchronousQueue(), Util.threadFactory("OkHttp SpdyConnection", true));
    }

    /*
     * Enabled aggressive block sorting
     */
    private SpdyConnection(Builder builder) {
        int n2 = 2;
        this.streams = new HashMap();
        this.idleStartTimeNs = System.nanoTime();
        this.unacknowledgedBytesRead = 0L;
        this.okHttpSettings = new Settings();
        this.peerSettings = new Settings();
        this.receivedInitialPeerSettings = false;
        this.currentPushRequests = new LinkedHashSet();
        this.protocol = builder.protocol;
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.handler = builder.handler;
        int n3 = builder.client ? 1 : n2;
        this.nextStreamId = n3;
        if (builder.client && this.protocol == Protocol.HTTP_2) {
            this.nextStreamId = 2 + this.nextStreamId;
        }
        if (builder.client) {
            n2 = 1;
        }
        this.nextPingId = n2;
        if (builder.client) {
            this.okHttpSettings.set(7, 0, 16777216);
        }
        this.hostName = builder.hostName;
        if (this.protocol == Protocol.HTTP_2) {
            this.variant = new Http2();
            TimeUnit timeUnit = TimeUnit.SECONDS;
            LinkedBlockingQueue linkedBlockingQueue = new LinkedBlockingQueue();
            Object[] arrobject = new Object[]{this.hostName};
            this.pushExecutor = new ThreadPoolExecutor(0, 1, 60L, timeUnit, (BlockingQueue)linkedBlockingQueue, Util.threadFactory(String.format((String)"OkHttp %s Push Observer", (Object[])arrobject), true));
            this.peerSettings.set(7, 0, 65535);
            this.peerSettings.set(5, 0, 16384);
        } else {
            if (this.protocol != Protocol.SPDY_3) {
                throw new AssertionError((Object)this.protocol);
            }
            this.variant = new Spdy3();
            this.pushExecutor = null;
        }
        this.bytesLeftInWriteWindow = this.peerSettings.getInitialWindowSize(65536);
        this.socket = builder.socket;
        this.frameWriter = this.variant.newWriter(Okio.buffer(Okio.sink(builder.socket)), this.client);
        this.readerRunnable = new Reader();
        new Thread((Runnable)this.readerRunnable).start();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private void close(ErrorCode errorCode, ErrorCode errorCode2) {
        IOException iOException;
        block20 : {
            SpdyStream[] arrspdyStream;
            IOException iOException2;
            Ping[] arrping;
            if (!$assertionsDisabled && Thread.holdsLock((Object)this)) {
                throw new AssertionError();
            }
            try {
                this.shutdown(errorCode);
                iOException2 = null;
            }
            catch (IOException iOException3) {
                iOException2 = iOException3;
            }
            SpdyConnection spdyConnection = this;
            // MONITORENTER : spdyConnection
            if (!this.streams.isEmpty()) {
                SpdyStream[] arrspdyStream2 = (SpdyStream[])this.streams.values().toArray((Object[])new SpdyStream[this.streams.size()]);
                this.streams.clear();
                this.setIdle(false);
                arrspdyStream = arrspdyStream2;
            } else {
                arrspdyStream = null;
            }
            if (this.pings != null) {
                Ping[] arrping2 = (Ping[])this.pings.values().toArray((Object[])new Ping[this.pings.size()]);
                this.pings = null;
                arrping = arrping2;
            } else {
                arrping = null;
            }
            // MONITOREXIT : spdyConnection
            if (arrspdyStream != null) {
                int n2 = arrspdyStream.length;
                IOException iOException4 = iOException2;
                for (int i2 = 0; i2 < n2; ++i2) {
                    SpdyStream spdyStream = arrspdyStream[i2];
                    try {
                        spdyStream.close(errorCode2);
                        continue;
                    }
                    catch (IOException iOException5) {
                        if (iOException4 == null) continue;
                        iOException4 = iOException5;
                    }
                }
                iOException2 = iOException4;
            }
            if (arrping != null) {
                int n3 = arrping.length;
                for (int i3 = 0; i3 < n3; ++i3) {
                    arrping[i3].cancel();
                }
            }
            try {
                this.frameWriter.close();
                iOException = iOException2;
            }
            catch (IOException iOException6) {
                if (iOException2 == null) break block20;
                iOException = iOException2;
            }
        }
        try {
            this.socket.close();
        }
        catch (IOException iOException7) {}
        if (iOException == null) return;
        throw iOException;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     * Converted monitor instructions to comments
     * Lifted jumps to return sites
     */
    private SpdyStream newStream(int n2, List<Header> list, boolean bl, boolean bl2) {
        FrameWriter frameWriter;
        boolean bl3 = true;
        boolean bl4 = !bl ? bl3 : false;
        if (bl2) {
            bl3 = false;
        }
        FrameWriter frameWriter2 = frameWriter = this.frameWriter;
        // MONITORENTER : frameWriter2
        SpdyConnection spdyConnection = this;
        // MONITORENTER : spdyConnection
        if (this.shutdown) {
            throw new IOException("shutdown");
        }
        int n3 = this.nextStreamId;
        this.nextStreamId = 2 + this.nextStreamId;
        SpdyStream spdyStream = new SpdyStream(n3, this, bl4, bl3, list);
        if (spdyStream.isOpen()) {
            this.streams.put((Object)n3, (Object)spdyStream);
            this.setIdle(false);
        }
        // MONITOREXIT : spdyConnection
        if (n2 == 0) {
            this.frameWriter.synStream(bl4, bl3, n3, n2, list);
        } else {
            if (this.client) {
                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
            }
            this.frameWriter.pushPromise(n2, n3, list);
        }
        // MONITOREXIT : frameWriter2
        if (bl) return spdyStream;
        this.frameWriter.flush();
        return spdyStream;
    }

    private void pushDataLater(final int n2, BufferedSource bufferedSource, final int n3, final boolean bl) {
        final Buffer buffer = new Buffer();
        bufferedSource.require(n3);
        bufferedSource.read(buffer, (long)n3);
        if (buffer.size() != (long)n3) {
            throw new IOException(buffer.size() + " != " + n3);
        }
        ExecutorService executorService = this.pushExecutor;
        Object[] arrobject = new Object[]{this.hostName, n2};
        executorService.execute((Runnable)new NamedRunnable("OkHttp %s Push Data[%s]", arrobject){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            @Override
            public void execute() {
                try {
                    SpdyConnection spdyConnection;
                    boolean bl2 = SpdyConnection.this.pushObserver.onData(n2, buffer, n3, bl);
                    if (bl2) {
                        SpdyConnection.this.frameWriter.rstStream(n2, ErrorCode.CANCEL);
                    }
                    if (!bl2) {
                        if (!bl) return;
                    }
                    SpdyConnection spdyConnection2 = spdyConnection = SpdyConnection.this;
                    // MONITORENTER : spdyConnection2
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                SpdyConnection.this.currentPushRequests.remove((Object)n2);
                // MONITOREXIT : spdyConnection2
                return;
            }
        });
    }

    private void pushHeadersLater(final int n2, final List<Header> list, final boolean bl) {
        ExecutorService executorService = this.pushExecutor;
        Object[] arrobject = new Object[]{this.hostName, n2};
        executorService.execute((Runnable)new NamedRunnable("OkHttp %s Push Headers[%s]", arrobject){

            /*
             * Unable to fully structure code
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            @Override
            public void execute() {
                var1_1 = SpdyConnection.access$2500(SpdyConnection.this).onHeaders(n2, (List<Header>)list, bl);
                if (!var1_1) ** GOTO lbl5
                try {
                    SpdyConnection.this.frameWriter.rstStream(n2, ErrorCode.CANCEL);
lbl5: // 2 sources:
                    if (!var1_1) {
                        if (bl == false) return;
                    }
                    var6_3 = var3_2 = SpdyConnection.this;
                    // MONITORENTER : var6_3
                }
                catch (IOException var2_4) {
                    // empty catch block
                }
                SpdyConnection.access$2600(SpdyConnection.this).remove((Object)n2);
                // MONITOREXIT : var6_3
                return;
            }
        });
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void pushRequestLater(final int n2, final List<Header> list) {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            if (this.currentPushRequests.contains((Object)n2)) {
                this.writeSynResetLater(n2, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add((Object)n2);
        }
        ExecutorService executorService = this.pushExecutor;
        Object[] arrobject = new Object[]{this.hostName, n2};
        executorService.execute((Runnable)new NamedRunnable("OkHttp %s Push Request[%s]", arrobject){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             * Converted monitor instructions to comments
             * Lifted jumps to return sites
             */
            @Override
            public void execute() {
                if (!SpdyConnection.this.pushObserver.onRequest(n2, (List<Header>)list)) return;
                try {
                    SpdyConnection spdyConnection;
                    SpdyConnection.this.frameWriter.rstStream(n2, ErrorCode.CANCEL);
                    SpdyConnection spdyConnection2 = spdyConnection = SpdyConnection.this;
                    // MONITORENTER : spdyConnection2
                }
                catch (IOException iOException) {
                    // empty catch block
                }
                SpdyConnection.this.currentPushRequests.remove((Object)n2);
                // MONITOREXIT : spdyConnection2
                return;
            }
        });
    }

    private void pushResetLater(final int n2, final ErrorCode errorCode) {
        ExecutorService executorService = this.pushExecutor;
        Object[] arrobject = new Object[]{this.hostName, n2};
        executorService.execute((Runnable)new NamedRunnable("OkHttp %s Push Reset[%s]", arrobject){

            /*
             * Enabled aggressive block sorting
             * Enabled unnecessary exception pruning
             * Enabled aggressive exception aggregation
             */
            @Override
            public void execute() {
                SpdyConnection spdyConnection;
                SpdyConnection.this.pushObserver.onReset(n2, errorCode);
                SpdyConnection spdyConnection2 = spdyConnection = SpdyConnection.this;
                synchronized (spdyConnection2) {
                    SpdyConnection.this.currentPushRequests.remove((Object)n2);
                    return;
                }
            }
        });
    }

    private boolean pushedStream(int n2) {
        return this.protocol == Protocol.HTTP_2 && n2 != 0 && (n2 & 1) == 0;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private Ping removePing(int n2) {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            if (this.pings == null) return null;
            return (Ping)this.pings.remove((Object)n2);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void setIdle(boolean bl) {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            long l2 = bl ? System.nanoTime() : Long.MAX_VALUE;
            this.idleStartTimeNs = l2;
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private void writePing(boolean bl, int n2, int n3, Ping ping) {
        FrameWriter frameWriter;
        FrameWriter frameWriter2 = frameWriter = this.frameWriter;
        synchronized (frameWriter2) {
            if (ping != null) {
                ping.send();
            }
            this.frameWriter.ping(bl, n2, n3);
            return;
        }
    }

    private void writePingLater(final boolean bl, final int n2, final int n3, final Ping ping) {
        ExecutorService executorService = executor;
        Object[] arrobject = new Object[]{this.hostName, n2, n3};
        executorService.execute((Runnable)new NamedRunnable("OkHttp %s ping %08x%08x", arrobject){

            @Override
            public void execute() {
                try {
                    SpdyConnection.this.writePing(bl, n2, n3, ping);
                    return;
                }
                catch (IOException iOException) {
                    return;
                }
            }
        });
    }

    void addBytesToWriteWindow(long l2) {
        this.bytesLeftInWriteWindow = l2 + this.bytesLeftInWriteWindow;
        if (l2 > 0L) {
            this.notifyAll();
        }
    }

    public void close() {
        this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    public void flush() {
        this.frameWriter.flush();
    }

    public long getIdleStartTimeNs() {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            long l2 = this.idleStartTimeNs;
            return l2;
        }
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    SpdyStream getStream(int n2) {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            SpdyStream spdyStream = (SpdyStream)this.streams.get((Object)n2);
            return spdyStream;
        }
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public boolean isIdle() {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            long l2 = this.idleStartTimeNs;
            if (l2 == Long.MAX_VALUE) return false;
            return true;
        }
    }

    public SpdyStream newStream(List<Header> list, boolean bl, boolean bl2) {
        return this.newStream(0, list, bl, bl2);
    }

    public int openStreamCount() {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            int n2 = this.streams.size();
            return n2;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public Ping ping() {
        int n2;
        Ping ping = new Ping();
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            if (this.shutdown) {
                throw new IOException("shutdown");
            }
            n2 = this.nextPingId;
            this.nextPingId = 2 + this.nextPingId;
            if (this.pings == null) {
                this.pings = new HashMap();
            }
            this.pings.put((Object)n2, (Object)ping);
        }
        this.writePing(false, n2, 1330343787, ping);
        return ping;
    }

    public SpdyStream pushStream(int n2, List<Header> list, boolean bl) {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        }
        if (this.protocol != Protocol.HTTP_2) {
            throw new IllegalStateException("protocol != HTTP_2");
        }
        return this.newStream(n2, list, bl, false);
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    SpdyStream removeStream(int n2) {
        SpdyConnection spdyConnection = this;
        synchronized (spdyConnection) {
            SpdyStream spdyStream = (SpdyStream)this.streams.remove((Object)n2);
            if (spdyStream != null && this.streams.isEmpty()) {
                this.setIdle(true);
            }
            this.notifyAll();
            return spdyStream;
        }
    }

    public void sendConnectionPreface() {
        this.frameWriter.connectionPreface();
        this.frameWriter.settings(this.okHttpSettings);
        int n2 = this.okHttpSettings.getInitialWindowSize(65536);
        if (n2 != 65536) {
            this.frameWriter.windowUpdate(0, n2 - 65536);
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    public void shutdown(ErrorCode errorCode) {
        FrameWriter frameWriter;
        FrameWriter frameWriter2 = frameWriter = this.frameWriter;
        synchronized (frameWriter2) {
            int n2;
            SpdyConnection spdyConnection = this;
            synchronized (spdyConnection) {
                if (this.shutdown) {
                    return;
                }
                this.shutdown = true;
                n2 = this.lastGoodStreamId;
            }
            this.frameWriter.goAway(n2, errorCode, Util.EMPTY_BYTE_ARRAY);
            return;
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
    public void writeData(int var1_1, boolean var2_2, Buffer var3_3, long var4_4) {
        if (var4_4 != 0L) ** GOTO lbl10
        this.frameWriter.data(var2_2, var1_1, var3_3, 0);
        return;
        {
            var8_5 = Math.min((int)((int)Math.min((long)var4_4, (long)this.bytesLeftInWriteWindow)), (int)this.frameWriter.maxDataLength());
            this.bytesLeftInWriteWindow -= (long)var8_5;
            // MONITOREXIT : this
            var9_6 = this.frameWriter;
            var10_7 = var2_2 != false && (var4_4 -= (long)var8_5) == 0L;
            var9_6.data(var10_7, var1_1, var3_3, var8_5);
lbl10: // 2 sources:
            if (var4_4 <= 0L) return;
            var11_8 = this;
            // MONITORENTER : var11_8
            do {
                if (this.bytesLeftInWriteWindow > 0L) continue block6;
                if (!this.streams.containsKey((Object)var1_1)) {
                    throw new IOException("stream closed");
                }
                this.wait();
            } while (true);
        }
        catch (InterruptedException var7_9) {
            throw new InterruptedIOException();
        }
    }

    void writeSynReply(int n2, boolean bl, List<Header> list) {
        this.frameWriter.synReply(bl, n2, list);
    }

    void writeSynReset(int n2, ErrorCode errorCode) {
        this.frameWriter.rstStream(n2, errorCode);
    }

    void writeSynResetLater(final int n2, final ErrorCode errorCode) {
        ExecutorService executorService = executor;
        Object[] arrobject = new Object[]{this.hostName, n2};
        executorService.submit((Runnable)new NamedRunnable("OkHttp %s stream %d", arrobject){

            @Override
            public void execute() {
                try {
                    SpdyConnection.this.writeSynReset(n2, errorCode);
                    return;
                }
                catch (IOException iOException) {
                    return;
                }
            }
        });
    }

    void writeWindowUpdateLater(final int n2, final long l2) {
        ExecutorService executorService = executor;
        Object[] arrobject = new Object[]{this.hostName, n2};
        executorService.execute((Runnable)new NamedRunnable("OkHttp Window Update %s stream %d", arrobject){

            @Override
            public void execute() {
                try {
                    SpdyConnection.this.frameWriter.windowUpdate(n2, l2);
                    return;
                }
                catch (IOException iOException) {
                    return;
                }
            }
        });
    }

    public static class Builder {
        private boolean client;
        private IncomingStreamHandler handler = IncomingStreamHandler.REFUSE_INCOMING_STREAMS;
        private String hostName;
        private Protocol protocol = Protocol.SPDY_3;
        private PushObserver pushObserver = PushObserver.CANCEL;
        private Socket socket;

        public Builder(String string, boolean bl, Socket socket) {
            this.hostName = string;
            this.client = bl;
            this.socket = socket;
        }

        public Builder(boolean bl, Socket socket) {
            this(((InetSocketAddress)socket.getRemoteSocketAddress()).getHostName(), bl, socket);
        }

        public SpdyConnection build() {
            return new SpdyConnection(this);
        }

        public Builder handler(IncomingStreamHandler incomingStreamHandler) {
            this.handler = incomingStreamHandler;
            return this;
        }

        public Builder protocol(Protocol protocol) {
            this.protocol = protocol;
            return this;
        }

        public Builder pushObserver(PushObserver pushObserver) {
            this.pushObserver = pushObserver;
            return this;
        }
    }

    class Reader
    extends NamedRunnable
    implements FrameReader.Handler {
        FrameReader frameReader;

        private Reader() {
            Object[] arrobject = new Object[]{SpdyConnection.this.hostName};
            super("OkHttp %s", arrobject);
        }

        private void ackSettingsLater(final Settings settings) {
            ExecutorService executorService = executor;
            Object[] arrobject = new Object[]{SpdyConnection.this.hostName};
            executorService.execute((Runnable)new NamedRunnable("OkHttp %s ACK Settings", arrobject){

                @Override
                public void execute() {
                    try {
                        SpdyConnection.this.frameWriter.ackSettings(settings);
                        return;
                    }
                    catch (IOException iOException) {
                        return;
                    }
                }
            });
        }

        @Override
        public void ackSettings() {
        }

        @Override
        public void alternateService(int n2, String string, ByteString byteString, String string2, int n3, long l2) {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void data(boolean bl, int n2, BufferedSource bufferedSource, int n3) {
            if (SpdyConnection.this.pushedStream(n2)) {
                SpdyConnection.this.pushDataLater(n2, bufferedSource, n3, bl);
                return;
            } else {
                SpdyStream spdyStream = SpdyConnection.this.getStream(n2);
                if (spdyStream == null) {
                    SpdyConnection.this.writeSynResetLater(n2, ErrorCode.INVALID_STREAM);
                    bufferedSource.skip(n3);
                    return;
                }
                spdyStream.receiveData(bufferedSource, n3);
                if (!bl) return;
                {
                    spdyStream.receiveFin();
                    return;
                }
            }
        }

        /*
         * Loose catch block
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Lifted jumps to return sites
         */
        @Override
        protected void execute() {
            block14 : {
                ErrorCode errorCode = ErrorCode.INTERNAL_ERROR;
                ErrorCode errorCode2 = ErrorCode.INTERNAL_ERROR;
                this.frameReader = SpdyConnection.this.variant.newReader(Okio.buffer(Okio.source(SpdyConnection.this.socket)), SpdyConnection.this.client);
                if (!SpdyConnection.this.client) {
                    this.frameReader.readConnectionPreface();
                }
                while (this.frameReader.nextFrame(this)) {
                }
                errorCode = ErrorCode.NO_ERROR;
                ErrorCode errorCode3 = ErrorCode.CANCEL;
                SpdyConnection.this.close(errorCode, errorCode3);
                break block14;
                catch (IOException iOException) {
                    ErrorCode errorCode4;
                    try {
                        errorCode4 = ErrorCode.PROTOCOL_ERROR;
                    }
                    catch (Throwable throwable) {
                        Throwable throwable2;
                        errorCode4 = errorCode;
                        throwable2 = throwable;
                        try {
                            SpdyConnection.this.close(errorCode4, errorCode2);
                        }
                        catch (IOException iOException2) {}
                        Util.closeQuietly(this.frameReader);
                        throw throwable2;
                    }
                    ErrorCode errorCode5 = ErrorCode.PROTOCOL_ERROR;
                    {
                        catch (Throwable throwable) {}
                    }
                    try {
                        SpdyConnection.this.close(errorCode4, errorCode5);
                    }
                    catch (IOException iOException3) {}
                    Util.closeQuietly(this.frameReader);
                    return;
                }
                catch (IOException iOException) {}
            }
            Util.closeQuietly(this.frameReader);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void goAway(int n2, ErrorCode errorCode, ByteString byteString) {
            SpdyConnection spdyConnection;
            SpdyStream[] arrspdyStream;
            if (byteString.size() > 0) {
                // empty if block
            }
            SpdyConnection spdyConnection2 = spdyConnection = SpdyConnection.this;
            synchronized (spdyConnection2) {
                arrspdyStream = (SpdyStream[])SpdyConnection.this.streams.values().toArray((Object[])new SpdyStream[SpdyConnection.this.streams.size()]);
                SpdyConnection.this.shutdown = true;
            }
            int n3 = arrspdyStream.length;
            int n4 = 0;
            while (n4 < n3) {
                SpdyStream spdyStream = arrspdyStream[n4];
                if (spdyStream.getId() > n2 && spdyStream.isLocallyInitiated()) {
                    spdyStream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    SpdyConnection.this.removeStream(spdyStream.getId());
                }
                ++n4;
            }
            return;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public void headers(boolean bl, boolean bl2, int n2, int n3, List<Header> list, HeadersMode headersMode) {
            SpdyConnection spdyConnection;
            if (SpdyConnection.this.pushedStream(n2)) {
                SpdyConnection.this.pushHeadersLater(n2, (List<Header>)list, bl2);
                return;
            }
            SpdyConnection spdyConnection2 = spdyConnection = SpdyConnection.this;
            // MONITORENTER : spdyConnection2
            if (SpdyConnection.this.shutdown) {
                // MONITOREXIT : spdyConnection2
                return;
            }
            SpdyStream spdyStream = SpdyConnection.this.getStream(n2);
            if (spdyStream == null) {
                if (headersMode.failIfStreamAbsent()) {
                    SpdyConnection.this.writeSynResetLater(n2, ErrorCode.INVALID_STREAM);
                    // MONITOREXIT : spdyConnection2
                    return;
                }
                if (n2 <= SpdyConnection.this.lastGoodStreamId) {
                    // MONITOREXIT : spdyConnection2
                    return;
                }
                if (n2 % 2 == SpdyConnection.this.nextStreamId % 2) {
                    // MONITOREXIT : spdyConnection2
                    return;
                }
                final SpdyStream spdyStream2 = new SpdyStream(n2, SpdyConnection.this, bl, bl2, list);
                SpdyConnection.this.lastGoodStreamId = n2;
                SpdyConnection.this.streams.put((Object)n2, (Object)spdyStream2);
                ExecutorService executorService = executor;
                Object[] arrobject = new Object[]{SpdyConnection.this.hostName, n2};
                executorService.execute((Runnable)new NamedRunnable("OkHttp %s stream %d", arrobject){

                    @Override
                    public void execute() {
                        try {
                            SpdyConnection.this.handler.receive(spdyStream2);
                            return;
                        }
                        catch (IOException iOException) {
                            Internal.logger.log(Level.INFO, "StreamHandler failure for " + SpdyConnection.this.hostName, (Throwable)iOException);
                            try {
                                spdyStream2.close(ErrorCode.PROTOCOL_ERROR);
                                return;
                            }
                            catch (IOException iOException2) {
                                return;
                            }
                        }
                    }
                });
                // MONITOREXIT : spdyConnection2
                return;
            }
            // MONITOREXIT : spdyConnection2
            if (headersMode.failIfStreamPresent()) {
                spdyStream.closeLater(ErrorCode.PROTOCOL_ERROR);
                SpdyConnection.this.removeStream(n2);
                return;
            }
            spdyStream.receiveHeaders(list, headersMode);
            if (!bl2) return;
            spdyStream.receiveFin();
        }

        @Override
        public void ping(boolean bl, int n2, int n3) {
            if (bl) {
                Ping ping = SpdyConnection.this.removePing(n2);
                if (ping != null) {
                    ping.receive();
                }
                return;
            }
            SpdyConnection.this.writePingLater(true, n2, n3, null);
        }

        @Override
        public void priority(int n2, int n3, int n4, boolean bl) {
        }

        @Override
        public void pushPromise(int n2, int n3, List<Header> list) {
            SpdyConnection.this.pushRequestLater(n3, (List<Header>)list);
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void rstStream(int n2, ErrorCode errorCode) {
            if (SpdyConnection.this.pushedStream(n2)) {
                SpdyConnection.this.pushResetLater(n2, errorCode);
                return;
            } else {
                SpdyStream spdyStream = SpdyConnection.this.removeStream(n2);
                if (spdyStream == null) return;
                {
                    spdyStream.receiveRstStream(errorCode);
                    return;
                }
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void settings(boolean bl, Settings settings) {
            SpdyStream[] arrspdyStream;
            long l2;
            SpdyConnection spdyConnection;
            SpdyConnection spdyConnection2 = spdyConnection = SpdyConnection.this;
            synchronized (spdyConnection2) {
                int n2;
                int n3 = SpdyConnection.this.peerSettings.getInitialWindowSize(65536);
                if (bl) {
                    SpdyConnection.this.peerSettings.clear();
                }
                SpdyConnection.this.peerSettings.merge(settings);
                if (SpdyConnection.this.getProtocol() == Protocol.HTTP_2) {
                    this.ackSettingsLater(settings);
                }
                if ((n2 = SpdyConnection.this.peerSettings.getInitialWindowSize(65536)) == -1) return;
                if (n2 == n3) return;
                l2 = n2 - n3;
                if (!SpdyConnection.this.receivedInitialPeerSettings) {
                    SpdyConnection.this.addBytesToWriteWindow(l2);
                    SpdyConnection.this.receivedInitialPeerSettings = true;
                }
                if (SpdyConnection.this.streams.isEmpty()) return;
                arrspdyStream = (SpdyStream[])SpdyConnection.this.streams.values().toArray((Object[])new SpdyStream[SpdyConnection.this.streams.size()]);
            }
            if (arrspdyStream == null) return;
            if (l2 == 0L) return;
            int n4 = arrspdyStream.length;
            int n5 = 0;
            while (n5 < n4) {
                void var12_11;
                void var16_12 = var12_11 = arrspdyStream[n5];
                synchronized (var16_12) {
                    var12_11.addBytesToWriteWindow(l2);
                }
                ++n5;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void windowUpdate(int n2, long l2) {
            if (n2 == 0) {
                SpdyConnection spdyConnection;
                SpdyConnection spdyConnection2 = spdyConnection = SpdyConnection.this;
                synchronized (spdyConnection2) {
                    SpdyConnection spdyConnection3 = SpdyConnection.this;
                    spdyConnection3.bytesLeftInWriteWindow = l2 + spdyConnection3.bytesLeftInWriteWindow;
                    SpdyConnection.this.notifyAll();
                    return;
                }
            }
            SpdyStream spdyStream = SpdyConnection.this.getStream(n2);
            if (spdyStream != null) {
                SpdyStream spdyStream2 = spdyStream;
                synchronized (spdyStream2) {
                    spdyStream.addBytesToWriteWindow(l2);
                    return;
                }
            }
        }

    }

}

