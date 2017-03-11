package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.Response.Builder;
import com.squareup.okhttp.ResponseBody;
import okio.Sink;

public interface Transport {
    public static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

    boolean canReuseConnection();

    Sink createRequestBody(Request request, long j);

    void disconnect(HttpEngine httpEngine);

    void finishRequest();

    ResponseBody openResponseBody(Response response);

    Builder readResponseHeaders();

    void releaseConnectionOnIdle();

    void writeRequestBody(RetryableSink retryableSink);

    void writeRequestHeaders(Request request);
}
