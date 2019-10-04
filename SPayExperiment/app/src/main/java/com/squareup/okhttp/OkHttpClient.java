/*
 * Decompiled with CFR 0.0.
 * 
 * Could not load the following classes:
 *  java.lang.AssertionError
 *  java.lang.Cloneable
 *  java.lang.IllegalArgumentException
 *  java.lang.Object
 *  java.lang.String
 *  java.net.CookieHandler
 *  java.net.Proxy
 *  java.net.ProxySelector
 *  java.security.GeneralSecurityException
 *  java.security.SecureRandom
 *  java.util.ArrayList
 *  java.util.Collection
 *  java.util.List
 *  java.util.concurrent.TimeUnit
 *  javax.net.SocketFactory
 *  javax.net.ssl.HostnameVerifier
 *  javax.net.ssl.KeyManager
 *  javax.net.ssl.SSLContext
 *  javax.net.ssl.SSLSocket
 *  javax.net.ssl.SSLSocketFactory
 *  javax.net.ssl.TrustManager
 */
package com.squareup.okhttp;

import com.squareup.okhttp.Authenticator;
import com.squareup.okhttp.Cache;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.CertificatePinner;
import com.squareup.okhttp.Connection;
import com.squareup.okhttp.ConnectionPool;
import com.squareup.okhttp.ConnectionSpec;
import com.squareup.okhttp.Dispatcher;
import com.squareup.okhttp.Headers;
import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.Protocol;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.internal.Internal;
import com.squareup.okhttp.internal.InternalCache;
import com.squareup.okhttp.internal.Network;
import com.squareup.okhttp.internal.RouteDatabase;
import com.squareup.okhttp.internal.Util;
import com.squareup.okhttp.internal.http.AuthenticatorAdapter;
import com.squareup.okhttp.internal.http.HttpEngine;
import com.squareup.okhttp.internal.http.Transport;
import com.squareup.okhttp.internal.tls.OkHostnameVerifier;
import java.net.CookieHandler;
import java.net.Proxy;
import java.net.ProxySelector;
import java.security.GeneralSecurityException;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.KeyManager;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import okio.BufferedSink;
import okio.BufferedSource;

public class OkHttpClient
implements Cloneable {
    private static final List<ConnectionSpec> DEFAULT_CONNECTION_SPECS;
    private static final List<Protocol> DEFAULT_PROTOCOLS;
    private static SSLSocketFactory defaultSslSocketFactory;
    private Authenticator authenticator;
    private Cache cache;
    private CertificatePinner certificatePinner;
    private int connectTimeout;
    private ConnectionPool connectionPool;
    private List<ConnectionSpec> connectionSpecs;
    private CookieHandler cookieHandler;
    private Dispatcher dispatcher;
    private boolean followRedirects = true;
    private boolean followSslRedirects = true;
    private HostnameVerifier hostnameVerifier;
    private final List<Interceptor> interceptors = new ArrayList();
    private InternalCache internalCache;
    private Network network;
    private final List<Interceptor> networkInterceptors = new ArrayList();
    private List<Protocol> protocols;
    private Proxy proxy;
    private ProxySelector proxySelector;
    private int readTimeout;
    private boolean retryOnConnectionFailure = true;
    private final RouteDatabase routeDatabase;
    private SocketFactory socketFactory;
    private SSLSocketFactory sslSocketFactory;
    private int writeTimeout;

    static {
        Protocol[] arrprotocol = new Protocol[]{Protocol.HTTP_2, Protocol.SPDY_3, Protocol.HTTP_1_1};
        DEFAULT_PROTOCOLS = Util.immutableList(arrprotocol);
        ConnectionSpec[] arrconnectionSpec = new ConnectionSpec[]{ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT};
        DEFAULT_CONNECTION_SPECS = Util.immutableList(arrconnectionSpec);
        Internal.instance = new Internal(){

            @Override
            public void addLenient(Headers.Builder builder, String string) {
                builder.addLenient(string);
            }

            @Override
            public void addLenient(Headers.Builder builder, String string, String string2) {
                builder.addLenient(string, string2);
            }

            @Override
            public void apply(ConnectionSpec connectionSpec, SSLSocket sSLSocket, boolean bl) {
                connectionSpec.apply(sSLSocket, bl);
            }

            @Override
            public Connection callEngineGetConnection(Call call) {
                return call.engine.getConnection();
            }

            @Override
            public void callEngineReleaseConnection(Call call) {
                call.engine.releaseConnection();
            }

            @Override
            public void callEnqueue(Call call, Callback callback, boolean bl) {
                call.enqueue(callback, bl);
            }

            @Override
            public boolean clearOwner(Connection connection) {
                return connection.clearOwner();
            }

            @Override
            public void closeIfOwnedBy(Connection connection, Object object) {
                connection.closeIfOwnedBy(object);
            }

            @Override
            public void connectAndSetOwner(OkHttpClient okHttpClient, Connection connection, HttpEngine httpEngine, Request request) {
                connection.connectAndSetOwner(okHttpClient, httpEngine, request);
            }

            @Override
            public BufferedSink connectionRawSink(Connection connection) {
                return connection.rawSink();
            }

            @Override
            public BufferedSource connectionRawSource(Connection connection) {
                return connection.rawSource();
            }

            @Override
            public void connectionSetOwner(Connection connection, Object object) {
                connection.setOwner(object);
            }

            @Override
            public InternalCache internalCache(OkHttpClient okHttpClient) {
                return okHttpClient.internalCache();
            }

            @Override
            public boolean isReadable(Connection connection) {
                return connection.isReadable();
            }

            @Override
            public Network network(OkHttpClient okHttpClient) {
                return okHttpClient.network;
            }

            @Override
            public Transport newTransport(Connection connection, HttpEngine httpEngine) {
                return connection.newTransport(httpEngine);
            }

            @Override
            public void recycle(ConnectionPool connectionPool, Connection connection) {
                connectionPool.recycle(connection);
            }

            @Override
            public int recycleCount(Connection connection) {
                return connection.recycleCount();
            }

            @Override
            public RouteDatabase routeDatabase(OkHttpClient okHttpClient) {
                return okHttpClient.routeDatabase();
            }

            @Override
            public void setCache(OkHttpClient okHttpClient, InternalCache internalCache) {
                okHttpClient.setInternalCache(internalCache);
            }

            @Override
            public void setNetwork(OkHttpClient okHttpClient, Network network) {
                okHttpClient.network = network;
            }

            @Override
            public void setOwner(Connection connection, HttpEngine httpEngine) {
                connection.setOwner(httpEngine);
            }

            @Override
            public void setProtocol(Connection connection, Protocol protocol) {
                connection.setProtocol(protocol);
            }
        };
    }

    public OkHttpClient() {
        this.routeDatabase = new RouteDatabase();
        this.dispatcher = new Dispatcher();
    }

    /*
     * Enabled aggressive block sorting
     */
    private OkHttpClient(OkHttpClient okHttpClient) {
        this.routeDatabase = okHttpClient.routeDatabase;
        this.dispatcher = okHttpClient.dispatcher;
        this.proxy = okHttpClient.proxy;
        this.protocols = okHttpClient.protocols;
        this.connectionSpecs = okHttpClient.connectionSpecs;
        this.interceptors.addAll(okHttpClient.interceptors);
        this.networkInterceptors.addAll(okHttpClient.networkInterceptors);
        this.proxySelector = okHttpClient.proxySelector;
        this.cookieHandler = okHttpClient.cookieHandler;
        this.cache = okHttpClient.cache;
        InternalCache internalCache = this.cache != null ? this.cache.internalCache : okHttpClient.internalCache;
        this.internalCache = internalCache;
        this.socketFactory = okHttpClient.socketFactory;
        this.sslSocketFactory = okHttpClient.sslSocketFactory;
        this.hostnameVerifier = okHttpClient.hostnameVerifier;
        this.certificatePinner = okHttpClient.certificatePinner;
        this.authenticator = okHttpClient.authenticator;
        this.connectionPool = okHttpClient.connectionPool;
        this.network = okHttpClient.network;
        this.followSslRedirects = okHttpClient.followSslRedirects;
        this.followRedirects = okHttpClient.followRedirects;
        this.retryOnConnectionFailure = okHttpClient.retryOnConnectionFailure;
        this.connectTimeout = okHttpClient.connectTimeout;
        this.readTimeout = okHttpClient.readTimeout;
        this.writeTimeout = okHttpClient.writeTimeout;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     * Enabled aggressive exception aggregation
     */
    private SSLSocketFactory getDefaultSSLSocketFactory() {
        OkHttpClient okHttpClient = this;
        synchronized (okHttpClient) {
            SSLSocketFactory sSLSocketFactory = defaultSslSocketFactory;
            if (sSLSocketFactory != null) return defaultSslSocketFactory;
            try {
                SSLContext sSLContext = SSLContext.getInstance((String)"TLS");
                sSLContext.init(null, null, null);
                defaultSslSocketFactory = sSLContext.getSocketFactory();
            }
            catch (GeneralSecurityException generalSecurityException) {
                throw new AssertionError();
            }
            return defaultSslSocketFactory;
        }
    }

    public OkHttpClient cancel(Object object) {
        this.getDispatcher().cancel(object);
        return this;
    }

    public OkHttpClient clone() {
        return new OkHttpClient(this);
    }

    OkHttpClient copyWithDefaults() {
        OkHttpClient okHttpClient = new OkHttpClient(this);
        if (okHttpClient.proxySelector == null) {
            okHttpClient.proxySelector = ProxySelector.getDefault();
        }
        if (okHttpClient.cookieHandler == null) {
            okHttpClient.cookieHandler = CookieHandler.getDefault();
        }
        if (okHttpClient.socketFactory == null) {
            okHttpClient.socketFactory = SocketFactory.getDefault();
        }
        if (okHttpClient.sslSocketFactory == null) {
            okHttpClient.sslSocketFactory = this.getDefaultSSLSocketFactory();
        }
        if (okHttpClient.hostnameVerifier == null) {
            okHttpClient.hostnameVerifier = OkHostnameVerifier.INSTANCE;
        }
        if (okHttpClient.certificatePinner == null) {
            okHttpClient.certificatePinner = CertificatePinner.DEFAULT;
        }
        if (okHttpClient.authenticator == null) {
            okHttpClient.authenticator = AuthenticatorAdapter.INSTANCE;
        }
        if (okHttpClient.connectionPool == null) {
            okHttpClient.connectionPool = ConnectionPool.getDefault();
        }
        if (okHttpClient.protocols == null) {
            okHttpClient.protocols = DEFAULT_PROTOCOLS;
        }
        if (okHttpClient.connectionSpecs == null) {
            okHttpClient.connectionSpecs = DEFAULT_CONNECTION_SPECS;
        }
        if (okHttpClient.network == null) {
            okHttpClient.network = Network.DEFAULT;
        }
        return okHttpClient;
    }

    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    public Cache getCache() {
        return this.cache;
    }

    public CertificatePinner getCertificatePinner() {
        return this.certificatePinner;
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    public List<ConnectionSpec> getConnectionSpecs() {
        return this.connectionSpecs;
    }

    public CookieHandler getCookieHandler() {
        return this.cookieHandler;
    }

    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    public boolean getFollowRedirects() {
        return this.followRedirects;
    }

    public boolean getFollowSslRedirects() {
        return this.followSslRedirects;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public List<Protocol> getProtocols() {
        return this.protocols;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public ProxySelector getProxySelector() {
        return this.proxySelector;
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public boolean getRetryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    public List<Interceptor> interceptors() {
        return this.interceptors;
    }

    InternalCache internalCache() {
        return this.internalCache;
    }

    public List<Interceptor> networkInterceptors() {
        return this.networkInterceptors;
    }

    public Call newCall(Request request) {
        return new Call(this, request);
    }

    RouteDatabase routeDatabase() {
        return this.routeDatabase;
    }

    public OkHttpClient setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public OkHttpClient setCache(Cache cache) {
        this.cache = cache;
        this.internalCache = null;
        return this;
    }

    public OkHttpClient setCertificatePinner(CertificatePinner certificatePinner) {
        this.certificatePinner = certificatePinner;
        return this;
    }

    public void setConnectTimeout(long l2, TimeUnit timeUnit) {
        if (l2 < 0L) {
            throw new IllegalArgumentException("timeout < 0");
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        }
        long l3 = timeUnit.toMillis(l2);
        if (l3 > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Timeout too large.");
        }
        if (l3 == 0L && l2 > 0L) {
            throw new IllegalArgumentException("Timeout too small.");
        }
        this.connectTimeout = (int)l3;
    }

    public OkHttpClient setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        return this;
    }

    public OkHttpClient setConnectionSpecs(List<ConnectionSpec> list) {
        this.connectionSpecs = Util.immutableList(list);
        return this;
    }

    public OkHttpClient setCookieHandler(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
        return this;
    }

    public OkHttpClient setDispatcher(Dispatcher dispatcher) {
        if (dispatcher == null) {
            throw new IllegalArgumentException("dispatcher == null");
        }
        this.dispatcher = dispatcher;
        return this;
    }

    public void setFollowRedirects(boolean bl) {
        this.followRedirects = bl;
    }

    public OkHttpClient setFollowSslRedirects(boolean bl) {
        this.followSslRedirects = bl;
        return this;
    }

    public OkHttpClient setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    void setInternalCache(InternalCache internalCache) {
        this.internalCache = internalCache;
        this.cache = null;
    }

    public OkHttpClient setProtocols(List<Protocol> list) {
        List<Protocol> list2 = Util.immutableList(list);
        if (!list2.contains((Object)Protocol.HTTP_1_1)) {
            throw new IllegalArgumentException("protocols doesn't contain http/1.1: " + list2);
        }
        if (list2.contains((Object)Protocol.HTTP_1_0)) {
            throw new IllegalArgumentException("protocols must not contain http/1.0: " + list2);
        }
        if (list2.contains(null)) {
            throw new IllegalArgumentException("protocols must not contain null");
        }
        this.protocols = Util.immutableList(list2);
        return this;
    }

    public OkHttpClient setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public OkHttpClient setProxySelector(ProxySelector proxySelector) {
        this.proxySelector = proxySelector;
        return this;
    }

    public void setReadTimeout(long l2, TimeUnit timeUnit) {
        if (l2 < 0L) {
            throw new IllegalArgumentException("timeout < 0");
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        }
        long l3 = timeUnit.toMillis(l2);
        if (l3 > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Timeout too large.");
        }
        if (l3 == 0L && l2 > 0L) {
            throw new IllegalArgumentException("Timeout too small.");
        }
        this.readTimeout = (int)l3;
    }

    public void setRetryOnConnectionFailure(boolean bl) {
        this.retryOnConnectionFailure = bl;
    }

    public OkHttpClient setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
        return this;
    }

    public OkHttpClient setSslSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.sslSocketFactory = sSLSocketFactory;
        return this;
    }

    public void setWriteTimeout(long l2, TimeUnit timeUnit) {
        if (l2 < 0L) {
            throw new IllegalArgumentException("timeout < 0");
        }
        if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        }
        long l3 = timeUnit.toMillis(l2);
        if (l3 > Integer.MAX_VALUE) {
            throw new IllegalArgumentException("Timeout too large.");
        }
        if (l3 == 0L && l2 > 0L) {
            throw new IllegalArgumentException("Timeout too small.");
        }
        this.writeTimeout = (int)l3;
    }

}

