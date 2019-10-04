/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.String
 */
package com.squareup.okhttp.internal.http;

import com.squareup.okhttp.Headers;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.ResponseBody;
import com.squareup.okhttp.internal.http.OkHeaders;
import okio.BufferedSource;

public final class RealResponseBody
extends ResponseBody {
    private final Headers headers;
    private final BufferedSource source;

    public RealResponseBody(Headers headers, BufferedSource bufferedSource) {
        this.headers = headers;
        this.source = bufferedSource;
    }

    @Override
    public long contentLength() {
        return OkHeaders.contentLength(this.headers);
    }

    @Override
    public MediaType contentType() {
        String string = this.headers.get("Content-Type");
        if (string != null) {
            return MediaType.parse(string);
        }
        return null;
    }

    @Override
    public BufferedSource source() {
        return this.source;
    }
}

