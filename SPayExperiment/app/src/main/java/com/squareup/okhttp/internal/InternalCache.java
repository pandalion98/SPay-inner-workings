/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.squareup.okhttp.internal;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.okhttp.internal.http.CacheRequest;
import com.squareup.okhttp.internal.http.CacheStrategy;

public interface InternalCache {
    public Response get(Request var1);

    public CacheRequest put(Response var1);

    public void remove(Request var1);

    public void trackConditionalCacheHit();

    public void trackResponse(CacheStrategy var1);

    public void update(Response var1, Response var2);
}

