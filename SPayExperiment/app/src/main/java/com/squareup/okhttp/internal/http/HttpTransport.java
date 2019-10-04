/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.Proxy
 *  java.net.Proxy$Type
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Connection;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.http.HttpConnection;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.OkHeaders;
import com.squareup.okhttp.internal.http.RealResponseBody;
import com.squareup.okhttp.internal.http.RequestLine;
import com.squareup.okhttp.internal.http.RetryableSink;
import com.squareup.okhttp.internal.http.Transport;
import java.net.Proxy;
import okio.BufferedSource;
import okio.Okio;
import okio.Sink;
import okio.Source;

public final class HttpTransport
implements Transport {
    private final HttpConnection httpConnection;
    private final HttpEngine httpEngine;

    public HttpTransport(HttpEngine httpEngine, HttpConnection httpConnection) {
        this.httpEngine = httpEngine;
        this.httpConnection = httpConnection;
    }

    private Source getTransferStream(Response response) {
        if (!HttpEngine.hasBody(response)) {
            return this.httpConnection.newFixedLengthSource(0L);
        }
        if ("chunked".equalsIgnoreCase(response.header("Transfer-Encoding"))) {
            return this.httpConnection.newChunkedSource(this.httpEngine);
        }
        long l2 = OkHeaders.contentLength(response);
        if (l2 != -1L) {
            return this.httpConnection.newFixedLengthSource(l2);
        }
        return this.httpConnection.newUnknownLengthSource();
    }

    /*
     * Enabled aggressive block sorting
     */
    @Override
    public boolean canReuseConnection() {
        return !"close".equalsIgnoreCase(this.httpEngine.getRequest().header("Connection")) && !"close".equalsIgnoreCase(this.httpEngine.getResponse().header("Connection")) && !this.httpConnection.isClosed();
    }

    @Override
    public Sink createRequestBody(Request request, long l2) {
        if ("chunked".equalsIgnoreCase(request.header("Transfer-Encoding"))) {
            return this.httpConnection.newChunkedSink();
        }
        if (l2 != -1L) {
            return this.httpConnection.newFixedLengthSink(l2);
        }
        throw new IllegalStateException("Cannot stream a request body without chunked encoding or a known content length!");
    }

    @Override
    public void disconnect(HttpEngine httpEngine) {
        this.httpConnection.closeIfOwnedBy(httpEngine);
    }

    @Override
    public void finishRequest() {
        this.httpConnection.flush();
    }

    @Override
    public ResponseBody openResponseBody(Response response) {
        Source source = this.getTransferStream(response);
        return new RealResponseBody(response.headers(), Okio.buffer(source));
    }

    @Override
    public Response.Builder readResponseHeaders() {
        return this.httpConnection.readResponse();
    }

    @Override
    public void releaseConnectionOnIdle() {
        if (this.canReuseConnection()) {
            this.httpConnection.poolOnIdle();
            return;
        }
        this.httpConnection.closeOnIdle();
    }

    @Override
    public void writeRequestBody(RetryableSink retryableSink) {
        this.httpConnection.writeRequestBody(retryableSink);
    }

    @Override
    public void writeRequestHeaders(Request request) {
        this.httpEngine.writingRequestHeaders();
        String string = RequestLine.get(request, this.httpEngine.getConnection().getRoute().getProxy().type(), this.httpEngine.getConnection().getProtocol());
        this.httpConnection.writeRequest(request.headers(), string);
    }
}

