/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.net.Proxy
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import java.net.Proxy;

public interface Authenticator {
    public Request authenticate(Proxy var1, Response var2);

    public Request authenticateProxy(Proxy var1, Response var2);
}

