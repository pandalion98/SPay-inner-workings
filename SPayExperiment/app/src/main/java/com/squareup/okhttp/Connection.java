/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.io.IOException
 *  java.lang.IllegalArgumentException
 *  java.lang.IllegalStateException
 *  java.lang.Object
 *  java.lang.String
 *  java.lang.StringBuilder
 *  java.lang.System
 *  java.lang.UnsupportedOperationException
 *  java.net.InetAddress
 *  java.net.InetSocketAddress
 *  java.net.Proxy
 *  java.net.Socket
 *  java.net.UnknownServiceException
 *  java.util.List
 *  javax.net.ssl.SSLSocketFactory
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Address;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Handshake;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Route;
import com.squareup.okhttp.internal.RouteDatabase;
import com.squareup.okhttp.internal.http.HttpConnection;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.HttpTransport;
import com.squareup.okhttp.internal.http.RouteException;
import com.squareup.okhttp.internal.http.SocketConnector;
import com.squareup.okhttp.internal.http.SpdyTransport;
import com.squareup.okhttp.internal.http.Transport;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.net.UnknownServiceException;
import java.util.List;
import javax.net.ssl.SSLSocketFactory;
import okio.BufferedSink;
import okio.BufferedSource;

public final class Connection {
    private boolean connected = false;
    private Handshake handshake;
    private HttpConnection httpConnection;
    private long idleStartTimeNs;
    private Object owner;
    private final ConnectionPool pool;
    private Protocol protocol = Protocol.HTTP_1_1;
    private int recycleCount;
    private final Route route;
    private Socket socket;
    private SpdyConnection spdyConnection;

    public Connection(ConnectionPool connectionPool, Route route) {
        this.pool = connectionPool;
        this.route = route;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    boolean clearOwner() {
        ConnectionPool connectionPool;
        ConnectionPool connectionPool2 = connectionPool = this.pool;
        synchronized (connectionPool2) {
            if (this.owner == null) {
                return false;
            }
            this.owner = null;
            return true;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void closeIfOwnedBy(Object object) {
        ConnectionPool connectionPool;
        if (this.isSpdy()) {
            throw new IllegalStateException();
        }
        ConnectionPool connectionPool2 = connectionPool = this.pool;
        synchronized (connectionPool2) {
            if (this.owner != object) {
                return;
            }
            this.owner = null;
        }
        this.socket.close();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void connect(int n2, int n3, int n4, Request request, List<ConnectionSpec> list, boolean bl) {
        SocketConnector.ConnectedSocket connectedSocket;
        if (this.connected) {
            throw new IllegalStateException("already connected");
        }
        SocketConnector socketConnector = new SocketConnector(this, this.pool);
        if (this.route.address.getSslSocketFactory() != null) {
            connectedSocket = socketConnector.connectTls(n2, n3, n4, request, this.route, list, bl);
        } else {
            if (!list.contains((Object)ConnectionSpec.CLEARTEXT)) {
                throw new RouteException((IOException)new UnknownServiceException("CLEARTEXT communication not supported: " + list));
            }
            connectedSocket = socketConnector.connectCleartext(n2, n3, this.route);
        }
        this.socket = connectedSocket.socket;
        this.handshake = connectedSocket.handshake;
        Protocol protocol = connectedSocket.alpnProtocol == null ? Protocol.HTTP_1_1 : connectedSocket.alpnProtocol;
        this.protocol = protocol;
        try {
            if (this.protocol != Protocol.SPDY_3 && this.protocol != Protocol.HTTP_2) {
                this.httpConnection = new HttpConnection(this.pool, this, this.socket);
            }
            this.socket.setSoTimeout(0);
            this.spdyConnection = new SpdyConnection.Builder(this.route.address.uriHost, true, this.socket).protocol(this.protocol).build();
            this.spdyConnection.sendConnectionPreface();
        }
        catch (IOException iOException) {
            throw new RouteException(iOException);
        }
        this.connected = true;
    }

    void connectAndSetOwner(OkHttpClient okHttpClient, Object object, Request request) {
        this.setOwner(object);
        if (!this.isConnected()) {
            List<ConnectionSpec> list = this.route.address.getConnectionSpecs();
            this.connect(okHttpClient.getConnectTimeout(), okHttpClient.getReadTimeout(), okHttpClient.getWriteTimeout(), request, list, okHttpClient.getRetryOnConnectionFailure());
            if (this.isSpdy()) {
                okHttpClient.getConnectionPool().share(this);
            }
            okHttpClient.routeDatabase().connected(this.getRoute());
        }
        this.setTimeouts(okHttpClient.getReadTimeout(), okHttpClient.getWriteTimeout());
    }

    public Handshake getHandshake() {
        return this.handshake;
    }

    long getIdleStartTimeNs() {
        if (this.spdyConnection == null) {
            return this.idleStartTimeNs;
        }
        return this.spdyConnection.getIdleStartTimeNs();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    Object getOwner() {
        ConnectionPool connectionPool;
        ConnectionPool connectionPool2 = connectionPool = this.pool;
        synchronized (connectionPool2) {
            return this.owner;
        }
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    public Route getRoute() {
        return this.route;
    }

    public Socket getSocket() {
        return this.socket;
    }

    void incrementRecycleCount() {
        this.recycleCount = 1 + this.recycleCount;
    }

    boolean isAlive() {
        return !this.socket.isClosed() && !this.socket.isInputShutdown() && !this.socket.isOutputShutdown();
    }

    boolean isConnected() {
        return this.connected;
    }

    boolean isIdle() {
        return this.spdyConnection == null || this.spdyConnection.isIdle();
    }

    boolean isReadable() {
        if (this.httpConnection != null) {
            return this.httpConnection.isReadable();
        }
        return true;
    }

    boolean isSpdy() {
        return this.spdyConnection != null;
    }

    Transport newTransport(HttpEngine httpEngine) {
        if (this.spdyConnection != null) {
            return new SpdyTransport(httpEngine, this.spdyConnection);
        }
        return new HttpTransport(httpEngine, this.httpConnection);
    }

    BufferedSink rawSink() {
        if (this.httpConnection == null) {
            throw new UnsupportedOperationException();
        }
        return this.httpConnection.rawSink();
    }

    BufferedSource rawSource() {
        if (this.httpConnection == null) {
            throw new UnsupportedOperationException();
        }
        return this.httpConnection.rawSource();
    }

    int recycleCount() {
        return this.recycleCount;
    }

    void resetIdleStartTime() {
        if (this.spdyConnection != null) {
            throw new IllegalStateException("spdyConnection != null");
        }
        this.idleStartTimeNs = System.nanoTime();
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    void setOwner(Object object) {
        ConnectionPool connectionPool;
        if (this.isSpdy()) {
            return;
        }
        ConnectionPool connectionPool2 = connectionPool = this.pool;
        synchronized (connectionPool2) {
            if (this.owner != null) {
                throw new IllegalStateException("Connection already has an owner!");
            }
            this.owner = object;
            return;
        }
    }

    void setProtocol(Protocol protocol) {
        if (protocol == null) {
            throw new IllegalArgumentException("protocol == null");
        }
        this.protocol = protocol;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    void setTimeouts(int n2, int n3) {
        if (!this.connected) {
            throw new IllegalStateException("setTimeouts - not connected");
        }
        if (this.httpConnection == null) return;
        try {
            this.socket.setSoTimeout(n2);
        }
        catch (IOException iOException) {
            throw new RouteException(iOException);
        }
        this.httpConnection.setTimeouts(n2, n3);
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    public String toString() {
        String string;
        StringBuilder stringBuilder = new StringBuilder().append("Connection{").append(this.route.address.uriHost).append(":").append(this.route.address.uriPort).append(", proxy=").append((Object)this.route.proxy).append(" hostAddress=").append(this.route.inetSocketAddress.getAddress().getHostAddress()).append(" cipherSuite=");
        if (this.handshake != null) {
            string = this.handshake.cipherSuite();
            do {
                return stringBuilder.append(string).append(" protocol=").append((Object)this.protocol).append('}').toString();
                break;
            } while (true);
        }
        string = "none";
        return stringBuilder.append(string).append(" protocol=").append((Object)this.protocol).append('}').toString();
    }
}

