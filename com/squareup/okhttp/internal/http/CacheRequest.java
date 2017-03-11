package com.squareup.okhttp.internal.http;

import okio.Sink;

public interface CacheRequest {
    void abort();

    Sink body();
}
