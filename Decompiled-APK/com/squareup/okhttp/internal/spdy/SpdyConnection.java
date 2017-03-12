package com.squareup.okhttp.internal.spdy;

import com.americanexpress.mobilepayments.hceclient.utils.common.HCEClientConstants;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.NamedRunnable;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.spdy.FrameReader.Handler;
import java.io.Closeable;
import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import okio.Buffer;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;

public final class SpdyConnection implements Closeable {
    static final /* synthetic */ boolean $assertionsDisabled;
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

    /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.1 */
    class C06571 extends NamedRunnable {
        final /* synthetic */ ErrorCode val$errorCode;
        final /* synthetic */ int val$streamId;

        C06571(String str, Object[] objArr, int i, ErrorCode errorCode) {
            this.val$streamId = i;
            this.val$errorCode = errorCode;
            super(str, objArr);
        }

        public void execute() {
            try {
                SpdyConnection.this.writeSynReset(this.val$streamId, this.val$errorCode);
            } catch (IOException e) {
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.2 */
    class C06582 extends NamedRunnable {
        final /* synthetic */ int val$streamId;
        final /* synthetic */ long val$unacknowledgedBytesRead;

        C06582(String str, Object[] objArr, int i, long j) {
            this.val$streamId = i;
            this.val$unacknowledgedBytesRead = j;
            super(str, objArr);
        }

        public void execute() {
            try {
                SpdyConnection.this.frameWriter.windowUpdate(this.val$streamId, this.val$unacknowledgedBytesRead);
            } catch (IOException e) {
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.3 */
    class C06593 extends NamedRunnable {
        final /* synthetic */ int val$payload1;
        final /* synthetic */ int val$payload2;
        final /* synthetic */ Ping val$ping;
        final /* synthetic */ boolean val$reply;

        C06593(String str, Object[] objArr, boolean z, int i, int i2, Ping ping) {
            this.val$reply = z;
            this.val$payload1 = i;
            this.val$payload2 = i2;
            this.val$ping = ping;
            super(str, objArr);
        }

        public void execute() {
            try {
                SpdyConnection.this.writePing(this.val$reply, this.val$payload1, this.val$payload2, this.val$ping);
            } catch (IOException e) {
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.4 */
    class C06604 extends NamedRunnable {
        final /* synthetic */ List val$requestHeaders;
        final /* synthetic */ int val$streamId;

        C06604(String str, Object[] objArr, int i, List list) {
            this.val$streamId = i;
            this.val$requestHeaders = list;
            super(str, objArr);
        }

        public void execute() {
            if (SpdyConnection.this.pushObserver.onRequest(this.val$streamId, this.val$requestHeaders)) {
                try {
                    SpdyConnection.this.frameWriter.rstStream(this.val$streamId, ErrorCode.CANCEL);
                    synchronized (SpdyConnection.this) {
                        SpdyConnection.this.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
                    }
                } catch (IOException e) {
                }
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.5 */
    class C06615 extends NamedRunnable {
        final /* synthetic */ boolean val$inFinished;
        final /* synthetic */ List val$requestHeaders;
        final /* synthetic */ int val$streamId;

        C06615(String str, Object[] objArr, int i, List list, boolean z) {
            this.val$streamId = i;
            this.val$requestHeaders = list;
            this.val$inFinished = z;
            super(str, objArr);
        }

        public void execute() {
            boolean onHeaders = SpdyConnection.this.pushObserver.onHeaders(this.val$streamId, this.val$requestHeaders, this.val$inFinished);
            if (onHeaders) {
                try {
                    SpdyConnection.this.frameWriter.rstStream(this.val$streamId, ErrorCode.CANCEL);
                } catch (IOException e) {
                    return;
                }
            }
            if (onHeaders || this.val$inFinished) {
                synchronized (SpdyConnection.this) {
                    SpdyConnection.this.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
                }
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.6 */
    class C06626 extends NamedRunnable {
        final /* synthetic */ Buffer val$buffer;
        final /* synthetic */ int val$byteCount;
        final /* synthetic */ boolean val$inFinished;
        final /* synthetic */ int val$streamId;

        C06626(String str, Object[] objArr, int i, Buffer buffer, int i2, boolean z) {
            this.val$streamId = i;
            this.val$buffer = buffer;
            this.val$byteCount = i2;
            this.val$inFinished = z;
            super(str, objArr);
        }

        public void execute() {
            try {
                boolean onData = SpdyConnection.this.pushObserver.onData(this.val$streamId, this.val$buffer, this.val$byteCount, this.val$inFinished);
                if (onData) {
                    SpdyConnection.this.frameWriter.rstStream(this.val$streamId, ErrorCode.CANCEL);
                }
                if (onData || this.val$inFinished) {
                    synchronized (SpdyConnection.this) {
                        SpdyConnection.this.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
                    }
                }
            } catch (IOException e) {
            }
        }
    }

    /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.7 */
    class C06637 extends NamedRunnable {
        final /* synthetic */ ErrorCode val$errorCode;
        final /* synthetic */ int val$streamId;

        C06637(String str, Object[] objArr, int i, ErrorCode errorCode) {
            this.val$streamId = i;
            this.val$errorCode = errorCode;
            super(str, objArr);
        }

        public void execute() {
            SpdyConnection.this.pushObserver.onReset(this.val$streamId, this.val$errorCode);
            synchronized (SpdyConnection.this) {
                SpdyConnection.this.currentPushRequests.remove(Integer.valueOf(this.val$streamId));
            }
        }
    }

    public static class Builder {
        private boolean client;
        private IncomingStreamHandler handler;
        private String hostName;
        private Protocol protocol;
        private PushObserver pushObserver;
        private Socket socket;

        public Builder(boolean z, Socket socket) {
            this(((InetSocketAddress) socket.getRemoteSocketAddress()).getHostName(), z, socket);
        }

        public Builder(String str, boolean z, Socket socket) {
            this.handler = IncomingStreamHandler.REFUSE_INCOMING_STREAMS;
            this.protocol = Protocol.SPDY_3;
            this.pushObserver = PushObserver.CANCEL;
            this.hostName = str;
            this.client = z;
            this.socket = socket;
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

        public SpdyConnection build() {
            return new SpdyConnection();
        }
    }

    class Reader extends NamedRunnable implements Handler {
        FrameReader frameReader;

        /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.Reader.1 */
        class C06641 extends NamedRunnable {
            final /* synthetic */ SpdyStream val$newStream;

            C06641(String str, Object[] objArr, SpdyStream spdyStream) {
                this.val$newStream = spdyStream;
                super(str, objArr);
            }

            public void execute() {
                try {
                    SpdyConnection.this.handler.receive(this.val$newStream);
                } catch (Throwable e) {
                    Internal.logger.log(Level.INFO, "StreamHandler failure for " + SpdyConnection.this.hostName, e);
                    try {
                        this.val$newStream.close(ErrorCode.PROTOCOL_ERROR);
                    } catch (IOException e2) {
                    }
                }
            }
        }

        /* renamed from: com.squareup.okhttp.internal.spdy.SpdyConnection.Reader.2 */
        class C06652 extends NamedRunnable {
            final /* synthetic */ Settings val$peerSettings;

            C06652(String str, Object[] objArr, Settings settings) {
                this.val$peerSettings = settings;
                super(str, objArr);
            }

            public void execute() {
                try {
                    SpdyConnection.this.frameWriter.ackSettings(this.val$peerSettings);
                } catch (IOException e) {
                }
            }
        }

        private Reader() {
            super("OkHttp %s", r5.hostName);
        }

        protected void execute() {
            ErrorCode errorCode;
            Throwable th;
            ErrorCode errorCode2 = ErrorCode.INTERNAL_ERROR;
            ErrorCode errorCode3 = ErrorCode.INTERNAL_ERROR;
            try {
                this.frameReader = SpdyConnection.this.variant.newReader(Okio.buffer(Okio.source(SpdyConnection.this.socket)), SpdyConnection.this.client);
                if (!SpdyConnection.this.client) {
                    this.frameReader.readConnectionPreface();
                }
                do {
                } while (this.frameReader.nextFrame(this));
                try {
                    SpdyConnection.this.close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
                } catch (IOException e) {
                }
                Util.closeQuietly(this.frameReader);
            } catch (IOException e2) {
                errorCode = ErrorCode.PROTOCOL_ERROR;
                try {
                    SpdyConnection.this.close(errorCode, ErrorCode.PROTOCOL_ERROR);
                } catch (IOException e3) {
                }
                Util.closeQuietly(this.frameReader);
            } catch (Throwable th2) {
                th = th2;
                SpdyConnection.this.close(errorCode, errorCode3);
                Util.closeQuietly(this.frameReader);
                throw th;
            }
        }

        public void data(boolean z, int i, BufferedSource bufferedSource, int i2) {
            if (SpdyConnection.this.pushedStream(i)) {
                SpdyConnection.this.pushDataLater(i, bufferedSource, i2, z);
                return;
            }
            SpdyStream stream = SpdyConnection.this.getStream(i);
            if (stream == null) {
                SpdyConnection.this.writeSynResetLater(i, ErrorCode.INVALID_STREAM);
                bufferedSource.skip((long) i2);
                return;
            }
            stream.receiveData(bufferedSource, i2);
            if (z) {
                stream.receiveFin();
            }
        }

        public void headers(boolean z, boolean z2, int i, int i2, List<Header> list, HeadersMode headersMode) {
            if (SpdyConnection.this.pushedStream(i)) {
                SpdyConnection.this.pushHeadersLater(i, list, z2);
                return;
            }
            synchronized (SpdyConnection.this) {
                if (SpdyConnection.this.shutdown) {
                    return;
                }
                SpdyStream stream = SpdyConnection.this.getStream(i);
                if (stream != null) {
                    if (headersMode.failIfStreamPresent()) {
                        stream.closeLater(ErrorCode.PROTOCOL_ERROR);
                        SpdyConnection.this.removeStream(i);
                        return;
                    }
                    stream.receiveHeaders(list, headersMode);
                    if (z2) {
                        stream.receiveFin();
                    }
                } else if (headersMode.failIfStreamAbsent()) {
                    SpdyConnection.this.writeSynResetLater(i, ErrorCode.INVALID_STREAM);
                } else if (i <= SpdyConnection.this.lastGoodStreamId) {
                } else if (i % 2 == SpdyConnection.this.nextStreamId % 2) {
                } else {
                    stream = new SpdyStream(i, SpdyConnection.this, z, z2, list);
                    SpdyConnection.this.lastGoodStreamId = i;
                    SpdyConnection.this.streams.put(Integer.valueOf(i), stream);
                    SpdyConnection.executor.execute(new C06641("OkHttp %s stream %d", new Object[]{SpdyConnection.this.hostName, Integer.valueOf(i)}, stream));
                }
            }
        }

        public void rstStream(int i, ErrorCode errorCode) {
            if (SpdyConnection.this.pushedStream(i)) {
                SpdyConnection.this.pushResetLater(i, errorCode);
                return;
            }
            SpdyStream removeStream = SpdyConnection.this.removeStream(i);
            if (removeStream != null) {
                removeStream.receiveRstStream(errorCode);
            }
        }

        public void settings(boolean z, Settings settings) {
            SpdyStream[] spdyStreamArr;
            synchronized (SpdyConnection.this) {
                long j;
                int initialWindowSize = SpdyConnection.this.peerSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized);
                if (z) {
                    SpdyConnection.this.peerSettings.clear();
                }
                SpdyConnection.this.peerSettings.merge(settings);
                if (SpdyConnection.this.getProtocol() == Protocol.HTTP_2) {
                    ackSettingsLater(settings);
                }
                int initialWindowSize2 = SpdyConnection.this.peerSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized);
                if (initialWindowSize2 == -1 || initialWindowSize2 == initialWindowSize) {
                    spdyStreamArr = null;
                    j = 0;
                } else {
                    j = (long) (initialWindowSize2 - initialWindowSize);
                    if (!SpdyConnection.this.receivedInitialPeerSettings) {
                        SpdyConnection.this.addBytesToWriteWindow(j);
                        SpdyConnection.this.receivedInitialPeerSettings = true;
                    }
                    spdyStreamArr = !SpdyConnection.this.streams.isEmpty() ? (SpdyStream[]) SpdyConnection.this.streams.values().toArray(new SpdyStream[SpdyConnection.this.streams.size()]) : null;
                }
            }
            if (spdyStreamArr != null && j != 0) {
                for (SpdyStream spdyStream : spdyStreamArr) {
                    synchronized (spdyStream) {
                        spdyStream.addBytesToWriteWindow(j);
                    }
                }
            }
        }

        private void ackSettingsLater(Settings settings) {
            SpdyConnection.executor.execute(new C06652("OkHttp %s ACK Settings", new Object[]{SpdyConnection.this.hostName}, settings));
        }

        public void ackSettings() {
        }

        public void ping(boolean z, int i, int i2) {
            if (z) {
                Ping access$2200 = SpdyConnection.this.removePing(i);
                if (access$2200 != null) {
                    access$2200.receive();
                    return;
                }
                return;
            }
            SpdyConnection.this.writePingLater(true, i, i2, null);
        }

        public void goAway(int i, ErrorCode errorCode, ByteString byteString) {
            if (byteString.size() > 0) {
            }
            synchronized (SpdyConnection.this) {
                SpdyStream[] spdyStreamArr = (SpdyStream[]) SpdyConnection.this.streams.values().toArray(new SpdyStream[SpdyConnection.this.streams.size()]);
                SpdyConnection.this.shutdown = true;
            }
            for (SpdyStream spdyStream : spdyStreamArr) {
                if (spdyStream.getId() > i && spdyStream.isLocallyInitiated()) {
                    spdyStream.receiveRstStream(ErrorCode.REFUSED_STREAM);
                    SpdyConnection.this.removeStream(spdyStream.getId());
                }
            }
        }

        public void windowUpdate(int i, long j) {
            if (i == 0) {
                synchronized (SpdyConnection.this) {
                    SpdyConnection spdyConnection = SpdyConnection.this;
                    spdyConnection.bytesLeftInWriteWindow += j;
                    SpdyConnection.this.notifyAll();
                }
                return;
            }
            SpdyStream stream = SpdyConnection.this.getStream(i);
            if (stream != null) {
                synchronized (stream) {
                    stream.addBytesToWriteWindow(j);
                }
            }
        }

        public void priority(int i, int i2, int i3, boolean z) {
        }

        public void pushPromise(int i, int i2, List<Header> list) {
            SpdyConnection.this.pushRequestLater(i2, list);
        }

        public void alternateService(int i, String str, ByteString byteString, String str2, int i2, long j) {
        }
    }

    static {
        boolean z;
        if (SpdyConnection.class.desiredAssertionStatus()) {
            z = $assertionsDisabled;
        } else {
            z = true;
        }
        $assertionsDisabled = z;
        executor = new ThreadPoolExecutor(0, CNCCCommands.CMD_CNCC_CMD_UNKNOWN, 60, TimeUnit.SECONDS, new SynchronousQueue(), Util.threadFactory("OkHttp SpdyConnection", true));
    }

    private SpdyConnection(Builder builder) {
        int i = 2;
        this.streams = new HashMap();
        this.idleStartTimeNs = System.nanoTime();
        this.unacknowledgedBytesRead = 0;
        this.okHttpSettings = new Settings();
        this.peerSettings = new Settings();
        this.receivedInitialPeerSettings = $assertionsDisabled;
        this.currentPushRequests = new LinkedHashSet();
        this.protocol = builder.protocol;
        this.pushObserver = builder.pushObserver;
        this.client = builder.client;
        this.handler = builder.handler;
        this.nextStreamId = builder.client ? 1 : 2;
        if (builder.client && this.protocol == Protocol.HTTP_2) {
            this.nextStreamId += 2;
        }
        if (builder.client) {
            i = 1;
        }
        this.nextPingId = i;
        if (builder.client) {
            this.okHttpSettings.set(7, 0, OKHTTP_CLIENT_WINDOW_SIZE);
        }
        this.hostName = builder.hostName;
        if (this.protocol == Protocol.HTTP_2) {
            this.variant = new Http2();
            this.pushExecutor = new ThreadPoolExecutor(0, 1, 60, TimeUnit.SECONDS, new LinkedBlockingQueue(), Util.threadFactory(String.format("OkHttp %s Push Observer", new Object[]{this.hostName}), true));
            this.peerSettings.set(7, 0, HCEClientConstants.HIGHEST_ATC_DEC_VALUE);
            this.peerSettings.set(5, 0, PKIFailureInfo.badPOP);
        } else if (this.protocol == Protocol.SPDY_3) {
            this.variant = new Spdy3();
            this.pushExecutor = null;
        } else {
            throw new AssertionError(this.protocol);
        }
        this.bytesLeftInWriteWindow = (long) this.peerSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized);
        this.socket = builder.socket;
        this.frameWriter = this.variant.newWriter(Okio.buffer(Okio.sink(builder.socket)), this.client);
        this.readerRunnable = new Reader();
        new Thread(this.readerRunnable).start();
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public synchronized int openStreamCount() {
        return this.streams.size();
    }

    synchronized SpdyStream getStream(int i) {
        return (SpdyStream) this.streams.get(Integer.valueOf(i));
    }

    synchronized SpdyStream removeStream(int i) {
        SpdyStream spdyStream;
        spdyStream = (SpdyStream) this.streams.remove(Integer.valueOf(i));
        if (spdyStream != null && this.streams.isEmpty()) {
            setIdle(true);
        }
        notifyAll();
        return spdyStream;
    }

    private synchronized void setIdle(boolean z) {
        this.idleStartTimeNs = z ? System.nanoTime() : Long.MAX_VALUE;
    }

    public synchronized boolean isIdle() {
        return this.idleStartTimeNs != Long.MAX_VALUE ? true : $assertionsDisabled;
    }

    public synchronized long getIdleStartTimeNs() {
        return this.idleStartTimeNs;
    }

    public SpdyStream pushStream(int i, List<Header> list, boolean z) {
        if (this.client) {
            throw new IllegalStateException("Client cannot push requests.");
        } else if (this.protocol == Protocol.HTTP_2) {
            return newStream(i, list, z, $assertionsDisabled);
        } else {
            throw new IllegalStateException("protocol != HTTP_2");
        }
    }

    public SpdyStream newStream(List<Header> list, boolean z, boolean z2) {
        return newStream(0, list, z, z2);
    }

    private SpdyStream newStream(int i, List<Header> list, boolean z, boolean z2) {
        SpdyStream spdyStream;
        boolean z3 = true;
        boolean z4 = !z ? true : $assertionsDisabled;
        if (z2) {
            z3 = $assertionsDisabled;
        }
        synchronized (this.frameWriter) {
            synchronized (this) {
                if (this.shutdown) {
                    throw new IOException("shutdown");
                }
                int i2 = this.nextStreamId;
                this.nextStreamId += 2;
                spdyStream = new SpdyStream(i2, this, z4, z3, list);
                if (spdyStream.isOpen()) {
                    this.streams.put(Integer.valueOf(i2), spdyStream);
                    setIdle($assertionsDisabled);
                }
            }
            if (i == 0) {
                this.frameWriter.synStream(z4, z3, i2, i, list);
            } else if (this.client) {
                throw new IllegalArgumentException("client streams shouldn't have associated stream IDs");
            } else {
                this.frameWriter.pushPromise(i, i2, list);
            }
        }
        if (!z) {
            this.frameWriter.flush();
        }
        return spdyStream;
    }

    void writeSynReply(int i, boolean z, List<Header> list) {
        this.frameWriter.synReply(z, i, list);
    }

    public void writeData(int i, boolean z, Buffer buffer, long j) {
        if (j == 0) {
            this.frameWriter.data(z, i, buffer, 0);
            return;
        }
        while (j > 0) {
            int min;
            boolean z2;
            synchronized (this) {
                while (this.bytesLeftInWriteWindow <= 0) {
                    try {
                        if (this.streams.containsKey(Integer.valueOf(i))) {
                            wait();
                        } else {
                            throw new IOException("stream closed");
                        }
                    } catch (InterruptedException e) {
                        throw new InterruptedIOException();
                    }
                }
                min = Math.min((int) Math.min(j, this.bytesLeftInWriteWindow), this.frameWriter.maxDataLength());
                this.bytesLeftInWriteWindow -= (long) min;
            }
            j -= (long) min;
            FrameWriter frameWriter = this.frameWriter;
            if (z && j == 0) {
                z2 = true;
            } else {
                z2 = $assertionsDisabled;
            }
            frameWriter.data(z2, i, buffer, min);
        }
    }

    void addBytesToWriteWindow(long j) {
        this.bytesLeftInWriteWindow += j;
        if (j > 0) {
            notifyAll();
        }
    }

    void writeSynResetLater(int i, ErrorCode errorCode) {
        executor.submit(new C06571("OkHttp %s stream %d", new Object[]{this.hostName, Integer.valueOf(i)}, i, errorCode));
    }

    void writeSynReset(int i, ErrorCode errorCode) {
        this.frameWriter.rstStream(i, errorCode);
    }

    void writeWindowUpdateLater(int i, long j) {
        executor.execute(new C06582("OkHttp Window Update %s stream %d", new Object[]{this.hostName, Integer.valueOf(i)}, i, j));
    }

    public Ping ping() {
        int i;
        Ping ping = new Ping();
        synchronized (this) {
            if (this.shutdown) {
                throw new IOException("shutdown");
            }
            i = this.nextPingId;
            this.nextPingId += 2;
            if (this.pings == null) {
                this.pings = new HashMap();
            }
            this.pings.put(Integer.valueOf(i), ping);
        }
        writePing($assertionsDisabled, i, 1330343787, ping);
        return ping;
    }

    private void writePingLater(boolean z, int i, int i2, Ping ping) {
        executor.execute(new C06593("OkHttp %s ping %08x%08x", new Object[]{this.hostName, Integer.valueOf(i), Integer.valueOf(i2)}, z, i, i2, ping));
    }

    private void writePing(boolean z, int i, int i2, Ping ping) {
        synchronized (this.frameWriter) {
            if (ping != null) {
                ping.send();
            }
            this.frameWriter.ping(z, i, i2);
        }
    }

    private synchronized Ping removePing(int i) {
        return this.pings != null ? (Ping) this.pings.remove(Integer.valueOf(i)) : null;
    }

    public void flush() {
        this.frameWriter.flush();
    }

    /* JADX WARNING: inconsistent code. */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void shutdown(com.squareup.okhttp.internal.spdy.ErrorCode r5) {
        /*
        r4 = this;
        r1 = r4.frameWriter;
        monitor-enter(r1);
        monitor-enter(r4);	 Catch:{ all -> 0x001a }
        r0 = r4.shutdown;	 Catch:{ all -> 0x001d }
        if (r0 == 0) goto L_0x000b;
    L_0x0008:
        monitor-exit(r4);	 Catch:{ all -> 0x001d }
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
    L_0x000a:
        return;
    L_0x000b:
        r0 = 1;
        r4.shutdown = r0;	 Catch:{ all -> 0x001d }
        r0 = r4.lastGoodStreamId;	 Catch:{ all -> 0x001d }
        monitor-exit(r4);	 Catch:{ all -> 0x001d }
        r2 = r4.frameWriter;	 Catch:{ all -> 0x001a }
        r3 = com.squareup.okhttp.internal.Util.EMPTY_BYTE_ARRAY;	 Catch:{ all -> 0x001a }
        r2.goAway(r0, r5, r3);	 Catch:{ all -> 0x001a }
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        goto L_0x000a;
    L_0x001a:
        r0 = move-exception;
        monitor-exit(r1);	 Catch:{ all -> 0x001a }
        throw r0;
    L_0x001d:
        r0 = move-exception;
        monitor-exit(r4);	 Catch:{ all -> 0x001d }
        throw r0;	 Catch:{ all -> 0x001a }
        */
        throw new UnsupportedOperationException("Method not decompiled: com.squareup.okhttp.internal.spdy.SpdyConnection.shutdown(com.squareup.okhttp.internal.spdy.ErrorCode):void");
    }

    public void close() {
        close(ErrorCode.NO_ERROR, ErrorCode.CANCEL);
    }

    private void close(ErrorCode errorCode, ErrorCode errorCode2) {
        IOException iOException;
        IOException e;
        if ($assertionsDisabled || !Thread.holdsLock(this)) {
            SpdyStream[] spdyStreamArr;
            Ping[] pingArr;
            try {
                shutdown(errorCode);
                iOException = null;
            } catch (IOException e2) {
                iOException = e2;
            }
            synchronized (this) {
                if (this.streams.isEmpty()) {
                    spdyStreamArr = null;
                } else {
                    SpdyStream[] spdyStreamArr2 = (SpdyStream[]) this.streams.values().toArray(new SpdyStream[this.streams.size()]);
                    this.streams.clear();
                    setIdle($assertionsDisabled);
                    spdyStreamArr = spdyStreamArr2;
                }
                if (this.pings != null) {
                    Ping[] pingArr2 = (Ping[]) this.pings.values().toArray(new Ping[this.pings.size()]);
                    this.pings = null;
                    pingArr = pingArr2;
                } else {
                    pingArr = null;
                }
            }
            if (spdyStreamArr != null) {
                e2 = iOException;
                for (SpdyStream close : spdyStreamArr) {
                    try {
                        close.close(errorCode2);
                    } catch (IOException iOException2) {
                        if (e2 != null) {
                            e2 = iOException2;
                        }
                    }
                }
                iOException2 = e2;
            }
            if (pingArr != null) {
                for (Ping cancel : pingArr) {
                    cancel.cancel();
                }
            }
            try {
                this.frameWriter.close();
                e2 = iOException2;
            } catch (IOException e3) {
                e2 = e3;
                if (iOException2 != null) {
                    e2 = iOException2;
                }
            }
            try {
                this.socket.close();
            } catch (IOException e4) {
                e2 = e4;
            }
            if (e2 != null) {
                throw e2;
            }
            return;
        }
        throw new AssertionError();
    }

    public void sendConnectionPreface() {
        this.frameWriter.connectionPreface();
        this.frameWriter.settings(this.okHttpSettings);
        int initialWindowSize = this.okHttpSettings.getInitialWindowSize(PKIFailureInfo.notAuthorized);
        if (initialWindowSize != PKIFailureInfo.notAuthorized) {
            this.frameWriter.windowUpdate(0, (long) (initialWindowSize - PKIFailureInfo.notAuthorized));
        }
    }

    private boolean pushedStream(int i) {
        return (this.protocol == Protocol.HTTP_2 && i != 0 && (i & 1) == 0) ? true : $assertionsDisabled;
    }

    private void pushRequestLater(int i, List<Header> list) {
        synchronized (this) {
            if (this.currentPushRequests.contains(Integer.valueOf(i))) {
                writeSynResetLater(i, ErrorCode.PROTOCOL_ERROR);
                return;
            }
            this.currentPushRequests.add(Integer.valueOf(i));
            this.pushExecutor.execute(new C06604("OkHttp %s Push Request[%s]", new Object[]{this.hostName, Integer.valueOf(i)}, i, list));
        }
    }

    private void pushHeadersLater(int i, List<Header> list, boolean z) {
        this.pushExecutor.execute(new C06615("OkHttp %s Push Headers[%s]", new Object[]{this.hostName, Integer.valueOf(i)}, i, list, z));
    }

    private void pushDataLater(int i, BufferedSource bufferedSource, int i2, boolean z) {
        Buffer buffer = new Buffer();
        bufferedSource.require((long) i2);
        bufferedSource.read(buffer, (long) i2);
        if (buffer.size() != ((long) i2)) {
            throw new IOException(buffer.size() + " != " + i2);
        }
        this.pushExecutor.execute(new C06626("OkHttp %s Push Data[%s]", new Object[]{this.hostName, Integer.valueOf(i)}, i, buffer, i2, z));
    }

    private void pushResetLater(int i, ErrorCode errorCode) {
        this.pushExecutor.execute(new C06637("OkHttp %s Push Reset[%s]", new Object[]{this.hostName, Integer.valueOf(i)}, i, errorCode));
    }
}
