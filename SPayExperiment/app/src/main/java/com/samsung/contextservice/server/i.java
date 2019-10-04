/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 */
package com.samsung.contextservice.server;

import com.samsung.android.spayfw.remoteservice.Request;

public class i<T> {
    private Request.a GY;
    private T request;

    public i(T t2, Request.a a2) {
        this.request = t2;
        this.GY = a2;
    }

    public T getRequest() {
        return this.request;
    }

    public Request.a gz() {
        return this.GY;
    }
}

