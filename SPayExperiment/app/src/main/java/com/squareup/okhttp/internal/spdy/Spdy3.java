/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.io.UnsupportedEncodingException
 *  java.lang.AssertionError
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.ProtocolException
 *  java.nio.charset.Charset
 *  java.util.List
 *  java.util.zip.Deflater
 */
package com.squareup.okhttp.internal.spdy;

import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.FrameReader;
import com.squareup.okhttp.internal.spdy.FrameWriter;
import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.HeadersMode;
import com.squareup.okhttp.internal.spdy.NameValueBlockReader;
import com.squareup.okhttp.internal.spdy.Settings;
import com.squareup.okhttp.internal.spdy.Variant;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.ProtocolException;
import java.nio.charset.Charset;
import java.util.List;
import java.util.zip.Deflater;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ByteString;
import okio.DeflaterSink;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class Spdy3
implements Variant {
    static final byte[] DICTIONARY;
    static final int FLAG_FIN = 1;
    static final int FLAG_UNIDIRECTIONAL = 2;
    static final int TYPE_DATA = 0;
    static final int TYPE_GOAWAY = 7;
    static final int TYPE_HEADERS = 8;
    static final int TYPE_PING = 6;
    static final int TYPE_RST_STREAM = 3;
    static final int TYPE_SETTINGS = 4;
    static final int TYPE_SYN_REPLY = 2;
    static final int TYPE_SYN_STREAM = 1;
    static final int TYPE_WINDOW_UPDATE = 9;
    static final int VERSION = 3;

    static {
        try {
            DICTIONARY = "\u0000\u0000\u0000\u0007options\u0000\u0000\u0000\u0004head\u0000\u0000\u0000\u0004post\u0000\u0000\u0000\u0003put\u0000\u0000\u0000\u0006delete\u0000\u0000\u0000\u0005trace\u0000\u0000\u0000\u0006accept\u0000\u0000\u0000\u000eaccept-charset\u0000\u0000\u0000\u000faccept-encoding\u0000\u0000\u0000\u000faccept-language\u0000\u0000\u0000\raccept-ranges\u0000\u0000\u0000\u0003age\u0000\u0000\u0000\u0005allow\u0000\u0000\u0000\rauthorization\u0000\u0000\u0000\rcache-control\u0000\u0000\u0000\nconnection\u0000\u0000\u0000\fcontent-base\u0000\u0000\u0000\u0010content-encoding\u0000\u0000\u0000\u0010content-language\u0000\u0000\u0000\u000econtent-length\u0000\u0000\u0000\u0010content-location\u0000\u0000\u0000\u000bcontent-md5\u0000\u0000\u0000\rcontent-range\u0000\u0000\u0000\fcontent-type\u0000\u0000\u0000\u0004date\u0000\u0000\u0000\u0004etag\u0000\u0000\u0000\u0006expect\u0000\u0000\u0000\u0007expires\u0000\u0000\u0000\u0004from\u0000\u0000\u0000\u0004host\u0000\u0000\u0000\bif-match\u0000\u0000\u0000\u0011if-modified-since\u0000\u0000\u0000\rif-none-match\u0000\u0000\u0000\bif-range\u0000\u0000\u0000\u0013if-unmodified-since\u0000\u0000\u0000\rlast-modified\u0000\u0000\u0000\blocation\u0000\u0000\u0000\fmax-forwards\u0000\u0000\u0000\u0006pragma\u0000\u0000\u0000\u0012proxy-authenticate\u0000\u0000\u0000\u0013proxy-authorization\u0000\u0000\u0000\u0005range\u0000\u0000\u0000\u0007referer\u0000\u0000\u0000\u000bretry-after\u0000\u0000\u0000\u0006server\u0000\u0000\u0000\u0002te\u0000\u0000\u0000\u0007trailer\u0000\u0000\u0000\u0011transfer-encoding\u0000\u0000\u0000\u0007upgrade\u0000\u0000\u0000\nuser-agent\u0000\u0000\u0000\u0004vary\u0000\u0000\u0000\u0003via\u0000\u0000\u0000\u0007warning\u0000\u0000\u0000\u0010www-authenticate\u0000\u0000\u0000\u0006method\u0000\u0000\u0000\u0003get\u0000\u0000\u0000\u0006status\u0000\u0000\u0000\u0006200 OK\u0000\u0000\u0000\u0007version\u0000\u0000\u0000\bHTTP/1.1\u0000\u0000\u0000\u0003url\u0000\u0000\u0000\u0006public\u0000\u0000\u0000\nset-cookie\u0000\u0000\u0000\nkeep-alive\u0000\u0000\u0000\u0006origin100101201202205206300302303304305306307402405406407408409410411412413414415416417502504505203 Non-Authoritative Information204 No Content301 Moved Permanently400 Bad Request401 Unauthorized403 Forbidden404 Not Found500 Internal Server Error501 Not Implemented503 Service UnavailableJan Feb Mar Apr May Jun Jul Aug Sept Oct Nov Dec 00:00:00 Mon, Tue, Wed, Thu, Fri, Sat, Sun, GMTchunked,text/html,image/png,image/jpg,image/gif,application/xml,application/xhtml+xml,text/plain,text/javascript,publicprivatemax-age=gzip,deflate,sdchcharset=utf-8charset=iso-8859-1,utf-,*,enq=0.".getBytes(Util.UTF_8.name());
            return;
        }
        catch (UnsupportedEncodingException unsupportedEncodingException) {
            throw new AssertionError();
        }
    }

    @Override
    public Protocol getProtocol() {
        return Protocol.SPDY_3;
    }

    @Override
    public FrameReader newReader(BufferedSource bufferedSource, boolean bl) {
        return new Reader(bufferedSource, bl);
    }

    @Override
    public FrameWriter newWriter(BufferedSink bufferedSink, boolean bl) {
        return new Writer(bufferedSink, bl);
    }

    static final class Reader
    implements FrameReader {
        private final boolean client;
        private final NameValueBlockReader headerBlockReader;
        private final BufferedSource source;

        Reader(BufferedSource bufferedSource, boolean bl) {
            this.source = bufferedSource;
            this.headerBlockReader = new NameValueBlockReader(this.source);
            this.client = bl;
        }

        private static /* varargs */ IOException ioException(String string, Object ... arrobject) {
            throw new IOException(String.format((String)string, (Object[])arrobject));
        }

        private void readGoAway(FrameReader.Handler handler, int n2, int n3) {
            if (n3 != 8) {
                Object[] arrobject = new Object[]{n3};
                throw Reader.ioException("TYPE_GOAWAY length: %d != 8", arrobject);
            }
            int n4 = Integer.MAX_VALUE & this.source.readInt();
            int n5 = this.source.readInt();
            ErrorCode errorCode = ErrorCode.fromSpdyGoAway(n5);
            if (errorCode == null) {
                Object[] arrobject = new Object[]{n5};
                throw Reader.ioException("TYPE_GOAWAY unexpected error code: %d", arrobject);
            }
            handler.goAway(n4, errorCode, ByteString.EMPTY);
        }

        private void readHeaders(FrameReader.Handler handler, int n2, int n3) {
            handler.headers(false, false, Integer.MAX_VALUE & this.source.readInt(), -1, this.headerBlockReader.readNameValueBlock(n3 - 4), HeadersMode.SPDY_HEADERS);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readPing(FrameReader.Handler handler, int n2, int n3) {
            int n4 = 1;
            if (n3 != 4) {
                Object[] arrobject = new Object[n4];
                arrobject[0] = n3;
                throw Reader.ioException("TYPE_PING length: %d != 4", arrobject);
            }
            int n5 = this.client;
            int n6 = this.source.readInt();
            int n7 = (n6 & 1) == n4 ? n4 : 0;
            if (n5 != n7) {
                n4 = 0;
            }
            handler.ping((boolean)n4, n6, 0);
        }

        private void readRstStream(FrameReader.Handler handler, int n2, int n3) {
            if (n3 != 8) {
                Object[] arrobject = new Object[]{n3};
                throw Reader.ioException("TYPE_RST_STREAM length: %d != 8", arrobject);
            }
            int n4 = Integer.MAX_VALUE & this.source.readInt();
            int n5 = this.source.readInt();
            ErrorCode errorCode = ErrorCode.fromSpdy3Rst(n5);
            if (errorCode == null) {
                Object[] arrobject = new Object[]{n5};
                throw Reader.ioException("TYPE_RST_STREAM unexpected error code: %d", arrobject);
            }
            handler.rstStream(n4, errorCode);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readSettings(FrameReader.Handler handler, int n2, int n3) {
            boolean bl = true;
            int n4 = this.source.readInt();
            if (n3 != 4 + n4 * 8) {
                Object[] arrobject = new Object[2];
                arrobject[0] = n3;
                arrobject[bl] = n4;
                throw Reader.ioException("TYPE_SETTINGS length: %d != 4 + 8 * %d", arrobject);
            }
            Settings settings = new Settings();
            for (int i2 = 0; i2 < n4; ++i2) {
                int n5 = this.source.readInt();
                int n6 = this.source.readInt();
                int n7 = (-16777216 & n5) >>> 24;
                settings.set(n5 & 16777215, n7, n6);
            }
            if ((n2 & 1) == 0) {
                bl = false;
            }
            handler.settings(bl, settings);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readSynReply(FrameReader.Handler handler, int n2, int n3) {
            int n4 = Integer.MAX_VALUE & this.source.readInt();
            List<Header> list = this.headerBlockReader.readNameValueBlock(n3 - 4);
            boolean bl = (n2 & 1) != 0;
            handler.headers(false, bl, n4, -1, list, HeadersMode.SPDY_REPLY);
        }

        /*
         * Enabled aggressive block sorting
         */
        private void readSynStream(FrameReader.Handler handler, int n2, int n3) {
            boolean bl = true;
            int n4 = this.source.readInt();
            int n5 = this.source.readInt();
            int n6 = n4 & Integer.MAX_VALUE;
            int n7 = n5 & Integer.MAX_VALUE;
            this.source.readShort();
            List<Header> list = this.headerBlockReader.readNameValueBlock(n3 - 10);
            boolean bl2 = (n2 & 1) != 0 ? bl : false;
            if ((n2 & 2) == 0) {
                bl = false;
            }
            handler.headers(bl, bl2, n6, n7, list, HeadersMode.SPDY_SYN_STREAM);
        }

        private void readWindowUpdate(FrameReader.Handler handler, int n2, int n3) {
            if (n3 != 8) {
                Object[] arrobject = new Object[]{n3};
                throw Reader.ioException("TYPE_WINDOW_UPDATE length: %d != 8", arrobject);
            }
            int n4 = this.source.readInt();
            int n5 = this.source.readInt();
            int n6 = n4 & Integer.MAX_VALUE;
            long l2 = n5 & Integer.MAX_VALUE;
            if (l2 == 0L) {
                Object[] arrobject = new Object[]{l2};
                throw Reader.ioException("windowSizeIncrement was 0", arrobject);
            }
            handler.windowUpdate(n6, l2);
        }

        public void close() {
            this.headerBlockReader.close();
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public boolean nextFrame(FrameReader.Handler handler) {
            int n2;
            int n3;
            int n4;
            block14 : {
                int n5;
                try {
                    n2 = this.source.readInt();
                    n5 = this.source.readInt();
                }
                catch (IOException iOException) {
                    return false;
                }
                boolean bl = (Integer.MIN_VALUE & n2) != 0;
                n4 = (-16777216 & n5) >>> 24;
                n3 = n5 & 16777215;
                if (!bl) break block14;
                int n6 = (2147418112 & n2) >>> 16;
                int n7 = 65535 & n2;
                if (n6 != 3) {
                    throw new ProtocolException("version != 3: " + n6);
                }
                switch (n7) {
                    default: {
                        this.source.skip(n3);
                        return true;
                    }
                    case 1: {
                        this.readSynStream(handler, n4, n3);
                        return true;
                    }
                    case 2: {
                        this.readSynReply(handler, n4, n3);
                        return true;
                    }
                    case 3: {
                        this.readRstStream(handler, n4, n3);
                        return true;
                    }
                    case 4: {
                        this.readSettings(handler, n4, n3);
                        return true;
                    }
                    case 6: {
                        this.readPing(handler, n4, n3);
                        return true;
                    }
                    case 7: {
                        this.readGoAway(handler, n4, n3);
                        return true;
                    }
                    case 8: {
                        this.readHeaders(handler, n4, n3);
                        return true;
                    }
                    case 9: 
                }
                this.readWindowUpdate(handler, n4, n3);
                return true;
            }
            int n8 = Integer.MAX_VALUE & n2;
            int n9 = n4 & 1;
            boolean bl = false;
            if (n9 != 0) {
                bl = true;
            }
            handler.data(bl, n8, this.source, n3);
            return true;
        }

        @Override
        public void readConnectionPreface() {
        }
    }

    static final class Writer
    implements FrameWriter {
        private final boolean client;
        private boolean closed;
        private final Buffer headerBlockBuffer;
        private final BufferedSink headerBlockOut;
        private final BufferedSink sink;

        Writer(BufferedSink bufferedSink, boolean bl) {
            this.sink = bufferedSink;
            this.client = bl;
            Deflater deflater = new Deflater();
            deflater.setDictionary(Spdy3.DICTIONARY);
            this.headerBlockBuffer = new Buffer();
            this.headerBlockOut = Okio.buffer(new DeflaterSink((Sink)this.headerBlockBuffer, deflater));
        }

        private void writeNameValueBlockToBuffer(List<Header> list) {
            if (this.headerBlockBuffer.size() != 0L) {
                throw new IllegalStateException();
            }
            this.headerBlockOut.writeInt(list.size());
            int n2 = list.size();
            for (int i2 = 0; i2 < n2; ++i2) {
                ByteString byteString = ((Header)list.get((int)i2)).name;
                this.headerBlockOut.writeInt(byteString.size());
                this.headerBlockOut.write(byteString);
                ByteString byteString2 = ((Header)list.get((int)i2)).value;
                this.headerBlockOut.writeInt(byteString2.size());
                this.headerBlockOut.write(byteString2);
            }
            this.headerBlockOut.flush();
        }

        @Override
        public void ackSettings(Settings settings) {
        }

        public void close() {
            Writer writer = this;
            synchronized (writer) {
                this.closed = true;
                Util.closeAll(this.sink, this.headerBlockOut);
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Converted monitor instructions to comments
         * Lifted jumps to return sites
         */
        @Override
        public void connectionPreface() {
            Writer writer = this;
            // MONITORENTER : writer
            // MONITOREXIT : writer
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
                int n4 = bl ? 1 : 0;
                this.sendDataFrame(n2, n4, buffer, n3);
                return;
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
                if (errorCode.spdyGoAwayCode == -1) {
                    throw new IllegalArgumentException("errorCode.spdyGoAwayCode == -1");
                }
                this.sink.writeInt(-2147287033);
                this.sink.writeInt(8);
                this.sink.writeInt(n2);
                this.sink.writeInt(errorCode.spdyGoAwayCode);
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
                this.writeNameValueBlockToBuffer(list);
                int n3 = (int)(4L + this.headerBlockBuffer.size());
                this.sink.writeInt(-2147287032);
                this.sink.writeInt(0 | n3 & 16777215);
                this.sink.writeInt(Integer.MAX_VALUE & n2);
                this.sink.writeAll(this.headerBlockBuffer);
                return;
            }
        }

        @Override
        public int maxDataLength() {
            return 16383;
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void ping(boolean n2, int n3, int n4) {
            int n5 = 1;
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                int n6 = this.client;
                int n7 = (n3 & 1) == n5 ? n5 : 0;
                if (n6 == n7) {
                    n5 = 0;
                }
                if (n2 != n5) {
                    throw new IllegalArgumentException("payload != reply");
                }
                this.sink.writeInt(-2147287034);
                this.sink.writeInt(4);
                this.sink.writeInt(n3);
                this.sink.flush();
                return;
            }
        }

        @Override
        public void pushPromise(int n2, int n3, List<Header> list) {
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
                this.sink.writeInt(-2147287037);
                this.sink.writeInt(8);
                this.sink.writeInt(Integer.MAX_VALUE & n2);
                this.sink.writeInt(errorCode.spdyRstCode);
                this.sink.flush();
                return;
            }
        }

        void sendDataFrame(int n2, int n3, Buffer buffer, int n4) {
            if (this.closed) {
                throw new IOException("closed");
            }
            if ((long)n4 > 0xFFFFFFL) {
                throw new IllegalArgumentException("FRAME_TOO_LARGE max size is 16Mib: " + n4);
            }
            this.sink.writeInt(Integer.MAX_VALUE & n2);
            this.sink.writeInt((n3 & 255) << 24 | 16777215 & n4);
            if (n4 > 0) {
                this.sink.write(buffer, (long)n4);
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         * Enabled aggressive exception aggregation
         */
        @Override
        public void settings(Settings settings) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                int n2 = settings.size();
                int n3 = 4 + n2 * 8;
                this.sink.writeInt(-2147287036);
                this.sink.writeInt(0 | n3 & 16777215);
                this.sink.writeInt(n2);
                int n4 = 0;
                do {
                    if (n4 > 10) {
                        this.sink.flush();
                        return;
                    }
                    if (settings.isSet(n4)) {
                        int n5 = settings.flags(n4);
                        this.sink.writeInt((n5 & 255) << 24 | n4 & 16777215);
                        this.sink.writeInt(settings.get(n4));
                    }
                    ++n4;
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
                this.writeNameValueBlockToBuffer(list);
                int n3 = bl ? 1 : 0;
                int n4 = (int)(4L + this.headerBlockBuffer.size());
                this.sink.writeInt(-2147287038);
                this.sink.writeInt((n3 & 255) << 24 | n4 & 16777215);
                this.sink.writeInt(Integer.MAX_VALUE & n2);
                this.sink.writeAll(this.headerBlockBuffer);
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
        public void synStream(boolean bl, boolean bl2, int n2, int n3, List<Header> list) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                this.writeNameValueBlockToBuffer(list);
                int n4 = (int)(10L + this.headerBlockBuffer.size());
                int n5 = bl ? 1 : 0;
                int n6 = 0;
                if (bl2) {
                    n6 = 2;
                }
                int n7 = n6 | n5;
                this.sink.writeInt(-2147287039);
                this.sink.writeInt((n7 & 255) << 24 | n4 & 16777215);
                this.sink.writeInt(n2 & Integer.MAX_VALUE);
                this.sink.writeInt(n3 & Integer.MAX_VALUE);
                this.sink.writeShort(0);
                this.sink.writeAll(this.headerBlockBuffer);
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
        public void windowUpdate(int n2, long l2) {
            Writer writer = this;
            synchronized (writer) {
                if (this.closed) {
                    throw new IOException("closed");
                }
                if (l2 != 0L && l2 <= Integer.MAX_VALUE) {
                    this.sink.writeInt(-2147287031);
                    this.sink.writeInt(8);
                    this.sink.writeInt(n2);
                    this.sink.writeInt((int)l2);
                    this.sink.flush();
                    return;
                }
                throw new IllegalArgumentException("windowSizeIncrement must be between 1 and 0x7fffffff: " + l2);
            }
        }
    }

}

