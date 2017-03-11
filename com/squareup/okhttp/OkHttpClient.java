package com.squareup.okhttp;

import com.squareup.okhttp.Headers.Builder;
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
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.net.SocketFactory;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import okio.BufferedSink;
import okio.BufferedSource;

public class OkHttpClient implements Cloneable {
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
    private boolean followRedirects;
    private boolean followSslRedirects;
    private HostnameVerifier hostnameVerifier;
    private final List<Interceptor> interceptors;
    private InternalCache internalCache;
    private Network network;
    private final List<Interceptor> networkInterceptors;
    private List<Protocol> protocols;
    private Proxy proxy;
    private ProxySelector proxySelector;
    private int readTimeout;
    private boolean retryOnConnectionFailure;
    private final RouteDatabase routeDatabase;
    private SocketFactory socketFactory;
    private SSLSocketFactory sslSocketFactory;
    private int writeTimeout;

    /* renamed from: com.squareup.okhttp.OkHttpClient.1 */
    static class C06321 extends Internal {
        C06321() {
        }

        public Transport newTransport(Connection connection, HttpEngine httpEngine) {
            return connection.newTransport(httpEngine);
        }

        public boolean clearOwner(Connection connection) {
            return connection.clearOwner();
        }

        public void closeIfOwnedBy(Connection connection, Object obj) {
            connection.closeIfOwnedBy(obj);
        }

        public int recycleCount(Connection connection) {
            return connection.recycleCount();
        }

        public void setProtocol(Connection connection, Protocol protocol) {
            connection.setProtocol(protocol);
        }

        public void setOwner(Connection connection, HttpEngine httpEngine) {
            connection.setOwner(httpEngine);
        }

        public boolean isReadable(Connection connection) {
            return connection.isReadable();
        }

        public void addLenient(Builder builder, String str) {
            builder.addLenient(str);
        }

        public void addLenient(Builder builder, String str, String str2) {
            builder.addLenient(str, str2);
        }

        public void setCache(OkHttpClient okHttpClient, InternalCache internalCache) {
            okHttpClient.setInternalCache(internalCache);
        }

        public InternalCache internalCache(OkHttpClient okHttpClient) {
            return okHttpClient.internalCache();
        }

        public void recycle(ConnectionPool connectionPool, Connection connection) {
            connectionPool.recycle(connection);
        }

        public RouteDatabase routeDatabase(OkHttpClient okHttpClient) {
            return okHttpClient.routeDatabase();
        }

        public Network network(OkHttpClient okHttpClient) {
            return okHttpClient.network;
        }

        public void setNetwork(OkHttpClient okHttpClient, Network network) {
            okHttpClient.network = network;
        }

        public void connectAndSetOwner(OkHttpClient okHttpClient, Connection connection, HttpEngine httpEngine, Request request) {
            connection.connectAndSetOwner(okHttpClient, httpEngine, request);
        }

        public void callEnqueue(Call call, Callback callback, boolean z) {
            call.enqueue(callback, z);
        }

        public void callEngineReleaseConnection(Call call) {
            call.engine.releaseConnection();
        }

        public Connection callEngineGetConnection(Call call) {
            return call.engine.getConnection();
        }

        public BufferedSource connectionRawSource(Connection connection) {
            return connection.rawSource();
        }

        public BufferedSink connectionRawSink(Connection connection) {
            return connection.rawSink();
        }

        public void connectionSetOwner(Connection connection, Object obj) {
            connection.setOwner(obj);
        }

        public void apply(ConnectionSpec connectionSpec, SSLSocket sSLSocket, boolean z) {
            connectionSpec.apply(sSLSocket, z);
        }
    }

    static {
        DEFAULT_PROTOCOLS = Util.immutableList(Protocol.HTTP_2, Protocol.SPDY_3, Protocol.HTTP_1_1);
        DEFAULT_CONNECTION_SPECS = Util.immutableList(ConnectionSpec.MODERN_TLS, ConnectionSpec.COMPATIBLE_TLS, ConnectionSpec.CLEARTEXT);
        Internal.instance = new C06321();
    }

    public OkHttpClient() {
        this.interceptors = new ArrayList();
        this.networkInterceptors = new ArrayList();
        this.followSslRedirects = true;
        this.followRedirects = true;
        this.retryOnConnectionFailure = true;
        this.routeDatabase = new RouteDatabase();
        this.dispatcher = new Dispatcher();
    }

    private OkHttpClient(OkHttpClient okHttpClient) {
        this.interceptors = new ArrayList();
        this.networkInterceptors = new ArrayList();
        this.followSslRedirects = true;
        this.followRedirects = true;
        this.retryOnConnectionFailure = true;
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
        this.internalCache = this.cache != null ? this.cache.internalCache : okHttpClient.internalCache;
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

    public void setConnectTimeout(long j, TimeUnit timeUnit) {
        if (j < 0) {
            throw new IllegalArgumentException("timeout < 0");
        } else if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        } else {
            long toMillis = timeUnit.toMillis(j);
            if (toMillis > 2147483647L) {
                throw new IllegalArgumentException("Timeout too large.");
            } else if (toMillis != 0 || j <= 0) {
                this.connectTimeout = (int) toMillis;
            } else {
                throw new IllegalArgumentException("Timeout too small.");
            }
        }
    }

    public int getConnectTimeout() {
        return this.connectTimeout;
    }

    public void setReadTimeout(long j, TimeUnit timeUnit) {
        if (j < 0) {
            throw new IllegalArgumentException("timeout < 0");
        } else if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        } else {
            long toMillis = timeUnit.toMillis(j);
            if (toMillis > 2147483647L) {
                throw new IllegalArgumentException("Timeout too large.");
            } else if (toMillis != 0 || j <= 0) {
                this.readTimeout = (int) toMillis;
            } else {
                throw new IllegalArgumentException("Timeout too small.");
            }
        }
    }

    public int getReadTimeout() {
        return this.readTimeout;
    }

    public void setWriteTimeout(long j, TimeUnit timeUnit) {
        if (j < 0) {
            throw new IllegalArgumentException("timeout < 0");
        } else if (timeUnit == null) {
            throw new IllegalArgumentException("unit == null");
        } else {
            long toMillis = timeUnit.toMillis(j);
            if (toMillis > 2147483647L) {
                throw new IllegalArgumentException("Timeout too large.");
            } else if (toMillis != 0 || j <= 0) {
                this.writeTimeout = (int) toMillis;
            } else {
                throw new IllegalArgumentException("Timeout too small.");
            }
        }
    }

    public int getWriteTimeout() {
        return this.writeTimeout;
    }

    public OkHttpClient setProxy(Proxy proxy) {
        this.proxy = proxy;
        return this;
    }

    public Proxy getProxy() {
        return this.proxy;
    }

    public OkHttpClient setProxySelector(ProxySelector proxySelector) {
        this.proxySelector = proxySelector;
        return this;
    }

    public ProxySelector getProxySelector() {
        return this.proxySelector;
    }

    public OkHttpClient setCookieHandler(CookieHandler cookieHandler) {
        this.cookieHandler = cookieHandler;
        return this;
    }

    public CookieHandler getCookieHandler() {
        return this.cookieHandler;
    }

    void setInternalCache(InternalCache internalCache) {
        this.internalCache = internalCache;
        this.cache = null;
    }

    InternalCache internalCache() {
        return this.internalCache;
    }

    public OkHttpClient setCache(Cache cache) {
        this.cache = cache;
        this.internalCache = null;
        return this;
    }

    public Cache getCache() {
        return this.cache;
    }

    public OkHttpClient setSocketFactory(SocketFactory socketFactory) {
        this.socketFactory = socketFactory;
        return this;
    }

    public SocketFactory getSocketFactory() {
        return this.socketFactory;
    }

    public OkHttpClient setSslSocketFactory(SSLSocketFactory sSLSocketFactory) {
        this.sslSocketFactory = sSLSocketFactory;
        return this;
    }

    public SSLSocketFactory getSslSocketFactory() {
        return this.sslSocketFactory;
    }

    public OkHttpClient setHostnameVerifier(HostnameVerifier hostnameVerifier) {
        this.hostnameVerifier = hostnameVerifier;
        return this;
    }

    public HostnameVerifier getHostnameVerifier() {
        return this.hostnameVerifier;
    }

    public OkHttpClient setCertificatePinner(CertificatePinner certificatePinner) {
        this.certificatePinner = certificatePinner;
        return this;
    }

    public CertificatePinner getCertificatePinner() {
        return this.certificatePinner;
    }

    public OkHttpClient setAuthenticator(Authenticator authenticator) {
        this.authenticator = authenticator;
        return this;
    }

    public Authenticator getAuthenticator() {
        return this.authenticator;
    }

    public OkHttpClient setConnectionPool(ConnectionPool connectionPool) {
        this.connectionPool = connectionPool;
        return this;
    }

    public ConnectionPool getConnectionPool() {
        return this.connectionPool;
    }

    public OkHttpClient setFollowSslRedirects(boolean z) {
        this.followSslRedirects = z;
        return this;
    }

    public boolean getFollowSslRedirects() {
        return this.followSslRedirects;
    }

    public void setFollowRedirects(boolean z) {
        this.followRedirects = z;
    }

    public boolean getFollowRedirects() {
        return this.followRedirects;
    }

    public void setRetryOnConnectionFailure(boolean z) {
        this.retryOnConnectionFailure = z;
    }

    public boolean getRetryOnConnectionFailure() {
        return this.retryOnConnectionFailure;
    }

    RouteDatabase routeDatabase() {
        return this.routeDatabase;
    }

    public OkHttpClient setDispatcher(Dispatcher dispatcher) {
        if (dispatcher == null) {
            throw new IllegalArgumentException("dispatcher == null");
        }
        this.dispatcher = dispatcher;
        return this;
    }

    public Dispatcher getDispatcher() {
        return this.dispatcher;
    }

    public OkHttpClient setProtocols(List<Protocol> list) {
        List immutableList = Util.immutableList((List) list);
        if (!immutableList.contains(Protocol.HTTP_1_1)) {
            throw new IllegalArgumentException("protocols doesn't contain http/1.1: " + immutableList);
        } else if (immutableList.contains(Protocol.HTTP_1_0)) {
            throw new IllegalArgumentException("protocols must not contain http/1.0: " + immutableList);
        } else if (immutableList.contains(null)) {
            throw new IllegalArgumentException("protocols must not contain null");
        } else {
            this.protocols = Util.immutableList(immutableList);
            return this;
        }
    }

    public List<Protocol> getProtocols() {
        return this.protocols;
    }

    public OkHttpClient setConnectionSpecs(List<ConnectionSpec> list) {
        this.connectionSpecs = Util.immutableList((List) list);
        return this;
    }

    public List<ConnectionSpec> getConnectionSpecs() {
        return this.connectionSpecs;
    }

    public List<Interceptor> interceptors() {
        return this.interceptors;
    }

    public List<Interceptor> networkInterceptors() {
        return this.networkInterceptors;
    }

    public Call newCall(Request request) {
        return new Call(this, request);
    }

    public OkHttpClient cancel(Object obj) {
        getDispatcher().cancel(obj);
        return this;
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
            okHttpClient.sslSocketFactory = getDefaultSSLSocketFactory();
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

    private synchronized SSLSocketFactory getDefaultSSLSocketFactory() {
        if (defaultSslSocketFactory == null) {
            try {
                SSLContext instance = SSLContext.getInstance("TLS");
                instance.init(null, null, null);
                defaultSslSocketFactory = instance.getSocketFactory();
            } catch (GeneralSecurityException e) {
                throw new AssertionError();
            }
        }
        return defaultSslSocketFactory;
    }

    public OkHttpClient clone() {
        return new OkHttpClient(this);
    }
}
