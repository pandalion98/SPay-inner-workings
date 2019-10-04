/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.RetryableSink;
import okio.Sink;

public interface Transport {
    public static final int DISCARD_STREAM_TIMEOUT_MILLIS = 100;

    public boolean canReuseConnection();

    public Sink createRequestBody(Request var1, long var2);

    public void disconnect(HttpEngine var1);

    public void finishRequest();

    public ResponseBody openResponseBody(Response var1);

    public Response.Builder readResponseHeaders();

    public void releaseConnectionOnIdle();

    public void writeRequestBody(RetryableSink var1);

    public void writeRequestHeaders(Request var1);
}

