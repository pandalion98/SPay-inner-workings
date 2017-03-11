package com.samsung.contextservice.server;

import com.samsung.android.spayfw.remoteservice.Request.C0413a;

/* renamed from: com.samsung.contextservice.server.i */
public class RequestBundle<T> {
    private C0413a GY;
    private T request;

    public RequestBundle(T t, C0413a c0413a) {
        this.request = t;
        this.GY = c0413a;
    }

    public T getRequest() {
        return this.request;
    }

    public C0413a gz() {
        return this.GY;
    }
}
