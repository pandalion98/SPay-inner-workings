/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.squareup.okhttp.internal.http;

import okio.Sink;

public interface CacheRequest {
    public void abort();

    public Sink body();
}

