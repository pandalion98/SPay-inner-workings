package com.squareup.okhttp;

import com.squareup.okhttp.internal.http.HttpConnection;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.HttpTransport;
import com.squareup.okhttp.internal.http.RouteException;
import com.squareup.okhttp.internal.http.SocketConnector;
import com.squareup.okhttp.internal.http.SocketConnector.ConnectedSocket;
import com.squareup.okhttp.internal.http.SpdyTransport;
import com.squareup.okhttp.internal.http.Transport;
import com.squareup.okhttp.internal.spdy.SpdyConnection;
import com.squareup.okhttp.internal.spdy.SpdyConnection.Builder;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownServiceException;
import java.util.List;
import okio.BufferedSink;
import okio.BufferedSource;

public final class Connection {
    private boolean connected;
    private Handshake handshake;
    private HttpConnection httpConnection;
    private long idleStartTimeNs;
    private Object owner;
    private final ConnectionPool pool;
    private Protocol protocol;
    private int recycleCount;
    private final Route route;
    private Socket socket;
    private SpdyConnection spdyConnection;

    public Connection(ConnectionPool connectionPool, Route route) {
        this.connected = false;
        this.protocol = Protocol.HTTP_1_1;
        this.pool = connectionPool;
        this.route = route;
    }

    Object getOwner() {
        Object obj;
        synchronized (this.pool) {
            obj = this.owner;
        }
        return obj;
    }

    void setOwner(Object obj) {
        if (!isSpdy()) {
            synchronized (this.pool) {
                if (this.owner != null) {
                    throw new IllegalStateException("Connection already has an owner!");
                }
                this.owner = obj;
            }
        }
    }

    boolean clearOwner() {
        boolean z;
        synchronized (this.pool) {
            if (this.owner == null) {
                z = false;
            } else {
                this.owner = null;
                z = true;
            }
        }
        return z;
    }

    void closeIfOwnedBy(Object obj) {
        if (isSpdy()) {
            throw new IllegalStateException();
        }
        synchronized (this.pool) {
            if (this.owner != obj) {
                return;
            }
            this.owner = null;
            this.socket.close();
        }
    }

    void connect(int i, int i2, int i3, Request request, List<ConnectionSpec> list, boolean z) {
        if (this.connected) {
            throw new IllegalStateException("already connected");
        }
        ConnectedSocket connectTls;
        SocketConnector socketConnector = new SocketConnector(this, this.pool);
        if (this.route.address.getSslSocketFactory() != null) {
            connectTls = socketConnector.connectTls(i, i2, i3, request, this.route, list, z);
        } else if (list.contains(ConnectionSpec.CLEARTEXT)) {
            connectTls = socketConnector.connectCleartext(i, i2, this.route);
        } else {
            throw new RouteException(new UnknownServiceException("CLEARTEXT communication not supported: " + list));
        }
        this.socket = connectTls.socket;
        this.handshake = connectTls.handshake;
        this.protocol = connectTls.alpnProtocol == null ? Protocol.HTTP_1_1 : connectTls.alpnProtocol;
        try {
            if (this.protocol == Protocol.SPDY_3 || this.protocol == Protocol.HTTP_2) {
                this.socket.setSoTimeout(0);
                this.spdyConnection = new Builder(this.route.address.uriHost, true, this.socket).protocol(this.protocol).build();
                this.spdyConnection.sendConnectionPreface();
            } else {
                this.httpConnection = new HttpConnection(this.pool, this, this.socket);
            }
            this.connected = true;
        } catch (IOException e) {
            throw new RouteException(e);
        }
    }

    void connectAndSetOwner(OkHttpClient okHttpClient, Object obj, Request request) {
        setOwner(obj);
        if (!isConnected()) {
            Request request2 = request;
            connect(okHttpClient.getConnectTimeout(), okHttpClient.getReadTimeout(), okHttpClient.getWriteTimeout(), request2, this.route.address.getConnectionSpecs(), okHttpClient.getRetryOnConnectionFailure());
            if (isSpdy()) {
                okHttpClient.getConnectionPool().share(this);
            }
            okHttpClient.routeDatabase().connected(getRoute());
        }
        setTimeouts(okHttpClient.getReadTimeout(), okHttpClient.getWriteTimeout());
    }

    boolean isConnected() {
        return this.connected;
    }

    public Route getRoute() {
        return this.route;
    }

    public Socket getSocket() {
        return this.socket;
    }

    BufferedSource rawSource() {
        if (this.httpConnection != null) {
            return this.httpConnection.rawSource();
        }
        throw new UnsupportedOperationException();
    }

    BufferedSink rawSink() {
        if (this.httpConnection != null) {
            return this.httpConnection.rawSink();
        }
        throw new UnsupportedOperationException();
    }

    boolean isAlive() {
        return (this.socket.isClosed() || this.socket.isInputShutdown() || this.socket.isOutputShutdown()) ? false : true;
    }

    boolean isReadable() {
        if (this.httpConnection != null) {
            return this.httpConnection.isReadable();
        }
        return true;
    }

    void resetIdleStartTime() {
        if (this.spdyConnection != null) {
            throw new IllegalStateException("spdyConnection != null");
        }
        this.idleStartTimeNs = System.nanoTime();
    }

    boolean isIdle() {
        return this.spdyConnection == null || this.spdyConnection.isIdle();
    }

    long getIdleStartTimeNs() {
        return this.spdyConnection == null ? this.idleStartTimeNs : this.spdyConnection.getIdleStartTimeNs();
    }

    public Handshake getHandshake() {
        return this.handshake;
    }

    Transport newTransport(HttpEngine httpEngine) {
        return this.spdyConnection != null ? new SpdyTransport(httpEngine, this.spdyConnection) : new HttpTransport(httpEngine, this.httpConnection);
    }

    boolean isSpdy() {
        return this.spdyConnection != null;
    }

    public Protocol getProtocol() {
        return this.protocol;
    }

    void setProtocol(Protocol protocol) {
        if (protocol == null) {
            throw new IllegalArgumentException("protocol == null");
        }
        this.protocol = protocol;
    }

    void setTimeouts(int i, int i2) {
        if (!this.connected) {
            throw new IllegalStateException("setTimeouts - not connected");
        } else if (this.httpConnection != null) {
            try {
                this.socket.setSoTimeout(i);
                this.httpConnection.setTimeouts(i, i2);
            } catch (IOException e) {
                throw new RouteException(e);
            }
        }
    }

    void incrementRecycleCount() {
        this.recycleCount++;
    }

    int recycleCount() {
        return this.recycleCount;
    }

    public String toString() {
        return "Connection{" + this.route.address.uriHost + ":" + this.route.address.uriPort + ", proxy=" + this.route.proxy + " hostAddress=" + this.route.inetSocketAddress.getAddress().getHostAddress() + " cipherSuite=" + (this.handshake != null ? this.handshake.cipherSuite() : "none") + " protocol=" + this.protocol + '}';
    }
}
