package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Response.Builder;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.spdy.ErrorCode;
import com.squareup.okhttp.internal.spdy.Header;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import com.squareup.okhttp.internal.spdy.SpdyStream;
import java.net.ProtocolException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import okio.ByteString;
import okio.Okio;
import okio.Sink;

public final class SpdyTransport implements Transport {
    private static final List<ByteString> HTTP_2_PROHIBITED_HEADERS;
    private static final List<ByteString> SPDY_3_PROHIBITED_HEADERS;
    private final HttpEngine httpEngine;
    private final SpdyConnection spdyConnection;
    private SpdyStream stream;

    static {
        SPDY_3_PROHIBITED_HEADERS = Util.immutableList(ByteString.encodeUtf8("connection"), ByteString.encodeUtf8("host"), ByteString.encodeUtf8("keep-alive"), ByteString.encodeUtf8("proxy-connection"), ByteString.encodeUtf8("transfer-encoding"));
        HTTP_2_PROHIBITED_HEADERS = Util.immutableList(ByteString.encodeUtf8("connection"), ByteString.encodeUtf8("host"), ByteString.encodeUtf8("keep-alive"), ByteString.encodeUtf8("proxy-connection"), ByteString.encodeUtf8("te"), ByteString.encodeUtf8("transfer-encoding"), ByteString.encodeUtf8("encoding"), ByteString.encodeUtf8("upgrade"));
    }

    public SpdyTransport(HttpEngine httpEngine, SpdyConnection spdyConnection) {
        this.httpEngine = httpEngine;
        this.spdyConnection = spdyConnection;
    }

    public Sink createRequestBody(Request request, long j) {
        return this.stream.getSink();
    }

    public void writeRequestHeaders(Request request) {
        if (this.stream == null) {
            this.httpEngine.writingRequestHeaders();
            this.stream = this.spdyConnection.newStream(writeNameValueBlock(request, this.spdyConnection.getProtocol(), RequestLine.version(this.httpEngine.getConnection().getProtocol())), this.httpEngine.permitsRequestBody(), true);
            this.stream.readTimeout().timeout((long) this.httpEngine.client.getReadTimeout(), TimeUnit.MILLISECONDS);
        }
    }

    public void writeRequestBody(RetryableSink retryableSink) {
        retryableSink.writeToSocket(this.stream.getSink());
    }

    public void finishRequest() {
        this.stream.getSink().close();
    }

    public Builder readResponseHeaders() {
        return readNameValueBlock(this.stream.getResponseHeaders(), this.spdyConnection.getProtocol());
    }

    public static List<Header> writeNameValueBlock(Request request, Protocol protocol, String str) {
        Headers headers = request.headers();
        List<Header> arrayList = new ArrayList(headers.size() + 10);
        arrayList.add(new Header(Header.TARGET_METHOD, request.method()));
        arrayList.add(new Header(Header.TARGET_PATH, RequestLine.requestPath(request.url())));
        String hostHeader = HttpEngine.hostHeader(request.url());
        if (Protocol.SPDY_3 == protocol) {
            arrayList.add(new Header(Header.VERSION, str));
            arrayList.add(new Header(Header.TARGET_HOST, hostHeader));
        } else if (Protocol.HTTP_2 == protocol) {
            arrayList.add(new Header(Header.TARGET_AUTHORITY, hostHeader));
        } else {
            throw new AssertionError();
        }
        arrayList.add(new Header(Header.TARGET_SCHEME, request.url().getProtocol()));
        Set linkedHashSet = new LinkedHashSet();
        int size = headers.size();
        for (int i = 0; i < size; i++) {
            ByteString encodeUtf8 = ByteString.encodeUtf8(headers.name(i).toLowerCase(Locale.US));
            String value = headers.value(i);
            if (!(isProhibitedHeader(protocol, encodeUtf8) || encodeUtf8.equals(Header.TARGET_METHOD) || encodeUtf8.equals(Header.TARGET_PATH) || encodeUtf8.equals(Header.TARGET_SCHEME) || encodeUtf8.equals(Header.TARGET_AUTHORITY) || encodeUtf8.equals(Header.TARGET_HOST) || encodeUtf8.equals(Header.VERSION))) {
                if (linkedHashSet.add(encodeUtf8)) {
                    arrayList.add(new Header(encodeUtf8, value));
                } else {
                    for (int i2 = 0; i2 < arrayList.size(); i2++) {
                        if (((Header) arrayList.get(i2)).name.equals(encodeUtf8)) {
                            arrayList.set(i2, new Header(encodeUtf8, joinOnNull(((Header) arrayList.get(i2)).value.utf8(), value)));
                            break;
                        }
                    }
                }
            }
        }
        return arrayList;
    }

    private static String joinOnNull(String str, String str2) {
        return '\u0000' + str2;
    }

    public static Builder readNameValueBlock(List<Header> list, Protocol protocol) {
        String str = null;
        String str2 = "HTTP/1.1";
        Headers.Builder builder = new Headers.Builder();
        builder.set(OkHeaders.SELECTED_PROTOCOL, protocol.toString());
        int size = list.size();
        int i = 0;
        while (i < size) {
            ByteString byteString = ((Header) list.get(i)).name;
            String utf8 = ((Header) list.get(i)).value.utf8();
            String str3 = str2;
            int i2 = 0;
            while (i2 < utf8.length()) {
                int indexOf = utf8.indexOf(0, i2);
                if (indexOf == -1) {
                    indexOf = utf8.length();
                }
                str2 = utf8.substring(i2, indexOf);
                if (!byteString.equals(Header.RESPONSE_STATUS)) {
                    if (byteString.equals(Header.VERSION)) {
                        str3 = str2;
                        str2 = str;
                    } else {
                        if (!isProhibitedHeader(protocol, byteString)) {
                            builder.add(byteString.utf8(), str2);
                        }
                        str2 = str;
                    }
                }
                str = str2;
                i2 = indexOf + 1;
            }
            i++;
            str2 = str3;
        }
        if (str == null) {
            throw new ProtocolException("Expected ':status' header not present");
        }
        StatusLine parse = StatusLine.parse(str2 + " " + str);
        return new Builder().protocol(protocol).code(parse.code).message(parse.message).headers(builder.build());
    }

    public ResponseBody openResponseBody(Response response) {
        return new RealResponseBody(response.headers(), Okio.buffer(this.stream.getSource()));
    }

    public void releaseConnectionOnIdle() {
    }

    public void disconnect(HttpEngine httpEngine) {
        if (this.stream != null) {
            this.stream.close(ErrorCode.CANCEL);
        }
    }

    public boolean canReuseConnection() {
        return true;
    }

    private static boolean isProhibitedHeader(Protocol protocol, ByteString byteString) {
        if (protocol == Protocol.SPDY_3) {
            return SPDY_3_PROHIBITED_HEADERS.contains(byteString);
        }
        if (protocol == Protocol.HTTP_2) {
            return HTTP_2_PROHIBITED_HEADERS.contains(byteString);
        }
        throw new AssertionError(protocol);
    }
}
