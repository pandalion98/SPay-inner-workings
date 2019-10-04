/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.CharSequence
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Integer
 *  java.lang.Math
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.UnsupportedOperationException
 *  java.util.List
 *  java.util.logging.Level
 *  java.util.logging.Logger
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.FrameReader;
import com.squareup.okhttp.internal.spdy.FrameWriter;
import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.HeadersMode;
import com.squareup.okhttp.internal.spdy.Hpack;
import com.squareup.okhttp.internal.spdy.Settings;
import com.squareup.okhttp.internal.spdy.Variant;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.Source;
import okio.Timeout;

public final class Http2
implements Variant {
    private static final ByteString CONNECTION_PREFACE;
    static final byte FLAG_ACK = 1;
    static final byte FLAG_COMPRESSED = 32;
    static final byte FLAG_END_HEADERS = 4;
    static final byte FLAG_END_PUSH_PROMISE = 4;
    static final byte FLAG_END_STREAM = 1;
    static final byte FLAG_NONE = 0;
    static final byte FLAG_PADDED = 8;
    static final byte FLAG_PRIORITY = 32;
    static final int INITIAL_MAX_FRAME_SIZE = 16384;
    static final byte TYPE_CONTINUATION = 9;
    static final byte TYPE_DATA = 0;
    static final byte TYPE_GOAWAY = 7;
    static final byte TYPE_HEADERS = 1;
    static final byte TYPE_PING = 6;
    static final byte TYPE_PRIORITY = 2;
    static final byte TYPE_PUSH_PROMISE = 5;
    static final byte TYPE_RST_STREAM = 3;
    static final byte TYPE_SETTINGS = 4;
    static final byte TYPE_WINDOW_UPDATE = 8;
    private static final Logger logger;

    static {
        logger = Logger.getLogger((String)FrameLogger.class.getName());
        CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
    }

    private static /* varargs */ IllegalArgumentException illegalArgument(String string, Object ... arrobject) {
        throw new IllegalArgumentException(String.format((String)string, (Object[])arrobject));
    }

    private static /* varargs */ IOException ioException(String string, Object ... arrobject) {
        throw new IOException(String.format((String)string, (Object[])arrobject));
    }

    private static int lengthWithoutPadding(int n2, byte by, short s2) {
        if ((by & 8) != 0) {
            --n2;
        }
        if (s2 > n2) {
            Object[] arrobject = new Object[]{s2, n2};
            throw Http2.ioException("PROTOCOL_ERROR padding %s > remaining length %s", arrobject);
        }
        return (short)(n2 - s2);
    }

    private static int readMedium(BufferedSource bufferedSource) {
        return (255 & bufferedSource.readByte()) << 16 | (255 & bufferedSource.readByte()) << 8 | 255 & bufferedSource.readByte();
    }

    private static void writeMedium(BufferedSink bufferedSink, int n2) {
        bufferedSink.writeByte(255 & n2 >>> 16);
        bufferedSink.writeByte(255 & n2 >>> 8);
        bufferedSink.writeByte(n2 & 255);
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    @Override
    public FrameReader newReader(BufferedSource bufferedSource, boolean bl) {
        return new Reader(bufferedSource, 4096, bl);
    }

    @Override
    public FrameWriter newWriter(BufferedSink bufferedSink, boolean bl) {
        return new Writer(bufferedSink, bl);
    }

    static final class ContinuationSource
    implements Source {
        byte flags;
        int left;
        int length;
        short padding;
        private final BufferedSource source;
        int streamId;

        public ContinuationSource(BufferedSource bufferedSource) {
            this.source = bufferedSource;
        }

        private void readContinuationHeader() {
            int n2;
            int n3 = this.streamId;
            this.left = n2 = Http2.readMedium(this.source);
            this.length = n2;
            byte by = (byte)(255 & this.source.readByte());
            this.flags = (byte)(255 & this.source.readByte());
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(FrameLogger.formatHeader(true, this.streamId, this.length, by, this.flags));
            }
            this.streamId = Integer.MAX_VALUE & this.source.readInt();
            if (by != 9) {
                Object[] arrobject = new Object[]{by};
                throw Http2.ioException("%s != TYPE_CONTINUATION", arrobject);
            }
            if (this.streamId != n3) {
                throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]);
            }
        }

        @Override
        public void close() {
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public long read(Buffer buffer, long l2) {
            while (this.left == 0) {
                this.source.skip(this.padding);
                this.padding = 0;
                if ((4 & this.flags) != 0) return -1L;
                {
                    this.readContinuationHeader();
                }
            }
            long l3 = this.source.read(buffer, Math.min((long)l2, (long)this.left));
            if (l3 == -1L) {
                return -1L;
            }
            this.left = (int)((long)this.left - l3);
            return l3;
        }

        @Override
        public Timeout timeout() {
            return this.source.timeout();
        }
    }

    static final class FrameLogger {
        private static final String[] BINARY;
        private static final String[] FLAGS;
        private static final String[] TYPES;

        static {
            TYPES = new String[]{"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};
            FLAGS = new String[64];
            BINARY = new String[256];
            for (int i2 = 0; i2 < BINARY.length; ++i2) {
                String[] arrstring = BINARY;
                Object[] arrobject = new Object[]{Integer.toBinaryString((int)i2)};
                arrstring[i2] = String.format((String)"%8s", (Object[])arrobject).replace(' ', '0');
            }
            FrameLogger.FLAGS[0] = "";
            FrameLogger.FLAGS[1] = "END_STREAM";
            int[] arrn = new int[]{1};
            FrameLogger.FLAGS[8] = "PADDED";
            int n2 = arrn.length;
            for (int i3 = 0; i3 < n2; ++i3) {
                int n3 = arrn[i3];
                FrameLogger.FLAGS[n3 | 8] = FLAGS[n3] + "|PADDED";
            }
            FrameLogger.FLAGS[4] = "END_HEADERS";
            FrameLogger.FLAGS[32] = "PRIORITY";
            FrameLogger.FLAGS[36] = "END_HEADERS|PRIORITY";
            int[] arrn2 = new int[]{4, 32, 36};
            int n4 = arrn2.length;
            int n5 = 0;
            do {
                if (n5 >= n4) break;
                int n6 = arrn2[n5];
                for (int n7 : arrn) {
                    FrameLogger.FLAGS[n7 | n6] = FLAGS[n7] + '|' + FLAGS[n6];
                    FrameLogger.FLAGS[8 | (n7 | n6)] = FLAGS[n7] + '|' + FLAGS[n6] + "|PADDED";
                }
                ++n5;
            } while (true);
            for (int i4 = 0; i4 < FLAGS.length; ++i4) {
                if (FLAGS[i4] != null) continue;
                FrameLogger.FLAGS[i4] = BINARY[i4];
            }
        }

        FrameLogger() {
        }

        /*
         * Enabled aggressive block sorting
         */
        static String formatFlags(byte by, byte by2) {
            String string;
            if (by2 == 0) {
                return "";
            }
            switch (by) {
                default: {
                    string = by2 < FLAGS.length ? FLAGS[by2] : BINARY[by2];
                }
                case 4: 
                case 6: {
                    if (by2 != 1) return BINARY[by2];
                    return "ACK";
                }
                case 2: 
                case 3: 
                case 7: 
                case 8: {
                    return BINARY[by2];
                }
            }
            if (by == 5 && (by2 & 4) != 0) {
                return string.replace((CharSequence)"HEADERS", (CharSequence)"PUSH_PROMISE");
            }
            if (by != 0) return string;
            if ((by2 & 32) == 0) return string;
            return string.replace((CharSequence)"PRIORITY", (CharSequence)"COMPRESSED");
        }

        /*
         * Enabled aggressive block sorting
         */
        static String formatHeader(boolean bl, int n2, int n3, byte by, byte by2) {
            String string;
            if (by < TYPES.length) {
                string = TYPES[by];
            } else {
                Object[] arrobject = new Object[]{by};
                string = String.format((String)"0x%02x", (Object[])arrobject);
            }
            String string2 = FrameLogger.formatFlags(by, by2);
            Object[] arrobject = new Object[5];
            String string3 = bl ? "<<" : ">>";
            arrobject[0] = string3;
            arrobject[1] = n2;
            arrobject[2] = n3;
            arrobject[3] = string;
            arrobject[4] = string2;
            return String.format((String)"%s 0x%08x %5d %-13s %s", (Object[])arrobject);
        }
    }

    static final class Reader
    implements FrameReader {
        private final boolean client;
        private final ContinuationSource continuation;
        final Hpack.Reader hpackReader;
        private final BufferedSource source;

        Reader(BufferedSource bufferedSource, int n2, boolean bl) {
            this.source = bufferedSource;
            this.client = bl;
            this.continuation = new ContinuationSource(this.source);
            this.hpackReader = new Hpack.Reader(n2, this.continuation);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readData(FrameReader.Handler handler, int n2, byte by, int n3) {
            boolean bl = true;
            boolean bl2 = (by & 1) != 0 ? bl : false;
            if ((by & 32) == 0) {
                bl = false;
            }
            if (bl) {
                throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]);
            }
            int n4 = by & 8;
            short s2 = 0;
            if (n4 != 0) {
                s2 = (short)(255 & this.source.readByte());
            }
            int n5 = Http2.lengthWithoutPadding(n2, by, s2);
            handler.data(bl2, n3, this.source, n5);
            this.source.skip(s2);
        }

        private void readGoAway(FrameReader.Handler handler, int n2, byte by, int n3) {
            if (n2 < 8) {
                Object[] arrobject = new Object[]{n2};
                throw Http2.ioException("TYPE_GOAWAY length < 8: %s", arrobject);
            }
            if (n3 != 0) {
                throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]);
            }
            int n4 = this.source.readInt();
            int n5 = this.source.readInt();
            int n6 = n2 - 8;
            ErrorCode errorCode = ErrorCode.fromHttp2(n5);
            if (errorCode == null) {
                Object[] arrobject = new Object[]{n5};
                throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", arrobject);
            }
            ByteString byteString = ByteString.EMPTY;
            if (n6 > 0) {
                byteString = this.source.readByteString(n6);
            }
            handler.goAway(n4, errorCode, byteString);
        }

        private List<Header> readHeaderBlock(int n2, short s2, byte by, int n3) {
            ContinuationSource continuationSource = this.continuation;
            this.continuation.left = n2;
            continuationSource.length = n2;
            this.continuation.padding = s2;
            this.continuation.flags = by;
            this.continuation.streamId = n3;
            this.hpackReader.readHeaders();
            return this.hpackReader.getAndResetHeaderList();
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readHeaders(FrameReader.Handler handler, int n2, byte by, int n3) {
            if (n3 == 0) {
                throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]);
            }
            boolean bl = (by & 1) != 0;
            short s2 = (by & 8) != 0 ? (short)(255 & this.source.readByte()) : (short)0;
            if ((by & 32) != 0) {
                this.readPriority(handler, n3);
                n2 -= 5;
            }
            handler.headers(false, bl, n3, -1, this.readHeaderBlock(Http2.lengthWithoutPadding(n2, by, s2), s2, by, n3), HeadersMode.HTTP_20_HEADERS);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readPing(FrameReader.Handler handler, int n2, byte by, int n3) {
            boolean bl = true;
            if (n2 != 8) {
                Object[] arrobject = new Object[bl];
                arrobject[0] = n2;
                throw Http2.ioException("TYPE_PING length != 8: %s", arrobject);
            }
            if (n3 != 0) {
                throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]);
            }
            int n4 = this.source.readInt();
            int n5 = this.source.readInt();
            if ((by & 1) == 0) {
                bl = false;
            }
            handler.ping(bl, n4, n5);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readPriority(FrameReader.Handler handler, int n2) {
            int n3 = this.source.readInt();
            boolean bl = (Integer.MIN_VALUE & n3) != 0;
            handler.priority(n2, n3 & Integer.MAX_VALUE, 1 + (255 & this.source.readByte()), bl);
        }

        private void readPriority(FrameReader.Handler handler, int n2, byte by, int n3) {
            if (n2 != 5) {
                Object[] arrobject = new Object[]{n2};
                throw Http2.ioException("TYPE_PRIORITY length: %d != 5", arrobject);
            }
            if (n3 == 0) {
                throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]);
            }
            this.readPriority(handler, n3);
        }

        private void readPushPromise(FrameReader.Handler handler, int n2, byte by, int n3) {
            if (n3 == 0) {
                throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]);
            }
            int n4 = by & 8;
            short s2 = 0;
            if (n4 != 0) {
                s2 = (short)(255 & this.source.readByte());
            }
            handler.pushPromise(n3, Integer.MAX_VALUE & this.source.readInt(), this.readHeaderBlock(Http2.lengthWithoutPadding(n2 - 4, by, s2), s2, by, n3));
        }

        private void readRstStream(FrameReader.Handler handler, int n2, byte by, int n3) {
            if (n2 != 4) {
                Object[] arrobject = new Object[]{n2};
                throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", arrobject);
            }
            if (n3 == 0) {
                throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]);
            }
            int n4 = this.source.readInt();
            ErrorCode errorCode = ErrorCode.fromHttp2(n4);
            if (errorCode == null) {
                Object[] arrobject = new Object[]{n4};
                throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", arrobject);
            }
            handler.rstStream(n3, errorCode);
        }

        /*
         * Unable to fully structure code
         * Enabled aggressive block sorting
         * Lifted jumps to return sites
         */
        private void readSettings(FrameReader.Handler var1_1, int var2_2, byte var3_3, int var4_4) {
            if (var4_4 != 0) {
                throw Http2.access$200("TYPE_SETTINGS streamId != 0", new Object[0]);
            }
            if ((var3_3 & 1) != 0) {
                if (var2_2 != 0) {
                    throw Http2.access$200("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]);
                }
                var1_1.ackSettings();
                return;
            }
            if (var2_2 % 6 != 0) {
                var12_5 = new Object[]{var2_2};
                throw Http2.access$200("TYPE_SETTINGS length %% 6 != 0: %s", var12_5);
            }
            var5_6 = new Settings();
            var6_7 = 0;
            do {
                if (var6_7 >= var2_2) {
                    var1_1.settings(false, var5_6);
                    if (var5_6.getHeaderTableSize() < 0) return;
                    this.hpackReader.headerTableSizeSetting(var5_6.getHeaderTableSize());
                    return;
                }
                var7_8 = this.source.readShort();
                var8_9 = this.source.readInt();
                switch (var7_8) {
                    default: {
                        var11_10 = new Object[]{var7_8};
                        throw Http2.access$200("PROTOCOL_ERROR invalid settings id: %s", var11_10);
                    }
                    case 2: {
                        if (var8_9 != 0 && var8_9 != 1) {
                            throw Http2.access$200("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]);
                        }
                        ** GOTO lbl36
                    }
                    case 3: {
                        var7_8 = 4;
                        break;
                    }
                    case 4: {
                        var7_8 = 7;
                        if (var8_9 < 0) {
                            throw Http2.access$200("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]);
                        }
                    }
lbl36: // 4 sources:
                    case 1: 
                    case 6: {
                        break;
                    }
                    case 5: {
                        if (var8_9 >= 16384 && var8_9 <= 16777215) break;
                        var9_11 = new Object[]{var8_9};
                        throw Http2.access$200("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", var9_11);
                    }
                }
                var5_6.set(var7_8, 0, var8_9);
                var6_7 += 6;
            } while (true);
        }

        private void readWindowUpdate(FrameReader.Handler handler, int n2, byte by, int n3) {
            if (n2 != 4) {
                Object[] arrobject = new Object[]{n2};
                throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", arrobject);
            }
            long l2 = Integer.MAX_VALUE & (long)this.source.readInt();
            if (l2 == 0L) {
                Object[] arrobject = new Object[]{l2};
                throw Http2.ioException("windowSizeIncrement was 0", arrobject);
            }
            handler.windowUpdate(n3, l2);
        }

        public void close() {
            this.source.close();
        }

        @Override
        public boolean nextFrame(FrameReader.Handler handler) {
            try {
                this.source.require(9L);
            }
            catch (IOException iOException) {
                return false;
            }
            int n2 = Http2.readMedium(this.source);
            if (n2 < 0 || n2 > 16384) {
                Object[] arrobject = new Object[]{n2};
                throw Http2.ioException("FRAME_SIZE_ERROR: %s", arrobject);
            }
            byte by = (byte)(255 & this.source.readByte());
            byte by2 = (byte)(255 & this.source.readByte());
            int n3 = Integer.MAX_VALUE & this.source.readInt();
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(FrameLogger.formatHeader(true, n3, n2, by, by2));
            }
            switch (by) {
                default: {
                    this.source.skip(n2);
                    return true;
                }
                case 0: {
                    this.readData(handler, n2, by2, n3);
                    return true;
                }
                case 1: {
                    this.readHeaders(handler, n2, by2, n3);
                    return true;
                }
                case 2: {
                    this.readPriority(handler, n2, by2, n3);
                    return true;
                }
                case 3: {
                    this.readRstStream(handler, n2, by2, n3);
                    return true;
                }
                case 4: {
                    this.readSettings(handler, n2, by2, n3);
                    return true;
                }
                case 5: {
                    this.readPushPromise(handler, n2, by2, n3);
                    return true;
                }
                case 6: {
                    this.readPing(handler, n2, by2, n3);
                    return true;
                }
                case 7: {
                    this.readGoAway(handler, n2, by2, n3);
                    return true;
                }
                case 8: 
            }
            this.readWindowUpdate(handler, n2, by2, n3);
            return true;
        }

        /*
         * Enabled aggressive block sorting
         */
        @Override
        public void readConnectionPreface() {
            ByteString byteString;
            block5 : {
                block4 : {
                    if (this.client) break block4;
                    byteString = this.source.readByteString(CONNECTION_PREFACE.size());
                    if (logger.isLoggable(Level.FINE)) {
                        Logger logger = logger;
                        Object[] arrobject = new Object[]{byteString.hex()};
                        logger.fine(String.format((String)"<< CONNECTION %s", (Object[])arrobject));
                    }
                    if (!CONNECTION_PREFACE.equals(byteString)) break block5;
                }
                return;
            }
            Object[] arrobject = new Object[]{byteString.utf8()};
            throw Http2.ioException("Expected a connection header but was %s", arrobject);
        }
    }

    static final class Writer
    implements FrameWriter {
        private final boolean client;
        private boolean closed;
        private final Buffer hpackBuffer;
        private final Hpack.Writer hpackWriter;
        private int maxFrameSize;
        private final BufferedSink sink;

        Writer(BufferedSink bufferedSink, boolean bl) {
            this.sink = bufferedSink;
            this.client = bl;
            this.hpackBuffer = new Buffer();
            this.hpackWriter = new Hpack.Writer(this.hpackBuffer);
            this.maxFrameSize = 16384;
        }

        /*
         * Enabled aggressive block sorting
         */
        private void writeContinuationFrames(int n2, long l2) {
            while (l2 > 0L) {
                int n3;
                byte by = (l2 -= (long)(n3 = (int)Math.min((long)this.maxFrameSize, (long)l2))) == 0L ? (byte)4 : 0;
                this.frameHeader(n2, n3, (byte)9, by);
                this.sink.write(this.hpackBuffer, (long)n3);
            }
            return;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void ackSettings(Settings settings) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                this.maxFrameSize = settings.getMaxFrameSize(this.maxFrameSize);
                this.frameHeader(0, 0, (byte)4, (byte)1);
                this.sink.flush();
                return;
            }
        }

        public void close() {
            Writer writer = this;
            synchronized (writer) {
                this.closed = true;
                this.sink.close();
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void connectionPreface() {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                boolean bl = this.client;
                if (bl) {
                    if (logger.isLoggable(Level.FINE)) {
                        Logger logger = logger;
                        Object[] arrobject = new Object[]{CONNECTION_PREFACE.hex()};
                        logger.fine(String.format((String)">> CONNECTION %s", (Object[])arrobject));
                    }
                    this.sink.write(CONNECTION_PREFACE.toByteArray());
                    this.sink.flush();
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void data(boolean bl, int n2, Buffer buffer, int n3) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                byte by = 0;
                if (bl) {
                    by = (byte)(true ? 1 : 0);
                }
                this.dataFrame(n2, by, buffer, n3);
                return;
            }
        }

        void dataFrame(int n2, byte by, Buffer buffer, int n3) {
            this.frameHeader(n2, n3, (byte)0, by);
            if (n3 > 0) {
                this.sink.write(buffer, (long)n3);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void flush() {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                this.sink.flush();
                return;
            }
        }

        void frameHeader(int n2, int n3, byte by, byte by2) {
            if (logger.isLoggable(Level.FINE)) {
                logger.fine(FrameLogger.formatHeader(false, n2, n3, by, by2));
            }
            if (n3 > this.maxFrameSize) {
                Object[] arrobject = new Object[]{this.maxFrameSize, n3};
                throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", arrobject);
            }
            if ((Integer.MIN_VALUE & n2) != 0) {
                Object[] arrobject = new Object[]{n2};
                throw Http2.illegalArgument("reserved bit set: %s", arrobject);
            }
            Http2.writeMedium(this.sink, n3);
            this.sink.writeByte(by & 255);
            this.sink.writeByte(by2 & 255);
            this.sink.writeInt(Integer.MAX_VALUE & n2);
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void goAway(int n2, ErrorCode errorCode, byte[] arrby) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (errorCode.httpCode == -1) {
                    throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]);
                }
                this.frameHeader(0, 8 + arrby.length, (byte)7, (byte)0);
                this.sink.writeInt(n2);
                this.sink.writeInt(errorCode.httpCode);
                if (arrby.length > 0) {
                    this.sink.write(arrby);
                }
                this.sink.flush();
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void headers(int n2, List<Header> list) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                this.headers(false, n2, list);
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         */
        void headers(boolean bl, int n2, List<Header> list) {
            if (this.closed) {
                throw new IOException("closed");
            }
            if (this.hpackBuffer.size() != 0L) {
                throw new IllegalStateException();
            }
            this.hpackWriter.writeHeaders(list);
            long l2 = this.hpackBuffer.size();
            int n3 = (int)Math.min((long)this.maxFrameSize, (long)l2);
            byte by = l2 == (long)n3 ? (byte)4 : 0;
            if (bl) {
                by = (byte)(by | 1);
            }
            this.frameHeader(n2, n3, (byte)1, by);
            this.sink.write(this.hpackBuffer, (long)n3);
            if (l2 > (long)n3) {
                this.writeContinuationFrames(n2, l2 - (long)n3);
            }
        }

        @Override
        public int maxDataLength() {
            return this.maxFrameSize;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void ping(boolean bl, int n2, int n3) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                byte by = 0;
                if (bl) {
                    by = 1;
                }
                this.frameHeader(0, 8, (byte)6, by);
                this.sink.writeInt(n2);
                this.sink.writeInt(n3);
                this.sink.flush();
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void pushPromise(int n2, int n3, List<Header> list) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (this.hpackBuffer.size() != 0L) {
                    throw new IllegalStateException();
                }
                this.hpackWriter.writeHeaders(list);
                long l2 = this.hpackBuffer.size();
                int n4 = (int)Math.min((long)(-4 + this.maxFrameSize), (long)l2);
                byte by = l2 == (long)n4 ? (byte)4 : 0;
                this.frameHeader(n2, n4 + 4, (byte)5, by);
                this.sink.writeInt(Integer.MAX_VALUE & n3);
                this.sink.write(this.hpackBuffer, (long)n4);
                if (l2 > (long)n4) {
                    this.writeContinuationFrames(n2, l2 - (long)n4);
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void rstStream(int n2, ErrorCode errorCode) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (errorCode.spdyRstCode == -1) {
                    throw new IllegalArgumentException();
                }
                this.frameHeader(n2, 4, (byte)3, (byte)0);
                this.sink.writeInt(errorCode.httpCode);
                this.sink.flush();
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void settings(Settings settings) {
            int n2 = 0;
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                this.frameHeader(0, 6 * settings.size(), (byte)4, (byte)0);
                do {
                    if (n2 >= 10) {
                        this.sink.flush();
                        return;
                    }
                    if (settings.isSet(n2)) {
                        int n3 = n2 == 4 ? 3 : (n2 == 7 ? 4 : n2);
                        this.sink.writeShort(n3);
                        this.sink.writeInt(settings.get(n2));
                    }
                    ++n2;
                } while (true);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void synReply(boolean bl, int n2, List<Header> list) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                this.headers(bl, n2, list);
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void synStream(boolean bl, boolean bl2, int n2, int n3, List<Header> list) {
            Writer writer = this;
            synchronized (writer) {
                if (bl2) {
                    throw new UnsupportedOperationException();
                }
                if (this.closed) {
                    throw new IOException("closed");
                }
                this.headers(bl, n2, list);
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void windowUpdate(int n2, long l2) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (l2 != 0L && l2 <= Integer.MAX_VALUE) {
                    this.frameHeader(n2, 4, (byte)8, (byte)0);
                    this.sink.writeInt((int)l2);
                    this.sink.flush();
                    return;
                }
                Object[] arrobject = new Object[]{l2};
                throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", arrobject);
            }
        }
    }

}

