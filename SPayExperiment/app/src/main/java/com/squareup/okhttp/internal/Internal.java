/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.Object
 *  java.lang.String
 *  java.util.logging.Logger
 *  javax.net.ssl.SSLSocket
 */
package com.squareup.okhttp.internal;

import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.internal.InternalCache;
import com.squareup.okhttp.internal.Network;
import com.squareup.okhttp.internal.RouteDatabase;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.Transport;
import java.util.logging.Logger;
import javax.net.ssl.SSLSocket;
import okio.BufferedSink;
import okio.BufferedSource;

public abstract class Internal {
    public static Internal instance;
    public static final Logger logger;

    static {
        logger = Logger.getLogger((String)OkHttpClient.class.getName());
    }

    public static void initializeInstanceForTests() {
        new OkHttpClient();
    }

    public abstract void addLenient(Headers.Builder var1, String var2);

    public abstract void addLenient(Headers.Builder var1, String var2, String var3);

    public abstract void apply(ConnectionSpec var1, SSLSocket var2, boolean var3);

    public abstract Connection callEngineGetConnection(Call var1);

    public abstract void callEngineReleaseConnection(Call var1);

    public abstract void callEnqueue(Call var1, Callback var2, boolean var3);

    public abstract boolean clearOwner(Connection var1);

    public abstract void closeIfOwnedBy(Connection var1, Object var2);

    public abstract void connectAndSetOwner(OkHttpClient var1, Connection var2, HttpEngine var3, Request var4);

    public abstract BufferedSink connectionRawSink(Connection var1);

    public abstract BufferedSource connectionRawSource(Connection var1);

    public abstract void connectionSetOwner(Connection var1, Object var2);

    public abstract InternalCache internalCache(OkHttpClient var1);

    public abstract boolean isReadable(Connection var1);

    public abstract Network network(OkHttpClient var1);

    public abstract Transport newTransport(Connection var1, HttpEngine var2);

    public abstract void recycle(ConnectionPool var1, Connection var2);

    public abstract int recycleCount(Connection var1);

    public abstract RouteDatabase routeDatabase(OkHttpClient var1);

    public abstract void setCache(OkHttpClient var1, InternalCache var2);

    public abstract void setNetwork(OkHttpClient var1, Network var2);

    public abstract void setOwner(Connection var1, HttpEngine var2);

    public abstract void setProtocol(Connection var1, Protocol var2);
}

