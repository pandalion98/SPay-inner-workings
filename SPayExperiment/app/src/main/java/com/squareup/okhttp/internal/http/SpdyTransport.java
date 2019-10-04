/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.AssertionError
 *  java.lang.Object
 *  java.lang.String
 *  java.net.ProtocolException
 *  java.net.URL
 *  java.util.ArrayList
 *  java.util.LinkedHashSet
 *  java.util.List
 *  java.util.Locale
 *  java.util.concurrent.TimeUnit
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.RealResponseBody;
import com.squareup.okhttp.internal.http.RequestLine;
import com.squareup.okhttp.internal.http.RetryableSink;
import com.squareup.okhttp.internal.http.StatusLine;
import com.squareup.okhttp.internal.http.Transport;
import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import com.squareup.okhttp.internal.spdy.SpdyStream;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;
import okio.BufferedSource;
import okio.ByteString;
import okio.Okio;
import okio.Sink;
import okio.Source;
import okio.Timeout;

public final class SpdyTransport
implements Transport {
    private static final List<ByteString> HTTP_2_PROHIBITED_HEADERS;
    private static final List<ByteString> SPDY_3_PROHIBITED_HEADERS;
    private final HttpEngine httpEngine;
    private final SpdyConnection spdyConnection;
    private SpdyStream stream;

    static {
        ByteString[] arrbyteString = new ByteString[]{ByteString.encodeUtf8("connection"), ByteString.encodeUtf8("host"), ByteString.encodeUtf8("keep-alive"), ByteString.encodeUtf8("proxy-connection"), ByteString.encodeUtf8("transfer-encoding")};
        SPDY_3_PROHIBITED_HEADERS = Util.immutableList(arrbyteString);
        ByteString[] arrbyteString2 = new ByteString[]{ByteString.encodeUtf8("connection"), ByteString.encodeUtf8("host"), ByteString.encodeUtf8("keep-alive"), ByteString.encodeUtf8("proxy-connection"), ByteString.encodeUtf8("te"), ByteString.encodeUtf8("transfer-encoding"), ByteString.encodeUtf8("encoding"), ByteString.encodeUtf8("upgrade")};
        HTTP_2_PROHIBITED_HEADERS = Util.immutableList(arrbyteString2);
    }

    public SpdyTransport(HttpEngine httpEngine, SpdyConnection spdyConnection) {
        this.httpEngine = httpEngine;
        this.spdyConnection = spdyConnection;
    }

    private static boolean isProhibitedHeader(Protocol protocol, ByteString byteString) {
        if (protocol == Protocol.SPDY_3) {
            return SPDY_3_PROHIBITED_HEADERS.contains((Object)byteString);
        }
        if (protocol == Protocol.HTTP_2) {
            return HTTP_2_PROHIBITED_HEADERS.contains((Object)byteString);
        }
        throw new AssertionError((Object)protocol);
    }

    private static String joinOnNull(String string, String string2) {
        return string + '\u0000' + string2;
    }

    /*
     * Enabled aggressive block sorting
     */
    public static Response.Builder readNameValueBlock(List<Header> list, Protocol protocol) {
        String string = null;
        String string2 = "HTTP/1.1";
        Headers.Builder builder = new Headers.Builder();
        builder.set(OkHeaders.SELECTED_PROTOCOL, protocol.toString());
        int n2 = list.size();
        int n3 = 0;
        do {
            String string3;
            ByteString byteString;
            int n4;
            String string4;
            if (n3 < n2) {
                byteString = ((Header)list.get((int)n3)).name;
                string3 = ((Header)list.get((int)n3)).value.utf8();
                string4 = string2;
                n4 = 0;
            } else {
                if (string == null) {
                    throw new ProtocolException("Expected ':status' header not present");
                }
                StatusLine statusLine = StatusLine.parse(string2 + " " + string);
                return new Response.Builder().protocol(protocol).code(statusLine.code).message(statusLine.message).headers(builder.build());
            }
            while (n4 < string3.length()) {
                int n5 = string3.indexOf(0, n4);
                if (n5 == -1) {
                    n5 = string3.length();
                }
                String string5 = string3.substring(n4, n5);
                if (!byteString.equals(Header.RESPONSE_STATUS)) {
                    if (byteString.equals(Header.VERSION)) {
                        string4 = string5;
                        string5 = string;
                    } else {
                        if (!SpdyTransport.isProhibitedHeader(protocol, byteString)) {
                            builder.add(byteString.utf8(), string5);
                        }
                        string5 = string;
                    }
                }
                int n6 = n5 + 1;
                string = string5;
                n4 = n6;
            }
            ++n3;
            string2 = string4;
        } while (true);
    }

    /*
     * Unable to fully structure code
     * Enabled aggressive block sorting
     * Lifted jumps to return sites
     */
    public static List<Header> writeNameValueBlock(Request var0, Protocol var1_1, String var2_2) {
        var3_3 = var0.headers();
        var4_4 = new ArrayList(10 + var3_3.size());
        var4_4.add((Object)new Header(Header.TARGET_METHOD, var0.method()));
        var4_4.add((Object)new Header(Header.TARGET_PATH, RequestLine.requestPath(var0.url())));
        var7_5 = HttpEngine.hostHeader(var0.url());
        if (Protocol.SPDY_3 == var1_1) {
            var4_4.add((Object)new Header(Header.VERSION, var2_2));
            var4_4.add((Object)new Header(Header.TARGET_HOST, var7_5));
        } else {
            if (Protocol.HTTP_2 != var1_1) throw new AssertionError();
            var4_4.add((Object)new Header(Header.TARGET_AUTHORITY, var7_5));
        }
        var4_4.add((Object)new Header(Header.TARGET_SCHEME, var0.url().getProtocol()));
        var10_6 = new LinkedHashSet();
        var11_7 = var3_3.size();
        var12_8 = 0;
        block0 : do {
            block9 : {
                if (var12_8 >= var11_7) return var4_4;
                var13_9 = ByteString.encodeUtf8(var3_3.name(var12_8).toLowerCase(Locale.US));
                var14_10 = var3_3.value(var12_8);
                if (SpdyTransport.isProhibitedHeader(var1_1, var13_9) || var13_9.equals(Header.TARGET_METHOD) || var13_9.equals(Header.TARGET_PATH) || var13_9.equals(Header.TARGET_SCHEME) || var13_9.equals(Header.TARGET_AUTHORITY) || var13_9.equals(Header.TARGET_HOST) || var13_9.equals(Header.VERSION)) ** GOTO lbl28
                if (!var10_6.add((Object)var13_9)) break block9;
                var4_4.add((Object)new Header(var13_9, var14_10));
                ** GOTO lbl28
            }
            var15_11 = 0;
            do {
                block10 : {
                    if (var15_11 < var4_4.size()) break block10;
lbl28: // 4 sources:
                    do {
                        ++var12_8;
                        continue block0;
                        break;
                    } while (true);
                }
                if (((Header)var4_4.get((int)var15_11)).name.equals(var13_9)) {
                    var4_4.set(var15_11, (Object)new Header(var13_9, SpdyTransport.joinOnNull(((Header)var4_4.get((int)var15_11)).value.utf8(), var14_10)));
                    ** continue;
                }
                ++var15_11;
            } while (true);
            break;
        } while (true);
    }

    @Override
    public boolean canReuseConnection() {
        return true;
    }

    @Override
    public Sink createRequestBody(Request request, long l2) {
        return this.stream.getSink();
    }

    @Override
    public void disconnect(HttpEngine httpEngine) {
        if (this.stream != null) {
            this.stream.close(ErrorCode.CANCEL);
        }
    }

    @Override
    public void finishRequest() {
        this.stream.getSink().close();
    }

    @Override
    public ResponseBody openResponseBody(Response response) {
        return new RealResponseBody(response.headers(), Okio.buffer(this.stream.getSource()));
    }

    @Override
    public Response.Builder readResponseHeaders() {
        return SpdyTransport.readNameValueBlock(this.stream.getResponseHeaders(), this.spdyConnection.getProtocol());
    }

    @Override
    public void releaseConnectionOnIdle() {
    }

    @Override
    public void writeRequestBody(RetryableSink retryableSink) {
        retryableSink.writeToSocket(this.stream.getSink());
    }

    @Override
    public void writeRequestHeaders(Request request) {
        if (this.stream != null) {
            return;
        }
        this.httpEngine.writingRequestHeaders();
        boolean bl = this.httpEngine.permitsRequestBody();
        String string = RequestLine.version(this.httpEngine.getConnection().getProtocol());
        this.stream = this.spdyConnection.newStream(SpdyTransport.writeNameValueBlock(request, this.spdyConnection.getProtocol(), string), bl, true);
        this.stream.readTimeout().timeout(this.httpEngine.client.getReadTimeout(), TimeUnit.MILLISECONDS);
    }
}

