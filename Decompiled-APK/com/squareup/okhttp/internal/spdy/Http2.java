package com.squareup.okhttp.internal.spdy;

import android.support.v4.view.ViewCompat;
import com.americanexpress.mobilepayments.hceclient.utils.common.LLVARUtil;
import com.samsung.android.spayfw.cncc.CNCCCommands;
import com.samsung.android.spaytzsvc.api.visa.BuildConfig;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.spdy.FrameReader.Handler;
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
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.crypto.macs.SkeinMac;
import org.bouncycastle.jce.X509KeyUsage;
import org.bouncycastle.math.ec.ECCurve;
import org.bouncycastle.math.ec.ECFieldElement.F2m;
import org.bouncycastle.pqc.crypto.rainbow.util.GF2Field;
import org.bouncycastle.pqc.jcajce.provider.util.CipherSpiExt;
import org.bouncycastle.x509.ExtendedPKIXParameters;

public final class Http2 implements Variant {
    private static final ByteString CONNECTION_PREFACE;
    static final byte FLAG_ACK = (byte) 1;
    static final byte FLAG_COMPRESSED = (byte) 32;
    static final byte FLAG_END_HEADERS = (byte) 4;
    static final byte FLAG_END_PUSH_PROMISE = (byte) 4;
    static final byte FLAG_END_STREAM = (byte) 1;
    static final byte FLAG_NONE = (byte) 0;
    static final byte FLAG_PADDED = (byte) 8;
    static final byte FLAG_PRIORITY = (byte) 32;
    static final int INITIAL_MAX_FRAME_SIZE = 16384;
    static final byte TYPE_CONTINUATION = (byte) 9;
    static final byte TYPE_DATA = (byte) 0;
    static final byte TYPE_GOAWAY = (byte) 7;
    static final byte TYPE_HEADERS = (byte) 1;
    static final byte TYPE_PING = (byte) 6;
    static final byte TYPE_PRIORITY = (byte) 2;
    static final byte TYPE_PUSH_PROMISE = (byte) 5;
    static final byte TYPE_RST_STREAM = (byte) 3;
    static final byte TYPE_SETTINGS = (byte) 4;
    static final byte TYPE_WINDOW_UPDATE = (byte) 8;
    private static final Logger logger;

    static final class ContinuationSource implements Source {
        byte flags;
        int left;
        int length;
        short padding;
        private final BufferedSource source;
        int streamId;

        public ContinuationSource(BufferedSource bufferedSource) {
            this.source = bufferedSource;
        }

        public long read(Buffer buffer, long j) {
            while (this.left == 0) {
                this.source.skip((long) this.padding);
                this.padding = (short) 0;
                if ((this.flags & 4) != 0) {
                    return -1;
                }
                readContinuationHeader();
            }
            long read = this.source.read(buffer, Math.min(j, (long) this.left));
            if (read == -1) {
                return -1;
            }
            this.left = (int) (((long) this.left) - read);
            return read;
        }

        public Timeout timeout() {
            return this.source.timeout();
        }

        public void close() {
        }

        private void readContinuationHeader() {
            int i = this.streamId;
            int access$300 = Http2.readMedium(this.source);
            this.left = access$300;
            this.length = access$300;
            byte readByte = (byte) (this.source.readByte() & GF2Field.MASK);
            this.flags = (byte) (this.source.readByte() & GF2Field.MASK);
            if (Http2.logger.isLoggable(Level.FINE)) {
                Http2.logger.fine(FrameLogger.formatHeader(true, this.streamId, this.length, readByte, this.flags));
            }
            this.streamId = this.source.readInt() & CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
            if (readByte != 9) {
                throw Http2.ioException("%s != TYPE_CONTINUATION", Byte.valueOf(readByte));
            } else if (this.streamId != i) {
                throw Http2.ioException("TYPE_CONTINUATION streamId changed", new Object[0]);
            }
        }
    }

    static final class FrameLogger {
        private static final String[] BINARY;
        private static final String[] FLAGS;
        private static final String[] TYPES;

        FrameLogger() {
        }

        static String formatHeader(boolean z, int i, int i2, byte b, byte b2) {
            String format = b < TYPES.length ? TYPES[b] : String.format("0x%02x", new Object[]{Byte.valueOf(b)});
            String formatFlags = formatFlags(b, b2);
            String str = "%s 0x%08x %5d %-13s %s";
            Object[] objArr = new Object[5];
            objArr[0] = z ? "<<" : ">>";
            objArr[1] = Integer.valueOf(i);
            objArr[2] = Integer.valueOf(i2);
            objArr[3] = format;
            objArr[4] = formatFlags;
            return String.format(str, objArr);
        }

        static String formatFlags(byte b, byte b2) {
            if (b2 == null) {
                return BuildConfig.FLAVOR;
            }
            switch (b) {
                case CipherSpiExt.DECRYPT_MODE /*2*/:
                case F2m.PPB /*3*/:
                case ECCurve.COORD_SKEWED /*7*/:
                case X509KeyUsage.keyAgreement /*8*/:
                    return BINARY[b2];
                case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                    return b2 == 1 ? "ACK" : BINARY[b2];
                default:
                    String str = b2 < FLAGS.length ? FLAGS[b2] : BINARY[b2];
                    if (b == 5 && (b2 & 4) != 0) {
                        return str.replace("HEADERS", "PUSH_PROMISE");
                    }
                    if (b != null || (b2 & 32) == 0) {
                        return str;
                    }
                    return str.replace("PRIORITY", "COMPRESSED");
            }
        }

        static {
            int i = 0;
            TYPES = new String[]{"DATA", "HEADERS", "PRIORITY", "RST_STREAM", "SETTINGS", "PUSH_PROMISE", "PING", "GOAWAY", "WINDOW_UPDATE", "CONTINUATION"};
            FLAGS = new String[64];
            BINARY = new String[SkeinMac.SKEIN_256];
            for (int i2 = 0; i2 < BINARY.length; i2++) {
                BINARY[i2] = String.format("%8s", new Object[]{Integer.toBinaryString(i2)}).replace(' ', LLVARUtil.EMPTY_STRING);
            }
            FLAGS[0] = BuildConfig.FLAVOR;
            FLAGS[1] = "END_STREAM";
            int[] iArr = new int[]{1};
            FLAGS[8] = "PADDED";
            for (int i3 : iArr) {
                FLAGS[i3 | 8] = FLAGS[i3] + "|PADDED";
            }
            FLAGS[4] = "END_HEADERS";
            FLAGS[32] = "PRIORITY";
            FLAGS[36] = "END_HEADERS|PRIORITY";
            for (int i4 : new int[]{4, 32, 36}) {
                for (int i5 : iArr) {
                    FLAGS[i5 | i4] = FLAGS[i5] + '|' + FLAGS[i4];
                    FLAGS[(i5 | i4) | 8] = FLAGS[i5] + '|' + FLAGS[i4] + "|PADDED";
                }
            }
            while (i < FLAGS.length) {
                if (FLAGS[i] == null) {
                    FLAGS[i] = BINARY[i];
                }
                i++;
            }
        }
    }

    static final class Reader implements FrameReader {
        private final boolean client;
        private final ContinuationSource continuation;
        final Reader hpackReader;
        private final BufferedSource source;

        Reader(BufferedSource bufferedSource, int i, boolean z) {
            this.source = bufferedSource;
            this.client = z;
            this.continuation = new ContinuationSource(this.source);
            this.hpackReader = new Reader(i, this.continuation);
        }

        public void readConnectionPreface() {
            if (!this.client) {
                ByteString readByteString = this.source.readByteString((long) Http2.CONNECTION_PREFACE.size());
                if (Http2.logger.isLoggable(Level.FINE)) {
                    Http2.logger.fine(String.format("<< CONNECTION %s", new Object[]{readByteString.hex()}));
                }
                if (!Http2.CONNECTION_PREFACE.equals(readByteString)) {
                    throw Http2.ioException("Expected a connection header but was %s", readByteString.utf8());
                }
            }
        }

        public boolean nextFrame(Handler handler) {
            try {
                this.source.require(9);
                int access$300 = Http2.readMedium(this.source);
                if (access$300 < 0 || access$300 > Http2.INITIAL_MAX_FRAME_SIZE) {
                    throw Http2.ioException("FRAME_SIZE_ERROR: %s", Integer.valueOf(access$300));
                }
                byte readByte = (byte) (this.source.readByte() & GF2Field.MASK);
                byte readByte2 = (byte) (this.source.readByte() & GF2Field.MASK);
                int readInt = this.source.readInt() & CNCCCommands.CMD_CNCC_CMD_UNKNOWN;
                if (Http2.logger.isLoggable(Level.FINE)) {
                    Http2.logger.fine(FrameLogger.formatHeader(true, readInt, access$300, readByte, readByte2));
                }
                switch (readByte) {
                    case ECCurve.COORD_AFFINE /*0*/:
                        readData(handler, access$300, readByte2, readInt);
                        return true;
                    case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        readHeaders(handler, access$300, readByte2, readInt);
                        return true;
                    case CipherSpiExt.DECRYPT_MODE /*2*/:
                        readPriority(handler, access$300, readByte2, readInt);
                        return true;
                    case F2m.PPB /*3*/:
                        readRstStream(handler, access$300, readByte2, readInt);
                        return true;
                    case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                        readSettings(handler, access$300, readByte2, readInt);
                        return true;
                    case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                        readPushPromise(handler, access$300, readByte2, readInt);
                        return true;
                    case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                        readPing(handler, access$300, readByte2, readInt);
                        return true;
                    case ECCurve.COORD_SKEWED /*7*/:
                        readGoAway(handler, access$300, readByte2, readInt);
                        return true;
                    case X509KeyUsage.keyAgreement /*8*/:
                        readWindowUpdate(handler, access$300, readByte2, readInt);
                        return true;
                    default:
                        this.source.skip((long) access$300);
                        return true;
                }
            } catch (IOException e) {
                return false;
            }
        }

        private void readHeaders(Handler handler, int i, byte b, int i2) {
            if (i2 == 0) {
                throw Http2.ioException("PROTOCOL_ERROR: TYPE_HEADERS streamId == 0", new Object[0]);
            }
            short readByte;
            boolean z = (b & 1) != 0;
            if ((b & 8) != 0) {
                readByte = (short) (this.source.readByte() & GF2Field.MASK);
            } else {
                readByte = (short) 0;
            }
            if ((b & 32) != 0) {
                readPriority(handler, i2);
                i -= 5;
            }
            handler.headers(false, z, i2, -1, readHeaderBlock(Http2.lengthWithoutPadding(i, b, readByte), readByte, b, i2), HeadersMode.HTTP_20_HEADERS);
        }

        private List<Header> readHeaderBlock(int i, short s, byte b, int i2) {
            ContinuationSource continuationSource = this.continuation;
            this.continuation.left = i;
            continuationSource.length = i;
            this.continuation.padding = s;
            this.continuation.flags = b;
            this.continuation.streamId = i2;
            this.hpackReader.readHeaders();
            return this.hpackReader.getAndResetHeaderList();
        }

        private void readData(Handler handler, int i, byte b, int i2) {
            boolean z;
            short s = (short) 1;
            short s2 = (short) 0;
            if ((b & 1) != 0) {
                z = true;
            } else {
                z = false;
            }
            if ((b & 32) == 0) {
                s = (short) 0;
            }
            if (s != (short) 0) {
                throw Http2.ioException("PROTOCOL_ERROR: FLAG_COMPRESSED without SETTINGS_COMPRESS_DATA", new Object[0]);
            }
            if ((b & 8) != 0) {
                s2 = (short) (this.source.readByte() & GF2Field.MASK);
            }
            handler.data(z, i2, this.source, Http2.lengthWithoutPadding(i, b, s2));
            this.source.skip((long) s2);
        }

        private void readPriority(Handler handler, int i, byte b, int i2) {
            if (i != 5) {
                throw Http2.ioException("TYPE_PRIORITY length: %d != 5", Integer.valueOf(i));
            } else if (i2 == 0) {
                throw Http2.ioException("TYPE_PRIORITY streamId == 0", new Object[0]);
            } else {
                readPriority(handler, i2);
            }
        }

        private void readPriority(Handler handler, int i) {
            int readInt = this.source.readInt();
            handler.priority(i, readInt & CNCCCommands.CMD_CNCC_CMD_UNKNOWN, (this.source.readByte() & GF2Field.MASK) + 1, (PKIFailureInfo.systemUnavail & readInt) != 0);
        }

        private void readRstStream(Handler handler, int i, byte b, int i2) {
            if (i != 4) {
                throw Http2.ioException("TYPE_RST_STREAM length: %d != 4", Integer.valueOf(i));
            } else if (i2 == 0) {
                throw Http2.ioException("TYPE_RST_STREAM streamId == 0", new Object[0]);
            } else {
                ErrorCode fromHttp2 = ErrorCode.fromHttp2(this.source.readInt());
                if (fromHttp2 == null) {
                    throw Http2.ioException("TYPE_RST_STREAM unexpected error code: %d", Integer.valueOf(r0));
                } else {
                    handler.rstStream(i2, fromHttp2);
                }
            }
        }

        private void readSettings(Handler handler, int i, byte b, int i2) {
            if (i2 != 0) {
                throw Http2.ioException("TYPE_SETTINGS streamId != 0", new Object[0]);
            } else if ((b & 1) != 0) {
                if (i != 0) {
                    throw Http2.ioException("FRAME_SIZE_ERROR ack frame should be empty!", new Object[0]);
                }
                handler.ackSettings();
            } else if (i % 6 != 0) {
                throw Http2.ioException("TYPE_SETTINGS length %% 6 != 0: %s", Integer.valueOf(i));
            } else {
                Settings settings = new Settings();
                for (int i3 = 0; i3 < i; i3 += 6) {
                    int readShort = this.source.readShort();
                    int readInt = this.source.readInt();
                    switch (readShort) {
                        case ExtendedPKIXParameters.CHAIN_VALIDITY_MODEL /*1*/:
                        case ECCurve.COORD_LAMBDA_PROJECTIVE /*6*/:
                            break;
                        case CipherSpiExt.DECRYPT_MODE /*2*/:
                            if (!(readInt == 0 || readInt == 1)) {
                                throw Http2.ioException("PROTOCOL_ERROR SETTINGS_ENABLE_PUSH != 0 or 1", new Object[0]);
                            }
                        case F2m.PPB /*3*/:
                            readShort = 4;
                            break;
                        case ECCurve.COORD_JACOBIAN_MODIFIED /*4*/:
                            readShort = 7;
                            if (readInt >= 0) {
                                break;
                            }
                            throw Http2.ioException("PROTOCOL_ERROR SETTINGS_INITIAL_WINDOW_SIZE > 2^31 - 1", new Object[0]);
                        case ECCurve.COORD_LAMBDA_AFFINE /*5*/:
                            if (readInt >= Http2.INITIAL_MAX_FRAME_SIZE && readInt <= ViewCompat.MEASURED_SIZE_MASK) {
                                break;
                            }
                            throw Http2.ioException("PROTOCOL_ERROR SETTINGS_MAX_FRAME_SIZE: %s", Integer.valueOf(readInt));
                        default:
                            throw Http2.ioException("PROTOCOL_ERROR invalid settings id: %s", Short.valueOf(readShort));
                    }
                    settings.set(readShort, 0, readInt);
                }
                handler.settings(false, settings);
                if (settings.getHeaderTableSize() >= 0) {
                    this.hpackReader.headerTableSizeSetting(settings.getHeaderTableSize());
                }
            }
        }

        private void readPushPromise(Handler handler, int i, byte b, int i2) {
            short s = (short) 0;
            if (i2 == 0) {
                throw Http2.ioException("PROTOCOL_ERROR: TYPE_PUSH_PROMISE streamId == 0", new Object[0]);
            }
            if ((b & 8) != 0) {
                s = (short) (this.source.readByte() & GF2Field.MASK);
            }
            handler.pushPromise(i2, this.source.readInt() & CNCCCommands.CMD_CNCC_CMD_UNKNOWN, readHeaderBlock(Http2.lengthWithoutPadding(i - 4, b, s), s, b, i2));
        }

        private void readPing(Handler handler, int i, byte b, int i2) {
            boolean z = true;
            if (i != 8) {
                throw Http2.ioException("TYPE_PING length != 8: %s", Integer.valueOf(i));
            } else if (i2 != 0) {
                throw Http2.ioException("TYPE_PING streamId != 0", new Object[0]);
            } else {
                int readInt = this.source.readInt();
                int readInt2 = this.source.readInt();
                if ((b & 1) == 0) {
                    z = false;
                }
                handler.ping(z, readInt, readInt2);
            }
        }

        private void readGoAway(Handler handler, int i, byte b, int i2) {
            if (i < 8) {
                throw Http2.ioException("TYPE_GOAWAY length < 8: %s", Integer.valueOf(i));
            } else if (i2 != 0) {
                throw Http2.ioException("TYPE_GOAWAY streamId != 0", new Object[0]);
            } else {
                int readInt = this.source.readInt();
                int i3 = i - 8;
                ErrorCode fromHttp2 = ErrorCode.fromHttp2(this.source.readInt());
                if (fromHttp2 == null) {
                    throw Http2.ioException("TYPE_GOAWAY unexpected error code: %d", Integer.valueOf(r0));
                }
                ByteString byteString = ByteString.EMPTY;
                if (i3 > 0) {
                    byteString = this.source.readByteString((long) i3);
                }
                handler.goAway(readInt, fromHttp2, byteString);
            }
        }

        private void readWindowUpdate(Handler handler, int i, byte b, int i2) {
            if (i != 4) {
                throw Http2.ioException("TYPE_WINDOW_UPDATE length !=4: %s", Integer.valueOf(i));
            }
            long readInt = ((long) this.source.readInt()) & 2147483647L;
            if (readInt == 0) {
                throw Http2.ioException("windowSizeIncrement was 0", Long.valueOf(readInt));
            } else {
                handler.windowUpdate(i2, readInt);
            }
        }

        public void close() {
            this.source.close();
        }
    }

    static final class Writer implements FrameWriter {
        private final boolean client;
        private boolean closed;
        private final Buffer hpackBuffer;
        private final Writer hpackWriter;
        private int maxFrameSize;
        private final BufferedSink sink;

        Writer(BufferedSink bufferedSink, boolean z) {
            this.sink = bufferedSink;
            this.client = z;
            this.hpackBuffer = new Buffer();
            this.hpackWriter = new Writer(this.hpackBuffer);
            this.maxFrameSize = Http2.INITIAL_MAX_FRAME_SIZE;
        }

        public synchronized void flush() {
            if (this.closed) {
                throw new IOException("closed");
            }
            this.sink.flush();
        }

        public synchronized void ackSettings(Settings settings) {
            if (this.closed) {
                throw new IOException("closed");
            }
            this.maxFrameSize = settings.getMaxFrameSize(this.maxFrameSize);
            frameHeader(0, 0, Http2.TYPE_SETTINGS, Http2.TYPE_HEADERS);
            this.sink.flush();
        }

        public synchronized void connectionPreface() {
            if (this.closed) {
                throw new IOException("closed");
            } else if (this.client) {
                if (Http2.logger.isLoggable(Level.FINE)) {
                    Http2.logger.fine(String.format(">> CONNECTION %s", new Object[]{Http2.CONNECTION_PREFACE.hex()}));
                }
                this.sink.write(Http2.CONNECTION_PREFACE.toByteArray());
                this.sink.flush();
            }
        }

        public synchronized void synStream(boolean z, boolean z2, int i, int i2, List<Header> list) {
            if (z2) {
                throw new UnsupportedOperationException();
            } else if (this.closed) {
                throw new IOException("closed");
            } else {
                headers(z, i, list);
            }
        }

        public synchronized void synReply(boolean z, int i, List<Header> list) {
            if (this.closed) {
                throw new IOException("closed");
            }
            headers(z, i, list);
        }

        public synchronized void headers(int i, List<Header> list) {
            if (this.closed) {
                throw new IOException("closed");
            }
            headers(false, i, list);
        }

        public synchronized void pushPromise(int i, int i2, List<Header> list) {
            if (this.closed) {
                throw new IOException("closed");
            } else if (this.hpackBuffer.size() != 0) {
                throw new IllegalStateException();
            } else {
                this.hpackWriter.writeHeaders(list);
                long size = this.hpackBuffer.size();
                int min = (int) Math.min((long) (this.maxFrameSize - 4), size);
                frameHeader(i, min + 4, Http2.TYPE_PUSH_PROMISE, size == ((long) min) ? Http2.TYPE_SETTINGS : Http2.TYPE_DATA);
                this.sink.writeInt(CNCCCommands.CMD_CNCC_CMD_UNKNOWN & i2);
                this.sink.write(this.hpackBuffer, (long) min);
                if (size > ((long) min)) {
                    writeContinuationFrames(i, size - ((long) min));
                }
            }
        }

        void headers(boolean z, int i, List<Header> list) {
            if (this.closed) {
                throw new IOException("closed");
            } else if (this.hpackBuffer.size() != 0) {
                throw new IllegalStateException();
            } else {
                this.hpackWriter.writeHeaders(list);
                long size = this.hpackBuffer.size();
                int min = (int) Math.min((long) this.maxFrameSize, size);
                byte b = size == ((long) min) ? Http2.TYPE_SETTINGS : Http2.TYPE_DATA;
                if (z) {
                    b = (byte) (b | 1);
                }
                frameHeader(i, min, Http2.TYPE_HEADERS, b);
                this.sink.write(this.hpackBuffer, (long) min);
                if (size > ((long) min)) {
                    writeContinuationFrames(i, size - ((long) min));
                }
            }
        }

        private void writeContinuationFrames(int i, long j) {
            while (j > 0) {
                int min = (int) Math.min((long) this.maxFrameSize, j);
                j -= (long) min;
                frameHeader(i, min, Http2.TYPE_CONTINUATION, j == 0 ? Http2.TYPE_SETTINGS : Http2.TYPE_DATA);
                this.sink.write(this.hpackBuffer, (long) min);
            }
        }

        public synchronized void rstStream(int i, ErrorCode errorCode) {
            if (this.closed) {
                throw new IOException("closed");
            } else if (errorCode.spdyRstCode == -1) {
                throw new IllegalArgumentException();
            } else {
                frameHeader(i, 4, Http2.TYPE_RST_STREAM, Http2.TYPE_DATA);
                this.sink.writeInt(errorCode.httpCode);
                this.sink.flush();
            }
        }

        public int maxDataLength() {
            return this.maxFrameSize;
        }

        public synchronized void data(boolean z, int i, Buffer buffer, int i2) {
            if (this.closed) {
                throw new IOException("closed");
            }
            byte b = Http2.TYPE_DATA;
            if (z) {
                b = (byte) 1;
            }
            dataFrame(i, b, buffer, i2);
        }

        void dataFrame(int i, byte b, Buffer buffer, int i2) {
            frameHeader(i, i2, Http2.TYPE_DATA, b);
            if (i2 > 0) {
                this.sink.write(buffer, (long) i2);
            }
        }

        public synchronized void settings(Settings settings) {
            synchronized (this) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                frameHeader(0, settings.size() * 6, Http2.TYPE_SETTINGS, Http2.TYPE_DATA);
                for (int i = 0; i < 10; i++) {
                    if (settings.isSet(i)) {
                        int i2;
                        if (i == 4) {
                            i2 = 3;
                        } else if (i == 7) {
                            i2 = 4;
                        } else {
                            i2 = i;
                        }
                        this.sink.writeShort(i2);
                        this.sink.writeInt(settings.get(i));
                    }
                }
                this.sink.flush();
            }
        }

        public synchronized void ping(boolean z, int i, int i2) {
            byte b = Http2.TYPE_DATA;
            synchronized (this) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (z) {
                    b = Http2.TYPE_HEADERS;
                }
                frameHeader(0, 8, Http2.TYPE_PING, b);
                this.sink.writeInt(i);
                this.sink.writeInt(i2);
                this.sink.flush();
            }
        }

        public synchronized void goAway(int i, ErrorCode errorCode, byte[] bArr) {
            if (this.closed) {
                throw new IOException("closed");
            } else if (errorCode.httpCode == -1) {
                throw Http2.illegalArgument("errorCode.httpCode == -1", new Object[0]);
            } else {
                frameHeader(0, bArr.length + 8, Http2.TYPE_GOAWAY, Http2.TYPE_DATA);
                this.sink.writeInt(i);
                this.sink.writeInt(errorCode.httpCode);
                if (bArr.length > 0) {
                    this.sink.write(bArr);
                }
                this.sink.flush();
            }
        }

        public synchronized void windowUpdate(int i, long j) {
            if (this.closed) {
                throw new IOException("closed");
            } else if (j == 0 || j > 2147483647L) {
                throw Http2.illegalArgument("windowSizeIncrement == 0 || windowSizeIncrement > 0x7fffffffL: %s", Long.valueOf(j));
            } else {
                frameHeader(i, 4, Http2.TYPE_WINDOW_UPDATE, Http2.TYPE_DATA);
                this.sink.writeInt((int) j);
                this.sink.flush();
            }
        }

        public synchronized void close() {
            this.closed = true;
            this.sink.close();
        }

        void frameHeader(int i, int i2, byte b, byte b2) {
            if (Http2.logger.isLoggable(Level.FINE)) {
                Http2.logger.fine(FrameLogger.formatHeader(false, i, i2, b, b2));
            }
            if (i2 > this.maxFrameSize) {
                throw Http2.illegalArgument("FRAME_SIZE_ERROR length > %d: %d", Integer.valueOf(this.maxFrameSize), Integer.valueOf(i2));
            } else if ((PKIFailureInfo.systemUnavail & i) != 0) {
                throw Http2.illegalArgument("reserved bit set: %s", Integer.valueOf(i));
            } else {
                Http2.writeMedium(this.sink, i2);
                this.sink.writeByte(b & GF2Field.MASK);
                this.sink.writeByte(b2 & GF2Field.MASK);
                this.sink.writeInt(CNCCCommands.CMD_CNCC_CMD_UNKNOWN & i);
            }
        }
    }

    static {
        logger = Logger.getLogger(FrameLogger.class.getName());
        CONNECTION_PREFACE = ByteString.encodeUtf8("PRI * HTTP/2.0\r\n\r\nSM\r\n\r\n");
    }

    public Protocol getProtocol() {
        return Protocol.HTTP_2;
    }

    public FrameReader newReader(BufferedSource bufferedSource, boolean z) {
        return new Reader(bufferedSource, PKIFailureInfo.certConfirmed, z);
    }

    public FrameWriter newWriter(BufferedSink bufferedSink, boolean z) {
        return new Writer(bufferedSink, z);
    }

    private static IllegalArgumentException illegalArgument(String str, Object... objArr) {
        throw new IllegalArgumentException(String.format(str, objArr));
    }

    private static IOException ioException(String str, Object... objArr) {
        throw new IOException(String.format(str, objArr));
    }

    private static int lengthWithoutPadding(int i, byte b, short s) {
        if ((b & 8) != 0) {
            short s2 = i - 1;
        }
        if (s <= s2) {
            return (short) (s2 - s);
        }
        throw ioException("PROTOCOL_ERROR padding %s > remaining length %s", Short.valueOf(s), Integer.valueOf(s2));
    }

    private static int readMedium(BufferedSource bufferedSource) {
        return (((bufferedSource.readByte() & GF2Field.MASK) << 16) | ((bufferedSource.readByte() & GF2Field.MASK) << 8)) | (bufferedSource.readByte() & GF2Field.MASK);
    }

    private static void writeMedium(BufferedSink bufferedSink, int i) {
        bufferedSink.writeByte((i >>> 16) & GF2Field.MASK);
        bufferedSink.writeByte((i >>> 8) & GF2Field.MASK);
        bufferedSink.writeByte(i & GF2Field.MASK);
    }
}
